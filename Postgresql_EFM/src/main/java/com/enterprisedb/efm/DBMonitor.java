package com.enterprisedb.efm;

import com.enterprisedb.efm.utils.I18N;
import com.enterprisedb.efm.utils.LogManager;
import com.enterprisedb.efm.utils.Notifications;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author: wk
 * @Date: 2020/11/6 16:40
 * @Description
 */
public class DBMonitor {
    private static final String QUERY = "select version();";

    private static final String IN_RECOVERY_QUERY = "select pg_is_in_recovery();";

    private static final String MASTER_XLOG_LOCATION = "select pg_current_xlog_location();";

    private static final String STANDBY_XLOG_LOCATION = "select pg_last_xlog_replay_location();";

    private static final String PAUSE_RECOVERY = "select pg_xlog_replay_pause();";

    private static final String PAUSE_CHECK = "select pg_is_xlog_replay_paused();";

    private static final String RESUME_REPLAY = "select pg_xlog_replay_resume();";

    private static final HashMap<String, String> DB_UP_SQL_STATE_CODE_MAP = new HashMap<String, String>();

    private final String address;

    private final String connectionUrl;

    static {
        DB_UP_SQL_STATE_CODE_MAP.put("53300", I18N.getString("DB_UP_BUT_ERROR_BODY_53300"));
        DB_UP_SQL_STATE_CODE_MAP.put("28P01", I18N.getString("DB_UP_BUT_ERROR_BODY_28P01", new Object[] { Environment.getEnvironment().getDBUser() }));
        DB_UP_SQL_STATE_CODE_MAP.put("28000", I18N.getString("DB_UP_BUT_ERROR_BODY_28000"));
    }

    private final Properties jdbcProps = new Properties();

    private static final Logger LOGGER = LogManager.getEfmLogger();

    private ThreadPoolExecutor threadPoolExecutor;

    private ScheduledExecutorService sharedConnExecutor;

    private ScheduledExecutorService pingTimer;

    private volatile int connectionUseCounter = 0;

    private final int maxConnectionUses;

    private final boolean reuseConnection;

    private final long timeoutMillis;

    private volatile boolean keepWatching;

    private volatile Thread watchingThread = null;

    private volatile Connection connection;

    private volatile PreparedStatement statement;

    private final Object connectionLock = new Object();

    private volatile long timestamp;

    public DBMonitor(String host, int port, String user, String password, String dbName) {
        this.address = host;
        this.connectionUrl = createConnectionString(host, port, dbName);
        Environment env = Environment.getEnvironment();
        if (env.isWitness()) {
            this.timeoutMillis = 0L;
            this.maxConnectionUses = 0;
            this.reuseConnection = false;
        } else {
            this.timeoutMillis = 1000L * env.getLocalDbTimeout();
            this.maxConnectionUses = env.getReuseDbConnectionCount();
            this.reuseConnection = (this.maxConnectionUses != 0);
        }
        this.jdbcProps.setProperty("user", user);
        this.jdbcProps.setProperty("password", password);
        if (env.getJdbcSsl()) {
            this.jdbcProps.setProperty("ssl", "true");
            if (env.getJdbcSslMode().equals("require"))
                this.jdbcProps.setProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        }
    }

    public void watchDB(Notifications notification) {
        LOGGER.log(Level.INFO, "DBMonitor connecting to {0}", this.connectionUrl);
        if (Environment.getEnvironment().getJdbcSsl())
            LOGGER.log(Level.INFO, "Using ssl for jdbc connections.");
        initConnectionAndStatement();
        this.watchingThread = Thread.currentThread();
        final Environment env = Environment.getEnvironment();
        notification.addSubjectParams(new String[] { env.getBindingAddress(), env.getClusterName() }).send();
        if (this.reuseConnection) {
            this.sharedConnExecutor = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable runnable) {
                    Thread retVal = new Thread(runnable, "DB_shared_conn_checker");
                    retVal.setDaemon(true);
                    return retVal;
                }
            });
            this.sharedConnExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    try {
                        if (DBMonitor.this.connection == null)
                            DBMonitor.LOGGER.warning("Database connection is null.");
                        if (DBMonitor.this.connectionUseCounter >= DBMonitor.this.maxConnectionUses || DBMonitor.this.connection == null || DBMonitor.this.connection.isClosed()) {
                            DBMonitor.LOGGER.log(Level.FINER, "DBMonitor connecting to {0}", DBMonitor.this.connectionUrl);
                            DBMonitor.this.initConnectionAndStatement();
                            DBMonitor.this.connectionUseCounter = 0;
                            synchronized (DBMonitor.this.connectionLock) {
                                if (DBMonitor.this.connection != null && !DBMonitor.this.connection.isClosed())
                                    DBMonitor.this.timestamp = System.currentTimeMillis();
                            }
                        }
                        if (DBMonitor.this.checkDB(DBMonitor.this.statement)) {
                            DBMonitor.LOGGER.finest("Updating timestamp.");
                            DBMonitor.this.timestamp = System.currentTimeMillis();
                        } else {
                            DBMonitor.LOGGER.finest("** No timestamp update **");
                        }
                        DBMonitor.this.connectionUseCounter++;
                    } catch (Throwable throwable) {
                        Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { env.getClusterName() }).addBodyParams(new String[] { throwable.toString() }).send();
                        //Notifications.UNEXPECTED_ERROR.addSubjectParams(new String[] { this.val$env.getClusterName() }).addBodyParams(new String[] { throwable.toString() }).send();
                        DBMonitor.LOGGER.severe(throwable.toString());
                    }
                }
            },0L, env.getLocalDbPeriod(), TimeUnit.SECONDS);
        } else {
            synchronized (this.connectionLock) {
                closeStatment(this.statement);
                closeConnection(this.connection);
            }
            this.threadPoolExecutor = createThreadPoolExecutor();
            this.pingTimer = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, "ping_timer");
                }
            });
            this.pingTimer.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    DBMonitor.this.threadPoolExecutor.execute(new Runnable() {
                        public void run() {
                            if (DBMonitor.this.checkOnce(false, false)) {
                                DBMonitor.LOGGER.finest("Updating timestamp.");
                                DBMonitor.this.timestamp = System.currentTimeMillis();
                            } else {
                                DBMonitor.LOGGER.finest("** No timestamp update **");
                            }
                        }
                    });
                }
            },0L, env.getLocalDbPeriod(), TimeUnit.SECONDS);
        }
        this.timestamp = System.currentTimeMillis();
        this.keepWatching = true;
        while (this.keepWatching) {
            long currentTimeMs = System.currentTimeMillis();
            if (this.timestamp + this.timeoutMillis < currentTimeMs) {
                LOGGER.log(Level.WARNING, "Database monitor has timed out. The last successful ping was {0} ms ago.", Long.valueOf(currentTimeMs - this.timestamp));
                if (stillCannotReach()) {
                    shutdown();
                } else {
                    this.timestamp = System.currentTimeMillis();
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(800L);
            } catch (InterruptedException e) {
                LOGGER.info("*DB monitor interrupted*");
            }
        }
    }

    public void shutdown() {
        LOGGER.info("Shutdown called on DB monitor.");
        LOGGER.log(Level.FINE, "Outputting call stack. (This is not an error.)", new Exception("CALL STACK"));
        this.keepWatching = false;
        if (this.watchingThread != null)
            this.watchingThread.interrupt();
        if (this.pingTimer != null)
            this.pingTimer.shutdownNow();
        if (this.threadPoolExecutor != null) {
            int numTasks = this.threadPoolExecutor.shutdownNow().size();
            if (numTasks > 0 && LOGGER.isLoggable(Level.FINER))
                LOGGER.log(Level.FINER, "cancelled {0} tasks in executor", Integer.valueOf(numTasks));
        }
        if (this.sharedConnExecutor != null) {
            int numTasks = this.sharedConnExecutor.shutdownNow().size();
            if (numTasks > 0 && LOGGER.isLoggable(Level.FINER))
                LOGGER.log(Level.FINER, "cancelled {0} tasks in scheduler", Integer.valueOf(numTasks));
        }
        synchronized (this.connectionLock) {
            closeConnection(this.connection);
        }
    }

    public boolean checkOnce(final boolean failForAnyError, long timeoutSec) {
        FutureTask<Boolean> task = new FutureTask<Boolean>(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(DBMonitor.this.checkOnce(failForAnyError, false));
            }
        });
        Thread taskThread = new Thread(task, "timed_db_check");
        taskThread.setDaemon(true);
        taskThread.start();
        try {
            return ((Boolean)task.get(timeoutSec, TimeUnit.SECONDS)).booleanValue();
        } catch (TimeoutException e) {
            LOGGER.log(Level.WARNING, "Database check timed out after {0} seconds. Thread stack to follow", Long.valueOf(timeoutSec));
            StringBuilder sb = new StringBuilder();
            sb.append(taskThread.getName()).append(" in state ").append(taskThread.getState());
            for (StackTraceElement ste : taskThread.getStackTrace()) {
                sb.append("\n    at ");
                sb.append(ste);
            }
            LOGGER.warning(sb.toString());
            task.cancel(true);
            return false;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Waiting up to {0} seconds for database check and received exception {1}", new Object[] { Long.valueOf(timeoutSec), e.toString() });
            return false;
        }
    }

    public boolean checkOnce(boolean failForAnyError, boolean logUrl) {
        if (logUrl) {
            LOGGER.info("DB monitor testing " + this.connectionUrl);
        } else if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "DBMonitor testing {0}", this.connectionUrl);
        }
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        long timeA = System.currentTimeMillis();
        try {
            conn = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, "Acquired connection after {0} ms", Long.valueOf(System.currentTimeMillis() - timeA));
            long timeB = System.currentTimeMillis();
            preparedStatement = conn.prepareStatement("select version();");
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, "Prepared statement after {0} ms", Long.valueOf(System.currentTimeMillis() - timeB));
            return checkDB(preparedStatement);
        } catch (SQLException sqle) {
            String sqlState = sqle.getSQLState();
            if (failForAnyError || !DB_UP_SQL_STATE_CODE_MAP.containsKey(sqlState)) {
                LOGGER.log(Level.WARNING, "Could not connect to database at {0}: {1}, state: {2}, time: {3}", new Object[] { this.address, sqle, sqlState, Long.valueOf(System.currentTimeMillis() - timeA) });
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Connection string: {0}", this.connectionUrl);
                return false;
            }
            String sqlStateMessage = DB_UP_SQL_STATE_CODE_MAP.get(sqlState);
            LOGGER.log(Level.WARNING, "Could not connect to database at {0} due to: {1}, {2}. time: {3}", new Object[] { this.address, sqlState, sqlStateMessage, Long.valueOf(System.currentTimeMillis() - timeA) });
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Connection string: {0}", this.connectionUrl);
            Notifications.DB_UP_BUT_ERROR.addSubjectParams(new String[] { this.address }).addBodyParams(new String[] { sqlStateMessage }).send();
            return true;
        } finally {
            closeStatment(preparedStatement);
            closeConnection(conn);
        }
    }

    public boolean isInRecovery() throws SQLException {
        return isInRecovery(Level.INFO);
    }

    public boolean isInRecovery(Level level) throws SQLException {
        LOGGER.log(level, "Performing pg_is_in_recovery query on {0}", this.connectionUrl);
        Connection conn = null;
        Statement query = null;
        ResultSet result = null;
        try {
            conn = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
            query = conn.createStatement();
            result = query.executeQuery("select pg_is_in_recovery();");
            if (result == null || !result.next()) {
                System.err.println("Received no result for pg_is_in_recovery query.");
                return false;
            }
            boolean retVal = result.getBoolean(1);
            LOGGER.log(level, "Query result: {0}", Boolean.valueOf(retVal));
            return retVal;
        } finally {
            if (result != null)
                try {
                    result.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Problem closing resultSet: {0}", e.toString());
                }
            closeStatment(query);
            closeConnection(conn);
        }
    }

    public boolean pauseRecovery() throws SQLException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Pausing recovery on {0}", this.address);
        Connection conn = null;
        ResultSet resultSet = null;
        PreparedStatement pauseStatement = null;
        PreparedStatement isPausedCheck = null;
        try {
            conn = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
            pauseStatement = conn.prepareStatement("select pg_xlog_replay_pause();");
            pauseStatement.execute();
            isPausedCheck = conn.prepareStatement("select pg_is_xlog_replay_paused();");
            resultSet = isPausedCheck.executeQuery();
            if (resultSet == null || !resultSet.next()) {
                LOGGER.log(Level.SEVERE, "Did not receive result from {0} query.", "select pg_is_xlog_replay_paused();");
                return false;
            }
            boolean result = resultSet.getBoolean(1);
            if (LOGGER.isLoggable(Level.FINER))
                LOGGER.log(Level.FINER, "Result from {0} {1}", new Object[] { "select pg_is_xlog_replay_paused();", Boolean.valueOf(result) });
            return result;
        } finally {
            closeStatment(pauseStatement);
            closeStatment(isPausedCheck);
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Problem closing resultSet: {0}", e.toString());
                }
            closeConnection(conn);
        }
    }

    public void resumeReplay() throws SQLException {
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.log(Level.FINE, "Resuming replay on {0}", this.address);
        Connection conn = null;
        PreparedStatement resumeStatement = null;
        try {
            conn = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
            resumeStatement = conn.prepareStatement("select pg_xlog_replay_resume();");
            resumeStatement.execute();
        } finally {
            closeStatment(resumeStatement);
            closeConnection(conn);
        }
    }

    public String getXlogLocation(boolean master) throws SQLException {
        String location = null;
        ResultSet resultSet = null;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
            if (master) {
                this.statement = conn.prepareStatement("select pg_current_xlog_location();");
            } else {
                this.statement = conn.prepareStatement("select pg_last_xlog_replay_location();");
            }
            resultSet = this.statement.executeQuery();
            if (!resultSet.next()) {
                LOGGER.warning("Did not receive result from select.");
            } else {
                location = resultSet.getString(1);
            }
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Problem closing resultSet: {0}", e.toString());
                }
            closeConnection(conn);
        }
        return location;
    }

    private boolean stillCannotReach() {
        LOGGER.severe("Agent trying again to reach local database after previous attempts have timed out.");
        boolean reachable = checkOnce(false, Environment.getEnvironment().getLocalFinalTimeout());
        if (reachable) {
            LOGGER.severe("The database is still reachable. Will continue monitoring, but there may be a problem affecting the failover manager performance.");
            Notifications.MONITOR_TIMING_OUT.addSubjectParams(new String[] { Environment.getEnvironment().getClusterName() }).send();
            return false;
        }
        LOGGER.severe("Could not reach local database. Proceeding with shutdown.");
        return true;
    }

    private boolean checkDB(PreparedStatement s) {
        ResultSet resultSet = null;
        try {
            long now = System.currentTimeMillis();
            resultSet = s.executeQuery();
            if (LOGGER.isLoggable(Level.FINEST))
                LOGGER.log(Level.FINEST, "Query took {0} ms.", Long.valueOf(System.currentTimeMillis() - now));
            if (!resultSet.next()) {
                LOGGER.warning("Did not receive result from select.");
                return false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Exception: {0} sql state: {1}", new Object[] { e, e.getSQLState() });
            return false;
        } finally {
            if (resultSet != null)
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Problem closing resultSet: {0}", e.toString());
                }
        }
        return true;
    }

    private ThreadPoolExecutor createThreadPoolExecutor() {
        Environment env = Environment.getEnvironment();
        int maxThreads = env.getLocalDbTimeout() / env.getLocalDbPeriod() + 1;
        return new ThreadPoolExecutor(1, maxThreads, (env.getLocalDbPeriod() + 1), TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread retVal = new Thread(runnable, "DB_checker");
                retVal.setDaemon(true);
                return retVal;
            }
        },new ThreadPoolExecutor.DiscardPolicy());
    }

    private void initConnectionAndStatement() {
        synchronized (this.connectionLock) {
            try {
                closeStatment(this.statement);
                closeConnection(this.connection);
                long timeA = System.currentTimeMillis();
                this.connection = DriverManager.getConnection(this.connectionUrl, this.jdbcProps);
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST, "Acquired connection after {0} ms", Long.valueOf(System.currentTimeMillis() - timeA));
                long timeB = System.currentTimeMillis();
                this.statement = this.connection.prepareStatement("select version();");
                if (LOGGER.isLoggable(Level.FINEST))
                    LOGGER.log(Level.FINEST, "Prepared statement after {0} ms", Long.valueOf(System.currentTimeMillis() - timeB));
            } catch (SQLException sqle) {
                LOGGER.log(Level.WARNING, "Could not connect to database on {0}. Error: {1}", new Object[] { this.connectionUrl, sqle });
                String sqlState = sqle.getSQLState();
                if (DB_UP_SQL_STATE_CODE_MAP.containsKey(sqlState))
                    Notifications.DB_UP_BUT_ERROR.addSubjectParams(new String[] { this.address }).addBodyParams(new String[] { DB_UP_SQL_STATE_CODE_MAP.get(sqlState) }).send();
            }
        }
    }

    private String createConnectionString(String host, int port, String dbName) {
        if (host.contains(":"))
            return String.format("jdbc:postgresql://[%s]:%s/%s?ApplicationName='%s'", new Object[] { host, Integer.valueOf(port), dbName, "efm-2.1" });
        return String.format("jdbc:postgresql://%s:%s/%s?ApplicationName='%s'", new Object[] { host, Integer.valueOf(port), dbName, "efm-2.1" });
    }

    private void closeConnection(Connection c) {
        if (c != null)
            try {
                c.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Problem closing connection: {0}", e.toString());
            }
    }

    private void closeStatment(Statement s) {
        if (s != null)
            try {
                s.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Problem closing statment: {0}", e.toString());
            }
    }
}

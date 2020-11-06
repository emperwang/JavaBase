package com.enterprisedb.efm.exec;

import com.enterprisedb.efm.utils.LogManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Logger;

public enum WorkQueue {
    INSTANCE;

    private final transient ExecutorService executor;

    private static final Logger LOGGER;

    static {
        LOGGER = LogManager.getEfmLogger();
    }

    WorkQueue() {
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "WorkQueueThread");
            }
        });
    }

    public static boolean executeTask(Runnable runnable) {
        try {
            INSTANCE.executor.execute(runnable);
            return true;
        } catch (RejectedExecutionException ree) {
            LOGGER.warning("Task rejected during shutdown.");
            return false;
        }
    }

    public static List<Runnable> shutdown() {
        return INSTANCE.executor.shutdownNow();
    }
}

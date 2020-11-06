package com.enterprisedb.efm.exec;

import com.enterprisedb.efm.utils.LogManager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerStream extends ByteArrayOutputStream {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    private final Level level;

    private static final Logger LOGGER = LogManager.getEfmLogger();

    LoggerStream(Level level) {
        this.level = level;
    }

    public void flush() throws IOException {
        synchronized (this) {
            super.flush();
            String record = toString("UTF-8");
            reset();
            if (record.trim().isEmpty())
                return;
            out(record);
        }
    }

    private void out(String record) {
        LOGGER.log(this.level, record);
    }
}

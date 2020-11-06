package com.enterprisedb.efm.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class I18N {
    private static final ResourceBundle I18N_STRINGS = ResourceBundle.getBundle("i18n");

    private static final Logger LOGGER = LogManager.getEfmLogger();

    public static String getString(String key) {
        if (I18N_STRINGS.containsKey(key))
            return I18N_STRINGS.getString(key);
        LOGGER.log(Level.WARNING, "WARNING: Missing resource for property {0}", key);
        return key;
    }

    public static String getString(String key, Object... params) {
        if (params == null || params.length == 0)
            return getString(key);
        return MessageFormat.format(getString(key), params);
    }
}

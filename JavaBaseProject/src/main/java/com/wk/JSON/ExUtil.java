package com.wk.JSON;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExUtil {
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static String getStackTraceString(Throwable e){
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder builder = new StringBuilder();
        if (stackTrace != null && stackTrace.length > 0){
            for (StackTraceElement element : stackTrace) {
                builder.append("\t");
                builder.append("\t");
                builder.append(element.toString());
                builder.append(NEW_LINE);
            }
        }
        return builder.toString();
    }

    public static String buildErrorMessage(Exception ex){
        String result;
        String stackTrace = getStackTraceString(ex);
        String exceptionType = ex.toString();
        String exceptionMessage = ex.getMessage();

        result = String.format("\t%s: %s "+NEW_LINE+"%s", exceptionType,
                exceptionMessage, stackTrace);
        return result;
    }

}

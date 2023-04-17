package com.example.webshop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggingUtil {

    private LoggingUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void logException(Throwable e, Log log) {
        StringBuilder builder = new StringBuilder();
        builder.append(e);
        builder.append(System.lineSeparator());
        for (StackTraceElement element : e.getStackTrace()) {
            builder.append(element);
            builder.append(System.lineSeparator());
        }
        log.error(builder);
    }

    public static <T> void logException(Throwable e, Class<T> clazz) {
        logException(e, LogFactory.getLog(clazz));
    }
}
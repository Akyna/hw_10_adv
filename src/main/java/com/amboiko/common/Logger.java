package com.amboiko.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    public static void info(final String message) {
        System.out.println("[" + LogLevel.INFO + ": " + (LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))) + "]: " + message);
    }

    public static void log(final LogLevel logLevel, final String message) {
        System.out.println("[" + logLevel + "]: " + message);
    }
}

package io.github.some_example_name.io;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private final LogDestination destination;
    private final String logFilePath;
    private final FileIO fileIO = new FileIO();

    public Logger(LogDestination destination, String logFilePath) {
        this.destination = destination;
        this.logFilePath = logFilePath;
    }

    public void info(LogCategory category, String message) {
        log("INFO", category, message);
    }

    public void warning(LogCategory category, String message) {
        log("WARN", category, message);
    }

    public void error(LogCategory category, String message) {
        log("ERROR", category, message);
    }

    private void log(String level, LogCategory category, String message) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String line = "[" + ts + "] "
                + "[" + category + "] "
                + level + ": "
                + message;

        if (destination == LogDestination.CONSOLE || destination == LogDestination.BOTH) {
            System.out.println(line);
        }

        if ((destination == LogDestination.FILE || destination == LogDestination.BOTH)
                && logFilePath != null) {

            String existing = "";
            if (fileIO.exists(logFilePath)) {
                existing = fileIO.readText(logFilePath);
            }
            fileIO.writeText(logFilePath, existing + line + "\n");
        }
    }
}

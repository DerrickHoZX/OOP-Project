package io.github.abstractengine.io;

public class Logging {

    private final Logger logger;

    public Logging(Logger logger) {
        this.logger = logger;
    }

    public void info(LogCategory category, String message) {
        logger.info(category, message);
    }

    public void warning(LogCategory category, String message) {
        logger.warning(category, message);
    }

    public void error(LogCategory category, String message) {
        logger.error(category, message);
    }

    public void dispose() {}
}

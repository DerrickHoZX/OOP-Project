package io.github.abstractengine.io;

public class Persistence {

    private final FileIO fileIO;
    private final String basePath;
    private final SaveFormat format;
    private final Logging logging;   // Injected logging

    public Persistence(FileIO fileIO,
                       String basePath,
                       SaveFormat format,
                       Logging logging) {
        this.fileIO = fileIO;
        this.basePath = basePath;
        this.format = format;
        this.logging = logging;
    }

    public void save(String path, String data) {
        String fullPath = basePath + path;

        fileIO.writeText(fullPath, data);

        logging.info(LogCategory.PERSISTENCE,
                "Saved file: " + fullPath + " (Format: " + format + ")");
    }

    public String load(String path) {
        String fullPath = basePath + path;

        String data = fileIO.readText(fullPath);

        logging.info(LogCategory.PERSISTENCE,
                "Loaded file: " + fullPath);

        return data;
    }

    public boolean exists(String path) {
        String fullPath = basePath + path;

        boolean exists = fileIO.exists(fullPath);

        logging.info(LogCategory.PERSISTENCE,
                "Checked file exists: " + fullPath + " -> " + exists);

        return exists;
    }

    public void dispose() {
        logging.info(LogCategory.PERSISTENCE,
                "Persistence disposed");
    }
}
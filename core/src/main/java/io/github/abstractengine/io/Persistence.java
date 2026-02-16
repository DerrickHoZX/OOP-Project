package io.github.abstractengine.io;

public class Persistence {

    private final FileIO fileIO;

    protected final String basePath;
    protected final SaveFormat format;

    public Persistence(FileIO fileIO, String basePath, SaveFormat format) {
        this.fileIO = (fileIO == null) ? new FileIO() : fileIO;
        this.basePath = (basePath == null) ? "saves/" : basePath;
        this.format = (format == null) ? SaveFormat.TEXT : format;
    }

    public void save(String path, String data) {
        fileIO.writeText(resolve(path), data);
    }

    public String load(String path) {
        return fileIO.readText(resolve(path));
    }

    public boolean exists(String path) {
        return fileIO.exists(resolve(path));
    }

    public void dispose() {
    }

    private String resolve(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path must not be empty");
        }
        if (basePath.endsWith("/") || basePath.endsWith("\\")) {
            return basePath + path;
        }
        return basePath + "/" + path;
    }
}

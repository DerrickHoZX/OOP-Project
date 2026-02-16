package io.github.abstractengine.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class FileIO {

    private final Charset charset;

    public FileIO() {
        this(StandardCharsets.UTF_8);
    }

    public FileIO(Charset charset) {
        this.charset = (charset == null) ? StandardCharsets.UTF_8 : charset;
    }

    public void writeText(String path, String content) {
        try {
            Path p = Paths.get(path);
            Path parent = p.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            Files.write(p, 
                (content == null ? "" : content).getBytes(charset)
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    public String readText(String path) {
        try {
            Path p = Paths.get(path);
            byte[] bytes = Files.readAllBytes(p);
            return new String(bytes, charset);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    public boolean exists(String path) {
        return Files.exists(Paths.get(path));
    }
}

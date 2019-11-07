package util;

import config.Config;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by thales on 09/04/17.
 */
public class FileUtils {
    public static void createFile(String path) throws IOException {
        if (!Files.exists(Paths.get(path))) {
            Files.createFile(Paths.get(path));
        }
    }

    public static void createDirectory(String path) throws IOException {
        if(!Files.exists(Paths.get(path))) {
            Files.createDirectory(Paths.get(path));
        }
    }

    public static void writeInFile(String path, String value) throws IOException {
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.write(value);
        writer.close();
    }

    public static void writeInFile(String path, byte[] value) throws IOException {
        Files.write(Paths.get(path), value);
    }
}

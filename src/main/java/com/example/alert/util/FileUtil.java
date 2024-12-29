package com.example.alert.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static void createDirectories(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
    }
    public static boolean existsDirectories(String path){
        return Files.exists(Path.of(path));
    }
    public static String getFileTxt(String path) throws IOException {
        return Files.readString(Path.of(path));
    };
    public static File getFile(String path) {
        return new File(path);
    }
}

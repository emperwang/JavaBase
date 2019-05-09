package com.wk.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    private static Charset FILE_ENC = StandardCharsets.UTF_8;

    public static boolean exists(String filePath){
        return Files.exists(Paths.get(filePath));
    }

    public static String readString(String filePath){
        return new String(FileUtil.readAllBytes(filePath),FILE_ENC);
    }

    public static byte[] readAllBytes(String filePath){
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}

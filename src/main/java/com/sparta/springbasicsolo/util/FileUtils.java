package com.sparta.springbasicsolo.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

public class FileUtils {

    public static String getAbsoluteUploadFolder() {
        File file = new File("");
        String currentAbsolutePath = String.valueOf(file.getAbsoluteFile()) + "/upload/";
        Path path = Paths.get(currentAbsolutePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("사진을 업로드할 폴더를 생성할 수 없습니다.", e);
            }
        }
        return currentAbsolutePath;
    }

    public static String createFileName(String originalFileName) {
        String extension = extractExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }

    public static String extractOriginalName(String originalFileName) {
        return originalFileName.substring(0, originalFileName.indexOf("."));
    }

    public static String extractExtension(String originalFileName) {
        int point = originalFileName.lastIndexOf(".");
        return originalFileName.substring(point + 1);
    }

    public static void invalidExtension(String fileName, String...extensions) {
        String substring = extractExtension(fileName).toLowerCase();
        for (String extension : extensions) {
            if (substring.equals(extension.toLowerCase())) {
                return;
            }
        }
        throw new IllegalArgumentException("저장할 수 없는 형식의 확장자입니다. (사용가능한 확장자:" + Arrays.toString(extensions) + ")");
    }

    public static void invalidFileSize(long fileSize, int maxMbSize) {
        if (fileSize > maxMbSize * 1024L * 1024L) {
            throw new IllegalArgumentException("파일의 크기가 너무 큽니다. (최대 " + maxMbSize + "MB 까지 가능)");
        }
    }
}
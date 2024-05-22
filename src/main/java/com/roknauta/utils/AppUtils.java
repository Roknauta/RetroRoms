package com.roknauta.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;

public class AppUtils {

    public static void renomearParaCheckSum(File file, String target) throws IOException {
        String crc32 = getCRC32(file);
        if (crcExists(crc32, target)) {
            file.delete();
        } else {
            FileUtils.moveFile(file,
                new File(target, crc32 + "." + FilenameUtils.getExtension(file.getName()).toLowerCase()));
        }
    }

    private static boolean crcExists(String crc32, String target) {
        return Arrays.stream(new File(target).listFiles())
            .anyMatch(f -> FilenameUtils.getBaseName(f.getName()).equalsIgnoreCase(crc32));
    }

    public static String getCRC32(File file) throws IOException {
        return String.format("%08X", FileUtils.checksumCRC32(file));
    }

    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return StringUtils.EMPTY;
        }
        long horas = duration.toHours();
        long minutos = duration.toMinutesPart();
        long segundos = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    public static String createNewFolder(String... path) {
        File folder = new File(buildPath(path));
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder.getAbsolutePath();
    }

    public static File getFolderFromPath(String... path) {
        return new File(buildPath(path));
    }

    private static String buildPath(String... folder) {
        StringBuilder builder = new StringBuilder();
        for (String pathItem : folder) {
            builder.append(pathItem).append(File.separator);
        }
        return builder.toString();
    }

}

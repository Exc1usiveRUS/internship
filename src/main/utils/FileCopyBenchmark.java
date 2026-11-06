package main.utils;

import java.io.*;
import java.util.Random;

public class FileCopyBenchmark {

    // побайтовое чтение и запись без буфера
    public static void copyWithoutBuffer(String src, String dst) throws IOException {
        try (FileInputStream input = new FileInputStream(src);
             FileOutputStream output = new FileOutputStream(dst)) {
            int b;
            while ((b = input.read()) != -1) {
                output.write(b);
            }
        }
    }

    // Версия с буферизацией
    public static void copyBuffered(String src, String dst) throws IOException {
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(dst))) {
            int b;
            while ((b = input.read()) != -1) {
                output.write(b);
            }
        }
    }

    // Генерация тестового файла

    public static void generateTestFile(String path, long sizeBytes) throws IOException {
        Random random = new Random(42);
        byte[] chunk = new byte[8192];
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path))) {
            long written = 0;
            while (written < sizeBytes) {
                random.nextBytes(chunk);
                int toWrite = (int) Math.min(chunk.length, sizeBytes - written);
                out.write(chunk, 0, toWrite);
                written += toWrite;
            }
        }
    }
}

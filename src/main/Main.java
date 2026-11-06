package main;

import main.model.Money;
import main.resources.DbConnection;
import main.resources.Session;
import main.utils.FileCopyBenchmark;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        moneyTask();
        System.out.println();
        demoClose();
        System.out.println();
        demoSuppressed();
        System.out.println();
        try {
            bufferingComparison();
        } catch (IOException ioException) {
            System.out.println("Ошибка при чтении/записи");
        }
    }

    private static void moneyTask() {
        Money a = new Money(new BigDecimal("10.00"), "usd");
        Money b = new Money(new BigDecimal("10"), " USD");
        Money c = new Money(new BigDecimal("10.00"), "USD");

        System.out.println("a.equals(b): " + a.equals(b));
        System.out.println("a.equals(c): " + a.equals(c));

        Set<Money> set = new HashSet<>();
        set.add(a);
        set.add(b);
        set.add(c);
        System.out.println("Размер HashSet: " + set.size());
    }

    // методы демонстрации третьей задачи

    private static void demoClose() {
        System.out.println("Порядок закрытия");
        try (DbConnection connection = new DbConnection(false);
            Session session = new Session(false)) {
            System.out.println("Работа внутри блока try");
        }
    }

    /**
     * Suppressed exception это подавленное исключение, которое возникает, когда летит другое исключение.
     * Оно не теряется, а прикрепляется к основному и доступно через getSuppressed(). В нашем случае выбрасывается
     * исключение в блоке catch, а исключение из close() добавляются к нему.
     */

    private static void demoSuppressed() {
        System.out.println("Демо Suppressed exceptions");
        try (DbConnection connection = new DbConnection(true);
            Session session = new Session(true)) {
            System.out.println("Работа внутри блока try");
            throw new RuntimeException("Ошибка в теле блока try");
        } catch (Exception e) {
            System.out.println("Основное исключение: " + e.getMessage());
            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("  suppressed: " + suppressed.getMessage());
            }
        }
    }

    /**
     * По замерам лично на моём ПК вышла разница +- в 200 раз(14 с небольшим минут против 2,5 секунд). Если коротко - благодаря буферизации мы в разы сокращаем
     * количество дорогих обращений к системе. С буфером мы берем кусок и обрабатываем его в памяти, вместо побайтового
     * обращения к системе.
     */

    private static void bufferingComparison() throws IOException {
        String src = "test_100mb.bin";
        String dst1 = "copy_nobuffer.bin";
        String dst2 = "copy_buffered.bin";

        // Готовим файл 100 МБ
        File f = new File(src);
        if (!f.exists() || f.length() < 100L * 1024 * 1024) {
            System.out.println("Генерируем тестовый файл 100 МБ...");
            FileCopyBenchmark.generateTestFile(src, 100L * 1024 * 1024);
        }
        System.out.printf("Размер исходного файла: %.2f МБ%n%n",
                f.length() / 1024.0 / 1024.0);

        System.out.println("Начинаем замер без буферизации");
        long t1 = System.nanoTime();
        FileCopyBenchmark.copyWithoutBuffer(src, dst1);
        long t2 = System.nanoTime();
        double secNoBuf = (t2 - t1) / 1_000_000_000.0;

        System.out.println("Начинаем замер с буферизацией");
        long t3 = System.nanoTime();
        FileCopyBenchmark.copyBuffered(src, dst2);
        long t4 = System.nanoTime();
        double secBuf = (t4 - t3) / 1_000_000_000.0;

        System.out.printf("Без буфера     : %.3f сек%n", secNoBuf);
        System.out.printf("С буфером      : %.3f сек%n", secBuf);
        System.out.printf("Ускорение      : в %.1f раз%n", secNoBuf / secBuf);
    }
}

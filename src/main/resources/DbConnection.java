package main.resources;

public class DbConnection implements AutoCloseable {

    private final boolean failOnClose;

    public DbConnection(boolean failOnClose) {
        this.failOnClose = failOnClose;
        System.out.println("Открываем DbConnection");
    }

    @Override
    public void close() {
        System.out.println("Закрываем DbConnection");
        if (failOnClose) {
            throw new IllegalStateException("Ошибка при закрытии DbConnection");
        }
    }
}

package main.resources;

public class Session implements AutoCloseable {

    private final boolean failOnClose;

    public Session(boolean failOnClose) {
        this.failOnClose = failOnClose;
        System.out.println("Открыто: Session");
    }

    @Override
    public void close() {
        System.out.println("Закрывается: Session");
        if (failOnClose) {
            throw new IllegalStateException("Ошибка при закрытии Session");
        }
    }
}

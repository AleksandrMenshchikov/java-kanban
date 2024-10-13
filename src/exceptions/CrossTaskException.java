package exceptions;

public final class CrossTaskException extends RuntimeException {
    public CrossTaskException() {
        super("Задача пересекается с существующими");
    }
}

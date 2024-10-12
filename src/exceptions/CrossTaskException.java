package exceptions;

public final class CrossTaskException extends Exception {
    public CrossTaskException() {
        super("Задача пересекается с существующими");
    }
}

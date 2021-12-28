package by.nosevich.carrental.exceptions;

public class OrderIsCrossException extends RuntimeException {
    public OrderIsCrossException() {
    }

    public OrderIsCrossException(String message) {
        super(message);
    }
}

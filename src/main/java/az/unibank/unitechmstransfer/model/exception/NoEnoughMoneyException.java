package az.unibank.unitechmstransfer.model.exception;

public class NoEnoughMoneyException extends RuntimeException {
    public NoEnoughMoneyException(String message) {
        super(message);
    }
}

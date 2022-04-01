package az.unibank.unitechmstransfer.model.exception;

public class NonActiveAccountException extends RuntimeException {
    public NonActiveAccountException(String message) {
        super(message);
    }
}

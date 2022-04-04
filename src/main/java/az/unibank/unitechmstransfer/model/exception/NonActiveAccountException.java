package az.unibank.unitechmstransfer.model.exception;

import lombok.Getter;

@Getter
public class NonActiveAccountException extends RuntimeException {
    private final String code;
    private final String message;

    public NonActiveAccountException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public NonActiveAccountException(String message) {
        super(message);
        this.code = null;
        this.message = message;
    }

    public static NonActiveAccountException of(String errorCode, String errorMessage) {
        return new NonActiveAccountException(errorCode, errorMessage);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

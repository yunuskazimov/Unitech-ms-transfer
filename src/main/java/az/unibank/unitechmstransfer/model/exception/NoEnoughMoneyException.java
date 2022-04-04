package az.unibank.unitechmstransfer.model.exception;

import lombok.Getter;

@Getter
public class NoEnoughMoneyException extends RuntimeException {
    private final String code;
    private final String message;

    public NoEnoughMoneyException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public NoEnoughMoneyException(String message) {
        super(message);
        this.code = null;
        this.message = message;
    }

    public static NoEnoughMoneyException of(String errorCode, String errorMessage) {
        return new NoEnoughMoneyException(errorCode, errorMessage);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

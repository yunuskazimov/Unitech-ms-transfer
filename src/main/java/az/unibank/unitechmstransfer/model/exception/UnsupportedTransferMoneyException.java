package az.unibank.unitechmstransfer.model.exception;

import lombok.Getter;

@Getter
public class UnsupportedTransferMoneyException extends RuntimeException {
    private final String code;
    private final String message;

    public UnsupportedTransferMoneyException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public UnsupportedTransferMoneyException(String message) {
        super(message);
        this.code = null;
        this.message = message;
    }

    public static UnsupportedTransferMoneyException of(String errorCode, String errorMessage) {
        return new UnsupportedTransferMoneyException(errorCode, errorMessage);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

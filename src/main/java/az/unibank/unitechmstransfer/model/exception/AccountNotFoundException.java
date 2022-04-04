package az.unibank.unitechmstransfer.model.exception;

import lombok.Getter;

@Getter
public class AccountNotFoundException extends RuntimeException {
    private final String code;
    private final String message;

    public AccountNotFoundException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AccountNotFoundException(String message) {
        super(message);
        this.code = null;
        this.message = message;
    }

    public static AccountNotFoundException of(String errorCode, String errorMessage) {
        return new AccountNotFoundException(errorCode, errorMessage);
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}

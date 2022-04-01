package az.unibank.unitechmstransfer.model.exception;

public class ErrorCodes {
    public static final String UNEXPECTED_EXCEPTION = "transfer.unexpected-exception";
    public static final String NON_ACTIVE = "transfer.non-active-account-exception";
    public static final String SAME_ACCOUNT = "transfer.same-account-exception";
    public static final String NO_MONEY = "transfer.not-enough-money-exception";

    private ErrorCodes() {
    }
}

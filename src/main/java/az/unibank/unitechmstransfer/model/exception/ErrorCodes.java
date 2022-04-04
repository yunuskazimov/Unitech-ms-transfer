package az.unibank.unitechmstransfer.model.exception;

public class ErrorCodes {
    public static final String UNEXPECTED_EXCEPTION = "transfer.unexpected-exception";
    public static final String NON_ACTIVE = "transfer.non-active-account-exception";
    public static final String NOT_FOUND = "transfer.not-found-account-exception";
    public static final String SAME_ACCOUNT = "transfer.same-account-exception";
    public static final String NO_MONEY = "transfer.not-enough-money-exception";
    public static final String MONEY_IS_ZERO_OR_NEGATIVE =
            "transfer.money-is-zero-negative-exception";

    private ErrorCodes() {
    }
}

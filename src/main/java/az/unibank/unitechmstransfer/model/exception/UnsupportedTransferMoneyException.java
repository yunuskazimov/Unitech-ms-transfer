package az.unibank.unitechmstransfer.model.exception;

public class UnsupportedTransferMoneyException extends RuntimeException {
    public UnsupportedTransferMoneyException(String message) {
        super(message);
    }
}

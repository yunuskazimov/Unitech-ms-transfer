package az.unibank.unitechmstransfer.service;

import az.unibank.unitechmstransfer.model.TransferDto;

public interface TransferService {
    String makeTransfer(TransferDto dto);
}

package az.unibank.unitechmstransfer.service.impl;

import az.unibank.unitechmstransfer.client.AccountClientRest;
import az.unibank.unitechmstransfer.client.dto.AccountDto;
import az.unibank.unitechmstransfer.client.dto.Status;
import az.unibank.unitechmstransfer.dao.repository.TransferRepo;
import az.unibank.unitechmstransfer.mapper.TransferMapper;
import az.unibank.unitechmstransfer.model.TransferDto;
import az.unibank.unitechmstransfer.model.exception.NoEnoughMoneyException;
import az.unibank.unitechmstransfer.model.exception.NonActiveAccountException;
import az.unibank.unitechmstransfer.model.exception.SameAccountException;
import az.unibank.unitechmstransfer.model.exception.UnsupportedTransferMoneyException;
import az.unibank.unitechmstransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final TransferRepo transferRepo;
    private final AccountClientRest accountClient;
    private final TransferMapper transferMapper;

    @Override
    @Transactional
    public String makeTransfer(TransferDto transferDto) {
        log.info("service makeTransfer started with Transfer DTO: {}", transferDto);
        checkAccountIsSame(transferDto);
        checkTransferAmount(transferDto);
        AccountDto senderAccount = accountClient.getById(transferDto.getSenderAccountId());
        AccountDto receiverAccount = accountClient.getById(transferDto.getReceiverAccountId());
        checkAccountStatusIsActive(senderAccount);
        checkAccountStatusIsActive(receiverAccount);
        checkSenderEnoughMoney(senderAccount, transferDto.getTransferAmount());
        transferMoneyToAccount(senderAccount, receiverAccount, transferDto);
        transferRepo.save(transferMapper.toEntity(transferDto));
        log.info("service makeTransfer completed with Transfer DTO: {}", transferDto);
        return "The Transfer was successful";
    }

    private void transferMoneyToAccount(AccountDto senderDto,
                                        AccountDto receiverDto,
                                        TransferDto transferDto) {

        BigDecimal transferMoney = transferDto.getTransferAmount();
        senderDto.setAmount(senderDto.getAmount().subtract(transferMoney));
        receiverDto.setAmount(receiverDto.getAmount().add(transferMoney));

        accountClient.updateAccountBalance(senderDto);
        accountClient.updateAccountBalance(receiverDto);

    }

    private void checkAccountStatusIsActive(AccountDto dto) {
        if (dto.getStatus().equals(Status.DEACTIVE)) {
            throw new NonActiveAccountException("Account is Deactived. Account ID: "
                    + dto.getAccountId());
        }
    }

    private void checkAccountIsSame(TransferDto dto) {
        if (dto.getSenderAccountId().equals(dto.getReceiverAccountId())) {
            throw new SameAccountException("Please Select different Account for Transfer");
        }
    }

    private void checkSenderEnoughMoney(AccountDto senderDto, BigDecimal transferAmount) {
        int result = senderDto.getAmount().subtract(transferAmount)
                .compareTo(new BigDecimal(0));
        if (result == 0 || result < 0) {
            throw new NoEnoughMoneyException("no enough money in account balance for transfer");
        }
    }

    private void checkTransferAmount(TransferDto transferDto) {
        if ((transferDto.getTransferAmount().doubleValue() <= 0)) {
            throw new UnsupportedTransferMoneyException("Transfer Amount is unsupported. Amount: "
                    + transferDto.getTransferAmount());
        }
    }
}

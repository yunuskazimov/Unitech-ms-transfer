package az.unibank.unitechmstransfer.service.impl;

import az.unibank.unitechmstransfer.client.AccountClientRest;
import az.unibank.unitechmstransfer.client.dto.AccountDto;
import az.unibank.unitechmstransfer.client.dto.CurrencyType;
import az.unibank.unitechmstransfer.client.dto.Status;
import az.unibank.unitechmstransfer.dao.entity.TransferEntity;
import az.unibank.unitechmstransfer.dao.repository.TransferRepo;
import az.unibank.unitechmstransfer.mapper.TransferMapper;
import az.unibank.unitechmstransfer.model.TransferDto;
import az.unibank.unitechmstransfer.model.exception.AccountNotFoundException;
import az.unibank.unitechmstransfer.model.exception.ErrorCodes;
import az.unibank.unitechmstransfer.model.exception.NoEnoughMoneyException;
import az.unibank.unitechmstransfer.model.exception.NonActiveAccountException;
import az.unibank.unitechmstransfer.model.exception.SameAccountException;
import az.unibank.unitechmstransfer.model.exception.UnsupportedTransferMoneyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    private static final String SENDER_CUSTOMER_PIN = "1OYG677";
    private static final String SENDER_ACCOUNT_ID = "dda8850c-5bff-4ee3-a118-9684e2fe004d";
    private static final String RECEIVER_ACCOUNT_ID = "548850c-5bff-4ee3-a118-9684e2fe004d";
    private static final BigDecimal TRANSFER_AMOUNT = BigDecimal.valueOf(100);

    @Mock
    private TransferRepo transferRepo;

    @Mock
    private AccountClientRest accountClient;

    @Mock
    private TransferMapper transferMapper;

    @InjectMocks
    private TransferServiceImpl transferService;

    @Test
    void makeTransfer() {
        //given
        TransferDto transferDto = getTransferDto();

        TransferEntity transferEntity = getTransferEntity();

        AccountDto senderAccount = getSenderAccount();

        AccountDto receiverAmount = getReceiverAccount();

        //when
        when(accountClient.getById(transferDto.getSenderAccountId()))
                .thenReturn(senderAccount);
        when(accountClient.getById(transferDto.getReceiverAccountId()))
                .thenReturn(receiverAmount);
        when(transferMapper.toEntity(transferDto)).thenReturn(transferEntity);
        when(transferRepo.save(transferMapper.toEntity(transferDto))).thenReturn(transferEntity);

        var actual = transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto);

        assertEquals(actual, "The Transfer was successful");

        verify(accountClient, times(1))
                .getById(transferDto.getSenderAccountId());
        verify(accountClient, times(1))
                .getById(transferDto.getReceiverAccountId());
        verify(transferRepo, times(1)).save(transferEntity);
    }

    @Test
    void makeTransfer_AccountIsSame_ShouldThrowSameAccountException() {
        //given
        TransferDto transferDto = getTransferDto();
        transferDto.setReceiverAccountId(SENDER_ACCOUNT_ID);

        var actual = assertThrows(SameAccountException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.SAME_ACCOUNT, actual.getCode());

    }

    @Test
    void makeTransfer_TransferAmountIsZero_ShouldThrowUnsupportedTransferMoneyException() {
        //given
        TransferDto transferDto = getTransferDto();
        transferDto.setTransferAmount(BigDecimal.valueOf(0));

        var actual = assertThrows(UnsupportedTransferMoneyException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.MONEY_IS_ZERO_OR_NEGATIVE, actual.getCode());
    }

    @Test
    void makeTransfer_TransferAmountIsNegative_ShouldThrowUnsupportedTransferMoneyException() {
        //given
        TransferDto transferDto = getTransferDto();
        transferDto.setTransferAmount(BigDecimal.valueOf(-5));

        var actual = assertThrows(UnsupportedTransferMoneyException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.MONEY_IS_ZERO_OR_NEGATIVE, actual.getCode());
    }

    @Test
    void makeTransfer_AccountBelongSomeone_ShouldThrowAccountNotFoundException() {
        //given
        TransferDto transferDto = getTransferDto();

        AccountDto senderAccount = getSenderAccount();
        senderAccount.setCustomerPin("5KJB321");

        //when
        when(accountClient.getById(SENDER_ACCOUNT_ID)).thenReturn(senderAccount);

        var actual = assertThrows(AccountNotFoundException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.NOT_FOUND, actual.getCode());

        verify(accountClient, times(1)).getById(SENDER_ACCOUNT_ID);
    }

    @Test
    void makeTransfer_AccountInactive_ShouldThrowNonActiveAccountException() {
        //given
        TransferDto transferDto = getTransferDto();

        AccountDto senderAccount = getSenderAccount();
        senderAccount.setStatus(Status.DEACTIVE);

        //when
        when(accountClient.getById(SENDER_ACCOUNT_ID)).thenReturn(senderAccount);

        var actual = assertThrows(NonActiveAccountException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.NON_ACTIVE, actual.getCode());

        verify(accountClient, times(1)).getById(SENDER_ACCOUNT_ID);
    }

    @Test
    void makeTransfer_SenderNotEnoughMoney_ShouldThrowNoEnoughMoneyException() {
        //given
        TransferDto transferDto = getTransferDto();

        AccountDto senderAccount = getSenderAccount();
        senderAccount.setAmount(BigDecimal.valueOf(50));

        AccountDto receiverAccount = getReceiverAccount();

        //when
        when(accountClient.getById(SENDER_ACCOUNT_ID)).thenReturn(senderAccount);
        when(accountClient.getById(RECEIVER_ACCOUNT_ID)).thenReturn(receiverAccount);

        var actual = assertThrows(NoEnoughMoneyException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.NO_MONEY, actual.getCode());

        verify(accountClient, times(1)).getById(SENDER_ACCOUNT_ID);
        verify(accountClient, times(1)).getById(RECEIVER_ACCOUNT_ID);
    }

    @Test
    void makeTransfer_Success() {
        //given
        TransferDto transferDto = getTransferDto();

        AccountDto senderAccount = getSenderAccount();
        senderAccount.setAmount(BigDecimal.valueOf(50));

        AccountDto receiverAccount = getReceiverAccount();

        //when
        when(accountClient.getById(SENDER_ACCOUNT_ID)).thenReturn(senderAccount);
        when(accountClient.getById(RECEIVER_ACCOUNT_ID)).thenReturn(receiverAccount);

        var actual = assertThrows(NoEnoughMoneyException.class,
                () -> transferService.makeTransfer(SENDER_CUSTOMER_PIN, transferDto));

        Assertions.assertEquals(ErrorCodes.NO_MONEY, actual.getCode());

        verify(accountClient, times(1)).getById(SENDER_ACCOUNT_ID);
        verify(accountClient, times(1)).getById(RECEIVER_ACCOUNT_ID);
    }

    private AccountDto getSenderAccount() {
        AccountDto senderAccount = new AccountDto();
        senderAccount.setAccountId(SENDER_ACCOUNT_ID);
        senderAccount.setCurrencyType(CurrencyType.AZN);
        senderAccount.setCustomerPin(SENDER_CUSTOMER_PIN);
        senderAccount.setAmount(BigDecimal.valueOf(250));
        senderAccount.setStatus(Status.ACTIVE);
        return senderAccount;
    }

    private AccountDto getReceiverAccount() {
        AccountDto senderAccount = new AccountDto();
        senderAccount.setAccountId(SENDER_ACCOUNT_ID);
        senderAccount.setCurrencyType(CurrencyType.AZN);
        senderAccount.setCustomerPin(SENDER_CUSTOMER_PIN);
        senderAccount.setAmount(BigDecimal.valueOf(10));
        senderAccount.setStatus(Status.ACTIVE);
        return senderAccount;
    }

    private TransferEntity getTransferEntity() {
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setSenderAccountId(SENDER_ACCOUNT_ID);
        transferEntity.setReceiverAccountId(RECEIVER_ACCOUNT_ID);
        transferEntity.setTransferAmount(TRANSFER_AMOUNT);
        transferEntity.setId(1L);
        return transferEntity;
    }

    private TransferDto getTransferDto() {
        TransferDto transferDto = new TransferDto();
        transferDto.setSenderAccountId(SENDER_ACCOUNT_ID);
        transferDto.setReceiverAccountId(RECEIVER_ACCOUNT_ID);
        transferDto.setTransferAmount(TRANSFER_AMOUNT);
        return transferDto;
    }
}
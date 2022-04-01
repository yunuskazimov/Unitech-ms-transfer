package az.unibank.unitechmstransfer.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String accountId;
    private String customerPin;
    private BigDecimal amount;
    private CurrencyType currencyType;
    private Status status;
}

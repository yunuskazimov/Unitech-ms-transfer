package az.unibank.unitechmstransfer.client;

import az.unibank.unitechmstransfer.client.dto.AccountDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AccountClientRest {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public AccountClientRest(RestTemplate restTemplate,
                             @Value("${client.users.int.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public AccountDto getById(String id) {
        log.info("client service getById started Account ID: {}",id);
        String url = String.format("%s/%s/%s", apiUrl,"id", id);
        return restTemplate.getForObject(url, AccountDto.class);
    }

    public AccountDto updateAccountBalance(AccountDto accountDto) {
        log.info("client service updateAccountBalance started Account ID: {}",accountDto.getAccountId());
        return restTemplate.postForObject(apiUrl, accountDto, AccountDto.class);
    }
}

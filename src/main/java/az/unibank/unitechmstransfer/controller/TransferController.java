package az.unibank.unitechmstransfer.controller;

import az.unibank.unitechmstransfer.model.TransferDto;
import az.unibank.unitechmstransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
@Slf4j
@RequiredArgsConstructor
public class TransferController {
    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String makeTransfer(@RequestHeader(name = "Customer-Pin") String customerPin,
                               @RequestBody TransferDto transferDto) {
        return service.makeTransfer(customerPin, transferDto);
    }
}

package az.unibank.unitechmstransfer.controller;

import az.unibank.unitechmstransfer.model.TransferDto;
import az.unibank.unitechmstransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfer")
@Slf4j
@RequiredArgsConstructor
public class TransferController {
    private final TransferService service;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String makeTransfer(@RequestBody TransferDto transferDto) {
        return service.makeTransfer(transferDto);
    }
}

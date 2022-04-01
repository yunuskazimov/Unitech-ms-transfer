package az.unibank.unitechmstransfer.controller.error;

import az.unibank.unitechmstransfer.model.ErrorDto;
import az.unibank.unitechmstransfer.model.exception.NoEnoughMoneyException;
import az.unibank.unitechmstransfer.model.exception.NonActiveAccountException;
import az.unibank.unitechmstransfer.model.exception.SameAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static az.unibank.unitechmstransfer.model.exception.ErrorCodes.*;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ErrorHandler.class.getName());

    @ExceptionHandler(SameAccountException.class)
    public ResponseEntity<Object> handleSameAccountException(SameAccountException ex,
                                                             WebRequest webRequest) {
        logger.info(ex.toString());

        return handleExceptionInternal(ex, ErrorDto.builder()
                        .code(SAME_ACCOUNT)
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(NoEnoughMoneyException.class)
    public ResponseEntity<Object> handleNoEnoughMoneyException(NoEnoughMoneyException ex,
                                                               WebRequest webRequest) {
        logger.info(ex.toString());

        return handleExceptionInternal(ex, ErrorDto.builder()
                        .code(NO_MONEY)
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(NonActiveAccountException.class)
    public ResponseEntity<Object> handleNonActiveAccountException(NonActiveAccountException ex,
                                                                  WebRequest webRequest) {
        logger.info(ex.toString());

        return handleExceptionInternal(ex, ErrorDto.builder()
                        .code(NON_ACTIVE)
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.CONFLICT, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllException(Exception ex,
                                                     WebRequest webRequest) {
        logger.info(ex.getMessage());

        return handleExceptionInternal(ex, ErrorDto.builder()
                        .code(UNEXPECTED_EXCEPTION)
                        .message(ex.getMessage())
                        .build(),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }
}

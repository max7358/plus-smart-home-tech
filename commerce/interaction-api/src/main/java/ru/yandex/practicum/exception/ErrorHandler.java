package ru.yandex.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice("ru.yandex.practicum")
public class ErrorHandler {

    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final NotFoundException ex) {
        return Error.builder()
                .status(HttpStatus.NOT_FOUND)
                .reason("The required object was not found.")
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)))
                .build();
    }

    @ExceptionHandler({BadRequestException.class, NoProductsInShoppingCartException.class,
            SpecifiedProductAlreadyInWarehouseException.class, NoSpecifiedProductInWarehouseException.class,
            ProductInShoppingCartLowQuantityInWarehouse.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleBadRequestException(final BadRequestException ex) {
        return Error.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Bad request.")
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)))
                .build();
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        return Error.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(ex.getMessage())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)))
                .build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleArgumentNotValidException(final MethodArgumentNotValidException ex) {
        return Error.builder()
                .status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(ex.getBindingResult().getFieldErrors().stream().map(fe -> "Field: " + fe.getField() + ". Error: " + fe.getDefaultMessage()
                ).collect(Collectors.joining(", ")))
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)))
                .build();
    }
}
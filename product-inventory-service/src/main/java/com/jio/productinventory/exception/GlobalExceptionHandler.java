package com.jio.productinventory.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<TmfErrorDto> handleNotFound(ProductNotFoundException ex) {
        log.warn("Product not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(TmfErrorDto.builder()
                        .code("404")
                        .reason("Not Found")
                        .message(ex.getMessage())
                        .status(404)
                        .atType("Error")
                        .build());
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<TmfErrorDto> handleInvalidTransition(InvalidStatusTransitionException ex) {
        log.warn("Invalid status transition: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(TmfErrorDto.builder()
                        .code("422")
                        .reason("Invalid Status Transition")
                        .message(ex.getMessage())
                        .status(422)
                        .atType("Error")
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TmfErrorDto> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(TmfErrorDto.builder()
                        .code("400")
                        .reason("Validation Failed")
                        .message(details)
                        .status(400)
                        .atType("Error")
                        .build());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<TmfErrorDto> handleOptimisticLock(OptimisticLockingFailureException ex) {
        log.warn("Optimistic lock conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(TmfErrorDto.builder()
                        .code("409")
                        .reason("Conflict")
                        .message("Resource was modified concurrently. Please retry.")
                        .status(409)
                        .atType("Error")
                        .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<TmfErrorDto> handleIllegalArg(IllegalArgumentException ex) {
        log.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(TmfErrorDto.builder()
                        .code("400")
                        .reason("Bad Request")
                        .message(ex.getMessage())
                        .status(400)
                        .atType("Error")
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TmfErrorDto> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(TmfErrorDto.builder()
                        .code("500")
                        .reason("Internal Server Error")
                        .message("An unexpected error occurred.")
                        .status(500)
                        .atType("Error")
                        .build());
    }
}

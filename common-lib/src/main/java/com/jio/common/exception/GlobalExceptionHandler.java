package com.jio.common.exception;

import com.jio.common.model.ErrorRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.OffsetDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRepresentation> handleAllExceptions(Exception ex) {
        ErrorRepresentation error = ErrorRepresentation.builder()
                .code("500")
                .reason("Internal Server Error")
                .message(ex.getMessage())
                .status("500")
                .timestamp(OffsetDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRepresentation> handleBadRequest(IllegalArgumentException ex) {
        ErrorRepresentation error = ErrorRepresentation.builder()
                .code("400")
                .reason("Bad Request")
                .message(ex.getMessage())
                .status("400")
                .timestamp(OffsetDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

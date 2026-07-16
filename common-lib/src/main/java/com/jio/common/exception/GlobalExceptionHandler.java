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
        ErrorRepresentation error = new ErrorRepresentation(
                "500",
                "Internal Server Error",
                ex.getMessage(),
                "500",
                null,
                OffsetDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRepresentation> handleBadRequest(IllegalArgumentException ex) {
        ErrorRepresentation error = new ErrorRepresentation(
                "400",
                "Bad Request",
                ex.getMessage(),
                "400",
                null,
                OffsetDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

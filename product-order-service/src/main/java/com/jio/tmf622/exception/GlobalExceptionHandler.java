package com.jio.tmf622.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// @RestControllerAdvice — applies to ALL @RestController classes in the application.
// Any exception that escapes a controller method is caught here before Spring
// converts it to a generic 500 response.
//
// Without this class:
//   InvalidStateTransitionException → 500 Internal Server Error (wrong)
//
// With this class:
//   InvalidStateTransitionException → 422 Unprocessable Entity  (correct)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler tells Spring: "when this exception type bubbles up
    // from any controller, run this method instead of the default error page."
    //
    // Returns a plain JSON object:
    //   {
    //     "error": "Invalid state transition",
    //     "message": "Transition from ACKNOWLEDGED to COMPLETED is not permitted."
    //   }
    //
    // 422 Unprocessable Entity:
    //   The HTTP spec defines 422 as "the server understands the content type
    //   and the syntax is correct, but the server was unable to process the
    //   contained instructions." A state machine violation fits this exactly —
    //   the JSON was valid, but the business rule rejected it.
    @ExceptionHandler(InvalidStateTransitionException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTransition(
            InvalidStateTransitionException ex) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                        "error",   "Invalid state transition",
                        "message", ex.getMessage()
                ));
    }
}

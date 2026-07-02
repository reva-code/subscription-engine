package com.subscriptionengine.productusage.api.exception;

import com.subscriptionengine.productusage.domain.exception.ActiveSubscriptionValidationException;
import com.subscriptionengine.productusage.domain.exception.PlanLimitExceededException;
import com.subscriptionengine.productusage.domain.exception.UsageValidationException;
import org.openapitools.model.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ActiveSubscriptionValidationException.class)
    public ResponseEntity<Error> handleActiveSubscriptionValidationException(ActiveSubscriptionValidationException ex) {
        Error error = new Error();
        error.setCode("400");
        error.setReason("Active Subscription Validation Failed");
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlanLimitExceededException.class)
    public ResponseEntity<Error> handlePlanLimitExceededException(PlanLimitExceededException ex) {
        Error error = new Error();
        error.setCode("403"); // Forbidden / limit exceeded
        error.setReason("Plan Limit Exceeded");
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsageValidationException.class)
    public ResponseEntity<Error> handleUsageValidationException(UsageValidationException ex) {
        Error error = new Error();
        error.setCode("400");
        error.setReason("Validation Error");
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}

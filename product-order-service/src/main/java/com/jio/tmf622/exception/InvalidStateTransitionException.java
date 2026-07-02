package com.jio.tmf622.exception;

import com.jio.tmf622.model.ProductOrderStateType;

// Thrown when a caller requests a state transition that is not permitted
// by the TMF622 order lifecycle rules.
//
// Extends RuntimeException — unchecked — so callers don't have to declare
// "throws" on every method. Spring's exception-handling machinery catches it.
public class InvalidStateTransitionException extends RuntimeException {

    private final ProductOrderStateType from;
    private final ProductOrderStateType to;

    public InvalidStateTransitionException(ProductOrderStateType from, ProductOrderStateType to) {
        super("Transition from " + from + " to " + to + " is not permitted.");
        this.from = from;
        this.to = to;
    }

    public ProductOrderStateType getFrom() {
        return from;
    }

    public ProductOrderStateType getTo() {
        return to;
    }
}

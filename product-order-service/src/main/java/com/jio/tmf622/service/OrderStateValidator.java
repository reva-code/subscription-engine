package com.jio.tmf622.service;

import com.jio.tmf622.exception.InvalidStateTransitionException;
import com.jio.tmf622.model.ProductOrderStateType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

// @Component — a generic Spring-managed bean.
// Not @Service because this class holds no business logic of its own;
// it is a pure rule table consumed by ProductOrderService.
@Component
public class OrderStateValidator {

    // Allowed transitions table.
    //
    // Key   = the state the order is currently in (FROM state).
    // Value = the set of states it is allowed to move to (TO states).
    //
    // EnumMap and EnumSet are used instead of HashMap/HashSet because:
    //   - They are backed by arrays, not hash buckets — O(1) lookup with
    //     zero hash-collision risk.
    //   - They preserve declaration order (useful for error messages).
    //
    // TMF622-compliant transitions implemented for this sprint:
    //
    //   ACKNOWLEDGED → IN_PROGRESS   (system picks up the order for fulfilment)
    //   ACKNOWLEDGED → CANCELLED     (customer cancels before fulfilment starts)
    //   IN_PROGRESS  → COMPLETED     (fulfilment finished successfully)
    //   IN_PROGRESS  → FAILED        (fulfilment failed unrecoverably)
    //
    // States with no entry (COMPLETED, FAILED, CANCELLED, REJECTED) are
    // terminal — the order lifecycle has ended and no further transitions
    // are allowed.
    private static final Map<ProductOrderStateType, Set<ProductOrderStateType>> ALLOWED_TRANSITIONS;

    static {
        ALLOWED_TRANSITIONS = new EnumMap<>(ProductOrderStateType.class);

        ALLOWED_TRANSITIONS.put(
            ProductOrderStateType.ACKNOWLEDGED,
            EnumSet.of(
                ProductOrderStateType.IN_PROGRESS,
                ProductOrderStateType.CANCELLED
            )
        );

        ALLOWED_TRANSITIONS.put(
            ProductOrderStateType.IN_PROGRESS,
            EnumSet.of(
                ProductOrderStateType.COMPLETED,
                ProductOrderStateType.FAILED
            )
        );
    }

    // Validates a requested state transition.
    //
    // Parameters:
    //   from — the order's current state (loaded from DB)
    //   to   — the state the caller wants to move to (from the PATCH request)
    //
    // Throws InvalidStateTransitionException if the move is not in the table.
    // Returns normally (void) if the move is allowed — callers don't need to
    // check a return value, they just catch the exception.
    public void validate(ProductOrderStateType from, ProductOrderStateType to) {

        // Look up which states are reachable from "from".
        Set<ProductOrderStateType> allowed = ALLOWED_TRANSITIONS.get(from);

        // allowed == null  → "from" is a terminal state with no outgoing transitions.
        // !allowed.contains(to) → "from" has transitions, but not to "to".
        // Both cases are illegal.
        if (allowed == null || !allowed.contains(to)) {
            throw new InvalidStateTransitionException(from, to);
        }
    }
}

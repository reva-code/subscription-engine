package com.jio.productinventory.validation;

import com.jio.productinventory.entity.ProductStatusType;
import com.jio.productinventory.exception.InvalidStatusTransitionException;

import java.util.*;

/**
 * Enforces TMF637 product lifecycle state transitions.
 * Terminal states (terminated, cancelled, aborted) cannot be exited.
 */
public final class ProductStatusMachine {

    private static final Map<ProductStatusType, Set<ProductStatusType>> ALLOWED;

    static {
        ALLOWED = new EnumMap<>(ProductStatusType.class);
        ALLOWED.put(ProductStatusType.created,          EnumSet.of(
                ProductStatusType.pendingActive, ProductStatusType.cancelled));
        ALLOWED.put(ProductStatusType.pendingActive,    EnumSet.of(
                ProductStatusType.active, ProductStatusType.cancelled, ProductStatusType.aborted));
        ALLOWED.put(ProductStatusType.active,           EnumSet.of(
                ProductStatusType.suspended, ProductStatusType.pendingTerminate));
        ALLOWED.put(ProductStatusType.suspended,        EnumSet.of(
                ProductStatusType.active, ProductStatusType.pendingTerminate, ProductStatusType.terminated));
        ALLOWED.put(ProductStatusType.pendingTerminate, EnumSet.of(
                ProductStatusType.terminated, ProductStatusType.active));
        ALLOWED.put(ProductStatusType.terminated,       Collections.emptySet());
        ALLOWED.put(ProductStatusType.cancelled,        Collections.emptySet());
        ALLOWED.put(ProductStatusType.aborted,          Collections.emptySet());
    }

    private ProductStatusMachine() {}

    public static void validate(ProductStatusType from, ProductStatusType to) {
        Set<ProductStatusType> allowed = ALLOWED.getOrDefault(from, Collections.emptySet());
        if (!allowed.contains(to)) {
            throw new InvalidStatusTransitionException(
                    String.format("Transition from '%s' to '%s' is not permitted.", from, to));
        }
    }

    public static boolean isTerminal(ProductStatusType status) {
        return ALLOWED.get(status) != null && ALLOWED.get(status).isEmpty();
    }
}

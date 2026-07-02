package com.jio.productinventory.entity;

/**
 * TMF637 §ProductStatusType lifecycle enumeration.
 * Transitions are enforced in ProductStatusMachine.
 */
public enum ProductStatusType {
    created,
    pendingActive,
    cancelled,
    active,
    pendingTerminate,
    terminated,
    suspended,
    aborted
}

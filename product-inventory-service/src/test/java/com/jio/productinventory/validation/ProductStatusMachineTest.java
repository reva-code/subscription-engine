package com.jio.productinventory.validation;

import com.jio.productinventory.entity.ProductStatusType;
import com.jio.productinventory.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class ProductStatusMachineTest {

    @ParameterizedTest(name = "{0} -> {1} should be ALLOWED")
    @CsvSource({
        "created,pendingActive",
        "created,cancelled",
        "pendingActive,active",
        "pendingActive,cancelled",
        "pendingActive,aborted",
        "active,suspended",
        "active,pendingTerminate",
        "suspended,active",
        "suspended,terminated",
        "pendingTerminate,terminated",
        "pendingTerminate,active"
    })
    void allowedTransitions(String from, String to) {
        assertThatCode(() ->
                ProductStatusMachine.validate(
                        ProductStatusType.valueOf(from),
                        ProductStatusType.valueOf(to)))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "{0} -> {1} should be REJECTED")
    @CsvSource({
        "terminated,active",
        "cancelled,active",
        "aborted,active",
        "active,created",
        "terminated,created"
    })
    void rejectedTransitions(String from, String to) {
        assertThatThrownBy(() ->
                ProductStatusMachine.validate(
                        ProductStatusType.valueOf(from),
                        ProductStatusType.valueOf(to)))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void terminalStates_areIdentifiedCorrectly() {
        assertThat(ProductStatusMachine.isTerminal(ProductStatusType.terminated)).isTrue();
        assertThat(ProductStatusMachine.isTerminal(ProductStatusType.cancelled)).isTrue();
        assertThat(ProductStatusMachine.isTerminal(ProductStatusType.aborted)).isTrue();
        assertThat(ProductStatusMachine.isTerminal(ProductStatusType.active)).isFalse();
    }
}

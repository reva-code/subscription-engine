package com.subscriptionengine.communication.adapter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SmsSenderAdapterTest {

    private final SmsSenderAdapter smsSenderAdapter = new LoggingSmsSenderAdapter();

    @Test
    public void testSendSms() {
        assertDoesNotThrow(() -> smsSenderAdapter.sendSms("+1234567890", "Hello Test SMS"));
    }
}

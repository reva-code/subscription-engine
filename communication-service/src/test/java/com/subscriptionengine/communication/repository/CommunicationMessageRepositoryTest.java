package com.subscriptionengine.communication.repository;

import com.subscriptionengine.communication.entity.CommunicationMessage;
import com.subscriptionengine.communication.entity.CommunicationStatus;
import com.subscriptionengine.communication.entity.CommunicationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommunicationMessageRepositoryTest {

    @Autowired
    private CommunicationMessageRepository repository;

    @Test
    public void testSaveAndFindById() {
        CommunicationMessage message = CommunicationMessage.builder()
                .communicationType(CommunicationType.EMAIL)
                .receiverEmail("user@example.com")
                .customerId("CUST-777")
                .subject("Welcome")
                .messageBody("Hello test")
                .status(CommunicationStatus.PENDING)
                .build();

        CommunicationMessage saved = repository.save(message);

        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());

        Optional<CommunicationMessage> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("CUST-777", found.get().getCustomerId());
        assertEquals(CommunicationStatus.PENDING, found.get().getStatus());
    }

    @Test
    public void testFindByCustomerId() {
        CommunicationMessage msg1 = CommunicationMessage.builder()
                .communicationType(CommunicationType.EMAIL)
                .customerId("CUST-999")
                .status(CommunicationStatus.SENT)
                .build();

        CommunicationMessage msg2 = CommunicationMessage.builder()
                .communicationType(CommunicationType.SMS)
                .customerId("CUST-999")
                .status(CommunicationStatus.FAILED)
                .build();

        repository.save(msg1);
        repository.save(msg2);

        List<CommunicationMessage> list = repository.findByCustomerId("CUST-999");
        assertEquals(2, list.size());
    }
}

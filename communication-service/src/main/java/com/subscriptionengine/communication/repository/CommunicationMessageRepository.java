package com.subscriptionengine.communication.repository;

import com.subscriptionengine.communication.entity.CommunicationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommunicationMessageRepository extends JpaRepository<CommunicationMessage, String> {
    List<CommunicationMessage> findByCustomerId(String customerId);
}

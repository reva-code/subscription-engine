package com.jio.productinventory.repository;

import com.jio.productinventory.entity.EventSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, String> {
}

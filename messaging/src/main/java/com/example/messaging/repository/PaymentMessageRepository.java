package com.example.messaging.repository;

import com.example.messaging.model.PaymentMessageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository de persistance des messages bancaires.
 */
public interface PaymentMessageRepository extends JpaRepository<PaymentMessageEntity, UUID> {
}


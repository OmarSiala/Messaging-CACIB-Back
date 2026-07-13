package com.example.messaging.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.messaging.model.MessageStatus;
import com.example.messaging.model.PaymentMessageEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class PaymentMessageRepositoryTest {

    @Autowired
    private PaymentMessageRepository repository;

    @Test
    void shouldPersistAndReadMessage() {
        PaymentMessageEntity entity = new PaymentMessageEntity();
        entity.setMqMessageId("ID:repo");
        entity.setCorrelationId("CORR-repo");
        entity.setSourceQueue("DEV.QUEUE.1");
        entity.setPayload("{\"amount\":120.5}");
        entity.setStatus(MessageStatus.RECEIVED);
        entity.setReceivedAt(Instant.now());

        PaymentMessageEntity saved = repository.save(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }
}


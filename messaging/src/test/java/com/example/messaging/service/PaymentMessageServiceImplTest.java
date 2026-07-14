package com.example.messaging.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.exceptions.MessageNotFoundException;
import com.example.messaging.factory.PaymentMessageFactory;
import com.example.messaging.mapper.PaymentMessageMapper;
import com.example.messaging.model.MessageStatus;
import com.example.messaging.model.PaymentMessageEntity;
import com.example.messaging.mq.InboundMqMessage;
import com.example.messaging.repository.PaymentMessageRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.example.messaging.service.impl.PaymentMessageServiceImpl;
import org.mapstruct.factory.Mappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class PaymentMessageServiceImplTest {

    @Mock
    private PaymentMessageRepository repository;

    private PaymentMessageFactory factory;
    private PaymentMessageMapper mapper;

    private PaymentMessageService service;

    @BeforeEach
    void setUp() {
        factory = new PaymentMessageFactory();
        mapper = Mappers.getMapper(PaymentMessageMapper.class);
        service = new PaymentMessageServiceImpl(repository, factory, mapper);
    }

    @Test
    void shouldSaveIncomingMessage() {
        InboundMqMessage inbound = new InboundMqMessage("ID:123", "CORR", "Q1", "payload", Instant.now());
        PaymentMessageEntity persisted = factory.buildReceived(inbound);
        when(repository.save(org.mockito.ArgumentMatchers.any(PaymentMessageEntity.class))).thenAnswer(invocation -> {
            PaymentMessageEntity entity = invocation.getArgument(0);
            makePersistentLikeEntity(entity, UUID.randomUUID());
            return entity;
        });

        PaymentMessageResponse result = service.saveIncomingMessage(inbound);

        assertThat(result.getMqMessageId()).isEqualTo(persisted.getMqMessageId());
        assertThat(result.getStatus()).isEqualTo(MessageStatus.RECEIVED);
    }

    @Test
    void shouldReturnMessageById() {
        UUID id = UUID.randomUUID();
        PaymentMessageEntity entity = factory.buildReceived(new InboundMqMessage("ID:123", "CORR", "Q1", "payload", Instant.now()));
        makePersistentLikeEntity(entity, id);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        assertThat(service.getById(id).getId()).isEqualTo(id);
    }

    @Test
    void shouldThrowWhenMessageNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
            .isInstanceOf(MessageNotFoundException.class)
            .hasMessageContaining(id.toString());
    }

    @Test
    void shouldListMessages() {
        PaymentMessageEntity entity = factory.buildReceived(new InboundMqMessage("ID:123", "CORR", "Q1", "payload", Instant.now()));
        makePersistentLikeEntity(entity, UUID.randomUUID());

        when(repository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(java.util.List.of(entity)));

        assertThat(service.getMessages(PageRequest.of(0, 10)).getContent())
            .hasSize(1)
            .first()
            .extracting(PaymentMessageResponse::getMqMessageId)
            .isEqualTo("ID:123");
    }

    private void makePersistentLikeEntity(PaymentMessageEntity entity, UUID id) {
        try {
            java.lang.reflect.Field idField = PaymentMessageEntity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
            java.lang.reflect.Field createdAtField = PaymentMessageEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(entity, Instant.now());
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }
}


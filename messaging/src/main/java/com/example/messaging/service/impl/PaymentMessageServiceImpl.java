package com.example.messaging.service.impl;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.exceptions.MessageNotFoundException;
import com.example.messaging.factory.PaymentMessageFactory;
import com.example.messaging.mapper.PaymentMessageMapper;
import com.example.messaging.model.PaymentMessageEntity;
import com.example.messaging.mq.InboundMqMessage;
import com.example.messaging.repository.PaymentMessageRepository;

import java.util.Objects;
import java.util.UUID;

import com.example.messaging.service.PaymentMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du Service Layer des messages.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentMessageServiceImpl implements PaymentMessageService {

    private final PaymentMessageRepository repository;
    private final PaymentMessageFactory factory;
    private final PaymentMessageMapper mapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PaymentMessageResponse saveIncomingMessage(InboundMqMessage inbound) {
        log.info("Persistance d'un message entrant. mqMessageId={} correlationId={} sourceQueue={}", inbound.getMqMessageId(), inbound.getCorrelationId(), inbound.getSourceQueue());
        Objects.requireNonNull(inbound, "Le message inbound ne peut pas être null");
        PaymentMessageEntity entity = factory.buildReceived(inbound);
        PaymentMessageEntity saved = repository.save(entity);
        log.debug("Message persisted with id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentMessageResponse getById(UUID id) {
        PaymentMessageEntity entity = repository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentMessageResponse> getMessages(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

}


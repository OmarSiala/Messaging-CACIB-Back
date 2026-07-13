package com.example.messaging.service;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.factory.PaymentMessageFactory;
import com.example.messaging.mapper.PaymentMessageMapper;
import com.example.messaging.model.PaymentMessageEntity;
import com.example.messaging.mq.InboundMqMessage;
import com.example.messaging.repository.PaymentMessageRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du Service Layer des messages.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentMessageServiceImpl implements PaymentMessageService {

    private final PaymentMessageRepository repository;
    private final PaymentMessageFactory factory;
    private final PaymentMessageMapper mapper;


    @Override
    public PaymentMessageResponse saveIncomingMessage(InboundMqMessage inbound) {
        PaymentMessageEntity entity = factory.buildReceived(inbound);
        PaymentMessageEntity saved = repository.save(entity);
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


package com.example.messaging.mapper;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.model.PaymentMessageEntity;
import org.mapstruct.Mapper;

/**
 * Mapper entre entité de persistance et DTO exposé en REST.
 */
@Mapper(componentModel = "spring")
public interface PaymentMessageMapper {

    /**
     * Convertit une entité message en DTO de réponse API.
     *
     * @param entity message persistant
     * @return DTO REST
     */
    PaymentMessageResponse toResponse(PaymentMessageEntity entity);
}


package com.example.messaging.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.exceptions.MessageNotFoundException;
import com.example.messaging.exceptions.RestExceptionHandler;
import com.example.messaging.model.MessageStatus;
import com.example.messaging.service.PaymentMessageService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PaymentMessageController.class)
@Import(RestExceptionHandler.class)
class PaymentMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentMessageService service;

    @Test
    void shouldReturnPagedMessages() throws Exception {
        UUID id = UUID.randomUUID();
        PaymentMessageResponse response = new PaymentMessageResponse(
                id,
                "ID:1",
                "CORR",
                "DEV.QUEUE.1",
                "payload",
                MessageStatus.RECEIVED,
                Instant.now(),
                Instant.now()
        );

        when(service.getMessages(org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/v1/messages").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(id.toString()))
                .andExpect(jsonPath("$.content[0].mqMessageId").value("ID:1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldReturn404WhenMessageNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getById(id)).thenThrow(new MessageNotFoundException(id));

        mockMvc.perform(get("/api/v1/messages/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").exists());
    }
}


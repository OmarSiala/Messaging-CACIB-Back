package com.example.messaging.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.messaging.dto.MqPushMessageResponse;
import com.example.messaging.service.MqMessagePublishException;
import com.example.messaging.service.MqMessagePublisherService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MqMessagePublisherController.class)
@Import(RestExceptionHandler.class)
class MqMessagePublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MqMessagePublisherService publisherService;

    @Test
    void shouldPushMessage() throws Exception {
        MqPushMessageResponse response = MqPushMessageResponse.builder()
            .queue("DEV.QUEUE.1")
            .correlationId("CORR-1")
            .sentAt(LocalDateTime.of(2026, 1, 1, 10, 0))
            .status("PUBLISHED")
            .build();

        when(publisherService.pushMessage(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/mq/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "payload": "message de test",
                      "correlationId": "CORR-1"
                    }
                    """))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.queue").value("DEV.QUEUE.1"))
            .andExpect(jsonPath("$.correlationId").value("CORR-1"))
            .andExpect(jsonPath("$.status").value("PUBLISHED"));
    }

    @Test
    void shouldReturnBadRequestWhenPayloadMissing() throws Exception {
        mockMvc.perform(post("/api/v1/mq/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "payload": ""
                    }
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.payload").value("Le payload est obligatoire"));
    }

    @Test
    void shouldReturnBadGatewayWhenMqPublishFails() throws Exception {
        when(publisherService.pushMessage(any()))
            .thenThrow(new MqMessagePublishException("DEV.QUEUE.1", new RuntimeException("mq down")));

        mockMvc.perform(post("/api/v1/mq/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "payload": "message de test"
                    }
                    """))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.error").value("Echec de publication vers la queue MQ DEV.QUEUE.1"));
    }
}


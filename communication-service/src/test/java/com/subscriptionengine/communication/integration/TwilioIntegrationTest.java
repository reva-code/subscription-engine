package com.subscriptionengine.communication.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.subscriptionengine.communication.entity.CommunicationMessage;
import com.subscriptionengine.communication.entity.CommunicationStatus;
import com.subscriptionengine.communication.entity.CommunicationType;
import com.subscriptionengine.communication.generated.model.CommunicationMessageCreate;
import com.subscriptionengine.communication.generated.model.Receiver;
import com.subscriptionengine.communication.generated.model.Sender;
import com.subscriptionengine.communication.dto.NotificationEvent;
import com.subscriptionengine.communication.repository.CommunicationMessageRepository;
import com.subscriptionengine.communication.service.TwilioSmsSenderService;
import com.subscriptionengine.communication.service.TwilioWhatsAppSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TwilioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommunicationMessageRepository repository;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private TwilioSmsSenderService twilioSmsSenderService;

    @MockBean
    private TwilioWhatsAppSenderService twilioWhatsAppSenderService;

    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        
        Session session = Session.getInstance(new Properties());
        mimeMessage = new MimeMessage(session);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void testPostEmailNotification_Success() throws Exception {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("EMAIL");
        createDto.setSubject("Integration Test Email");
        createDto.setContent("Email content for integration testing");

        Receiver receiver = new Receiver();
        receiver.setEmail("integration-email@example.com");
        createDto.setReceiver(Collections.singletonList(receiver));

        Sender sender = new Sender();
        sender.setName("Subscription Engine");
        sender.setEmail("no-reply@subscriptionengine.com");
        createDto.setSender(sender);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        mockMvc.perform(post("/tmf-api/communicationManagement/v4/communicationMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.messageType").value("EMAIL"));

        List<CommunicationMessage> dbMessages = repository.findAll();
        assertEquals(1, dbMessages.size());
        CommunicationMessage savedMsg = dbMessages.get(0);
        assertEquals(CommunicationType.EMAIL, savedMsg.getCommunicationType());
        assertEquals("SMTP", savedMsg.getProvider());
        assertEquals("SENT", savedMsg.getDeliveryStatus());
        assertNotNull(savedMsg.getDeliveryTimestamp());
    }

    @Test
    public void testPostSmsNotification_Success() throws Exception {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("SMS");
        createDto.setContent("Integration Test SMS");

        Receiver receiver = new Receiver();
        receiver.setPhoneNumber("+1234567890");
        createDto.setReceiver(Collections.singletonList(receiver));

        Sender sender = new Sender();
        sender.setName("Subscription Engine");
        sender.setPhoneNumber("100");
        createDto.setSender(sender);

        when(twilioSmsSenderService.sendSms(anyString(), anyString())).thenReturn("twilio-sid-12345");

        mockMvc.perform(post("/tmf-api/communicationManagement/v4/communicationMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.messageType").value("SMS"));

        List<CommunicationMessage> dbMessages = repository.findAll();
        assertEquals(1, dbMessages.size());
        CommunicationMessage savedMsg = dbMessages.get(0);
        assertEquals(CommunicationType.SMS, savedMsg.getCommunicationType());
        assertEquals("TWILIO_SMS", savedMsg.getProvider());
        assertEquals("twilio-sid-12345", savedMsg.getProviderMessageId());
        assertEquals("SENT", savedMsg.getDeliveryStatus());
        assertNotNull(savedMsg.getDeliveryTimestamp());
    }

    @Test
    public void testPostWhatsAppNotification_Success() throws Exception {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("WHATSAPP");
        createDto.setContent("Integration Test WhatsApp");

        Receiver receiver = new Receiver();
        receiver.setPhoneNumber("+1234567890");
        createDto.setReceiver(Collections.singletonList(receiver));

        Sender sender = new Sender();
        sender.setName("Subscription Engine");
        sender.setPhoneNumber("100");
        createDto.setSender(sender);

        when(twilioWhatsAppSenderService.sendWhatsApp(anyString(), anyString())).thenReturn("twilio-whatsapp-sid-67890");

        mockMvc.perform(post("/tmf-api/communicationManagement/v4/communicationMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.messageType").value("WHATSAPP"));

        List<CommunicationMessage> dbMessages = repository.findAll();
        assertEquals(1, dbMessages.size());
        CommunicationMessage savedMsg = dbMessages.get(0);
        assertEquals(CommunicationType.WHATSAPP, savedMsg.getCommunicationType());
        assertEquals("TWILIO_WHATSAPP", savedMsg.getProvider());
        assertEquals("twilio-whatsapp-sid-67890", savedMsg.getProviderMessageId());
        assertEquals("SENT", savedMsg.getDeliveryStatus());
        assertNotNull(savedMsg.getDeliveryTimestamp());
    }

    @Test
    public void testReceiveEvent_DispatchesAllChannels() throws Exception {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("evt-999")
                .eventType("SUBSCRIPTION_CREATED")
                .customerId("CUST-999")
                .receiverEmail("cust-email@example.com")
                .receiverMobile("+919999999999")
                .templateParameters(new HashMap<>() {{
                    put("customerName", "John Doe");
                    put("subscriptionId", "SUB-123");
                    put("date", "2026-06-17");
                }})
                .build();

        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        when(twilioSmsSenderService.sendSms(anyString(), anyString())).thenReturn("event-sms-sid");
        when(twilioWhatsAppSenderService.sendWhatsApp(anyString(), anyString())).thenReturn("event-whatsapp-sid");

        mockMvc.perform(post("/tmf-api/communicationManagement/v4/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isAccepted());

        List<CommunicationMessage> dbMessages = repository.findAll();
        // Should have 3 messages: 1 Email, 1 SMS, 1 WhatsApp
        assertEquals(3, dbMessages.size());

        long emailCount = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.EMAIL).count();
        long smsCount = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.SMS).count();
        long whatsappCount = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.WHATSAPP).count();

        assertEquals(1, emailCount);
        assertEquals(1, smsCount);
        assertEquals(1, whatsappCount);

        CommunicationMessage emailMsg = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.EMAIL).findFirst().get();
        assertEquals("SMTP", emailMsg.getProvider());
        assertEquals("SENT", emailMsg.getDeliveryStatus());

        CommunicationMessage smsMsg = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.SMS).findFirst().get();
        assertEquals("TWILIO_SMS", smsMsg.getProvider());
        assertEquals("event-sms-sid", smsMsg.getProviderMessageId());
        assertEquals("SENT", smsMsg.getDeliveryStatus());

        CommunicationMessage whatsappMsg = dbMessages.stream().filter(m -> m.getCommunicationType() == CommunicationType.WHATSAPP).findFirst().get();
        assertEquals("TWILIO_WHATSAPP", whatsappMsg.getProvider());
        assertEquals("event-whatsapp-sid", whatsappMsg.getProviderMessageId());
        assertEquals("SENT", whatsappMsg.getDeliveryStatus());
    }
}

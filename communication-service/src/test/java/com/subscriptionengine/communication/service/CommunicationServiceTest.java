package com.subscriptionengine.communication.service;

import com.subscriptionengine.communication.adapter.EmailSenderAdapter;
import com.subscriptionengine.communication.adapter.SmsSenderAdapter;
import com.subscriptionengine.communication.dto.NotificationEvent;
import com.subscriptionengine.communication.entity.CommunicationMessage;
import com.subscriptionengine.communication.entity.CommunicationStatus;
import com.subscriptionengine.communication.entity.CommunicationType;
import com.subscriptionengine.communication.generated.model.CommunicationMessageCreate;
import com.subscriptionengine.communication.generated.model.Receiver;
import com.subscriptionengine.communication.generated.model.RelatedParty;
import com.subscriptionengine.communication.repository.CommunicationMessageRepository;
import com.subscriptionengine.communication.template.TemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommunicationServiceTest {

    private CommunicationService service;

    @Mock
    private CommunicationMessageRepository repository;

    @Mock
    private EmailSenderAdapter emailSenderAdapter;

    @Mock
    private SmsSenderAdapter smsSenderAdapter;

    @Mock
    private TwilioSmsSenderService twilioSmsSenderService;

    @Mock
    private TwilioWhatsAppSenderService twilioWhatsAppSenderService;

    private TemplateService templateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        templateService = new TemplateService();
        service = new CommunicationService(repository, emailSenderAdapter, smsSenderAdapter, templateService, twilioSmsSenderService, twilioWhatsAppSenderService);
    }

    @Test
    public void testCreateAndSendMessage_Email_Success() {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("EMAIL");
        createDto.setSubject("Test Email Subject");
        createDto.setContent("Test Content");

        Receiver receiver = new Receiver();
        receiver.setEmail("customer@example.com");
        RelatedParty party = new RelatedParty();
        party.setId("CUST-001");
        receiver.setParty(party);
        createDto.setReceiver(Collections.singletonList(receiver));

        CommunicationMessage initialEntity = CommunicationMessage.builder()
                .id("some-uuid")
                .communicationType(CommunicationType.EMAIL)
                .receiverEmail("customer@example.com")
                .customerId("CUST-001")
                .subject("Test Email Subject")
                .messageBody("Test Content")
                .status(CommunicationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(CommunicationMessage.class))).thenReturn(initialEntity);
        doNothing().when(emailSenderAdapter).sendEmail(anyString(), anyString(), anyString());

        com.subscriptionengine.communication.generated.model.CommunicationMessage response = service.createAndSendMessage(createDto);

        assertNotNull(response);
        assertEquals("some-uuid", response.getId());
        verify(emailSenderAdapter, times(1)).sendEmail("customer@example.com", "Test Email Subject", "Test Content");
        
        ArgumentCaptor<CommunicationMessage> captor = ArgumentCaptor.forClass(CommunicationMessage.class);
        verify(repository, times(2)).save(captor.capture());
        
        List<CommunicationMessage> savedEntities = captor.getAllValues();
        CommunicationMessage finalSaved = savedEntities.get(savedEntities.size() - 1);
        assertEquals(CommunicationStatus.SENT, finalSaved.getStatus());
        assertEquals("SMTP", finalSaved.getProvider());
        assertEquals("SENT", finalSaved.getDeliveryStatus());
    }

    @Test
    public void testCreateAndSendMessage_Sms_Success() {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("SMS");
        createDto.setContent("Test SMS Content");

        Receiver receiver = new Receiver();
        receiver.setPhoneNumber("+12345678");
        createDto.setReceiver(Collections.singletonList(receiver));

        CommunicationMessage initialEntity = CommunicationMessage.builder()
                .id("sms-uuid")
                .communicationType(CommunicationType.SMS)
                .receiverMobile("+12345678")
                .messageBody("Test SMS Content")
                .status(CommunicationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(CommunicationMessage.class))).thenReturn(initialEntity);
        when(twilioSmsSenderService.sendSms(anyString(), anyString())).thenReturn("mock-sid-sms");

        com.subscriptionengine.communication.generated.model.CommunicationMessage response = service.createAndSendMessage(createDto);

        assertNotNull(response);
        verify(twilioSmsSenderService, times(1)).sendSms("+12345678", "Test SMS Content");
        
        ArgumentCaptor<CommunicationMessage> captor = ArgumentCaptor.forClass(CommunicationMessage.class);
        verify(repository, times(2)).save(captor.capture());
        
        List<CommunicationMessage> savedEntities = captor.getAllValues();
        CommunicationMessage finalSaved = savedEntities.get(savedEntities.size() - 1);
        assertEquals(CommunicationStatus.SENT, finalSaved.getStatus());
        assertEquals("TWILIO_SMS", finalSaved.getProvider());
        assertEquals("mock-sid-sms", finalSaved.getProviderMessageId());
        assertEquals("SENT", finalSaved.getDeliveryStatus());
        assertNotNull(finalSaved.getDeliveryTimestamp());
    }

    @Test
    public void testCreateAndSendMessage_WhatsApp_Success() {
        CommunicationMessageCreate createDto = new CommunicationMessageCreate();
        createDto.setMessageType("WHATSAPP");
        createDto.setContent("Test WhatsApp Content");

        Receiver receiver = new Receiver();
        receiver.setPhoneNumber("+12345678");
        createDto.setReceiver(Collections.singletonList(receiver));

        CommunicationMessage initialEntity = CommunicationMessage.builder()
                .id("whatsapp-uuid")
                .communicationType(CommunicationType.WHATSAPP)
                .receiverMobile("+12345678")
                .messageBody("Test WhatsApp Content")
                .status(CommunicationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(CommunicationMessage.class))).thenReturn(initialEntity);
        when(twilioWhatsAppSenderService.sendWhatsApp(anyString(), anyString())).thenReturn("mock-sid-whatsapp");

        com.subscriptionengine.communication.generated.model.CommunicationMessage response = service.createAndSendMessage(createDto);

        assertNotNull(response);
        verify(twilioWhatsAppSenderService, times(1)).sendWhatsApp("+12345678", "Test WhatsApp Content");
        
        ArgumentCaptor<CommunicationMessage> captor = ArgumentCaptor.forClass(CommunicationMessage.class);
        verify(repository, times(2)).save(captor.capture());
        
        List<CommunicationMessage> savedEntities = captor.getAllValues();
        CommunicationMessage finalSaved = savedEntities.get(savedEntities.size() - 1);
        assertEquals(CommunicationStatus.SENT, finalSaved.getStatus());
        assertEquals("TWILIO_WHATSAPP", finalSaved.getProvider());
        assertEquals("mock-sid-whatsapp", finalSaved.getProviderMessageId());
        assertEquals("SENT", finalSaved.getDeliveryStatus());
        assertNotNull(finalSaved.getDeliveryTimestamp());
    }

    @Test
    public void testProcessNotificationEvent() {
        NotificationEvent event = NotificationEvent.builder()
                .eventId("event-123")
                .eventType("PAYMENT_SUCCESS")
                .customerId("CUST-100")
                .receiverEmail("cust100@example.com")
                .receiverMobile("+919999999999")
                .templateParameters(new HashMap<>())
                .build();

        CommunicationMessage savedEntity = CommunicationMessage.builder()
                .id("uuid-abc")
                .build();
        when(repository.save(any(CommunicationMessage.class))).thenReturn(savedEntity);
        when(twilioSmsSenderService.sendSms(anyString(), anyString())).thenReturn("mock-sid-sms");
        when(twilioWhatsAppSenderService.sendWhatsApp(anyString(), anyString())).thenReturn("mock-sid-whatsapp");

        service.processNotificationEvent(event);

        verify(emailSenderAdapter, times(1)).sendEmail(eq("cust100@example.com"), eq("Payment Successful - Subscription Engine"), anyString());
        verify(twilioSmsSenderService, times(1)).sendSms(eq("+919999999999"), anyString());
        verify(twilioWhatsAppSenderService, times(1)).sendWhatsApp(eq("+919999999999"), anyString());
        verify(repository, atLeast(3)).save(any(CommunicationMessage.class));
    }
}

package com.subscriptionengine.communication.service;

import com.subscriptionengine.communication.adapter.EmailSenderAdapter;
import com.subscriptionengine.communication.adapter.SmsSenderAdapter;
import com.subscriptionengine.communication.entity.CommunicationMessage;
import com.subscriptionengine.communication.entity.CommunicationStatus;
import com.subscriptionengine.communication.entity.CommunicationType;
import com.subscriptionengine.communication.repository.CommunicationMessageRepository;
import com.subscriptionengine.communication.generated.model.*;
import com.subscriptionengine.communication.dto.NotificationEvent;
import com.subscriptionengine.communication.template.TemplateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommunicationService {

    private static final Logger log = LoggerFactory.getLogger(CommunicationService.class);

    private final CommunicationMessageRepository repository;
    private final EmailSenderAdapter emailSenderAdapter;
    private final SmsSenderAdapter smsSenderAdapter;
    private final TemplateService templateService;
    private final TwilioSmsSenderService twilioSmsSenderService;
    private final TwilioWhatsAppSenderService twilioWhatsAppSenderService;

    private static final String BASE_HREF = "/tmf-api/communicationManagement/v4/communicationMessage/";

    public CommunicationService(CommunicationMessageRepository repository,
                                EmailSenderAdapter emailSenderAdapter,
                                SmsSenderAdapter smsSenderAdapter,
                                TemplateService templateService,
                                TwilioSmsSenderService twilioSmsSenderService,
                                TwilioWhatsAppSenderService twilioWhatsAppSenderService) {
        this.repository = repository;
        this.emailSenderAdapter = emailSenderAdapter;
        this.smsSenderAdapter = smsSenderAdapter;
        this.templateService = templateService;
        this.twilioSmsSenderService = twilioSmsSenderService;
        this.twilioWhatsAppSenderService = twilioWhatsAppSenderService;
    }

    @Transactional
    public com.subscriptionengine.communication.generated.model.CommunicationMessage createAndSendMessage(CommunicationMessageCreate createDto) {
        log.info("Processing request to create and send communication message");

        // Parse communicationType: handles flat or TMF-compliant property
        String commTypeStr = createDto.getCommunicationType() != null ? createDto.getCommunicationType() : createDto.getMessageType();
        CommunicationType type = parseType(commTypeStr);
        
        String receiverEmail = null;
        String receiverMobile = createDto.getReceiverMobile();
        if (createDto.getReceiver() != null && !createDto.getReceiver().isEmpty()) {
            Receiver firstReceiver = createDto.getReceiver().get(0);
            receiverEmail = firstReceiver.getEmail();
            if (receiverMobile == null || receiverMobile.trim().isEmpty()) {
                receiverMobile = firstReceiver.getPhoneNumber();
            }
        }

        String customerId = null;
        if (createDto.getReceiver() != null && !createDto.getReceiver().isEmpty()) {
            Receiver firstReceiver = createDto.getReceiver().get(0);
            if (firstReceiver.getParty() != null) {
                customerId = firstReceiver.getParty().getId();
            }
        }

        String messageBody = createDto.getMessageBody();
        if (messageBody == null || messageBody.trim().isEmpty()) {
            messageBody = createDto.getContent();
        }

        CommunicationMessage entity = CommunicationMessage.builder()
                .communicationType(type)
                .receiverEmail(receiverEmail)
                .receiverMobile(receiverMobile)
                .customerId(customerId)
                .subject(createDto.getSubject())
                .messageBody(messageBody)
                .status(CommunicationStatus.PENDING)
                .build();

        // Save initial record
        entity = repository.save(entity);

        // Send notification & route based on type
        try {
            if (type == CommunicationType.EMAIL) {
                if (receiverEmail == null || receiverEmail.trim().isEmpty()) {
                    throw new IllegalArgumentException("Email address is required for EMAIL communication type");
                }
                emailSenderAdapter.sendEmail(receiverEmail, entity.getSubject(), entity.getMessageBody());
                entity.setProvider("SMTP");
                entity.setProviderMessageId(null);
            } else if (type == CommunicationType.SMS) {
                if (receiverMobile == null || receiverMobile.trim().isEmpty()) {
                    throw new IllegalArgumentException("Phone number is required for SMS communication type");
                }
                // Try Twilio SMS
                String twilioSid = twilioSmsSenderService.sendSms(receiverMobile, entity.getMessageBody());
                entity.setProvider("TWILIO_SMS");
                entity.setProviderMessageId(twilioSid);
            } else if (type == CommunicationType.WHATSAPP) {
                if (receiverMobile == null || receiverMobile.trim().isEmpty()) {
                    throw new IllegalArgumentException("Phone number is required for WHATSAPP communication type");
                }
                // Try Twilio WhatsApp
                String twilioSid = twilioWhatsAppSenderService.sendWhatsApp(receiverMobile, entity.getMessageBody());
                entity.setProvider("TWILIO_WHATSAPP");
                entity.setProviderMessageId(twilioSid);
            }

            entity.setStatus(CommunicationStatus.SENT);
            entity.setDeliveryStatus("SENT");
            entity.setDeliveryTimestamp(java.time.LocalDateTime.now());
        } catch (Exception e) {
            log.error("Failed to send message: {}", e.getMessage());
            entity.setStatus(CommunicationStatus.FAILED);
            entity.setDeliveryStatus("FAILED");
            entity.setDeliveryTimestamp(java.time.LocalDateTime.now());
            if (type == CommunicationType.SMS) {
                entity.setProvider("TWILIO_SMS");
            } else if (type == CommunicationType.WHATSAPP) {
                entity.setProvider("TWILIO_WHATSAPP");
            } else {
                entity.setProvider("SMTP");
            }
        }

        // Save final status
        entity = repository.save(entity);

        return mapToTmfModel(entity);
    }

    public com.subscriptionengine.communication.generated.model.CommunicationMessage getMessageById(String id) {
        CommunicationMessage entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Communication message not found with ID: " + id));
        return mapToTmfModel(entity);
    }

    public List<com.subscriptionengine.communication.generated.model.CommunicationMessage> getAllMessages() {
        return repository.findAll().stream()
                .map(this::mapToTmfModel)
                .collect(Collectors.toList());
    }

    public List<com.subscriptionengine.communication.generated.model.CommunicationMessage> getMessagesByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(this::mapToTmfModel)
                .collect(Collectors.toList());
    }

    @Transactional
    public void processNotificationEvent(NotificationEvent event) {
        log.info("Received event: {} for customerId: {}", event.getEventType(), event.getCustomerId());

        String templateName = event.getEventType();
        Map<String, String> vars = event.getTemplateParameters();

        // Send Email if email is present
        if (event.getReceiverEmail() != null && !event.getReceiverEmail().trim().isEmpty()) {
            try {
                String subject = templateService.getEmailSubject(templateName);
                String body = templateService.renderEmail(templateName, vars);

                CommunicationMessage emailRecord = CommunicationMessage.builder()
                        .communicationType(CommunicationType.EMAIL)
                        .receiverEmail(event.getReceiverEmail())
                        .customerId(event.getCustomerId())
                        .subject(subject)
                        .messageBody(body)
                        .status(CommunicationStatus.PENDING)
                        .messageReference(event.getEventId())
                        .build();

                emailRecord = repository.save(emailRecord);

                try {
                    emailSenderAdapter.sendEmail(event.getReceiverEmail(), subject, body);
                    emailRecord.setStatus(CommunicationStatus.SENT);
                    emailRecord.setProvider("SMTP");
                    emailRecord.setDeliveryStatus("SENT");
                    emailRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                } catch (Exception e) {
                    log.error("Failed to send event email: {}", e.getMessage());
                    emailRecord.setStatus(CommunicationStatus.FAILED);
                    emailRecord.setProvider("SMTP");
                    emailRecord.setDeliveryStatus("FAILED");
                    emailRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                }
                repository.save(emailRecord);

            } catch (Exception e) {
                log.error("Failed to process email template or record: {}", e.getMessage());
            }
        }

        // Send SMS if mobile is present
        if (event.getReceiverMobile() != null && !event.getReceiverMobile().trim().isEmpty()) {
            try {
                String body = templateService.renderSms(templateName, vars);

                CommunicationMessage smsRecord = CommunicationMessage.builder()
                        .communicationType(CommunicationType.SMS)
                        .receiverMobile(event.getReceiverMobile())
                        .customerId(event.getCustomerId())
                        .messageBody(body)
                        .status(CommunicationStatus.PENDING)
                        .messageReference(event.getEventId())
                        .build();

                smsRecord = repository.save(smsRecord);

                try {
                    String sid = twilioSmsSenderService.sendSms(event.getReceiverMobile(), body);
                    smsRecord.setProvider("TWILIO_SMS");
                    smsRecord.setProviderMessageId(sid);
                    smsRecord.setDeliveryStatus("SENT");
                    smsRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                    smsRecord.setStatus(CommunicationStatus.SENT);
                } catch (Exception e) {
                    log.error("Failed to send event SMS: {}", e.getMessage());
                    smsRecord.setProvider("TWILIO_SMS");
                    smsRecord.setDeliveryStatus("FAILED");
                    smsRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                    smsRecord.setStatus(CommunicationStatus.FAILED);
                }
                repository.save(smsRecord);

            } catch (Exception e) {
                log.error("Failed to process SMS template or record: {}", e.getMessage());
            }
        }

        // Send WhatsApp if mobile is present
        if (event.getReceiverMobile() != null && !event.getReceiverMobile().trim().isEmpty()) {
            try {
                String body = templateService.renderSms(templateName, vars);

                CommunicationMessage whatsappRecord = CommunicationMessage.builder()
                        .communicationType(CommunicationType.WHATSAPP)
                        .receiverMobile(event.getReceiverMobile())
                        .customerId(event.getCustomerId())
                        .messageBody(body)
                        .status(CommunicationStatus.PENDING)
                        .messageReference(event.getEventId())
                        .build();

                whatsappRecord = repository.save(whatsappRecord);

                try {
                    String sid = twilioWhatsAppSenderService.sendWhatsApp(event.getReceiverMobile(), body);
                    whatsappRecord.setProvider("TWILIO_WHATSAPP");
                    whatsappRecord.setProviderMessageId(sid);
                    whatsappRecord.setDeliveryStatus("SENT");
                    whatsappRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                    whatsappRecord.setStatus(CommunicationStatus.SENT);
                } catch (Exception e) {
                    log.error("Failed to send event WhatsApp: {}", e.getMessage());
                    whatsappRecord.setProvider("TWILIO_WHATSAPP");
                    whatsappRecord.setDeliveryStatus("FAILED");
                    whatsappRecord.setDeliveryTimestamp(java.time.LocalDateTime.now());
                    whatsappRecord.setStatus(CommunicationStatus.FAILED);
                }
                repository.save(whatsappRecord);

            } catch (Exception e) {
                log.error("Failed to process WhatsApp template or record: {}", e.getMessage());
            }
        }
    }

    private CommunicationType parseType(String typeStr) {
        if (typeStr == null) return CommunicationType.EMAIL;
        if (typeStr.equalsIgnoreCase("SMS") || typeStr.equalsIgnoreCase("short message")) {
            return CommunicationType.SMS;
        }
        if (typeStr.equalsIgnoreCase("WHATSAPP") || typeStr.toLowerCase().contains("whatsapp")) {
            return CommunicationType.WHATSAPP;
        }
        return CommunicationType.EMAIL;
    }

    private com.subscriptionengine.communication.generated.model.CommunicationMessage mapToTmfModel(CommunicationMessage entity) {
        com.subscriptionengine.communication.generated.model.CommunicationMessage tmf = new com.subscriptionengine.communication.generated.model.CommunicationMessage();
        tmf.setId(entity.getId());
        tmf.setHref(BASE_HREF + entity.getId());
        tmf.setContent(entity.getMessageBody());
        tmf.setSubject(entity.getSubject());
        tmf.setMessageType(entity.getCommunicationType().name());
        tmf.setCommunicationType(entity.getCommunicationType().name());
        tmf.setReceiverMobile(entity.getReceiverMobile());
        tmf.setMessageBody(entity.getMessageBody());
        if (entity.getCreatedAt() != null) {
            tmf.setSendTime(entity.getCreatedAt().atOffset(ZoneOffset.UTC));
        }

        if (entity.getStatus() == CommunicationStatus.SENT) {
            tmf.setState(CommunicationMessageStateType.COMPLETED);
        } else if (entity.getStatus() == CommunicationStatus.FAILED) {
            tmf.setState(CommunicationMessageStateType.FAILED);
        } else {
            tmf.setState(CommunicationMessageStateType.INITIAL);
        }

        List<Receiver> receivers = new ArrayList<>();
        Receiver receiver = new Receiver();
        receiver.setEmail(entity.getReceiverEmail());
        receiver.setPhoneNumber(entity.getReceiverMobile());
        if (entity.getCustomerId() != null) {
            RelatedParty party = new RelatedParty();
            party.setId(entity.getCustomerId());
            party.setAtReferredType("Customer");
            receiver.setParty(party);
        }
        receivers.add(receiver);
        tmf.setReceiver(receivers);

        Sender sender = new Sender();
        sender.setName("Subscription Engine");
        if (entity.getCommunicationType() == CommunicationType.EMAIL) {
            sender.setEmail("no-reply@subscriptionengine.com");
        } else {
            sender.setPhoneNumber("100");
        }
        tmf.setSender(sender);

        return tmf;
    }
}

package com.subscriptionengine.communication.adapter;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EmailSenderAdapterTest {

    private EmailSenderAdapter emailSenderAdapter;

    @Mock
    private JavaMailSender javaMailSender;

    private MimeMessage mimeMessage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        emailSenderAdapter = new SmtpEmailSenderAdapter(javaMailSender);
        
        Session session = Session.getInstance(new Properties());
        mimeMessage = new MimeMessage(session);
        
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void testSendEmail_Success() {
        doNothing().when(javaMailSender).send(any(MimeMessage.class));

        assertDoesNotThrow(() -> emailSenderAdapter.sendEmail("test@example.com", "Test Subject", "<p>Hello</p>"));

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    public void testSendEmail_Failure() {
        doThrow(new RuntimeException("SMTP Connection failed")).when(javaMailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> 
            emailSenderAdapter.sendEmail("test@example.com", "Test Subject", "<p>Hello</p>")
        );

        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }
}

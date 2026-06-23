package com.subscriptionengine.communication.template;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemplateService {

    private final Map<String, String> emailTemplates = new HashMap<>();
    private final Map<String, String> smsTemplates = new HashMap<>();
    private final Map<String, String> emailSubjects = new HashMap<>();

    public TemplateService() {
        initTemplates();
    }

    private void initTemplates() {
        // --- Subject mappings ---
        emailSubjects.put("PAYMENT_SUCCESS", "Payment Successful - Subscription Engine");
        emailSubjects.put("PAYMENT_FAILURE", "Action Required: Payment Failed");
        emailSubjects.put("SUBSCRIPTION_ACTIVATED", "Welcome! Your Subscription is Now Active");
        emailSubjects.put("SUBSCRIPTION_RENEWAL", "Upcoming Renewal Notice");
        emailSubjects.put("BILL_GENERATED", "New Bill Generated");
        emailSubjects.put("SUBSCRIPTION_CREATED", "Subscription Created - Subscription Engine");
        emailSubjects.put("SUBSCRIPTION_TERMINATED", "Subscription Terminated - Subscription Engine");

        // --- EMAIL Templates (Styled HTML) ---
        emailTemplates.put("PAYMENT_SUCCESS", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #11998e, #38ef7d); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Payment Successful</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>We are pleased to inform you that your payment has been processed successfully. Below are the details:</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Amount Paid:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right; color: #11998e;\">{{amount}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Payment Reference:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{paymentId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Transaction Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">Thank you for choosing Subscription Engine!</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("PAYMENT_FAILURE", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #ff416c, #ff4b2b); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Payment Failed</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p style=\"color: #ff416c;\">Warning: We were unable to process your payment for the subscription.</p>" +
            "    <p>Please review the details below and update your payment method to avoid service interruption:</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Failed Amount:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right; color: #ff416c;\">{{amount}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Attempted Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px; text-align: center;\">" +
            "      <a href=\"#\" style=\"background-color: #ff416c; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;\">Update Payment Method</a>" +
            "    </p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("SUBSCRIPTION_ACTIVATED", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #00c6ff, #0072ff); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Subscription Activated!</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>Your subscription has been successfully activated! Thank you for choosing us.</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Activation Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">Your services are now fully operational. Enjoy your experience!</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("SUBSCRIPTION_RENEWAL", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #f857a6, #ff5858); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Upcoming Renewal Notice</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>This is a reminder that your subscription is scheduled for auto-renewal soon. Below are the details:</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Renewal Amount:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right; color: #ff5858;\">{{amount}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Renewal Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">No action is required from your side. The subscription will renew automatically using your saved payment details.</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("BILL_GENERATED", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #4b6cb7, #182848); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">New Bill Generated</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>A new bill has been generated for your subscription. Please review the details below:</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Bill ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{billId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Total Amount Due:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right; color: #4b6cb7;\">{{amount}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Due Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">Please ensure timely payment to maintain active service status.</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("SUBSCRIPTION_CREATED", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #00c6ff, #0072ff); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Subscription Created</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>Your subscription request has been received and created successfully.</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Creation Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">We will notify you once your services are activated.</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        emailTemplates.put("SUBSCRIPTION_TERMINATED", 
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.05);\">" +
            "  <div style=\"background: linear-gradient(135deg, #3a6073, #3a6073); padding: 20px; text-align: center; border-radius: 10px 10px 0 0; color: white;\">" +
            "    <h2 style=\"margin: 0;\">Subscription Terminated</h2>" +
            "  </div>" +
            "  <div style=\"padding: 20px; color: #333;\">" +
            "    <p>Dear <strong>{{customerName}}</strong>,</p>" +
            "    <p>Your subscription has been terminated.</p>" +
            "    <table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Subscription ID:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{subscriptionId}}</td></tr>" +
            "      <tr><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; color: #666;\">Termination Date:</td><td style=\"padding: 8px 0; border-bottom: 1px solid #eee; font-weight: bold; text-align: right;\">{{date}}</td></tr>" +
            "    </table>" +
            "    <p style=\"margin-top: 20px;\">If you believe this was an error or would like to re-subscribe, please contact customer support.</p>" +
            "  </div>" +
            "  <div style=\"background-color: #f9f9f9; padding: 15px; text-align: center; font-size: 12px; color: #999; border-radius: 0 0 10px 10px;\">" +
            "    This is an automated notification. Please do not reply directly to this email." +
            "  </div>" +
            "</div>");

        // --- SMS Templates ---
        smsTemplates.put("PAYMENT_SUCCESS", "Dear {{customerName}}, payment of {{amount}} for subscription {{subscriptionId}} was successful. Ref: {{paymentId}}.");
        smsTemplates.put("PAYMENT_FAILURE", "Dear {{customerName}}, payment of {{amount}} for subscription {{subscriptionId}} failed. Please update your payment method.");
        smsTemplates.put("SUBSCRIPTION_ACTIVATED", "Dear {{customerName}}, subscription {{subscriptionId}} is active! Enjoy your services.");
        smsTemplates.put("SUBSCRIPTION_RENEWAL", "Dear {{customerName}}, subscription {{subscriptionId}} is renewing on {{date}}. Amt: {{amount}}.");
        smsTemplates.put("BILL_GENERATED", "Dear {{customerName}}, bill generated for subscription {{subscriptionId}}. Bill ID: {{billId}}, Amt: {{amount}}, Due: {{date}}.");
        smsTemplates.put("SUBSCRIPTION_CREATED", "Dear {{customerName}}, your subscription {{subscriptionId}} has been created successfully.");
        smsTemplates.put("SUBSCRIPTION_TERMINATED", "Dear {{customerName}}, your subscription {{subscriptionId}} has been terminated.");
    }

    public String getEmailSubject(String templateName) {
        return emailSubjects.getOrDefault(templateName, "Notification from Subscription Engine");
    }

    public String renderEmail(String templateName, Map<String, String> variables) {
        String template = emailTemplates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("Email template not found: " + templateName);
        }
        return replacePlaceholders(template, variables);
    }

    public String renderSms(String templateName, Map<String, String> variables) {
        String template = smsTemplates.get(templateName);
        if (template == null) {
            throw new IllegalArgumentException("SMS template not found: " + templateName);
        }
        return replacePlaceholders(template, variables);
    }

    private String replacePlaceholders(String text, Map<String, String> variables) {
        if (variables == null) {
            return text;
        }
        String result = text;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }
}

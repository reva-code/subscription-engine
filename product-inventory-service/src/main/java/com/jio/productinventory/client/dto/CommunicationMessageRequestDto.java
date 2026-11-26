package com.jio.productinventory.client.dto;

import lombok.*;

/**
 * Local request DTO mirroring the subset of communication-service's
 * POST /tmf-api/communicationManagement/v4/communicationMessage body
 * this service needs. Deliberately does NOT import communication-service's
 * internal CommunicationMessage/CommunicationType classes (cross-module
 * boundary violation).
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CommunicationMessageRequestDto {

    private String customerId;
    private String receiverMobile;
    private String communicationType;
    private String subject;
    private String messageBody;
}

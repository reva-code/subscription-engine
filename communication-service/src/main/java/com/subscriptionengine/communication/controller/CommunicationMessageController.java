package com.subscriptionengine.communication.controller;

import com.subscriptionengine.communication.generated.api.CommunicationMessageApi;
import com.subscriptionengine.communication.generated.model.CommunicationMessage;
import com.subscriptionengine.communication.generated.model.CommunicationMessageCreate;
import com.subscriptionengine.communication.generated.model.CommunicationMessageUpdate;
import com.subscriptionengine.communication.service.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/communicationManagement/v4")
public class CommunicationMessageController implements CommunicationMessageApi {

    private static final Logger log = LoggerFactory.getLogger(CommunicationMessageController.class);

    private final CommunicationService service;

    public CommunicationMessageController(CommunicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<CommunicationMessage> createCommunicationMessage(CommunicationMessageCreate communicationMessage) throws Exception {
        log.info("Request received to create communication message");
        CommunicationMessage result = service.createAndSendMessage(communicationMessage);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteCommunicationMessage(String id) throws Exception {
        log.warn("Delete operation is not supported or not fully implemented by specification details");
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<List<CommunicationMessage>> listCommunicationMessage(String fields, Integer offset, Integer limit) throws Exception {
        log.info("Request received to list communication messages. Fields: {}, Offset: {}, Limit: {}", fields, offset, limit);
        List<CommunicationMessage> list = service.getAllMessages();
        return ResponseEntity.ok(list);
    }

    @Override
    public ResponseEntity<CommunicationMessage> patchCommunicationMessage(String id, CommunicationMessageUpdate communicationMessage) throws Exception {
        log.warn("Patch operation is not implemented");
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    public ResponseEntity<CommunicationMessage> retrieveCommunicationMessage(String id, String fields) throws Exception {
        log.info("Request received to retrieve communication message by ID: {}", id);
        CommunicationMessage message = service.getMessageById(id);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/communicationMessage/customer/{customerId}")
    public ResponseEntity<List<CommunicationMessage>> getMessagesByCustomerId(@PathVariable("customerId") String customerId) {
        log.info("Request received to retrieve communication messages for customer: {}", customerId);
        List<CommunicationMessage> list = service.getMessagesByCustomerId(customerId);
        return ResponseEntity.ok(list);
    }
}

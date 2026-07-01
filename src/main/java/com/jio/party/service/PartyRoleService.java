package com.jio.party.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.party.entity.*;
import com.jio.party.exception.ResourceNotFoundException;
import com.jio.party.repository.PartyRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class PartyRoleService {

    private static final Logger log = LoggerFactory.getLogger(PartyRoleService.class);

    private final PartyRoleRepository repository;
    private final ObjectMapper objectMapper;

    public PartyRoleService(PartyRoleRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Transactional(readOnly = true)
    public List<PartyRole> list(String roleType, String status) {
        if (roleType != null && status != null) return repository.findByRoleTypeAndStatus(roleType, status);
        if (roleType != null) return repository.findByRoleType(roleType);
        if (status != null) return repository.findByStatus(status);
        return repository.findAll();
    }

    @Transactional
    public PartyRole create(PartyRole partyRole) {
        PartyRole saved = repository.save(partyRole);
        saved.setHref("/tmf-api/partyManagement/v4/partyRole/" + saved.getId());
        saved = repository.save(saved);
        log.info("PartyRole persisted id={} href={}", saved.getId(), saved.getHref());
        return saved;
    }

    @Transactional(readOnly = true)
    public PartyRole findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PartyRole not found with id: " + id));
    }

    @Transactional
    public PartyRole partialUpdate(String id, Map<String, Object> fields) {
        PartyRole existing = findById(id);

        if (fields.containsKey("name"))            existing.setName((String) fields.get("name"));
        if (fields.containsKey("status"))          existing.setStatus((String) fields.get("status"));
        if (fields.containsKey("statusReason"))    existing.setStatusReason((String) fields.get("statusReason"));
        if (fields.containsKey("roleType"))        existing.setRoleType((String) fields.get("roleType"));
        if (fields.containsKey("@type"))           existing.setAtType((String) fields.get("@type"));
        if (fields.containsKey("@baseType"))       existing.setAtBaseType((String) fields.get("@baseType"));
        if (fields.containsKey("@schemaLocation")) existing.setAtSchemaLocation((String) fields.get("@schemaLocation"));

        if (fields.containsKey("validFor"))
            existing.setValidFor(objectMapper.convertValue(fields.get("validFor"), TimePeriod.class));
        if (fields.containsKey("engagedParty"))
            existing.setEngagedParty(objectMapper.convertValue(fields.get("engagedParty"), RelatedParty.class));
        if (fields.containsKey("relatedParty"))
            existing.setRelatedParty(convertList(fields.get("relatedParty"), RelatedParty.class));
        if (fields.containsKey("contactMedium"))
            existing.setContactMedium(convertList(fields.get("contactMedium"), ContactMedium.class));
        if (fields.containsKey("creditProfile"))
            existing.setCreditProfile(convertList(fields.get("creditProfile"), CreditProfile.class));
        if (fields.containsKey("characteristic"))
            existing.setCharacteristic(convertList(fields.get("characteristic"), Characteristic.class));
        if (fields.containsKey("account"))
            existing.setAccount(convertList(fields.get("account"), AccountRef.class));
        if (fields.containsKey("paymentMethod"))
            existing.setPaymentMethod(convertList(fields.get("paymentMethod"), PaymentMethodRef.class));
        if (fields.containsKey("agreement"))
            existing.setAgreement(convertList(fields.get("agreement"), AgreementRef.class));

        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PartyRole not found with id: " + id);
        }
        repository.deleteById(id);
        log.info("PartyRole deleted id={}", id);
    }

    private <T> List<T> convertList(Object raw, Class<T> type) {
        return objectMapper.convertValue(raw,
                objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }
}

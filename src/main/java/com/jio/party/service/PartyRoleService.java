package com.jio.party.service;

import com.jio.party.entity.PartyRole;
import com.jio.party.exception.ResourceNotFoundException;
import com.jio.party.repository.PartyRoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class PartyRoleService {

    private final PartyRoleRepository repository;

    public PartyRoleService(PartyRoleRepository repository) {
        this.repository = repository;
    }

    public List<PartyRole> list(String roleType, String status) {
        if (roleType != null && status != null) return repository.findByRoleTypeAndStatus(roleType, status);
        if (roleType != null) return repository.findByRoleType(roleType);
        if (status != null) return repository.findByStatus(status);
        return repository.findAll();
    }

    public PartyRole create(PartyRole partyRole) {
        PartyRole saved = repository.save(partyRole);
        saved.setHref("/tmf-api/partyManagement/v4/partyRole/" + saved.getId());
        return repository.save(saved);
    }

    public PartyRole findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PartyRole not found with id: " + id));
    }

    public PartyRole partialUpdate(String id, Map<String, Object> fields) {
        PartyRole existing = findById(id);
        // Only top-level scalar fields are patched; nested collections require full replace
        if (fields.containsKey("name"))          existing.setName((String) fields.get("name"));
        if (fields.containsKey("status"))        existing.setStatus((String) fields.get("status"));
        if (fields.containsKey("statusReason"))  existing.setStatusReason((String) fields.get("statusReason"));
        if (fields.containsKey("roleType"))      existing.setRoleType((String) fields.get("roleType"));
        if (fields.containsKey("@type"))         existing.setAtType((String) fields.get("@type"));
        if (fields.containsKey("@baseType"))     existing.setAtBaseType((String) fields.get("@baseType"));
        if (fields.containsKey("@schemaLocation")) existing.setAtSchemaLocation((String) fields.get("@schemaLocation"));
        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PartyRole not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

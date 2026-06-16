package com.jio.party.controller;

import com.jio.party.entity.PartyRole;
import com.jio.party.service.PartyRoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/partyRole")
public class PartyRoleController {

    private final PartyRoleService service;

    public PartyRoleController(PartyRoleService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PartyRole>> list(
            @RequestParam(required = false) String roleType,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(service.list(roleType, status));
    }

    @PostMapping
    public ResponseEntity<PartyRole> create(@Valid @RequestBody PartyRole partyRole) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(partyRole));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartyRole> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PartyRole> partialUpdate(
            @PathVariable String id,
            @RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok(service.partialUpdate(id, fields));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

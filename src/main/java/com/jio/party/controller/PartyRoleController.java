package com.jio.party.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jio.party.entity.PartyRole;
import com.jio.party.service.PartyRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/partyRole")
@Tag(name = "Party Role Management", description = "TMF669 Party Role Management API — manage Subscriber, Business Customer, ContentProvider, and Banking party roles")
public class PartyRoleController {

    private static final Logger log = LoggerFactory.getLogger(PartyRoleController.class);

    private final PartyRoleService service;
    private final ObjectMapper objectMapper;

    public PartyRoleController(PartyRoleService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @Operation(
        summary = "List all PartyRoles",
        description = "Returns a list of PartyRole objects. Filter by roleType or status. Supports pagination (?offset=, ?limit=) and field selection (?fields=). Returns X-Total-Count and X-Result-Count headers."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Object> list(
            @Parameter(description = "Filter by role type — Subscriber, Business Customer, ContentProvider, or Banking")
            @RequestParam(required = false) String roleType,
            @Parameter(description = "Filter by status — e.g. Active, Approved, Inactive")
            @RequestParam(required = false) String status,
            @Parameter(description = "Zero-based pagination offset")
            @RequestParam(required = false) Integer offset,
            @Parameter(description = "Maximum number of results to return")
            @RequestParam(required = false) Integer limit,
            @Parameter(description = "Comma-separated list of fields to include in the response")
            @RequestParam(required = false) String fields) {

        List<PartyRole> all = service.list(roleType, status);
        int total = all.size();

        int from = (offset != null && offset >= 0) ? offset : 0;
        List<PartyRole> paged = (limit != null && limit > 0)
                ? all.subList(Math.min(from, total), Math.min(from + limit, total))
                : all.subList(Math.min(from, total), total);

        Object body = (fields != null && !fields.isBlank())
                ? applyFieldSelectionToList(paged, fields)
                : paged;

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(total))
                .header("X-Result-Count", String.valueOf(paged.size()))
                .body(body);
    }

    @Operation(
        summary = "Create a PartyRole",
        description = "Creates a new PartyRole. The field name is required. Returns 201 with a Location header pointing to the new resource."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created successfully"),
        @ApiResponse(responseCode = "400", description = "Bad Request — validation failed", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid JWT", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PartyRole> create(@Valid @RequestBody PartyRole partyRole) {
        log.info("Creating PartyRole name={} roleType={}", partyRole.getName(), partyRole.getRoleType());
        PartyRole created = service.create(partyRole);
        log.info("Created PartyRole id={}", created.getId());
        return ResponseEntity
                .created(URI.create(created.getHref()))
                .body(created);
    }

    @Operation(
        summary = "Get a PartyRole by ID",
        description = "Retrieves a single PartyRole by its unique identifier. Supports field selection (?fields=)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "404", description = "PartyRole not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(
            @Parameter(description = "Unique ID of the PartyRole to retrieve")
            @PathVariable String id,
            @Parameter(description = "Comma-separated list of fields to include in the response")
            @RequestParam(required = false) String fields) {
        PartyRole role = service.findById(id);
        if (fields != null && !fields.isBlank()) {
            return ResponseEntity.ok(applyFieldSelection(role, fields));
        }
        return ResponseEntity.ok(role);
    }

    @Operation(
        summary = "Partially update a PartyRole",
        description = "Updates only the fields provided in the request body. Supports scalar fields (name, status, statusReason, roleType, @type, @baseType, @schemaLocation, validFor, engagedParty) and full collection replacement (relatedParty, contactMedium, creditProfile, characteristic, account, paymentMethod, agreement)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "404", description = "PartyRole not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PartyRole> partialUpdate(
            @Parameter(description = "Unique ID of the PartyRole to update")
            @PathVariable String id,
            @RequestBody Map<String, Object> fields) {
        log.info("Patching PartyRole id={} fields={}", id, fields.keySet());
        return ResponseEntity.ok(service.partialUpdate(id, fields));
    }

    @Operation(
        summary = "Delete a PartyRole",
        description = "Deletes a PartyRole by its unique identifier."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid JWT", content = @Content),
        @ApiResponse(responseCode = "404", description = "PartyRole not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Unique ID of the PartyRole to delete")
            @PathVariable String id) {
        log.info("Deleting PartyRole id={}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Object applyFieldSelectionToList(List<PartyRole> roles, String fields) {
        Set<String> keep = parseFields(fields);
        return roles.stream()
                .map(r -> filterNode(objectMapper.valueToTree(r), keep))
                .collect(Collectors.toList());
    }

    private Object applyFieldSelection(PartyRole role, String fields) {
        return filterNode(objectMapper.valueToTree(role), parseFields(fields));
    }

    private Set<String> parseFields(String fields) {
        return Arrays.stream(fields.split(","))
                .map(String::trim)
                .filter(f -> !f.isEmpty())
                .collect(Collectors.toSet());
    }

    private ObjectNode filterNode(JsonNode node, Set<String> keepFields) {
        ObjectNode result = objectMapper.createObjectNode();
        node.fields().forEachRemaining(entry -> {
            if (keepFields.contains(entry.getKey())) {
                result.set(entry.getKey(), entry.getValue());
            }
        });
        return result;
    }
}

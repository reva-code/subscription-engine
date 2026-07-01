package com.ott.subscription_engine.tmf635_usage;

import com.ott.subscription_engine.common.TmfFilteringUtil;
import com.ott.subscription_engine.tmf635_usage.api.UsageApi;
import com.ott.subscription_engine.tmf635_usage.api.UsageSpecificationApi;
import com.ott.subscription_engine.tmf635_usage.model.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/tmf-api/usageManagement/v4")
@RequiredArgsConstructor
@Tag(name = "TMF635 - Usage Management", description = "Subscriber consumption ingestion and rating per TM Forum TMF635")
public class UsageController implements UsageApi, UsageSpecificationApi {

    private final UsageService usageService;
    private final NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(nativeWebRequest);
    }

    // ── Usage Ingestion (UsageApi) ───────────────────────────────────────

    @Override
    public ResponseEntity<Usage> createUsage(@Valid @RequestBody UsageCreate usage) {
        Usage saved = usageService.createUsage(usage);
        URI location = URI.create("/tmf-api/usageManagement/v4/usage/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @Override
    public ResponseEntity<List<Usage>> listUsage(
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) {
        return listUsageFiltered(fields, offset, limit, null, null);
    }

    @GetMapping("/usage")
    public ResponseEntity<List<Usage>> listUsageFiltered(
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "customerId", required = false) String customerId,
            @RequestParam(value = "subscriptionId", required = false) String subscriptionId) {
        List<Usage> all;
        if (customerId != null)          all = usageService.getUsagesByCustomer(customerId);
        else if (subscriptionId != null) all = usageService.getUsagesBySubscription(subscriptionId);
        else                             all = usageService.getAllUsagesPaginated(offset, limit);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(usageService.getAllUsages().size()))
                .header("X-Result-Count", String.valueOf(all.size()))
                .body((List<Usage>) TmfFilteringUtil.filter(all, fields));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<Usage> retrieveUsage(
            @PathVariable("id") String id,
            @RequestParam(value = "fields", required = false) String fields) {
        return usageService.getUsageById(id)
                .map(u -> ResponseEntity.ok((Usage) TmfFilteringUtil.filter(u, fields)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Usage> patchUsage(
            @PathVariable("id") String id,
            @Valid @RequestBody UsageUpdate usage) {
        return usageService.patchUsage(id, usage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteUsage(@PathVariable("id") String id) {
        return usageService.deleteUsage(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/usage/{id}/rate")
    public ResponseEntity<Usage> rateUsage(
            @PathVariable String id,
            @RequestParam String specificationId) {
        Usage rated = usageService.rateUsageById(id, specificationId);
        return ResponseEntity.ok(rated);
    }

    // ── Usage Specification (UsageSpecificationApi) ─────────────────────

    @Override
    public ResponseEntity<UsageSpecification> createUsageSpecification(@Valid @RequestBody UsageSpecificationCreate usageSpecification) {
        UsageSpecification saved = usageService.createUsageSpec(usageSpecification);
        URI location = URI.create("/tmf-api/usageManagement/v4/usageSpecification/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    @Override
    public ResponseEntity<List<UsageSpecification>> listUsageSpecification(
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit) {
        List<UsageSpecification> all = usageService.getAllUsageSpecs();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(all.size()))
                .body((List<UsageSpecification>) TmfFilteringUtil.filter(all, fields));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<UsageSpecification> retrieveUsageSpecification(
            @PathVariable("id") String id,
            @RequestParam(value = "fields", required = false) String fields) {
        return usageService.getUsageSpecById(id)
                .map(s -> ResponseEntity.ok((UsageSpecification) TmfFilteringUtil.filter(s, fields)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<UsageSpecification> patchUsageSpecification(
            @PathVariable("id") String id,
            @Valid @RequestBody UsageSpecificationUpdate usageSpecification) {
        return usageService.patchUsageSpec(id, usageSpecification)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteUsageSpecification(@PathVariable("id") String id) {
        return usageService.deleteUsageSpec(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}

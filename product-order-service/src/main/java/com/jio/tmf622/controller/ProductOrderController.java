package com.jio.tmf622.controller;

import com.jio.tmf622.api.ProductOrderApi;
import com.jio.tmf622.entity.ProductOrderEntity;
import com.jio.tmf622.entity.ProductOrderItemEntity;
import com.jio.tmf622.model.ProductOrder;
import com.jio.tmf622.model.ProductOrderCreate;
import com.jio.tmf622.model.ProductOrderItem;
import com.jio.tmf622.model.ProductOrderUpdate;
import com.jio.tmf622.service.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// @RestController — two annotations combined into one:
//   @Controller  → registers this class as a Spring MVC controller bean
//   @ResponseBody → every method return value is written directly as
//                   JSON into the HTTP response body (no view/template needed)
//
// We do NOT add @RequestMapping here — the URL mappings are already
// declared on the ProductOrderApi interface we implement. Adding them
// here too would conflict and produce duplicate route registrations.
@RestController
public class ProductOrderController implements ProductOrderApi {

    // Final field + constructor injection — same pattern as the service.
    // Spring injects the ProductOrderService bean automatically.
    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    // ── createProductOrder ────────────────────────────────────────────────────
    //
    // Handles: POST /productOrder
    // The @RequestMapping, @Valid, and @RequestBody annotations are already
    // declared on this method in ProductOrderApi — we inherit them by overriding.
    //
    // Parameter: ProductOrderCreate productOrder
    //   The request JSON body, already deserialized and validated by Spring
    //   (@Valid triggers Bean Validation — e.g. @NotNull on productOrderItem list).
    //
    // Returns: ResponseEntity<ProductOrder>
    //   ResponseEntity wraps both the body and the HTTP status code.
    //   We return 201 Created (not 200 OK) because a new resource was created.
    @Override
    public ResponseEntity<ProductOrder> createProductOrder(
            @Valid ProductOrderCreate productOrder) {

        // Delegate to the service — all business logic is there.
        // The service generates the ID, sets state=ACKNOWLEDGED, saves to DB.
        ProductOrderEntity savedEntity = productOrderService.createProductOrder(productOrder);

        // Convert the saved entity to the API DTO that the interface requires.
        // The client must receive the TMF622 ProductOrder model, not our
        // internal entity class — those two must stay separate.
        ProductOrder responseDto = toDto(savedEntity);

        // ResponseEntity.status(201).body(dto) — sets HTTP 201 Created
        // and places the DTO as the JSON response body.
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // ── retrieveProductOrder ──────────────────────────────────────────────────
    //
    // Handles: GET /productOrder/{id}
    //
    // Parameters:
    //   id     — the {id} path variable from the URL
    //   fields — optional ?fields= query param for field selection (ignored for now)
    //
    // Returns:
    //   200 OK   + ProductOrder body  →  order found
    //   404 Not Found                 →  no order with this id exists
    @Override
    public ResponseEntity<ProductOrder> retrieveProductOrder(
            String id,
            @Nullable String fields) {

        // The service returns Optional<ProductOrderEntity>.
        // Optional forces us to handle the "not found" case explicitly —
        // there is no risk of a NullPointerException being silently swallowed.
        Optional<ProductOrderEntity> result = productOrderService.getProductOrderById(id);

        // map() runs toDto() only if the Optional contains a value.
        // If the Optional is empty (order not found), map() returns an empty Optional.
        //
        // .map(this::toDto)         → Optional<ProductOrder>  (if found)
        //                           → Optional.empty()         (if not found)
        //
        // .map(ResponseEntity::ok)  → Optional<ResponseEntity<ProductOrder>> with 200
        //
        // .orElse(notFound)         → if still empty, return 404 Not Found
        return result
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        // The chain above is equivalent to this if/else but without null checks:
        //
        // if (result.isPresent()) {
        //     return ResponseEntity.ok(toDto(result.get()));
        // } else {
        //     return ResponseEntity.notFound().build();
        // }
    }

    // ── patchProductOrder ─────────────────────────────────────────────────────
    //
    // Handles: PATCH /productOrder/{id}
    //
    // PATCH means "apply a partial update" — the request body only needs to
    // contain the fields the caller wants to change. In this sprint only the
    // "state" field is processed; all other fields in ProductOrderUpdate are
    // ignored.
    //
    // HTTP responses:
    //   200 OK                 — transition was valid, order updated
    //   404 Not Found          — no order with this id
    //   422 Unprocessable      — transition is not permitted by the state machine
    //   400 Bad Request        — request body has no "state" field
    @Override
    public ResponseEntity<ProductOrder> patchProductOrder(
            String id,
            @Valid ProductOrderUpdate productOrder) {

        // updateProductOrderState may throw:
        //   RuntimeException("Order not found")      → caught below → 404
        //   InvalidStateTransitionException           → caught by GlobalExceptionHandler → 422
        //   IllegalArgumentException (no state)      → caught below → 400
        try {
            ProductOrderEntity updated =
                    productOrderService.updateProductOrderState(id, productOrder);
            return ResponseEntity.ok(toDto(updated));

        } catch (IllegalArgumentException ex) {
            // Missing "state" field in request body.
            return ResponseEntity.badRequest().build();

        } catch (RuntimeException ex) {
            // Order not found — the service throws RuntimeException with
            // "Order not found: <id>" as the message.
            if (ex.getMessage() != null && ex.getMessage().startsWith("Order not found")) {
                return ResponseEntity.notFound().build();
            }
            throw ex;
        }
    }

    // ── toDto ─────────────────────────────────────────────────────────────────
    //
    // Translates a ProductOrderEntity (internal DB representation) into a
    // ProductOrder (the TMF622 API DTO the client expects in the response).
    //
    // This is a private helper — it belongs here because it is only used
    // by this controller. When we introduce MapStruct later, this method
    // is replaced by a generated mapper and this helper is deleted.
    //
    // We only map the fields we actually persist right now (id, state,
    // orderDate, productOrderItems). Other fields from the full TMF622
    // spec will be added when persistence is extended.
    private ProductOrder toDto(ProductOrderEntity entity) {
        ProductOrder dto = new ProductOrder(new ArrayList<>());

        // id — the UUID we generated on creation
        dto.setId(entity.getId());

        // state — ACKNOWLEDGED / IN_PROGRESS / COMPLETED etc.
        dto.setState(entity.getState());

        // orderDate — the timestamp of when the order was placed
        dto.setOrderDate(entity.getOrderDate());

        // Map each ProductOrderItemEntity → ProductOrderItem DTO
        if (entity.getProductOrderItems() != null) {
            List<ProductOrderItem> itemDtos = new ArrayList<>();
            for (ProductOrderItemEntity itemEntity : entity.getProductOrderItems()) {
                ProductOrderItem itemDto = new ProductOrderItem(
                        itemEntity.getId(),    // id (sequence number e.g. "1")
                        itemEntity.getAction() // action (add/modify/delete/noChange)
                );
                itemDto.setState(itemEntity.getState());
                itemDtos.add(itemDto);
            }
            dto.setProductOrderItem(itemDtos);
        }

        return dto;
    }
}

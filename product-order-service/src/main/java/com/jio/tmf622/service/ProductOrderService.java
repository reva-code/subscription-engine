package com.jio.tmf622.service;

import com.jio.tmf622.entity.ProductOrderEntity;
import com.jio.tmf622.entity.ProductOrderItemEntity;
import com.jio.tmf622.exception.InvalidStateTransitionException;
import com.jio.tmf622.model.ProductOrderCreate;
import com.jio.tmf622.model.ProductOrderItem;
import com.jio.tmf622.model.ProductOrderStateType;
import com.jio.tmf622.model.ProductOrderUpdate;
import com.jio.tmf622.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

// @Service — marks this class as a Spring-managed bean in the service layer.
// Spring creates one instance of this class and injects it wherever it is needed
// (e.g. into the controller). Functionally similar to @Component but communicates
// intent: "this class contains business logic."
@Service
public class ProductOrderService {

    private final ProductOrderRepository productOrderRepository;
    private final OrderStateValidator orderStateValidator;
    private final CatalogValidationService catalogValidationService;

    public ProductOrderService(ProductOrderRepository productOrderRepository,
                           OrderStateValidator orderStateValidator,
                           CatalogValidationService catalogValidationService) {

    this.productOrderRepository = productOrderRepository;
    this.orderStateValidator = orderStateValidator;
    this.catalogValidationService = catalogValidationService;
}

    // ── createProductOrder ────────────────────────────────────────────────────
    //
    // Accepts a ProductOrderCreate DTO (the incoming JSON payload from the client).
    // Applies business rules, builds a ProductOrderEntity, saves it, returns it.
    //
    // @Transactional — wraps this entire method in a single database transaction.
    // If anything fails partway through (e.g. saving an item throws an exception),
    // the whole operation rolls back — no partial data is written to the DB.
    // The order and all its items are saved atomically.
    @Transactional
    public ProductOrderEntity createProductOrder(ProductOrderCreate productOrderCreate) {

        // Step 1: Build the parent entity.
        ProductOrderEntity orderEntity = new ProductOrderEntity();

        // Business rule: the ID is always generated server-side as a UUID.
        // The client never supplies an ID on a POST request.
        orderEntity.setId(UUID.randomUUID().toString());

        // Business rule: a new order always starts as ACKNOWLEDGED.
        // We ignore any state the client may have included in the request.
        orderEntity.setState(ProductOrderStateType.ACKNOWLEDGED);

        // ProductOrderCreate intentionally omits orderDate (it is a server-set field —
        // see the "Skipped properties" comment in the generated class). Always default
        // to the current timestamp; the client cannot override this.
        orderEntity.setOrderDate(OffsetDateTime.now());

        // Step 2: Map each incoming ProductOrderItem DTO to a ProductOrderItemEntity.
        // productOrderCreate.getProductOrderItem() is the list from the JSON body.
        if (productOrderCreate.getProductOrderItem() != null) {
            for (ProductOrderItem itemDto : productOrderCreate.getProductOrderItem()) {
                if (itemDto.getProduct() != null &&
                    itemDto.getProduct().getProductOffering() != null) {

                    catalogValidationService.validateOffering(
                        itemDto.getProduct()
                            .getProductOffering()
                            .getId());
                            }
                ProductOrderItemEntity itemEntity = new ProductOrderItemEntity();

                // The item ID comes from the caller — it is a sequence number
                // like "1", "2", "3" that the client assigns to identify line items.
                itemEntity.setId(itemDto.getId());

                // action (add / modify / delete / noChange) is mandatory on the item.
                itemEntity.setAction(itemDto.getAction());

                // addProductOrderItem() sets both sides of the relationship:
                //   - adds the item to order.productOrderItems list
                //   - sets item.productOrder = this order
                // Both sides must be consistent before Hibernate flushes to DB.
                orderEntity.addProductOrderItem(itemEntity);
            }
        }

        // Step 3: Save the order entity.
        // Because of CascadeType.ALL on the @OneToMany relationship,
        // Hibernate saves all ProductOrderItemEntities in the same call —
        // you do not need to call save() separately for each item.
        ProductOrderEntity saved = productOrderRepository.save(orderEntity);

        // Step 4: Return the saved entity (now has the DB-assigned id, timestamps, etc.)
        return saved;
    }

    // ── updateProductOrderState ───────────────────────────────────────────────
    //
    // Implements PATCH semantics for the order lifecycle.
    // Only the "state" field is mutable via PATCH in this sprint — clients
    // advance the order through the state machine by sending a new state value.
    //
    // Flow:
    //   1. Load the current entity from the DB (404 if not found).
    //   2. Extract the requested new state from the update DTO.
    //   3. Validate the transition (throws InvalidStateTransitionException if illegal).
    //   4. Apply the change and save.
    //
    // Returns the updated entity so the controller can map it to a response DTO.
    // Throws IllegalArgumentException (→ 400) if the request contains no state.
    // Throws InvalidStateTransitionException (→ 422) for illegal transitions.
    //
    // @Transactional (read-write) — we load and then write in the same transaction
    // so Hibernate cannot see a partially-updated row in between.
    @Transactional
    public ProductOrderEntity updateProductOrderState(String id, ProductOrderUpdate update) {

        // Step 1: Load from DB.
        // findById returns Optional — orElseThrow converts "not found" to a
        // RuntimeException that the controller catches and maps to 404.
        ProductOrderEntity order = productOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));

        // Step 2: A PATCH without a state field is meaningless for our current
        // implementation — reject it early with a clear message.
        ProductOrderStateType requestedState = update.getState();
        if (requestedState == null) {
            throw new IllegalArgumentException(
                "PATCH request must include a 'state' field.");
        }

        // Step 3: Validate.
        // orderStateValidator.validate() checks the ALLOWED_TRANSITIONS table.
        // If the move is illegal it throws InvalidStateTransitionException —
        // GlobalExceptionHandler converts that to a 422 response automatically.
        orderStateValidator.validate(order.getState(), requestedState);

        // Step 4: Apply and persist.
        // Hibernate detects the change (dirty-checking) and issues an UPDATE
        // statement when the transaction commits — no explicit save() needed.
        // We call save() anyway to be explicit and to get the returned entity
        // with any DB-generated fields refreshed.
        order.setState(requestedState);
        return productOrderRepository.save(order);
    }

    // ── getProductOrderById ───────────────────────────────────────────────────
    //
    // Looks up a single ProductOrder by its ID.
    // Returns Optional<ProductOrderEntity> — the caller (controller) decides
    // what HTTP status to return if the order is not found (404).
    //
    // @Transactional(readOnly = true) — opens a read-only transaction.
    // This is a performance hint to Hibernate and the DB driver:
    //   - Hibernate skips the "dirty check" (it won't look for changes to flush).
    //   - Some DB drivers route read-only transactions to a read replica.
    // Use readOnly = true on every method that only reads data.
    @Transactional(readOnly = true)
    public Optional<ProductOrderEntity> getProductOrderById(String id) {

        // findById() is inherited from JpaRepository.
        // It returns Optional<ProductOrderEntity> — never null.
        // Optional forces the caller to handle the "not found" case explicitly
        // rather than risking a NullPointerException.
        return productOrderRepository.findById(id);
    }
}

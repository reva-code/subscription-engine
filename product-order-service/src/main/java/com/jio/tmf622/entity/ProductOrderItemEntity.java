package com.jio.tmf622.entity;

import com.jio.tmf622.model.OrderItemActionType;
import com.jio.tmf622.model.ProductOrderItemStateType;
import jakarta.persistence.*;

// @Entity — this class is a DB table.
@Entity

// @Table — exact table name in MariaDB.
@Table(name = "product_order_item")
public class ProductOrderItemEntity {

    // @Id + @Column — primary key.
    // Same UUID string strategy as ProductOrderEntity.
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    // "action" tells us what to do with this line item:
    // add / modify / delete / noChange.
    // Stored as the string value of the enum, not an integer.
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 50)
    private OrderItemActionType action;

    // Each item tracks its own state independently from the parent order.
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 50)
    private ProductOrderItemStateType state;

    // @ManyToOne — "many items belong to one order."
    // This is the OWNING side of the relationship — the FK column lives here.
    //
    // fetch = FetchType.LAZY — do not load the full parent ProductOrderEntity
    // every time you load a single item. Load only the FK value (order_id)
    // and fetch the parent only if you explicitly call getProductOrder().
    @ManyToOne(fetch = FetchType.LAZY)

    // @JoinColumn — names the actual foreign key column in this table.
    // "order_id" in product_order_item references "id" in product_order.
    // nullable = false — every item MUST belong to an order.
    @JoinColumn(name = "order_id", nullable = false)
    private ProductOrderEntity productOrder;

    // ── Constructors ────────────────────────────────────────────────────────

    public ProductOrderItemEntity() {
        // Required by JPA.
    }

    // ── Getters & Setters ───────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OrderItemActionType getAction() {
        return action;
    }

    public void setAction(OrderItemActionType action) {
        this.action = action;
    }

    public ProductOrderItemStateType getState() {
        return state;
    }

    public void setState(ProductOrderItemStateType state) {
        this.state = state;
    }

    public ProductOrderEntity getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(ProductOrderEntity productOrder) {
        this.productOrder = productOrder;
    }
}

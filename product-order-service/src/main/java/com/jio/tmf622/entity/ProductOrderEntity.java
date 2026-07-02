package com.jio.tmf622.entity;

import com.jio.tmf622.model.ProductOrderStateType;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

// @Entity — tells Hibernate this class is a database table.
// Every field (unless ignored) becomes a column.
@Entity

// @Table — sets the exact MariaDB table name to "product_order".
// Without this, Hibernate would default to "ProductOrderEntity" which is ugly.
@Table(name = "product_order")
public class ProductOrderEntity {

    // @Id — marks this field as the PRIMARY KEY of the table.
    // Every @Entity must have exactly one @Id field.
    @Id

    // @Column — maps the Java field "id" to the DB column "id".
    // nullable = false means this column has a NOT NULL constraint in the DB.
    // length = 36 matches the UUID format (e.g. "f3a2b1c9-...").
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    // @Enumerated(EnumType.STRING) — stores the enum as its string value.
    // e.g. stores "acknowledged", not 0.
    // Always prefer STRING over ORDINAL — ORDINAL breaks if you reorder the enum.
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 50)
    private ProductOrderStateType state;

    // Plain column — OffsetDateTime maps to DATETIME in MariaDB via Hibernate.
    @Column(name = "order_date")
    private OffsetDateTime orderDate;

    // @OneToMany — "one ProductOrder has many ProductOrderItems."
    //
    // mappedBy = "productOrder" — tells Hibernate that the OTHER side
    // (ProductOrderItemEntity.productOrder) owns the foreign key column.
    // This side is purely navigational — no extra join table is created.
    //
    // cascade = CascadeType.ALL — any operation on the order (save, delete)
    // is automatically cascaded to all its items. You only call save() once
    // on the order and all items are saved too.
    //
    // orphanRemoval = true — if you remove an item from the list and save
    // the order, Hibernate deletes that item row from the DB automatically.
    //
    // fetch = FetchType.LAZY — items are NOT loaded from the DB until you
    // actually call getProductOrderItems(). This avoids a large JOIN query
    // every time you load an order. Default for @OneToMany is already LAZY,
    // but we declare it explicitly for clarity.
    @OneToMany(
        mappedBy      = "productOrder",
        cascade       = CascadeType.ALL,
        orphanRemoval = true,
        fetch         = FetchType.LAZY
    )
    private List<ProductOrderItemEntity> productOrderItems = new ArrayList<>();

    // ── Constructors ────────────────────────────────────────────────────────

    public ProductOrderEntity() {
        // JPA requires a no-argument constructor.
        // Hibernate uses this to instantiate the object when loading from DB.
    }

    // ── Getters & Setters ───────────────────────────────────────────────────

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductOrderStateType getState() {
        return state;
    }

    public void setState(ProductOrderStateType state) {
        this.state = state;
    }

    public OffsetDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(OffsetDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<ProductOrderItemEntity> getProductOrderItems() {
        return productOrderItems;
    }

    public void setProductOrderItems(List<ProductOrderItemEntity> productOrderItems) {
        this.productOrderItems = productOrderItems;
    }

    // ── Convenience method ──────────────────────────────────────────────────
    // Used when adding a single item to the order.
    // Sets the back-reference on the item so both sides of the relationship
    // are consistent in memory before Hibernate flushes to DB.
    public void addProductOrderItem(ProductOrderItemEntity item) {
        productOrderItems.add(item);
        item.setProductOrder(this);
    }
}

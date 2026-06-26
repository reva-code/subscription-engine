-- ============================================================
-- TMF637 Product Inventory - Indexes V2
-- ============================================================

USE jio_product_inventory;

-- product: primary query patterns
CREATE INDEX idx_product_status           ON product(status);
CREATE INDEX idx_product_billing_account  ON product(billing_account_id);
CREATE INDEX idx_product_offering_id      ON product(product_offering_id);
CREATE INDEX idx_product_spec_id          ON product(product_spec_id);
CREATE INDEX idx_product_start_date       ON product(start_date);
CREATE INDEX idx_product_order_date       ON product(order_date);
CREATE INDEX idx_product_termination_date ON product(termination_date);
CREATE INDEX idx_product_is_bundle        ON product(is_bundle);
CREATE INDEX idx_product_created_at       ON product(created_at);

-- product_characteristic: lookup by product + name (e.g. find MSISDN for product)
CREATE INDEX idx_pchar_product_id         ON product_characteristic(product_id);
CREATE INDEX idx_pchar_name               ON product_characteristic(name);
CREATE INDEX idx_pchar_product_name       ON product_characteristic(product_id, name);

-- product_relationship
CREATE INDEX idx_prel_product_id          ON product_relationship(product_id);
CREATE INDEX idx_prel_related_product_id  ON product_relationship(related_product_id);
CREATE INDEX idx_prel_type                ON product_relationship(relationship_type);

-- product_price
CREATE INDEX idx_pprice_product_id        ON product_price(product_id);
CREATE INDEX idx_pprice_type              ON product_price(price_type);

-- product_price_alteration
CREATE INDEX idx_ppa_price_id             ON product_price_alteration(product_price_id);

-- product_term
CREATE INDEX idx_pterm_product_id         ON product_term(product_id);

-- related_party: customer-centric query (find all products of a customer)
CREATE INDEX idx_rparty_product_id        ON related_party(product_id);
CREATE INDEX idx_rparty_party_id          ON related_party(party_id);
CREATE INDEX idx_rparty_role              ON related_party(role);
CREATE INDEX idx_rparty_party_role        ON related_party(party_id, role);

-- product_place
CREATE INDEX idx_pplace_product_id        ON product_place(product_id);
CREATE INDEX idx_pplace_place_id          ON product_place(place_id);

-- product_order_ref: reverse-lookup from order to products
CREATE INDEX idx_porref_product_id        ON product_order_ref(product_id);
CREATE INDEX idx_porref_order_id          ON product_order_ref(product_order_id);

-- agreement_ref
CREATE INDEX idx_agref_product_id         ON agreement_ref(product_id);
CREATE INDEX idx_agref_agreement_id       ON agreement_ref(agreement_id);

-- product_note
CREATE INDEX idx_pnote_product_id         ON product_note(product_id);

-- product_attachment
CREATE INDEX idx_patt_product_id          ON product_attachment(product_id);

-- realizing_resource
CREATE INDEX idx_rres_product_id          ON realizing_resource(product_id);
CREATE INDEX idx_rres_resource_id         ON realizing_resource(resource_id);

-- realizing_service
CREATE INDEX idx_rsvc_product_id          ON realizing_service(product_id);
CREATE INDEX idx_rsvc_service_id          ON realizing_service(service_id);

-- product_status_history
CREATE INDEX idx_psh_product_id           ON product_status_history(product_id);
CREATE INDEX idx_psh_changed_at           ON product_status_history(changed_at);
CREATE INDEX idx_psh_to_status            ON product_status_history(to_status);

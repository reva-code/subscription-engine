-- ============================================================
-- TMF637 Product Inventory Management - Schema V1
-- Database: jio_product_inventory
-- MariaDB 12.3 / InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS jio_product_inventory
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE jio_product_inventory;

-- ============================================================
-- CORE: product
-- Maps to TMF637 Product resource
-- ============================================================
CREATE TABLE product (
    id                      VARCHAR(36)     NOT NULL,          -- UUID
    href                    VARCHAR(512),
    name                    VARCHAR(255),
    description             TEXT,
    is_bundle               BOOLEAN         NOT NULL DEFAULT FALSE,
    is_customer_visible     BOOLEAN         NOT NULL DEFAULT TRUE,
    product_serial_number   VARCHAR(255),
    order_date              DATETIME(6),
    start_date              DATETIME(6),
    termination_date        DATETIME(6),
    status                  VARCHAR(50)     NOT NULL,          -- ProductStatusType enum

    -- ProductOfferingRef (inline, FK to catalog via href)
    product_offering_id     VARCHAR(36),
    product_offering_href   VARCHAR(512),
    product_offering_name   VARCHAR(255),

    -- ProductSpecificationRef (inline)
    product_spec_id         VARCHAR(36),
    product_spec_href       VARCHAR(512),
    product_spec_name       VARCHAR(255),
    product_spec_version    VARCHAR(50),

    -- BillingAccountRef (inline)
    billing_account_id      VARCHAR(36),
    billing_account_href    VARCHAR(512),
    billing_account_name    VARCHAR(255),

    -- TMF polymorphism
    at_base_type            VARCHAR(255)    DEFAULT 'Product',
    at_schema_location      VARCHAR(512),
    at_type                 VARCHAR(255)    DEFAULT 'Product',

    -- Audit / optimistic locking
    created_at              DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at              DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by              VARCHAR(100),
    updated_by              VARCHAR(100),
    version                 BIGINT          NOT NULL DEFAULT 0,  -- optimistic lock

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_characteristic
-- TMF637 §ProductCharacteristic
-- ============================================================
CREATE TABLE product_characteristic (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    name            VARCHAR(255)    NOT NULL,
    value           TEXT,
    value_type      VARCHAR(100),                               -- string, integer, boolean, object
    at_type         VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_pchar_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_relationship
-- TMF637 §ProductRelationship (product-to-product)
-- ============================================================
CREATE TABLE product_relationship (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    product_id                  VARCHAR(36)     NOT NULL,       -- owner product
    relationship_type           VARCHAR(100)    NOT NULL,       -- bundled, reliesOn, targets, isContainedIn
    related_product_id          VARCHAR(36)     NOT NULL,       -- related product UUID
    related_product_href        VARCHAR(512),
    related_product_name        VARCHAR(255),
    at_type                     VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_prel_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_price
-- TMF637 §ProductPrice
-- ============================================================
CREATE TABLE product_price (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    product_id                  VARCHAR(36)     NOT NULL,
    name                        VARCHAR(255),
    description                 TEXT,
    price_type                  VARCHAR(100)    NOT NULL,       -- recurring, oneTime, usage
    recurring_charge_period     VARCHAR(50),                    -- monthly, yearly
    unit_of_measure             VARCHAR(50),

    -- Price (embedded Money)
    duty_free_amount            DECIMAL(18,4),
    tax_included_amount         DECIMAL(18,4),
    tax_rate                    DECIMAL(7,4),
    percentage                  DECIMAL(7,4),
    currency_unit               CHAR(3),                        -- ISO 4217

    -- ProductOfferingPriceRef
    pop_id                      VARCHAR(36),
    pop_href                    VARCHAR(512),
    pop_name                    VARCHAR(255),

    -- BillingAccountRef (price-level override)
    billing_account_id          VARCHAR(36),
    billing_account_href        VARCHAR(512),

    at_type                     VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_pprice_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_price_alteration
-- TMF637 §PriceAlteration — discount/surcharge on a ProductPrice
-- ============================================================
CREATE TABLE product_price_alteration (
    id                          BIGINT          NOT NULL AUTO_INCREMENT,
    product_price_id            BIGINT          NOT NULL,
    name                        VARCHAR(255),
    description                 TEXT,
    application_duration        INT,
    price_type                  VARCHAR(100)    NOT NULL,
    recurring_charge_period     VARCHAR(50),
    unit_of_measure             VARCHAR(50),
    priority                    INT,

    duty_free_amount            DECIMAL(18,4),
    tax_included_amount         DECIMAL(18,4),
    tax_rate                    DECIMAL(7,4),
    percentage                  DECIMAL(7,4),
    currency_unit               CHAR(3),

    pop_id                      VARCHAR(36),
    pop_href                    VARCHAR(512),
    pop_name                    VARCHAR(255),

    at_type                     VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_ppa_price FOREIGN KEY (product_price_id)
        REFERENCES product_price(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_term
-- TMF637 §ProductTerm — contractual commitment
-- ============================================================
CREATE TABLE product_term (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    name            VARCHAR(255),
    description     TEXT,
    duration_amount DECIMAL(10,2),
    duration_units  VARCHAR(50),                                -- month, year, day
    valid_for_start DATETIME(6),
    valid_for_end   DATETIME(6),
    at_type         VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_pterm_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- related_party
-- TMF637 §RelatedParty — customer, dealer, CSR
-- ============================================================
CREATE TABLE related_party (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    party_id        VARCHAR(36)     NOT NULL,
    href            VARCHAR(512),
    name            VARCHAR(255),
    role            VARCHAR(100)    NOT NULL,                   -- Customer, Seller, Dealer
    at_referred_type VARCHAR(255),
    at_type         VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_rparty_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_place
-- TMF637 §RelatedPlaceRefOrValue — installation/service address
-- ============================================================
CREATE TABLE product_place (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    place_id        VARCHAR(36),
    href            VARCHAR(512),
    name            VARCHAR(255),
    role            VARCHAR(100)    NOT NULL,                   -- installationAddress, usageAddress
    at_type         VARCHAR(255),
    at_referred_type VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_pplace_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_order_ref
-- TMF637 §RelatedProductOrderItem — traceability to TMF622
-- ============================================================
CREATE TABLE product_order_ref (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    product_id          VARCHAR(36)     NOT NULL,
    product_order_id    VARCHAR(36)     NOT NULL,
    product_order_href  VARCHAR(512),
    order_item_id       VARCHAR(100)    NOT NULL,
    order_item_action   VARCHAR(100),                           -- add, modify, delete, noChange
    role                VARCHAR(100),
    at_type             VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_porref_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- agreement_ref
-- TMF637 §AgreementItemRef
-- ============================================================
CREATE TABLE agreement_ref (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    product_id          VARCHAR(36)     NOT NULL,
    agreement_id        VARCHAR(36)     NOT NULL,
    href                VARCHAR(512),
    name                VARCHAR(255),
    agreement_item_id   VARCHAR(100),
    at_referred_type    VARCHAR(255),
    at_type             VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_agref_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_note
-- TMF637 §Note — comments/annotations on a product
-- ============================================================
CREATE TABLE product_note (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    product_id  VARCHAR(36)     NOT NULL,
    author      VARCHAR(255),
    date        DATETIME(6),
    text        TEXT            NOT NULL,
    at_type     VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_pnote_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_attachment
-- TMF637 §AttachmentRefOrValue
-- ============================================================
CREATE TABLE product_attachment (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    attachment_id   VARCHAR(36),
    href            VARCHAR(512),
    name            VARCHAR(255),
    description     TEXT,
    url             VARCHAR(1024),
    mime_type       VARCHAR(100),
    size_amount     DECIMAL(18,4),
    size_units      VARCHAR(50),
    valid_for_start DATETIME(6),
    valid_for_end   DATETIME(6),
    at_type         VARCHAR(255),
    at_referred_type VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_patt_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- realizing_resource
-- TMF637 §ResourceRef — links to TMF639 Resource Inventory
-- Stored as slim reference; full resource lives in TMF639
-- ============================================================
CREATE TABLE realizing_resource (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    product_id  VARCHAR(36)     NOT NULL,
    resource_id VARCHAR(36)     NOT NULL,
    href        VARCHAR(512),
    name        VARCHAR(255),
    value       VARCHAR(255),
    at_type     VARCHAR(255),
    at_referred_type VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_rres_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- realizing_service
-- TMF637 §ServiceRef — links to TMF638 Service Inventory
-- ============================================================
CREATE TABLE realizing_service (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    product_id  VARCHAR(36)     NOT NULL,
    service_id  VARCHAR(36)     NOT NULL,
    href        VARCHAR(512),
    name        VARCHAR(255),
    at_type     VARCHAR(255),
    at_referred_type VARCHAR(255),

    PRIMARY KEY (id),
    CONSTRAINT fk_rsvc_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- product_status_history
-- Not in TMF637 spec but essential for telecom audit trail
-- Tracks every lifecycle transition
-- ============================================================
CREATE TABLE product_status_history (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    product_id      VARCHAR(36)     NOT NULL,
    from_status     VARCHAR(50),
    to_status       VARCHAR(50)     NOT NULL,
    reason          TEXT,
    changed_by      VARCHAR(100),
    changed_at      DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    order_ref_id    VARCHAR(36),                                -- which order triggered this

    PRIMARY KEY (id),
    CONSTRAINT fk_psh_product FOREIGN KEY (product_id)
        REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ============================================================
-- event_subscription
-- TMF637 §EventSubscription — hub/listener registry
-- ============================================================
CREATE TABLE event_subscription (
    id          VARCHAR(36)     NOT NULL,
    callback    VARCHAR(1024)   NOT NULL,
    query       VARCHAR(2048),
    created_at  DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),

    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

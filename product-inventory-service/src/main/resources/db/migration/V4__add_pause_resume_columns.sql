-- ============================================================
-- TMF637 Product Inventory - Pause/Resume Columns V4
-- Supports pausing a Product (status=suspended) with an
-- automatic resume date.
-- ============================================================

USE jio_product_inventory;

ALTER TABLE product
    ADD COLUMN pause_reason VARCHAR(255) NULL,
    ADD COLUMN resume_date  DATE         NULL;

ALTER TABLE product_status_history
    ADD COLUMN resume_date DATE NULL;

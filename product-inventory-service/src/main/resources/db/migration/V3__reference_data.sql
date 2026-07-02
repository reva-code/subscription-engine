-- ============================================================
-- TMF637 Product Inventory - Reference Data V3
-- Seed data for status transitions, event types, etc.
-- ============================================================

USE jio_product_inventory;

-- No lookup tables in TMF637 for status (it's an enum in code).
-- We insert a canonical test product (integration-test fixture only).
-- Production deployments should remove this block.

-- Valid status transition reference (documentation only via comment):
-- created        -> pendingActive, cancelled
-- pendingActive  -> active, cancelled, aborted
-- active         -> suspended, pendingTerminate
-- suspended      -> active, pendingTerminate, terminated
-- pendingTerminate -> terminated, active
-- terminated     -> (terminal state)
-- cancelled      -> (terminal state)
-- aborted        -> (terminal state)

-- Seed a system event_subscription placeholder (optional, for smoke test):
-- INSERT INTO event_subscription (id, callback, query, created_at)
-- VALUES ('00000000-0000-0000-0000-000000000001',
--         'http://localhost:8089/tmf-api/processFlow/v4/listener/productCreateEvent',
--         NULL,
--         NOW(6));

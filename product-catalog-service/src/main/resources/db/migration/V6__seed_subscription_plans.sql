-- Product Specification
INSERT INTO product_specification
(id, name, description, lifecycle_status)
VALUES
(
1,
'Netflix Premium Plan',
'4K Streaming Subscription',
'ACTIVE'
);

-- Product Offering
INSERT INTO product_offering
(id, name, description, lifecycle_status, product_specification_id)
VALUES
(
1,
'Netflix Summer Offer',
'Special Discount',
'ACTIVE',
1
);

-- Product Offering Price
INSERT INTO product_offering_price
(id, name, price, currency, recurring_charge_period, product_offering_id)
VALUES
(
1,
'Monthly Subscription',
799,
'INR',
'MONTHLY',
1
);

-- Category
INSERT INTO category
(id, name, description)
VALUES
(
1,
'Streaming Services',
'OTT subscription products'
);

-- Bundled Product Offering
INSERT INTO bundled_product_offering
(id, bundle_name, description, product_offering_id)
VALUES
(
1,
'Entertainment Bundle',
'Netflix + Spotify Combo',
1
);
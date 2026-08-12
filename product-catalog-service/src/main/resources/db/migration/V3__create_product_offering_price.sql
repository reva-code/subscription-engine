CREATE TABLE product_offering_price (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255),

    price DOUBLE,

    currency VARCHAR(255),

    recurring_charge_period VARCHAR(255),

    product_offering_id BIGINT,

    CONSTRAINT FK_price_offering
    FOREIGN KEY (product_offering_id)
    REFERENCES product_offering(id)

);
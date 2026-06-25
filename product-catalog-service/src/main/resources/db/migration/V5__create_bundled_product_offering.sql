CREATE TABLE bundled_product_offering (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    bundle_name VARCHAR(255),

    description VARCHAR(255),

    product_offering_id BIGINT,

    CONSTRAINT FK_bundle_offering
    FOREIGN KEY (product_offering_id)
    REFERENCES product_offering(id)

);
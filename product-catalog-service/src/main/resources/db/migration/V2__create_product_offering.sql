CREATE TABLE product_offering (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255),

    description VARCHAR(255),

    lifecycle_status VARCHAR(255),

    product_specification_id BIGINT,

    CONSTRAINT FK_product_offering_specification
    FOREIGN KEY (product_specification_id)
    REFERENCES product_specification(id)

);
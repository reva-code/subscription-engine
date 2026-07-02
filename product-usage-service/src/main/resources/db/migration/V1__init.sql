CREATE TABLE usage_consumption (
    id VARCHAR(255) PRIMARY KEY,
    href VARCHAR(255),
    creation_date DATETIME(6),
    description VARCHAR(255),
    last_update DATETIME(6),
    version INT,
    name VARCHAR(255),
    status VARCHAR(50),
    usage_type VARCHAR(255),
    product_ref LONGTEXT,
    related_party LONGTEXT,
    usage_consumption_product_ref LONGTEXT
);

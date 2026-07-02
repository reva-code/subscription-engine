CREATE TABLE IF NOT EXISTS related_party (
    db_id               VARCHAR(36)  NOT NULL,
    id                  VARCHAR(255) NOT NULL,
    href                VARCHAR(255),
    name                VARCHAR(255),
    role                VARCHAR(255),
    at_referred_type    VARCHAR(255) NOT NULL,
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (db_id)
);

CREATE TABLE IF NOT EXISTS party_role (
    id                  VARCHAR(36)  NOT NULL,
    href                VARCHAR(255),
    name                VARCHAR(255) NOT NULL,
    status              VARCHAR(255),
    status_reason       VARCHAR(255),
    role_type           VARCHAR(255),
    valid_for_start     DATETIME(6),
    valid_for_end       DATETIME(6),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    engaged_party_id    VARCHAR(36),
    created_date        DATETIME(6),
    last_modified_date  DATETIME(6),
    version             BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS contact_medium (
    id                  VARCHAR(36)  NOT NULL,
    medium_type         VARCHAR(255) NOT NULL,
    preferred           TINYINT(1),
    valid_for_start     DATETIME(6),
    valid_for_end       DATETIME(6),
    city                VARCHAR(255),
    contact_type        VARCHAR(255),
    country             VARCHAR(255),
    email_address       VARCHAR(255),
    fax_number          VARCHAR(255),
    phone_number        VARCHAR(255),
    post_code           VARCHAR(255),
    social_network_id   VARCHAR(255),
    state_or_province   VARCHAR(255),
    street1             VARCHAR(255),
    street2             VARCHAR(255),
    mc_at_base_type     VARCHAR(255),
    mc_at_schema_location VARCHAR(255),
    mc_at_type          VARCHAR(255),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS credit_profile (
    id                  VARCHAR(36) NOT NULL,
    credit_profile_date DATETIME(6) NOT NULL,
    credit_risk_rating  INTEGER,
    credit_score        INTEGER,
    valid_for_start     DATETIME(6),
    valid_for_end       DATETIME(6),
    at_base_type        VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS characteristic (
    id                  VARCHAR(36)  NOT NULL,
    name                VARCHAR(255) NOT NULL,
    value               TEXT         NOT NULL,
    value_type          VARCHAR(255),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS account_ref (
    db_id               VARCHAR(36)  NOT NULL,
    id                  VARCHAR(255) NOT NULL,
    href                VARCHAR(255),
    name                VARCHAR(255),
    description         VARCHAR(255),
    at_referred_type    VARCHAR(255),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (db_id)
);

CREATE TABLE IF NOT EXISTS payment_method_ref (
    db_id               VARCHAR(36)  NOT NULL,
    id                  VARCHAR(255) NOT NULL,
    href                VARCHAR(255),
    name                VARCHAR(255),
    at_referred_type    VARCHAR(255),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (db_id)
);

CREATE TABLE IF NOT EXISTS agreement_ref (
    db_id               VARCHAR(36)  NOT NULL,
    id                  VARCHAR(255) NOT NULL,
    href                VARCHAR(255),
    name                VARCHAR(255),
    at_referred_type    VARCHAR(255),
    at_base_type        VARCHAR(255),
    at_schema_location  VARCHAR(255),
    at_type             VARCHAR(255),
    party_role_id       VARCHAR(36),
    PRIMARY KEY (db_id)
);

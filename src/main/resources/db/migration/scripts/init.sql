-- liquibase formatted sql

-- changeset create tables:1

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL UNIQUE,
    masked_number VARCHAR(255) NOT NULL,
    expired_at VARCHAR(55) NOT NULL,
    status VARCHAR(50) NOT NULL,
    balance DECIMAL NOT NULL,
    owner_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE transfers (
    id BIGSERIAL,
    from_card_id BIGINT,
    to_card_id BIGINT,
    amount DECIMAL NOT NULL,
    operation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_card_id) REFERENCES cards(id),
    FOREIGN KEY (to_card_id) REFERENCES cards(id)
);
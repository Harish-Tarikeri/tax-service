CREATE TABLE transactions (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id VARCHAR(50),
    customer_id VARCHAR(50),
    amount DECIMAL(10,2),
    tax DECIMAL(10,2),
    transaction_type VARCHAR(20),
    date DATE
);

CREATE TABLE tax_exception (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_id VARCHAR(50),
    rule_name VARCHAR(100),
    severity VARCHAR(20),
    message TEXT
);

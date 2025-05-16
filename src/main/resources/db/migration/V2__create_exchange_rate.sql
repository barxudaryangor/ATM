CREATE TABLE exchange_rate (
    id SERIAL PRIMARY KEY,
    usd_to_amd DOUBLE PRECISION NOT NULL,
    local_date_time TIMESTAMP NOT NULL
);
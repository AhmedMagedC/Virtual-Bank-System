CREATE DATABASE account_service_db;
CREATE DATABASE transaction_service_db;
CREATE DATABASE user_service_db;
CREATE DATABASE logging_service_db;

\connect account_service_db

-- Create a sequence inside the account_service_db
CREATE SEQUENCE account_number_seq START 100 INCREMENT 1;

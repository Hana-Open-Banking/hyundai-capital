-- Drop tables if they exist
DROP TABLE IF EXISTS loan_transaction;
DROP TABLE IF EXISTS loan_contract;
DROP TABLE IF EXISTS oauth_token;
DROP TABLE IF EXISTS oauth_client;
DROP TABLE IF EXISTS user;

-- Create user table
CREATE TABLE user (
    user_seq_no VARCHAR(10) PRIMARY KEY,
    user_ci VARCHAR(100) NOT NULL UNIQUE,
    user_name VARCHAR(20) NOT NULL,
    user_reg_num VARCHAR(50) NOT NULL UNIQUE,
    gender VARCHAR(6),
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    user_email VARCHAR(100) NOT NULL UNIQUE,
    address VARCHAR(100),
    user_di VARCHAR(100) NOT NULL,
    user_status VARCHAR(10) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create oauth_client table
CREATE TABLE oauth_client (
    client_id VARCHAR(10) PRIMARY KEY,
    client_secret VARCHAR(100) NOT NULL,
    client_name VARCHAR(100) NOT NULL,
    redirect_uri VARCHAR(200),
    client_status VARCHAR(10) DEFAULT 'ACTIVE',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Create oauth_token table
CREATE TABLE oauth_token (
    access_token_id VARCHAR(100) PRIMARY KEY,
    refresh_token VARCHAR(100) NOT NULL,
    scope VARCHAR(50) NOT NULL,
    expires_in INT NOT NULL,
    issued_at DATETIME NOT NULL,
    refreshed_at DATETIME,
    token_status VARCHAR(10) DEFAULT 'ACTIVE',
    user_seq_no VARCHAR(10) NOT NULL,
    client_id VARCHAR(10) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_seq_no) REFERENCES user(user_seq_no),
    FOREIGN KEY (client_id) REFERENCES oauth_client(client_id)
);

-- Create loan_contract table
CREATE TABLE loan_contract (
    loan_id VARCHAR(20) PRIMARY KEY,
    loan_account_num VARCHAR(20) NOT NULL UNIQUE,
    loan_product_name VARCHAR(100) NOT NULL,
    loan_amount DECIMAL(15,2) NOT NULL,
    remaining_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2) NOT NULL,
    contract_date CHAR(8) NOT NULL,
    maturity_date CHAR(8) NOT NULL,
    repayment_day INT NOT NULL,
    loan_status VARCHAR(10) DEFAULT 'ACTIVE',
    loan_type VARCHAR(20) NOT NULL,
    user_seq_no VARCHAR(10) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_seq_no) REFERENCES user(user_seq_no)
);

-- Create loan_transaction table
CREATE TABLE loan_transaction (
    transaction_id VARCHAR(20) PRIMARY KEY,
    transaction_unique_no VARCHAR(20) NOT NULL UNIQUE,
    transaction_date DATETIME NOT NULL,
    transaction_amount DECIMAL(15,2) NOT NULL,
    after_balance DECIMAL(15,2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    transaction_summary VARCHAR(100) NOT NULL,
    branch_name VARCHAR(50),
    loan_id VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (loan_id) REFERENCES loan_contract(loan_id)
);

-- Create indexes
CREATE INDEX idx_user_ci ON user(user_ci);
CREATE INDEX idx_user_email ON user(user_email);
CREATE INDEX idx_loan_account_num ON loan_contract(loan_account_num);
CREATE INDEX idx_transaction_unique_no ON loan_transaction(transaction_unique_no);
CREATE INDEX idx_oauth_token_refresh ON oauth_token(refresh_token);

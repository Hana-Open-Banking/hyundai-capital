-- Insert test OAuth clients
INSERT INTO oauth_client (client_id, client_secret, client_name, redirect_uri, client_status)
VALUES 
('CLIENT001', '$2a$10$encrypted_secret_1', '현대캐피탈 오픈뱅킹', 'https://hyundai-capital.com/callback', 'ACTIVE'),
('CLIENT002', '$2a$10$encrypted_secret_2', '금융결제원', 'https://kftc.or.kr/callback', 'ACTIVE');

-- Insert test users
INSERT INTO user (user_seq_no, user_ci, user_name, user_reg_num, gender, password, phone_number, user_email, address, user_di, user_status)
VALUES 
('1000000001', 'CI_12345678901234567890', '김현대', '1', 'M', '$2a$10$encrypted_password_1', '010-1234-5678', 'kim@example.com', '서울시 강남구 테헤란로 123', 'DI_12345678901234567890', 'ACTIVE'),
('1000000002', 'CI_09876543210987654321', '이캐피탈', '2', 'F', '$2a$10$encrypted_password_2', '010-9876-5432', 'lee@example.com', '서울시 서초구 서초대로 456', 'DI_09876543210987654321', 'ACTIVE');

-- Insert test loan contracts
INSERT INTO loan_contract (loan_id, loan_account_num, loan_product_name, loan_amount, remaining_amount, interest_rate, contract_date, maturity_date, repayment_day, loan_status, loan_type, user_seq_no)
VALUES 
('LOAN20240001', 'HC12345678901234567', '개인신용대출', 10000000.00, 8500000.00, 4.50, '20240101', '20270101', 15, 'ACTIVE', 'PERSONAL_LOAN', '1000000001'),
('LOAN20240002', 'HC98765432109876543', '자동차대출', 25000000.00, 20000000.00, 3.80, '20231201', '20281201', 25, 'ACTIVE', 'AUTO_LOAN', '1000000001'),
('LOAN20240003', 'HC45678901234567890', '주택담보대출', 150000000.00, 145000000.00, 3.20, '20240201', '20340201', 10, 'ACTIVE', 'MORTGAGE_LOAN', '1000000002');

-- Insert test loan transactions
INSERT INTO loan_transaction (transaction_id, transaction_unique_no, transaction_date, transaction_amount, after_balance, transaction_type, transaction_summary, branch_name, loan_id)
VALUES 
('TXN20240001', 'TXN12345678901234567', '2024-01-01 10:00:00', 10000000.00, 10000000.00, 'LOAN_EXECUTION', '대출실행', '강남지점', 'LOAN20240001'),
('TXN20240002', 'TXN23456789012345678', '2024-02-15 14:30:00', 500000.00, 9500000.00, 'TOTAL_PAYMENT', '정기상환 1회차', '온라인', 'LOAN20240001'),
('TXN20240003', 'TXN34567890123456789', '2024-03-15 14:30:00', 500000.00, 9000000.00, 'TOTAL_PAYMENT', '정기상환 2회차', '온라인', 'LOAN20240001'),
('TXN20240004', 'TXN45678901234567890', '2024-04-15 14:30:00', 500000.00, 8500000.00, 'TOTAL_PAYMENT', '정기상환 3회차', '온라인', 'LOAN20240001'),
('TXN20240005', 'TXN56789012345678901', '2023-12-01 11:00:00', 25000000.00, 25000000.00, 'LOAN_EXECUTION', '대출실행', '서초지점', 'LOAN20240002'),
('TXN20240006', 'TXN67890123456789012', '2024-01-25 15:00:00', 1250000.00, 23750000.00, 'TOTAL_PAYMENT', '정기상환 1회차', '온라인', 'LOAN20240002'),
('TXN20240007', 'TXN78901234567890123', '2024-02-25 15:00:00', 1250000.00, 22500000.00, 'TOTAL_PAYMENT', '정기상환 2회차', '온라인', 'LOAN20240002'),
('TXN20240008', 'TXN89012345678901234', '2024-03-25 15:00:00', 1250000.00, 21250000.00, 'TOTAL_PAYMENT', '정기상환 3회차', '온라인', 'LOAN20240002'),
('TXN20240009', 'TXN90123456789012345', '2024-04-25 15:00:00', 1250000.00, 20000000.00, 'TOTAL_PAYMENT', '정기상환 4회차', '온라인', 'LOAN20240002'),
('TXN20240010', 'TXN01234567890123456', '2024-02-01 09:30:00', 150000000.00, 150000000.00, 'LOAN_EXECUTION', '대출실행', '여의도지점', 'LOAN20240003'),
('TXN20240011', 'TXN12345098765432109', '2024-03-10 13:45:00', 5000000.00, 145000000.00, 'TOTAL_PAYMENT', '정기상환 1회차', '온라인', 'LOAN20240003');

-- Insert test OAuth tokens
INSERT INTO oauth_token (access_token_id, refresh_token, scope, expires_in, issued_at, token_status, user_seq_no, client_id, created_at)
VALUES 
('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDAwMDAwMDAxIn0.abcdefghijklmnopqrstuvwxyz123456', 
 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDAwMDAwMDAxIiwidHlwZSI6InJlZnJlc2gifQ.123456abcdefghijklmnopqrstuvwxyz', 
 'read', 3600, NOW(), 'ACTIVE', '1000000001', 'CLIENT001', NOW()),
('eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDAwMDAwMDAyIn0.zyxwvutsrqponmlkjihgfedcba654321', 
 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMDAwMDAwMDAyIiwidHlwZSI6InJlZnJlc2gifQ.654321zyxwvutsrqponmlkjihgfedcba', 
 'read write', 3600, NOW(), 'ACTIVE', '1000000002', 'CLIENT002', NOW());
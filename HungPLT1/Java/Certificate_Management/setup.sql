-- ============================================================
-- FILE: setup.sql
-- Certificate Management System - Database Setup Script
-- ============================================================

-- 1. Tao database
DROP DATABASE IF EXISTS certificate_management;
CREATE DATABASE certificate_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE certificate_management;

-- ============================================================
-- 2. Tao bang users
-- ============================================================
CREATE TABLE users (
    id          VARCHAR(10)  PRIMARY KEY,
    full_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(20)  NOT NULL,
    email       VARCHAR(100) NOT NULL,
    password    VARCHAR(100) NOT NULL
);

-- ============================================================
-- 3. Tao bang certificates
-- ============================================================
CREATE TABLE certificates (
    id                  VARCHAR(10)  PRIMARY KEY,
    certificate_name    VARCHAR(100) NOT NULL,
    certificate_number  VARCHAR(100) NOT NULL,
    issue_date          VARCHAR(20)  NOT NULL,
    expiry_date         VARCHAR(20)  NOT NULL,
    score               DOUBLE       NOT NULL,
    user_id             VARCHAR(10)  NOT NULL,
    CONSTRAINT fk_cert_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ============================================================
-- 4. Stored Procedures cho USERS (ho tro)
-- ============================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS insert_user$$
CREATE PROCEDURE insert_user(
    IN p_id        VARCHAR(10),
    IN p_full_name VARCHAR(100),
    IN p_phone     VARCHAR(20),
    IN p_email     VARCHAR(100),
    IN p_password  VARCHAR(100)
)
BEGIN
    INSERT INTO users(id, full_name, phone, email, password)
    VALUES(p_id, p_full_name, p_phone, p_email, p_password);
END$$

DROP PROCEDURE IF EXISTS find_user_by_id$$
CREATE PROCEDURE find_user_by_id(
    IN p_id VARCHAR(10)
)
BEGIN
    SELECT * FROM users WHERE id = p_id;
END$$

-- ============================================================
-- 5. Stored Procedures cho CERTIFICATES (5 stored procedures chinh)
-- ============================================================

-- SP 1: insert_certificate
DROP PROCEDURE IF EXISTS insert_certificate$$
CREATE PROCEDURE insert_certificate(
    IN p_id                 VARCHAR(10),
    IN p_certificate_name   VARCHAR(100),
    IN p_certificate_number VARCHAR(100),
    IN p_issue_date         VARCHAR(20),
    IN p_expiry_date        VARCHAR(20),
    IN p_score              DOUBLE,
    IN p_user_id            VARCHAR(10)
)
BEGIN
    INSERT INTO certificates(id, certificate_name, certificate_number, issue_date, expiry_date, score, user_id)
    VALUES(p_id, p_certificate_name, p_certificate_number, p_issue_date, p_expiry_date, p_score, p_user_id);
END$$

-- SP 2: get_all_certificates
DROP PROCEDURE IF EXISTS get_all_certificates$$
CREATE PROCEDURE get_all_certificates()
BEGIN
    SELECT c.id, c.certificate_name, c.certificate_number,
           c.issue_date, c.expiry_date, c.score, c.user_id
    FROM certificates c
    ORDER BY c.id;
END$$

-- SP 3: update_certificate
DROP PROCEDURE IF EXISTS update_certificate$$
CREATE PROCEDURE update_certificate(
    IN p_id                 VARCHAR(10),
    IN p_certificate_name   VARCHAR(100),
    IN p_certificate_number VARCHAR(100),
    IN p_issue_date         VARCHAR(20),
    IN p_expiry_date        VARCHAR(20),
    IN p_score              DOUBLE,
    IN p_user_id            VARCHAR(10)
)
BEGIN
    UPDATE certificates
    SET certificate_name   = p_certificate_name,
        certificate_number = p_certificate_number,
        issue_date         = p_issue_date,
        expiry_date        = p_expiry_date,
        score              = p_score,
        user_id            = p_user_id
    WHERE id = p_id;
END$$

-- SP 4: delete_certificate
DROP PROCEDURE IF EXISTS delete_certificate$$
CREATE PROCEDURE delete_certificate(
    IN p_id VARCHAR(10)
)
BEGIN
    DELETE FROM certificates WHERE id = p_id;
END$$

-- SP 5: find_certificate_by_id
DROP PROCEDURE IF EXISTS find_certificate_by_id$$
CREATE PROCEDURE find_certificate_by_id(
    IN p_id VARCHAR(10)
)
BEGIN
    SELECT c.id, c.certificate_name, c.certificate_number,
           c.issue_date, c.expiry_date, c.score, c.user_id
    FROM certificates c
    WHERE c.id = p_id;
END$$

DELIMITER ;

-- ============================================================
-- 6. Du lieu mau de test
-- ============================================================
INSERT INTO users(id, full_name, phone, email, password) VALUES
    ('U001', 'Nguyen Van A', '0901234567', 'nguyenvana@email.com', '123456'),
    ('U002', 'Tran Thi B',   '0912345678', 'tranthib@email.com',  '123456');

INSERT INTO certificates(id, certificate_name, certificate_number, issue_date, expiry_date, score, user_id) VALUES
    ('C001', 'Java OCP',           'CERT-2024-001', '2024-01-10', '2027-01-10', 3.8, 'U001'),
    ('C002', 'AWS Cloud Practitioner', 'CERT-2024-002', '2024-03-15', '2027-03-15', 3.5, 'U002');

SELECT 'Database setup completed successfully!' AS Status;

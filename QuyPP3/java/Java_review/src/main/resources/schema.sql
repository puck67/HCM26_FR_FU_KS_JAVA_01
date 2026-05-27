/* 1. Create Database (MySQL syntax) */
CREATE DATABASE IF NOT EXISTS productdb;
USE productdb;

/* 2. Create Table */
CREATE TABLE IF NOT EXISTS Product (
    id       VARCHAR(10)  PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    price    DOUBLE       NOT NULL,
    quantity INT          NOT NULL,
    category VARCHAR(50)  NOT NULL
);

DELIMITER $$

/* SP 1: Insert product */
CREATE PROCEDURE insert_product(
    IN p_id       VARCHAR(10),
    IN p_name     VARCHAR(100),
    IN p_price    DOUBLE,
    IN p_quantity INT,
    IN p_category VARCHAR(50)
)
BEGIN
    INSERT INTO Product(id, name, price, quantity, category)
    VALUES (p_id, p_name, p_price, p_quantity, p_category);
END$$

/* SP 2: Get all products */
CREATE PROCEDURE get_all_products()
BEGIN
    SELECT * FROM Product ORDER BY id;
END$$

/* SP 3: Update product */
CREATE PROCEDURE update_product(
    IN p_id       VARCHAR(10),
    IN p_name     VARCHAR(100),
    IN p_price    DOUBLE,
    IN p_quantity INT,
    IN p_category VARCHAR(50)
)
BEGIN
    UPDATE Product
    SET name = p_name, price = p_price, quantity = p_quantity, category = p_category
    WHERE id = p_id;
END$$

/* SP 4: Delete product */
CREATE PROCEDURE delete_product(
    IN p_id VARCHAR(10)
)
BEGIN
    DELETE FROM Product WHERE id = p_id;
END$$

/* SP 5: Find product by ID */
CREATE PROCEDURE find_product_by_id(
    IN p_id VARCHAR(10)
)
BEGIN
    SELECT * FROM Product WHERE id = p_id;
END$$

DELIMITER ;

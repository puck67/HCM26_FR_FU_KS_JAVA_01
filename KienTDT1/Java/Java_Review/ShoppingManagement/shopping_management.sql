-- Database: shopping_management
CREATE DATABASE IF NOT EXISTS shopping_management;
USE shopping_management;

-- Table: shopping_item
CREATE TABLE IF NOT EXISTS shopping_item (
    id VARCHAR(50) PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- Stored Procedures

-- 1. Insert Shopping Item
DROP PROCEDURE IF EXISTS insert_shopping_item;
DELIMITER //
CREATE PROCEDURE insert_shopping_item(
    IN p_id VARCHAR(50),
    IN p_item_name VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_price DOUBLE,
    IN p_quantity INT
)
BEGIN
    INSERT INTO shopping_item (id, item_name, category, price, quantity)
    VALUES (p_id, p_item_name, p_category, p_price, p_quantity);
END //
DELIMITER ;

-- 2. Get All Shopping Items
DROP PROCEDURE IF EXISTS get_all_shopping_items;
DELIMITER //
CREATE PROCEDURE get_all_shopping_items()
BEGIN
    SELECT id, item_name, category, price, quantity FROM shopping_item;
END //
DELIMITER ;

-- 3. Update Shopping Item
DROP PROCEDURE IF EXISTS update_shopping_item;
DELIMITER //
CREATE PROCEDURE update_shopping_item(
    IN p_id VARCHAR(50),
    IN p_item_name VARCHAR(100),
    IN p_category VARCHAR(50),
    IN p_price DOUBLE,
    IN p_quantity INT
)
BEGIN
    UPDATE shopping_item
    SET item_name = p_item_name,
        category = p_category,
        price = p_price,
        quantity = p_quantity
    WHERE id = p_id;
END //
DELIMITER ;

-- 4. Delete Shopping Item
DROP PROCEDURE IF EXISTS delete_shopping_item;
DELIMITER //
CREATE PROCEDURE delete_shopping_item(
    IN p_id VARCHAR(50)
)
BEGIN
    DELETE FROM shopping_item WHERE id = p_id;
END //
DELIMITER ;

-- 5. Find Shopping Item By ID
DROP PROCEDURE IF EXISTS find_shopping_item_by_id;
DELIMITER //
CREATE PROCEDURE find_shopping_item_by_id(
    IN p_id VARCHAR(50)
)
BEGIN
    SELECT id, item_name, category, price, quantity
    FROM shopping_item
    WHERE id = p_id;
END //
DELIMITER ;

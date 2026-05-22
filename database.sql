-- Create database
CREATE DATABASE IF NOT EXISTS teacher_management;
USE teacher_management;

DROP TABLE IF EXISTS teachers;

CREATE TABLE teachers (
    id VARCHAR(10) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL
);

-- Drop existing procedures
DROP PROCEDURE IF EXISTS insert_teacher;
DROP PROCEDURE IF EXISTS get_all_teachers;
DROP PROCEDURE IF EXISTS update_teacher;
DROP PROCEDURE IF EXISTS delete_teacher;
DROP PROCEDURE IF EXISTS find_teacher_by_id;
DROP PROCEDURE IF EXISTS find_teacher_by_email;
DROP PROCEDURE IF EXISTS find_teacher_by_phone;

DELIMITER $$

CREATE PROCEDURE insert_teacher(IN p_id VARCHAR(10), IN p_name VARCHAR(100), IN p_email VARCHAR(100), IN p_phone VARCHAR(20), IN p_salary DOUBLE)
BEGIN
    INSERT INTO teachers (id, name, email, phone, salary)
    VALUES (p_id, p_name, p_email, p_phone, p_salary);
END$$

CREATE PROCEDURE get_all_teachers()
BEGIN
    SELECT * FROM teachers ORDER BY id;
END$$

CREATE PROCEDURE update_teacher(IN p_id VARCHAR(10), IN p_name VARCHAR(100), IN p_email VARCHAR(100), IN p_phone VARCHAR(20), IN p_salary DOUBLE)
BEGIN
    UPDATE teachers
    SET name = p_name, email = p_email, phone = p_phone, salary = p_salary
    WHERE id = p_id;
END$$

CREATE PROCEDURE delete_teacher(IN p_id VARCHAR(10))
BEGIN
    DELETE FROM teachers WHERE id = p_id;
END$$

CREATE PROCEDURE find_teacher_by_id(IN p_id VARCHAR(10))
BEGIN
    SELECT * FROM teachers WHERE id = p_id;
END$$

CREATE PROCEDURE find_teacher_by_email(IN p_email VARCHAR(100))
BEGIN
    SELECT * FROM teachers WHERE email = p_email;
END$$

CREATE PROCEDURE find_teacher_by_phone(IN p_phone VARCHAR(20))
BEGIN
    SELECT * FROM teachers WHERE phone = p_phone;
END$$

DELIMITER ;

DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS warehouses;

CREATE TABLE warehouses (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200) NOT NULL,
    capacity INT NOT NULL CHECK (capacity > 0)
);

CREATE TABLE employees (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    warehouse_id VARCHAR(10),
    CONSTRAINT fk_employee_warehouse FOREIGN KEY (warehouse_id) 
        REFERENCES warehouses(id) ON DELETE SET NULL
);

CREATE TABLE products (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION NOT NULL CHECK (price > 0),
    quantity INT NOT NULL CHECK (quantity >= 0),
    warehouse_id VARCHAR(10),
    CONSTRAINT fk_product_warehouse FOREIGN KEY (warehouse_id) 
        REFERENCES warehouses(id) ON DELETE CASCADE
);

CREATE OR REPLACE PROCEDURE insert_warehouse(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_address VARCHAR(200),
    p_capacity INT
) AS $$
BEGIN
    INSERT INTO warehouses(id, name, address, capacity)
    VALUES(p_id, p_name, p_address, p_capacity);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_all_warehouses()
RETURNS TABLE(
    id VARCHAR(10), 
    name VARCHAR(100), 
    address VARCHAR(200), 
    capacity INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT w.id, w.name, w.address, w.capacity 
    FROM warehouses w
    ORDER BY w.id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE update_warehouse(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_address VARCHAR(200),
    p_capacity INT
) AS $$
BEGIN
    UPDATE warehouses
    SET name = p_name, address = p_address, capacity = p_capacity
    WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE delete_warehouse(
    p_id VARCHAR(10)
) AS $$
BEGIN
    DELETE FROM warehouses WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION find_warehouse_by_id(
    p_id VARCHAR(10)
)
RETURNS TABLE(
    id VARCHAR(10), 
    name VARCHAR(100), 
    address VARCHAR(200), 
    capacity INT
) AS $$
BEGIN
    RETURN QUERY 
    SELECT w.id, w.name, w.address, w.capacity 
    FROM warehouses w 
    WHERE w.id = p_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE PROCEDURE insert_employee(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_email VARCHAR(100),
    p_phone VARCHAR(20),
    p_warehouse_id VARCHAR(10)
) AS $$
BEGIN
    INSERT INTO employees(id, name, email, phone, warehouse_id)
    VALUES(p_id, p_name, p_email, p_phone, p_warehouse_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_employees_by_warehouse(
    p_warehouse_id VARCHAR(10)
)
RETURNS TABLE(
    id VARCHAR(10),
    name VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20),
    warehouse_id VARCHAR(10)
) AS $$
BEGIN
    RETURN QUERY
    SELECT e.id, e.name, e.email, e.phone, e.warehouse_id
    FROM employees e
    WHERE e.warehouse_id = p_warehouse_id
    ORDER BY e.id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE insert_product(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_price DOUBLE PRECISION,
    p_quantity INT,
    p_warehouse_id VARCHAR(10)
) AS $$
BEGIN
    INSERT INTO products(id, name, price, quantity, warehouse_id)
    VALUES(p_id, p_name, p_price, p_quantity, p_warehouse_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_products_by_warehouse(
    p_warehouse_id VARCHAR(10)
)
RETURNS TABLE(
    id VARCHAR(10),
    name VARCHAR(100),
    price DOUBLE PRECISION,
    quantity INT,
    warehouse_id VARCHAR(10)
) AS $$
BEGIN
    RETURN QUERY
    SELECT p.id, p.name, p.price, p.quantity, p.warehouse_id
    FROM products p
    WHERE p.warehouse_id = p_warehouse_id
    ORDER BY p.id;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_total_quantity_in_warehouse(
    p_warehouse_id VARCHAR(10)
)
RETURNS INT AS $$
DECLARE
    total_qty INT;
BEGIN
    SELECT COALESCE(SUM(quantity), 0) INTO total_qty
    FROM products
    WHERE warehouse_id = p_warehouse_id;
    RETURN total_qty;
END;
$$ LANGUAGE plpgsql;

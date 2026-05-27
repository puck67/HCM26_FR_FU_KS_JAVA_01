DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS manufacturers;

CREATE TABLE manufacturers (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country VARCHAR(50) NOT NULL
);

CREATE TABLE categories (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE vehicles (
    id VARCHAR(10) PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    price DOUBLE PRECISION NOT NULL CHECK (price > 0),
    owner_email VARCHAR(100) NOT NULL,
    owner_phone VARCHAR(20) NOT NULL,
    manufacturer_id VARCHAR(10) REFERENCES manufacturers(id) ON DELETE RESTRICT,
    category_id VARCHAR(10) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE OR REPLACE PROCEDURE insert_manufacturer(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_country VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO manufacturers (id, name, country)
    VALUES (p_id, p_name, p_country);
END;
$$;

CREATE OR REPLACE PROCEDURE update_manufacturer(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_country VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE manufacturers
    SET name = p_name,
        country = p_country
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_manufacturer(
    p_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM manufacturers WHERE id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_manufacturers()
RETURNS TABLE (
    id VARCHAR(10),
    name VARCHAR(100),
    country VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT m.id, m.name, m.country FROM manufacturers m ORDER BY m.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_manufacturer_by_id(p_id VARCHAR(10))
RETURNS TABLE (
    id VARCHAR(10),
    name VARCHAR(100),
    country VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT m.id, m.name, m.country FROM manufacturers m WHERE m.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_category(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_description VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO categories (id, name, description)
    VALUES (p_id, p_name, p_description);
END;
$$;

CREATE OR REPLACE PROCEDURE update_category(
    p_id VARCHAR(10),
    p_name VARCHAR(100),
    p_description VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE categories
    SET name = p_name,
        description = p_description
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_category(
    p_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM categories WHERE id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_categories()
RETURNS TABLE (
    id VARCHAR(10),
    name VARCHAR(100),
    description VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id, c.name, c.description FROM categories c ORDER BY c.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_category_by_id(p_id VARCHAR(10))
RETURNS TABLE (
    id VARCHAR(10),
    name VARCHAR(100),
    description VARCHAR(255)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id, c.name, c.description FROM categories c WHERE c.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_vehicle(
    p_id VARCHAR(10),
    p_model VARCHAR(100),
    p_price DOUBLE PRECISION,
    p_owner_email VARCHAR(100),
    p_owner_phone VARCHAR(20),
    p_manufacturer_id VARCHAR(10),
    p_category_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO vehicles (id, model, price, owner_email, owner_phone, manufacturer_id, category_id)
    VALUES (p_id, p_model, p_price, p_owner_email, p_owner_phone, p_manufacturer_id, p_category_id);
END;
$$;

CREATE OR REPLACE PROCEDURE update_vehicle(
    p_id VARCHAR(10),
    p_model VARCHAR(100),
    p_price DOUBLE PRECISION,
    p_owner_email VARCHAR(100),
    p_owner_phone VARCHAR(20),
    p_manufacturer_id VARCHAR(10),
    p_category_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE vehicles
    SET model = p_model,
        price = p_price,
        owner_email = p_owner_email,
        owner_phone = p_owner_phone,
        manufacturer_id = p_manufacturer_id,
        category_id = p_category_id
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_vehicle(
    p_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM vehicles WHERE id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_vehicles()
RETURNS TABLE (
    id VARCHAR(10),
    model VARCHAR(100),
    price DOUBLE PRECISION,
    owner_email VARCHAR(100),
    owner_phone VARCHAR(20),
    manufacturer_id VARCHAR(10),
    category_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY 
    SELECT v.id, v.model, v.price, v.owner_email, v.owner_phone, v.manufacturer_id, v.category_id 
    FROM vehicles v
    ORDER BY v.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_vehicle_by_id(p_id VARCHAR(10))
RETURNS TABLE (
    id VARCHAR(10),
    model VARCHAR(100),
    price DOUBLE PRECISION,
    owner_email VARCHAR(100),
    owner_phone VARCHAR(20),
    manufacturer_id VARCHAR(10),
    category_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY 
    SELECT v.id, v.model, v.price, v.owner_email, v.owner_phone, v.manufacturer_id, v.category_id 
    FROM vehicles v 
    WHERE v.id = p_id;
END;
$$;

CALL insert_manufacturer('M01', 'Toyota', 'Japan');
CALL insert_manufacturer('M02', 'Honda', 'Japan');
CALL insert_manufacturer('M03', 'Ford', 'USA');
CALL insert_manufacturer('M04', 'Tesla', 'USA');

CALL insert_category('C01', 'Sedan', 'Comfortable 4-door passenger car');
CALL insert_category('C02', 'SUV', 'Sport Utility Vehicle with higher ground clearance');
CALL insert_category('C03', 'Truck', 'Large vehicle for transporting cargo');
CALL insert_category('C04', 'Electric', 'Battery powered electric vehicle');

CALL insert_vehicle('V01', 'Camry', 35000.0, 'john.doe@example.com', '0912345678', 'M01', 'C01');
CALL insert_vehicle('V02', 'Civic', 28000.0, 'jane.smith@example.com', '0987654321', 'M02', 'C01');
CALL insert_vehicle('V03', 'F-150', 45000.0, 'cargo.boss@example.com', '0901112222', 'M03', 'C03');
CALL insert_vehicle('V04', 'Model Y', 55000.0, 'elon@tesla.com', '0999888777', 'M04', 'C04');


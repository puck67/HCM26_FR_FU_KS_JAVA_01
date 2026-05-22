DROP FUNCTION IF EXISTS find_books_by_title(VARCHAR);
DROP FUNCTION IF EXISTS find_book_by_id(VARCHAR);
DROP FUNCTION IF EXISTS get_all_books();
DROP FUNCTION IF EXISTS count_books();

DROP PROCEDURE IF EXISTS insert_book(VARCHAR, VARCHAR, INT, INT, INT, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS update_book(VARCHAR, VARCHAR, INT, INT, INT, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS delete_book(VARCHAR);

DROP FUNCTION IF EXISTS find_author_by_id(INT);
DROP FUNCTION IF EXISTS get_all_authors();
DROP FUNCTION IF EXISTS insert_author(VARCHAR);

DROP FUNCTION IF EXISTS find_category_by_id(INT);
DROP FUNCTION IF EXISTS get_all_categories();
DROP FUNCTION IF EXISTS insert_category(VARCHAR);

DROP TABLE IF EXISTS books CASCADE;
DROP TABLE IF EXISTS authors CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

CREATE TABLE authors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE books (
    id VARCHAR(10) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author_id INT REFERENCES authors(id) ON DELETE SET NULL,
    category_id INT REFERENCES categories(id) ON DELETE SET NULL,
    publish_year INT CHECK (publish_year >= 1900),
    price DOUBLE PRECISION CHECK (price > 0)
);

CREATE OR REPLACE FUNCTION insert_author(p_name VARCHAR)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO authors(name)
    VALUES(p_name)
    RETURNING id INTO v_id;

    RETURN v_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_authors()
RETURNS TABLE(
    id INT,
    name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT a.id, a.name
    FROM authors a
    ORDER BY a.id;
END;
$$;

CREATE OR REPLACE FUNCTION insert_category(p_name VARCHAR)
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO categories(name)
    VALUES(p_name)
    RETURNING id INTO v_id;

    RETURN v_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_categories()
RETURNS TABLE(
    id INT,
    name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id, c.name
    FROM categories c
    ORDER BY c.id;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_book(
    p_id VARCHAR,
    p_title VARCHAR,
    p_author_id INT,
    p_category_id INT,
    p_publish_year INT,
    p_price DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO books(
        id,
        title,
        author_id,
        category_id,
        publish_year,
        price
    )
    VALUES(
        p_id,
        p_title,
        p_author_id,
        p_category_id,
        p_publish_year,
        p_price
    );
END;
$$;

CREATE OR REPLACE FUNCTION get_all_books()
RETURNS TABLE(
    id VARCHAR,
    title VARCHAR,
    author_id INT,
    author_name VARCHAR,
    category_id INT,
    category_name VARCHAR,
    publish_year INT,
    price DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.title,
        b.author_id,
        a.name AS author_name,
        b.category_id,
        c.name AS category_name,
        b.publish_year,
        b.price
    FROM books b
    LEFT JOIN authors a
        ON b.author_id = a.id
    LEFT JOIN categories c
        ON b.category_id = c.id
    ORDER BY b.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_book_by_id(p_id VARCHAR)
RETURNS TABLE(
    id VARCHAR,
    title VARCHAR,
    author_id INT,
    author_name VARCHAR,
    category_id INT,
    category_name VARCHAR,
    publish_year INT,
    price DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.title,
        b.author_id,
        a.name AS author_name,
        b.category_id,
        c.name AS category_name,
        b.publish_year,
        b.price
    FROM books b
    LEFT JOIN authors a
        ON b.author_id = a.id
    LEFT JOIN categories c
        ON b.category_id = c.id
    WHERE b.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION find_books_by_title(p_title VARCHAR)
RETURNS TABLE(
    id VARCHAR,
    title VARCHAR,
    author_id INT,
    author_name VARCHAR,
    category_id INT,
    category_name VARCHAR,
    publish_year INT,
    price DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT
        b.id,
        b.title,
        b.author_id,
        a.name AS author_name,
        b.category_id,
        c.name AS category_name,
        b.publish_year,
        b.price
    FROM books b
    LEFT JOIN authors a
        ON b.author_id = a.id
    LEFT JOIN categories c
        ON b.category_id = c.id
    WHERE LOWER(b.title)
        LIKE LOWER('%' || p_title || '%');
END;
$$;

CREATE OR REPLACE PROCEDURE update_book(
    p_id VARCHAR,
    p_title VARCHAR,
    p_author_id INT,
    p_category_id INT,
    p_publish_year INT,
    p_price DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE books
    SET
        title = p_title,
        author_id = p_author_id,
        category_id = p_category_id,
        publish_year = p_publish_year,
        price = p_price
    WHERE books.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_book(
    p_id VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM books
    WHERE books.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION count_books()
RETURNS INT
LANGUAGE plpgsql
AS $$
DECLARE
    v_count INT;
BEGIN
    SELECT COUNT(*)
    INTO v_count
    FROM books;

    RETURN v_count;
END;
$$;

-- ==========================================
-- AUTHOR PROCEDURES & FUNCTIONS
-- ==========================================

CREATE OR REPLACE FUNCTION find_author_by_id(p_id INT)
RETURNS TABLE(
    id INT,
    name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT a.id, a.name
    FROM authors a
    WHERE a.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_author(p_id INT, p_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE authors
    SET name = p_name
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_author(p_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM authors
    WHERE id = p_id;
END;
$$;

-- ==========================================
-- CATEGORY PROCEDURES & FUNCTIONS
-- ==========================================

CREATE OR REPLACE FUNCTION find_category_by_id(p_id INT)
RETURNS TABLE(
    id INT,
    name VARCHAR
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY
    SELECT c.id, c.name
    FROM categories c
    WHERE c.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_category(p_id INT, p_name VARCHAR)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE categories
    SET name = p_name
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_category(p_id INT)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM categories
    WHERE id = p_id;
END;
$$; 
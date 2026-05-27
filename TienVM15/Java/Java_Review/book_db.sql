
DROP PROCEDURE IF EXISTS insert_book(VARCHAR, VARCHAR, VARCHAR, VARCHAR, DOUBLE PRECISION, INT);
DROP FUNCTION IF EXISTS get_all_books();
DROP PROCEDURE IF EXISTS update_book(VARCHAR, VARCHAR, VARCHAR, VARCHAR, DOUBLE PRECISION, INT);
DROP PROCEDURE IF EXISTS delete_book(VARCHAR);
DROP FUNCTION IF EXISTS find_book_by_id(VARCHAR);

DROP TABLE IF EXISTS book CASCADE;

CREATE TABLE book (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author_email VARCHAR(255) NOT NULL,
    publisher_phone VARCHAR(20) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    quantity INT NOT NULL
);


CREATE OR REPLACE PROCEDURE insert_book(
    p_id VARCHAR(50),
    p_title VARCHAR(255),
    p_author_email VARCHAR(255),
    p_publisher_phone VARCHAR(20),
    p_price DOUBLE PRECISION,
    p_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO book(id, title, author_email, publisher_phone, price, quantity)
    VALUES(p_id, p_title, p_author_email, p_publisher_phone, p_price, p_quantity);
END;
$$;

CREATE OR REPLACE FUNCTION get_all_books()
RETURNS TABLE (
    id VARCHAR(50),
    title VARCHAR(255),
    author_email VARCHAR(255),
    publisher_phone VARCHAR(20),
    price DOUBLE PRECISION,
    quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY 
    SELECT b.id, b.title, b.author_email, b.publisher_phone, b.price, b.quantity 
    FROM book b
    ORDER BY b.id ASC;
END;
$$;

CREATE OR REPLACE PROCEDURE update_book(
    p_id VARCHAR(50),
    p_title VARCHAR(255),
    p_author_email VARCHAR(255),
    p_publisher_phone VARCHAR(20),
    p_price DOUBLE PRECISION,
    p_quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE book
    SET title = p_title,
        author_email = p_author_email,
        publisher_phone = p_publisher_phone,
        price = p_price,
        quantity = p_quantity
    WHERE id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_book(
    p_id VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM book WHERE id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION find_book_by_id(
    p_id VARCHAR(50)
)
RETURNS TABLE (
    id VARCHAR(50),
    title VARCHAR(255),
    author_email VARCHAR(255),
    publisher_phone VARCHAR(20),
    price DOUBLE PRECISION,
    quantity INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    RETURN QUERY 
    SELECT b.id, b.title, b.author_email, b.publisher_phone, b.price, b.quantity 
    FROM book b 
    WHERE b.id = p_id;
END;
$$;

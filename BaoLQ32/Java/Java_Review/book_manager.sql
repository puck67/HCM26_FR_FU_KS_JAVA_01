-- 1. Create table books
IF OBJECT_ID('books', 'U') IS NOT NULL 
    DROP TABLE books;

CREATE TABLE books (
    id VARCHAR(10) PRIMARY KEY,
    title NVARCHAR(100) NOT NULL,
    author NVARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    price FLOAT NOT NULL,
    quantity INT NOT NULL,
    category NVARCHAR(50) NOT NULL
);
GO

-- 2. Stored Procedures for Microsoft SQL Server

-- Procedure 1: Insert Book
CREATE OR ALTER PROCEDURE insert_book
    @p_id VARCHAR(10),
    @p_title NVARCHAR(100),
    @p_author NVARCHAR(100),
    @p_email VARCHAR(100),
    @p_phone VARCHAR(20),
    @p_price FLOAT,
    @p_quantity INT,
    @p_category NVARCHAR(50)
AS
BEGIN
    INSERT INTO books(id, title, author, email, phone, price, quantity, category)
    VALUES(@p_id, @p_title, @p_author, @p_email, @p_phone, @p_price, @p_quantity, @p_category);
END;
GO

-- Procedure 2: Get All Books
CREATE OR ALTER PROCEDURE get_all_books
AS
BEGIN
    SELECT id, title, author, email, phone, price, quantity, category 
    FROM books;
END;
GO

-- Procedure 3: Update Book
CREATE OR ALTER PROCEDURE update_book
    @p_id VARCHAR(10),
    @p_title NVARCHAR(100),
    @p_author NVARCHAR(100),
    @p_email VARCHAR(100),
    @p_phone VARCHAR(20),
    @p_price FLOAT,
    @p_quantity INT,
    @p_category NVARCHAR(50)
AS
BEGIN
    UPDATE books
    SET title = @p_title,
        author = @p_author,
        email = @p_email,
        phone = @p_phone,
        price = @p_price,
        quantity = @p_quantity,
        category = @p_category
    WHERE id = @p_id;
END;
GO

-- Procedure 4: Delete Book
CREATE OR ALTER PROCEDURE delete_book
    @p_id VARCHAR(10)
AS
BEGIN
    DELETE FROM books 
    WHERE id = @p_id;
END;
GO

-- Procedure 5: Find Book By ID
CREATE OR ALTER PROCEDURE find_book_by_id
    @p_id VARCHAR(10)
AS
BEGIN
    SELECT id, title, author, email, phone, price, quantity, category 
    FROM books 
    WHERE id = @p_id;
END;
GO

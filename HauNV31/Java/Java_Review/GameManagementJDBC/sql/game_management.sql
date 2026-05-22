-- Microsoft SQL Server T-SQL Script
-- Create Database
CREATE DATABASE game_management;
GO

USE game_management;
GO

-- Create Table
CREATE TABLE games (
    id VARCHAR(10) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    genre NVARCHAR(100),
    price FLOAT, -- DOUBLE in MySQL is mapped to FLOAT in SQL Server
    developer NVARCHAR(100)
);
GO

-- 1. Procedure: Insert Game
CREATE PROCEDURE insert_game
    @p_id VARCHAR(10),
    @p_name NVARCHAR(100),
    @p_genre NVARCHAR(100),
    @p_price FLOAT,
    @p_developer NVARCHAR(100)
AS
BEGIN
    INSERT INTO games (id, name, genre, price, developer)
    VALUES (@p_id, @p_name, @p_genre, @p_price, @p_developer);
END;
GO

-- 2. Procedure: Get All Games
CREATE PROCEDURE get_all_games
AS
BEGIN
    SELECT id, name, genre, price, developer FROM games;
END;
GO

-- 3. Procedure: Find Game By ID
CREATE PROCEDURE find_game_by_id
    @p_id VARCHAR(10)
AS
BEGIN
    SELECT id, name, genre, price, developer FROM games
    WHERE id = @p_id;
END;
GO

-- 4. Procedure: Update Game
CREATE PROCEDURE update_game
    @p_id VARCHAR(10),
    @p_name NVARCHAR(100),
    @p_genre NVARCHAR(100),
    @p_price FLOAT,
    @p_developer NVARCHAR(100)
AS
BEGIN
    UPDATE games
    SET name = @p_name,
        genre = @p_genre,
        price = @p_price,
        developer = @p_developer
    WHERE id = @p_id;
END;
GO

-- 5. Procedure: Delete Game
CREATE PROCEDURE delete_game
    @p_id VARCHAR(10)
AS
BEGIN
    DELETE FROM games
    WHERE id = @p_id;
END;
GO

-- ==========================================
-- POSTGRESQL DATABASE CREATION & SETUP SCRIPT
-- Project: Hibernate Assignment 01
-- Database Name: fadb
-- Port: 5433 (or 5432 depending on setup)
-- ==========================================

-- ------------------------------------------
-- STEP 1: CREATE DATABASE
-- Note: Run this part while connected to the default 'postgres' database.
-- ------------------------------------------

-- Check if fadb database exists, if not, create it
-- (If running inside pgAdmin, you can right-click and create or execute this line)
-- CREATE DATABASE fadb;

-- ------------------------------------------
-- STEP 2: CREATE TABLE (Run this inside 'fadb' database)
-- ------------------------------------------

-- Connect to fadb database before running this:
-- \c fadb;

-- Drop table if it already exists to start fresh
DROP TABLE IF EXISTS Employee;

-- Create the Employee table according to ERD requirements
CREATE TABLE Employee (
    ID SERIAL NOT NULL,                     -- Primary Key, auto-incremented
    first_name VARCHAR(50) NOT NULL,        -- First name, maximum 50 characters, not null
    last_name VARCHAR(50) NOT NULL,         -- Last name, maximum 50 characters, not null
    CONSTRAINT pk_employee PRIMARY KEY (ID)
);

-- ------------------------------------------
-- STEP 3: INSERT SAMPLE DATA
-- ------------------------------------------
INSERT INTO Employee (first_name, last_name) VALUES ('Nguyen', 'An');
INSERT INTO Employee (first_name, last_name) VALUES ('Tran', 'Binh');
INSERT INTO Employee (first_name, last_name) VALUES ('Le', 'Chi');
INSERT INTO Employee (first_name, last_name) VALUES ('Pham', 'Dung');

-- ------------------------------------------
-- STEP 4: VERIFY DATA
-- ------------------------------------------
SELECT * FROM Employee;

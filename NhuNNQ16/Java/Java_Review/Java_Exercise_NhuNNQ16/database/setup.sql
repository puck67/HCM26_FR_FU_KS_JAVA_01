
DROP TABLE IF EXISTS students CASCADE;

DROP PROCEDURE IF EXISTS insert_student(VARCHAR, VARCHAR, VARCHAR, VARCHAR, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS update_student(VARCHAR, VARCHAR, VARCHAR, VARCHAR, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS delete_student(VARCHAR);
DROP FUNCTION  IF EXISTS get_all_students();
DROP FUNCTION  IF EXISTS find_student_by_id(VARCHAR);
DROP FUNCTION  IF EXISTS find_students_by_name(VARCHAR);

--Create table
CREATE TABLE students (
    id    VARCHAR(10)  PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20)  NOT NULL,
    gpa   DOUBLE PRECISION NOT NULL CHECK (gpa >= 0 AND gpa <= 4)
);

--Stored Procedures & Functions 


-- 4.1 Insert a student
CREATE OR REPLACE PROCEDURE insert_student(
    p_id    VARCHAR(10),
    p_name  VARCHAR(100),
    p_email VARCHAR(100),
    p_phone VARCHAR(20),
    p_gpa   DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO students (id, name, email, phone, gpa)
    VALUES (p_id, p_name, p_email, p_phone, p_gpa);
END;
$$;

-- 4.2 Get all students
CREATE OR REPLACE FUNCTION get_all_students()
RETURNS SETOF students
LANGUAGE sql
AS $$
    SELECT * FROM students ORDER BY id;
$$;

-- 4.3 Update a student
CREATE OR REPLACE PROCEDURE update_student(
    p_id    VARCHAR(10),
    p_name  VARCHAR(100),
    p_email VARCHAR(100),
    p_phone VARCHAR(20),
    p_gpa   DOUBLE PRECISION
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE students
    SET name  = p_name,
        email = p_email,
        phone = p_phone,
        gpa   = p_gpa
    WHERE id = p_id;
END;
$$;

-- 4.4 Delete a student
CREATE OR REPLACE PROCEDURE delete_student(
    p_id VARCHAR(10)
)
LANGUAGE plpgsql
AS $$
BEGIN
    DELETE FROM students WHERE id = p_id;
END;
$$;

-- 4.5 Find student by ID
CREATE OR REPLACE FUNCTION find_student_by_id(
    p_id VARCHAR(10)
)
RETURNS SETOF students
LANGUAGE sql
AS $$
    SELECT * FROM students WHERE id = p_id;
$$;

-- 4.6 Find students by name (case-insensitive, partial match)
CREATE OR REPLACE FUNCTION find_students_by_name(
    p_name VARCHAR(100)
)
RETURNS SETOF students
LANGUAGE sql
AS $$
    SELECT * FROM students
    WHERE name ILIKE '%' || p_name || '%'
    ORDER BY name;
$$;

-- Sample data
CALL insert_student('S001', 'Nguyen Van An',   'an.nguyen@fpt.edu.vn',    '0901111111', 3.75);
CALL insert_student('S002', 'Tran Thi Bich',   'bich.tran@fpt.edu.vn',    '0902222222', 3.20);
CALL insert_student('S003', 'Le Hoang Cuong',  'cuong.le@fpt.edu.vn',     '0903333333', 2.85);
CALL insert_student('S004', 'Pham Thi Dung',   'dung.pham@fpt.edu.vn',    '0904444444', 3.90);
CALL insert_student('S005', 'Hoang Minh Duc',  'duc.hoang@fpt.edu.vn',    '0905555555', 3.10);

-- ─── 6. Verify setup ─────────────────────────────────────────
SELECT * FROM get_all_students();

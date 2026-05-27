CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    description TEXT,
    start_time DATE,
    end_time DATE
);

CREATE TABLE IF NOT EXISTS learners (
    id SERIAL PRIMARY KEY,
    student_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    birth_date DATE,
    class_room VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS trainers (
    id SERIAL PRIMARY KEY,
    trainer_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    birth_date DATE,
    class_name VARCHAR(100)
);


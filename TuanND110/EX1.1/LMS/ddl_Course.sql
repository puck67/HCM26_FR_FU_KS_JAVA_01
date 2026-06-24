CREATE TABLE courses
(
    course_name VARCHAR(255) NOT NULL,
    category    VARCHAR(255),
    instructor  VARCHAR(255),
    course_code VARCHAR(255) NOT NULL,
    start_date  date         NOT NULL,
    CONSTRAINT pk_courses PRIMARY KEY (course_code, start_date)
);
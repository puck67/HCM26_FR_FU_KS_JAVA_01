-- Root Menus
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (1, 'Dashboard', '/', 'bi-speedometer2', NULL, 1, true);
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (2, 'User Management', '#', 'bi-people', NULL, 2, true);
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (5, 'Training Management', '#', 'bi-journal-bookmark', NULL, 3, true);

-- Sub Menus for User Management
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (3, 'Student Management', '/students', 'bi-person-badge', 2, 1, true);
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (4, 'Lecturer Management', '/lecturers', 'bi-person-video3', 2, 2, true);

-- Sub Menus for Training Management
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (6, 'Subject Management', '/subjects', 'bi-book', 5, 1, true);
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (7, 'Course Management', '/courses', 'bi-mortarboard', 5, 2, true);
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (9, 'Add Course', '/courses/new', 'bi-plus-circle', 5, 3, true);

-- Menu Management (Admin section)
INSERT INTO menu (id, name, url, icon, parent_id, display_order, status) VALUES (8, 'Menu Management', '/menus', 'bi-list-nested', NULL, 4, true);

-- Reset sequence for menu table since we manually inserted IDs up to 9
ALTER TABLE menu ALTER COLUMN id RESTART WITH 10;

-- Sample Data for Courses
INSERT INTO course (course_code, start_date, category, course_name, instructor) VALUES ('CS101', '2026-06-01', 'Computer Science', 'Introduction to Programming', 'Dr. Alan Turing');
INSERT INTO course (course_code, start_date, category, course_name, instructor) VALUES ('CS201', '2026-07-15', 'Computer Science', 'Data Structures', 'Dr. Grace Hopper');
INSERT INTO course (course_code, start_date, category, course_name, instructor) VALUES ('UI101', '2026-06-10', 'Design', 'UI/UX Fundamentals', 'Steve Jobs');

-- Sample Data for Lessons
INSERT INTO lesson (content_type, duration, lesson_name, status, course_code, start_date) VALUES ('Video', 45, 'Welcome to CS101', 'Active', 'CS101', '2026-06-01');
INSERT INTO lesson (content_type, duration, lesson_name, status, course_code, start_date) VALUES ('Document', 30, 'Setting up IDE', 'Active', 'CS101', '2026-06-01');
INSERT INTO lesson (content_type, duration, lesson_name, status, course_code, start_date) VALUES ('Video', 60, 'Arrays and Lists', 'Active', 'CS201', '2026-07-15');

-- Sample Data for Students
INSERT INTO student (name, email, major, enrollment_date, status) VALUES ('Nguyen Van A', 'nguyenvana@example.com', 'Computer Science', '2025-09-01', 'Active');
INSERT INTO student (name, email, major, enrollment_date, status) VALUES ('Tran Thi B', 'tranthib@example.com', 'Information Technology', '2025-09-01', 'Active');
INSERT INTO student (name, email, major, enrollment_date, status) VALUES ('Le Van C', 'levanc@example.com', 'Software Engineering', '2026-01-15', 'Active');
INSERT INTO student (name, email, major, enrollment_date, status) VALUES ('Pham Thi D', 'phamthid@example.com', 'Data Science', '2024-09-01', 'Graduated');
INSERT INTO student (name, email, major, enrollment_date, status) VALUES ('Hoang Van E', 'hoangvane@example.com', 'Computer Science', '2026-01-15', 'Suspended');

-- Sample Data for Lecturers
INSERT INTO lecturer (name, email, department, expertise) VALUES ('Dr. Alan Turing', 'alan.turing@example.com', 'Computer Science', 'Cryptography, AI');
INSERT INTO lecturer (name, email, department, expertise) VALUES ('Dr. Grace Hopper', 'grace.hopper@example.com', 'Software Engineering', 'Compilers, Programming Languages');
INSERT INTO lecturer (name, email, department, expertise) VALUES ('Steve Jobs', 'steve.jobs@example.com', 'Design', 'UI/UX, Product Management');

-- Sample Data for Subjects
INSERT INTO subject (code, name, credits) VALUES ('CS101', 'Introduction to Computer Science', 3);
INSERT INTO subject (code, name, credits) VALUES ('SE201', 'Software Engineering Principles', 4);
INSERT INTO subject (code, name, credits) VALUES ('DS301', 'Data Structures and Algorithms', 4);

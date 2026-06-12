-- Sample Data Script
-- 1. Dashboard (/dashboard, parent: NULL)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (1, 'Dashboard', '/dashboard', 'bi-speedometer2', NULL, 1, true);

-- 2. User Management (parent: NULL)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (2, 'User Management', '#', 'bi-people', NULL, 2, true);

-- 3. Student Management (/students, parent: 2)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (3, 'Student Management', '/students', 'bi-person', 2, 1, true);

-- 4. Lecturer Management (/lecturers, parent: 2)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (4, 'Lecturer Management', '/lecturers', 'bi-person-badge', 2, 2, true);

-- 5. Training Management (parent: NULL)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (5, 'Training Management', '#', 'bi-book', NULL, 3, true);

-- 6. Subject Management (/subjects, parent: 5)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (6, 'Subject Management', '/subjects', 'bi-journal-text', 5, 1, true);

-- 7. Course Management (/courses, parent: 5)
INSERT INTO menus (id, name, url, icon, parent_id, display_order, status) VALUES (7, 'Course Management', '/courses', 'bi-award', 5, 2, true);

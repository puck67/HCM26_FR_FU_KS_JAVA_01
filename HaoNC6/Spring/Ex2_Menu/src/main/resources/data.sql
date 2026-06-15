-- =============================================
-- Sample Menu Data
-- Uses INSERT ... ON CONFLICT to be idempotent
-- =============================================

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (1, 'Dashboard', '/', 'fas fa-tachometer-alt', NULL, 1, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (2, 'User Management', '#', 'fas fa-users', NULL, 2, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (3, 'Student Management', '/students', 'fas fa-user-graduate', 2, 1, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (4, 'Lecturer Management', '/lecturers', 'fas fa-chalkboard-teacher', 2, 2, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (5, 'Training Management', '#', 'fas fa-graduation-cap', NULL, 3, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (6, 'Subject Management', '/subjects', 'fas fa-book', 5, 1, true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO menu (id, name, url, icon, parent_id, display_order, status)
VALUES (7, 'Course Management', '/courses', 'fas fa-laptop', 5, 2, true)
ON CONFLICT (id) DO NOTHING;

-- Update the sequence to avoid conflicts with future inserts
SELECT setval('menu_id_seq', (SELECT COALESCE(MAX(id), 0) FROM menu) + 1, false);

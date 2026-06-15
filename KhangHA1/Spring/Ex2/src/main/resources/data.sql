INSERT INTO roles(id, name) VALUES
(1, 'ADMIN'),
(2, 'TEACHER'),
(3, 'STUDENT');

INSERT INTO menus
(id, name, url, icon, display_order, status, parent_id)
VALUES
(1, 'Dashboard', '/', 'bi-speedometer2', 1, true, NULL),

(2, 'User Management', '#', 'bi-people', 2, true, NULL),

(3, 'Training Management', '#', 'bi-book', 3, true, NULL),

(4, 'Exam Management', '#', 'bi-file-earmark-text', 4, true, NULL),

(5, 'System Management', '#', 'bi-gear', 5, true, NULL),

(6, 'My Courses', '/my-courses', 'bi-mortarboard', 6, true, NULL);

INSERT INTO menus
(id, name, url, icon, display_order, status, parent_id)
VALUES
(7, 'Student Management',
 '/students',
 'bi-person',
 1,
 true,
 2),

(8, 'Lecturer Management',
 '/lecturers',
 'bi-person-badge',
 2,
 true,
 2);

 INSERT INTO menus
(id, name, url, icon, display_order, status, parent_id)
VALUES
(9, 'Subject Management',
 '/subjects',
 'bi-journal-bookmark',
 1,
 true,
 3),

(10, 'Course Management',
 '/courses',
 'bi-mortarboard',
 2,
 true,
 3);

 INSERT INTO menus
(id, name, url, icon, display_order, status, parent_id)
VALUES
(11, 'Online Courses',
 '/courses/online',
 'bi-laptop',
 1,
 true,
 10),

(12, 'Offline Courses',
 '/courses/offline',
 'bi-building',
 2,
 true,
 10),

(13, 'Weekend Courses',
 '/courses/weekend',
 'bi-calendar-week',
 3,
 true,
 10);

INSERT INTO menus
(id, name, url, icon, display_order, status, parent_id)
VALUES
(14, 'Question Bank',
 '/questions',
 'bi-question-circle',
 1,
 true,
 4),

(15, 'Exam',
 '/exams',
 'bi-file-earmark-text',
 2,
 true,
 4),

(16, 'Results',
 '/results',
 'bi-bar-chart',
 3,
 true,
 4);

INSERT INTO app_users(id, username, role_id)
VALUES
(1, 'admin', 1),
(2, 'teacher1', 2),
(3, 'student1', 3);

INSERT INTO menu_role(menu_id, role_id)
VALUES
(1,1),
(2,1),
(3,1),
(4,1),
(5,1),

(7,1),
(8,1),
(9,1),
(10,1),
(11,1),
(12,1),
(13,1),

(14,1),
(15,1),
(16,1),

(17,1),
(18,1);

INSERT INTO menu_role(menu_id, role_id)
VALUES
(1,2),

(3,2),

(9,2),
(10,2),

(11,2),
(12,2),
(13,2),

(4,2),

(14,2),
(15,2),
(16,2);

INSERT INTO menu_role(menu_id, role_id)
VALUES
(1,3),
(6,3);
-- Insert instructor only if not exists
INSERT INTO tbl_instructor (username, password, full_name, email)
SELECT 'instructor', '$2a$10$uuWycY9ZXBLRfoYmqkUHW.6/aNJ59OZKcs5XuJ9u0O/pQ9/yroCP2', 'John Instructor', 'instructor@lms.com'
WHERE NOT EXISTS (SELECT 1 FROM tbl_instructor WHERE username = 'instructor');

-- Lookup values for status
INSERT INTO tbl_lookup (type, code, label)
SELECT 'COURSE_STATUS', '1', 'Draft' WHERE NOT EXISTS (SELECT 1 FROM tbl_lookup WHERE type='COURSE_STATUS' AND code='1');
INSERT INTO tbl_lookup (type, code, label)
SELECT 'COURSE_STATUS', '2', 'Published' WHERE NOT EXISTS (SELECT 1 FROM tbl_lookup WHERE type='COURSE_STATUS' AND code='2');
INSERT INTO tbl_lookup (type, code, label)
SELECT 'COURSE_STATUS', '3', 'Archived' WHERE NOT EXISTS (SELECT 1 FROM tbl_lookup WHERE type='COURSE_STATUS' AND code='3');

INSERT INTO tbl_lookup (type, code, label)
SELECT 'REVIEW_STATUS', '1', 'Pending' WHERE NOT EXISTS (SELECT 1 FROM tbl_lookup WHERE type='REVIEW_STATUS' AND code='1');
INSERT INTO tbl_lookup (type, code, label)
SELECT 'REVIEW_STATUS', '2', 'Approved' WHERE NOT EXISTS (SELECT 1 FROM tbl_lookup WHERE type='REVIEW_STATUS' AND code='2');

-- Sample courses
INSERT INTO tbl_course (title, description, content, status, category, instructor_id)
SELECT 'Introduction to Spring Boot', 'Learn Spring Boot from scratch with hands-on examples.', '## Overview\n\nSpring Boot makes it easy to create stand-alone, production-grade Spring based Applications.\n\n## Topics\n\n- Auto-configuration\n- Embedded servers\n- Spring Data JPA\n\n## Getting Started\n\nAdd the Spring Boot starter to your pom.xml and run!', 2, 'Spring,Java,Backend', 1
WHERE NOT EXISTS (SELECT 1 FROM tbl_course WHERE title = 'Introduction to Spring Boot');

INSERT INTO tbl_course (title, description, content, status, category, instructor_id)
SELECT 'Spring Security Fundamentals', 'Master authentication and authorization with Spring Security.', '## What you will learn\n\n- Form-based login\n- JWT tokens\n- Role-based access control\n\n## Prerequisites\n\nBasic Spring Boot knowledge required.', 2, 'Spring,Security,Java', 1
WHERE NOT EXISTS (SELECT 1 FROM tbl_course WHERE title = 'Spring Security Fundamentals');

INSERT INTO tbl_course (title, description, content, status, category, instructor_id)
SELECT 'React for Beginners', 'Build modern UI with React and hooks.', '## React Basics\n\n- Components\n- Props and State\n- Hooks (useState, useEffect)\n\n## Project\n\nBuild a todo app from scratch.', 2, 'React,JavaScript,Frontend', 1
WHERE NOT EXISTS (SELECT 1 FROM tbl_course WHERE title = 'React for Beginners');

INSERT INTO tbl_course (title, description, content, status, category, instructor_id)
SELECT 'Docker & Kubernetes', 'Container orchestration for modern applications.', '## Docker\n\n- Images and containers\n- Dockerfile\n- Docker Compose\n\n## Kubernetes\n\n- Pods, Services, Deployments\n- kubectl commands', 1, 'DevOps,Docker,Kubernetes', 1
WHERE NOT EXISTS (SELECT 1 FROM tbl_course WHERE title = 'Docker & Kubernetes');

-- Sample reviews
INSERT INTO tbl_review (course_id, author_name, email, rating, content, status)
SELECT c.id, 'Alice Nguyen', 'alice@example.com', 5, 'Excellent course! Very clear explanations.', 2
FROM tbl_course c WHERE c.title = 'Introduction to Spring Boot'
AND NOT EXISTS (SELECT 1 FROM tbl_review r JOIN tbl_course cc ON r.course_id=cc.id WHERE cc.title='Introduction to Spring Boot' AND r.author_name='Alice Nguyen');

INSERT INTO tbl_review (course_id, author_name, email, rating, content, status)
SELECT c.id, 'Bob Tran', 'bob@example.com', 4, 'Good content. Would love more exercises.', 2
FROM tbl_course c WHERE c.title = 'Introduction to Spring Boot'
AND NOT EXISTS (SELECT 1 FROM tbl_review r JOIN tbl_course cc ON r.course_id=cc.id WHERE cc.title='Introduction to Spring Boot' AND r.author_name='Bob Tran');

INSERT INTO tbl_review (course_id, author_name, email, rating, content, status)
SELECT c.id, 'Carol Le', 'carol@example.com', 5, 'Best security course I have taken!', 2
FROM tbl_course c WHERE c.title = 'Spring Security Fundamentals'
AND NOT EXISTS (SELECT 1 FROM tbl_review r JOIN tbl_course cc ON r.course_id=cc.id WHERE cc.title='Spring Security Fundamentals' AND r.author_name='Carol Le');

-- Categories
INSERT INTO tbl_category (name, frequency)
SELECT 'Spring', 2 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Spring');
INSERT INTO tbl_category (name, frequency)
SELECT 'Java', 2 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Java');
INSERT INTO tbl_category (name, frequency)
SELECT 'Backend', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Backend');
INSERT INTO tbl_category (name, frequency)
SELECT 'Security', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Security');
INSERT INTO tbl_category (name, frequency)
SELECT 'React', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='React');
INSERT INTO tbl_category (name, frequency)
SELECT 'JavaScript', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='JavaScript');
INSERT INTO tbl_category (name, frequency)
SELECT 'Frontend', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Frontend');
INSERT INTO tbl_category (name, frequency)
SELECT 'DevOps', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='DevOps');
INSERT INTO tbl_category (name, frequency)
SELECT 'Docker', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Docker');
INSERT INTO tbl_category (name, frequency)
SELECT 'Kubernetes', 1 WHERE NOT EXISTS (SELECT 1 FROM tbl_category WHERE name='Kubernetes');

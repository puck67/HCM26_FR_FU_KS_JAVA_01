# Ex2: Hibernate CRUD Application with DAO and Service Layers

## Objective

Build a Java application using Hibernate with a layered architecture (DAO + Service). The goal is to understand how Hibernate is used in a realistic project structure while practicing CRUD operations and different query techniques.

## Background

A training center manages students and courses.

Each student can enroll in multiple courses, and each course can have multiple students.

You must design the application using a **layered architecture** to separate persistence logic, business logic, and application logic.

## Required Technologies

- Java
- Maven
- Hibernate ORM
- H2 in-memory database
- Jakarta Persistence (annotations)
- SLF4J logging

## Application Architecture

Your project must follow this structure:

com.example

- entity
- dao
- dao.impl
- service
- service.impl
- util
- app

## Layer responsibilities

- Entity layer
    - Defines persistent objects mapped to database tables.
- DAO layer
    - Handles database access using Hibernate sessions.
- Service layer
    - Contains business logic and coordinates DAO operations.
- App layer
    - Contains the main program used to test the system.
- Util layer
    - Provides Hibernate SessionFactory configuration.

## Project Tasks

### Task 1: Project Setup

1. Create a Maven project.
2. Add dependencies:
    - Hibernate
    - H2 Database
    - Jakarta Persistence API
    - SLF4J
3. Create a Hibernate configuration file (hibernate.cfg.xml).
4. Configure Hibernate to automatically create/update tables.

### Task 2: Entity Classes

Create two entities.

Student

- id
- name
- age
- Set<Course> courses

Course

- id
- title
- credit
- Set<Student> students

Requirements

- Use annotations
- Implement Many-to-Many mapping
- Use a join table: student_course

### Task 3: Hibernate Utility

Create a class: **HibernateUtil**

Responsibilities

- Initialize SessionFactory
- Provide a static method to get SessionFactory
- Handle configuration loading

Example purpose:

SessionFactory factory = HibernateUtil.getSessionFactory();

### Task 4: DAO Layer

Create DAO interfaces.

**StudentDAO**

- save(Student student)
- update(Student student)
- delete(int studentId)
- findById(int id)
- findAll()

**CourseDAO**

- save(Course course)
- update(Course course)
- delete(int courseId)
- findById(int id)
- findAll()

Then implement them:

- StudentDAOImpl
- CourseDAOImpl

Responsibilities

- Open Hibernate session
- Manage transactions
- Execute queries

### Task 5: Service Layer

Create service interfaces.

**StudentService**

- createStudent(name, age)
- updateStudent(id, name, age)
- deleteStudent(id)
- getStudentById(id)
- getAllStudents()
- enrollStudentInCourse(studentId, courseId)
- removeStudentFromCourse(studentId, courseId)
- getCoursesOfStudent(studentId)

**CourseService**

- createCourse(title, credit)
- updateCourse(id, title, credit)
- deleteCourse(id)
- getCourseById(id)
- getAllCourses()
- getStudentsOfCourse(courseId)

Implement them:

- StudentServiceImpl
- CourseServiceImpl

Responsibilities

- Call DAO methods
- Handle business logic
- Manage relationships between Student and Course

Task 6: Advanced Hibernate Queries

Implement the following queries in DAO or Service layer.

1. HQL Query
    
    Find all students older than a given age.
    
2. HQL Join Query
    
    List all students with the titles of the courses they take.
    
3. Named Query
    
    Define a named query in Student entity:
    
    Find students by name.
    
4. Criteria API Query
    
    Find courses with credit greater than a given value.
    
5. Aggregation Query
    
    Count how many students are enrolled in each course.
    
    - Example output idea:
        - Course: Java Programming | Students enrolled: 3
        - Course: Database Systems | Students enrolled: 2
6. Parameterized Query
    - Find students enrolled in a specific course.

### Task 7: Console Menu Application

Create a console-based menu system that allows the user to interact with the application. The menu should guide the user to manage Students, Courses, and Enrollments through different submenus.

The program must continuously run until the user chooses to exit.

Main Menu

When the program starts, display the following menu:

1. Student Management
2. Course Management
3. Enrollment Management
4. Queries and Reports
5. Exit

The user selects a number to enter the corresponding submenu

1. **Student Management Menu**

This menu allows the user to perform CRUD operations related to students.

Example menu:

**Student Management**

1. Create a new student
2. Update student information
3. Delete a student
4. View student by ID
5. List all students
6. Back to main menu

Requirements:

- Each operation must call the corresponding methods in the StudentService.
- Display results clearly in the console.

**2. Course Management Menu**

This menu allows the user to manage courses.

Example menu:

Course Management

1. Create a new course
2. Update course information
3. Delete a course
4. View course by ID
5. List all courses
6. Back to main menu

Requirements:

- Each operation must call methods in the CourseService.

**3. Enrollment Management Menu**

This menu manages the many-to-many relationship between students and courses.

Example menu:

Enrollment Management

1. Enroll a student in a course
2. Remove a student from a course
3. View courses of a student
4. View students of a course
5. Back to main menu

Requirements:

- The system should ask the user to input Student ID and/or Course ID.
- The corresponding Service methods must be used to perform the operations.

Queries and Reports Menu

This menu demonstrates the advanced Hibernate queries implemented earlier.

Example menu:

Queries and Reports

1. Find students older than a given age (HQL)
2. Find students by name (Named Query)
3. List students and their courses (HQL Join)
4. Find courses with credit greater than a value (Criteria API)
5. Count number of students in each course (Aggregation Query)
6. Find students enrolled in a specific course
7. Back to main menu

Requirements:

- Prompt the user for necessary parameters (such as age, name, credit value, or course ID).
- Display results in a readable format.

Program Behavior Requirements

- The application must keep running until the user selects "Exit".
- Each submenu should allow the user to perform multiple operations before returning to the main menu.
- Invalid inputs should be handled gracefully (for example, non-existing IDs or incorrect menu options).
- All database operations must go through the Service layer.

Example Flow

Start program

→ Main Menu

→ User selects "1. Student Management"

→ Student submenu appears

→ User selects "1. Create student"

→ User enters name and age

→ Student is saved to the database

→ Menu appears again
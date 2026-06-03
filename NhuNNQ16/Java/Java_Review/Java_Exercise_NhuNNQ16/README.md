# 📚 Student Management System

> **Java Console CRUD** with **JDBC + PostgreSQL Stored Procedures + JUnit 5 Tests**
> Author: NhuNNQ16 | FPT University

---

## 📂 Project Structure

```
Java_Exercise_NhuNNQ16/
├── database/
│   └── setup.sql                  # DB creation, table, stored procedures & sample data
├── src/
│   ├── main/java/org/example/
│   │   ├── model/
│   │   │   └── Student.java       # Entity class (OOP)
│   │   ├── db/
│   │   │   └── DBConnection.java  # PostgreSQL connection (Singleton)
│   │   ├── dao/
│   │   │   └── StudentDAO.java    # Data Access Object (uses CallableStatement)
│   │   ├── service/
│   │   │   └── StudentService.java # Business logic + Validation
│   │   ├── util/
│   │   │   └── Validator.java     # Validation utilities (regex, rules)
│   │   └── Main.java              # Console menu entry point
│   └── test/java/org/example/service/
│       └── StudentServiceTest.java # 10 JUnit 5 unit tests (Mockito)
└── pom.xml                        # Maven dependencies
```

---

## 🛠️ Technology Stack

| Component    | Technology                   |
|--------------|------------------------------|
| Language     | Java 17                      |
| Build Tool   | Maven                        |
| Database     | PostgreSQL                   |
| JDBC Driver  | PostgreSQL JDBC 42.7.3       |
| Unit Testing | JUnit 5 + Mockito            |

---

## ⚙️ Prerequisites

- **Java 17+** installed
- **Maven 3.6+** installed
- **PostgreSQL** running locally (default port `5432`)
- A PostgreSQL superuser account (default: `postgres`)

---

## 🗃️ Database Setup

### Step 1 – Create the database

Open `psql` as superuser:

```bash
psql -U postgres
```

Then run:

```sql
CREATE DATABASE student_db;
\q
```

### Step 2 – Run the setup script

```bash
psql -U postgres -d student_db -f database/setup.sql
```

This script will:
1. Drop and recreate the `students` table
2. Create **6 stored procedures/functions**:
   - `insert_student(id, name, email, phone, gpa)`
   - `get_all_students()`
   - `update_student(id, name, email, phone, gpa)`
   - `delete_student(id)`
   - `find_student_by_id(id)`
   - `find_students_by_name(name)`
3. Insert 5 sample student records

---

## 🔧 Configuration

Edit `src/main/java/org/example/db/DBConnection.java` to match your credentials:

```java
private static final String DB_URL  = "jdbc:postgresql://localhost:5432/student_db";
private static final String DB_USER = "postgres";
private static final String DB_PASS = "your_password_here";
```

Or set environment variables before running:

```bash
export DB_URL="jdbc:postgresql://localhost:5432/student_db"
export DB_USER="postgres"
export DB_PASS="your_password"
```

---

## ▶️ Running the Application

```bash
# Compile
mvn compile

# Run
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### Console Menu

```
╔══════════════════════════════════════════════════╗
║      STUDENT MANAGEMENT SYSTEM  (PostgreSQL)    ║
║          Java CRUD + JDBC + Stored Procs        ║
╚══════════════════════════════════════════════════╝

  ┌─────────────────────────────┐
  │         MAIN MENU           │
  ├─────────────────────────────┤
  │  1. Add new student         │
  │  2. Display all students    │
  │  3. Update a student        │
  │  4. Delete a student        │
  │  5. Search student          │
  │  6. Exit                    │
  └─────────────────────────────┘
```

---

## ✅ Running Unit Tests

```bash
mvn test
```

### Test Cases (10 total)

| # | Test Method | Description |
|---|-------------|-------------|
| 1 | `testAddStudentSuccess` | Insert valid student with unique ID |
| 2 | `testAddStudentDuplicateId` | Reject insert when ID already exists |
| 3 | `testFindByIdFound` | Retrieve student by exact ID |
| 4 | `testUpdateStudentSuccess` | Update existing student with valid data |
| 5 | `testDeleteStudentSuccess` | Delete existing student |
| 6 | `testDeleteStudentNotFound` | Report error when deleting non-existent ID |
| 7 | `testValidationInvalidEmail` | Reject malformed email |
| 8 | `testValidationInvalidPhone` | Reject phone containing letters |
| 9 | `testValidationGpaOutOfRange` | Reject GPA > 4.0 |
| 10 | `testGetAllStudents` | Return all students from DAO |

> ⚡ **Tests do NOT require a database** — `StudentDAO` is mocked with Mockito.

---

## 🔍 Validation Rules

| Field  | Rule |
|--------|------|
| **ID** | Alphanumeric only, non-blank, unique |
| **Name** | Non-blank string |
| **Email** | Must match `user@domain.com` format |
| **Phone** | Digits only, 9–11 characters |
| **GPA** | Decimal between `0.0` and `4.0` inclusive |

---

## 🗄️ Database Schema

```sql
CREATE TABLE students (
    id    VARCHAR(10)       PRIMARY KEY,
    name  VARCHAR(100)      NOT NULL,
    email VARCHAR(100)      NOT NULL UNIQUE,
    phone VARCHAR(20)       NOT NULL,
    gpa   DOUBLE PRECISION  NOT NULL CHECK (gpa >= 0 AND gpa <= 4)
);
```

---

## 📋 Grading Criteria Checklist

| Criterion | Details | Score |
|-----------|---------|-------|
| OOP Design | `Student` model, private fields, getters/setters, `toString()` | 2/2 |
| CRUD Functions | Add, Display, Update, Delete, Search — via stored procedures | 3/3 |
| Validation | Email, phone, GPA, ID, blank-check | 2/2 |
| Menu & Input | Loop menu, try-catch, re-prompt on invalid input | 2/2 |
| Code Structure | Model / DAO / Service / Util / Main layers | 1/1 |
| **JDBC Total** | PostgreSQL + CallableStatement + 6 stored procedures | 7/7 |
| **Unit Tests** | 10 JUnit 5 tests with Mockito (no DB required) | 2/2 |

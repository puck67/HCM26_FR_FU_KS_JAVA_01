# Hibernate Assignment 01 — H2 In-Memory Database

## Tech Stack
| Tool | Version |
|------|---------|
| Java | 8+ |
| Hibernate | 5.6.15.Final |
| H2 Database | 2.2.224 (in-memory, no install needed) |
| Maven | 3+ |

---

## Project Structure

```
hibernate_assignment01/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/fa/training/
    │   │   ├── Main.java               ← Entry point (menu UI)
    │   │   ├── entities/Employee.java  ← @Entity mapped to EMPLOYEE table
    │   │   ├── dao/EmployeeDAO.java    ← CRUD with try-with-resources
    │   │   └── utils/HibernateUtil.java← SessionFactory singleton
    │   └── resources/
    │       └── hibernate.cfg.xml       ← H2 config, show_sql=true
    └── test/
        └── java/fa/training/dao/
            └── EmployeeDAOTest.java    ← Real JUnit assertions
```

---

## Problems Completed

### Problem 1 — Maven Project
- Created Maven project `hibernate_assignment01`
- `pom.xml` includes Hibernate 5, H2 driver, JUnit 4

### Problem 2 — Hibernate Configuration
- `hibernate.cfg.xml` configures H2 in-memory database named `fadb`
- `show_sql=true` for SQL debugging
- `hbm2ddl.auto=update` (tables created on first run, preserved on subsequent runs)
- `HibernateUtil` manages a singleton `SessionFactory`

### Problem 3 — Employee Entity
- `Employee` class in `fa.training.entities` package
- Annotated with `@Entity`, `@Table(name="EMPLOYEE")`
- Columns: `ID` (PK, auto), `FIRST_NAME`, `LAST_NAME`
- `Main.java` seeds two sample employees and provides a menu to manage them

---

## How to Run

```bash
# Download dependencies & compile
mvn clean install

# Run unit tests
mvn test

# Run the application
mvn exec:java -Dexec.mainClass="fa.training.Main"
```

---

## Design Notes (Avoiding Common AI Pitfalls)

| Issue | Solution Applied |
|-------|-----------------|
| **Connection Leak** | All `Session` opens use `try-with-resources` → auto-closed even on exception |
| **Resource Leak** | `HibernateUtil.shutdown()` called in `finally` block in `Main` |
| **Layer Violation** | `Scanner` and `System.out` only in `Main.java` — never inside DAO or Service |
| **Fake unit tests** | All test methods have real assertions (`assertEquals`, `assertTrue(condition)`) |
| **Input crash** | `readInt()` uses Regex (`-?\\d+`) to validate before parsing — no crash on bad input |
| **Java 8 features** | Lambda `Consumer<Scanner>` for menu actions, `Optional` for nullable returns, `forEach` + method references |

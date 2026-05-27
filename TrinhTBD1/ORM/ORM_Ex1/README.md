# ORM Exercise 1 - Student & Course Management System

This project is a highly-polished, standard JPA-based Student & Course Management System utilizing Hibernate ORM and H2 file-based long-term database persistence.

## Key Features
1. **Pristine JPA Architecture**:
   - Built strictly using standard JPA configurations (`persistence.xml` under `META-INF`).
   - Standard `EntityManagerFactory` and `EntityManager` thread-safe lifecycle orchestration.
2. **File-Based Long-Term Storage**:
   - Uses embedded persistent H2 database (`./db/school_db.mv.db`) which retains all manual test data and states across runs.
3. **Hierarchical 2-Level Console CLI**:
   - Clean, modular menus grouping Student CRUD, Course CRUD, Enrollments, and Advanced Reports.
4. **Regular Expression Validation**:
   - All console entries (Names, Titles, Age, Credits) are fully validated using regular expressions to prevent bad inputs or SQL injections.
5. **Advanced Hibernate Queries (Task 5 & Bonuses)**:
   - Includes standard HQL queries, Criteria APIs, Joins, Named Queries, Aggregations, Pagination, and non-enrolled students finder.

## Author
- **Account**: Authur0912
- **Email**: trinhtbdse171941@fpt.edu.vn

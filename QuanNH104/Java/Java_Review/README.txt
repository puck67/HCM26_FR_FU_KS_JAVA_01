WAREHOUSE MANAGEMENT SYSTEM RUN GUIDE (POSTGRESQL + JDBC)

1. PROJECT STRUCTURE
- db_setup.sql: SQL file containing table creation scripts and all stored procedures/functions.
- src/main/resources/db.properties: PostgreSQL database connection settings.
- src/main/java/fa/training/model: Contains Entity classes (Warehouse, Employee, Product).
- src/main/java/fa/training/db: Contains DBConnection class to fetch JDBC database connections.
- src/main/java/fa/training/validation: Contains Validator class for checking properties validation.
- src/main/java/fa/training/dao: Contains Interface classes (WarehouseDAO, EmployeeDAO, ProductDAO).
- src/main/java/fa/training/dao/impl: Contains Implementation classes (WarehouseDAOImpl, EmployeeDAOImpl, ProductDAOImpl).
- src/main/java/fa/training/Main.java: The main entry point featuring an interactive console menu.
- src/test/java/fa/training/dao/WarehouseDAOTest.java: JUnit 5 test cases.

2. DATABASE SETUP (POSTGRESQL)
- Step 1: Create a new database named: warehouse_management
  SQL command: CREATE DATABASE warehouse_management;
- Step 2: Connect to the database and run all scripts in "db_setup.sql" to initialize tables, foreign key constraints, and stored procedures/functions.
- Step 3: Update your database connection credentials (URL, User, Password) in:
  src/main/resources/db.properties

3. RUNNING THE APPLICATION
- Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.).
- Run the "src/main/java/fa/training/Main.java" file to launch the console application.
- Select from the following choices (1 to 8):
  1. Add new Warehouse (ID format: WH-XXX)
  2. Display all Warehouses (along with linked employees and products)
  3. Update a Warehouse (capacity decrease is validated against stored products count)
  4. Delete a Warehouse (automatically deletes products, unassigns employees)
  5. Search Warehouse by ID
  6. Add Employee to a Warehouse (ID format: EM-XXX, validates email and phone)
  7. Add Product to a Warehouse (ID format: PR-XXX, validates capacity limits)
  8. Exit

4. RUNNING UNIT TESTS
- Run the JUnit 5 test class: "src/test/java/fa/training/dao/WarehouseDAOTest.java".
- It contains 6 tests:
  - testInsertRecord(): Verifies new warehouse insertion.
  - testFindById(): Verifies finding warehouse by ID.
  - testUpdateRecord(): Verifies updating warehouse details.
  - testDeleteRecord(): Verifies warehouse deletion.
  - testValidation(): Verifies formatting and regex rules (email, phone, ID prefixes).
  - testCapacityExceeded(): Verifies business constraint preventing adding products when capacity is full.

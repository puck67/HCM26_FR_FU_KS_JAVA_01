# Certificate Management System

## Mo ta
Ung dung console Java thuc hien CRUD cho Certificate su dung JDBC + MySQL Stored Procedures.
- Java 8 syntax, chay tren JDK 17
- MySQL lam co so du lieu
- JUnit 5 cho Unit Test

## Yeu cau moi truong
- JDK 17
- MySQL Server 8.x tro len
- MySQL Connector/J (da co trong Libraries: mysql-connector-j-9.7.0.zip)
- JUnit 5 JARs (junit-jupiter-api, junit-jupiter-engine, junit-platform-commons)

## Huong dan cai dat va chay

### Buoc 1: Tao Database
Mo MySQL Workbench hoac MySQL Command Line, chay file `setup.sql`:
```
mysql -u root -p < setup.sql
```
Hoac copy noi dung file `setup.sql` va paste vao MySQL Workbench roi Execute.

### Buoc 2: Cau hinh ket noi
Mo file `src/utils/Constants.java` va sua thong tin ket noi cho phu hop:
```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/certificate_management";
public static final String DB_USER = "root";         // sua thanh username cua ban
public static final String DB_PASSWORD = "password";  // sua thanh password cua ban
```

### Buoc 3: Them JUnit 5 vao Test Libraries (trong NetBeans)
1. Click phai vao "Test Libraries" trong NetBeans
2. Chon "Add JAR/Folder"
3. Them cac file JAR:
   - junit-jupiter-api-5.x.x.jar
   - junit-jupiter-engine-5.x.x.jar
   - junit-platform-commons-1.x.x.jar
   - junit-platform-engine-1.x.x.jar
   - junit-platform-launcher-1.x.x.jar
   - apiguardian-api-1.x.x.jar
   - opentest4j-1.x.x.jar

### Buoc 4: Chay chuong trinh
- Trong NetBeans: Click phai vao file `Test.java` > Run File
- Hoac: Shift + F6

### Buoc 5: Chay Unit Test
- Trong NetBeans: Click phai vao file `CertificateServiceTest.java` > Test File
- Hoac: Click phai vao project > Test

## Cau truc du an
```
Certificate_Management/
├── src/
│   ├── entities/
│   │   ├── Certificate.java      (Model - entity Certificate)
│   │   └── User.java             (Model - entity User)
│   ├── main/
│   │   └── Test.java             (Main - Menu console)
│   ├── services/
│   │   ├── CertificateService.java (DAO - CRUD Certificate)
│   │   └── UserService.java       (DAO - User operations)
│   └── utils/
│       ├── Constants.java         (Hang so cau hinh)
│       ├── DBConnection.java      (Ket noi database)
│       └── Validator.java         (Validation du lieu)
├── test/
│   └── services/
│       └── CertificateServiceTest.java (JUnit 5 - 5 test cases)
├── setup.sql                       (SQL: database + table + stored procedures)
└── README.md                       (Huong dan)
```

## Stored Procedures (5 procedures chinh)
1. `insert_certificate(p_id, p_name, p_number, p_issue, p_expiry, p_score, p_user_id)`
2. `get_all_certificates()`
3. `update_certificate(p_id, p_name, p_number, p_issue, p_expiry, p_score, p_user_id)`
4. `delete_certificate(p_id)`
5. `find_certificate_by_id(p_id)`

## Validation
- Email: dung dinh dang (regex)
- Phone: chi chua so, 9-15 ky tu
- Score/GPA: pham vi 0.0 - 4.0
- ID: khong duoc rong, khong duoc trung

## Unit Test (5 test cases)
1. `testInsertRecord()` - kiem tra them du lieu thanh cong
2. `testFindById()` - kiem tra tim dung record
3. `testUpdateRecord()` - kiem tra update thanh cong
4. `testDeleteRecord()` - kiem tra xoa du lieu
5. `testValidation()` - kiem tra validation (email, phone, score)

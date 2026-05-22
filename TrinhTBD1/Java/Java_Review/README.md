# Song Management System

This is a Java CLI application for managing a database of songs and authors using JDBC and MySQL Stored Procedures.

## Tech Stack
- Java 17
- JDBC (MySQL Connector/J)
- MySQL Database

## Project Structure
- `review.entities`: Contains entity classes (`Song`, `Authur`).
- `review.database`: Contains `DBConnection` class for database connectivity.
- `review.dao`: Contains `SongDAO` for CRUD operations via MySQL stored procedures.
- `review.services`: Contains `SongService` for business logic and validation checks.
- `review.main`: Contains the main entry point `SongManagement`.
- `review.utils`: Contains validators and constants.

## Database Setup

1. Make sure you have a MySQL server running on `localhost:3306`.
2. Connect to MySQL with your preferred client (e.g., MySQL Workbench, Command Line) using the root user:
   - Username: `root`
   - Password: `root` (If your password is different, modify `DBConnection.java` accordingly)
3. Open the `song_db.sql` file located in the root of the project.
4. Execute the entire SQL script. It will automatically:
   - Create the `song_db` database.
   - Create `authurs` and `songs` tables.
   - Create 5 stored procedures (`insert_song`, `get_all_songs`, `update_song`, `delete_song`, `get_song_by_id`).

## How to Run the Application

Navigate to the project root directory and execute:
```bash
mvn clean compile exec:java -Dexec.mainClass="review.main.SongManagement"
```

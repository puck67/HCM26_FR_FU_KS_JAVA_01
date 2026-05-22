-- ============================================================
-- Quiz Management System - H2 Database Schema
-- ============================================================

CREATE TABLE IF NOT EXISTS quizzes (
    id          VARCHAR(10)  PRIMARY KEY,
    question    VARCHAR(255) NOT NULL,
    answer      VARCHAR(255) NOT NULL,
    difficulty  INT          NOT NULL,
    created_by  VARCHAR(100) NOT NULL
);

-- ============================================================
-- Stored Procedure 1: insert_quiz
-- ============================================================
CREATE ALIAS IF NOT EXISTS insert_quiz AS '
void insert_quiz(java.sql.Connection conn, String id, String question, String answer,
                 int difficulty, String createdBy) throws java.sql.SQLException {
    java.sql.PreparedStatement ps = conn.prepareStatement(
        "INSERT INTO quizzes(id, question, answer, difficulty, created_by) VALUES(?,?,?,?,?)");
    ps.setString(1, id);
    ps.setString(2, question);
    ps.setString(3, answer);
    ps.setInt(4, difficulty);
    ps.setString(5, createdBy);
    ps.executeUpdate();
    ps.close();
}
';

-- ============================================================
-- Stored Procedure 2: get_all_quizzes
-- ============================================================
CREATE ALIAS IF NOT EXISTS get_all_quizzes AS '
java.sql.ResultSet get_all_quizzes(java.sql.Connection conn) throws java.sql.SQLException {
    return conn.createStatement().executeQuery("SELECT * FROM quizzes ORDER BY id");
}
';

-- ============================================================
-- Stored Procedure 3: update_quiz
-- ============================================================
CREATE ALIAS IF NOT EXISTS update_quiz AS '
void update_quiz(java.sql.Connection conn, String id, String question, String answer,
                 int difficulty, String createdBy) throws java.sql.SQLException {
    java.sql.PreparedStatement ps = conn.prepareStatement(
        "UPDATE quizzes SET question=?, answer=?, difficulty=?, created_by=? WHERE id=?");
    ps.setString(1, question);
    ps.setString(2, answer);
    ps.setInt(3, difficulty);
    ps.setString(4, createdBy);
    ps.setString(5, id);
    ps.executeUpdate();
    ps.close();
}
';

-- ============================================================
-- Stored Procedure 4: delete_quiz
-- ============================================================
CREATE ALIAS IF NOT EXISTS delete_quiz AS '
void delete_quiz(java.sql.Connection conn, String id) throws java.sql.SQLException {
    java.sql.PreparedStatement ps = conn.prepareStatement("DELETE FROM quizzes WHERE id=?");
    ps.setString(1, id);
    ps.executeUpdate();
    ps.close();
}
';

-- ============================================================
-- Stored Procedure 5: find_quiz_by_id
-- ============================================================
CREATE ALIAS IF NOT EXISTS find_quiz_by_id AS '
java.sql.ResultSet find_quiz_by_id(java.sql.Connection conn, String id) throws java.sql.SQLException {
    java.sql.PreparedStatement ps = conn.prepareStatement("SELECT * FROM quizzes WHERE id=?");
    ps.setString(1, id);
    return ps.executeQuery();
}
';

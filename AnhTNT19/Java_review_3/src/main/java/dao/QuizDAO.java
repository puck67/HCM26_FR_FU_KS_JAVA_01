package dao;

import database.DBConnection;
import model.Quiz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public void add(Quiz q) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call insert_quiz(?,?,?,?,?)}")) {
            cs.setString(1, q.getId());
            cs.setString(2, q.getQuestion());
            cs.setString(3, q.getAnswer());
            cs.setInt(4, q.getDifficulty());
            cs.setString(5, q.getCreatedBy());
            cs.execute();
        }
    }

    public List<Quiz> getAll() throws SQLException {
        List<Quiz> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call get_all_quizzes()}")) {
            boolean hasRs = cs.execute();
            if (hasRs) {
                try (ResultSet rs = cs.getResultSet()) {
                    while (rs.next()) list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void update(Quiz q) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call update_quiz(?,?,?,?,?)}")) {
            cs.setString(1, q.getId());
            cs.setString(2, q.getQuestion());
            cs.setString(3, q.getAnswer());
            cs.setInt(4, q.getDifficulty());
            cs.setString(5, q.getCreatedBy());
            cs.execute();
        }
    }

    public void delete(String id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call delete_quiz(?)}")) {
            cs.setString(1, id);
            cs.execute();
        }
    }

    public Quiz findById(String id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall("{call find_quiz_by_id(?)}")) {
            cs.setString(1, id);
            boolean hasRs = cs.execute();
            if (hasRs) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) return mapRow(rs);
                }
            }
        }
        return null;
    }

    private Quiz mapRow(ResultSet rs) throws SQLException {
        return new Quiz(
            rs.getString("id"),
            rs.getString("question"),
            rs.getString("answer"),
            rs.getInt("difficulty"),
            rs.getString("created_by")
        );
    }
}

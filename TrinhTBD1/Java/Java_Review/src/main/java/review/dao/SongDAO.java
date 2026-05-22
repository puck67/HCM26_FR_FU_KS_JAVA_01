package review.dao;

import review.database.DBConnection;
import review.entities.Authur;
import review.entities.Song;
import review.utils.Constants;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SongDAO {

    public boolean add(Song song) {
        if (song == null || 
            song.getId() == null || !song.getId().matches(Constants.SONG_ID_REGEX) ||
            song.getTitle() == null || song.getTitle().trim().isEmpty() ||
            song.getAuthurName() == null || 
            song.getAuthurName().getAuthurId() == null || !song.getAuthurName().getAuthurId().matches(Constants.AUTHOR_ID_REGEX) ||
            song.getAuthurName().getAuthurName() == null || song.getAuthurName().getAuthurName().trim().isEmpty() ||
            song.getType() == null || !song.getType().matches(Constants.GENRE_REGEX) ||
            song.getDuration() <= 0 ||
            song.getReleaseDate() == null) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("{call insert_song(?, ?, ?, ?, ?, ?, ?)}");
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql.toString())) {
            
            stmt.setString(1, song.getId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getAuthurName().getAuthurId());
            stmt.setString(4, song.getAuthurName().getAuthurName());
            stmt.setString(5, song.getType());
            stmt.setInt(6, song.getDuration());
            stmt.setDate(7, Date.valueOf(song.getReleaseDate()));
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Song> getAll() {
        List<Song> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("{call get_all_songs()}");
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql.toString());
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Authur authur = new Authur(
                    rs.getString("authur_id"),
                    rs.getString("authur_name")
                );
                Song song = new Song(
                    authur,
                    rs.getInt("duration"),
                    rs.getString("id"),
                    rs.getString("title"),
                    rs.getString("type"),
                    rs.getDate("release_date").toLocalDate()
                );
                list.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean update(Song song) {
        if (song == null || 
            song.getId() == null || !song.getId().matches(Constants.SONG_ID_REGEX) ||
            song.getTitle() == null || song.getTitle().trim().isEmpty() ||
            song.getAuthurName() == null || 
            song.getAuthurName().getAuthurId() == null || !song.getAuthurName().getAuthurId().matches(Constants.AUTHOR_ID_REGEX) ||
            song.getAuthurName().getAuthurName() == null || song.getAuthurName().getAuthurName().trim().isEmpty() ||
            song.getType() == null || !song.getType().matches(Constants.GENRE_REGEX) ||
            song.getDuration() <= 0 ||
            song.getReleaseDate() == null) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("{call update_song(?, ?, ?, ?, ?, ?, ?)}");
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql.toString())) {
            
            stmt.setString(1, song.getId());
            stmt.setString(2, song.getTitle());
            stmt.setString(3, song.getAuthurName().getAuthurId());
            stmt.setString(4, song.getAuthurName().getAuthurName());
            stmt.setString(5, song.getType());
            stmt.setInt(6, song.getDuration());
            stmt.setDate(7, Date.valueOf(song.getReleaseDate()));
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String id) {
        if (id == null || !id.matches(Constants.SONG_ID_REGEX)) {
            return false;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("{call delete_song(?)}");
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql.toString())) {
            
            stmt.setString(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Song findById(String id) {
        if (id == null || !id.matches(Constants.SONG_ID_REGEX)) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("{call get_song_by_id(?)}");
        try (Connection conn = DBConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql.toString())) {
            
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Authur authur = new Authur(
                        rs.getString("authur_id"),
                        rs.getString("authur_name")
                    );
                    return new Song(
                        authur,
                        rs.getInt("duration"),
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("type"),
                        rs.getDate("release_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Authur> getAllAuthors() {
        List<Authur> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT authur_id, authur_name FROM authurs");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString());
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Authur(
                    rs.getString("authur_id"),
                    rs.getString("authur_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

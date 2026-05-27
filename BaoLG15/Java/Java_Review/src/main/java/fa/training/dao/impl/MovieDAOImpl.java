package fa.training.dao.impl;

import fa.training.dao.MovieDAO;
import fa.training.database.DBConnection;
import fa.training.entities.Director;
import fa.training.entities.Genre;
import fa.training.entities.Movie;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MovieDAOImpl extends BaseDAOImpl<Movie, String> implements MovieDAO {

    private static final String SP_INSERT = "CALL insert_movie(?,?,?,?,?,?)";
    private static final String SP_UPDATE = "CALL update_movie(?,?,?,?,?,?)";
    private static final String SP_DELETE = "CALL delete_movie(?)";
    private static final String SP_GET_ALL = "{call get_all_movies()}";
    private static final String SP_FIND_BY_ID = "{call find_movie_by_id(?)}";
    private static final String SP_FIND_BY_TITLE = "{call find_movies_by_title(?)}";

    @Override
    protected String getGetAllSP() { return SP_GET_ALL; }

    @Override
    protected String getFindByIdSP() { return SP_FIND_BY_ID; }

    @Override
    protected String getUpdateSP() { return SP_UPDATE; }

    @Override
    protected String getDeleteSP() { return SP_DELETE; }

    @Override
    protected Movie mapRow(ResultSet rs) throws SQLException {
        var movie = new Movie();
        movie.setId(rs.getString("id"));
        movie.setTitle(rs.getString("title"));

        var director = new Director(rs.getInt("director_id"), rs.getString("director_name"));
        movie.setDirector(director);

        var genre = new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        movie.setGenre(genre);

        movie.setReleaseYear(rs.getInt("release_year"));
        movie.setRating(rs.getDouble("rating"));
        return movie;
    }

    @Override
    protected void setUpdateParams(CallableStatement stmt, Movie entity) throws SQLException {
        stmt.setString(1, entity.getId());
        stmt.setString(2, entity.getTitle());
        stmt.setInt(3, entity.getDirector().getId());
        stmt.setInt(4, entity.getGenre().getId());
        stmt.setInt(5, entity.getReleaseYear());
        stmt.setDouble(6, entity.getRating());
    }

    @Override
    protected void setDeleteParam(CallableStatement stmt, String id) throws SQLException {
        stmt.setString(1, id);
    }

    @Override
    protected void setFindByIdParam(CallableStatement stmt, String id) throws SQLException {
        stmt.setString(1, id);
    }

    @Override
    public boolean add(Movie movie) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(SP_INSERT)) {
                stmt.setString(1, movie.getId());
                stmt.setString(2, movie.getTitle());
                stmt.setInt(3, movie.getDirector().getId());
                stmt.setInt(4, movie.getGenre().getId());
                stmt.setInt(5, movie.getReleaseYear());
                stmt.setDouble(6, movie.getRating());
                stmt.execute();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding movie: " + e.getMessage());
            return false;
        } finally {
            DBConnection.closeConnection(conn);
        }
    }

    @Override
    public List<Movie> findByTitle(String title) {
        var movies = new ArrayList<Movie>();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            try (CallableStatement stmt = conn.prepareCall(SP_FIND_BY_TITLE)) {
                stmt.setString(1, title);
                try (var rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        movies.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding movies by title: " + e.getMessage());
        } finally {
            DBConnection.closeConnection(conn);
        }
        return movies;
    }
}

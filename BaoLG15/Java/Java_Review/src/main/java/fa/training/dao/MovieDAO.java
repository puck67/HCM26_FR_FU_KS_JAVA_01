package fa.training.dao;

import fa.training.entities.Movie;

import java.util.List;

public interface MovieDAO extends BaseDAO<Movie, String> {
    List<Movie> findByTitle(String title);
}

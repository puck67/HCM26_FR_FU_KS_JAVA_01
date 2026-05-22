package fa.training.dao;

import fa.training.entities.Genre;

import java.util.List;

public interface GenreDAO extends BaseDAO<Genre, Integer> {
    List<Genre> findByName(String name);
}

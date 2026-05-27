package fa.training.dao;

import fa.training.entities.Director;

import java.util.List;

public interface DirectorDAO extends BaseDAO<Director, Integer> {
    List<Director> findByName(String name);
}

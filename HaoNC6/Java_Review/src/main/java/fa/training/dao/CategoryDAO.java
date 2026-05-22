package fa.training.dao;

import fa.training.entities.Category;
import java.util.List;

public interface CategoryDAO {
    List<Category> getAll();
    Category findById(String id);
    boolean add(Category category);
    boolean update(Category category);
    boolean delete(String id);
}

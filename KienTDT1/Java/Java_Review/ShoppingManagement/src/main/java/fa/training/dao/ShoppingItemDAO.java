package fa.training.dao;

import fa.training.entity.ShoppingItem;

import java.util.List;

public interface ShoppingItemDAO {

    boolean add(ShoppingItem item);

    List<ShoppingItem> getAll();

    boolean update(ShoppingItem item);

    boolean delete(String id);

    ShoppingItem findById(String id);
}
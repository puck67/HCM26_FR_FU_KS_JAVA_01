package fa.training.dao;

import fa.training.entities.Manufacturer;
import java.util.List;

public interface ManufacturerDAO {
    List<Manufacturer> getAll();
    Manufacturer findById(String id);
    boolean add(Manufacturer manufacturer);
    boolean update(Manufacturer manufacturer);
    boolean delete(String id);
}

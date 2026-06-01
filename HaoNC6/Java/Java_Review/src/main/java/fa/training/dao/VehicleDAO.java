package fa.training.dao;

import fa.training.entities.Vehicle;
import java.util.List;

public interface VehicleDAO {
    boolean add(Vehicle vehicle);
    List<Vehicle> getAll();
    boolean update(Vehicle vehicle);
    boolean delete(String id);
    Vehicle findById(String id);
}

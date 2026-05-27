package fa.training.dao;

import fa.training.model.Helicopter;
import java.util.List;

public interface HelicopterDAO {
    void create(Helicopter helicopter);

    void update(Helicopter helicopter);

    void delete(String id);

    Helicopter getHelicopterById(String id);

    List<Helicopter> getAllHelicopters();

    void createHelicopterSP(Helicopter helicopter);

    void updateHelicopterSP(Helicopter helicopter);

    void deleteHelicopterSP(String id);
}

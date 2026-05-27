package fa.training.dao;

import fa.training.model.Fixedwing;
import java.util.List;

public interface FixedwingDAO {
    void create(Fixedwing fixedwing);

    void update(Fixedwing fixedwing);

    void delete(String id);

    Fixedwing getFixedwingById(String id);

    List<Fixedwing> getAllFixedwings();

    void createFixedwingSP(Fixedwing fixedwing);

    void updateFixedwingSP(Fixedwing fixedwing);

    void deleteFixedwingSP(String id);
}

package fa.training.service;

import fa.training.dao.FixedwingDAO;
import fa.training.dao.HelicopterDAO;
import fa.training.dao.impl.FixedwingDAOImpl;
import fa.training.dao.impl.HelicopterDAOImpl;
import fa.training.model.Fixedwing;
import fa.training.model.Helicopter;

import java.util.List;

public class AirplaneService {

    private final FixedwingDAO fixedwingDAO = new FixedwingDAOImpl();
    private final HelicopterDAO helicopterDAO = new HelicopterDAOImpl();

    public void createFixedwing(Fixedwing fw) { fixedwingDAO.create(fw); }
    public void updateFixedwing(Fixedwing fw) { fixedwingDAO.update(fw); }
    public void deleteFixedwing(String id) { fixedwingDAO.delete(id); }
    public Fixedwing getFixedwingById(String id) { return fixedwingDAO.getFixedwingById(id); }
    public List<Fixedwing> getAllFixedwings() { return fixedwingDAO.getAllFixedwings(); }

    public void createFixedwingSP(Fixedwing fw) { fixedwingDAO.createFixedwingSP(fw); }
    public void updateFixedwingSP(Fixedwing fw) { fixedwingDAO.updateFixedwingSP(fw); }
    public void deleteFixedwingSP(String id) { fixedwingDAO.deleteFixedwingSP(id); }

    public void createHelicopter(Helicopter h) { helicopterDAO.create(h); }
    public void updateHelicopter(Helicopter h) { helicopterDAO.update(h); }
    public void deleteHelicopter(String id) { helicopterDAO.delete(id); }
    public Helicopter getHelicopterById(String id) { return helicopterDAO.getHelicopterById(id); }
    public List<Helicopter> getAllHelicopters() { return helicopterDAO.getAllHelicopters(); }

    public void createHelicopterSP(Helicopter h) { helicopterDAO.createHelicopterSP(h); }
    public void updateHelicopterSP(Helicopter h) { helicopterDAO.updateHelicopterSP(h); }
    public void deleteHelicopterSP(String id) { helicopterDAO.deleteHelicopterSP(id); }
}

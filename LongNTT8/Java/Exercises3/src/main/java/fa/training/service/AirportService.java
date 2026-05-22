package fa.training.service;

import fa.training.dao.AirportDAO;
import fa.training.dao.impl.AirportDAOImpl;
import fa.training.model.Airport;

import java.util.List;

public class AirportService {

    private final AirportDAO airportDAO = new AirportDAOImpl();

    public void createAirport(Airport airport) { airportDAO.create(airport); }
    public void updateAirport(Airport airport) { airportDAO.update(airport); }
    public void deleteAirport(String id) { airportDAO.delete(id); }
    public Airport getAirportById(String id) { return airportDAO.getAirportById(id); }
    public List<Airport> getAllAirport() { return airportDAO.getAllAirport(); }

    public void createAirportSP(Airport airport) { airportDAO.createAirportSP(airport); }
    public void updateAirportSP(Airport airport) { airportDAO.updateAirportSP(airport); }
    public void deleteAirportSP(String id) { airportDAO.deleteAirportSP(id); }
}

package fa.training.dao;

import fa.training.model.Airport;
import java.util.List;

public interface AirportDAO {
    void create(Airport airport);

    void update(Airport airport);

    void delete(String airportId);

    Airport getAirportById(String airportId);

    List<Airport> getAllAirport();

    void createAirportSP(Airport airport);

    void updateAirportSP(Airport airport);

    void deleteAirportSP(String airportId);
}

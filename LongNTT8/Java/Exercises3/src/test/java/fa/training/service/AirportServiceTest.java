package fa.training.service;

import fa.training.model.Airport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportServiceTest {

    private AirportService airportService;

    @BeforeEach
    void setUp() {
        airportService = new AirportService();
    }

    @Test
    void testGetAirportById_NonExistingId_ReturnsNull() {
        Airport result = airportService.getAirportById("AP-NONEXISTENT");
        assertNull(result);
    }

    @Test
    void testGetAllAirport_ReturnsList() {
        List<Airport> result = airportService.getAllAirport();
        assertNotNull(result);
    }
}

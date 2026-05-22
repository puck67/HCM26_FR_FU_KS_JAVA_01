package fa.training.controller;

import fa.training.database.LambdaUtil;
import fa.training.handler.ConsoleInputUtil;
import fa.training.model.Airport;
import fa.training.service.AirportService;

import java.util.List;

public class AirportController {

    private static final AirportService airportService = new AirportService();

    public static void createAirport() {
        Airport airport = ConsoleInputUtil.readAirportFromConsole(false);
        if (airport != null) {
            airportService.createAirport(airport);
            System.out.println("Airport created successfully!");
        }
    }

    public static void updateAirport() {
        Airport airport = ConsoleInputUtil.readAirportFromConsole(true);
        if (airport != null) {
            airportService.updateAirport(airport);
            System.out.println("Airport updated successfully!");
        }
    }

    public static void deleteAirport() {
        String id = ConsoleInputUtil.readString("Enter Airport ID to delete: ");
        airportService.deleteAirport(id);
        System.out.println("Airport deleted successfully!");
    }

    public static void listAirports() {
        List<Airport> list = airportService.getAllAirport();
        System.out.println("\n--- Airport List ---");

        if (list == null || list.isEmpty()) {
            System.out.println("No airports found.");
        } else {
            list.forEach(a -> System.out.printf(
                    "ID: %s | Name: %s | Runway: %.2f%n",
                    a.getId(), a.getName(), a.getRunwaySize()));
        }
    }

    public static void createAirportSP() {
        Airport airport = ConsoleInputUtil.readAirportFromConsole(false);
        if (airport != null) {
            LambdaUtil.handleSQLException(() -> {
                airportService.createAirportSP(airport);
                System.out.println("Airport created via SP successfully!");
            });
        }
    }

    public static void updateAirportSP() {
        Airport airport = ConsoleInputUtil.readAirportFromConsole(true);
        if (airport != null) {
            LambdaUtil.handleSQLException(() -> {
                airportService.updateAirportSP(airport);
                System.out.println("Airport updated via SP successfully!");
            });
        }
    }

    public static void deleteAirportSP() {
        String id = ConsoleInputUtil.readString("Enter Airport ID to delete: ");
        LambdaUtil.handleSQLException(() -> {
            airportService.deleteAirportSP(id);
            System.out.println("Airport deleted via SP successfully!");
        });
    }
}

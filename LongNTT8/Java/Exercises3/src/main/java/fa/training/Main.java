package fa.training;

import fa.training.controller.AirplaneController;
import fa.training.controller.AirportController;
import fa.training.handler.ConsoleInputUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    private static final Map<Integer, Runnable> masterMenuMap = new LinkedHashMap<>();
    private static final Map<Integer, Runnable> airplaneMenuMap = new LinkedHashMap<>();
    private static final Map<Integer, Runnable> airportMenuMap = new LinkedHashMap<>();

    static {
        masterMenuMap.put(1, Main::airplaneMenuLoop);
        masterMenuMap.put(2, Main::airportMenuLoop);
        masterMenuMap.put(0, () -> { System.out.println("Goodbye!"); System.exit(0); });

        airplaneMenuMap.put(1, AirplaneController::createAirplane);
        airplaneMenuMap.put(2, AirplaneController::updateAirplane);
        airplaneMenuMap.put(3, AirplaneController::deleteAirplane);
        airplaneMenuMap.put(4, AirplaneController::listAirplanes);
        airplaneMenuMap.put(5, AirplaneController::createAirplaneSP);
        airplaneMenuMap.put(6, AirplaneController::updateAirplaneSP);
        airplaneMenuMap.put(7, AirplaneController::deleteAirplaneSP);
        airplaneMenuMap.put(0, () -> System.out.println("Returning to Master Menu..."));

        airportMenuMap.put(1, AirportController::createAirport);
        airportMenuMap.put(2, AirportController::updateAirport);
        airportMenuMap.put(3, AirportController::deleteAirport);
        airportMenuMap.put(4, AirportController::listAirports);
        airportMenuMap.put(5, AirportController::createAirportSP);
        airportMenuMap.put(6, AirportController::updateAirportSP);
        airportMenuMap.put(7, AirportController::deleteAirportSP);
        airportMenuMap.put(0, () -> System.out.println("Returning to Master Menu..."));
    }

    public static void main(String[] args) {
        int choice;
        do {
            printMenu("MASTER SYSTEM MENU",
                "1. Airplane Management",
                "2. Airport Management",
                "0. Exit"
            );
            choice = runChoice(masterMenuMap);
        } while (choice != 0);
    }

    private static void airplaneMenuLoop() {
        int choice;
        do {
            printMenu("AIRPLANE MANAGEMENT",
                "1. Create Airplane",
                "2. Update Airplane",
                "3. Delete Airplane",
                "4. List All Airplanes",
                "5. Create with Stored Procedure",
                "6. Update with Stored Procedure",
                "7. Delete with Stored Procedure",
                "0. Back"
            );
            choice = runChoice(airplaneMenuMap);
        } while (choice != 0);
    }

    private static void airportMenuLoop() {
        int choice;
        do {
            printMenu("AIRPORT MANAGEMENT",
                "1. Create Airport",
                "2. Update Airport",
                "3. Delete Airport",
                "4. List All Airports",
                "5. Create with Stored Procedure",
                "6. Update with Stored Procedure",
                "7. Delete with Stored Procedure",
                "0. Back"
            );
            choice = runChoice(airportMenuMap);
        } while (choice != 0);
    }

    private static void printMenu(String title, String... options) {
        System.out.println("\n====== " + title + " ======");
        for (String option : options) {
            System.out.println(option);
        }
    }


    private static int runChoice(Map<Integer, Runnable> menuMap) {
        try {
            int choice = ConsoleInputUtil.readInt("Choose an option: ");
            menuMap.getOrDefault(choice, () -> System.out.println("Invalid option!")).run();
            return choice;
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return -1;
        }
    }
}

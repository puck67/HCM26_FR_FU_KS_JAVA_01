package fa.training.handler;

import fa.training.model.Airport;
import fa.training.model.Fixedwing;
import fa.training.model.Helicopter;
import fa.training.model.PlaneType;

import java.util.Scanner;

public class ConsoleInputUtil {

    public static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty())
                return input;
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    public static PlaneType readPlaneType(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return PlaneType.fromString(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static Airport readAirportFromConsole(boolean isUpdate) {
        String id;
        if (isUpdate) {
            id = readString("Enter Airport ID to update: ");
        } else {
            id = ValidationUtil.generateId("AP");
            System.out.println("Generated Airport ID: " + id);
        }

        String name = readString("Enter Airport Name: ");
        double runwaySize = readDouble("Enter Runway Size: ");
        int maxFixed = readInt("Enter Max Fixed Wing Parking Place: ");
        int maxRotated = readInt("Enter Max Rotated Wing Parking Place: ");

        return new Airport(id, name, runwaySize, maxFixed, maxRotated);
    }

    public static Fixedwing readFixedwingFromConsole(boolean isUpdate) {
        String id;
        if (isUpdate) {
            id = readString("Enter Fixedwing ID to update: ");
        } else {
            id = ValidationUtil.generateId("FW");
            System.out.println("Generated Fixedwing ID: " + id);
        }

        String model = readString("Enter Model: ");
        double cruiseSpeed = readDouble("Enter Cruise Speed: ");
        double emptyWeight = readDouble("Enter Empty Weight: ");
        double maxTakeoffWeight = readDouble("Enter Max Takeoff Weight: ");
        PlaneType planeType = readPlaneType("Enter Plane Type (CAG, LGR, PRV): ");
        double runwaySize = readDouble("Enter Min Needed Runway Size: ");

        return new Fixedwing(id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight, planeType, runwaySize);
    }

    public static Helicopter readHelicopterFromConsole(boolean isUpdate) {
        String id;
        if (isUpdate) {
            id = readString("Enter Helicopter ID to update: ");
        } else {
            id = ValidationUtil.generateId("HL");
            System.out.println("Generated Helicopter ID: " + id);
        }

        String model = readString("Enter Model: ");
        double cruiseSpeed = readDouble("Enter Cruise Speed: ");
        double emptyWeight = readDouble("Enter Empty Weight: ");
        double maxTakeoffWeight = readDouble("Enter Max Takeoff Weight: ");
        double range = readDouble("Enter Range: ");

        return new Helicopter(id, model, cruiseSpeed, emptyWeight, maxTakeoffWeight, range);
    }
}

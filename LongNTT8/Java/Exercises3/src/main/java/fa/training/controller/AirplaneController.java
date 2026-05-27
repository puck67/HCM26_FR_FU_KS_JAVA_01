package fa.training.controller;

import fa.training.handler.ConsoleInputUtil;
import fa.training.model.Fixedwing;
import fa.training.model.Helicopter;
import fa.training.service.AirplaneService;

import java.util.List;

public class AirplaneController {

    private static final AirplaneService airplaneService = new AirplaneService();

    public static void createAirplane() {
        dispatchByType(
                () -> {
                    Fixedwing fw = ConsoleInputUtil.readFixedwingFromConsole(false);
                    if (fw != null) {
                        airplaneService.createFixedwing(fw);
                        System.out.println("Fixedwing created successfully!");
                    }
                },
                () -> {
                    Helicopter h = ConsoleInputUtil.readHelicopterFromConsole(false);
                    if (h != null) {
                        airplaneService.createHelicopter(h);
                        System.out.println("Helicopter created successfully!");
                    }
                });
    }

    public static void updateAirplane() {
        dispatchByType(
                () -> {
                    Fixedwing fw = ConsoleInputUtil.readFixedwingFromConsole(true);
                    if (fw != null) {
                        airplaneService.updateFixedwing(fw);
                        System.out.println("Fixedwing updated successfully!");
                    }
                },
                () -> {
                    Helicopter h = ConsoleInputUtil.readHelicopterFromConsole(true);
                    if (h != null) {
                        airplaneService.updateHelicopter(h);
                        System.out.println("Helicopter updated successfully!");
                    }
                });
    }

    public static void deleteAirplane() {
        dispatchByType(
                () -> {
                    String id = ConsoleInputUtil.readString("Enter Fixedwing ID to delete: ");
                    airplaneService.deleteFixedwing(id);
                    System.out.println("Fixedwing deleted successfully!");
                },
                () -> {
                    String id = ConsoleInputUtil.readString("Enter Helicopter ID to delete: ");
                    airplaneService.deleteHelicopter(id);
                    System.out.println("Helicopter deleted successfully!");
                });
    }

    public static void listAirplanes() {
        dispatchByType(
                () -> {
                    List<Fixedwing> list = airplaneService.getAllFixedwings();
                    System.out.println("\n--- Fixedwing List ---");
                    if (list == null || list.isEmpty()) {
                        System.out.println("No fixedwings found.");
                    } else {
                        list.forEach(fw -> System.out.printf(
                                "ID: %s | Model: %s | Speed: %.2f | Type: %s%n",
                                fw.getId(), fw.getModel(), fw.getCruiseSpeed(), fw.getPlaneType()));
                    }
                },
                () -> {
                    List<Helicopter> list = airplaneService.getAllHelicopters();
                    System.out.println("\n--- Helicopter List ---");
                    if (list == null || list.isEmpty()) {
                        System.out.println("No helicopters found.");
                    } else {
                        list.forEach(h -> System.out.printf(
                                "ID: %s | Model: %s | Speed: %.2f | Range: %.2f%n",
                                h.getId(), h.getModel(), h.getCruiseSpeed(), h.getRange()));
                    }
                });
    }

    public static void createAirplaneSP() {
        dispatchByType(
                () -> {
                    Fixedwing fw = ConsoleInputUtil.readFixedwingFromConsole(false);
                    if (fw != null) {
                        airplaneService.createFixedwingSP(fw);
                        System.out.println("Fixedwing created via SP successfully!");
                    }
                },
                () -> {
                    Helicopter h = ConsoleInputUtil.readHelicopterFromConsole(false);
                    if (h != null) {
                        airplaneService.createHelicopterSP(h);
                        System.out.println("Helicopter created via SP successfully!");
                    }
                });
    }

    public static void updateAirplaneSP() {
        dispatchByType(
                () -> {
                    Fixedwing fw = ConsoleInputUtil.readFixedwingFromConsole(true);
                    if (fw != null) {
                        airplaneService.updateFixedwingSP(fw);
                        System.out.println("Fixedwing updated via SP successfully!");
                    }
                },
                () -> {
                    Helicopter h = ConsoleInputUtil.readHelicopterFromConsole(true);
                    if (h != null) {
                        airplaneService.updateHelicopterSP(h);
                        System.out.println("Helicopter updated via SP successfully!");
                    }
                });
    }

    public static void deleteAirplaneSP() {
        dispatchByType(
                () -> {
                    String id = ConsoleInputUtil.readString("Enter Fixedwing ID to delete: ");
                    airplaneService.deleteFixedwingSP(id);
                    System.out.println("Fixedwing deleted via SP successfully!");
                },
                () -> {
                    String id = ConsoleInputUtil.readString("Enter Helicopter ID to delete: ");
                    airplaneService.deleteHelicopterSP(id);
                    System.out.println("Helicopter deleted via SP successfully!");
                });
    }

    private static void dispatchByType(Runnable fixedwingAction, Runnable helicopterAction) {
        System.out.println("1. Fixedwing\n2. Helicopter");
        int type = ConsoleInputUtil.readInt("Choose type: ");
        if (type == 1) {
            fixedwingAction.run();
        } else if (type == 2) {
            helicopterAction.run();
        } else {
            System.out.println("Invalid type.");
        }
    }
}

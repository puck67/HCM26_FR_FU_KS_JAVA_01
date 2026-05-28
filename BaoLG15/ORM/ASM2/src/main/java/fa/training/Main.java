package fa.training;

import fa.training.ui.ConsoleUI;
import fa.training.ui.MenuHandler;
import fa.training.util.HibernateUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleUI.printInfo("Connecting to database...");
            HibernateUtil.getSessionFactory();
            ConsoleUI.printSuccess("Database connected. Movie Theater System is ready.");

            MenuHandler menuHandler = new MenuHandler(scanner);
            menuHandler.run();
        } catch (Exception ex) {
            ConsoleUI.printError("Fatal error: " + ex.getMessage());
        }
    }
}
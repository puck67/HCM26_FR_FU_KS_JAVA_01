import dao.GameDAO;
import model.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GameDAO dao = new GameDAO();
        Map<Integer, Runnable> menu = new HashMap<>();

        menu.put(1, () -> {
            try {
                System.out.print("Enter ID: ");
                String id = sc.nextLine();
                System.out.print("Enter name: ");
                String name = sc.nextLine();
                System.out.print("Enter genre: ");
                String genre = sc.nextLine();
                double price;
                while (true) {
                    try {
                        System.out.print("Enter price: ");
                        price = Double.parseDouble(sc.nextLine());
                        if (price > 0) {
                            break;
                        } else {
                            System.out.println("Price must be > 0");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price");
                    }
                }
                System.out.print("Enter developer: ");
                String developer = sc.nextLine();

                Game game = new Game(id, name, genre, price, developer);
                boolean result = dao.addGame(game);
                System.out.println(result ? "Added successfully." : "Add failed.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        menu.put(2, () -> {
            dao.getAllGames().forEach(System.out::println);
        });

        menu.put(3, () -> {
            try {
                System.out.print("Enter game ID: ");
                String id = sc.nextLine();
                Game game = dao.findById(id);
                if (game == null) {
                    System.out.println("Game not found.");
                    return;
                }
                System.out.print("New name: ");
                game.setName(sc.nextLine());
                System.out.print("New genre: ");
                game.setGenre(sc.nextLine());
                System.out.print("New price: ");
                game.setPrice(Double.parseDouble(sc.nextLine()));
                System.out.print("New developer: ");
                game.setDeveloper(sc.nextLine());

                boolean result = dao.updateGame(game);
                System.out.println(result ? "Updated successfully." : "Update failed.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });

        menu.put(4, () -> {
            System.out.print("Enter game ID: ");
            String id = sc.nextLine();
            boolean result = dao.deleteGame(id);
            System.out.println(result ? "Deleted successfully." : "Delete failed.");
        });

        menu.put(5, () -> {
            System.out.print("Enter game ID: ");
            String id = sc.nextLine();
            Game game = dao.findById(id);
            if (game != null) {
                System.out.println(game);
            } else {
                System.out.println("Game not found.");
            }
        });

        int choice = 0;
        do {
            System.out.println("\n===== GAME MANAGEMENT =====");
            System.out.println("1. Add new game");
            System.out.println("2. Display all games");
            System.out.println("3. Update game");
            System.out.println("4. Delete game");
            System.out.println("5. Search game by ID");
            System.out.println("6. Exit");
            try {
                System.out.print("Choose: ");
                choice = Integer.parseInt(sc.nextLine());
                if (choice == 6) {
                    System.out.println("Program terminated.");
                    break;
                }
                Runnable action = menu.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        } while (true);

        sc.close();
    }
}

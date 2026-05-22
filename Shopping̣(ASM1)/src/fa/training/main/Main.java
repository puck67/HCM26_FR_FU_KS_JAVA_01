package fa.training.main;

import fa.training.service.ShoppingService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        ShoppingService service = new ShoppingService();

        Scanner scanner = new Scanner(System.in);

        int choice;

        do {

            System.out.println("\n========= SHOPPING MANAGEMENT =========");

            System.out.println("1. Add shopping item");
            System.out.println("2. Display all items");
            System.out.println("3. Update item");
            System.out.println("4. Delete item");
            System.out.println("5. Search item");
            System.out.println("6. Sort by name");
            System.out.println("7. Exit");

            while (true) {

                try {

                    System.out.print("Choose: ");
                    choice =
                            Integer.parseInt(scanner.nextLine());

                    break;

                } catch (Exception e) {
                    System.out.println("Invalid choice!");
                }
            }

            switch (choice) {

                case 1:
                    service.addItem();
                    break;

                case 2:
                    service.displayItems();
                    break;

                case 3:
                    service.updateItem();
                    break;

                case 4:
                    service.deleteItem();
                    break;

                case 5:
                    service.searchItem();
                    break;

                case 6:
                    service.sortByName();
                    break;

                case 7:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 7);
    }
}
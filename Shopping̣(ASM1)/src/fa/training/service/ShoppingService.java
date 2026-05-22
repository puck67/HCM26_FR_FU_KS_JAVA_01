package fa.training.service;

import fa.training.entities.ShoppingItem;
import fa.training.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ShoppingService {

    private final List<ShoppingItem> items;
    private final Scanner scanner;

    public ShoppingService() {
        items = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    // ================= ADD =================

    public void addItem() {

        System.out.println("\n===== ADD SHOPPING ITEM =====");

        String id;

        while (true) {

            System.out.print("Enter item id: ");
            id = scanner.nextLine();

            if (ValidationUtils.isEmpty(id)) {
                System.out.println("ID cannot be empty!");
                continue;
            }

            if (findById(id) != null) {
                System.out.println("ID already exists!");
                continue;
            }

            break;
        }

        String name;

        while (true) {

            System.out.print("Enter item name: ");
            name = scanner.nextLine();

            if (ValidationUtils.isEmpty(name)) {
                System.out.println("Name cannot be empty!");
            } else {
                break;
            }
        }

        double price;

        while (true) {

            try {

                System.out.print("Enter price: ");
                price = Double.parseDouble(scanner.nextLine());

                if (!ValidationUtils.isValidPrice(price)) {
                    System.out.println("Price must be > 0");
                    continue;
                }

                break;

            } catch (Exception e) {
                System.out.println("Invalid price!");
            }
        }

        int quantity;

        while (true) {

            try {

                System.out.print("Enter quantity: ");
                quantity = Integer.parseInt(scanner.nextLine());

                if (!ValidationUtils.isValidQuantity(quantity)) {
                    System.out.println("Quantity must be >= 0");
                    continue;
                }

                break;

            } catch (Exception e) {
                System.out.println("Invalid quantity!");
            }
        }

        String brand;

        while (true) {

            System.out.print("Enter brand: ");
            brand = scanner.nextLine();

            if (ValidationUtils.isEmpty(brand)) {
                System.out.println("Brand cannot be empty!");
            } else {
                break;
            }
        }

        ShoppingItem item =
                new ShoppingItem(id, name, price, quantity, brand);

        items.add(item);

        System.out.println("Add item successfully!");
    }

    // ================= DISPLAY =================

    public void displayItems() {

        System.out.println("\n===== SHOPPING ITEMS =====");

        if (items.isEmpty()) {
            System.out.println("No items found!");
            return;
        }

        System.out.printf(
                "%-10s %-20s %-10s %-10s %-15s\n",
                "ID",
                "NAME",
                "PRICE",
                "QUANTITY",
                "BRAND"
        );

        for (ShoppingItem item : items) {
            System.out.println(item);
        }
    }

    // ================= UPDATE =================

    public void updateItem() {

        System.out.print("Enter item id: ");
        String id = scanner.nextLine();

        ShoppingItem item = findById(id);

        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        System.out.print("Enter new name: ");
        item.setItemName(scanner.nextLine());

        while (true) {

            try {

                System.out.print("Enter new price: ");
                double price =
                        Double.parseDouble(scanner.nextLine());

                if (!ValidationUtils.isValidPrice(price)) {
                    System.out.println("Price must be > 0");
                    continue;
                }

                item.setPrice(price);

                break;

            } catch (Exception e) {
                System.out.println("Invalid price!");
            }
        }

        while (true) {

            try {

                System.out.print("Enter new quantity: ");
                int quantity =
                        Integer.parseInt(scanner.nextLine());

                if (!ValidationUtils.isValidQuantity(quantity)) {
                    System.out.println("Quantity must be >= 0");
                    continue;
                }

                item.setQuantity(quantity);

                break;

            } catch (Exception e) {
                System.out.println("Invalid quantity!");
            }
        }

        System.out.print("Enter new brand: ");
        item.setBrand(scanner.nextLine());

        System.out.println("Update successfully!");
    }

    // ================= DELETE =================

    public void deleteItem() {

        System.out.print("Enter item id: ");
        String id = scanner.nextLine();

        ShoppingItem item = findById(id);

        if (item == null) {
            System.out.println("Item not found!");
            return;
        }

        items.remove(item);

        System.out.println("Delete successfully!");
    }

    // ================= SEARCH =================

    public void searchItem() {

        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().toLowerCase();

        boolean found = false;

        for (ShoppingItem item : items) {

            if (item.getItemId().toLowerCase().contains(keyword)
                    || item.getItemName().toLowerCase().contains(keyword)) {

                System.out.println(item);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No item found!");
        }
    }

    // ================= SORT =================

    public void sortByName() {

        items.sort(
                Comparator.comparing(ShoppingItem::getItemName)
        );

        System.out.println("Sort successfully!");
    }

    // ================= FIND =================

    private ShoppingItem findById(String id) {

        for (ShoppingItem item : items) {

            if (item.getItemId().equalsIgnoreCase(id)) {
                return item;
            }
        }

        return null;
    }
}
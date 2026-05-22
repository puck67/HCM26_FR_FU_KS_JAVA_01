package fa.training.entities;

public class ShoppingItem {

    private String itemId;
    private String itemName;
    private double price;
    private int quantity;
    private String brand;

    public ShoppingItem() {
    }

    public ShoppingItem(String itemId,
                        String itemName,
                        double price,
                        int quantity,
                        String brand) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return String.format(
                "%-10s %-20s %-10.2f %-10d %-15s",
                itemId,
                itemName,
                price,
                quantity,
                brand
        );
    }
}
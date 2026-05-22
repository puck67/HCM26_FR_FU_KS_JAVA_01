package fa.training.entity;

public class ShoppingItem {

    private String id;
    private String itemName;
    private String category;
    private double price;
    private int quantity;

    public ShoppingItem() {
    }

    public ShoppingItem(
            String id,
            String itemName,
            String category,
            double price,
            int quantity
    ) {

        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    @Override
    public String toString() {

        return id + " | "
                + itemName + " | "
                + category + " | "
                + price + " | "
                + quantity;
    }
}
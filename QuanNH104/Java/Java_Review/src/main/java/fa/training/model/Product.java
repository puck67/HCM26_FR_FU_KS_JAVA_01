package fa.training.model;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String warehouseId;

    public Product() {
    }

    public Product(String id, String name, double price, int quantity, String warehouseId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.warehouseId = warehouseId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    @Override
    public String toString() {
        return String.format("Product [ID: %s, Name: %s, Price: %,.2f, Quantity: %d, Warehouse ID: %s]", 
                id, name, price, quantity, warehouseId);
    }
}

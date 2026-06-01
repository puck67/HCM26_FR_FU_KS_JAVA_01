package fa.training.entities;

public class Vehicle {
    private String id;
    private String model;
    private double price;
    private String ownerEmail;
    private String ownerPhone;
    private Manufacturer manufacturer;
    private Category category;

    public Vehicle() {
    }

    public Vehicle(String id, String model, double price, String ownerEmail, String ownerPhone, Manufacturer manufacturer, Category category) {
        this.id = id;
        this.model = model;
        this.price = price;
        this.ownerEmail = ownerEmail;
        this.ownerPhone = ownerPhone;
        this.manufacturer = manufacturer;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Vehicle [id=%s, model=%s, price=%,.2f, ownerEmail=%s, ownerPhone=%s, manufacturer=%s, category=%s]"
                .formatted(
                        id, 
                        model, 
                        price, 
                        ownerEmail, 
                        ownerPhone, 
                        manufacturer != null ? manufacturer.getName() : "null", 
                        category != null ? category.getName() : "null"
                );
    }
}

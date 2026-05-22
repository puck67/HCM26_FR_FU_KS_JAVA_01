package src.entities;

public class Product {
    private String id;
    private String name;
    private double price;
    private int quantity;
    private String category;

    public Product(){

    }

    public Product(String id, String name, double price, int quantity, String category){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String id){
        this.id = id;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String toFileString(){
        return "|" + id + "|" + name + "|" + price + "|" + quantity + "|" + category + "|";
    }

    public static Product fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 5) return null;
        try {
            return new Product(p[0], p[1], Double.parseDouble(p[2]), Integer.parseInt(p[3]), p[4]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("%-8s %-20s %10.2f %6d  %s",
                id, name, price, quantity, category);
    }
}

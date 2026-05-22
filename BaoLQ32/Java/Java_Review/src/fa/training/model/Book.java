package fa.training.model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String email;       // author contact email
    private String phone;       // publisher phone
    private double price;
    private int quantity;
    private String category;

    public Book() {}

    public Book(String id, String title, String author,
                String email, String phone,
                double price, int quantity, String category) {
        this.id       = id;
        this.title    = title;
        this.author   = author;
        this.email    = email;
        this.phone    = phone;
        this.price    = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getId()           { return id; }
    public void setId(String id)    { this.id = id; }

    public String getTitle()              { return title; }
    public void setTitle(String title)    { this.title = title; }

    public String getAuthor()               { return author; }
    public void setAuthor(String author)    { this.author = author; }

    public String getEmail()              { return email; }
    public void setEmail(String email)    { this.email = email; }

    public String getPhone()              { return phone; }
    public void setPhone(String phone)    { this.phone = phone; }

    public double getPrice()              { return price; }
    public void setPrice(double price)    { this.price = price; }

    public int getQuantity()                { return quantity; }
    public void setQuantity(int quantity)   { this.quantity = quantity; }

    public String getCategory()                 { return category; }
    public void setCategory(String category)    { this.category = category; }


    @Override
    public String toString() {
        return String.format("| %-8s | %-30s | %-20s | %-10.2f | %-5d | %-15s |",
                id, title, author, price, quantity, category);
    }
}

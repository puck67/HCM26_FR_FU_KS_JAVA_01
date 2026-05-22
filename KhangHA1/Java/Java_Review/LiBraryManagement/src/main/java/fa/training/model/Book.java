package fa.training.model;

public class Book {

    private String id;
    private String title;

    private Integer authorId;
    private String authorName;

    private Integer categoryId;
    private String categoryName;

    private int publishYear;
    private double price;

    public Book() {
    }

    // INSERT / UPDATE constructor
    public Book(
            String id,
            String title,
            Integer authorId,
            Integer categoryId,
            int publishYear,
            double price
    ) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.publishYear = publishYear;
        this.price = price;
    }

    // SELECT / DISPLAY constructor
    public Book(
            String id,
            String title,
            String authorName,
            String categoryName,
            int publishYear,
            double price
    ) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.categoryName = categoryName;
        this.publishYear = publishYear;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
package model;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private String nationality;

    public Customer(String customerId, String name, String email, String phone, String nationality) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.nationality = nationality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', email='%s', phone='%s', nationality='%s'}",
            customerId, name, email, phone, nationality);
    }
}

package fa.training.model;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private String id;
    private String name;
    private String address;
    private int capacity;
    private List<Employee> employees = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public Warehouse() {
    }

    public Warehouse(String id, String name, String address, int capacity) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCurrentProductCount() {
        return products.stream().mapToInt(Product::getQuantity).sum();
    }

    @Override
    public String toString() {
        return String.format("Warehouse [ID: %s, Name: %s, Address: %s, Capacity: %d, Current Products: %d, Employees Count: %d]", 
                id, name, address, capacity, getCurrentProductCount(), employees.size());
    }
}

package src.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import src.entities.Product;

public class ProductService {

    private final ArrayList<Product> products;

    public ProductService(List<Product> initial) {
        this.products = new ArrayList<>(initial);
    }


    public void add(Product p) {
        products.add(p);
    }

    public boolean delete(String id) {
        Product found = findById(id);
        if (found != null) {
            products.remove(found);
            return true;
        }
        return false;
    }


    public ArrayList<Product> getAll() {
        return products;
    }

    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equalsIgnoreCase(id)) return p;
        }
        return null;
    }

    public List<Product> findByName(String keyword) {
        List<Product> result = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }


    public void displayAll() {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product p : products) {
            System.out.println(p);
        }
    }


    public void sortByName() {
        products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
    }

    public void sortById() {
        products.sort(Comparator.comparing(Product::getId, String.CASE_INSENSITIVE_ORDER));
    }
}

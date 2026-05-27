package fa.training.controller;

import fa.training.dao.CategoryDAO;
import fa.training.dao.impl.CategoryDAOImpl;
import fa.training.model.Category;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;
import fa.training.view.CategoryView;

public class CategoryController {

    private final CategoryDAO categoryDAO =
            new CategoryDAOImpl();

    private final CategoryView view =
            new CategoryView();

    public CategoryView getView() {
        return view;
    }

    private boolean isDuplicateName(
            String name,
            Integer excludeId) {

        return categoryDAO.getAll().stream()
                .anyMatch(c ->
                        c.getName().equalsIgnoreCase(name.trim())
                                && (excludeId == null
                                || c.getId() != excludeId));
    }

    public void addCategory() {

        String name;

        while (true) {

            name = ConsoleUtil.readString(
                    "Enter category name: ");

            if (!Validator.isValidName(name)) {
                view.printWarning(
                        "Name cannot be empty.");
            } else if (isDuplicateName(name, null)) {
                view.printWarning(
                        "Category already exists.");
            } else {
                break;
            }
        }

        Category category = new Category();
        category.setName(name);

        if (categoryDAO.add(category)) {
            view.printSuccess(
                    "Category added successfully.");
        } else {
            view.printError(
                    "Failed to add category.");
        }
    }

    public void displayAllCategories() {
        view.displayCategories(
                categoryDAO.getAll());
    }

    public void updateCategory() {

        view.printHeader("Update Category");

        int id =
                ConsoleUtil.readInt(
                        "Enter category ID: ");

        Category existing =
                categoryDAO.findById(id);

        if (existing == null) {
            view.printWarning(
                    "Category not found.");
            return;
        }

        String name;

        while (true) {

            name = ConsoleUtil.readString(
                    "Enter new name [%s]: "
                            .formatted(
                                    existing.getName()));

            if (name.isBlank()) {
                name = existing.getName();
            }

            if (!Validator.isValidName(name)) {
                view.printWarning(
                        "Name cannot be empty.");
            } else if (!name.equalsIgnoreCase(
                    existing.getName())
                    && isDuplicateName(
                    name,
                    existing.getId())) {

                view.printWarning(
                        "Category already exists.");
            } else {
                break;
            }
        }

        existing.setName(name);

        if (categoryDAO.update(existing)) {
            view.printSuccess(
                    "Category updated successfully.");
        } else {
            view.printError(
                    "Failed to update category.");
        }
    }

    public void deleteCategory() {

        view.printHeader("Delete Category");

        int id =
                ConsoleUtil.readInt(
                        "Enter category ID: ");

        Category existing =
                categoryDAO.findById(id);

        if (existing == null) {
            view.printWarning(
                    "Category not found.");
            return;
        }

        if (categoryDAO.delete(id)) {
            view.printSuccess(
                    "Category deleted successfully.");
        } else {
            view.printError(
                    "Failed to delete category.");
        }
    }
}
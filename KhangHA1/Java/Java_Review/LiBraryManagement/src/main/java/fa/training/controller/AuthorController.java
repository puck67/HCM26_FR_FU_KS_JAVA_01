package fa.training.controller;

import fa.training.dao.AuthorDAO;
import fa.training.dao.impl.AuthorDAOImpl;
import fa.training.model.Author;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;
import fa.training.view.AuthorView;

public class AuthorController {

    private final AuthorDAO authorDAO = new AuthorDAOImpl();
    private final AuthorView view = new AuthorView();

    public AuthorView getView() {
        return view;
    }

    private boolean isDuplicateName(String name, Integer excludeId) {
        return authorDAO.getAll().stream()
                .anyMatch(a ->
                        a.getName().equalsIgnoreCase(name.trim())
                                && (excludeId == null || a.getId() != excludeId));
    }

    public void addAuthor() {

        String name;

        while (true) {

            name = ConsoleUtil.readString("Enter author name: ");

            if (!Validator.isValidName(name)) {
                view.printWarning("Name cannot be empty.");
            } else if (isDuplicateName(name, null)) {
                view.printWarning("Author already exists.");
            } else {
                break;
            }
        }

        Author author = new Author();
        author.setName(name);

        if (authorDAO.add(author)) {
            view.printSuccess("Author added successfully.");
        } else {
            view.printError("Failed to add author.");
        }
    }

    public void displayAllAuthors() {
        view.displayAuthors(authorDAO.getAll());
    }

    public void updateAuthor() {

        view.printHeader("Update Author");

        int id = ConsoleUtil.readInt("Enter author ID: ");

        Author existing = authorDAO.findById(id);

        if (existing == null) {
            view.printWarning("Author not found.");
            return;
        }

        String name;

        while (true) {

            name = ConsoleUtil.readString(
                    "Enter new name [%s]: "
                            .formatted(existing.getName()));

            if (name.isBlank()) {
                name = existing.getName();
            }

            if (!Validator.isValidName(name)) {
                view.printWarning("Name cannot be empty.");
            } else if (!name.equalsIgnoreCase(existing.getName())
                    && isDuplicateName(name, existing.getId())) {
                view.printWarning("Author already exists.");
            } else {
                break;
            }
        }

        existing.setName(name);

        if (authorDAO.update(existing)) {
            view.printSuccess("Author updated successfully.");
        } else {
            view.printError("Failed to update author.");
        }
    }

    public void deleteAuthor() {

        view.printHeader("Delete Author");

        int id = ConsoleUtil.readInt("Enter author ID: ");

        Author existing = authorDAO.findById(id);

        if (existing == null) {
            view.printWarning("Author not found.");
            return;
        }

        if (authorDAO.delete(id)) {
            view.printSuccess("Author deleted successfully.");
        } else {
            view.printError("Failed to delete author.");
        }
    }
}
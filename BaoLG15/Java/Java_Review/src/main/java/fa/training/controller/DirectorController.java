package fa.training.controller;

import fa.training.dao.DirectorDAO;
import fa.training.dao.impl.DirectorDAOImpl;
import fa.training.entities.Director;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;
import fa.training.view.DirectorView;

public class DirectorController {
    private final DirectorDAO directorDAO = new DirectorDAOImpl();
    private final DirectorView view = new DirectorView();

    public DirectorView getView() {
        return view;
    }

    private boolean isDuplicateName(String name, Integer excludeId) {
        return directorDAO.getAll().stream()
                .anyMatch(d -> d.getName().equalsIgnoreCase(name.trim()) && 
                               (excludeId == null || d.getId() != excludeId));
    }

    public void addDirector() {
        String name;
        while (true) {
            name = ConsoleUtil.readString("Enter director name: ");
            if (!Validator.isValidName(name)) {
                view.printWarning("Name cannot be empty.");
            } else if (isDuplicateName(name, null)) {
                view.printWarning("Director name already exists. Please try again.");
            } else {
                break;
            }
        }

        Director newDirector = new Director(name);
        if (directorDAO.add(newDirector)) {
            view.printSuccess("Director added with ID: " + newDirector.getId());
        } else {
            view.printError("Failed to add director.");
        }
    }

    public void displayAllDirectors() {
        var directors = directorDAO.getAll();
        view.displayDirectors(directors);
    }

    public void updateDirector() {
        view.printHeader("Update Director");
        int id = ConsoleUtil.readInt("Enter director ID to update: ");
        var existing = directorDAO.findById(id);
        if (existing == null) {
            view.printWarning("Director not found.");
            return;
        }
        System.out.println("Current name: " + existing.getName());

        String name;
        while (true) {
            name = ConsoleUtil.readString("Enter new name [%s]: ".formatted(existing.getName()));
            if (name.isBlank()) {
                name = existing.getName();
            }
            if (!Validator.isValidName(name)) {
                view.printWarning("Name cannot be empty.");
            } else if (!name.equalsIgnoreCase(existing.getName()) && isDuplicateName(name, existing.getId())) {
                view.printWarning("Director name already exists. Please try again.");
            } else {
                break;
            }
        }

        existing.setName(name);
        if (directorDAO.update(existing)) {
            view.printSuccess("Director updated successfully!");
        } else {
            view.printError("Failed to update director.");
        }
    }

    public void deleteDirector() {
        view.printHeader("Delete Director");
        int id = ConsoleUtil.readInt("Enter director ID to delete: ");
        var existing = directorDAO.findById(id);
        if (existing == null) {
            view.printWarning("Director not found.");
            return;
        }
        System.out.println("Deleting: " + existing);
        if (directorDAO.delete(id)) {
            view.printSuccess("Director deleted successfully!");
        } else {
            view.printError("Failed to delete director.");
        }
    }

    public void searchDirectorByName() {
        view.printHeader("Search Director by Name");
        var keyword = ConsoleUtil.readString("Enter name keyword: ");
        var directors = directorDAO.findByName(keyword);
        view.displayDirectorsSearch(directors, keyword);
    }

    public Director selectOrCreateDirector(boolean allowSkip) {
        var directors = directorDAO.getAll();
        if (!directors.isEmpty()) {
            System.out.println("Available directors:");
            directors.forEach(d -> System.out.printf("  %d - %s%n", d.getId(), d.getName()));
        }
        if (allowSkip) {
            System.out.println("Enter director ID to select, 0 to create new (press Enter to keep current):");
        } else {
            System.out.println("Enter director ID to select, or 0 to create new:");
        }

        while (true) {
            String input = ConsoleUtil.readString("> ");
            if (allowSkip && input.isBlank()) {
                return null;
            }

            int dirId = ConsoleUtil.parseIntOrDefault(input, -1);
            if (dirId == -1) {
                view.printWarning("Invalid input. Please enter a valid numeric ID.");
                continue;
            }

            if (dirId == 0) {
                String name;
                while (true) {
                    name = ConsoleUtil.readString("Enter new director name: ");
                    if (!Validator.isValidName(name)) {
                        view.printWarning("Name cannot be empty.");
                    } else if (isDuplicateName(name, null)) {
                        view.printWarning("Director name already exists. Please try again.");
                    } else {
                        break;
                    }
                }
                Director newDirector = new Director(name);
                if (directorDAO.add(newDirector))
                    return newDirector;
                view.printError("Failed to create director. Try again.");
                continue;
            }
            var found = directorDAO.findById(dirId);
            if (found != null) {
                return found;
            }
            view.printWarning("Director not found. Please try again.");
        }
    }
}

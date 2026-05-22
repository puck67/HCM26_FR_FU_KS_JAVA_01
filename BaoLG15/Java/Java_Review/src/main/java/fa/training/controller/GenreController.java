package fa.training.controller;

import fa.training.dao.GenreDAO;
import fa.training.dao.impl.GenreDAOImpl;
import fa.training.entities.Genre;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;
import fa.training.view.GenreView;

public class GenreController {
    private final GenreDAO genreDAO = new GenreDAOImpl();
    private final GenreView view = new GenreView();

    public GenreView getView() {
        return view;
    }

    private boolean isDuplicateName(String name, Integer excludeId) {
        return genreDAO.getAll().stream()
                .anyMatch(g -> g.getName().equalsIgnoreCase(name.trim()) && 
                               (excludeId == null || g.getId() != excludeId));
    }

    public void addGenre() {
        String name;
        while (true) {
            name = ConsoleUtil.readString("Enter genre name: ");
            if (!Validator.isValidName(name)) {
                view.printWarning("Name cannot be empty.");
            } else if (isDuplicateName(name, null)) {
                view.printWarning("Genre name already exists. Please try again.");
            } else {
                break;
            }
        }

        Genre newGenre = new Genre(name);
        if (genreDAO.add(newGenre)) {
            view.printSuccess("Genre added with ID: " + newGenre.getId());
        } else {
            view.printError("Failed to add genre.");
        }
    }

    public void displayAllGenres() {
        var genres = genreDAO.getAll();
        view.displayGenres(genres);
    }

    public void updateGenre() {
        view.printHeader("Update Genre");
        int id = ConsoleUtil.readInt("Enter genre ID to update: ");
        var existing = genreDAO.findById(id);
        if (existing == null) {
            view.printWarning("Genre not found.");
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
                view.printWarning("Genre name already exists. Please try again.");
            } else {
                break;
            }
        }

        existing.setName(name);
        if (genreDAO.update(existing)) {
            view.printSuccess("Genre updated successfully!");
        } else {
            view.printError("Failed to update genre.");
        }
    }

    public void deleteGenre() {
        view.printHeader("Delete Genre");
        int id = ConsoleUtil.readInt("Enter genre ID to delete: ");
        var existing = genreDAO.findById(id);
        if (existing == null) {
            view.printWarning("Genre not found.");
            return;
        }
        System.out.println("Deleting: " + existing);
        if (genreDAO.delete(id)) {
            view.printSuccess("Genre deleted successfully!");
        } else {
            view.printError("Failed to delete genre.");
        }
    }

    public void searchGenreByName() {
        view.printHeader("Search Genre by Name");
        var keyword = ConsoleUtil.readString("Enter name keyword: ");
        var genres = genreDAO.findByName(keyword);
        view.displayGenresSearch(genres, keyword);
    }

    public Genre selectOrCreateGenre(boolean allowSkip) {
        var genres = genreDAO.getAll();
        if (!genres.isEmpty()) {
            System.out.println("Available genres:");
            genres.forEach(g -> System.out.printf("  %d - %s%n", g.getId(), g.getName()));
        }
        if (allowSkip) {
            System.out.println("Enter genre ID to select, 0 to create new (press Enter to keep current):");
        } else {
            System.out.println("Enter genre ID to select, or 0 to create new:");
        }

        while (true) {
            String input = ConsoleUtil.readString("> ");
            if (allowSkip && input.isBlank()) {
                return null;
            }

            int genId = ConsoleUtil.parseIntOrDefault(input, -1);
            if (genId == -1) {
                view.printWarning("Invalid input. Please enter a valid numeric ID.");
                continue;
            }

            if (genId == 0) {
                String name;
                while (true) {
                    name = ConsoleUtil.readString("Enter new genre name: ");
                    if (!Validator.isValidName(name)) {
                        view.printWarning("Name cannot be empty.");
                    } else if (isDuplicateName(name, null)) {
                        view.printWarning("Genre name already exists. Please try again.");
                    } else {
                        break;
                    }
                }
                Genre newGenre = new Genre(name);
                if (genreDAO.add(newGenre))
                    return newGenre;
                view.printError("Failed to create genre. Try again.");
                continue;
            }
            var found = genreDAO.findById(genId);
            if (found != null) {
                return found;
            }
            view.printWarning("Genre not found. Please try again.");
        }
    }
}

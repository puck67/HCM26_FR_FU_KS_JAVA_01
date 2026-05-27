package fa.training.controller;

import fa.training.dao.MovieDAO;
import fa.training.dao.impl.MovieDAOImpl;
import fa.training.entities.Movie;
import fa.training.util.ConsoleUtil;
import fa.training.util.Validator;
import fa.training.view.MovieView;

public class MovieController {
    private final MovieDAO movieDAO = new MovieDAOImpl();
    private final MovieView view = new MovieView();
    private final DirectorController directorController;
    private final GenreController genreController;

    public MovieController(DirectorController directorController, GenreController genreController) {
        this.directorController = directorController;
        this.genreController = genreController;
    }

    public void addMovie() {
        view.printHeader("Add New Movie");

        String id = generateNextMovieId();
        System.out.println("Generated Movie ID: " + id);

        String title;
        do {
            title = ConsoleUtil.readString("Enter movie title: ");
            if (!Validator.isValidTitle(title)) {
                view.printWarning("Title cannot be empty.");
            }
        } while (!Validator.isValidTitle(title));

        var director = directorController.selectOrCreateDirector(false);
        var genre = genreController.selectOrCreateGenre(false);

        int year;
        do {
            year = ConsoleUtil.readInt("Enter release year (1888-%d): ".formatted(java.time.Year.now().getValue() + 5));
            if (!Validator.isValidReleaseYear(year)) {
                view.printWarning("Invalid release year.");
            }
        } while (!Validator.isValidReleaseYear(year));

        double rating;
        do {
            rating = ConsoleUtil.readDouble("Enter rating (0.0 - 10.0): ");
            if (!Validator.isValidRating(rating)) {
                view.printWarning("Rating must be between 0.0 and 10.0.");
            }
        } while (!Validator.isValidRating(rating));

        var movie = new Movie(id, title, director, genre, year, rating);
        if (movieDAO.add(movie)) {
            view.printSuccess("Movie added successfully!");
        } else {
            view.printError("Failed to add movie.");
        }
    }

    public void displayAllMovies() {
        var movies = movieDAO.getAll();
        view.displayAllMovies(movies);
    }

    public void updateMovie() {
        view.printHeader("Update Movie");
        var id = ConsoleUtil.readString("Enter movie ID to update: ");
        var existing = movieDAO.findById(id);
        if (existing == null) {
            view.printWarning("Movie not found.");
            return;
        }
        System.out.println("Current: " + existing);

        String title;
        do {
            title = ConsoleUtil.readString("Enter new title [%s]: ".formatted(existing.getTitle()));
            if (title.isBlank())
                title = existing.getTitle();
        } while (!Validator.isValidTitle(title));

        System.out.println("Current director: " + existing.getDirector().getName());
        var director = directorController.selectOrCreateDirector(true);
        if (director == null)
            director = existing.getDirector();

        System.out.println("Current genre: " + existing.getGenre().getName());
        var genre = genreController.selectOrCreateGenre(true);
        if (genre == null)
            genre = existing.getGenre();

        int year;
        do {
            var input = ConsoleUtil.readString("Enter new release year [%d]: ".formatted(existing.getReleaseYear()));
            year = input.isBlank() ? existing.getReleaseYear() : ConsoleUtil.parseIntOrDefault(input, -1);
            if (!Validator.isValidReleaseYear(year)) {
                view.printWarning("Invalid release year.");
            }
        } while (!Validator.isValidReleaseYear(year));

        double rating;
        do {
            var input = ConsoleUtil.readString("Enter new rating [%.1f]: ".formatted(existing.getRating()));
            rating = input.isBlank() ? existing.getRating() : ConsoleUtil.parseDoubleOrDefault(input, -1);
            if (!Validator.isValidRating(rating)) {
                view.printWarning("Rating must be between 0.0 and 10.0.");
            }
        } while (!Validator.isValidRating(rating));

        var movie = new Movie(id, title, director, genre, year, rating);
        if (movieDAO.update(movie)) {
            view.printSuccess("Movie updated successfully!");
        } else {
            view.printError("Failed to update movie.");
        }
    }

    public void deleteMovie() {
        view.printHeader("Delete Movie");
        var id = ConsoleUtil.readString("Enter movie ID to delete: ");
        var existing = movieDAO.findById(id);
        if (existing == null) {
            view.printWarning("Movie not found.");
            return;
        }
        System.out.println("Deleting: " + existing);
        if (movieDAO.delete(id)) {
            view.printSuccess("Movie deleted successfully!");
        } else {
            view.printError("Failed to delete movie.");
        }
    }

    public void searchMovieById() {
        view.printHeader("Search Movie by ID");
        var id = ConsoleUtil.readString("Enter movie ID: ");
        var movie = movieDAO.findById(id);
        view.displayMovie(movie);
    }

    public void searchMovieByTitle() {
        view.printHeader("Search Movies by Title");
        var keyword = ConsoleUtil.readString("Enter title keyword: ");
        var movies = movieDAO.findByTitle(keyword);
        view.displayMoviesSearch(movies, keyword);
    }

    private String generateNextMovieId() {
        var movies = movieDAO.getAll();
        int maxId = 0;
        for (Movie movie : movies) {
            String idStr = movie.getId();
            if (idStr != null && idStr.startsWith("MOV")) {
                try {
                    int idNum = Integer.parseInt(idStr.substring(3));
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Number format error!");
                }
            }
        }
        return String.format("MOV%03d", maxId + 1);
    }
}

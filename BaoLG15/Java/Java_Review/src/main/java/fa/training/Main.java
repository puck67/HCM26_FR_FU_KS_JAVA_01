package fa.training;

import fa.training.controller.DirectorController;
import fa.training.controller.GenreController;
import fa.training.controller.MovieController;
import fa.training.util.ConsoleUtil;
import fa.training.view.MainView;

public class Main {
    public static void main(String[] args) {
        MainView mainView = new MainView();
        DirectorController directorController = new DirectorController();
        GenreController genreController = new GenreController();
        MovieController movieController = new MovieController(directorController, genreController);

        boolean running = true;
        while (running) {
            mainView.printMainMenu();
            boolean validChoice = false;
            while (!validChoice) {
                int choice = ConsoleUtil.readInt("Enter your choice: ");
                validChoice = true;
                running = switch (choice) {
                    case 1 -> {
                        movieController.addMovie();
                        yield true;
                    }
                    case 2 -> {
                        movieController.displayAllMovies();
                        yield true;
                    }
                    case 3 -> {
                        movieController.updateMovie();
                        yield true;
                    }
                    case 4 -> {
                        movieController.deleteMovie();
                        yield true;
                    }
                    case 5 -> {
                        movieController.searchMovieById();
                        yield true;
                    }
                    case 6 -> {
                        movieController.searchMovieByTitle();
                        yield true;
                    }
                    case 7 -> {
                        manageDirectors(directorController);
                        yield true;
                    }
                    case 8 -> {
                        manageGenres(genreController);
                        yield true;
                    }
                    case 9 -> {
                        mainView.printGoodbye();
                        yield false;
                    }
                    default -> {
                        mainView.printInvalidChoice();
                        validChoice = false;
                        yield true;
                    }
                };
            }
        }
        ConsoleUtil.close();
    }

    private static void manageDirectors(DirectorController directorController) {
        boolean back = false;
        while (!back) {
            directorController.getView().printMenu();
            boolean validChoice = false;
            while (!validChoice) {
                int choice = ConsoleUtil.readInt("Enter your choice: ");
                validChoice = true;
                back = switch (choice) {
                    case 1 -> {
                        directorController.addDirector();
                        yield false;
                    }
                    case 2 -> {
                        directorController.displayAllDirectors();
                        yield false;
                    }
                    case 3 -> {
                        directorController.updateDirector();
                        yield false;
                    }
                    case 4 -> {
                        directorController.deleteDirector();
                        yield false;
                    }
                    case 5 -> {
                        directorController.searchDirectorByName();
                        yield false;
                    }
                    case 6 -> true;
                    default -> {
                        directorController.getView().printInvalidChoice();
                        validChoice = false;
                        yield false;
                    }
                };
            }
        }
    }

    private static void manageGenres(GenreController genreController) {
        boolean back = false;
        while (!back) {
            genreController.getView().printMenu();
            boolean validChoice = false;
            while (!validChoice) {
                int choice = ConsoleUtil.readInt("Enter your choice: ");
                validChoice = true;
                back = switch (choice) {
                    case 1 -> {
                        genreController.addGenre();
                        yield false;
                    }
                    case 2 -> {
                        genreController.displayAllGenres();
                        yield false;
                    }
                    case 3 -> {
                        genreController.updateGenre();
                        yield false;
                    }
                    case 4 -> {
                        genreController.deleteGenre();
                        yield false;
                    }
                    case 5 -> {
                        genreController.searchGenreByName();
                        yield false;
                    }
                    case 6 -> true;
                    default -> {
                        genreController.getView().printInvalidChoice();
                        validChoice = false;
                        yield false;
                    }
                };
            }
        }
    }
}
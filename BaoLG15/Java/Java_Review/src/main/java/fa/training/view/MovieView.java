package fa.training.view;

import fa.training.entities.Movie;
import fa.training.util.ConsoleUtil;

import java.util.List;

public class MovieView {
    public void displayAllMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("\nNo movies found.");
            return;
        }
        ConsoleUtil.printMovieTable(movies);
    }
    
    public void displayMoviesSearch(List<Movie> movies, String keyword) {
        if (movies.isEmpty()) {
            System.out.printf("  [!] No movies found matching '%s'.%n", keyword);
        } else {
            System.out.printf("Found %d movie(s):%n", movies.size());
            ConsoleUtil.printMovieTable(movies);
        }
    }
    
    public void displayMovie(Movie movie) {
        if (movie == null) {
            printWarning("Movie not found.");
        } else {
            ConsoleUtil.printMovieTable(List.of(movie));
        }
    }
    
    public void printHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }
    
    public void printSuccess(String message) {
        System.out.println("  [+] " + message);
    }
    
    public void printError(String message) {
        System.out.println("  [x] " + message);
    }
    
    public void printWarning(String message) {
        System.out.println("  [!] " + message);
    }
}

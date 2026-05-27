package fa.training.entities;

import fa.training.util.Validator;

public class Movie {

    private String id;
    private String title;
    private Director director;
    private Genre genre;
    private int releaseYear;
    private double rating;

    public Movie() {
    }

    public Movie(String id, String title, Director director, Genre genre,
                 int releaseYear, double rating) {
        setId(id);
        setTitle(title);
        setDirector(director);
        setGenre(genre);
        setReleaseYear(releaseYear);
        setRating(rating);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (!Validator.isValidId(id)) {
            throw new IllegalArgumentException("Invalid movie ID");
        }
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (!Validator.isValidTitle(title)) {
            throw new IllegalArgumentException("Invalid movie title");
        }
        this.title = title;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        if (!Validator.isValidReleaseYear(releaseYear)) {
            throw new IllegalArgumentException("Invalid release year");
        }
        this.releaseYear = releaseYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (!Validator.isValidRating(rating)) {
            throw new IllegalArgumentException("Invalid rating");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return """
                Movie{id='%s', title='%s', director='%s', genre='%s', \
                releaseYear=%d, rating=%.1f}\
                """.formatted(
                id, title,
                director != null ? director.getName() : "N/A",
                genre != null ? genre.getName() : "N/A",
                releaseYear, rating
        );
    }

    public String toDisplayString() {
        return """
                | %-10s | %-30s | %-20s | %-15s | %-6d | %-6.1f |""".formatted(
                id, title,
                director != null ? director.getName() : "N/A",
                genre != null ? genre.getName() : "N/A",
                releaseYear, rating
        );
    }
}

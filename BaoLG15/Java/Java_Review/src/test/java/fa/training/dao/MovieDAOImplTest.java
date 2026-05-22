package fa.training.dao;

import fa.training.dao.impl.DirectorDAOImpl;
import fa.training.dao.impl.GenreDAOImpl;
import fa.training.dao.impl.MovieDAOImpl;
import fa.training.entities.Director;
import fa.training.entities.Genre;
import fa.training.entities.Movie;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovieDAOImplTest {

    private static final MovieDAO movieDAO = new MovieDAOImpl();
    private static final DirectorDAO directorDAO = new DirectorDAOImpl();
    private static final GenreDAO genreDAO = new GenreDAOImpl();

    private static final String TEST_MOVIE_ID = "TEST001";
    private static int testDirectorId;
    private static int testGenreId;

    @BeforeAll
    static void setUp() {
        Director dir = new Director("Test Director");
        assertTrue(directorDAO.add(dir), "Test director should be created");
        testDirectorId = dir.getId();

        Genre gen = new Genre("TestGenre");
        assertTrue(genreDAO.add(gen), "Test genre should be created");
        testGenreId = gen.getId();
    }

    @AfterAll
    static void tearDown() {
        movieDAO.delete(TEST_MOVIE_ID);
        movieDAO.delete("TEST002");
    }

    @Test
    @Order(1)
    @DisplayName("Test insert a new movie record")
    void testInsertMovie() {
        var director = new Director(testDirectorId, "Test Director");
        var genre = new Genre(testGenreId, "TestGenre");
        var movie = new Movie(TEST_MOVIE_ID, "Test Movie Title", director, genre, 2024, 8.5);

        boolean result = movieDAO.add(movie);

        assertTrue(result, "Insert should return true");
    }

    @Test
    @Order(2)
    @DisplayName("Test find movie by ID returns correct record")
    void testFindById() {
        var movie = movieDAO.findById(TEST_MOVIE_ID);

        assertNotNull(movie, "Movie should be found");
        assertEquals(TEST_MOVIE_ID, movie.getId());
        assertEquals("Test Movie Title", movie.getTitle());
        assertEquals(2024, movie.getReleaseYear());
        assertEquals(8.5, movie.getRating(), 0.01);
        assertNotNull(movie.getDirector(), "Director should not be null");
        assertNotNull(movie.getGenre(), "Genre should not be null");
    }

    @Test
    @Order(3)
    @DisplayName("Test update an existing movie record")
    void testUpdateMovie() {
        var director = new Director(testDirectorId, "Test Director");
        var genre = new Genre(testGenreId, "TestGenre");
        var movie = new Movie(TEST_MOVIE_ID, "Updated Title", director, genre, 2025, 9.0);

        boolean result = movieDAO.update(movie);
        assertTrue(result, "Update should return true");

        var updated = movieDAO.findById(TEST_MOVIE_ID);
        assertNotNull(updated);
        assertEquals("Updated Title", updated.getTitle());
        assertEquals(2025, updated.getReleaseYear());
        assertEquals(9.0, updated.getRating(), 0.01);
    }

    @Test
    @Order(4)
    @DisplayName("Test get all movies returns non-empty list")
    void testGetAllMovies() {
        var movies = movieDAO.getAll();

        assertNotNull(movies, "List should not be null");
        assertFalse(movies.isEmpty(), "Should have at least one movie");

        boolean found = movies.stream()
                .anyMatch(m -> TEST_MOVIE_ID.equals(m.getId()));
        assertTrue(found, "Test movie should be in the list");
    }

    @Test
    @Order(5)
    @DisplayName("Test delete a movie record")
    void testDeleteMovie() {
        var director = new Director(testDirectorId, "Test Director");
        var genre = new Genre(testGenreId, "TestGenre");
        movieDAO.add(new Movie("TEST002", "To Delete", director, genre, 2020, 5.0));

        boolean result = movieDAO.delete("TEST002");
        assertTrue(result, "Delete should return true");

        var deleted = movieDAO.findById("TEST002");
        assertNull(deleted, "Deleted movie should not be found");
    }

    @Test
    @Order(6)
    @DisplayName("Test find non-existent movie returns null")
    void testFindByIdNotFound() {
        var movie = movieDAO.findById("NONEXIST");
        assertNull(movie, "Non-existent movie should return null");
    }
}

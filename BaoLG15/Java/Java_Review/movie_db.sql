DROP FUNCTION IF EXISTS find_movies_by_title(VARCHAR);
DROP FUNCTION IF EXISTS find_movie_by_id(VARCHAR);
DROP FUNCTION IF EXISTS get_all_movies();
DROP FUNCTION IF EXISTS count_movies();
DROP PROCEDURE IF EXISTS insert_movie(VARCHAR, VARCHAR, INT, INT, INT, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS update_movie(VARCHAR, VARCHAR, INT, INT, INT, DOUBLE PRECISION);
DROP PROCEDURE IF EXISTS delete_movie(VARCHAR);

DROP FUNCTION IF EXISTS find_genre_by_id(INT);
DROP FUNCTION IF EXISTS find_genre_by_name(VARCHAR);
DROP FUNCTION IF EXISTS get_all_genres();
DROP FUNCTION IF EXISTS insert_genre(VARCHAR);
DROP PROCEDURE IF EXISTS update_genre(INT, VARCHAR);
DROP PROCEDURE IF EXISTS delete_genre(INT);

DROP FUNCTION IF EXISTS find_director_by_id(INT);
DROP FUNCTION IF EXISTS find_director_by_name(VARCHAR);
DROP FUNCTION IF EXISTS get_all_directors();
DROP FUNCTION IF EXISTS insert_director(VARCHAR);
DROP PROCEDURE IF EXISTS update_director(INT, VARCHAR);
DROP PROCEDURE IF EXISTS delete_director(INT);

DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS genres CASCADE;

CREATE TABLE directors (
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL
);

CREATE TABLE genres (
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE movies (
    id            VARCHAR(10) PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    director_id   INT REFERENCES directors(id) ON DELETE SET NULL,
    genre_id      INT REFERENCES genres(id) ON DELETE SET NULL,
    release_year  INT,
    rating        DOUBLE PRECISION
);

CREATE OR REPLACE FUNCTION insert_director(p_name VARCHAR)
RETURNS INT
LANGUAGE plpgsql AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO directors(name) VALUES(p_name) RETURNING id INTO v_id;
    RETURN v_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_directors()
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT d.id, d.name FROM directors d ORDER BY d.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_director_by_id(p_id INT)
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT d.id, d.name FROM directors d WHERE d.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION find_director_by_name(p_name VARCHAR)
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT d.id, d.name FROM directors d
    WHERE LOWER(d.name) LIKE LOWER('%' || p_name || '%')
    ORDER BY d.id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_director(p_id INT, p_name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE directors SET name = p_name WHERE directors.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_director(p_id INT)
LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM directors WHERE directors.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION insert_genre(p_name VARCHAR)
RETURNS INT
LANGUAGE plpgsql AS $$
DECLARE
    v_id INT;
BEGIN
    INSERT INTO genres(name) VALUES(p_name) RETURNING id INTO v_id;
    RETURN v_id;
END;
$$;

CREATE OR REPLACE FUNCTION get_all_genres()
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT g.id, g.name FROM genres g ORDER BY g.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_genre_by_id(p_id INT)
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT g.id, g.name FROM genres g WHERE g.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION find_genre_by_name(p_name VARCHAR)
RETURNS TABLE(id INT, name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY SELECT g.id, g.name FROM genres g
    WHERE LOWER(g.name) LIKE LOWER('%' || p_name || '%')
    ORDER BY g.id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_genre(p_id INT, p_name VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE genres SET name = p_name WHERE genres.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_genre(p_id INT)
LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM genres WHERE genres.id = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE insert_movie(
    p_id           VARCHAR,
    p_title        VARCHAR,
    p_director_id  INT,
    p_genre_id     INT,
    p_release_year INT,
    p_rating       DOUBLE PRECISION
)
LANGUAGE plpgsql AS $$
BEGIN
    INSERT INTO movies(id, title, director_id, genre_id, release_year, rating)
    VALUES(p_id, p_title, p_director_id, p_genre_id, p_release_year, p_rating);
END;
$$;

CREATE OR REPLACE FUNCTION get_all_movies()
RETURNS TABLE(
    id            VARCHAR,
    title         VARCHAR,
    director_id   INT,
    director_name VARCHAR,
    genre_id      INT,
    genre_name    VARCHAR,
    release_year  INT,
    rating        DOUBLE PRECISION
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT m.id, m.title,
           m.director_id, d.name AS director_name,
           m.genre_id, g.name AS genre_name,
           m.release_year, m.rating
    FROM movies m
    LEFT JOIN directors d ON m.director_id = d.id
    LEFT JOIN genres g ON m.genre_id = g.id
    ORDER BY m.id;
END;
$$;

CREATE OR REPLACE FUNCTION find_movie_by_id(p_id VARCHAR)
RETURNS TABLE(
    id            VARCHAR,
    title         VARCHAR,
    director_id   INT,
    director_name VARCHAR,
    genre_id      INT,
    genre_name    VARCHAR,
    release_year  INT,
    rating        DOUBLE PRECISION
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT m.id, m.title,
           m.director_id, d.name AS director_name,
           m.genre_id, g.name AS genre_name,
           m.release_year, m.rating
    FROM movies m
    LEFT JOIN directors d ON m.director_id = d.id
    LEFT JOIN genres g ON m.genre_id = g.id
    WHERE m.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION find_movies_by_title(p_title VARCHAR)
RETURNS TABLE(
    id            VARCHAR,
    title         VARCHAR,
    director_id   INT,
    director_name VARCHAR,
    genre_id      INT,
    genre_name    VARCHAR,
    release_year  INT,
    rating        DOUBLE PRECISION
)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT m.id, m.title,
           m.director_id, d.name AS director_name,
           m.genre_id, g.name AS genre_name,
           m.release_year, m.rating
    FROM movies m
    LEFT JOIN directors d ON m.director_id = d.id
    LEFT JOIN genres g ON m.genre_id = g.id
    WHERE LOWER(m.title) LIKE LOWER('%' || p_title || '%')
    ORDER BY m.id;
END;
$$;

CREATE OR REPLACE PROCEDURE update_movie(
    p_id           VARCHAR,
    p_title        VARCHAR,
    p_director_id  INT,
    p_genre_id     INT,
    p_release_year INT,
    p_rating       DOUBLE PRECISION
)
LANGUAGE plpgsql AS $$
BEGIN
    UPDATE movies
    SET title        = p_title,
        director_id  = p_director_id,
        genre_id     = p_genre_id,
        release_year = p_release_year,
        rating       = p_rating
    WHERE movies.id  = p_id;
END;
$$;

CREATE OR REPLACE PROCEDURE delete_movie(p_id VARCHAR)
LANGUAGE plpgsql AS $$
BEGIN
    DELETE FROM movies WHERE movies.id = p_id;
END;
$$;

CREATE OR REPLACE FUNCTION count_movies()
RETURNS INT
LANGUAGE plpgsql AS $$
DECLARE
    v_count INT;
BEGIN
    SELECT COUNT(*) INTO v_count FROM movies;
    RETURN v_count;
END;
$$;


SELECT insert_director('Christopher Nolan');
SELECT insert_director('Quentin Tarantino');
SELECT insert_director('Steven Spielberg');

SELECT insert_genre('Action');
SELECT insert_genre('Drama');
SELECT insert_genre('Sci-Fi');
SELECT insert_genre('Comedy');
SELECT insert_genre('Horror');

CALL insert_movie('MOV001', 'Inception', 1, 3, 2010, 8.8);
CALL insert_movie('MOV002', 'The Dark Knight', 1, 1, 2008, 9.0);
CALL insert_movie('MOV003', 'Pulp Fiction', 2, 2, 1994, 8.9);
CALL insert_movie('MOV004', 'Schindlers List', 3, 2, 1993, 9.0);
CALL insert_movie('MOV005', 'Interstellar', 1, 3, 2014, 8.7);

SELECT routine_name, routine_type
FROM information_schema.routines
WHERE routine_schema = 'public'
ORDER BY routine_type, routine_name;

SELECT * FROM get_all_directors();
SELECT * FROM get_all_genres();
SELECT * FROM get_all_movies();
SELECT count_movies();

CREATE DATABASE IF NOT EXISTS song_db;
USE song_db;

CREATE TABLE IF NOT EXISTS authurs (
    authur_id VARCHAR(10) PRIMARY KEY,
    authur_name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS songs (
    id VARCHAR(10) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    authur_id VARCHAR(10),
    type VARCHAR(50),
    duration INT,
    release_date DATE,
    FOREIGN KEY (authur_id) REFERENCES authurs(authur_id) ON DELETE SET NULL
);

DELIMITER //

CREATE PROCEDURE insert_song(
    IN p_id VARCHAR(10),
    IN p_title VARCHAR(100),
    IN p_author_id VARCHAR(10),
    IN p_author_name VARCHAR(100),
    IN p_type VARCHAR(50),
    IN p_duration INT,
    IN p_release_date DATE
)
BEGIN
    INSERT INTO authurs (authur_id, authur_name)
    VALUES (p_author_id, p_author_name)
    ON DUPLICATE KEY UPDATE authur_name = p_author_name;

    INSERT INTO songs (id, title, authur_id, type, duration, release_date)
    VALUES (p_id, p_title, p_author_id, p_type, p_duration, p_release_date);
END //

CREATE PROCEDURE get_all_songs()
BEGIN
    SELECT s.id, s.title, s.type, s.duration, s.release_date, a.authur_id, a.authur_name
    FROM songs s
    JOIN authurs a ON s.authur_id = a.authur_id;
END //

CREATE PROCEDURE update_song(
    IN p_id VARCHAR(10),
    IN p_title VARCHAR(100),
    IN p_author_id VARCHAR(10),
    IN p_author_name VARCHAR(100),
    IN p_type VARCHAR(50),
    IN p_duration INT,
    IN p_release_date DATE
)
BEGIN
    INSERT INTO authurs (authur_id, authur_name)
    VALUES (p_author_id, p_author_name)
    ON DUPLICATE KEY UPDATE authur_name = p_author_name;

    UPDATE songs
    SET title = p_title,
        authur_id = p_author_id,
        type = p_type,
        duration = p_duration,
        release_date = p_release_date
    WHERE id = p_id;
END //

CREATE PROCEDURE delete_song(
    IN p_id VARCHAR(10)
)
BEGIN
    DELETE FROM songs WHERE id = p_id;
END //

CREATE PROCEDURE get_song_by_id(
    IN p_id VARCHAR(10)
)
BEGIN
    SELECT s.id, s.title, s.type, s.duration, s.release_date, a.authur_id, a.authur_name
    FROM songs s
    JOIN authurs a ON s.authur_id = a.authur_id
    WHERE s.id = p_id;
END //

DELIMITER ;

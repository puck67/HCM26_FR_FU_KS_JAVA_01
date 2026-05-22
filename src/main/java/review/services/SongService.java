package review.services;

import review.dao.SongDAO;
import review.entities.Authur;
import review.entities.Song;
import review.utils.Validations;
import java.util.List;

public class SongService {
    private final SongDAO songDAO = new SongDAO();

    public void addSong() {
        System.out.println("\n--- ADD NEW SONG ---");
        List<Song> songs = songDAO.getAll();
        String id = Validations.getValidSongId("Song ID: ", songs, false, "");
        String title = Validations.getValidString("Song name: ");
        
        List<Authur> authors = songDAO.getAllAuthors();
        String authorId = Validations.getValidAuthorId("Authur ID: ", authors, false, "");
        String authorName = Validations.getValidString("Authur name: ");

        String type = Validations.getValidGenre("Song type: ");
        int duration = Validations.getValidDuration("Duration (second): ");
        java.time.LocalDate releaseDate = Validations.getValidDate("Release date (dd/MM/yyyy): ");

        Authur author = new Authur(authorId, authorName);
        Song song = new Song(author, duration, id, title, type, releaseDate);
        
        if (songDAO.add(song)) {
            System.out.println("Add song successfully!");
        } else {
            System.out.println("Failed to add song!");
        }
    }

    public void displayAllSongs() {
        System.out.println("\n--- SONG LIST ---");
        List<Song> songs = songDAO.getAll();
        if (songs.isEmpty()) {
            System.out.println("Still empty.");
            return;
        }
        for (Song song : songs) {
            System.out.println(song.toString());
        }
    }

    public void updateSong() {
        System.out.println("\n--- UPDATE SONG INFORMATION ---");
        String searchId = Validations.getValidString("Song ID to update: ");
        Song foundSong = songDAO.findById(searchId);

        if (foundSong == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find song with ID: ").append(searchId);
            System.out.println(sb.toString());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Song: ").append(foundSong);
        System.out.println(sb.toString());
        System.out.println("New information:");

        String newTitle = Validations.getValidString("Song name: ");
        
        List<Authur> authors = songDAO.getAllAuthors();
        String newAuthorId = Validations.getValidAuthorId("Authur ID: ", authors, true, foundSong.getAuthurName().getAuthurId());
        String newAuthorName = Validations.getValidString("Authur name: ");

        String newType = Validations.getValidGenre("Song type: ");
        int newDuration = Validations.getValidDuration("Duration (second): ");
        java.time.LocalDate newReleaseDate = Validations.getValidDate("Release date (dd/MM/yyyy): ");

        Song updatedSong = new Song(new Authur(newAuthorId, newAuthorName), newDuration, foundSong.getId(), newTitle, newType, newReleaseDate);
        
        if (songDAO.update(updatedSong)) {
            System.out.println("Update song successfully!");
        } else {
            System.out.println("Update song failed!");
        }
    }

    public void deleteSong() {
        System.out.println("\n--- DELETE SONG ---");
        String searchId = Validations.getValidString("Song ID to delete: ");
        Song foundSong = songDAO.findById(searchId);
        if (foundSong == null) {
            System.out.println("Song not found!");
            return;
        }
        if (songDAO.delete(searchId)) {
            System.out.println("Delete song successfully!");
        } else {
            System.out.println("Delete song failed!");
        }
    }

    public void searchSong() {
        System.out.println("\n--- SEARCH SONG BY ID ---");
        String searchId = Validations.getValidString("Song ID to search: ");
        Song song = songDAO.findById(searchId);
        if (song != null) {
            System.out.println(song);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Song not found with ID: ").append(searchId);
            System.out.println(sb.toString());
        }
    }
}

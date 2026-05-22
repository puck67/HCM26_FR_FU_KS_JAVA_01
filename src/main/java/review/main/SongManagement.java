package review.main;

import review.services.SongService;
import review.utils.Validations;

public class SongManagement {
    public static void main(String[] args) {
        SongService songService = new SongService();
        boolean running = true;

        while (running) {
            System.out.println("\n===== SONG MANAGEMENT =====");
            System.out.println("1. Add new record");
            System.out.println("2. Display all records");
            System.out.println("3. Update a record");
            System.out.println("4. Delete a record");
            System.out.println("5. Search record by ID");
            System.out.println("6. Exit");
            
            int choice = Validations.getMenuChoice("Enter choice: ");

            switch (choice) {
                case 1 -> songService.addSong();
                case 2 -> songService.displayAllSongs();
                case 3 -> songService.updateSong();
                case 4 -> songService.deleteSong();
                case 5 -> songService.searchSong();
                case 6 -> {
                    System.out.println("Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice! Please choose 1 to 6.");
            }
        }
    }
}

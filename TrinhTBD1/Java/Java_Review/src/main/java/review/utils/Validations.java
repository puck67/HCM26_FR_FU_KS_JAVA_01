package review.utils;

import review.entities.Song;
import review.entities.Authur;
import java.util.List;
import java.util.Scanner;

public class Validations {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getValidString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Cannot be empty!");
        }
    }

    public static String getValidSongId(String message, List<Song> songs, boolean isUpdate, String currentId) {
        while (true) {
            String id = getValidString(message).toUpperCase();

            if (!id.matches(Constants.SONG_ID_REGEX)) {
                System.out.println("Song ID must be Sxxx (Ex: S001, S002)!");
                continue;
            }

            if (isUpdate && id.equalsIgnoreCase(currentId)) {
                return id;
            }

            boolean isDuplicate = false;
            for (Song s : songs) {
                if (s.getId().equalsIgnoreCase(id)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                return id;
            }
            System.out.println("ID already exists!");
        }
    }

    public static String getValidAuthorId(String message, List<Authur> authors, boolean isUpdate, String currentAuthorId) {
        while (true) {
            String id = getValidString(message).toUpperCase();

            if (!id.matches(Constants.AUTHOR_ID_REGEX)) {
                System.out.println("Author ID must be Axxx (Ex: A001, A002)!");
                continue;
            }

            if (isUpdate && id.equalsIgnoreCase(currentAuthorId)) {
                return id;
            }

            boolean isDuplicate = false;
            for (Authur a : authors) {
                if (a.getAuthurId().equalsIgnoreCase(id)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (!isDuplicate) {
                return id;
            }
            System.out.println("ID already exists!");
        }
    }

    public static String getValidGenre(String message) {
        while (true) {
            String genre = getValidString(message);
            if (genre.matches(Constants.GENRE_REGEX)) {
                return genre;
            }
            System.out.println("Type of song must have character and space");
        }
    }

    public static int getValidDuration(String message) {
        while (true) {
            try {
                System.out.print(message);
                int duration = Integer.parseInt(scanner.nextLine().trim());
                if (duration > 0) {
                    return duration;
                }
                System.out.println("Duration must larger 0 second");
            } catch (NumberFormatException e) {
                System.out.println("Error! Pls enter integer.");
            }
        }
    }

    public static int getMenuChoice(String message) {
        try {
            System.out.print(message);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static java.time.LocalDate getValidDate(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (!input.matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                System.out.println("Invalid date format! Use dd/MM/yyyy.");
                continue;
            }
            try {
                String[] parts = input.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return java.time.LocalDate.of(year, month, day);
            } catch (java.time.DateTimeException e) {
                System.out.println(
                        "Invalid calendar date! Please check the day and month (e.g. Feb 31st does not exist).");
            }
        }
    }
}

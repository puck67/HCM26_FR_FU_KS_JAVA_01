package fa.training;

import fa.training.controller.MovieTheaterController;
import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;
import fa.training.view.Menu;
import org.hibernate.Session;
import java.time.LocalDate;
import java.util.Scanner;

public final class App {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
        System.setProperty("org.slf4j.simpleLogger.log.org.hibernate", "error");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        initializeSampleData();

        MovieTheaterController controller = new MovieTheaterController();
        Scanner scanner = new Scanner(System.in);

        Menu roomMenu = new Menu("Cinema Room Management");
        roomMenu.addItem("Add Cinema Room", () -> controller.addCinemaRoom(scanner));
        roomMenu.addItem("Display All Cinema Rooms", controller::listCinemaRooms);
        roomMenu.addItem("Update Cinema Room", () -> controller.updateCinemaRoom(scanner));
        roomMenu.addItem("Delete Cinema Room", () -> controller.deleteCinemaRoom(scanner));
        roomMenu.addItem("Search Cinema Room by ID", () -> controller.searchCinemaRoom(scanner));
        roomMenu.addItem("Back to Main Menu", roomMenu::exitMenu);

        Menu detailMenu = new Menu("Cinema Room Detail Management");
        detailMenu.addItem("Add Cinema Room Detail", () -> controller.addCinemaRoomDetail(scanner));
        detailMenu.addItem("Display All Cinema Room Details", controller::listCinemaRoomDetails);
        detailMenu.addItem("Update Cinema Room Detail", () -> controller.updateCinemaRoomDetail(scanner));
        detailMenu.addItem("Delete Cinema Room Detail", () -> controller.deleteCinemaRoomDetail(scanner));
        detailMenu.addItem("Search Cinema Room Detail by ID", () -> controller.searchCinemaRoomDetail(scanner));
        detailMenu.addItem("Back to Main Menu", detailMenu::exitMenu);

        Menu seatMenu = new Menu("Seat Management");
        seatMenu.addItem("Add Seat", () -> controller.addSeat(scanner));
        seatMenu.addItem("Display All Seats", controller::listSeats);
        seatMenu.addItem("Update Seat", () -> controller.updateSeat(scanner));
        seatMenu.addItem("Delete Seat", () -> controller.deleteSeat(scanner));
        seatMenu.addItem("Search Seat by ID", () -> controller.searchSeat(scanner));
        seatMenu.addItem("Back to Main Menu", seatMenu::exitMenu);

        Menu mainMenu = new Menu("Main Menu");
        mainMenu.addItem("Cinema Room Management", () -> roomMenu.show(scanner));
        mainMenu.addItem("Cinema Room Detail Management", () -> detailMenu.show(scanner));
        mainMenu.addItem("Seat Management", () -> seatMenu.show(scanner));
        mainMenu.addItem("Run Scenario Simulation", controller::runScenario);
        mainMenu.addItem("Exit Application", () -> {
            System.out.println("Exiting application.");
            scanner.close();
            HibernateUtil.shutdown();
            System.exit(0);
        });

        mainMenu.show(scanner);
    }

    private static void initializeSampleData() {
        RoomDao roomDao = new RoomDao();
        if (roomDao.getAllCinemaRooms().isEmpty()) {
            CinemaRoom room1 = new CinemaRoom("Room 01", 10);
            CinemaRoom room2 = new CinemaRoom("Room 02", 20);
            roomDao.insertCinemaRoom(room1);
            roomDao.insertCinemaRoom(room2);

            RoomDetailDao roomDetailDao = new RoomDetailDao();
            CinemaRoomDetail detail1 = new CinemaRoomDetail(room1, 80000, LocalDate.now(), "Standard room with Dolby Sound");
            CinemaRoomDetail detail2 = new CinemaRoomDetail(room2, 120000, LocalDate.now().plusDays(1), "VIP room with IMAX screen");
            roomDetailDao.insertCinemaRoomDetail(detail1);
            roomDetailDao.insertCinemaRoomDetail(detail2);

            SeatDao seatDao = new SeatDao();
            seatDao.insertSeat(new Seat(room1, "A", 1, "Available", "Normal"));
            seatDao.insertSeat(new Seat(room1, "A", 2, "Booked", "VIP"));
            seatDao.insertSeat(new Seat(room1, "B", 1, "Not Available", "Normal"));
            seatDao.insertSeat(new Seat(room2, "A", 1, "Available", "VIP"));
            seatDao.insertSeat(new Seat(room2, "A", 2, "Available", "VIP"));
        }
    }
}

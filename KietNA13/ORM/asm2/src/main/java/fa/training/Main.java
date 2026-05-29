package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final RoomDao roomDao = new RoomDao();
    private static final RoomDetailDao detailDao = new RoomDetailDao();
    private static final SeatDao seatDao = new SeatDao();

    public static void main(String[] args) {
        setupRoomsAndDetails();
        setupSeats();
        listAllRooms();
        upgradeRoomCapacity();
        bookSeat();
        listAllSeats();
        removeRoom();
        HibernateUtil.shutdown();
    }

    private static void setupRoomsAndDetails() {
        System.out.println("=== [1] Creating cinema rooms ===");

        CinemaRoom vipHall = new CinemaRoom("VIP Hall", 50);
        CinemaRoom standardHall = new CinemaRoom("Standard Hall", 100);
        roomDao.save(vipHall);
        roomDao.save(standardHall);

        System.out.println("Saved: " + vipHall);
        System.out.println("Saved: " + standardHall);

        System.out.println("\n=== [2] Adding room details ===");

        CinemaRoomDetail vipDetail = new CinemaRoomDetail(
                vipHall, 250, LocalDate.of(2025, 3, 1), "Premium VIP hall with recliner seats"
        );
        CinemaRoomDetail standardDetail = new CinemaRoomDetail(
                standardHall, 90, LocalDate.of(2025, 3, 15), "Standard viewing hall, general admission"
        );
        detailDao.save(vipDetail);
        detailDao.save(standardDetail);

        System.out.println("Saved: " + vipDetail);
        System.out.println("Saved: " + standardDetail);
    }

    private static void setupSeats() {
        System.out.println("\n=== [3] Adding seats ===");

        List<String> vipColumns = List.of("A", "B");
        List<CinemaRoom> rooms = roomDao.findAll();

        CinemaRoom vipHall = rooms.stream()
                .filter(r -> r.getCinemaRoomName().equals("VIP Hall"))
                .findFirst()
                .orElseThrow();

        CinemaRoom standardHall = rooms.stream()
                .filter(r -> r.getCinemaRoomName().equals("Standard Hall"))
                .findFirst()
                .orElseThrow();

        vipColumns.forEach(col -> {
            Seat seat = new Seat(vipHall, col, 1, "Available", "VIP");
            seatDao.save(seat);
            System.out.println("Saved: " + seat);
        });

        List.of("C", "D").forEach(col -> {
            Seat seat = new Seat(standardHall, col, 1, "Available", "Normal");
            seatDao.save(seat);
            System.out.println("Saved: " + seat);
        });
    }

    private static void listAllRooms() {
        System.out.println("\n=== [4] All cinema rooms ===");
        roomDao.findAll().forEach(System.out::println);
    }

    private static void upgradeRoomCapacity() {
        System.out.println("\n=== [5] Update VIP Hall capacity ===");

        roomDao.findAll().stream()
                .filter(r -> r.getCinemaRoomName().equals("VIP Hall"))
                .findFirst()
                .ifPresent(room -> {
                    room.setSeatQuantity(60);
                    roomDao.update(room);
                    roomDao.findById(room.getCinemaRoomId())
                           .ifPresent(updated -> System.out.println("Updated: " + updated));
                });
    }

    private static void bookSeat() {
        System.out.println("\n=== [6] Book seat A-1 in VIP Hall ===");

        seatDao.findAll().stream()
                .filter(s -> "A".equals(s.getSeatColumn()) && s.getSeatRow() == 1)
                .findFirst()
                .ifPresent(seat -> {
                    seat.setSeatStatus("Booked");
                    seatDao.update(seat);
                    seatDao.findById(seat.getSeatId())
                           .ifPresent(booked -> System.out.println("Booked: " + booked));
                });
    }

    private static void listAllSeats() {
        System.out.println("\n=== [7] All seats ===");
        seatDao.findAll().forEach(System.out::println);
    }

    private static void removeRoom() {
        System.out.println("\n=== [8] Remove Standard Hall ===");

        roomDao.findAll().stream()
                .filter(r -> r.getCinemaRoomName().equals("Standard Hall"))
                .findFirst()
                .ifPresent(room -> {
                    roomDao.deleteById(room.getCinemaRoomId());
                    System.out.println("Deleted: " + room.getCinemaRoomName());
                });

        System.out.println("Remaining rooms:");
        roomDao.findAll().forEach(System.out::println);
    }
}

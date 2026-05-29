package fa.training;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieTheaterApp {

    public static void main(String[] args) {
        System.out.println("--- MOVIE THEATER SIMULATION START ---");

        RoomDao roomDao = new RoomDaoImpl();
        RoomDetailDao detailDao = new RoomDetailDaoImpl();
        SeatDao seatDao = new SeatDaoImpl();

        // 1. Create a Cinema Room
        CinemaRoom room = new CinemaRoom("Grand Hall", 4);
        roomDao.insertRoom(room);
        System.out.println("Created Room: " + room);

        // 2. Add Room Detail
        CinemaRoomDetail detail = new CinemaRoomDetail(120000, LocalDate.now(), "Main screening hall with IMAX support.");
        detail.setCinemaRoom(room);
        detailDao.insertRoomDetail(detail);
        System.out.println("Added Detail: " + detail);

        // 3. Add Seats to the Room
        List<Seat> seats = new ArrayList<>();
        seats.add(new Seat("A", 1, "Available", "VIP"));
        seats.add(new Seat("A", 2, "Available", "VIP"));
        seats.add(new Seat("B", 1, "Available", "Normal"));
        seats.add(new Seat("B", 2, "Available", "Normal"));

        for (Seat seat : seats) {
            seat.setCinemaRoom(room);
            seatDao.insertSeat(seat);
            System.out.println("Added Seat: " + seat);
        }

        // 4. Update a Seat status (Book it)
        Seat seatToBook = seatDao.getSeatById(1);
        if (seatToBook != null) {
            seatToBook.setSeatStatus("Booked");
            seatDao.updateSeatById(1, seatToBook);
            System.out.println("Updated Seat 1 Status: Booked");
        }

        // 5. List all rooms and their details
        List<CinemaRoom> allRooms = roomDao.getAllRooms();
        System.out.println("\n--- CURRENT ROOMS ---");
        for (CinemaRoom r : allRooms) {
            System.out.println(r);
            CinemaRoomDetail d = r.getCinemaRoomDetail(); // OneToOne relationship
            if (d != null) {
                System.out.println("  Detail: " + d.getRoomDescription() + " (Rate: " + d.getRoomRate() + ")");
            }
        }

        // 6. List all seats
        List<Seat> allSeats = seatDao.getAllSeats();
        System.out.println("\n--- CURRENT SEATS ---");
        for (Seat s : allSeats) {
            System.out.println(s + " in " + s.getCinemaRoom().getCinemaRoomName());
        }

        HibernateUtils.shutdown();
        System.out.println("\n--- MOVIE THEATER SIMULATION END ---");
    }
}

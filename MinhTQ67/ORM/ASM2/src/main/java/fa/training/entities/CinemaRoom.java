package fa.training.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity mapped to CINEMA_ROOM table.
 *
 * Relationships:
 *   - OneToMany  with Seat           (1 room has many seats)
 *   - OneToOne   with CinemaRoomDetail (1 room has 1 detail)
 */
@Entity
@Table(name = "CINEMA_ROOM")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_ID")
    private int cinemaRoomId;

    @Column(name = "CINEMA_ROOM_NAME", nullable = false, unique = true, length = 255)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY", nullable = false)
    private int seatQuantity;

    /**
     * OneToMany: One CinemaRoom has many Seats.
     * cascade = ALL  -> save/delete room also saves/deletes its seats
     * orphanRemoval  -> removing seat from list also deletes from DB
     */
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    /**
     * OneToOne: One CinemaRoom has one CinemaRoomDetail.
     * cascade = ALL  -> save/delete room also saves/deletes its detail
     */
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CinemaRoomDetail cinemaRoomDetail;

    // ============ Constructors ============

    public CinemaRoom() {}

    public CinemaRoom(String cinemaRoomName, int seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    // ============ Helper methods ============

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setCinemaRoom(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setCinemaRoom(null);
    }

    public void setDetail(CinemaRoomDetail detail) {
        this.cinemaRoomDetail = detail;
        detail.setCinemaRoom(this);
    }

    // ============ Getters & Setters ============

    public int getCinemaRoomId() { return cinemaRoomId; }
    public void setCinemaRoomId(int cinemaRoomId) { this.cinemaRoomId = cinemaRoomId; }

    public String getCinemaRoomName() { return cinemaRoomName; }
    public void setCinemaRoomName(String cinemaRoomName) { this.cinemaRoomName = cinemaRoomName; }

    public int getSeatQuantity() { return seatQuantity; }
    public void setSeatQuantity(int seatQuantity) { this.seatQuantity = seatQuantity; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    public CinemaRoomDetail getCinemaRoomDetail() { return cinemaRoomDetail; }
    public void setCinemaRoomDetail(CinemaRoomDetail cinemaRoomDetail) { this.cinemaRoomDetail = cinemaRoomDetail; }

    @Override
    public String toString() {
        return "CinemaRoom{id=" + cinemaRoomId
                + ", name='" + cinemaRoomName + '\''
                + ", seatQuantity=" + seatQuantity + '}';
    }
}

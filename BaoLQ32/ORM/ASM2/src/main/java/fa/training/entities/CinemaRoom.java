package fa.training.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to CINEMA_ROOM table.
 *
 * Relationships:
 *   OneToOne  → CinemaRoomDetail  (a room has exactly one detail record)
 *   OneToMany → Seat              (a room has many seats)
 *
 * Many-to-many is intentionally avoided as per assignment constraints.
 */
@Entity
@Table(name = "CINEMA_ROOM")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_ID")
    private int cinemaRoomId;

    @Column(name = "CINEMA_ROOM_NAME", nullable = false, length = 255, unique = true)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY", nullable = false)
    private int seatQuantity;

    /**
     * OneToOne: a room owns one detail record.
     * CascadeType.ALL so detail is persisted/deleted together with room.
     * mappedBy = field name in CinemaRoomDetail (not column name).
     */
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL,
              fetch = FetchType.LAZY, orphanRemoval = true)
    private CinemaRoomDetail cinemaRoomDetail;

    /**
     * OneToMany: a room owns many seats.
     * Bidirectional — Seat holds the FK column (CINEMA_ROOM_ID).
     */
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL,
               fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    // --- Constructors ---

    public CinemaRoom() { }

    public CinemaRoom(String cinemaRoomName, int seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    // --- Bidirectional helpers ---

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setCinemaRoom(this);
    }

    public void setDetail(CinemaRoomDetail detail) {
        this.cinemaRoomDetail = detail;
        detail.setCinemaRoom(this);
    }

    // --- Getters & Setters ---

    public int getCinemaRoomId() { return cinemaRoomId; }
    public void setCinemaRoomId(int cinemaRoomId) { this.cinemaRoomId = cinemaRoomId; }

    public String getCinemaRoomName() { return cinemaRoomName; }
    public void setCinemaRoomName(String cinemaRoomName) { this.cinemaRoomName = cinemaRoomName; }

    public int getSeatQuantity() { return seatQuantity; }
    public void setSeatQuantity(int seatQuantity) { this.seatQuantity = seatQuantity; }

    public CinemaRoomDetail getCinemaRoomDetail() { return cinemaRoomDetail; }
    public void setCinemaRoomDetail(CinemaRoomDetail cinemaRoomDetail) { this.cinemaRoomDetail = cinemaRoomDetail; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    @Override
    public String toString() {
        return "CinemaRoom{id=" + cinemaRoomId
                + ", name='" + cinemaRoomName + '\''
                + ", seatQuantity=" + seatQuantity + '}';
    }
}

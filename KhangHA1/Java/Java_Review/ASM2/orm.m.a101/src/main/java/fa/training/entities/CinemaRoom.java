package fa.training.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CINEMA_ROOM")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_ID")
    private int cinemaRoomId;

    @Column(name = "CINEMA_ROOM_NAME", nullable = false, length = 255)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY", nullable = false)
    private int seatQuantity;

    // One-to-many relationship with Seat. A room has a lot of seats.
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    // One-to-one relationship with CinemaRoomDetail.
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private CinemaRoomDetail cinemaRoomDetail;

    public CinemaRoom() {
    }

    public CinemaRoom(String cinemaRoomName, int seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    public int getCinemaRoomId() {
        return cinemaRoomId;
    }

    public void setCinemaRoomId(int cinemaRoomId) {
        this.cinemaRoomId = cinemaRoomId;
    }

    public String getCinemaRoomName() {
        return cinemaRoomName;
    }

    public void setCinemaRoomName(String cinemaRoomName) {
        this.cinemaRoomName = cinemaRoomName;
    }

    public int getSeatQuantity() {
        return seatQuantity;
    }

    public void setSeatQuantity(int seatQuantity) {
        this.seatQuantity = seatQuantity;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public CinemaRoomDetail getCinemaRoomDetail() {
        return cinemaRoomDetail;
    }

    public void setCinemaRoomDetail(CinemaRoomDetail cinemaRoomDetail) {
        this.cinemaRoomDetail = cinemaRoomDetail;
        if (cinemaRoomDetail != null && cinemaRoomDetail.getCinemaRoom() != this) {
            cinemaRoomDetail.setCinemaRoom(this);
        }
    }

    // Helper methods to keep relationships in sync
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setCinemaRoom(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setCinemaRoom(null);
    }

    @Override
    public String toString() {
        return "CinemaRoom{id=" + cinemaRoomId + ", name='" + cinemaRoomName + "', seatQuantity=" + seatQuantity + "}";
    }
}

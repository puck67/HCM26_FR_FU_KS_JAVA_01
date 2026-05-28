package fa.training.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CINEMA_ROOM")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_ID")
    private int cinemaRoomId;

    @Column(name = "CINEMA_ROOM_NAME", nullable = false)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY")
    private int seatQuantity;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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
    }

    // Helper methods for cascading relationship management
    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setCinemaRoom(this);
    }

    public void removeSeat(Seat seat) {
        seats.remove(seat);
        seat.setCinemaRoom(null);
    }

    public void setCinemaRoomDetailHelper(CinemaRoomDetail detail) {
        if (detail == null) {
            if (this.cinemaRoomDetail != null) {
                this.cinemaRoomDetail.setCinemaRoom(null);
            }
        } else {
            detail.setCinemaRoom(this);
        }
        this.cinemaRoomDetail = detail;
    }

    @Override
    public String toString() {
        return "CinemaRoom{" +
                "cinemaRoomId=" + cinemaRoomId +
                ", cinemaRoomName='" + cinemaRoomName + '\'' +
                ", seatQuantity=" + seatQuantity +
                '}';
    }
}

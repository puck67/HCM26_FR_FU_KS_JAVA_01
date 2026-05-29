package fa.training.entities;

import javax.persistence.*;
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

    @Column(name = "SEAT_QUANTITY")
    private int seatQuantity;

    // One-to-Many với Seat (mappedBy trỏ tới thuộc tính cinemaRoom trong class Seat)
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

    // One-to-One với Detail (mappedBy trỏ tới thuộc tính cinemaRoom trong class CinemaRoomDetail)
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CinemaRoomDetail cinemaRoomDetail;

    public CinemaRoom() {
    }

    // Getters and Setters
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
}
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

    @Column(name = "CINEMA_ROOM_NAME", length = 255, nullable = false)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY")
    private int seatQuantity;

    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL)
    private CinemaRoomDetail cinemaRoomDetail;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats;

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

    public CinemaRoomDetail getCinemaRoomDetail() {
        return cinemaRoomDetail;
    }

    public void setCinemaRoomDetail(CinemaRoomDetail cinemaRoomDetail) {
        this.cinemaRoomDetail = cinemaRoomDetail;
        if (cinemaRoomDetail != null) {
            cinemaRoomDetail.setCinemaRoom(this);
        }
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
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

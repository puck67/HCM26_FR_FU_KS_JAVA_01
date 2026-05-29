package fa.training.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToOne(mappedBy = "cinemaRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private CinemaRoomDetail detail;

    @OneToMany(mappedBy = "cinemaRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    public CinemaRoom() {}

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

    public CinemaRoomDetail getDetail() {
        return detail;
    }

    public void setDetail(CinemaRoomDetail detail) {
        this.detail = detail;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return new StringBuilder("CinemaRoom{")
                .append("id=").append(cinemaRoomId)
                .append(", name='").append(cinemaRoomName).append('\'')
                .append(", seatQty=").append(seatQuantity)
                .append('}')
                .toString();
    }
}

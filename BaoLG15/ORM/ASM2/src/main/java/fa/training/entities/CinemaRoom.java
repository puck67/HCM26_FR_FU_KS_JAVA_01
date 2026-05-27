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

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CinemaRoomDetail cinemaRoomDetail;

    public CinemaRoom() {}

    public CinemaRoom(String cinemaRoomName, int seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

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
        return String.format("CinemaRoom{id=%d, name='%s', seatQty=%d}", cinemaRoomId, cinemaRoomName, seatQuantity);
    }
}

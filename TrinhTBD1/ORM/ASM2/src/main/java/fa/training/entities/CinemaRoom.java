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
    private Integer cinemaRoomId;

    @Column(name = "CINEMA_ROOM_NAME", unique = true, nullable = false, length = 255)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY")
    private Integer seatQuantity;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private CinemaRoomDetail cinemaRoomDetail;

    public CinemaRoom() {
    }

    public CinemaRoom(String cinemaRoomName, Integer seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    public Integer getCinemaRoomId() {
        return cinemaRoomId;
    }

    public void setCinemaRoomId(Integer cinemaRoomId) {
        this.cinemaRoomId = cinemaRoomId;
    }

    public String getCinemaRoomName() {
        return cinemaRoomName;
    }

    public void setCinemaRoomName(String cinemaRoomName) {
        this.cinemaRoomName = cinemaRoomName;
    }

    public Integer getSeatQuantity() {
        return seatQuantity;
    }

    public void setSeatQuantity(Integer seatQuantity) {
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

    public void addSeat(Seat seat) {
        if (seat != null) {
            seats.add(seat);
            seat.setCinemaRoom(this);
        }
    }

    public void removeSeat(Seat seat) {
        if (seat != null) {
            seats.remove(seat);
            seat.setCinemaRoom(null);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("CinemaRoom{cinemaRoomId=").append(cinemaRoomId)
                .append(", cinemaRoomName='").append(cinemaRoomName).append('\'')
                .append(", seatQuantity=").append(seatQuantity)
                .append('}')
                .toString();
    }
}

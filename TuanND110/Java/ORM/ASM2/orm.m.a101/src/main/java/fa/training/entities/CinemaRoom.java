package fa.training.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * CinemaRoom Entity - Represents a cinema room
 */
@Entity
@Table(name = "cinema_room")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_room_id")
    private Integer cinemaRoomId;

    @Column(name = "cinema_room_name", nullable = false, unique = true, length = 255)
    private String cinemaRoomName;

    @Column(name = "seat_quantity", nullable = false)
    private Integer seatQuantity;

    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private CinemaRoomDetail roomDetail;

    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Seat> seats = new HashSet<>();

    // Constructors
    public CinemaRoom() {
    }

    public CinemaRoom(String cinemaRoomName, Integer seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    // Getters and Setters
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

    public CinemaRoomDetail getRoomDetail() {
        return roomDetail;
    }

    public void setRoomDetail(CinemaRoomDetail roomDetail) {
        this.roomDetail = roomDetail;
        if (roomDetail != null && roomDetail.getCinemaRoom() != this) {
            roomDetail.setCinemaRoom(this);
        }
    }

    public Set<Seat> getSeats() {
        return seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public void addSeat(Seat seat) {
        this.seats.add(seat);
        seat.setCinemaRoom(this);
    }

    public void removeSeat(Seat seat) {
        this.seats.remove(seat);
        seat.setCinemaRoom(null);
    }

    @Override
    public String toString() {
        return "CinemaRoom{" +
                "cinemaRoomId=" + cinemaRoomId +
                ", cinemaRoomName='" + cinemaRoomName + '\'' +
                ", seatQuantity=" + seatQuantity +
                ", seatsCount=" + seats.size() +
                '}';
    }
}

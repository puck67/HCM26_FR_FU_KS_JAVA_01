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

    @Column(name = "CINEMA_ROOM_NAME", nullable = false, length = 255)
    private String cinemaRoomName;

    @Column(name = "SEAT_QUANTITY")
    private int seatQuantity;

    // Quan hệ 1-1 với CinemaRoomDetail
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private CinemaRoomDetail cinemaRoomDetail;

    // Quan hệ 1-n với Seat
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Seat> seats = new ArrayList<>();

    public CinemaRoom() {}

    public CinemaRoom(String cinemaRoomName, int seatQuantity) {
        this.cinemaRoomName = cinemaRoomName;
        this.seatQuantity = seatQuantity;
    }

    // Tiện ích quản lý quan hệ hai chiều (Bidirectional synchronization)
    public void setCinemaRoomDetail(CinemaRoomDetail detail) {
        if (detail == null) {
            if (this.cinemaRoomDetail != null) {
                this.cinemaRoomDetail.setCinemaRoom(null);
            }
        } else {
            detail.setCinemaRoom(this);
        }
        this.cinemaRoomDetail = detail;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
        seat.setCinemaRoom(this);
    }

    // Getters and Setters
    public int getCinemaRoomId() { return cinemaRoomId; }
    public void setCinemaRoomId(int cinemaRoomId) { this.cinemaRoomId = cinemaRoomId; }

    public String getCinemaRoomName() { return cinemaRoomName; }
    public void setCinemaRoomName(String cinemaRoomName) { this.cinemaRoomName = cinemaRoomName; }

    public int getSeatQuantity() { return seatQuantity; }
    public void setSeatQuantity(int seatQuantity) { this.seatQuantity = seatQuantity; }

    public CinemaRoomDetail getCinemaRoomDetail() { return cinemaRoomDetail; }
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    @Override
    public String toString() {
        return String.format("Phòng chiếu [ID: %d | Tên: %s | Số lượng ghế: %d]", cinemaRoomId, cinemaRoomName, seatQuantity);
    }
}

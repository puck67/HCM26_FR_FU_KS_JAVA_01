package fa.training.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CINEMA_ROOM")
public class CinemaRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    // One room has one detail (bidirectional OneToOne)
    @OneToOne(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private CinemaRoomDetail cinemaRoomDetail;

    // One room has many seats (bidirectional OneToMany)
    @OneToMany(mappedBy = "cinemaRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Seat> seats = new ArrayList<>();

    public CinemaRoom() {}

    public CinemaRoom(String roomName, int capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public CinemaRoomDetail getCinemaRoomDetail() { return cinemaRoomDetail; }
    public void setCinemaRoomDetail(CinemaRoomDetail cinemaRoomDetail) { this.cinemaRoomDetail = cinemaRoomDetail; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CinemaRoom{roomId=").append(roomId)
          .append(", roomName='").append(roomName).append("'")
          .append(", capacity=").append(capacity)
          .append("}");
        return sb.toString();
    }
}

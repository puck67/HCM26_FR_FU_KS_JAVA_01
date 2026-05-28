package fa.training.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "CINEMA_ROOM_DETAIL")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_DETAIL_ID")
    private int cinemaRoomDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(name = "ROOM_RATE")
    private int roomRate;

    @Column(name = "ACTIVE_DATE")
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION", length = 250)
    private String roomDescription;

    public CinemaRoomDetail() {}

    public CinemaRoomDetail(CinemaRoom cinemaRoom, int roomRate, LocalDate activeDate, String roomDescription) {
        this.cinemaRoom = cinemaRoom;
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    public int getCinemaRoomDetailId() { return cinemaRoomDetailId; }
    public void setCinemaRoomDetailId(int cinemaRoomDetailId) { this.cinemaRoomDetailId = cinemaRoomDetailId; }

    public CinemaRoom getCinemaRoom() { return cinemaRoom; }
    public void setCinemaRoom(CinemaRoom cinemaRoom) { this.cinemaRoom = cinemaRoom; }

    public int getRoomRate() { return roomRate; }
    public void setRoomRate(int roomRate) { this.roomRate = roomRate; }

    public LocalDate getActiveDate() { return activeDate; }
    public void setActiveDate(LocalDate activeDate) { this.activeDate = activeDate; }

    public String getRoomDescription() { return roomDescription; }
    public void setRoomDescription(String roomDescription) { this.roomDescription = roomDescription; }

    @Override
    public String toString() {
        return String.format("CinemaRoomDetail{id=%d, roomId=%d, rate=%d, activeDate=%s}",
                cinemaRoomDetailId, cinemaRoom != null ? cinemaRoom.getCinemaRoomId() : 0,
                roomRate, activeDate);
    }
}

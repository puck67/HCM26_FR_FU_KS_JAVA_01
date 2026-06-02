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

    @Column(name = "ROOM_RATE", nullable = false)
    private int roomRate;

    @Column(name = "ACTIVE_DATE", nullable = false)
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION", length = 250, nullable = false)
    private String roomDescription;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    public CinemaRoomDetail() {}

    public CinemaRoomDetail(int roomRate, LocalDate activeDate, String roomDescription) {
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    public int getCinemaRoomDetailId() { return cinemaRoomDetailId; }
    public void setCinemaRoomDetailId(int cinemaRoomDetailId) { this.cinemaRoomDetailId = cinemaRoomDetailId; }

    public int getRoomRate() { return roomRate; }
    public void setRoomRate(int roomRate) { this.roomRate = roomRate; }

    public LocalDate getActiveDate() { return activeDate; }
    public void setActiveDate(LocalDate activeDate) { this.activeDate = activeDate; }

    public String getRoomDescription() { return roomDescription; }
    public void setRoomDescription(String roomDescription) { this.roomDescription = roomDescription; }

    public CinemaRoom getCinemaRoom() { return cinemaRoom; }
    public void setCinemaRoom(CinemaRoom cinemaRoom) { this.cinemaRoom = cinemaRoom; }

    @Override
    public String toString() {
        return "CinemaRoomDetail{id=" + cinemaRoomDetailId + ", rate=" + roomRate
                + ", activeDate=" + activeDate + ", desc='" + roomDescription + "'}";
    }
}
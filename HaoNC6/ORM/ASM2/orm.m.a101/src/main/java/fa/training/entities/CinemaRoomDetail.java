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

    @Column(name = "ROOM_RATE")
    private int roomRate;

    // Đề bài khuyến khích dùng LocalDate cho các cột Date/Time để chính xác cao
    @Column(name = "ACTIVE_DATE")
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION", length = 250)
    private String roomDescription;

    @OneToOne
    @JoinColumn(name = "CINEMA_ROOM_ID", referencedColumnName = "CINEMA_ROOM_ID")
    private CinemaRoom cinemaRoom;

    public CinemaRoomDetail() {
    }

    // Getters and Setters
    public int getCinemaRoomDetailId() {
        return cinemaRoomDetailId;
    }

    public void setCinemaRoomDetailId(int cinemaRoomDetailId) {
        this.cinemaRoomDetailId = cinemaRoomDetailId;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(int roomRate) {
        this.roomRate = roomRate;
    }

    public LocalDate getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(LocalDate activeDate) {
        this.activeDate = activeDate;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }
}
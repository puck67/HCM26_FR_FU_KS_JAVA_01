package fa.training.entities;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity mapped to CINEMA_ROOM_DETAIL table.
 *
 * Relationships:
 *   - OneToOne with CinemaRoom (owns the FK: CINEMA_ROOM_ID)
 *
 * Uses LocalDate for ACTIVE_DATE column (Java 8, high accuracy).
 */
@Entity
@Table(name = "CINEMA_ROOM_DETAIL")
public class CinemaRoomDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CINEMA_ROOM_DETAIL_ID")
    private int cinemaRoomDetailId;

    /**
     * OneToOne owning side: holds the FK CINEMA_ROOM_ID in this table.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false, unique = true)
    private CinemaRoom cinemaRoom;

    @Column(name = "ROOM_RATE", nullable = false)
    private int roomRate;

    /**
     * Using LocalDate as recommended in the assignment guidelines.
     * Requires hibernate-java8 dependency.
     */
    @Column(name = "ACTIVE_DATE", nullable = false)
    private LocalDate activeDate;

    @Column(name = "ROOM_DESCRIPTION", length = 250)
    private String roomDescription;

    // ============ Constructors ============

    public CinemaRoomDetail() {}

    public CinemaRoomDetail(int roomRate, LocalDate activeDate, String roomDescription) {
        this.roomRate = roomRate;
        this.activeDate = activeDate;
        this.roomDescription = roomDescription;
    }

    // ============ Getters & Setters ============

    public int getCinemaRoomDetailId() { return cinemaRoomDetailId; }
    public void setCinemaRoomDetailId(int id) { this.cinemaRoomDetailId = id; }

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
        return "CinemaRoomDetail{id=" + cinemaRoomDetailId
                + ", roomRate=" + roomRate
                + ", activeDate=" + activeDate
                + ", description='" + roomDescription + '\'' + '}';
    }
}

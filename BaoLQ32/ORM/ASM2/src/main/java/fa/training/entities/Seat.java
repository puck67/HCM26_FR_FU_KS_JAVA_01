package fa.training.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Maps to SEAT table.
 *
 * ManyToOne (owning side) — holds FK column CINEMA_ROOM_ID.
 *
 * seatStatus: 'Available' | 'Not Available' | 'Booked'
 * seatType  : 'VIP'       | 'Normal'
 */
@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private int seatId;

    /**
     * Owning side of OneToMany/ManyToOne.
     * FK column CINEMA_ROOM_ID is stored in SEAT table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    @Column(name = "SEAT_COLUMN", nullable = false, length = 255)
    private String seatColumn;

    @Column(name = "SEAT_ROW", nullable = false)
    private int seatRow;

    /** Values: 'Available', 'Not Available', 'Booked' */
    @Column(name = "SEAT_STATUS", nullable = false, length = 255)
    private String seatStatus;

    /** Values: 'VIP', 'Normal' */
    @Column(name = "SEAT_TYPE", nullable = false, length = 255)
    private String seatType;

    // --- Constructors ---

    public Seat() { }

    public Seat(String seatColumn, int seatRow, String seatStatus, String seatType) {
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    // --- Getters & Setters ---

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }

    public CinemaRoom getCinemaRoom() { return cinemaRoom; }
    public void setCinemaRoom(CinemaRoom cinemaRoom) { this.cinemaRoom = cinemaRoom; }

    public String getSeatColumn() { return seatColumn; }
    public void setSeatColumn(String seatColumn) { this.seatColumn = seatColumn; }

    public int getSeatRow() { return seatRow; }
    public void setSeatRow(int seatRow) { this.seatRow = seatRow; }

    public String getSeatStatus() { return seatStatus; }
    public void setSeatStatus(String seatStatus) { this.seatStatus = seatStatus; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    @Override
    public String toString() {
        return "Seat{id=" + seatId
                + ", col='" + seatColumn + '\''
                + ", row=" + seatRow
                + ", status='" + seatStatus + '\''
                + ", type='" + seatType + '\'' + '}';
    }
}

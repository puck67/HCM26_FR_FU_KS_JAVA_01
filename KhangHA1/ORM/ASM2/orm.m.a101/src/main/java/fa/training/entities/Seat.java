package fa.training.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private int seatId;

    @Column(name = "SEAT_COLUMN", nullable = false, length = 255)
    private String seatColumn;

    @Column(name = "SEAT_ROW", nullable = false)
    private int seatRow;

    @Column(name = "SEAT_STATUS", nullable = false, length = 255)
    private String seatStatus; // e.g. 'Available', 'Not Available', 'Booked'

    @Column(name = "SEAT_TYPE", nullable = false, length = 255)
    private String seatType; // e.g. 'VIP', 'Normal'

    @ManyToOne
    @JoinColumn(name = "CINEMA_ROOM_ID", nullable = false)
    private CinemaRoom cinemaRoom;

    public Seat() {
    }

    public Seat(String seatColumn, int seatRow, String seatStatus, String seatType) {
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(String seatColumn) {
        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public CinemaRoom getCinemaRoom() {
        return cinemaRoom;
    }

    public void setCinemaRoom(CinemaRoom cinemaRoom) {
        this.cinemaRoom = cinemaRoom;
    }

    @Override
    public String toString() {
        return "Seat{id=" + seatId + ", column='" + seatColumn + "', row=" + seatRow + ", status='" + seatStatus + "', type='" + seatType + "'}";
    }
}

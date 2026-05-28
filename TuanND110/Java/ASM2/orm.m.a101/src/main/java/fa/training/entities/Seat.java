package fa.training.entities;

import javax.persistence.*;

/**
 * Seat Entity - Represents a seat in a cinema room
 */
@Entity
@Table(name = "seat", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"cinema_room_id", "seat_row", "seat_column"})
})
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Integer seatId;

    @Column(name = "seat_row", nullable = false)
    private Integer seatRow;

    @Column(name = "seat_column", nullable = false, length = 255)
    private String seatColumn;

    @Column(name = "seat_status", nullable = false, length = 255)
    private String seatStatus; // 'Available', 'Not Available', 'Booked'

    @Column(name = "seat_type", nullable = false, length = 255)
    private String seatType; // 'VIP', 'Normal'

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_room_id", nullable = false)
    private CinemaRoom cinemaRoom;

    // Constructors
    public Seat() {
    }

    public Seat(Integer seatRow, String seatColumn, String seatStatus, String seatType) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    public Seat(Integer seatRow, String seatColumn, String seatStatus, String seatType, CinemaRoom cinemaRoom) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
        this.cinemaRoom = cinemaRoom;
    }

    // Getters and Setters
    public Integer getSeatId() {
        return seatId;
    }

    public void setSeatId(Integer seatId) {
        this.seatId = seatId;
    }

    public Integer getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(Integer seatRow) {
        this.seatRow = seatRow;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    public void setSeatColumn(String seatColumn) {
        this.seatColumn = seatColumn;
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
        return "Seat{" +
                "seatId=" + seatId +
                ", seatRow=" + seatRow +
                ", seatColumn='" + seatColumn + '\'' +
                ", seatStatus='" + seatStatus + '\'' +
                ", seatType='" + seatType + '\'' +
                '}';
    }
}

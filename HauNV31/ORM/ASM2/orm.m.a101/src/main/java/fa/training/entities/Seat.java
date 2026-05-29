package fa.training.entities;

import javax.persistence.*;

@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "seat_column", nullable = false, length = 5)
    private String seatColumn;

    @Column(name = "seat_row", nullable = false)
    private int seatRow;

    // Allowed values: 'Available', 'Not Available', 'Booked'
    @Column(name = "seat_status", nullable = false, length = 20)
    private String seatStatus;

    // Allowed values: 'VIP', 'Normal'
    @Column(name = "seat_type", nullable = false, length = 10)
    private String seatType;

    // ManyToOne - many seats belong to one room
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private CinemaRoom cinemaRoom;

    public Seat() {}

    public Seat(String seatColumn, int seatRow, String seatStatus, String seatType) {
        this.seatColumn = seatColumn;
        this.seatRow = seatRow;
        this.seatStatus = seatStatus;
        this.seatType = seatType;
    }

    public Long getSeatId() { return seatId; }
    public void setSeatId(Long seatId) { this.seatId = seatId; }

    public String getSeatColumn() { return seatColumn; }
    public void setSeatColumn(String seatColumn) { this.seatColumn = seatColumn; }

    public int getSeatRow() { return seatRow; }
    public void setSeatRow(int seatRow) { this.seatRow = seatRow; }

    public String getSeatStatus() { return seatStatus; }
    public void setSeatStatus(String seatStatus) { this.seatStatus = seatStatus; }

    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    public CinemaRoom getCinemaRoom() { return cinemaRoom; }
    public void setCinemaRoom(CinemaRoom cinemaRoom) { this.cinemaRoom = cinemaRoom; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Seat{seatId=").append(seatId)
          .append(", seatColumn='").append(seatColumn).append("'")
          .append(", seatRow=").append(seatRow)
          .append(", seatStatus='").append(seatStatus).append("'")
          .append(", seatType='").append(seatType).append("'")
          .append("}");
        return sb.toString();
    }
}

package model;

public class HotelRoom {
    private String     roomId;
    private RoomType   roomType;
    private double     pricePerNight;
    private int        capacity;
    private RoomStatus status;
    private String     description;

    public HotelRoom(String roomId, RoomType roomType, double pricePerNight,
                     int capacity, RoomStatus status, String description) {
        this.roomId        = roomId;
        this.roomType      = roomType;
        this.pricePerNight = pricePerNight;
        this.capacity      = capacity;
        this.status        = status;
        this.description   = description;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append(String.format("| %-8s | %-10s | %12.2f | %8d | %-12s | %-30s |",
                roomId,
                roomType.name(),
                pricePerNight,
                capacity,
                status.name(),
                description))
            .toString();
    }

    public String toCsvLine() {
        return new StringBuilder()
            .append(roomId).append(",")
            .append(roomType.name()).append(",")
            .append(String.format("%.2f", pricePerNight)).append(",")
            .append(capacity).append(",")
            .append(status.name()).append(",")
            .append(description)
            .toString();
    }
}

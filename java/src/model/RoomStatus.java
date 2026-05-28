package model;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RoomStatus {
    AVAILABLE("Available"),
    OCCUPIED("Occupied"),
    MAINTENANCE("Maintenance");

    private final String displayName;

    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    /** Parse từ string, trả Optional để caller tự xử lý lỗi. */
    public static Optional<RoomStatus> fromString(String input) {
        if (input == null) return Optional.empty();
        return Arrays.stream(values())
                     .filter(s -> s.name().equalsIgnoreCase(input.trim()))
                     .findFirst();
    }

    /** Liệt kê tất cả tên enum, phân cách bằng dấu / */
    public static String allNames() {
        return Arrays.stream(values())
                     .map(Enum::name)
                     .collect(Collectors.joining(" / "));
    }
}

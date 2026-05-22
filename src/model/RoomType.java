package model;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public enum RoomType {
    SINGLE("Single Bed", 1),
    DOUBLE("Double Bed", 2),
    SUITE("Suite",       4),
    DELUXE("Deluxe",    3);

    private final String displayName;
    private final int defaultCapacity;

    RoomType(String displayName, int defaultCapacity) {
        this.displayName     = displayName;
        this.defaultCapacity = defaultCapacity;
    }

    public String getDisplayName()    { return displayName; }
    public int getDefaultCapacity()   { return defaultCapacity; }

    /** Parse từ string, trả Optional để caller tự xử lý lỗi. */
    public static Optional<RoomType> fromString(String input) {
        if (input == null) return Optional.empty();
        return Arrays.stream(values())
                     .filter(t -> t.name().equalsIgnoreCase(input.trim()))
                     .findFirst();
    }

    /** Liệt kê tất cả tên enum, phân cách bằng dấu / */
    public static String allNames() {
        return Arrays.stream(values())
                     .map(Enum::name)
                     .collect(Collectors.joining(" / "));
    }
}

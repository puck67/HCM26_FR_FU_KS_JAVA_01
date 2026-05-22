package util;

import model.HotelRoom;

public class Validator {

    public static final String ROOM_ID_REGEX = "^R\\d{3}$";

    public static final String PRICE_REGEX = "^\\d+(\\.\\d{1,2})?$";

    public static final String CAPACITY_REGEX = "^([1-9]|10)$";

    public static final String ROOM_TYPE_REGEX = "^(?i)(SINGLE|DOUBLE|SUITE|DELUXE)$";

    public static final String STATUS_REGEX = "^(?i)(AVAILABLE|OCCUPIED|MAINTENANCE)$";

    public static final String DESCRIPTION_REGEX = "^.{3,100}$";

    public static final String EMAIL_REGEX = "^[A-Za-z0-9][A-Za-z0-9+_.\\-]*@[A-Za-z0-9][A-Za-z0-9.\\-]*\\.[A-Za-z]{2,10}$";

    public static final String PHONE_REGEX = "^0\\d{8,10}$";

    public static boolean isValidRoomId(String id) {
        return id != null && id.matches(ROOM_ID_REGEX);
    }

    public static boolean isValidPriceString(String input) {
        if (input == null || !input.matches(PRICE_REGEX)) return false;
        try { return Double.parseDouble(input) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidCapacityString(String input) {
        if (input == null || !input.matches(CAPACITY_REGEX)) return false;
        try {
            int c = Integer.parseInt(input);
            return c >= 1 && c <= 10;
        } catch (NumberFormatException e) { return false; }
    }

    public static boolean isValidCapacity(int capacity) {
        return capacity >= 1 && capacity <= 10;
    }

    public static boolean isValidRoomType(String input) {
        return input != null && input.trim().matches(ROOM_TYPE_REGEX);
    }

    public static boolean isValidStatus(String input) {
        return input != null && input.trim().matches(STATUS_REGEX);
    }

    public static boolean isValidDescription(String input) {
        return input != null && input.trim().matches(DESCRIPTION_REGEX);
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }

    public static String roomIdError() {
        return "Room ID phải có dạng R + 3 chữ số (R001–R999). Pattern: " + ROOM_ID_REGEX;
    }

    public static String priceError() {
        return "Giá phải là số dương, tối đa 2 chữ số thập phân (vd: 99 hoặc 149.99). Pattern: " + PRICE_REGEX;
    }

    public static String capacityError() {
        return "Sức chứa phải là số nguyên từ 1 đến 10. Pattern: " + CAPACITY_REGEX;
    }

    public static String roomTypeError() {
        return "Loại phòng phải là SINGLE / DOUBLE / SUITE / DELUXE. Pattern: " + ROOM_TYPE_REGEX;
    }

    public static String statusError() {
        return "Trạng thái phải là AVAILABLE / OCCUPIED / MAINTENANCE. Pattern: " + STATUS_REGEX;
    }

    public static String descriptionError() {
        return "Mô tả phải có từ 3 đến 100 ký tự. Pattern: " + DESCRIPTION_REGEX;
    }

    public static String emailError() {
        return "Email không hợp lệ. Ví dụ: user@example.com. Pattern: " + EMAIL_REGEX;
    }

    public static String phoneError() {
        return "Số điện thoại phải bắt đầu bằng 0, gồm 9–11 chữ số. Pattern: " + PHONE_REGEX;
    }

    public static boolean validateHotelRoom(HotelRoom room) {
        if (room == null) return false;
        if (!isValidRoomId(room.getRoomId())) return false;
        if (room.getRoomType() == null) return false;
        if (!isValidPrice(room.getPricePerNight())) return false;
        if (!isValidCapacity(room.getCapacity())) return false;
        if (room.getStatus() == null) return false;
        if (!isValidDescription(room.getDescription())) return false;
        return true;
    }
}

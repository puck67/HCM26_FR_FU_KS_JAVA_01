package util;

import model.HotelRoom;

public class Validator {

    // ══════════════════════════════════════════════════════════════════════════
    // REGEX PATTERNS — khai báo tập trung, dễ bảo trì
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Room ID: ký tự 'R' hoa + chính xác 3 chữ số (R001 – R999).
     * <pre>
     *   Hợp lệ  : R001, R123, R999
     *   Không HV: R1, R0001, r001, A001, R12A
     * </pre>
     */
    public static final String ROOM_ID_REGEX = "^R\\d{3}$";

    /**
     * Giá tiền: số nguyên dương hoặc thập phân tối đa 2 chữ số sau dấu chấm.
     * Điều kiện > 0 được kiểm tra bổ sung sau khi parse.
     * <pre>
     *   Hợp lệ  : 99, 99.9, 99.99, 1000, 1000.00
     *   Không HV: -1, 0, .5, 99.999, 1,000, abc
     * </pre>
     */
    public static final String PRICE_REGEX = "^\\d+(\\.\\d{1,2})?$";

    /**
     * Sức chứa: số nguyên từ 1 đến 10 (không chứa khoảng trắng hay ký tự thừa).
     * <pre>
     *   Hợp lệ  : 1, 2, 5, 10
     *   Không HV: 0, 11, -1, 1.5, 01
     * </pre>
     */
    public static final String CAPACITY_REGEX = "^([1-9]|10)$";

    /**
     * Loại phòng: SINGLE / DOUBLE / SUITE / DELUXE — không phân biệt hoa thường.
     * {@code (?i)} bật case-insensitive cho toàn bộ pattern.
     * <pre>
     *   Hợp lệ  : SINGLE, single, Single, DOUBLE, SUITE, DELUXE
     *   Không HV: TWIN, SUPERIOR, "", SINGLE ROOM
     * </pre>
     */
    public static final String ROOM_TYPE_REGEX = "^(?i)(SINGLE|DOUBLE|SUITE|DELUXE)$";

    /**
     * Trạng thái phòng: AVAILABLE / OCCUPIED / MAINTENANCE — không phân biệt hoa thường.
     * <pre>
     *   Hợp lệ  : AVAILABLE, available, Available, OCCUPIED, MAINTENANCE
     *   Không HV: VACANT, BOOKED, "", FREE
     * </pre>
     */
    public static final String STATUS_REGEX =
            "^(?i)(AVAILABLE|OCCUPIED|MAINTENANCE)$";

    /**
     * Mô tả phòng: tối thiểu 3, tối đa 100 ký tự bất kỳ (kiểm tra sau khi trim).
     * {@code .} khớp mọi ký tự trừ newline — phù hợp với input console.
     * <pre>
     *   Hợp lệ  : "Sea view", "Cozy room with AC and WiFi" (3-100 chars)
     *   Không HV: "AB" (< 3), "" (rỗng), chuỗi > 100 ký tự
     * </pre>
     */
    public static final String DESCRIPTION_REGEX = "^.{3,100}$";

    /**
     * Email chuẩn RFC-lite: local@domain.tld.
     * <pre>
     *   Hợp lệ  : user@example.com, name.last+tag@mail.co.vn
     *   Không HV: user@, @domain.com, user@.com
     * </pre>
     */
    public static final String EMAIL_REGEX =
            "^[A-Za-z0-9][A-Za-z0-9+_.\\-]*@[A-Za-z0-9][A-Za-z0-9.\\-]*\\.[A-Za-z]{2,10}$";

    /**
     * Số điện thoại Việt Nam: bắt đầu bằng 0, theo sau là 8–10 chữ số (tổng 9–11 ký tự).
     * <pre>
     *   Hợp lệ  : 0912345678 (10 số), 01234567890 (11 số)
     *   Không HV: 912345678 (không có 0 đầu), +84912345678 (dùng mã quốc gia)
     * </pre>
     */
    public static final String PHONE_REGEX = "^0\\d{8,10}$";


    /**
     * Kiểm tra Room ID: không null + khớp {@link #ROOM_ID_REGEX}.
     */
    public static boolean isValidRoomId(String id) {
        return id != null && id.matches(ROOM_ID_REGEX);
    }

    /**
     * Kiểm tra chuỗi giá từ console:
     * không null + khớp {@link #PRICE_REGEX} + parse ra giá trị > 0.
     */
    public static boolean isValidPriceString(String input) {
        if (input == null || !input.matches(PRICE_REGEX)) return false;
        try { return Double.parseDouble(input) > 0; }
        catch (NumberFormatException e) { return false; }
    }

    /**
     * Kiểm tra giá dạng double đã parse: phải > 0.
     */
    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    /**
     * Kiểm tra chuỗi capacity từ console:
     * không null + khớp {@link #CAPACITY_REGEX} + parse ra giá trị 1–10.
     */
    public static boolean isValidCapacityString(String input) {
        if (input == null || !input.matches(CAPACITY_REGEX)) return false;
        try {
            int c = Integer.parseInt(input);
            return c >= 1 && c <= 10;
        } catch (NumberFormatException e) { return false; }
    }

    /**
     * Kiểm tra capacity dạng int đã parse: phải trong [1, 10].
     */
    public static boolean isValidCapacity(int capacity) {
        return capacity >= 1 && capacity <= 10;
    }

    /**
     * Kiểm tra loại phòng: không null + khớp {@link #ROOM_TYPE_REGEX} (case-insensitive).
     */
    public static boolean isValidRoomType(String input) {
        return input != null && input.trim().matches(ROOM_TYPE_REGEX);
    }

    /**
     * Kiểm tra trạng thái phòng: không null + khớp {@link #STATUS_REGEX} (case-insensitive).
     */
    public static boolean isValidStatus(String input) {
        return input != null && input.trim().matches(STATUS_REGEX);
    }

    /**
     * Kiểm tra mô tả phòng: không null + trim khớp {@link #DESCRIPTION_REGEX} (3–100 ký tự).
     */
    public static boolean isValidDescription(String input) {
        return input != null && input.trim().matches(DESCRIPTION_REGEX);
    }

    /**
     * Chuỗi không null và không blank.
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Kiểm tra email.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    /**
     * Kiểm tra số điện thoại VN.
     */
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

    /**
     * LAYER 2 VALIDATION: Kiểm tra toàn diện đối tượng HotelRoom (Domain Entity Validation).
     * Đảm bảo mọi trường dữ liệu của đối tượng đều hoàn toàn hợp lệ trước khi thao tác DB.
     */
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

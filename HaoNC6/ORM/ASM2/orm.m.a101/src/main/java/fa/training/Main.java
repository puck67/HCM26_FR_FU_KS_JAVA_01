package fa.training;

import fa.training.dao.CinemaRoomDao;
import fa.training.dao.CinemaRoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final CinemaRoomDao roomDao = new CinemaRoomDao();
    private static final CinemaRoomDetailDao detailDao = new CinemaRoomDetailDao();
    private static final SeatDao seatDao = new SeatDao();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        printBanner();
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Nhập lựa chọn của bạn (0-9): ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    insertDemoDataInteractive();
                    break;
                case "2":
                    displayAllData();
                    break;
                case "3":
                    findCinemaRoomById();
                    break;
                case "4":
                    findSeatById();
                    break;
                case "5":
                    updateCinemaRoom();
                    break;
                case "6":
                    updateSeat();
                    break;
                case "7":
                    deleteSeat();
                    break;
                case "8":
                    deleteCinemaRoom();
                    break;
                case "9":
                    runAutomatedTestSuite();
                    break;
                case "0":
                    printInfo("Đang đóng kết nối Hibernate và thoát chương trình...");
                    HibernateUtils.shutdown();
                    printSuccess("Hẹn gặp lại!");
                    running = false;
                    break;
                default:
                    printError("Lựa chọn không hợp lệ! Vui lòng chọn lại từ 0 đến 9.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("==========================================================================");
        System.out.println("             HỆ THỐNG QUẢN LÝ PHÒNG CHIẾU PHIM - HIBERNATE ORM            ");
        System.out.println("==========================================================================");
    }

    private static void printMenu() {
        System.out.println("\n=== MENU CHỨC NĂNG ===");
        System.out.println("1. Thêm dữ liệu mẫu phòng chiếu (Cascade Insert Room + Detail + Seat)");
        System.out.println("2. Hiển thị danh sách toàn bộ Phòng chiếu, Chi tiết & Ghế");
        System.out.println("3. Tìm kiếm Phòng chiếu theo ID");
        System.out.println("4. Tìm kiếm Ghế theo ID");
        System.out.println("5. Cập nhật thông tin Phòng chiếu");
        System.out.println("6. Cập nhật trạng thái Ghế ngồi");
        System.out.println("7. Xóa Ghế ngồi theo ID");
        System.out.println("8. Xóa Phòng chiếu theo ID (Cascade Delete Detail & Seats)");
        System.out.println("9. Chạy tiến trình TỰ ĐỘNG KIỂM THỬ (Automated Test Suite)");
        System.out.println("0. Thoát chương trình");
        System.out.println("======================");
    }

    private static void printHeader(String title) {
        System.out.println("\n>>> " + title.toUpperCase() + " <<<");
        System.out.println("----------------------------------------------------------------------");
    }

    private static void printSuccess(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    private static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    private static void printError(String message) {
        System.out.println("[ERROR] " + message);
    }

    // [1] Cascade Insert
    private static void insertDemoDataInteractive() {
        printHeader("Thêm mới Phòng chiếu");

        System.out.print("Nhập tên phòng chiếu (ví dụ: IMAX Room 01): ");
        String roomName = scanner.nextLine().trim();
        if (roomName.isEmpty()) {
            printError("Tên phòng chiếu không được để trống!");
            return;
        }

        System.out.print("Nhập số lượng ghế (ví dụ: 2): ");
        int seatQuantity;
        try {
            seatQuantity = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("Số lượng ghế phải là số nguyên!");
            return;
        }

        System.out.print("Nhập giá vé phòng chiếu (ví dụ: 150000): ");
        int roomRate;
        try {
            roomRate = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("Giá vé phải là số nguyên!");
            return;
        }

        System.out.print("Nhập ngày hoạt động (yyyy-MM-dd, để trống sẽ lấy ngày hôm nay): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate activeDate = LocalDate.now();
        if (!dateStr.isEmpty()) {
            try {
                activeDate = LocalDate.parse(dateStr, dateFormatter);
            } catch (DateTimeParseException e) {
                printError("Định dạng ngày không hợp lệ! Sử dụng mặc định là ngày hôm nay.");
            }
        }

        System.out.print("Nhập mô tả phòng chiếu: ");
        String description = scanner.nextLine().trim();

        // Khởi tạo các đối tượng và thiết lập quan hệ
        CinemaRoom room = new CinemaRoom();
        room.setCinemaRoomName(roomName);
        room.setSeatQuantity(seatQuantity);

        CinemaRoomDetail detail = new CinemaRoomDetail();
        detail.setRoomRate(roomRate);
        detail.setActiveDate(activeDate);
        detail.setRoomDescription(description);
        detail.setCinemaRoom(room);
        room.setCinemaRoomDetail(detail);

        // Tạo danh sách ghế demo dựa trên số lượng ghế
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= seatQuantity; i++) {
            Seat seat = new Seat();
            seat.setSeatRow(1);
            seat.setSeatColumn("A" + i);
            seat.setSeatStatus("Available");
            seat.setSeatType(i % 2 == 0 ? "VIP" : "Normal");
            seat.setCinemaRoom(room);
            seats.add(seat);
        }
        room.setSeats(seats);

        printInfo("Đang lưu phòng chiếu và các thực thể liên quan...");
        roomDao.insertCinemaRoom(room);
        printSuccess("Thêm phòng chiếu thành công! ID phòng chiếu: " + room.getCinemaRoomId());
    }

    // [2] Display All
    private static void displayAllData() {
        printHeader("Danh sách toàn bộ Phòng chiếu, Chi tiết & Ghế");
        List<CinemaRoom> rooms = roomDao.getAllCinemaRoom();
        if (rooms == null || rooms.isEmpty()) {
            printInfo("Không có phòng chiếu nào trong cơ sở dữ liệu.");
            return;
        }

        for (CinemaRoom room : rooms) {
            System.out.println("\n=== PHÒNG CHIẾU: " + room.getCinemaRoomName() + " (ID: " + room.getCinemaRoomId() + ") ===");
            System.out.println("  • Số lượng ghế định mức: " + room.getSeatQuantity());

            CinemaRoomDetail detail = room.getCinemaRoomDetail();
            if (detail != null) {
                System.out.println("  • Chi tiết Phòng:");
                System.out.println("    - Giá vé: " + String.format("%,d VNĐ", detail.getRoomRate()));
                System.out.println("    - Ngày hoạt động: " + (detail.getActiveDate() != null ? detail.getActiveDate().format(dateFormatter) : "N/A"));
                System.out.println("    - Mô tả: " + detail.getRoomDescription());
            } else {
                System.out.println("  • Chi tiết Phòng: Không có thông tin.");
            }

            List<Seat> seats = room.getSeats();
            if (seats != null && !seats.isEmpty()) {
                System.out.println("  • Sơ đồ ghế:");
                System.out.printf("    %-10s | %-10s | %-10s | %-15s | %-12s\n", "ID Ghế", "Hàng", "Cột", "Trạng thái", "Loại ghế");
                System.out.println("    ---------------------------------------------------------------------");
                for (Seat seat : seats) {
                    System.out.printf("    %-10d | %-10d | %-10s | %-15s | %-12s\n",
                            seat.getSeatId(),
                            seat.getSeatRow(),
                            seat.getSeatColumn(),
                            seat.getSeatStatus(),
                            seat.getSeatType()
                    );
                }
            } else {
                System.out.println("  • Danh sách ghế: Chưa cấu hình ghế.");
            }
        }
    }

    // [3] Find Room by ID
    private static void findCinemaRoomById() {
        printHeader("Tìm kiếm phòng chiếu");
        System.out.print("Nhập ID phòng chiếu cần tìm: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        CinemaRoom room = roomDao.getCinemaRoomById(id);
        if (room != null) {
            printSuccess("Tìm thấy phòng chiếu!");
            System.out.println("  - Tên phòng: " + room.getCinemaRoomName());
            System.out.println("  - Số lượng ghế: " + room.getSeatQuantity());
            if (room.getCinemaRoomDetail() != null) {
                System.out.println("  - Giá vé: " + String.format("%,d VNĐ", room.getCinemaRoomDetail().getRoomRate()));
                System.out.println("  - Mô tả: " + room.getCinemaRoomDetail().getRoomDescription());
            }
        } else {
            printError("Không tìm thấy phòng chiếu nào với ID = " + id);
        }
    }

    // [4] Find Seat by ID
    private static void findSeatById() {
        printHeader("Tìm kiếm ghế ngồi");
        System.out.print("Nhập ID ghế cần tìm: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        Seat seat = seatDao.getSeatById(id);
        if (seat != null) {
            printSuccess("Tìm thấy ghế ngồi!");
            System.out.println("  - ID Ghế: " + seat.getSeatId());
            System.out.println("  - Vị trí: Hàng " + seat.getSeatRow() + ", Cột " + seat.getSeatColumn());
            System.out.println("  - Trạng thái: " + seat.getSeatStatus());
            System.out.println("  - Loại ghế: " + seat.getSeatType());
            if (seat.getCinemaRoom() != null) {
                System.out.println("  - Thuộc phòng chiếu: " + seat.getCinemaRoom().getCinemaRoomName() + " (ID: " + seat.getCinemaRoom().getCinemaRoomId() + ")");
            }
        } else {
            printError("Không tìm thấy ghế ngồi nào với ID = " + id);
        }
    }

    // [5] Update Room
    private static void updateCinemaRoom() {
        printHeader("Cập nhật thông tin phòng chiếu");
        System.out.print("Nhập ID phòng chiếu cần cập nhật: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        CinemaRoom room = roomDao.getCinemaRoomById(id);
        if (room == null) {
            printError("Không tìm thấy phòng chiếu ID = " + id);
            return;
        }

        System.out.print("Nhập tên mới (để trống nếu giữ nguyên [" + room.getCinemaRoomName() + "]): ");
        String newName = scanner.nextLine().trim();
        if (!newName.isEmpty()) {
            room.setCinemaRoomName(newName);
        }

        System.out.print("Nhập số lượng ghế mới (để trống nếu giữ nguyên [" + room.getSeatQuantity() + "]): ");
        String quantityStr = scanner.nextLine().trim();
        if (!quantityStr.isEmpty()) {
            try {
                room.setSeatQuantity(Integer.parseInt(quantityStr));
            } catch (NumberFormatException e) {
                printError("Số lượng ghế không hợp lệ! Giữ nguyên giá trị cũ.");
            }
        }

        printInfo("Đang cập nhật cơ sở dữ liệu...");
        roomDao.updateCinemaRoomById(room);
        printSuccess("Cập nhật thông tin phòng chiếu thành công!");
    }

    // [6] Update Seat
    private static void updateSeat() {
        printHeader("Cập nhật thông tin ghế");
        System.out.print("Nhập ID ghế cần cập nhật: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        Seat seat = seatDao.getSeatById(id);
        if (seat == null) {
            printError("Không tìm thấy ghế ID = " + id);
            return;
        }

        System.out.println("Chọn trạng thái ghế mới:");
        System.out.println("  1. Available");
        System.out.println("  2. Not Available");
        System.out.println("  3. Booked");
        System.out.print("Nhập lựa chọn (để trống nếu giữ nguyên [" + seat.getSeatStatus() + "]): ");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) seat.setSeatStatus("Available");
        else if ("2".equals(choice)) seat.setSeatStatus("Not Available");
        else if ("3".equals(choice)) seat.setSeatStatus("Booked");

        System.out.print("Nhập loại ghế mới VIP/Normal (để trống nếu giữ nguyên [" + seat.getSeatType() + "]): ");
        String newType = scanner.nextLine().trim();
        if (!newType.isEmpty()) {
            seat.setSeatType(newType);
        }

        printInfo("Đang cập nhật ghế...");
        seatDao.updateSeatById(seat);
        printSuccess("Cập nhật ghế thành công!");
    }

    // [7] Delete Seat
    private static void deleteSeat() {
        printHeader("Xóa ghế đơn lẻ");
        System.out.print("Nhập ID ghế muốn xóa: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        Seat seat = seatDao.getSeatById(id);
        if (seat == null) {
            printError("Không tìm thấy ghế ID = " + id);
            return;
        }

        printInfo("Đang xóa ghế...");
        seatDao.deleteSeatById(id);
        printSuccess("Đã xóa ghế thành công!");
    }

    // [8] Delete Cinema Room (Cascade Delete)
    private static void deleteCinemaRoom() {
        printHeader("Xóa phòng chiếu");
        System.out.print("Nhập ID phòng chiếu muốn xóa: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            printError("ID phải là số nguyên!");
            return;
        }

        CinemaRoom room = roomDao.getCinemaRoomById(id);
        if (room == null) {
            printError("Không tìm thấy phòng chiếu ID = " + id);
            return;
        }

        System.out.print("CẢNH BÁO: Xóa phòng chiếu này sẽ xóa sạch Chi tiết phòng và toàn bộ Ghế liên quan. Xác nhận? (Y/N): ");
        String confirm = scanner.nextLine().trim();
        if ("Y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
            printInfo("Đang xóa phòng chiếu...");
            roomDao.deleteCinemaRoomById(id);
            printSuccess("Đã xóa phòng chiếu và toàn bộ các thực thể phụ thuộc thành công!");
        } else {
            printInfo("Đã hủy bỏ thao tác xóa.");
        }
    }

    // [9] Run Automated Test Suite
    private static void runAutomatedTestSuite() {
        printHeader("Chạy kịch bản tự động kiểm thử toàn diện (CRUD)");

        // 1. CREATE
        printInfo("BƯỚC 1: Thực hiện CASCADE INSERT");
        CinemaRoom room = new CinemaRoom();
        room.setCinemaRoomName("Gold Class Room AutoTest");
        room.setSeatQuantity(2);

        CinemaRoomDetail detail = new CinemaRoomDetail();
        detail.setRoomRate(250000);
        detail.setActiveDate(LocalDate.now());
        detail.setRoomDescription("Mô tả kịch bản tự động");
        detail.setCinemaRoom(room);
        room.setCinemaRoomDetail(detail);

        List<Seat> seats = new ArrayList<>();
        Seat s1 = new Seat();
        s1.setSeatRow(1);
        s1.setSeatColumn("A1");
        s1.setSeatStatus("Available");
        s1.setSeatType("VIP");
        s1.setCinemaRoom(room);

        Seat s2 = new Seat();
        s2.setSeatRow(1);
        s2.setSeatColumn("A2");
        s2.setSeatStatus("Booked");
        s2.setSeatType("Normal");
        s2.setCinemaRoom(room);

        seats.add(s1);
        seats.add(s2);
        room.setSeats(seats);

        roomDao.insertCinemaRoom(room);
        int roomId = room.getCinemaRoomId();
        int detailId = room.getCinemaRoomDetail().getCinemaRoomDetailId();
        int seatId = room.getSeats().get(0).getSeatId();
        printSuccess("Thêm dữ liệu tự động thành công. Room ID: " + roomId + ", Detail ID: " + detailId + ", Seat ID: " + seatId);

        // 2. READ
        printInfo("BƯỚC 2: Kiểm tra truy vấn READ");
        CinemaRoom checkRoom = roomDao.getCinemaRoomById(roomId);
        if (checkRoom != null && checkRoom.getCinemaRoomName().equals("Gold Class Room AutoTest")) {
            printSuccess("-> Đọc CinemaRoom thành công!");
        } else {
            printError("-> Đọc CinemaRoom thất bại!");
        }

        CinemaRoomDetail checkDetail = detailDao.getCinemaRoomDetailById(detailId);
        if (checkDetail != null && checkDetail.getRoomRate() == 250000) {
            printSuccess("-> Đọc CinemaRoomDetail thành công!");
        } else {
            printError("-> Đọc CinemaRoomDetail thất bại!");
        }

        // 3. UPDATE
        printInfo("BƯỚC 3: Cập nhật UPDATE");
        checkRoom.setCinemaRoomName("Gold Class Room AutoTest - Updated");
        roomDao.updateCinemaRoomById(checkRoom);

        CinemaRoom updatedRoom = roomDao.getCinemaRoomById(roomId);
        if (updatedRoom != null && "Gold Class Room AutoTest - Updated".equals(updatedRoom.getCinemaRoomName())) {
            printSuccess("-> Cập nhật CinemaRoom thành công!");
        } else {
            printError("-> Cập nhật CinemaRoom thất bại!");
        }

        // 4. DELETE
        printInfo("BƯỚC 4: Cascade Delete phòng chiếu");
        roomDao.deleteCinemaRoomById(roomId);

        CinemaRoom deletedRoom = roomDao.getCinemaRoomById(roomId);
        CinemaRoomDetail deletedDetail = detailDao.getCinemaRoomDetailById(detailId);
        Seat deletedSeat = seatDao.getSeatById(seatId);

        if (deletedRoom == null && deletedDetail == null && deletedSeat == null) {
            printSuccess("-> Cascade Delete hoạt động hoàn hảo! Toàn bộ dữ liệu tự động đã được xóa sạch.");
        } else {
            printError("-> Lỗi: Một số dữ liệu liên quan vẫn tồn tại.");
        }
    }
}

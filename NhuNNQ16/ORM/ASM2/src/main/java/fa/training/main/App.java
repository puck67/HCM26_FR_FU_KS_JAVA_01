package fa.training.main;

import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.dao.impl.RoomDaoImpl;
import fa.training.dao.impl.RoomDetailDaoImpl;
import fa.training.dao.impl.SeatDaoImpl;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.util.HibernateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RoomDao roomDao = new RoomDaoImpl();
    private static final RoomDetailDao roomDetailDao = new RoomDetailDaoImpl();
    private static final SeatDao seatDao = new SeatDaoImpl();
    private static boolean running = true;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM (ASM2)        ");
        System.out.println("=================================================");

        Map<String, Runnable> menuActions = new HashMap<>();
        menuActions.put("1", App::handleCreateRoom);
        menuActions.put("2", App::handleCreateRoomDetail);
        menuActions.put("3", App::handleCreateSeatsForRoom);
        menuActions.put("4", App::handleViewAllRooms);
        menuActions.put("5", App::handleSimulateBookingScenario);
        menuActions.put("6", App::handleDeleteRoom);
        menuActions.put("7", App::handleExit);

        while (running) {
            printMenu();
            System.out.print("Nhập lựa chọn của bạn (1-7): ");
            String choice = scanner.nextLine().trim();
            menuActions.getOrDefault(choice, () -> {
                System.out.println("\n[LỖI] Lựa chọn không hợp lệ! Vui lòng nhập số từ 1 đến 7.");
            }).run();
        }
    }

    private static void printMenu() {
        System.out.println("\n-------------------------------------------------");
        System.out.println("1. Tạo Phòng Chiếu Phim Mới");
        System.out.println("2. Thêm Cấu Hình Chi Tiết cho Phòng Chiếu (1:1)");
        System.out.println("3. Tự Động Tạo Ghế cho Phòng Chiếu (1:N)");
        System.out.println("4. Xem Toàn Bộ Phòng Chiếu kèm Chi Tiết & Danh Sách Ghế");
        System.out.println("5. Mô Phỏng Kịch Bản Đặt Ghế (Mô Phỏng Hoạt Động)");
        System.out.println("6. Xóa Phòng Chiếu Phim");
        System.out.println("7. Thoát Ứng Dụng");
        System.out.println("-------------------------------------------------");
    }

    private static void handleCreateRoom() {
        System.out.println("\n>>> TẠO PHÒNG CHIẾU PHIM MỚI <<<");
        System.out.print("Nhập tên phòng chiếu phim: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("[LỖI] Tên phòng chiếu không được để trống!");
            return;
        }

        System.out.print("Nhập số lượng ghế tối đa: ");
        int qty;
        try {
            qty = Integer.parseInt(scanner.nextLine().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Số lượng ghế không hợp lệ. Phải là một số nguyên dương!");
            return;
        }

        CinemaRoom room = new CinemaRoom(name, qty);
        boolean success = roomDao.insertRoom(room);

        Optional.of(success)
                .filter(res -> res)
                .map(res -> "\n[THÀNH CÔNG] Phòng chiếu phim '" + name + "' đã được tạo thành công với ID: " + room.getCinemaRoomId())
                .orElseGet(() -> "\n[LỖI] Thất bại khi lưu Phòng chiếu phim (tên phòng chiếu có thể đã tồn tại).")
                .chars().forEach(c -> System.out.print((char) c));
        System.out.println();
    }

    private static void handleCreateRoomDetail() {
        System.out.println("\n>>> THÊM CHI TIẾT CẤU HÌNH PHÒNG CHIẾU <<<");
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu nào! Vui lòng tạo phòng chiếu trước.");
            return;
        }

        System.out.println("Các phòng chiếu hiện có chưa cấu hình chi tiết:");
        rooms.stream()
                .filter(r -> r.getCinemaRoomDetail() == null)
                .forEach(r -> System.out.println(" - ID: " + r.getCinemaRoomId() + " | Tên: " + r.getCinemaRoomName()));

        System.out.print("Nhập ID phòng để thêm cấu hình chi tiết: ");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] ID phòng không hợp lệ!");
            return;
        }

        CinemaRoom selectedRoom = roomDao.getRoomById(roomId);
        if (selectedRoom == null) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu!");
            return;
        }
        if (selectedRoom.getCinemaRoomDetail() != null) {
            System.out.println("[LỖI] Phòng chiếu đã có cấu hình chi tiết rồi!");
            return;
        }

        System.out.print("Nhập giá phòng (Ví dụ: 120000): ");
        int rate;
        try {
            rate = Integer.parseInt(scanner.nextLine().trim());
            if (rate < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Giá phòng không hợp lệ! Phải là số không âm.");
            return;
        }

        System.out.print("Nhập ngày kích hoạt (yyyy-MM-dd, hoặc nhấn Enter để lấy ngày hôm nay): ");
        String dateStr = scanner.nextLine().trim();
        LocalDate activeDate = dateStr.isEmpty() ? LocalDate.now() : LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);

        System.out.print("Nhập mô tả: ");
        String description = scanner.nextLine().trim();

        CinemaRoomDetail detail = new CinemaRoomDetail(rate, activeDate, description);
        detail.setCinemaRoom(selectedRoom);
        selectedRoom.setCinemaRoomDetail(detail);

        boolean success = roomDetailDao.insertRoomDetail(detail);
        if (success) {
            System.out.println("\n[THÀNH CÔNG] Đã cấu hình chi tiết thành công cho Phòng '" + selectedRoom.getCinemaRoomName() + "'");
        } else {
            System.out.println("\n[LỖI] Thất bại khi lưu chi tiết cấu hình phòng.");
        }
    }

    private static void handleCreateSeatsForRoom() {
        System.out.println("\n>>> TỰ ĐỘNG TẠO GHẾ CHO PHÒNG CHIẾU PHIM <<<");
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu phim nào!");
            return;
        }

        rooms.forEach(r -> System.out.println(" - ID: " + r.getCinemaRoomId() + " | Tên: " + r.getCinemaRoomName() + " (Sức chứa tối đa: " + r.getSeatQuantity() + ", Số ghế hiện tại: " + r.getSeats().size() + ")"));

        System.out.print("Nhập ID phòng để thêm ghế: ");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] ID phòng không hợp lệ!");
            return;
        }

        CinemaRoom room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu phim!");
            return;
        }

        System.out.print("Số lượng ghế cần tạo? (Tối đa " + room.getSeatQuantity() + "): ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine().trim());
            if (count <= 0 || count > room.getSeatQuantity()) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] Số lượng ghế không hợp lệ!");
            return;
        }

        System.out.println("Đang tạo " + count + " ghế...");
        String[] columns = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        for (int i = 0; i < count; i++) {
            int row = (i / 10) + 1;
            String col = columns[i % 10];
            String type = (row >= 5) ? "VIP" : "Thường"; // Hàng từ số 5 trở đi là ghế VIP
            
            Seat seat = new Seat(col, row, "Available", type);
            room.addSeat(seat);
        }

        boolean success = roomDao.updateRoomById(room);
        if (success) {
            System.out.println("\n[THÀNH CÔNG] Đã tạo và lưu thành công " + count + " ghế cho Phòng '" + room.getCinemaRoomName() + "'");
        } else {
            System.out.println("\n[LỖI] Thất bại khi lưu danh sách ghế.");
        }
    }

    private static void handleViewAllRooms() {
        System.out.println("\n>>> PHÒNG CHIẾU PHIM VÀ THÔNG TIN LIÊN KẾT <<<");
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("[THÔNG TIN] Không có phòng chiếu phim nào trong cơ sở dữ liệu.");
            return;
        }

        rooms.forEach(room -> {
            System.out.println("\n=================================================");
            System.out.println("PHÒNG CHIẾU: " + room.getCinemaRoomName().toUpperCase() + " (ID: " + room.getCinemaRoomId() + ")");
            System.out.println("Sức chứa ghế tối đa: " + room.getSeatQuantity());
            System.out.println("-------------------------------------------------");
            
            Optional.ofNullable(room.getCinemaRoomDetail())
                    .ifPresent(d -> {
                        System.out.println("Cấu hình chi tiết phòng chiếu:");
                        System.out.println("  * Giá phòng: " + d.getRoomRate() + " VND");
                        System.out.println("  * Ngày kích hoạt: " + d.getActiveDate());
                        System.out.println("  * Mô tả: " + d.getRoomDescription());
                    });
            if (room.getCinemaRoomDetail() == null) {
                System.out.println("Cấu hình chi tiết phòng chiếu: [Chưa Cấu Hình]");
            }

            long totalSeats = room.getSeats().size();
            long vipCount = room.getSeats().stream().filter(s -> "VIP".equalsIgnoreCase(s.getSeatType())).count();
            long bookedCount = room.getSeats().stream().filter(s -> "Booked".equalsIgnoreCase(s.getSeatStatus())).count();
            long availableCount = room.getSeats().stream().filter(s -> "Available".equalsIgnoreCase(s.getSeatStatus())).count();

            System.out.println("Tóm tắt số lượng ghế:");
            System.out.println("  * Tổng số ghế đã tạo: " + totalSeats);
            System.out.println("  * Số ghế VIP: " + vipCount);
            System.out.println("  * Đã đặt: " + bookedCount);
            System.out.println("  * Còn trống: " + availableCount);

            if (totalSeats > 0) {
                System.out.print("  * Sơ đồ ghế: ");
                room.getSeats().stream()
                        .sorted(Comparator.comparingInt(Seat::getSeatRow).thenComparing(Seat::getSeatColumn))
                        .forEach(s -> {
                            String statusChar = "Available".equalsIgnoreCase(s.getSeatStatus()) ? "O" : "X";
                            System.out.print(s.getSeatColumn() + s.getSeatRow() + "[" + statusChar + "] ");
                        });
                System.out.println();
            }
        });
    }

    private static void handleSimulateBookingScenario() {
        System.out.println("\n>>> MÔ PHỎNG KỊCH BẢN ĐẶT GHẾ <<<");
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu phim nào!");
            return;
        }

        rooms.stream()
                .filter(r -> !r.getSeats().isEmpty())
                .forEach(r -> System.out.println(" - ID: " + r.getCinemaRoomId() + " | Phòng: " + r.getCinemaRoomName() + " (" + r.getSeats().size() + " ghế)"));

        System.out.print("Nhập ID phòng chiếu phim để mô phỏng đặt ghế: ");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] ID phòng không hợp lệ!");
            return;
        }

        CinemaRoom room = roomDao.getRoomById(roomId);
        if (room == null || room.getSeats().isEmpty()) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu hoặc phòng này chưa có ghế nào được tạo!");
            return;
        }

        System.out.println("\nCác ghế còn trống trong phòng '" + room.getCinemaRoomName() + "':");

        room.getSeats().stream()
                .filter(seat -> "Available".equalsIgnoreCase(seat.getSeatStatus()))
                .sorted(Comparator.comparingInt(Seat::getSeatRow).thenComparing(Seat::getSeatColumn))
                .forEach(seat -> System.out.println("  * ID Ghế: " + seat.getSeatId() + " | Hàng (Row) " + seat.getSeatRow() + ", Cột (Col) " + seat.getSeatColumn() + " | Loại: " + ("VIP".equalsIgnoreCase(seat.getSeatType()) ? "VIP" : "Thường")));

        long availableCount = room.getSeats().stream().filter(seat -> "Available".equalsIgnoreCase(seat.getSeatStatus())).count();
        if (availableCount == 0) {
            System.out.println("[THÔNG TIN] Tất cả các ghế trong phòng đã được đặt hết!");
            return;
        }

        System.out.print("Nhập ID Ghế muốn đặt: ");
        int seatId;
        try {
            seatId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] ID ghế không hợp lệ!");
            return;
        }

        // Tìm ghế sử dụng bộ lọc Java 8 Stream
        Optional<Seat> targetSeat = room.getSeats().stream()
                .filter(s -> s.getSeatId() == seatId)
                .filter(s -> "Available".equalsIgnoreCase(s.getSeatStatus()))
                .findFirst();

        if (targetSeat.isPresent()) {
            Seat seat = targetSeat.get();
            seat.setSeatStatus("Booked");
            boolean success = seatDao.updateSeatById(seat);
            if (success) {
                System.out.println("\n[THÀNH CÔNG] Đã đặt thành công Ghế ID " + seatId + " (" + seat.getSeatColumn() + seat.getSeatRow() + " - " + ("VIP".equalsIgnoreCase(seat.getSeatType()) ? "VIP" : "Thường") + ")");
            } else {
                System.out.println("\n[LỖI] Giao dịch thất bại khi cập nhật trạng thái ghế.");
            }
        } else {
            System.out.println("\n[LỖI] Ghế này không còn trống hoặc không tồn tại!");
        }
    }

    private static void handleDeleteRoom() {
        System.out.println("\n>>> XÓA PHÒNG CHIẾU PHIM (CASCADING DEMO) <<<");
        List<CinemaRoom> rooms = roomDao.getAllRooms();
        if (rooms == null || rooms.isEmpty()) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu nào.");
            return;
        }

        rooms.forEach(r -> System.out.println(" - ID: " + r.getCinemaRoomId() + " | Tên: " + r.getCinemaRoomName()));

        System.out.print("Nhập ID phòng chiếu muốn xóa: ");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[LỖI] ID phòng không hợp lệ!");
            return;
        }

        CinemaRoom room = roomDao.getRoomById(roomId);
        if (room == null) {
            System.out.println("[LỖI] Không tìm thấy phòng chiếu!");
            return;
        }

        System.out.print("CẢNH BÁO: Xóa phòng chiếu này sẽ tự động xóa tất cả các ghế và thông tin chi tiết liên quan! Tiếp tục? (y/n): ");
        String confirm = scanner.nextLine().trim();
        if ("y".equalsIgnoreCase(confirm)) {
            boolean success = roomDao.deleteRoomById(roomId);
            if (success) {
                System.out.println("\n[THÀNH CÔNG] Đã xóa thành công Phòng ID " + roomId + " cùng toàn bộ Ghế và Chi tiết cấu hình liên kết (Cascade)!");
            } else {
                System.out.println("\n[LỖI] Thất bại khi xóa phòng chiếu.");
            }
        } else {
            System.out.println("\n[THÔNG TIN] Đã hủy bỏ thao tác xóa.");
        }
    }

    private static void handleExit() {
        System.out.println("\nĐang tắt Hibernate Session Factory...");
        HibernateUtil.shutdown();
        System.out.println("Tạm biệt! Cảm ơn bạn đã sử dụng Hệ thống Quản lý Rạp Chiếu Phim!");
        running = false;
    }
}

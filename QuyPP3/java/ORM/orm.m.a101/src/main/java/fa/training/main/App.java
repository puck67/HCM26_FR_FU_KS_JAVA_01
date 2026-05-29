package fa.training.main;

import fa.training.config.HibernateUtils;
import fa.training.dao.RoomDao;
import fa.training.dao.RoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class App {
    private static final RoomDao roomDao = new RoomDao();
    private static final RoomDetailDao detailDao = new RoomDetailDao();
    private static final SeatDao seatDao = new SeatDao();

    public static void main(String[] args) {
        System.out.println("--- KHỞI TẠO HỆ THỐNG MOVIE THEATER (H2 IN-MEMORY) ---");
        // Trigger tải session factory tạo bảng tự động
        HibernateUtils.getSessionFactory();

        boolean running = true;
        while (running) {
            System.out.println("\n================ MOVIE THEATER APPLICATION ================");
            System.out.println("1. Thêm phòng chiếu mới (kèm chi tiết & tự động tạo ghế)");
            System.out.println("2. Hiển thị danh sách tất cả các phòng chiếu");
            System.out.println("3. Cập nhật thông tin phòng chiếu");
            System.out.println("4. Tìm kiếm ghế ngồi theo ID");
            System.out.println("5. Cập nhật trạng thái ghế ngồi (Đặt ghế / Huỷ ghế)");
            System.out.println("6. Xoá phòng chiếu (Cascade tự động xoá chi tiết và ghế)");
            System.out.println("7. Thoát chương trình");
            System.out.println("===========================================================");

            int choice = InputValidator.readInt("Lựa chọn chức năng của bạn (1-7): ", 1, 7);

            switch (choice) {
                case 1 -> createNewTheaterScenario();
                case 2 -> showAllRooms();
                case 3 -> updateRoomInfo();
                case 4 -> findSeatById();
                case 5 -> updateSeatStatus();
                case 6 -> deleteRoomScenario();
                case 7 -> {
                    System.out.println("Đang tắt ứng dụng và giải phóng tài nguyên database...");
                    HibernateUtils.shutdown();
                    running = false;
                    System.out.println("Tạm biệt!");
                }
            }
        }
    }

    private static void createNewTheaterScenario() {
        System.out.println("\n--- 1. TẠO PHÒNG CHIẾU MỚI MẪU ---");
        String name = InputValidator.readString("Nhập tên phòng chiếu (Ví dụ: Room 01 Deluxe): ");
        int qty = InputValidator.readInt("Nhập số lượng ghế trong phòng (1 - 100): ", 1, 100);

        CinemaRoom room = new CinemaRoom(name, qty);

        System.out.println("\n--- Nhập thông tin chi tiết phòng ---");
        int rate = InputValidator.readInt("Nhập giá vé cơ bản (VND): ", 10000, 1000000);
        LocalDate activeDate = InputValidator.readDate("Nhập ngày hoạt động (Định dạng YYYY-MM-DD): ");
        String desc = InputValidator.readString("Nhập mô tả phòng: ");

        CinemaRoomDetail detail = new CinemaRoomDetail(rate, activeDate, desc);
        room.setCinemaRoomDetail(detail);

        // Mô phỏng tự động sinh các dãy ghế dựa trên số lượng ghế yêu cầu
        for (int i = 1; i <= qty; i++) {
            String col = (i % 2 == 0) ? "B" : "A";
            String type = (i > qty / 2) ? "VIP" : "Normal";
            Seat seat = new Seat(col, i, "Available", type);
            room.addSeat(seat);
        }

        roomDao.insertRoom(room);
        System.out.println("Tạo thành công phòng chiếu, bảng chi tiết phòng và " + qty + " ghế ngồi tương ứng!");
    }

    private static void showAllRooms() {
        System.out.println("\n--- 2. DANH SÁCH PHÒNG CHIẾU ---");
        List<CinemaRoom> list = roomDao.getAllRooms();
        if (list.isEmpty()) {
            System.out.println("Hiện tại chưa có phòng chiếu nào trong hệ thống.");
            return;
        }
        list.forEach(r -> {
            System.out.println(r);
            if (r.getCinemaRoomDetail() != null) {
                System.out.println("  └─ " + r.getCinemaRoomDetail());
            }
            System.out.println("  └─ Tổng số ghế thực tế trong DB: " + r.getSeats().size());
        });
    }

    private static void updateRoomInfo() {
        System.out.println("\n--- 3. CẬP NHẬT THÔNG TIN PHÒNG ---");
        int roomId = InputValidator.readInt("Nhập ID phòng cần sửa đổi: ", 1, Integer.MAX_VALUE);
        CinemaRoom room = roomDao.getRoomById(roomId);

        if (room == null) {
            System.out.println("Lỗi: Không tìm thấy phòng chiếu mang ID: " + roomId);
            return;
        }

        String newName = InputValidator.readString("Nhập tên mới của phòng chiếu: ");
        room.setCinemaRoomName(newName);
        roomDao.updateRoom(room);
        System.out.println("Cập nhật tên phòng thành công!");
    }

    private static void findSeatById() {
        System.out.println("\n--- 4. TÌM KIẾM GHẾ THEO ID ---");
        int seatId = InputValidator.readInt("Nhập mã ID ghế cần tìm: ", 1, Integer.MAX_VALUE);
        Seat seat = seatDao.getSeatById(seatId);

        if (seat == null) {
            System.out.println("Lỗi: Không tìm thấy ghế ngồi mang ID tương ứng.");
        } else {
            System.out.println("Kết quả tìm kiếm: " + seat);
            System.out.println("  Thuộc về: " + seat.getCinemaRoom().getCinemaRoomName());
        }
    }

    private static void updateSeatStatus() {
        System.out.println("\n--- 5. THAY ĐỔI TRẠNG THÁI GHẾ NGỒI ---");
        int seatId = InputValidator.readInt("Nhập ID ghế cần thao tác: ", 1, Integer.MAX_VALUE);
        Seat seat = seatDao.getSeatById(seatId);

        if (seat == null) {
            System.out.println("Lỗi: Không tồn tại ghế ngồi mang ID: " + seatId);
            return;
        }

        System.out.println("Trạng thái hiện tại: " + seat.getSeatStatus());
        String status = InputValidator.readValidOptions(
                "Chọn trạng thái mới (Available, Not Available, Booked): ",
                "Available", "Not Available", "Booked"
        );

        seat.setSeatStatus(status);
        seatDao.updateSeat(seat);
        System.out.println("Trạng thái ghế ngồi đã được cập nhật thành công thành: " + status);
    }

    private static void deleteRoomScenario() {
        System.out.println("\n--- 6. XÓA PHÒNG CHIẾU ---");
        int roomId = InputValidator.readInt("Nhập ID phòng muốn xóa khỏi hệ thống: ", 1, Integer.MAX_VALUE);
        CinemaRoom room = roomDao.getRoomById(roomId);

        if (room == null) {
            System.out.println("Lỗi: Không tìm thấy thực thể phòng chiếu cần xóa.");
            return;
        }

        roomDao.deleteRoomById(roomId);
        System.out.println("Đã xóa phòng chiếu thành công! Nhờ thiết lập cơ chế Cascade, toàn bộ dữ liệu ghế ngồi và chi tiết phòng liên quan đều đã được giải phóng sạch sẽ.");
    }
}

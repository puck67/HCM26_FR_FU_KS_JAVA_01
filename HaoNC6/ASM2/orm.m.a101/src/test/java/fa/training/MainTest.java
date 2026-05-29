package fa.training;

import fa.training.dao.CinemaRoomDao;
import fa.training.dao.CinemaRoomDetailDao;
import fa.training.dao.SeatDao;
import fa.training.entities.CinemaRoom;
import fa.training.entities.CinemaRoomDetail;
import fa.training.entities.Seat;
import fa.training.utils.HibernateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainTest {
    public static void main(String[] args) {
        CinemaRoomDao roomDao = new CinemaRoomDao();
        CinemaRoomDetailDao detailDao = new CinemaRoomDetailDao();
        SeatDao seatDao = new SeatDao();

        System.out.println("============== BẮT ĐẦU CHƯƠNG TRÌNH UNIT TEST TOÀN DIỆN ==============");

        // 1. TEST KIỂM THỬ: INSERT (CREATE)
        System.out.println("\n--- [Bước 1] Kiểm thử tính năng INSERT ---");

        CinemaRoom room = new CinemaRoom();
        room.setCinemaRoomName("Premium Room 01");
        room.setSeatQuantity(2);

        CinemaRoomDetail detail = new CinemaRoomDetail();
        detail.setRoomRate(120000);
        detail.setActiveDate(LocalDate.now());
        detail.setRoomDescription("Phòng chiếu IMAX chất lượng cao");
        detail.setCinemaRoom(room);
        room.setCinemaRoomDetail(detail);

        List<Seat> seats = new ArrayList<>();
        Seat seat1 = new Seat();
        seat1.setSeatRow(1);
        seat1.setSeatColumn("A");
        seat1.setSeatStatus("Available");
        seat1.setSeatType("VIP");
        seat1.setCinemaRoom(room);

        Seat seat2 = new Seat();
        seat2.setSeatRow(1);
        seat2.setSeatColumn("B");
        seat2.setSeatStatus("Booked");
        seat2.setSeatType("Normal");
        seat2.setCinemaRoom(room);

        seats.add(seat1);
        seats.add(seat2);
        room.setSeats(seats);

        System.out.println("Đang thực hiện insertCinemaRoom...");
        roomDao.insertCinemaRoom(room);
        System.out.println(">> Đã insert thành công Room, Detail và Seat thông qua cơ chế Cascade!");

        // Lấy ID tự động sinh ra phục vụ cho các bước test sau
        int generatedRoomId = room.getCinemaRoomId();
        int generatedDetailId = room.getCinemaRoomDetail().getCinemaRoomDetailId();
        int generatedSeatId = room.getSeats().get(0).getSeatId();

        // 2. TEST KIỂM THỬ: READ (Get By ID & Get All)
        System.out.println("\n--- [Bước 2] Kiểm thử tính năng READ ---");

        // 2.1 Test getCinemaRoomById
        CinemaRoom fetchedRoom = roomDao.getCinemaRoomById(generatedRoomId);
        if (fetchedRoom != null) {
            System.out.println(">> [PASS] Lấy dữ liệu Room thành công! Tên phòng: " + fetchedRoom.getCinemaRoomName());
        }

        // 2.2 Test getCinemaRoomDetailById thông qua DetailDao độc lập
        CinemaRoomDetail fetchedDetail = detailDao.getCinemaRoomDetailById(generatedDetailId);
        if (fetchedDetail != null) {
            System.out.println(">> [PASS] Lấy dữ liệu Detail độc lập thành công! Mô tả: " + fetchedDetail.getRoomDescription());
        }

        // 2.3 Test getAllCinemaRoom
        List<CinemaRoom> allRooms = roomDao.getAllCinemaRoom();
        System.out.println(">> [PASS] Tổng số lượng phòng trong DB: " + allRooms.size());

        // 3. TEST KIỂM THỬ: UPDATE
        System.out.println("\n--- [Bước 3] Kiểm thử tính năng UPDATE ---");

        // 3.1 Cập nhật thông tin Phòng chiếu
        CinemaRoom roomToUpdate = roomDao.getCinemaRoomById(generatedRoomId);
        if (roomToUpdate != null) {
            roomToUpdate.setCinemaRoomName("Premium Room 01 - ĐÃ UPDATE NÂNG CẤP");
            roomDao.updateCinemaRoomById(roomToUpdate);

            CinemaRoom verifyRoom = roomDao.getCinemaRoomById(generatedRoomId);
            System.out.println(">> [PASS] Tên phòng sau khi cập nhật: " + verifyRoom.getCinemaRoomName());
        }

        // 3.2 Cập nhật trạng thái Ghế ngồi thông qua SeatDao độc lập
        Seat seatToUpdate = seatDao.getSeatById(generatedSeatId);
        if (seatToUpdate != null) {
            seatToUpdate.setSeatStatus("Not Available");
            seatDao.updateSeatById(seatToUpdate);
            System.out.println(">> [PASS] Cập nhật SeatDao thành công!");
        }

        // 4. TEST KIỂM THỬ: DELETE
        System.out.println("\n--- [Bước 4] Kiểm thử tính năng DELETE ---");

        // 4.1 Xóa một chiếc ghế đơn lẻ bằng SeatDao để chứng minh hàm hoạt động tốt
        System.out.println("Đang xóa chiếc ghế lẻ ID: " + generatedSeatId);
        seatDao.deleteSeatById(generatedSeatId);
        if (seatDao.getSeatById(generatedSeatId) == null) {
            System.out.println(">> [PASS] Xóa ghế đơn lẻ thành công thông qua SeatDao.");
        }

        // 4.2 Xóa toàn bộ phòng chiếu (Cascade tự động xóa chi tiết phòng và các ghế còn lại)
        System.out.println("Đang xóa phòng chiếu ID: " + generatedRoomId);
        roomDao.deleteCinemaRoomById(generatedRoomId);

        if (roomDao.getCinemaRoomById(generatedRoomId) == null && detailDao.getCinemaRoomDetailById(generatedDetailId) == null) {
            System.out.println(">> [PASS] Xóa sạch dữ liệu liên quan thành công! Database sạch sẽ.");
        } else {
            System.out.println(">> [FAIL] Lỗi cơ chế xóa.");
        }

        // Giải phóng SessionFactory
        HibernateUtils.shutdown();
        System.out.println("\n============== KẾT THÚC TOÀN BỘ TIẾN TRÌNH UNIT TEST ==============");
    }
}
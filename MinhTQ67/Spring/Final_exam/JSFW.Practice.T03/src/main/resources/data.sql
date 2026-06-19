-- Thêm dữ liệu trực tiếp vào database bằng SQL
-- spring.sql.init.continue-on-error=true trong properties sẽ bỏ qua nếu data đã tồn tại

INSERT INTO rooms (id, number, type, price, status) VALUES (1, 'R101', 'SINGLE', 100.0, 'AVAILABLE');
INSERT INTO rooms (id, number, type, price, status) VALUES (2, 'R102', 'DOUBLE', 150.0, 'AVAILABLE');
INSERT INTO rooms (id, number, type, price, status) VALUES (3, 'R103', 'DELUXE', 200.0, 'AVAILABLE');
INSERT INTO rooms (id, number, type, price, status) VALUES (4, 'R104', 'TWIN', 150.0, 'AVAILABLE');
INSERT INTO rooms (id, number, type, price, status) VALUES (5, 'R201', 'SINGLE', 100.0, 'MAINTENANCE');

INSERT INTO guests (id, full_name, phone) VALUES (1, 'Nguyễn Văn A', '0901234567');
INSERT INTO guests (id, full_name, phone) VALUES (2, 'Trần Thị B', '0987654321');

INSERT INTO bookings (id, checkin_date, checkout_date, status, total_price, guest_id, room_id) VALUES (1, CURRENT_DATE + 1, CURRENT_DATE + 3, 'CONFIRMED', 220.0, 1, 1);
INSERT INTO bookings (id, checkin_date, checkout_date, status, total_price, guest_id, room_id) VALUES (2, CURRENT_DATE - 1, CURRENT_DATE + 1, 'CHECKED_IN', 330.0, 2, 2);
INSERT INTO bookings (id, checkin_date, checkout_date, status, total_price, guest_id, room_id) VALUES (3, CURRENT_DATE - 5, CURRENT_DATE - 3, 'CHECKED_OUT', 440.0, 1, 3);

-- Reset identity columns so JPA doesn't try to reuse ID 1 and crash
ALTER TABLE rooms ALTER COLUMN id RESTART WITH 10;
ALTER TABLE guests ALTER COLUMN id RESTART WITH 10;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 10;

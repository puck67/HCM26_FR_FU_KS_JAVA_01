$(document).ready(function() {

    /**
     * Hàm tính toán lại tổng tiền từ tất cả các dòng sản phẩm hiện có trong bảng.
     */
    function calculateGrandTotal() {
        let total = 0;

        // Duyệt qua từng dòng tr trong tbody
        $('#shopping-list tr').each(function() {
            // Lấy giá trị cost được lưu trong thuộc tính data của jQuery
            const cost = $(this).data('cost');
            
            // Cộng dồn vào tổng tiền nếu là số hợp lệ
            if (typeof cost === 'number' && !isNaN(cost)) {
                total += cost;
            }
        });

        // Định dạng và hiển thị số tiền lên giao diện
        $('#grand-total').text('$' + total.toFixed(2));

        // Sử dụng .css() của jQuery để đổi màu chữ sang màu đỏ nếu tổng tiền lớn hơn $500
        if (total > 500) {
            $('#grand-total').css('color', 'red');
        } else {
            // Trở về màu mặc định (xanh dương) khi tổng tiền từ $500 trở xuống
            $('#grand-total').css('color', '#007bff');
        }
    }

    /**
     * Hàm thêm sản phẩm mới
     */
    function addItem() {
        // 1. Nhận giá trị từ các ô nhập liệu bằng jQuery selector $()
        const name = $('#item-name').val().trim();
        const costText = $('#item-cost').val().trim();
        const cost = parseFloat(costText);

        // Lấy thẻ hiển thị thông báo lỗi inline
        const $errorMsg = $('#error-message');

        // Reset thông báo lỗi trước đó
        $errorMsg.text('');

        // 2. Kiểm tra dữ liệu đầu vào (Validation) - Cải tiến dùng thông báo inline thay vì alert()
        if (name === "") {
            $errorMsg.text("Lỗi: Vui lòng nhập tên sản phẩm!");
            $('#item-name').focus();
            return;
        }
        if (costText === "" || isNaN(cost) || cost <= 0) {
            $errorMsg.text("Lỗi: Vui lòng nhập giá tiền hợp lệ (số dương lớn hơn 0)!");
            $('#item-cost').focus();
            return;
        }

        // 3. Tạo dòng mới dạng jQuery object, lưu trữ giá trị cost bằng data-cost
        const $newRow = $(`
            <tr data-cost="${cost}">
                <td>${name}</td>
                <td>$${cost.toFixed(2)}</td>
                <td>
                    <button type="button" class="btn-remove" aria-label="Xóa sản phẩm ${name}">Xóa</button>
                </td>
            </tr>
        `);

        // Gắn dữ liệu kiểu số trực tiếp vào jQuery data() của dòng để tính toán chính xác
        $newRow.data('cost', cost);

        // 4. Thêm dòng mới vào bảng bằng phương thức .append() của jQuery
        $('#shopping-list').append($newRow);

        // 5. Tính toán lại tổng tiền từ đầu dựa trên bảng dữ liệu hiện tại
        calculateGrandTotal();

        // 6. Làm sạch biểu mẫu và focus lại vào ô nhập tên
        $('#item-name').val('');
        $('#item-cost').val('');
        $('#item-name').focus();
    }

    // Đăng ký sự kiện Click cho nút "Thêm" bằng .on()
    $('#btn-add').on('click', function() {
        addItem();
    });

    // Đăng ký sự kiện Click cho nút "Xóa"
    // Sử dụng cơ chế Event Delegation (Ủy quyền sự kiện) bằng cách lắng nghe từ #shopping-list
    $('#shopping-list').on('click', '.btn-remove', function() {
        // Tìm dòng tr chứa nút Xóa vừa click và loại bỏ nó khỏi DOM bằng .remove()
        $(this).closest('tr').remove();

        // Tính toán lại tổng tiền sau khi xóa dòng
        calculateGrandTotal();
    });

    // Chặn sự kiện gửi biểu mẫu mặc định (tránh tải lại trang ngoài ý muốn)
    $('#shopping-form').on('submit', function(event) {
        event.preventDefault();
    });

    // Bắt phím Enter bằng sự kiện keydown (độ tương thích tốt hơn keypress cũ)
    $('#item-name, #item-cost').on('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // Ngăn chặn hành vi submit mặc định của phím Enter trong form
            addItem();
        }
    });
});

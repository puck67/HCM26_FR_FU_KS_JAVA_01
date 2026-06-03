// Khởi tạo xử lý bằng jQuery sau khi DOM sẵn sàng
$(document).ready(function() {
    // Hàm tính toán và cập nhật lại Grand Total theo chuẩn jQuery
    function updateGrandTotalJQ() {
        let total = 0;
        
        // Duyệt qua từng dòng dữ liệu trong bảng để cộng dồn tiền
        $('#tableBody tr').each(function() {
            const costText = $(this).find('td').eq(1).text();
            total += parseFloat(costText) || 0;
        });

        const formattedTotal = `$${total.toFixed(2)}`;
        $('#grandTotal').text(formattedTotal);

        // Yêu cầu đặc tả Problem 03: Nếu vượt quá $500 chuyển chữ sang màu đỏ
        if (total > 500) {
            $('#grandTotal').css('color', '#e53e3e'); // Màu đỏ (Danger)
        } else {
            $('#grandTotal').css('color', '#b7791f'); // Trở lại màu gốc ban đầu
        }
    }

    // Sự kiện thêm item sử dụng jQuery event binding .on('click')
    $('#btnAddItem').on('click', function() {
        const name = $('#itemName').val().trim();
        const cost = parseFloat($('#itemCost').val());

        // Validate dữ liệu đầu vào
        if (name === '') {
            alert('Vui lòng nhập tên sản phẩm (Item Name).');
            return;
        }
        if (isNaN(cost) || cost <= 0) {
            alert('Vui lòng nhập giá tiền hợp lệ và lớn hơn 0 (Item Cost).');
            return;
        }

        // Tạo cấu trúc chuỗi HTML mới và thêm vào cuối bảng bằng .append()
        const newRow = `
            <tr>
                <td>${name}</td>
                <td>${cost.toFixed(2)}</td>
                <td><button class="btn-remove">✕ Remove</button></td>
            </tr>
        `;
        
        $('#tableBody').append(newRow);

        // Reset giá trị các ô nhập liệu
        $('#itemName').val('');
        $('#itemCost').val('');

        // Cập nhật lại tổng tiền
        updateGrandTotalJQ();
    });

    // Sử dụng event delegation với .on('click') để bắt sự kiện của nút Remove động
    $('#tableBody').on('click', '.btn-remove', function() {
        // Sử dụng .remove() để loại bỏ dòng được chọn khỏi DOM
        $(this).closest('tr').remove();
        
        // Recalculate lại tổng số tiền
        updateGrandTotalJQ();
    });

    // Thực hiện tính toán ban đầu cho danh sách mặc định
    updateGrandTotalJQ();
});

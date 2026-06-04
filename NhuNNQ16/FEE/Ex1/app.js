document.addEventListener('DOMContentLoaded', function() {
    // Lấy các phần tử DOM cần thiết
    const shoppingForm = document.getElementById('shopping-form');
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const btnAdd = document.getElementById('btn-add');
    const errorMsg = document.getElementById('error-message');
    const shoppingList = document.getElementById('shopping-list');
    const grandTotal = document.getElementById('grand-total');

    /**
     * Hàm tính toán lại tổng tiền từ tất cả các dòng sản phẩm hiện có trong bảng
     */
    function calculateGrandTotal() {
        let total = 0;
        
        // Lấy tất cả các dòng tr hiện tại trong danh sách
        const rows = shoppingList.querySelectorAll('tr');
        
        rows.forEach(function(row) {
            // Lấy giá trị cost từ thuộc tính dataset
            const cost = parseFloat(row.dataset.cost);
            if (!isNaN(cost)) {
                total += cost;
            }
        });

        // Định dạng và hiển thị số tiền lên giao diện
        grandTotal.textContent = '$' + total.toFixed(2);

        // Đổi màu chữ sang đỏ nếu tổng tiền lớn hơn $500, ngược lại dùng màu chủ đạo xanh dương
        if (total > 500) {
            grandTotal.style.color = 'red';
        } else {
            grandTotal.style.color = '#007bff';
        }
    }

    /**
     * Hàm thêm sản phẩm mới sử dụng pure DOM manipulation
     */
    function addItem() {
        // 1. Nhận và chuẩn hóa giá trị từ các ô nhập liệu
        const name = itemNameInput.value.trim();
        const costText = itemCostInput.value.trim();
        const cost = parseFloat(costText);

        // Reset thông báo lỗi trước đó
        errorMsg.textContent = '';

        // 2. Kiểm tra dữ liệu đầu vào (Validation)
        if (name === "") {
            errorMsg.textContent = "Lỗi: Vui lòng nhập tên sản phẩm!";
            itemNameInput.focus();
            return;
        }
        if (costText === "" || isNaN(cost) || cost <= 0) {
            errorMsg.textContent = "Lỗi: Vui lòng nhập giá tiền hợp lệ (số dương lớn hơn 0)!";
            itemCostInput.focus();
            return;
        }

        // 3. Tạo dòng mới bằng pure DOM manipulation (document.createElement)
        const row = document.createElement('tr');
        // Lưu trữ giá trị cost vào data attribute để phục vụ việc tính tổng tiền
        row.dataset.cost = cost;

        // Tạo cột Tên sản phẩm
        const nameCell = document.createElement('td');
        nameCell.textContent = name;
        row.appendChild(nameCell);

        // Tạo cột Giá tiền
        const costCell = document.createElement('td');
        costCell.textContent = '$' + cost.toFixed(2);
        row.appendChild(costCell);

        // Tạo cột Hành động chứa nút Xóa
        const actionCell = document.createElement('td');
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'btn-remove';
        removeBtn.textContent = 'Xóa';
        removeBtn.setAttribute('aria-label', 'Xóa sản phẩm ' + name);

        // Gán sự kiện click xóa dòng bằng native JS
        removeBtn.addEventListener('click', function() {
            // Loại bỏ dòng khỏi DOM
            row.remove();
            // Tính toán lại tổng tiền sau khi xóa
            calculateGrandTotal();
        });

        actionCell.appendChild(removeBtn);
        row.appendChild(actionCell);

        // 4. Thêm dòng mới vào bảng bằng appendChild
        shoppingList.appendChild(row);

        // 5. Tính toán lại tổng tiền sau khi thêm mới
        calculateGrandTotal();

        // 6. Làm sạch biểu mẫu và focus lại vào ô nhập tên sản phẩm
        itemNameInput.value = '';
        itemCostInput.value = '';
        itemNameInput.focus();
    }

    // Đăng ký sự kiện Click cho nút "Thêm"
    btnAdd.addEventListener('click', function() {
        addItem();
    });

    // Chặn sự kiện gửi biểu mẫu mặc định (tránh tải lại trang ngoài ý muốn)
    shoppingForm.addEventListener('submit', function(event) {
        event.preventDefault();
    });

    // Bắt phím Enter trên các ô nhập liệu để kích hoạt thêm nhanh
    const inputs = [itemNameInput, itemCostInput];
    inputs.forEach(function(input) {
        input.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // Ngăn chặn hành vi submit mặc định
                addItem();
            }
        });
    });
});

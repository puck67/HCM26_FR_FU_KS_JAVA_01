// Xử lý bằng Native JavaScript (Problem 02)
document.addEventListener('DOMContentLoaded', () => {
    const itemNameInput = document.getElementById('itemName');
    const itemCostInput = document.getElementById('itemCost');
    const btnAddItem = document.getElementById('btnAddItem');
    const tableBody = document.getElementById('tableBody');
    const grandTotalElement = document.getElementById('grandTotal');

    // Hàm tính toán và cập nhật Grand Total
    function updateGrandTotal() {
        let total = 0;
        const rows = tableBody.querySelectorAll('tr');
        
        rows.forEach(row => {
            const costText = row.cells[1].textContent;
            total += parseFloat(costText) || 0;
        });

        // Định dạng hiển thị tiền tệ hiển thị 2 chữ số thập phân
        grandTotalElement.textContent = `$${total.toFixed(2)}`;
    }

    // Sự kiện click nút "Add Item"
    btnAddItem.addEventListener('click', () => {
        const name = itemNameInput.value.trim();
        const cost = parseFloat(itemCostInput.value);

        // Validation kiểm tra trống và giá trị dương hợp lệ
        if (name === '') {
            alert('Vui lòng nhập tên sản phẩm (Item Name).');
            return;
        }
        if (isNaN(cost) || cost <= 0) {
            alert('Vui lòng nhập giá tiền hợp lệ và lớn hơn 0 (Item Cost).');
            return;
        }

        // Tạo các phần tử DOM thuần theo yêu cầu đặc tả
        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        tdName.textContent = name;

        const tdCost = document.createElement('td');
        tdCost.textContent = cost.toFixed(2);

        const tdAction = document.createElement('td');
        const btnRemove = document.createElement('button');
        btnRemove.className = 'btn-remove';
        btnRemove.textContent = '✕ Remove';
        tdAction.appendChild(btnRemove);

        // Append vào hàng và bảng
        tr.appendChild(tdName);
        tr.appendChild(tdCost);
        tr.appendChild(tdAction);
        tableBody.appendChild(tr);

        // Xóa trắng input sau khi thêm thành công
        itemNameInput.value = '';
        itemCostInput.value = '';

        // Cập nhật lại tổng tiền
        updateGrandTotal();
    });

    // Ủy quyền sự kiện (Event Delegation) cho nút "Remove" bên trong tbody
    tableBody.addEventListener('click', (event) => {
        if (event.target.classList.contains('btn-remove')) {
            const row = event.target.closest('tr');
            if (row) {
                row.remove(); // Xóa hàng tương ứng khỏi bảng
                updateGrandTotal(); // Tính toán lại tổng tiền
            }
        }
    });

    // Chạy tính toán tổng tiền lần đầu cho dữ liệu mẫu có sẵn
    updateGrandTotal();
});

/**
 * shoppingList.js
 * Reusable Shopping List module – pure DOM manipulation (Problem 02)
 *
 * Usage:
 *   ShoppingList.init({
 *     formId:     'add-item-form',
 *     nameInputId: 'item-name',
 *     costInputId:  'item-cost',
 *     addBtnId:    'btn-add',
 *     tbodyId:     'list-body',
 *     totalId:     'grand-total',
 *     msgId:       'validation-msg',
 *   });
 */

const ShoppingList = (() => {
  // ── Private State per instance ──────────────────────────────────────────
  // Dùng class hoặc constructor function để có thể tạo nhiều instance độc lập
  class ListComponent {
    constructor(cfg) {
      this.nameInput = document.getElementById(cfg.nameInputId);
      this.costInput = document.getElementById(cfg.costInputId);
      this.addBtn    = document.getElementById(cfg.addBtnId);
      this.tbody     = document.getElementById(cfg.tbodyId);
      this.totalEl   = document.getElementById(cfg.totalId);
      this.msgEl     = document.getElementById(cfg.msgId);

      // Quản lý dữ liệu tập trung (State)
      this.items = cfg.initialData || [];

      // HashSet kiểm tra tên trùng lặp (lưu lowercase để so sánh không phân biệt hoa/thường)
      this.nameSet = new Set(this.items.map(i => i.name.trim().toLowerCase()));

      this.initEvents();
      this.render();
    }

    initEvents() {
      if (!this.addBtn) return;

      this.addBtn.addEventListener('click', () => this.addItem());

      this.nameInput.addEventListener('keydown', e => {
        if (e.key === 'Enter') this.addItem();
      });

      // Xóa lỗi của ô name ngay khi người dùng bắt đầu gõ vào
      this.nameInput.addEventListener('input', () => {
        const NAME_ERRORS = [
          'Item Name cannot be empty.',
          'An item with this name already exists!'
        ];
        if (NAME_ERRORS.includes(this.msgEl.textContent) && this.nameInput.value.trim()) {
          this.msgEl.textContent = '';
          this.nameInput.classList.remove('input-error');
        }
      });

      if (this.costInput) {
        this.costInput.addEventListener('keydown', e => {
          if (e.key === 'Enter') { this.addItem(); return; }

          // Cho phép các phím điều hướng / chức năng đi qua bình thường
          const allowed = [
            'Backspace', 'Delete', 'Tab', 'Escape',
            'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
            'Home', 'End'
          ];
          if (allowed.includes(e.key) || e.ctrlKey || e.metaKey) return;

          const val   = this.costInput.value;
          const parts = val.split('.');

          // Nếu đã có dấu chấm và phần thập phân đã đủ 2 chữ số
          if (parts.length > 1 && parts[1].length >= 2) {
            const input = this.costInput;
            const selectionCoversDecimal = input.selectionStart !== input.selectionEnd;

            if (!selectionCoversDecimal && e.key !== '.') {
              e.preventDefault();
              this.msgEl.textContent = 'Only up to 2 decimal places are allowed!';
              this.costInput.classList.add('input-error');
              return;
            }
          }

          // Nếu hợp lệ thì xóa thông báo lỗi decimal (nếu đang hiện)
          if (this.msgEl.textContent === 'Only up to 2 decimal places are allowed!') {
            this.msgEl.textContent = '';
            this.costInput.classList.remove('input-error');
          }
        });

        // Xóa lỗi cost khi người dùng xóa ký tự để về đúng format (dùng input event để bắt Backspace/Delete)
        this.costInput.addEventListener('input', () => {
          if (this.msgEl.textContent !== 'Only up to 2 decimal places are allowed!') return;
          const parts = this.costInput.value.split('.');
          if (!(parts.length > 1 && parts[1].length > 2)) {
            this.msgEl.textContent = '';
            this.costInput.classList.remove('input-error');
          }
        });

        // Xử lý trường hợp paste
        this.costInput.addEventListener('paste', (e) => {
          e.preventDefault();
          const pasted = (e.clipboardData || window.clipboardData).getData('text');
          const num = parseFloat(pasted);
          if (!isNaN(num)) {
            const fixed = (Math.ceil(num * 100) / 100).toFixed(2);
            this.costInput.value = fixed;
          }
        });
      }
    }

    validate(name, cost) {
      if (!name.trim()) return { valid: false, error: 'Item Name cannot be empty.' };
      // Kiểm tra tên trùng lặp qua Set (O(1))
      if (this.nameSet.has(name.trim().toLowerCase())) {
        return { valid: false, error: 'An item with this name already exists!' };
      }
      const num = parseFloat(cost);
      if (cost.trim() === '' || isNaN(num) || num <= 0) {
        return { valid: false, error: 'Item Cost must be a positive number.' };
      }
      // Kiểm tra chỉ cho phép tối đa 2 số sau dấu thập phân
      const parts = cost.trim().split('.');
      if (parts.length > 1 && parts[1].length > 2) {
        return { valid: false, error: 'Item Cost cannot have more than 2 decimal places.' };
      }
      return { valid: true, error: '' };
    }

    addItem() {
      const name = this.nameInput.value;
      const cost = this.costInput.value;

      const { valid, error } = this.validate(name, cost);
      if (!valid) {
        this.msgEl.textContent = error;
        this.nameInput.classList.toggle('input-error', !name.trim());
        this.costInput.classList.toggle('input-error', name.trim() !== '' || !cost.trim() || parseFloat(cost) <= 0 || (cost.split('.')[1] && cost.split('.')[1].length > 2));
        return;
      }

      // Xóa lỗi
      this.msgEl.textContent = '';
      this.nameInput.classList.remove('input-error');
      this.costInput.classList.remove('input-error');

      // Làm tròn LÊN (ceil) số tiền nhập vào về đúng 2 chữ số thập phân (cents)
      const roundedCost = Math.ceil(parseFloat(cost) * 100) / 100;

      // Thêm data vào mảng (State) và cập nhật Set
      const trimmedName = name.trim();
      this.items.push({
        id: Date.now(),
        name: trimmedName,
        cost: roundedCost
      });
      this.nameSet.add(trimmedName.toLowerCase());

      this.render();

      // Reset ô nhập
      this.nameInput.value = '';
      this.costInput.value = '';
      this.nameInput.focus();
    }

    removeItem(id) {
      // Tìm tên item trước khi xóa để xóa khỏi Set
      const removed = this.items.find(item => item.id === id);
      if (removed) this.nameSet.delete(removed.name.toLowerCase());

      // Cập nhật State: lọc bỏ phần tử bị xóa
      this.items = this.items.filter(item => item.id !== id);
      this.render();
    }

    // Tính tổng tiền từ mảng data
    calcTotal() {
      return this.items.reduce((sum, item) => sum + item.cost, 0);
    }

    // Vẽ lại toàn bộ giao diện từ mảng dữ liệu (State)
    render() {
      // 1. Dọn dẹp tbody cũ
      this.tbody.innerHTML = '';

      // 2. Nếu trống, render dòng rỗng
      if (this.items.length === 0) {
        const emptyRow = document.createElement('tr');
        emptyRow.className = 'empty-row';
        const td = document.createElement('td');
        td.colSpan = 3;
        td.textContent = 'No items yet. Add your first item above!';
        emptyRow.appendChild(td);
        this.tbody.appendChild(emptyRow);
      } else {
        // 3. Render các hàng từ dữ liệu
        this.items.forEach(item => {
          const tr = document.createElement('tr');

          const tdName = document.createElement('td');
          tdName.textContent = item.name;

          const tdCost = document.createElement('td');
          tdCost.textContent = `$${item.cost.toFixed(2)}`;

          const tdAction = document.createElement('td');
          const btn = document.createElement('button');
          btn.className = 'btn-remove';
          btn.innerHTML = '&#10005; Remove';
          btn.addEventListener('click', () => this.removeItem(item.id));

          tdAction.appendChild(btn);
          tr.appendChild(tdName);
          tr.appendChild(tdCost);
          tr.appendChild(tdAction);

          this.tbody.appendChild(tr);
        });
      }

      // 4. Cập nhật tổng tiền
      const total = this.calcTotal();
      this.totalEl.textContent = `$${total.toFixed(2)}`;
    }
  }

  // Khởi tạo
  function init(cfg) {
    return new ListComponent(cfg);
  }

  return { init };
})();

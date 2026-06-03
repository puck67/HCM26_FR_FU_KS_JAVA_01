const ShoppingListJQ = (() => {
  class JQListComponent {
    constructor(cfg) {
      this.overBudget = cfg.overBudget ?? 500;

      this.$nameInput = $(`#${cfg.nameInputId}`);
      this.$costInput = $(`#${cfg.costInputId}`);
      this.$addBtn    = $(`#${cfg.addBtnId}`);
      this.$tbody     = $(`#${cfg.tbodyId}`);
      this.$totalEl   = $(`#${cfg.totalId}`);
      this.$msgEl     = $(`#${cfg.msgId}`);

      // Quản lý dữ liệu tập trung (State)
      this.items = cfg.initialData || [];

      // HashSet kiểm tra tên trùng lặp (lưu lowercase)
      this.nameSet = new Set(this.items.map(i => i.name.trim().toLowerCase()));

      this.initEvents();
      this.render();
    }

    initEvents() {
      // jQuery event binding với .on()
      this.$addBtn.on('click', () => this.addItem());

      this.$nameInput.on('keydown', (e) => {
        if (e.key === 'Enter') this.addItem();
      });

      // Xóa lỗi của ô name ngay khi người dùng bắt đầu gõ vào
      this.$nameInput.on('input', () => {
        const NAME_ERRORS = [
          'Item Name cannot be empty.',
          'An item with this name already exists!'
        ];
        if (NAME_ERRORS.includes(this.$msgEl.text()) && this.$nameInput.val().trim()) {
          this.$msgEl.text('');
          this.$nameInput.removeClass('input-error');
        }
      });

      // Ngăn chặn nhập quá 2 số sau dấu phẩy bằng keydown (chặn trước khi ký tự được nhập)
      this.$costInput.on('keydown', (e) => {
        if (e.key === 'Enter') { this.addItem(); return; }

        // Cho phép các phím điều hướng / chức năng đi qua bình thường
        const allowed = [
          'Backspace', 'Delete', 'Tab', 'Escape',
          'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
          'Home', 'End'
        ];
        if (allowed.includes(e.key) || e.ctrlKey || e.metaKey) return;

        const val   = this.$costInput.val();
        const parts = val.split('.');

        // Nếu đã có dấu chấm và phần thập phân đã đủ 2 chữ số
        if (parts.length > 1 && parts[1].length >= 2) {
          const el = this.$costInput[0];
          const selectionCoversDecimal = el.selectionStart !== el.selectionEnd;

          if (!selectionCoversDecimal && e.key !== '.') {
            e.preventDefault();
            this.$msgEl.text('Only up to 2 decimal places are allowed!');
            this.$costInput.addClass('input-error');
            return;
          }
        }

        // Nếu hợp lệ thì xóa thông báo lỗi decimal (nếu đang hiện)
        if (this.$msgEl.text() === 'Only up to 2 decimal places are allowed!') {
          this.$msgEl.text('');
          this.$costInput.removeClass('input-error');
        }
      });

      // Xóa lỗi cost khi người dùng xóa ký tự về đúng format
      this.$costInput.on('input', () => {
        if (this.$msgEl.text() !== 'Only up to 2 decimal places are allowed!') return;
        const parts = this.$costInput.val().split('.');
        if (!(parts.length > 1 && parts[1].length > 2)) {
          this.$msgEl.text('');
          this.$costInput.removeClass('input-error');
        }
      });

      // Xử lý trường hợp paste
      this.$costInput.on('paste', (e) => {
        e.preventDefault();
        const pasted = (e.originalEvent.clipboardData || window.clipboardData).getData('text');
        const num = parseFloat(pasted);
        if (!isNaN(num)) {
          const fixed = (Math.ceil(num * 100) / 100).toFixed(2);
          this.$costInput.val(fixed);
        }
      });
    }

    validate(name, cost) {
      if (!name.trim()) return { valid: false, error: 'Item Name cannot be empty.' };
      // Kiểm tra tên trùng lặp qua Set 
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
      const name = this.$nameInput.val();
      const cost = this.$costInput.val();

      const { valid, error } = this.validate(name, cost);
      if (!valid) {
        this.$msgEl.text(error);
        this.$nameInput.toggleClass('input-error', !name.trim());
        this.$costInput.toggleClass('input-error', name.trim() !== '' || !cost.trim() || parseFloat(cost) <= 0 || (cost.split('.')[1] && cost.split('.')[1].length > 2));
        return;
      }

      // Xóa lỗi
      this.$msgEl.text('');
      this.$nameInput.removeClass('input-error');
      this.$costInput.removeClass('input-error');

      // Làm tròn LÊN (ceil) số tiền nhập vào về đúng 2 chữ số thập phân (cents)
      const roundedCost = Math.ceil(parseFloat(cost) * 100) / 100;

      // Thêm vào State và cập nhật Set
      const trimmedName = name.trim();
      this.items.push({
        id: Date.now(),
        name: trimmedName,
        cost: roundedCost
      });
      this.nameSet.add(trimmedName.toLowerCase());

      this.render();

      // Reset
      this.$nameInput.val('');
      this.$costInput.val('');
      this.$nameInput.trigger('focus');
    }

    removeItem(id) {
      // Tìm tên trước khi xóa để cập nhật Set
      const removed = this.items.find(item => item.id === id);
      if (removed) this.nameSet.delete(removed.name.toLowerCase());

      this.items = this.items.filter(item => item.id !== id);
      this.render();
    }

    calcTotal() {
      return this.items.reduce((sum, item) => sum + item.cost, 0);
    }

    render() {
      this.$tbody.empty();

      if (this.items.length === 0) {
        this.$tbody.append(
          $('<tr>').addClass('empty-row').append(
            $('<td>').attr('colspan', 3).text('No items yet. Add your first item above!')
          )
        );
      } else {
        this.items.forEach(item => {
          const $btn = $('<button>')
            .addClass('btn-remove')
            .html('&#10005; Remove')
            .on('click', () => this.removeItem(item.id));

          const $row = $('<tr>').append(
            $('<td>').text(item.name),
            $('<td>').text(`$${item.cost.toFixed(2)}`),
            $('<td>').append($btn)
          );

          this.$tbody.append($row);
        });
      }

      // Cập nhật tổng và dùng jQuery .css()
      const total = this.calcTotal();
      this.$totalEl.text(`$${total.toFixed(2)}`);

      if (total > this.overBudget) {
        this.$totalEl.css('color', '#d93a3a'); // Over budget -> Red
      } else {
        this.$totalEl.css('color', '#e87c1e'); // Normal -> Orange
      }
    }
  }

  function init(cfg) {
    return new JQListComponent(cfg);
  }

  return { init };
})();

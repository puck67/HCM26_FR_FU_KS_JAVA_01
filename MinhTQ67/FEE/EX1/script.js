/**
 * script.js — Shopping List & Budget Tracker
 *
 * Problem 02: Pure DOM manipulation (document.createElement / appendChild)
 * Problem 03: jQuery refactoring — $(), .on(), .append(), .remove(), .css()
 */

$(document).ready(function () {

  const BUDGET_LIMIT = 500;

  /* ─── Helpers ─────────────────────────────────────── */

  // Hiện / ẩn bảng và empty-state
  function updateEmptyState() {
    const hasRows = $('#table-body tr').length > 0;
    if (hasRows) {
      $('#empty-state').addClass('d-none');
      $('#table-wrapper').removeClass('d-none');
    } else {
      $('#empty-state').removeClass('d-none');
      $('#table-wrapper').addClass('d-none');
    }
  }

  // Tính lại Grand Total
  function updateTotal() {
    let total = 0;

    // Problem 02: duyệt từng hàng, lấy data-cost
    $('#table-body tr').each(function () {
      const cost = parseFloat($(this).find('.cost-cell').data('cost'));
      if (!isNaN(cost)) total += cost;
    });

    const $amount = $('#grand-total');
    $amount.text('$' + total.toFixed(2));

    // Problem 03: .css() đổi màu đỏ khi vượt $500
    if (total > BUDGET_LIMIT) {
      $amount.css('color', '#dc3545');   // Bootstrap danger red
    } else {
      $amount.css('color', '');          // reset về mặc định (text-warning)
    }
  }

  // Hiện thông báo alert Bootstrap
  function showAlert(msg, type) {
    const $box = $('#alert-box');
    $box
      .removeClass('d-none alert-success alert-danger alert-warning')
      .addClass('alert alert-' + type)
      .html(msg);

    setTimeout(function () {
      $box.fadeOut(300, function () {
        $(this).addClass('d-none').show().removeClass('alert alert-' + type);
      });
    }, 2500);
  }

  /* ─── Problem 02: Tạo row bằng document.createElement ─── */

  function addItemToTable(name, cost) {
    // Dùng document.createElement (constraint Problem 02)
    const tr       = document.createElement('tr');
    const tdName   = document.createElement('td');
    const tdCost   = document.createElement('td');
    const tdAction = document.createElement('td');

    tdName.textContent = name;

    tdCost.className = 'text-center fw-semibold cost-cell';
    tdCost.textContent = '$' + cost.toFixed(2);
    tdCost.setAttribute('data-cost', cost);

    tdAction.className = 'text-center';

    // Nút Remove dùng Bootstrap classes
    const btn = document.createElement('button');
    btn.className = 'btn btn-outline-danger btn-sm btn-remove';
    btn.innerHTML = '<i class="bi bi-x-circle me-1"></i>Remove';
    tdAction.appendChild(btn);

    tr.appendChild(tdName);
    tr.appendChild(tdCost);
    tr.appendChild(tdAction);

    // Problem 03: .append() để thêm vào tbody
    $('#table-body').append(tr);

    updateEmptyState();
    updateTotal();
  }

  /* ─── Problem 03: jQuery .on() event binding ────────── */

  // Nhấn Add Item
  $('#btn-add').on('click', function () {
    const name    = $('#item-name').val().trim();
    const costRaw = $('#item-cost').val().trim();

    // Validate (Problem 02)
    if (name === '') {
      showAlert('<i class="bi bi-exclamation-triangle me-1"></i>Please enter an item name.', 'warning');
      $('#item-name').focus();
      return;
    }

    const cost = parseFloat(costRaw);
    if (costRaw === '' || isNaN(cost) || cost <= 0) {
      showAlert('<i class="bi bi-exclamation-triangle me-1"></i>Please enter a valid positive cost.', 'warning');
      $('#item-cost').focus();
      return;
    }

    addItemToTable(name, cost);

    $('#item-name').val('');
    $('#item-cost').val('');
    $('#item-name').focus();

    showAlert(`<i class="bi bi-check-circle me-1"></i><strong>${name}</strong> added — $${cost.toFixed(2)}`, 'success');
  });

  // Enter key để Add
  $('#item-name, #item-cost').on('keydown', function (e) {
    if (e.key === 'Enter') $('#btn-add').trigger('click');
  });

  // Nhấn Remove — delegated event (Problem 03: .on() delegation)
  $('#table-body').on('click', '.btn-remove', function () {
    const $row = $(this).closest('tr');
    const itemName = $row.find('td:first').text();

    // Problem 03: .remove() để xóa row
    $row.fadeOut(200, function () {
      $(this).remove();
      updateEmptyState();
      updateTotal();
    });

    showAlert(`<i class="bi bi-trash me-1"></i><strong>${itemName}</strong> removed.`, 'danger');
  });

  /* ─── Khởi tạo ──────────────────────────────────────── */
  updateEmptyState();
  updateTotal();
});

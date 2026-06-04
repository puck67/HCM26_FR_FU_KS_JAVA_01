const MAX_NAME_LENGTH = 50;
const MAX_ITEMS       = 50;
const MAX_COST        = 9999.99;

const initialItems = [
  { name: 'Apples', cost: 3.49 },
  { name: 'Milk (1 Gallon)', cost: 4.15 },
  { name: 'Bread (Whole Wheat)', cost: 2.79 },
  { name: 'Eggs (Dozen)', cost: 5.20 },
  { name: 'Cereal (Cheerios)', cost: 4.89 },
];

let items = [...initialItems];

// ── Hiển thị / xóa lỗi inline ──────────────────────────────────
function setError(elId, msg) {
  $('#' + elId).text(msg);
  const inputId = elId === 'nameError' ? 'itemName' : 'itemCost';
  if (msg) $('#' + inputId).addClass('is-invalid');
  else     $('#' + inputId).removeClass('is-invalid');
}

// ── Làm tròn 2 chữ số thập phân bằng integer ───────────────────
function roundCents(value) {
  return Math.round(value * 100) / 100;
}

// ── Tính tổng bằng cents để tránh lỗi float ────────────────────
function calcTotal() {
  const cents = items.reduce(function (sum, item) {
    return sum + Math.round(item.cost * 100);
  }, 0);
  return cents / 100;
}

// ── Cập nhật Grand Total ────────────────────────────────────────
function updateTotal() {
  const total = calcTotal();
  const $grandTotal = $('#grandTotal');
  $grandTotal.text('$' + total.toFixed(2));

  // Đổi màu đỏ nếu tổng > $500 (yêu cầu Problem 03)
  $grandTotal.css('color', total > 500 ? 'red' : '#dc3545');
}

// ── Vẽ lại bảng ────────────────────────────────────────────────
function render() {
  const $tbody = $('#tableBody');
  $tbody.empty();

  items.forEach(function (item, index) {
    const $tr = $('<tr>');

    // title tooltip hiển thị tên đầy đủ khi tên bị cắt bởi ellipsis
    const $tdName = $('<td>').text(item.name).attr('title', item.name);
    const $tdCost = $('<td>').addClass('text-center').text(item.cost.toFixed(2));

    const $btn = $('<button>')
      .addClass('btn btn-outline-danger btn-sm')
      .text('✕ Remove')
      .on('click', function () { removeItem(index); });

    const $tdAction = $('<td>').addClass('text-center').append($btn);

    $tr.append($tdName).append($tdCost).append($tdAction);
    $tbody.append($tr);
  });

  updateTotal();
}

// ── Sự kiện Add Item ────────────────────────────────────────────
$('#addBtn').on('click', function () {
  const name    = $('#itemName').val().trim();
  const rawCost = parseFloat($('#itemCost').val());
  const cost    = roundCents(rawCost);
  let valid     = true;

  // Validate tên
  if (!name) {
    setError('nameError', 'Tên sản phẩm không được để trống.');
    valid = false;
  } else if (name.length > MAX_NAME_LENGTH) {
    setError('nameError', `Tên tối đa ${MAX_NAME_LENGTH} ký tự.`);
    valid = false;
  } else {
    setError('nameError', '');
  }

  // Validate giá
  if ($('#itemCost').val() === '' || isNaN(rawCost)) {
    setError('costError', 'Vui lòng nhập giá.');
    valid = false;
  } else if (cost <= 0) {
    setError('costError', 'Giá phải lớn hơn 0.');
    valid = false;
  } else if (cost > MAX_COST) {
    setError('costError', `Giá tối đa $${MAX_COST.toFixed(2)}.`);
    valid = false;
  } else {
    setError('costError', '');
  }

  // Validate số lượng
  if (valid && items.length >= MAX_ITEMS) {
    setError('nameError', `Danh sách tối đa ${MAX_ITEMS} sản phẩm.`);
    valid = false;
  }

  if (!valid) return;

  items.push({ name: name, cost: cost });
  $('#itemName').val('').focus();
  $('#itemCost').val('');
  render();
});

// ── Xóa item ────────────────────────────────────────────────────
function removeItem(index) {
  items.splice(index, 1);
  render();
}

// ── Khởi động ───────────────────────────────────────────────────
$(document).ready(function () {
  render();
});

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
  const el = document.getElementById(elId);
  el.textContent = msg;
  const input = document.getElementById(elId === 'nameError' ? 'itemName' : 'itemCost');
  if (msg) input.classList.add('is-invalid');
  else input.classList.remove('is-invalid');
}

// ── Làm tròn 2 chữ số thập phân bằng integer (tránh float lỗi) ─
function roundCents(value) {
  return Math.round(value * 100) / 100;
}

// ── Tính tổng bằng cents để tránh lỗi float ────────────────────
function calcTotal() {
  const cents = items.reduce(function (sum, item) {
    return sum + Math.round(item.cost * 100); // cộng dồn nguyên cents
  }, 0);
  return cents / 100; // chuyển lại về đơn vị đô
}

// ── Vẽ lại bảng ────────────────────────────────────────────────
function render() {
  const tbody = document.getElementById('tableBody');
  tbody.innerHTML = '';

  items.forEach(function (item, index) {
    const tr = document.createElement('tr');

    // Ô tên: title tooltip hiển thị tên đầy đủ khi hover
    const tdName = document.createElement('td');
    tdName.textContent = item.name;
    tdName.title = item.name; // tooltip full name khi bị truncate

    const tdCost = document.createElement('td');
    tdCost.className = 'text-center';
    tdCost.textContent = item.cost.toFixed(2);

    const tdAction = document.createElement('td');
    tdAction.className = 'text-center';

    const btn = document.createElement('button');
    btn.className = 'btn btn-outline-danger btn-sm';
    btn.textContent = '✕ Remove';
    btn.onclick = function () { removeItem(index); };

    tdAction.appendChild(btn);
    tr.appendChild(tdName);
    tr.appendChild(tdCost);
    tr.appendChild(tdAction);
    tbody.appendChild(tr);
  });

  document.getElementById('grandTotal').textContent = '$' + calcTotal().toFixed(2);
}

// ── Thêm item ───────────────────────────────────────────────────
function addItem() {
  const nameInput = document.getElementById('itemName');
  const costInput = document.getElementById('itemCost');
  let valid = true;

  const name = nameInput.value.trim();
  const rawCost = parseFloat(costInput.value);
  const cost = roundCents(rawCost); // làm tròn 2 số thập phân khi lưu

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
  if (isNaN(rawCost) || costInput.value === '') {
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

  // Validate số lượng item
  if (valid && items.length >= MAX_ITEMS) {
    setError('nameError', `Danh sách tối đa ${MAX_ITEMS} sản phẩm.`);
    valid = false;
  }

  if (!valid) return;

  items.push({ name: name, cost: cost });
  nameInput.value = '';
  costInput.value = '';
  nameInput.focus();
  render();
}

// ── Xóa item ────────────────────────────────────────────────────
function removeItem(index) {
  items.splice(index, 1);
  render();
}

render();

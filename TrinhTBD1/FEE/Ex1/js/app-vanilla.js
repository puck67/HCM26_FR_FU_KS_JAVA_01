document.addEventListener('DOMContentLoaded', () => {
  const itemNameInput = document.getElementById('item-name');
  const itemCostInput = document.getElementById('item-cost');
  const btnAdd = document.getElementById('btn-add');
  const tableBody = document.getElementById('table-body');
  const grandTotalText = document.getElementById('grand-total');
  const errorBox = document.getElementById('error-message');

  const initialItems = [
    { name: 'Apples', cost: 3.49 },
    { name: 'Milk (1 Gallon)', cost: 4.15 },
    { name: 'Bread (Whole Wheat)', cost: 2.79 },
    { name: 'Eggs (Dozen)', cost: 5.20 },
    { name: 'Cereal (Cheerios)', cost: 4.89 }
  ];

  function showError(msg) {
    errorBox.textContent = msg;
    errorBox.style.display = 'block';
  }

  function clearError() {
    errorBox.textContent = '';
    errorBox.style.display = 'none';
  }

  itemNameInput.addEventListener('input', clearError);
  itemCostInput.addEventListener('input', clearError);

  function createRowElement(name, cost) {
    const tr = document.createElement('tr');

    const tdName = document.createElement('td');
    tdName.textContent = name;

    const tdCost = document.createElement('td');
    tdCost.className = 'cost-col';
    tdCost.textContent = cost.toFixed(2);

    const tdAction = document.createElement('td');
    tdAction.className = 'action-col';

    const btnRemove = document.createElement('button');
    btnRemove.className = 'btn-remove';
    btnRemove.innerHTML = '&times; Remove';

    tdAction.appendChild(btnRemove);
    tr.appendChild(tdName);
    tr.appendChild(tdCost);
    tr.appendChild(tdAction);

    return tr;
  }

  function updateTotal() {
    let total = 0;
    const rows = tableBody.querySelectorAll('tr');
    rows.forEach(row => {
      const costText = row.querySelector('.cost-col').textContent;
      const costValue = parseFloat(costText);
      if (!isNaN(costValue)) {
        total += costValue;
      }
    });
    grandTotalText.textContent = `$${total.toFixed(2)}`;
  }

  initialItems.forEach(item => {
    const row = createRowElement(item.name, item.cost);
    tableBody.appendChild(row);
  });
  updateTotal();

  btnAdd.addEventListener('click', () => {
    const name = itemNameInput.value.trim();
    const costRaw = itemCostInput.value.trim();

    if (!name) {
      showError('Error: Item name cannot be empty.');
      return;
    }

    if (costRaw === '') {
      showError('Error: Item cost cannot be empty.');
      return;
    }

    const costValue = parseFloat(costRaw);
    if (isNaN(costValue)) {
      showError('Error: Item cost must be a valid number.');
      return;
    }

    if (costValue <= 0) {
      showError('Error: Item cost must be a positive number greater than 0.');
      return;
    }

    clearError();
    const row = createRowElement(name, costValue);
    tableBody.appendChild(row);
    updateTotal();

    itemNameInput.value = '';
    itemCostInput.value = '';
    itemNameInput.focus();
  });

  tableBody.addEventListener('click', (event) => {
    if (event.target.classList.contains('btn-remove')) {
      const row = event.target.closest('tr');
      if (row) {
        tableBody.removeChild(row);
        updateTotal();
      }
    }
  });
});

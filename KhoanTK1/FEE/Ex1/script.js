document.addEventListener('DOMContentLoaded', function () {
  const itemForm = document.getElementById('item-form');
  const itemNameInput = document.getElementById('item-name');
  const itemCostInput = document.getElementById('item-cost');
  const tableBody = document.getElementById('table-body');
  const grandTotalElement = document.getElementById('grand-total');

  function calculateGrandTotal() {
    let total = 0;
    const rows = tableBody.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
      const costCell = rows[i].getElementsByTagName('td')[1];
      if (costCell) {
        const costValue = parseFloat(costCell.textContent.replace('$', ''));
        if (!isNaN(costValue)) {
          total += costValue;
        }
      }
    }

    grandTotalElement.textContent = '$' + total.toFixed(2);
  }

  tableBody.addEventListener('click', function (event) {
    const removeButton = event.target.closest('.btn-remove');
    if (removeButton) {
      const row = removeButton.closest('tr');
      if (row) {
        row.remove();
        calculateGrandTotal();
      }
    }
  });

  itemForm.addEventListener('submit', function (event) {
    event.preventDefault();

    const itemName = itemNameInput.value.trim();
    const itemCost = parseFloat(itemCostInput.value);

    // validate
    if (itemName === '') {
      alert('Please enter an item name.');
      return;
    }

    if (isNaN(itemCost) || itemCost <= 0) {
      alert('Please enter a valid cost greater than 0.');
      return;
    }

    const tr = document.createElement('tr');

    const tdName = document.createElement('td');
    tdName.className = 'col-name';
    tdName.textContent = itemName;

    const tdCost = document.createElement('td');
    tdCost.className = 'col-cost';
    tdCost.textContent = '$' + itemCost.toFixed(2);

    const tdAction = document.createElement('td');
    tdAction.className = 'col-action';

    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'btn-remove';

    const xSpan = document.createElement('span');
    xSpan.className = 'remove-x';
    xSpan.innerHTML = '&times; ';

    removeButton.appendChild(xSpan);
    removeButton.appendChild(document.createTextNode('Remove'));
    tdAction.appendChild(removeButton);

    tr.appendChild(tdName);
    tr.appendChild(tdCost);
    tr.appendChild(tdAction);

    tableBody.appendChild(tr);

    calculateGrandTotal();
    itemForm.reset();
    itemNameInput.focus();
  });

  // auto sum
  calculateGrandTotal();
});

document.addEventListener('DOMContentLoaded', () => {
  // Select DOM elements
  const form = document.getElementById('form-native');
  const itemNameInput = document.getElementById('item-name-native');
  const itemCostInput = document.getElementById('item-cost-native');
  const tableBody = document.getElementById('tbody-native');
  const grandTotalSpan = document.getElementById('total-amount-native');
  
  const errorName = document.getElementById('error-name-native');
  const errorCost = document.getElementById('error-cost-native');

  // Calculate grand total by reading from the DOM table rows
  function recalculateGrandTotal() {
    let total = 0;
    const rows = tableBody.getElementsByTagName('tr');
    
    for (let i = 0; i < rows.length; i++) {
      const costCell = rows[i].querySelector('.col-cost-value');
      if (costCell) {
        const costValue = parseFloat(costCell.getAttribute('data-cost'));
        if (!isNaN(costValue)) {
          total += costValue;
        }
      }
    }
    
    // Update total display
    grandTotalSpan.innerText = `$${total.toFixed(2)}`;
  }

  // Handle live inputs to clear errors as user typing/focusing
  itemNameInput.addEventListener('input', () => {
    itemNameInput.classList.remove('is-invalid');
    errorName.classList.remove('active');
  });

  itemCostInput.addEventListener('input', () => {
    itemCostInput.classList.remove('is-invalid');
    errorCost.classList.remove('active');
  });

  // Handle row removal using event delegation
  tableBody.addEventListener('click', (e) => {
    // Check if the clicked element is the remove button
    const removeBtn = e.target.closest('.btn-remove');
    if (removeBtn && tableBody.contains(removeBtn)) {
      const row = removeBtn.closest('tr');
      if (row) {
        row.remove();
        recalculateGrandTotal();
      }
    }
  });

  // Handle form submission
  form.addEventListener('submit', (e) => {
    e.preventDefault();

    // Reset error displays
    itemNameInput.classList.remove('is-invalid');
    itemCostInput.classList.remove('is-invalid');
    errorName.classList.remove('active');
    errorCost.classList.remove('active');

    const name = itemNameInput.value.trim();
    const costText = itemCostInput.value.trim();
    const cost = parseFloat(costText);
    
    let hasError = false;

    // 1. Validate Item Name (not empty)
    if (name === '') {
      itemNameInput.classList.add('is-invalid');
      errorName.classList.add('active');
      hasError = true;
    }

    // 2. Validate Item Cost (not empty, is valid positive number)
    if (costText === '') {
      itemCostInput.classList.add('is-invalid');
      errorCost.innerText = 'Cost cannot be empty.';
      errorCost.classList.add('active');
      hasError = true;
    } else if (isNaN(cost) || cost <= 0) {
      itemCostInput.classList.add('is-invalid');
      errorCost.innerText = 'Cost must be a valid positive number.';
      errorCost.classList.add('active');
      hasError = true;
    }

    if (hasError) {
      return;
    }

    // 3. Create elements using pure DOM manipulation
    const tr = document.createElement('tr');

    // Item Name column
    const tdName = document.createElement('td');
    tdName.className = 'col-name';
    tdName.innerText = name;

    // Item Cost column
    const tdCost = document.createElement('td');
    tdCost.className = 'col-cost';
    
    const costSpan = document.createElement('span');
    costSpan.className = 'col-cost-value';
    costSpan.setAttribute('data-cost', cost.toString());
    costSpan.innerText = `$${cost.toFixed(2)}`;
    tdCost.appendChild(costSpan);

    // Action column
    const tdAction = document.createElement('td');
    tdAction.className = 'col-action';

    // Remove button
    const removeBtn = document.createElement('button');
    removeBtn.type = 'button';
    removeBtn.className = 'btn-remove';
    removeBtn.innerText = '✖ Remove';

    tdAction.appendChild(removeBtn);

    // Append columns to row
    tr.appendChild(tdName);
    tr.appendChild(tdCost);
    tr.appendChild(tdAction);

    // Append row to table body
    tableBody.appendChild(tr);

    // Recalculate and update displays
    recalculateGrandTotal();

    // Clear inputs and set focus back
    itemNameInput.value = '';
    itemCostInput.value = '';
    itemNameInput.focus();
  });

  // Calculate total for preloaded items on page load
  recalculateGrandTotal();
});

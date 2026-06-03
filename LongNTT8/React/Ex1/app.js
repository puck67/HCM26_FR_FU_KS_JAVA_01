// Problem 02: Use JavaScript to calculate Budget
document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('shopping-form');
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const tableBody = document.getElementById('table-body');
    const grandTotalSpan = document.getElementById('grand-total');
    
    let grandTotal = 0;

    const formatCurrency = (amount) => {
        return `$${amount.toFixed(2)}`;
    };

    const updateGrandTotal = () => {
        grandTotalSpan.textContent = formatCurrency(grandTotal);
        // Requirement from problem 3 applied optionally in vanilla for consistency
        if (grandTotal > 500) {
            grandTotalSpan.style.color = 'red';
        } else {
            grandTotalSpan.style.color = '#A94400';
        }
    };

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const itemName = itemNameInput.value.trim();
        const itemCost = parseFloat(itemCostInput.value);

        if (!itemName || isNaN(itemCost) || itemCost <= 0) {
            alert('Please enter a valid item name and a positive cost.');
            return;
        }

        // Create row
        const tr = document.createElement('tr');

        // Item Name cell
        const tdName = document.createElement('td');
        tdName.textContent = itemName;
        tdName.classList.add('border-round-table');
        // Cost cell
        const tdCost = document.createElement('td');
        tdCost.textContent = itemCost.toFixed(2);
        tdCost.classList.add('text-center');

        // Action cell
        const tdAction = document.createElement('td');
        tdAction.classList.add('text-center');
        tdAction.classList.add('border-round-table-end');
        const removeBtn = document.createElement('button');
        removeBtn.type = 'button';
        removeBtn.className = 'btn-remove';
        removeBtn.innerHTML = '<span class="remove-icon">&#10006;</span> Remove';
        
        removeBtn.addEventListener('click', () => {
            tr.remove();
            grandTotal -= itemCost;
            updateGrandTotal();
        });

        tdAction.appendChild(removeBtn);

        tr.appendChild(tdName);
        tr.appendChild(tdCost);
        tr.appendChild(tdAction);

        tableBody.appendChild(tr);

        // Update total
        grandTotal += itemCost;
        updateGrandTotal();

        // Reset inputs
        itemNameInput.value = '';
        itemCostInput.value = '';
        itemNameInput.focus();
    });
});

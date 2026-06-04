document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('shopping-form');
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const tableBody = document.getElementById('table-body');
    const grandTotalSpan = document.getElementById('grand-total');
    const errorAlert = document.getElementById('error-alert');
    const errorMsg = document.getElementById('error-msg');

    // Default items from the PDF specification/screenshot
    const defaultItems = [
        { name: "Apples", cost: 3.49 },
        { name: "Milk (1 Gallon)", cost: 4.15 },
        { name: "Bread (Whole Wheat)", cost: 2.79 },
        { name: "Eggs (Dozen)", cost: 5.20 },
        { name: "Cereal (Cheerios)", cost: 4.89 }
    ];

    // Initialize list with default items
    defaultItems.forEach(item => {
        addItemToTable(item.name, item.cost);
    });
    updateGrandTotal();

    // Form submission event
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        
        const name = itemNameInput.value.trim();
        const costVal = itemCostInput.value.trim();
        
        // Validation
        if (!name) {
            showError("Item Name cannot be empty.");
            return;
        }

        if (!costVal) {
            showError("Item Cost cannot be empty.");
            return;
        }

        const cost = parseFloat(costVal);
        if (isNaN(cost) || cost <= 0) {
            showError("Item Cost must be a valid positive number.");
            return;
        }

        // Add to table
        addItemToTable(name, cost);
        
        // Recalculate Grand Total
        updateGrandTotal();
        
        // Reset form inputs & clear errors
        form.reset();
        clearError();
    });

    // Helper to add item row using pure DOM manipulation methods
    function addItemToTable(name, cost) {
        // Create table row element
        const tr = document.createElement('tr');

        // Create Item Name cell
        const tdName = document.createElement('td');
        tdName.textContent = name;
        tr.appendChild(tdName);

        // Create Item Cost cell
        const tdCost = document.createElement('td');
        tdCost.className = 'text-center';
        tdCost.textContent = cost.toFixed(2);
        tr.appendChild(tdCost);

        // Create Action cell containing Remove button
        const tdAction = document.createElement('td');
        tdAction.className = 'text-center';

        const btnRemove = document.createElement('button');
        btnRemove.className = 'btn-remove';
        btnRemove.type = 'button';
        
        // Create remove icon and text
        const icon = document.createElement('i');
        icon.className = 'fa-solid fa-xmark';
        btnRemove.appendChild(icon);
        
        const btnText = document.createTextNode(' Remove');
        btnRemove.appendChild(btnText);

        // Remove row event listener
        btnRemove.addEventListener('click', () => {
            // Remove the corresponding row from the table
            tr.remove();
            // Recalculate grand total
            updateGrandTotal();
        });

        tdAction.appendChild(btnRemove);
        tr.appendChild(tdAction);

        // Append the row to the table body
        tableBody.appendChild(tr);
    }

    // Helper to calculate grand total
    function updateGrandTotal() {
        let total = 0;
        // Use querySelectorAll to find all rows
        const rows = tableBody.querySelectorAll('tr');
        
        rows.forEach(row => {
            // The cost is the second cell (index 1)
            const costCell = row.cells[1];
            if (costCell) {
                total += parseFloat(costCell.textContent);
            }
        });

        // Set value in UI
        grandTotalSpan.textContent = `$${total.toFixed(2)}`;
    }

    // Validation alerts helper functions
    function showError(message) {
        errorMsg.textContent = message;
        errorAlert.classList.add('visible');
    }

    function clearError() {
        errorAlert.classList.remove('visible');
        errorMsg.textContent = '';
    }
});

// Problem 02: Use Native JavaScript to manage the list interactivity

document.addEventListener('DOMContentLoaded', () => {
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const addBtn = document.getElementById('add-btn');
    const tableBody = document.getElementById('table-body');
    const grandTotalElement = document.getElementById('grand-total');

    let items = [];

    const calculateTotal = () => {
        const total = items.reduce((sum, item) => sum + item.cost, 0);
        grandTotalElement.textContent = `$${total.toFixed(2)}`;
    };

    const removeItem = (id) => {
        items = items.filter(item => item.id !== id);
        renderTable();
        calculateTotal();
    };

    const renderTable = () => {
        // Clear current table
        tableBody.innerHTML = '';

        items.forEach(item => {
            const row = document.createElement('tr');

            const nameCell = document.createElement('td');
            nameCell.textContent = item.name;
            row.appendChild(nameCell);

            const costCell = document.createElement('td');
            costCell.textContent = item.cost.toFixed(2);
            row.appendChild(costCell);

            const actionCell = document.createElement('td');
            const removeBtn = document.createElement('button');
            removeBtn.className = 'remove-btn';
            removeBtn.innerHTML = '<span style="color:red; font-weight:bold;">&times;</span> Remove';
            removeBtn.onclick = () => removeItem(item.id);
            actionCell.appendChild(removeBtn);
            row.appendChild(actionCell);

            tableBody.appendChild(row);
        });
    };

    addBtn.addEventListener('click', () => {
        const name = itemNameInput.value.trim();
        const costValue = itemCostInput.value;
        const cost = parseFloat(costValue);

        // Validation
        if (name === '') {
            alert('Please enter an item name.');
            return;
        }
        if (isNaN(cost) || cost <= 0) {
            alert('Please enter a valid positive cost.');
            return;
        }

        // Add item
        const newItem = {
            id: Date.now(),
            name: name,
            cost: cost
        };

        items.push(newItem);
        
        // Pure DOM manipulation as per requirement (though I'm using renderTable for simplicity, 
        // the requirement says "Append a new row to the table" and "use pure DOM manipulation methods")
        // To be strictly compliant with "Append a new row", I'll implement it directly:
        
        const row = document.createElement('tr');
        
        const nameCell = document.createElement('td');
        nameCell.textContent = newItem.name;
        row.appendChild(nameCell);

        const costCell = document.createElement('td');
        costCell.textContent = newItem.cost.toFixed(2);
        row.appendChild(costCell);

        const actionCell = document.createElement('td');
        const removeBtn = document.createElement('button');
        removeBtn.className = 'remove-btn';
        removeBtn.innerHTML = '&times; Remove';
        removeBtn.onclick = function() {
            // Recalculate based on existing items in array
            items = items.filter(i => i.id !== newItem.id);
            row.remove();
            calculateTotal();
        };
        actionCell.appendChild(removeBtn);
        row.appendChild(actionCell);

        tableBody.appendChild(row);

        // Update total
        calculateTotal();

        // Clear inputs
        itemNameInput.value = '';
        itemCostInput.value = '';
        itemNameInput.focus();
    });
});

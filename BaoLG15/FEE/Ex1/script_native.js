document.addEventListener('DOMContentLoaded', function () {

    const addItemBtn = document.getElementById('add-item-btn');
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const tableBody = document.querySelector('#shopping-list tbody');
    const grandTotalElement = document.getElementById('grand-total');

    function updateGrandTotal() {
        let total = 0;
        const rows = tableBody.querySelectorAll('tr');

        rows.forEach(row => {
            const costCell = row.children[1];
            const cost = parseFloat(costCell.textContent);
            if (!isNaN(cost)) {
                total += cost;
            }
        });

        grandTotalElement.textContent = '$' + total.toFixed(2);
    }

    addItemBtn.addEventListener('click', function () {
        const itemName = itemNameInput.value.trim();
        const itemCost = parseFloat(itemCostInput.value);

        if (itemName === '') {
            alert('Please enter an Item Name.');
            return;
        }

        if (isNaN(itemCost) || itemCost <= 0) {
            alert('Please enter a valid positive Item Cost.');
            return;
        }

        const newRow = document.createElement('tr');

        const nameCell = document.createElement('td');
        nameCell.textContent = itemName;

        const costCell = document.createElement('td');
        costCell.textContent = itemCost.toFixed(2);

        const actionCell = document.createElement('td');
        const removeBtn = document.createElement('button');
        removeBtn.className = 'remove-btn';
        removeBtn.innerHTML = '&#10006; Remove';

        removeBtn.addEventListener('click', function () {
            tableBody.removeChild(newRow);
            updateGrandTotal();
        });

        actionCell.appendChild(removeBtn);

        newRow.appendChild(nameCell);
        newRow.appendChild(costCell);
        newRow.appendChild(actionCell);

        tableBody.appendChild(newRow);

        itemNameInput.value = '';
        itemCostInput.value = '';

        updateGrandTotal();
    });
});

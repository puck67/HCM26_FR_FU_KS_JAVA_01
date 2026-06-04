document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('shopping-form');
    const itemNameInput = document.getElementById('item-name');
    const itemCostInput = document.getElementById('item-cost');
    const itemList = document.getElementById('item-list');
    const grandTotal = document.getElementById('grand-total');

    // Prevent focus on click to completely avoid Chrome's focus ring ghost rendering bug
    const clearFocus = (event) => {
        const target = event.target.closest('button, a');
        if (target) {
            event.preventDefault();
            target.blur();
            if (document.activeElement) {
                document.activeElement.blur();
            }
        }
    };
    document.addEventListener('mousedown', clearFocus);
    document.addEventListener('pointerdown', clearFocus);

    function updateGrandTotal() {
        let total = 0;
        const costCells = itemList.getElementsByClassName('cost-val');
        for (let i = 0; i < costCells.length; i++) {
            const cost = parseFloat(costCells[i].textContent);
            if (!isNaN(cost)) {
                total += cost;
            }
        }
        grandTotal.textContent = '$' + total.toFixed(2);
    }

    form.addEventListener('submit', (event) => {
        event.preventDefault();

        const nameInput = itemNameInput.value.trim();
        const costInput = parseFloat(itemCostInput.value);

        if (nameInput === '' || isNaN(costInput) || costInput <= 0) {
            alert('Vui lòng nhập tên sản phẩm và giá trị số dương hợp lệ!');
            return;
        }

        const tr = document.createElement('tr');

        const tdName = document.createElement('td');
        tdName.appendChild(document.createTextNode(nameInput));

        const tdCost = document.createElement('td');
        tdCost.className = 'cost-val';
        tdCost.appendChild(document.createTextNode(costInput.toFixed(2)));

        const tdAction = document.createElement('td');
        const removeBtn = document.createElement('button');
        removeBtn.className = 'remove-btn';
        removeBtn.appendChild(document.createTextNode('✕ Remove'));
        
        tdAction.appendChild(removeBtn);
        tr.appendChild(tdName);
        tr.appendChild(tdCost);
        tr.appendChild(tdAction);

        itemList.appendChild(tr);
        form.reset();
        updateGrandTotal();
        itemNameInput.focus();
    });

    itemList.addEventListener('click', (event) => {
        if (event.target && event.target.classList.contains('remove-btn')) {
            const tr = event.target.closest('tr');
            if (tr) {
                event.target.blur();
                if (document.activeElement) {
                    document.activeElement.blur();
                }
                itemList.removeChild(tr);
                updateGrandTotal();
            }
        }
    });

    updateGrandTotal();
});

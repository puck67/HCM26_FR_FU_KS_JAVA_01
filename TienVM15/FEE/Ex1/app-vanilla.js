(function() {
    // Pure DOM manipulation version
    window.initVanilla = function() {
        // Clear jQuery delegated event listeners to avoid conflicts/duplicate events in Vanilla mode
        if (typeof jQuery !== 'undefined') {
            jQuery('#items-table-body').off('click', '.btn-remove');
        }

        const form = document.querySelector('.input-form');
        const itemNameInput = document.getElementById('item-name');
        const itemCostInput = document.getElementById('item-cost');
        const itemsTableBody = document.getElementById('items-table-body');
        const grandTotalSpan = document.getElementById('grand-total');
        const alertBox = document.getElementById('alert-box');

        const formSubmitHandler = function(e) {
            e.preventDefault();
            alertBox.style.display = 'none';

            const name = itemNameInput.value.trim();
            const costVal = itemCostInput.value.trim();
            const cost = parseFloat(costVal);

            // Validation
            if (!name) {
                showAlert('Please enter an item name.');
                return;
            }
            if (!costVal || isNaN(cost) || cost <= 0) {
                showAlert('Please enter a valid positive number for item cost.');
                return;
            }

            // Append new row
            appendRowVanilla(name, cost);

            // Clear inputs
            itemNameInput.value = '';
            itemCostInput.value = '';
            itemNameInput.focus();
        };

        form.addEventListener('submit', formSubmitHandler);

        function showAlert(msg) {
            alertBox.textContent = msg;
            alertBox.style.display = 'block';
        }

        function appendRowVanilla(name, cost) {
            // Remove empty state if present
            const emptyState = itemsTableBody.querySelector('.empty-state-row');
            if (emptyState) {
                itemsTableBody.removeChild(emptyState);
            }

            // Create Elements using pure DOM manipulation
            const tr = document.createElement('tr');

            const tdName = document.createElement('td');
            tdName.textContent = name;

            const tdCost = document.createElement('td');
            tdCost.textContent = `$${cost.toFixed(2)}`;

            const tdAction = document.createElement('td');
            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'btn-remove';
            removeBtn.innerHTML = '<span class="remove-icon">✖</span> Remove';
            
            // Event listener for remove
            removeBtn.addEventListener('click', function() {
                itemsTableBody.removeChild(tr);
                updateGrandTotalVanilla();
                checkEmptyState();
            });

            tdAction.appendChild(removeBtn);
            tr.appendChild(tdName);
            tr.appendChild(tdCost);
            tr.appendChild(tdAction);
            itemsTableBody.appendChild(tr);

            updateGrandTotalVanilla();
        }

        function checkEmptyState() {
            if (itemsTableBody.children.length === 0) {
                const tr = document.createElement('tr');
                tr.className = 'empty-state-row';
                const td = document.createElement('td');
                td.colSpan = 3;
                td.className = 'empty-state';
                td.textContent = 'No items in the list.';
                tr.appendChild(td);
                itemsTableBody.appendChild(tr);
            }
        }

        function updateGrandTotalVanilla() {
            let total = 0;
            const rows = itemsTableBody.querySelectorAll('tr:not(.empty-state-row)');
            rows.forEach(function(row) {
                const costCell = row.children[1];
                const cost = parseFloat(costCell.textContent.replace('$', ''));
                if (!isNaN(cost)) {
                    total += cost;
                }
            });

            grandTotalSpan.textContent = `$${total.toFixed(2)}`;

            // Vanilla doesn't strictly require red coloring but we can keep standard colors
            // or let the jQuery logic handle color changes explicitly.
            // Problem 03 specifies jQuery should change it to red if > 500.
            // Let's reset the class in vanilla mode so it works cleanly.
            if (total > 500) {
                grandTotalSpan.classList.add('exceeded');
            } else {
                grandTotalSpan.classList.remove('exceeded');
            }
        }

        // Initialize state
        checkEmptyState();
        updateGrandTotalVanilla();
    };
})();

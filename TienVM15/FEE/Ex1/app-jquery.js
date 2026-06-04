(function() {
    window.initJQuery = function() {
        // Target elements using jQuery selectors
        const $form = $('.input-form');
        const $itemNameInput = $('#item-name');
        const $itemCostInput = $('#item-cost');
        const $itemsTableBody = $('#items-table-body');
        const $grandTotalSpan = $('#grand-total');
        const $alertBox = $('#alert-box');

        // Unbind any previous submit handlers on form to avoid duplicate bindings
        $form.off('submit');

        // Bind submit handler
        $form.on('submit', function(e) {
            e.preventDefault();
            $alertBox.hide();

            const name = $itemNameInput.val().trim();
            const costVal = $itemCostInput.val().trim();
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

            // Remove empty state if present
            $itemsTableBody.find('.empty-state-row').remove();

            // Create new row markup using jQuery and append
            const $row = $('<tr>');
            $row.append($('<td>').text(name));
            $row.append($('<td>').text(`$${cost.toFixed(2)}`));
            
            const $removeBtn = $('<button>')
                .attr('type', 'button')
                .addClass('btn-remove')
                .html('<span class="remove-icon">✖</span> Remove');

            $row.append($('<td>').append($removeBtn));
            $itemsTableBody.append($row);

            // Clear inputs
            $itemNameInput.val('');
            $itemCostInput.val('');
            $itemNameInput.focus();

            updateGrandTotalJQuery();
        });

        // Use jQuery delegated event binding for remove actions
        $itemsTableBody.off('click', '.btn-remove');
        $itemsTableBody.on('click', '.btn-remove', function() {
            // Remove the parent row using jQuery's .remove()
            $(this).closest('tr').remove();
            updateGrandTotalJQuery();
            checkEmptyState();
        });

        function showAlert(msg) {
            $alertBox.text(msg).show();
        }

        function checkEmptyState() {
            if ($itemsTableBody.children('tr:not(.empty-state-row)').length === 0) {
                $itemsTableBody.find('.empty-state-row').remove(); // avoid duplicate empty rows
                const $emptyRow = $('<tr class="empty-state-row">')
                    .append($('<td colspan="3" class="empty-state">').text('No items in the list.'));
                $itemsTableBody.append($emptyRow);
            }
        }

        function updateGrandTotalJQuery() {
            let total = 0;
            // Iterate over active rows using jQuery
            $itemsTableBody.find('tr:not(.empty-state-row)').each(function() {
                const costText = $(this).find('td:eq(1)').text().replace('$', '');
                const cost = parseFloat(costText);
                if (!isNaN(cost)) {
                    total += cost;
                }
            });

            $grandTotalSpan.text(`$${total.toFixed(2)}`);

            // Requirement: Change color to red if total exceeds $500 using jQuery's .css()
            if (total > 500) {
                $grandTotalSpan.css('color', '#e53e3e'); // Red color
            } else {
                $grandTotalSpan.css('color', '#a0522d'); // Restore original color
            }
        }

        // Initialize state
        checkEmptyState();
        updateGrandTotalJQuery();
    };
})();


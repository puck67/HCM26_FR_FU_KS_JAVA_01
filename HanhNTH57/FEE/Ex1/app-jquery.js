// Problem 03: Use jQuery for Refactoring

$(document).ready(function() {
    // Selectors
    const $itemNameInput = $('#item-name');
    const $itemCostInput = $('#item-cost');
    const $addBtn = $('#add-btn');
    const $tableBody = $('#table-body');
    const $grandTotalElement = $('#grand-total');

    function calculateTotal() {
        let total = 0;
        
        // Sum up costs from the table rows
        $tableBody.find('tr').each(function() {
            const costStr = $(this).find('td:nth-child(2)').text();
            total += parseFloat(costStr);
        });

        $grandTotalElement.text(`$${total.toFixed(2)}`);

        // Requirement: Change color to red if total > $500
        if (total > 500) {
            $grandTotalElement.css('color', '#dc3545'); // Using .css() as requested
        } else {
            $grandTotalElement.css('color', '#a0522d'); // Default color
        }
    }

    // Event binding for Add Button
    $addBtn.on('click', function() {
        const name = $itemNameInput.val().trim();
        const costStr = $itemCostInput.val();
        const cost = parseFloat(costStr);

        // Validation
        if (name === '') {
            alert('Please enter an item name.');
            return;
        }
        if (isNaN(cost) || cost <= 0) {
            alert('Please enter a valid positive cost.');
            return;
        }

        // Create new row using jQuery .append()
        const $row = $('<tr>');
        
        $row.append($('<td>').text(name));
        $row.append($('<td>').text(cost.toFixed(2)));
        
        // Action column with Remove button
        const $actionCell = $('<td>');
        const $removeBtn = $('<button>')
            .addClass('remove-btn')
            .html('<span style="color:red; font-weight:bold;">&times;</span> Remove');
            
        // Event binding for Remove button
        $removeBtn.on('click', function() {
            $row.remove(); // Using jQuery .remove()
            calculateTotal();
        });

        $actionCell.append($removeBtn);
        $row.append($actionCell);

        $tableBody.append($row);

        // Update total
        calculateTotal();

        // Clear inputs
        $itemNameInput.val('').focus();
        $itemCostInput.val('');
    });
});

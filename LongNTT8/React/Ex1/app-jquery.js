// Problem 03: Use jQuery for Refactoring
$(document).ready(function() {
    let grandTotal = 0;

    const formatCurrency = (amount) => {
        return `$${amount.toFixed(2)}`;
    };

    const updateGrandTotal = () => {
        const $totalSpan = $('#grand-total');
        $totalSpan.text(formatCurrency(grandTotal));
        
        // Change color to red if total > $500
        if (grandTotal > 500) {
            $totalSpan.css('color', 'red');
        } else {
            // Revert to original styling variable from CSS
            $totalSpan.css('color', '#A94400'); 
        }
    };

    $('#shopping-form').on('submit', function(e) {
        e.preventDefault();

        const $itemNameInput = $('#item-name');
        const $itemCostInput = $('#item-cost');
        
        const itemName = $itemNameInput.val().trim();
        const itemCost = parseFloat($itemCostInput.val());

        if (!itemName || isNaN(itemCost) || itemCost <= 0) {
            alert('Please enter a valid item name and a positive cost.');
            return;
        }

        const rowHtml = `
            <tr>
                <td>${itemName}</td>
                <td class="text-center">${itemCost.toFixed(2)}</td>
                <td class="text-center">
                    <button type="button" class="btn-remove" data-cost="${itemCost}">
                        <span class="remove-icon">&#10006;</span> Remove
                    </button>
                </td>
            </tr>
        `;

        // jQuery method .append() for DOM manipulation
        $('#table-body').append(rowHtml);

        grandTotal += itemCost;
        updateGrandTotal();

        $itemNameInput.val('');
        $itemCostInput.val('');
        $itemNameInput.focus();
    });

    // Use jQuery event binding to handle the Remove action
    $('#table-body').on('click', '.btn-remove', function() {
        const itemCost = parseFloat($(this).data('cost'));
        // jQuery method .remove()
        $(this).closest('tr').remove();
        
        grandTotal -= itemCost;
        updateGrandTotal();
    });
});

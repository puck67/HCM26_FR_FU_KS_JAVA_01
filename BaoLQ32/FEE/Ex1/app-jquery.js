$(document).ready(function() {
    var $form = $('#shopping-form');
    var $itemName = $('#item-name');
    var $itemCost = $('#item-cost');
    var $tableBody = $('#table-body');
    var $grandTotal = $('#grand-total');
    var $errorAlert = $('#error-alert');
    var $errorMsg = $('#error-msg');

    // Default mock items
    var defaultItems = [
        { name: "Apples", cost: 3.49 },
        { name: "Milk (1 Gallon)", cost: 4.15 },
        { name: "Bread (Whole Wheat)", cost: 2.79 },
        { name: "Eggs (Dozen)", cost: 5.20 },
        { name: "Cereal (Cheerios)", cost: 4.89 }
    ];

    // Prepopulate table rows using jQuery .append()
    $.each(defaultItems, function(index, item) {
        addItem(item.name, item.cost);
    });
    recalculateTotal();

    // Event binding: Form Submit for adding item
    $form.on('submit', function(e) {
        e.preventDefault();

        var name = $.trim($itemName.val());
        var costVal = $.trim($itemCost.val());

        // Validations
        if (!name) {
            displayError("Item Name cannot be empty.");
            return;
        }

        if (!costVal) {
            displayError("Item Cost cannot be empty.");
            return;
        }

        var cost = parseFloat(costVal);
        if (isNaN(cost) || cost <= 0) {
            displayError("Item Cost must be a valid positive number.");
            return;
        }

        // Add row
        addItem(name, cost);
        recalculateTotal();

        // Clear values & error messages
        $form[0].reset();
        hideError();
    });

    // Event binding: Delegate "Remove" click to handle dynamically added items
    $tableBody.on('click', '.btn-remove', function() {
        // Find parent row and remove it
        $(this).closest('tr').remove();
        recalculateTotal();
    });

    // Helper to append a row to table using jQuery .append()
    function addItem(name, cost) {
        var rowHtml = '<tr>' +
            '<td>' + escapeHtml(name) + '</td>' +
            '<td class="text-center cost-value">' + cost.toFixed(2) + '</td>' +
            '<td class="text-center">' +
                '<button type="button" class="btn-remove">' +
                    '<i class="fa-solid fa-xmark"></i> Remove' +
                '</button>' +
            '</td>' +
        '</tr>';

        $tableBody.append(rowHtml);
    }

    // Helper to calculate total cost and update styles
    function recalculateTotal() {
        var total = 0;
        
        // Sum up costs using jQuery selector and loop
        $tableBody.find('.cost-value').each(function() {
            var value = parseFloat($(this).text());
            if (!isNaN(value)) {
                total += value;
            }
        });

        // Set value text
        $grandTotal.text('$' + total.toFixed(2));

        // Requirement: changes color to red if total exceeds $500 using jQuery .css()
        if (total > 500) {
            $grandTotal.css('color', 'red');
            $grandTotal.addClass('exceeded');
        } else {
            // Reset to stylesheet standard color
            $grandTotal.css('color', '');
            $grandTotal.removeClass('exceeded');
        }
    }

    // Helper to show/hide errors
    function displayError(message) {
        $errorMsg.text(message);
        $errorAlert.addClass('visible');
    }

    function hideError() {
        $errorAlert.removeClass('visible');
        $errorMsg.text('');
    }

    // Simple HTML escaping helper for security
    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});

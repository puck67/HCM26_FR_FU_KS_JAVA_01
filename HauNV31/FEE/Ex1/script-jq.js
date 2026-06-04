// Problem 03 - jQuery Implementation
$(document).ready(function() {
    let grandTotal = 0;

    function updateGrandTotal(amount) {
        grandTotal += amount;
        $('#grandTotal').text('$' + grandTotal.toFixed(2));
        
        // Requirement: "Grand Total" text changes color to red if > 500 using jQuery .css()
        if (grandTotal > 500) {
            $('#grandTotal').css('color', 'red');
        } else {
            $('#grandTotal').css('color', '#a04000'); // reset to default color
        }
    }

    $('#addItemBtn').on('click', function() {
        const itemName = $('#itemName').val().trim();
        const itemCost = parseFloat($('#itemCost').val());

        // Validate inputs
        if (itemName === "") {
            alert("Please enter an item name.");
            return;
        }

        if (isNaN(itemCost) || itemCost <= 0) {
            alert("Please enter a valid positive cost.");
            return;
        }

        // Create row structure
        const rowHTML = `
            <tr>
                <td>${itemName}</td>
                <td>${itemCost.toFixed(2)}</td>
                <td><button class="remove-btn" data-cost="${itemCost}">✘ Remove</button></td>
            </tr>
        `;

        // Append row to table
        $('#shoppingBody').append(rowHTML);

        // Update total
        updateGrandTotal(itemCost);

        // Clear inputs
        $('#itemName').val('');
        $('#itemCost').val('');
    });

    // Event delegation for remove button (since rows are dynamically added)
    $('#shoppingBody').on('click', '.remove-btn', function() {
        const costToRemove = parseFloat($(this).data('cost'));
        
        // Remove the row
        $(this).closest('tr').remove();
        
        // Recalculate total
        updateGrandTotal(-costToRemove);
    });
});

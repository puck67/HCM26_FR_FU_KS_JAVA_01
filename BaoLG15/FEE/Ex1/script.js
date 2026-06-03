$(document).ready(function () {
    function updateGrandTotal() {
        let total = 0;

        $('#shopping-list tbody tr').each(function () {
            let costText = $(this).find('td:eq(1)').text();
            let cost = parseFloat(costText);
            if (!isNaN(cost)) {
                total += cost;
            }
        });

        $('#grand-total').text('$' + total.toFixed(2));

        if (total > 500) {
            $('#grand-total').css('color', 'red');
        } else {
            $('#grand-total').css('color', '#9c4221');
        }
    }

    $('#add-item-btn').on('click', function () {
        let itemName = $('#item-name').val().trim();
        let itemCost = parseFloat($('#item-cost').val());

        if (itemName === '') {
            alert('Please enter an Item Name.');
            return;
        }

        if (isNaN(itemCost) || itemCost <= 0) {
            alert('Please enter a valid positive Item Cost.');
            return;
        }

        let newRow = $(`
            <tr>
                <td>${itemName}</td>
                <td>${itemCost.toFixed(2)}</td>
                <td>
                    <button class="remove-btn">&#10006; Remove</button>
                </td>
            </tr>
        `);

        $('#shopping-list tbody').append(newRow);

        $('#item-name').val('');
        $('#item-cost').val('');
        updateGrandTotal();
    });

    $('#shopping-list').on('click', '.remove-btn', function () {
        $(this).closest('tr').remove();
        updateGrandTotal();
    });
});

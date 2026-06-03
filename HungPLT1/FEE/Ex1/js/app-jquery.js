$(document).ready(function() {
    
    // Prevent focus on click to completely avoid Chrome's focus ring ghost rendering bug
    $(document).on('mousedown pointerdown', 'button, a', function(event) {
        event.preventDefault();
        $(this).blur();
        if (document.activeElement) {
            document.activeElement.blur();
        }
    });

    function updateGrandTotal() {
        let total = 0;
        
        $('.cost-val').each(function() {
            let cost = parseFloat($(this).text());
            if (!isNaN(cost)) {
                total += cost;
            }
        });

        $('#grand-total').text('$' + total.toFixed(2));

        if (total > 500) {
            $('#grand-total').css('color', 'red');
        } else {
            $('#grand-total').css('color', '#d35400');
        }
    }

    $('#shopping-form').on('submit', function(event) {
        event.preventDefault();

        let nameInput = $('#item-name').val().trim();
        let costInput = parseFloat($('#item-cost').val());

        if (nameInput === '' || isNaN(costInput) || costInput <= 0) {
            alert('Vui lòng nhập tên sản phẩm và giá trị số dương hợp lệ!');
            return;
        }

        let newRow = `
            <tr>
                <td>${nameInput}</td>
                <td class="cost-val">${costInput.toFixed(2)}</td>
                <td><button class="remove-btn">✕ Remove</button></td>
            </tr>
        `;

        $('#item-list').append(newRow);
        $('#shopping-form')[0].reset();
        updateGrandTotal();
        $('#item-name').focus();
    });

    $('#item-list').on('click', '.remove-btn', function() {
        $(this).blur();
        if (document.activeElement) {
            document.activeElement.blur();
        }
        $(this).closest('tr').remove();
        updateGrandTotal();
    });

    updateGrandTotal();
});

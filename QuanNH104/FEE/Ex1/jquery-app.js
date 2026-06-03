$(document).ready(function() {
    const $itemName = $('#jquery-item-name');
    const $itemCost = $('#jquery-item-cost');
    const $addBtn = $('#jquery-add-btn');
    const $itemList = $('#jquery-item-list');
    const $grandTotal = $('#jquery-total');

    function updateTotal() {
        let total = 0;
        
        $itemList.find('tr:not(.empty-row)').each(function() {
            const costText = $(this).find('td:nth-child(2)').text();
            const costValue = parseFloat(costText);
            if (!isNaN(costValue)) {
                total += costValue;
            }
        });

        $grandTotal.text(`$${total.toFixed(2)}`);

        if (total > 500) {
            $grandTotal.css('color', '#dc2626');
        } else {
            $grandTotal.css('color', '#c2410c');
        }
    }

    function toggleEmptyRow() {
        const rowCount = $itemList.find('tr:not(.empty-row)').length;
        const $emptyRow = $itemList.find('.empty-row');

        if (rowCount === 0) {
            if ($emptyRow.length === 0) {
                const $tr = $('<tr class="empty-row"></tr>');
                const $td = $('<td colspan="3" class="text-center text-muted fst-italic">Chưa có sản phẩm nào. Hãy thêm ở trên!</td>');
                $tr.append($td);
                $itemList.append($tr);
            }
        } else {
            $emptyRow.remove();
        }
    }

    $addBtn.on('click', function() {
        const nameValue = $itemName.val().trim();
        const costValue = parseFloat($itemCost.val());

        let isValid = true;

        if (nameValue === '') {
            $itemName.addClass('is-invalid');
            isValid = false;
        } else {
            $itemName.removeClass('is-invalid');
        }

        if (isNaN(costValue) || costValue <= 0) {
            $itemCost.addClass('is-invalid');
            isValid = false;
        } else {
            $itemCost.removeClass('is-invalid');
        }

        if (!isValid) return;

        toggleEmptyRow();

        const $tr = $('<tr></tr>');
        
        $tr.append($('<td></td>').text(nameValue));
        $tr.append($('<td></td>').addClass('text-end').text(costValue.toFixed(2)));

        const $removeBtn = $('<button></button>')
            .addClass('btn btn-outline-danger btn-sm btn-remove')
            .html('<i class="fa-solid fa-xmark me-1"></i> <span>Remove</span>');

        $removeBtn.on('click', function() {
            $tr.remove();
            toggleEmptyRow();
            updateTotal();
        });

        $tr.append($('<td></td>').addClass('text-center').append($removeBtn));

        $itemList.append($tr);

        updateTotal();

        $itemName.val('');
        $itemCost.val('');
        $itemName.trigger('focus');
    });

    $itemName.on('input', function() {
        if ($itemName.val().trim() !== '') {
            $itemName.removeClass('is-invalid');
        }
    });

    $itemCost.on('input', function() {
        const costValue = parseFloat($itemCost.val());
        if (!isNaN(costValue) && costValue > 0) {
            $itemCost.removeClass('is-invalid');
        }
    });
});
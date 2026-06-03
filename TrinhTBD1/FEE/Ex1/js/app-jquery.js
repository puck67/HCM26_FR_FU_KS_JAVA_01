$(document).ready(function() {
  const $itemNameInput = $('#item-name');
  const $itemCostInput = $('#item-cost');
  const $btnAdd = $('#btn-add');
  const $tableBody = $('#table-body');
  const $grandTotalText = $('#grand-total');
  const $errorBox = $('#error-message');

  const initialItems = [
    { name: 'Apples', cost: 3.49 },
    { name: 'Milk (1 Gallon)', cost: 4.15 },
    { name: 'Bread (Whole Wheat)', cost: 2.79 },
    { name: 'Eggs (Dozen)', cost: 5.20 },
    { name: 'Cereal (Cheerios)', cost: 4.89 }
  ];

  function showError(msg) {
    $errorBox.text(msg).show();
  }

  function clearError() {
    $errorBox.text('').hide();
  }

  $itemNameInput.on('input', clearError);
  $itemCostInput.on('input', clearError);

  function createRowElement(name, cost) {
    const $row = $('<tr></tr>');
    const $tdName = $('<td></td>').text(name);
    const $tdCost = $('<td></td>').addClass('cost-col').text(cost.toFixed(2));
    const $tdAction = $('<td></td>').addClass('action-col');
    const $btnRemove = $('<button></button>')
      .addClass('btn-remove')
      .html('&times; Remove');

    $tdAction.append($btnRemove);
    $row.append($tdName).append($tdCost).append($tdAction);
    return $row;
  }

  function updateTotal() {
    let total = 0;
    $tableBody.find('tr').each(function() {
      const costText = $(this).find('.cost-col').text();
      const costValue = parseFloat(costText);
      if (!isNaN(costValue)) {
        total += costValue;
      }
    });

    $grandTotalText.text('$' + total.toFixed(2));

    if (total > 500) {
      $grandTotalText.css('color', 'red');
    } else {
      $grandTotalText.css('color', '');
    }
  }

  initialItems.forEach(item => {
    const $row = createRowElement(item.name, item.cost);
    $tableBody.append($row);
  });
  updateTotal();

  $btnAdd.on('click', function() {
    const name = $.trim($itemNameInput.val());
    const costRaw = $.trim($itemCostInput.val());

    if (!name) {
      showError('Error: Item name cannot be empty.');
      return;
    }

    if (costRaw === '') {
      showError('Error: Item cost cannot be empty.');
      return;
    }

    const costValue = parseFloat(costRaw);
    if (isNaN(costValue)) {
      showError('Error: Item cost must be a valid number.');
      return;
    }

    if (costValue <= 0) {
      showError('Error: Item cost must be a positive number greater than 0.');
      return;
    }

    clearError();
    const $row = createRowElement(name, costValue);
    $tableBody.append($row);
    updateTotal();

    $itemNameInput.val('');
    $itemCostInput.val('');
    $itemNameInput.focus();
  });

  $tableBody.on('click', '.btn-remove', function() {
    $(this).closest('tr').remove();
    updateTotal();
  });
});

$(document).ready(function () {
  const $itemForm = $('#item-form');
  const $itemNameInput = $('#item-name');
  const $itemCostInput = $('#item-cost');
  const $tableBody = $('#table-body');
  const $grandTotalElement = $('#grand-total');

  function calculateGrandTotal() {
    let total = 0;

    $tableBody.find('tr').each(function () {
      const costText = $(this).find('td').eq(1).text();
      const costValue = parseFloat(costText.replace('$', ''));
      if (!isNaN(costValue)) {
        total += costValue;
      }
    });

    $grandTotalElement.text('$' + total.toFixed(2));

    if (total > 500) {
      $grandTotalElement.css('color', 'red');
    } else {
      $grandTotalElement.css('color', '#9c4221');
    }
  }

  $tableBody.on('click', '.btn-remove', function () {
    $(this).closest('tr').remove();
    calculateGrandTotal();
  });

  $itemForm.on('submit', function (event) {
    event.preventDefault();

    const itemName = $.trim($itemNameInput.val());
    const itemCost = parseFloat($itemCostInput.val());

    // validate
    if (itemName === '') {
      alert('Please enter an item name.');
      return;
    }

    if (isNaN(itemCost) || itemCost <= 0) {
      alert('Please enter a valid cost greater than 0.');
      return;
    }

    const rowHtml = '<tr>' +
      '<td class="col-name">' + itemName + '</td>' +
      '<td class="col-cost">$' + itemCost.toFixed(2) + '</td>' +
      '<td class="col-action">' +
      '<button type="button" class="btn-remove">' +
      '<span class="remove-x">&times; </span>Remove' +
      '</button>' +
      '</td>' +
      '</tr>';

    $tableBody.append(rowHtml);
    calculateGrandTotal();
    $itemForm[0].reset();
    $itemNameInput.focus();
  });

  // auto sum
  calculateGrandTotal();
});

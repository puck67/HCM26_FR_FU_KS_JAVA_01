$(document).ready(function() {
  // Elements targeted via jQuery selectors
  const $form = $('#form-jquery');
  const $nameInput = $('#item-name-jquery');
  const $costInput = $('#item-cost-jquery');
  const $tableBody = $('#tbody-jquery');
  const $grandTotal = $('#total-amount-jquery');
  
  const $errorName = $('#error-name-jquery');
  const $errorCost = $('#error-cost-jquery');

  // Calculate grand total by iterating through table row elements
  function recalculateGrandTotal() {
    let total = 0;
    
    // Find all custom data-cost attributes and add them up
    $tableBody.find('.col-cost-value').each(function() {
      const costValue = parseFloat($(this).attr('data-cost'));
      if (!isNaN(costValue)) {
        total += costValue;
      }
    });

    // Update grand total text
    $grandTotal.text('$' + total.toFixed(2));

    // Dynamic color change based on $500 limit using jQuery .css() method
    if (total > 500) {
      $grandTotal.css('color', '#dc3545'); // Red alert color from Bootstrap/standard red
    } else {
      $grandTotal.css('color', '#9f4d1d'); // Default brownish/dark orange color from mock
    }
  }

  // Bind live input change events to clear errors on typing
  $nameInput.on('input', function() {
    $nameInput.removeClass('is-invalid');
    $errorName.removeClass('active');
  });

  $costInput.on('input', function() {
    $costInput.removeClass('is-invalid');
    $errorCost.removeClass('active');
  });

  // Form submit handler using jQuery .on('submit', ...)
  $form.on('submit', function(e) {
    e.preventDefault();

    // Reset validation errors
    $nameInput.removeClass('is-invalid');
    $costInput.removeClass('is-invalid');
    $errorName.removeClass('active');
    $errorCost.removeClass('active');

    const name = $.trim($nameInput.val());
    const costText = $.trim($costInput.val());
    const cost = parseFloat(costText);

    let hasError = false;

    // 1. Validate Item Name
    if (name === '') {
      $nameInput.addClass('is-invalid');
      $errorName.addClass('active');
      hasError = true;
    }

    // 2. Validate Item Cost
    if (costText === '') {
      $costInput.addClass('is-invalid');
      $errorCost.text('Cost cannot be empty.');
      $errorCost.addClass('active');
      hasError = true;
    } else if (isNaN(cost) || cost <= 0) {
      $costInput.addClass('is-invalid');
      $errorCost.text('Cost must be a valid positive number.');
      $errorCost.addClass('active');
      hasError = true;
    }

    if (hasError) {
      return;
    }

    // 3. Create row content HTML and append to table body using jQuery .append()
    const $row = $('<tr></tr>');
    const $tdName = $('<td class="col-name"></td>').text(name);
    
    const $tdCost = $('<td class="col-cost"></td>');
    const $costSpan = $('<span class="col-cost-value"></span>')
      .attr('data-cost', cost)
      .text('$' + cost.toFixed(2));
    $tdCost.append($costSpan);
    
    const $tdAction = $('<td class="col-action"></td>');
    const $removeBtn = $('<button type="button" class="btn-remove">✖ Remove</button>');
    $tdAction.append($removeBtn);
    
    $row.append($tdName).append($tdCost).append($tdAction);
    $tableBody.append($row);

    // Update total
    recalculateGrandTotal();

    // Reset inputs and focus back
    $nameInput.val('');
    $costInput.val('');
    $nameInput.focus();
  });

  // Action column event delegation for click using jQuery .on()
  $tableBody.on('click', '.btn-remove', function() {
    $(this).closest('tr').remove();
    recalculateGrandTotal();
  });

  // Initial calculation for preloaded items on page load
  recalculateGrandTotal();
});

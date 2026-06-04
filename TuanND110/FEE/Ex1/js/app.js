$(document).ready(() => {
  // DOM references using jQuery
  const $itemNameInput = $('#item-name');
  const $itemCostInput = $('#item-cost');
  const $addItemBtn = $('#add-item-btn');
  const $itemsList = $('#items-list');
  const $grandTotal = $('#grand-total');
  const $validationError = $('#validation-error');

  // Function to show inline error message and highlight inputs
  function showError(message, $invalidInput = null) {
    $validationError.html(`<span style="margin-right: 0.25rem;">⚠️</span> ${message}`).slideDown(200);
    if ($invalidInput) {
      $invalidInput.addClass('budget-app__input--invalid');
    }
  }

  // Clear errors when the user interacts with the input fields
  $('.budget-app__input').on('input focus', function () {
    $(this).removeClass('budget-app__input--invalid');
    if ($('.budget-app__input--invalid').length === 0) {
      $validationError.slideUp(150);
    }
  });

  // Calculate and update grand total using jQuery
  function updateGrandTotal() {
    let total = 0;
    
    // Sum costs from the table cells
    $itemsList.find('.budget-table__cell--cost').each(function () {
      const value = parseFloat($(this).text().replace('$', ''));
      if (!isNaN(value)) {
        total += value;
      }
    });

    // Update text
    $grandTotal.text(`$${total.toFixed(2)}`);

    // Requirement: total cost exceeds $500 -> red text using jQuery's .css() method
    if (total > 500) {
      $grandTotal.css('color', '#ef4444');
      $grandTotal.addClass('total-card__value--danger');
    } else {
      $grandTotal.css('color', ''); // Resets to CSS fallback value
      $grandTotal.removeClass('total-card__value--danger');
    }
  }

  // Add Item Click Event Handler
  $addItemBtn.on('click', (e) => {
    e.preventDefault();

    const name = $itemNameInput.val().trim();
    const costText = $itemCostInput.val().trim();
    const cost = parseFloat(costText);

    // Validate inputs
    if (!name) {
      showError('Please enter an item name.', $itemNameInput);
      return;
    }
    if (isNaN(cost) || cost <= 0) {
      showError('Please enter a valid positive number for item cost.', $itemCostInput);
      return;
    }

    // Append new row using jQuery
    const newRowHTML = `
      <tr class="budget-table__row budget-table__row--item">
        <td class="budget-table__cell budget-table__cell--name">${name}</td>
        <td class="budget-table__cell budget-table__cell--cost">$${cost.toFixed(2)}</td>
        <td class="budget-table__cell budget-table__cell--action">
          <button class="budget-table__remove-btn"><span class="remove-icon">✕</span> Remove</button>
        </td>
      </tr>
    `;
    $itemsList.append(newRowHTML);

    // Clear inputs
    $itemNameInput.val('');
    $itemCostInput.val('');

    // Update total
    updateGrandTotal();
  });

  // Remove Item Event Delegation Handler using jQuery
  $itemsList.on('click', '.budget-table__remove-btn', function (e) {
    e.preventDefault();
    
    // Remove closest table row using jQuery
    $(this).closest('tr').remove();
    
    // Recalculate total
    updateGrandTotal();
  });

  // Initial update on page load
  updateGrandTotal();
});

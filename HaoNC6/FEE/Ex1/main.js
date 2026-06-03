
const btnAdd    = document.getElementById('btnAdd');
const inpName   = document.getElementById('itemName');
const inpCost   = document.getElementById('itemCost');
const tableBody = document.getElementById('tableBody');
const errorMsg  = document.getElementById('errorMsg');

function recalculateTotal() {
  const cells = tableBody.querySelectorAll('.cost-cell');
  let sum = 0;
  cells.forEach(function (td) {
    sum += parseFloat(td.dataset.cost);
  });

  updateTotalDisplay(sum);
}

/**
 * @param {string} name  
 * @param {number} cost 
 * @returns {HTMLTableRowElement}
 */
function createRow(name, cost) {
  const tr      = document.createElement('tr');
  const tdName  = document.createElement('td');
  const tdCost  = document.createElement('td');
  const tdAct   = document.createElement('td');
  const btnRm   = document.createElement('button');

  tdName.textContent   = name;

  tdCost.className     = 'cost-cell';
  tdCost.dataset.cost  = cost;
  tdCost.textContent   = parseFloat(cost).toFixed(2);

  btnRm.className   = 'btn-remove';
  btnRm.textContent = '✕ Remove';

  btnRm.addEventListener('click', function () {
    tableBody.removeChild(tr);   
    recalculateTotal();
  });

  tdAct.appendChild(btnRm);
  tr.appendChild(tdName);
  tr.appendChild(tdCost);
  tr.appendChild(tdAct);

  return tr;
}


function handleAddItem() {
  const name = inpName.value.trim();
  const cost = parseFloat(inpCost.value);

  if (!name) {
    errorMsg.textContent = '⚠ Item Name cannot be empty.';
    inpName.focus();
    return;
  }
  if (isNaN(cost) || cost <= 0) {
    errorMsg.textContent = '⚠ Cost must be a valid positive number.';
    inpCost.focus();
    return;
  }

  errorMsg.textContent = '';

  const newRow = createRow(name, cost);
  tableBody.appendChild(newRow); 

  recalculateTotal();

  inpName.value = '';
  inpCost.value = '';
  inpName.focus();
}

btnAdd.addEventListener('click', handleAddItem);

[inpName, inpCost].forEach(function (el) {
  el.addEventListener('keydown', function (e) {
    if (e.key === 'Enter') handleAddItem();
  });
});

/**
 * @param {number} total
 */
function updateTotalDisplay(total) {
  const $el = $('#grandTotal');           
  $el.text('$' + total.toFixed(2));
  $el.css('color', total > 500 ? '#e53e3e' : '#b45309');
}

$(function () {

  $('#btnAdd').on('click', function () {
    const name = $('#itemName').val().trim();   
    const cost = parseFloat($('#itemCost').val());

    if (!name) {
      $('#errorMsg').text('⚠ Item Name cannot be empty.');
      $('#itemName').focus();
      return;
    }
    if (isNaN(cost) || cost <= 0) {
      $('#errorMsg').text('⚠ Cost must be a valid positive number.');
      $('#itemCost').focus();
      return;
    }

    $('#errorMsg').text('');

    const safeName = $('<span>').text(name).html();
    $('#tableBody').append(                      
      '<tr>' +
        '<td>' + safeName + '</td>' +
        '<td class="cost-cell" data-cost="' + cost + '">' + cost.toFixed(2) + '</td>' +
        '<td><button class="btn-remove">✕ Remove</button></td>' +
      '</tr>'
    );

    recalculateTotal();
    $('#itemName').val('');
    $('#itemCost').val('');
    $('#itemName').focus();
  });

  $('#tableBody').on('click', '.btn-remove', function () {
    $(this).closest('tr').remove();   
    recalculateTotal();
  });

  recalculateTotal();

}); 

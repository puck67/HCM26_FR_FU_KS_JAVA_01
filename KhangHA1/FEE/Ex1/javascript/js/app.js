const itemNameInput = document.getElementById("itemName");
const itemCostInput = document.getElementById("itemCost");
const addBtn = document.getElementById("addBtn");

const tbody = document.getElementById("itemTableBody");
const totalElement = document.getElementById("total");

let grandTotal = 0;

addBtn.addEventListener("click", addItem);

function addItem() {
    const name = itemNameInput.value.trim();
    const cost = parseFloat(itemCostInput.value);

    if (!validateInput(name, cost)) {
        return;
    }

    const row = document.createElement("tr");

    const nameCell = document.createElement("td");
    const costCell = document.createElement("td");
    const actionCell = document.createElement("td");

    nameCell.textContent = name;
    costCell.textContent = cost.toFixed(2);

    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Remove";

    removeBtn.classList.add("remove-btn");

    removeBtn.addEventListener("click", function () {
        row.remove();
        grandTotal -= cost;
        updateTotal();
    });

    actionCell.appendChild(removeBtn);

    row.appendChild(nameCell);
    row.appendChild(costCell);
    row.appendChild(actionCell);

    tbody.appendChild(row);

    grandTotal += cost;
    updateTotal();

    itemNameInput.value = "";
    itemCostInput.value = "";
}

function validateInput(name, cost) {

    if (name.trim() === "") {
        alert("Item name is required");
        return false;
    }

    if (isNaN(cost) || cost <= 0) {
        alert("Cost must be positive");
        return false;
    }

    return true;
}

function updateTotal() {
    totalElement.textContent =
        "$" + grandTotal.toFixed(2);
}

document.addEventListener("DOMContentLoaded", function () {

    const itemNameInput = document.getElementById("itemName");
    const itemCostInput = document.getElementById("itemCost");
    const addBtn = document.getElementById("addBtn");
    const tableBody = document.getElementById("itemTableBody");
    const grandTotal = document.getElementById("grandTotal");

    // Calculate initial total
    updateGrandTotal();

    addBtn.addEventListener("click", function () {

        const itemName = itemNameInput.value.trim();
        const itemCost = parseFloat(itemCostInput.value);

        // Validation
        if (itemName === "") {
            alert("Item Name cannot be empty");
            return;
        }

        if (isNaN(itemCost) || itemCost <= 0) {
            alert("Item Cost must be a positive number");
            return;
        }

        // Create row
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        nameCell.textContent = itemName;

        const costCell = document.createElement("td");
        costCell.textContent = itemCost.toFixed(2);

        const actionCell = document.createElement("td");

        const removeBtn = document.createElement("button");
        removeBtn.textContent = "✖ Remove";
        removeBtn.className = "remove-btn";

        removeBtn.addEventListener("click", function () {
            row.remove();
            updateGrandTotal();
        });

        actionCell.appendChild(removeBtn);

        row.appendChild(nameCell);
        row.appendChild(costCell);
        row.appendChild(actionCell);

        tableBody.appendChild(row);

        updateGrandTotal();

        itemNameInput.value = "";
        itemCostInput.value = "";
    });

    // Existing remove buttons
    document.querySelectorAll(".remove-btn").forEach(button => {
        button.addEventListener("click", function () {
            this.closest("tr").remove();
            updateGrandTotal();
        });
    });

    function updateGrandTotal() {

        let total = 0;

        const rows = tableBody.querySelectorAll("tr");

        rows.forEach(row => {
            const cost = parseFloat(row.children[1].textContent);
            total += cost;
        });

        grandTotal.textContent = "$" + total.toFixed(2);
    }

});
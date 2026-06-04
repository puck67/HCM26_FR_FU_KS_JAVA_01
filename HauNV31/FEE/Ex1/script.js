// Problem 02 - Pure JavaScript Implementation
document.addEventListener("DOMContentLoaded", function() {
    const addItemBtn = document.getElementById("addItemBtn");
    const itemNameInput = document.getElementById("itemName");
    const itemCostInput = document.getElementById("itemCost");
    const shoppingBody = document.getElementById("shoppingBody");
    const grandTotalEl = document.getElementById("grandTotal");
    
    let grandTotal = 0;

    function updateGrandTotal(amount) {
        grandTotal += amount;
        grandTotalEl.textContent = "$" + grandTotal.toFixed(2);
        
        // This is part of Problem 3 but added here for consistency if used
        if (grandTotal > 500) {
            grandTotalEl.style.color = "red";
        } else {
            grandTotalEl.style.color = "#a04000"; // default color
        }
    }

    addItemBtn.addEventListener("click", function() {
        const itemName = itemNameInput.value.trim();
        const itemCost = parseFloat(itemCostInput.value);

        if (itemName === "") {
            alert("Please enter an item name.");
            return;
        }

        if (isNaN(itemCost) || itemCost <= 0) {
            alert("Please enter a valid positive cost.");
            return;
        }

        // Create row
        const tr = document.createElement("tr");

        // Item Name cell
        const tdName = document.createElement("td");
        tdName.textContent = itemName;
        tr.appendChild(tdName);

        // Item Cost cell
        const tdCost = document.createElement("td");
        tdCost.textContent = itemCost.toFixed(2);
        tr.appendChild(tdCost);

        // Action cell
        const tdAction = document.createElement("td");
        const removeBtn = document.createElement("button");
        removeBtn.className = "remove-btn";
        removeBtn.textContent = "✘ Remove";
        
        // Remove functionality
        removeBtn.addEventListener("click", function() {
            shoppingBody.removeChild(tr);
            updateGrandTotal(-itemCost);
        });

        tdAction.appendChild(removeBtn);
        tr.appendChild(tdAction);

        // Append to table
        shoppingBody.appendChild(tr);

        // Update Total
        updateGrandTotal(itemCost);

        // Clear inputs
        itemNameInput.value = "";
        itemCostInput.value = "";
    });
});

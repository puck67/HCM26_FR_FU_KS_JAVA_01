$(document).ready(function () {

    let grandTotal = 0;

    $("#addBtn").on("click", function () {

        const name = $("#itemName").val().trim();
        const cost = parseFloat($("#itemCost").val());

        // Validation
        if (name === "") {
            alert("Item name is required");
            return;
        }

        if (isNaN(cost) || cost <= 0) {
            alert("Cost must be a positive number");
            return;
        }

        // Create row
        const row = `
            <tr>
                <td>${name}</td>
                <td>${cost.toFixed(2)}</td>
                <td>
                    <button class="remove-btn">
                        Remove
                    </button>
                </td>
            </tr>
        `;

        $("#itemTableBody").append(row);

        grandTotal += cost;

        updateTotal();

        $("#itemName").val("");
        $("#itemCost").val("");
    });

    // Remove button (event delegation)
    $("#itemTableBody").on("click", ".remove-btn", function () {

        const row = $(this).closest("tr");

        const cost = parseFloat(
            row.find("td:eq(1)").text()
        );

        grandTotal -= cost;

        row.remove();

        updateTotal();
    });

    function updateTotal() {

        $("#total").text("$" + grandTotal.toFixed(2));

        // Requirement:
        // Red when total > 500

        if (grandTotal > 500) {

            $("#total").css("color", "red");

        } else {

            $("#total").css("color", "#b34a00");
        }
    }

});
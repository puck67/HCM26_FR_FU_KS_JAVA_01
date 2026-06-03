$(document).ready(function () {

    updateTotal();

    $("#addBtn").click(function () {

        let itemName = $("#itemName").val().trim();
        let itemCost = $("#itemCost").val().trim();

        if (itemName === "") {
            alert("Item name cannot be empty!");
            return;
        }

        if (itemCost === "" || isNaN(itemCost) || Number(itemCost) <= 0) {
            alert("Please enter a valid positive cost!");
            return;
        }

        let newRow = `
            <tr>
                <td>${itemName}</td>
                <td>${parseFloat(itemCost).toFixed(2)}</td>
                <td>
                    <button class="remove-btn">
                        ✖ Remove
                    </button>
                </td>
            </tr>
        `;

        $("#itemTableBody").append(newRow);

        $("#itemName").val("");
        $("#itemCost").val("");

        updateTotal();
    });

    $(document).on("click", ".remove-btn", function () {

        $(this).closest("tr").remove();

        updateTotal();
    });

    function updateTotal() {

        let total = 0;

        $("#itemTableBody tr").each(function () {

            let cost = parseFloat(
                $(this).find("td:eq(1)").text()
            );

            total += cost;
        });

        $("#grandTotal").text("$" + total.toFixed(2));
    }

});
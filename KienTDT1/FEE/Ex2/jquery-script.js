function createTask(title, targetColumnId) {

    let buttonHtml = `
        <button class="move-btn">
            Move
        </button>
    `;

    if (targetColumnId === "doneTasks") {

        buttonHtml = `
            <button class="delete-btn">
                Delete
            </button>
        `;
    }

    const card = `
        <div class="task-card">

            <h3>${title}</h3>

            <div class="actions">
                ${buttonHtml}
            </div>

        </div>
    `;

    $(card).appendTo(`#${targetColumnId}`);
}

$(".add-btn").on("click", function () {

    const column = $(this).closest(".column");
    const input = column.find(".task-input");

    const title = input.val().trim();

    if (!title) {
        return;
    }

    createTask(title, column.attr("id"));

    input.val("");
});

$(".task-input").on("keypress", function (e) {

    if (e.which === 13) {

        const column = $(this).closest(".column");

        const title = $(this).val().trim();

        if (!title) {
            return;
        }

        createTask(title, column.attr("id"));

        $(this).val("");
    }
});

$("#board").on("click", ".move-btn", function () {

    const card = $(this).closest(".task-card");

    const currentColumn =
        card.parent().attr("id");

    if (currentColumn === "todoTasks") {

        card.appendTo("#progressTasks");
    }

    else if (currentColumn === "progressTasks") {

        card.find(".actions").html(`
            <button class="delete-btn">
                Delete
            </button>
        `);

        card.appendTo("#doneTasks");
    }

});

$("#board").on("click", ".delete-btn", function () {

    $(this)
        .closest(".task-card")
        .remove();

});

$("#searchInput").on("keyup", function () {

    const keyword =
        $(this).val().toLowerCase();

    $(".task-card").each(function () {

        const text =
            $(this).text().toLowerCase();

        if (text.includes(keyword)) {

            $(this).show();

        } else {

            $(this).hide();
        }

    });

});
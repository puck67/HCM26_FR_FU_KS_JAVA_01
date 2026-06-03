function createTask(title, targetColumn) {

    const card = document.createElement("div");
    card.className = "task-card";

    let buttonText = "Move";
    let buttonClass = "move-btn";

    if (targetColumn.id === "doneTasks") {
        buttonText = "Delete";
        buttonClass = "delete-btn";
    }

    card.innerHTML = `
        <h3>${title}</h3>

        <div class="actions">
            <button class="${buttonClass}">
                ${buttonText}
            </button>
        </div>
    `;

    targetColumn.appendChild(card);
}

document.querySelectorAll(".column").forEach(column => {

    const input = column.querySelector(".task-input");
    const addBtn = column.querySelector(".add-btn");

    function addTask() {

        const title = input.value.trim();

        if (!title) {
            return;
        }

        createTask(title, column);

        input.value = "";
    }

    addBtn.addEventListener("click", addTask);

    input.addEventListener("keydown", function (e) {

        if (e.key === "Enter") {
            addTask();
        }

    });

});

document.addEventListener("click", function (e) {

    const target = e.target;

    if (target.classList.contains("move-btn")) {

        const card = target.closest(".task-card");
        const column = card.parentNode;

        if (column.id === "todoTasks") {

            document
                .getElementById("progressTasks")
                .appendChild(card);
        }

        else if (column.id === "progressTasks") {

            card.querySelector(".actions").innerHTML = `
                <button class="delete-btn">
                    Delete
                </button>
            `;

            document
                .getElementById("doneTasks")
                .appendChild(card);
        }
    }

    if (target.classList.contains("delete-btn")) {

        const card = target.closest(".task-card");

        card.parentNode.removeChild(card);
    }

});
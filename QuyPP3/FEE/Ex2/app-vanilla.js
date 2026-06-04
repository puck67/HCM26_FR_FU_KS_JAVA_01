// Problem 02 - Vanilla JS
document.addEventListener("DOMContentLoaded", () => {

    const columns = document.querySelectorAll('.kanban-column');

    columns.forEach(column => {
        const input = column.querySelector('.task-input');
        const addButton = column.querySelector('.btn-add');

        addButton.addEventListener('click', () => {
            handleAddTask(input, column);
        });

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                handleAddTask(input, column);
            }
        });

        input.addEventListener('input', () => {
            input.classList.remove('error');
        });
    });

    function handleAddTask(inputElement, columnElement) {
        const taskText = inputElement.value.trim();

        if (taskText === "") {
            inputElement.classList.add('error');
            inputElement.focus();
            return;
        }

        const taskCard = document.createElement('div');
        taskCard.className = 'task-card';

        const label = document.createElement('span');
        label.className = 'label-title';
        label.textContent = 'Title';

        const title = document.createElement('div');
        title.className = 'task-title';
        title.textContent = taskText;

        const actionsDiv = document.createElement('div');
        actionsDiv.className = 'card-actions';

        const currentStatus = columnElement.getAttribute('data-status');
        if (currentStatus === 'done') {
            createDeleteButton(actionsDiv, taskCard);
        } else {
            createMoveButton(actionsDiv, taskCard);
        }

        taskCard.appendChild(label);
        taskCard.appendChild(title);
        taskCard.appendChild(actionsDiv);

        const taskList = columnElement.querySelector('.task-list');
        taskList.appendChild(taskCard);

        inputElement.value = "";
        triggerRealtimeSearch();
    }

    function createMoveButton(container, cardElement) {
        const moveBtn = document.createElement('button');
        moveBtn.className = 'btn-move';
        moveBtn.textContent = 'Move';

        moveBtn.addEventListener('click', () => {
            const currentColumn = cardElement.parentNode.parentNode;
            const currentStatus = currentColumn.getAttribute('data-status');

            let nextColumnId = '';
            if (currentStatus === 'todo') {
                nextColumnId = 'col-progress';
            } else if (currentStatus === 'progress') {
                nextColumnId = 'col-done';
            }

            if (nextColumnId) {
                const nextColumn = document.getElementById(nextColumnId);
                const nextList = nextColumn.querySelector('.task-list');

                nextList.appendChild(cardElement);

                if (nextColumnId === 'col-done') {
                    container.innerHTML = '';
                    createDeleteButton(container, cardElement);
                }
            }
        });

        container.appendChild(moveBtn);
    }

    function createDeleteButton(container, cardElement) {
        const deleteBtn = document.createElement('button');
        deleteBtn.className = 'btn-delete';
        deleteBtn.textContent = 'Delete';

        deleteBtn.addEventListener('click', () => {
            const parentList = cardElement.parentNode;
            if (parentList) {
                parentList.removeChild(cardElement);
            }
        });

        container.appendChild(deleteBtn);
    }

    // --- Event Delegation for dynamically added cards ---
    const board = document.getElementById('board-container');

    board.addEventListener('click', (e) => {
        if (e.target.classList.contains('btn-move')) {
            const card = e.target.closest('.task-card');
            const currentColumn = e.target.closest('.kanban-column');
            const currentStatus = currentColumn.getAttribute('data-status');
            const actionsDiv = card.querySelector('.card-actions');

            if (currentStatus === 'todo') {
                document.querySelector('#col-progress .task-list').appendChild(card);
            } else if (currentStatus === 'progress') {
                document.querySelector('#col-done .task-list').appendChild(card);
                actionsDiv.innerHTML = '';
                createDeleteButton(actionsDiv, card);
            }
        }

        if (e.target.classList.contains('btn-delete')) {
            const card = e.target.closest('.task-card');
            card.parentNode.removeChild(card);
        }
    });

    // --- Real-time Search ---
    const searchInput = document.getElementById('global-search');
    if (searchInput) {
        searchInput.addEventListener('input', triggerRealtimeSearch);
    }

    function triggerRealtimeSearch() {
        const query = document.getElementById('global-search').value.toLowerCase().trim();
        document.querySelectorAll('.task-card').forEach(card => {
            const title = card.querySelector('.task-title').textContent.toLowerCase();
            card.style.display = title.includes(query) ? '' : 'none';
        });
    }
});

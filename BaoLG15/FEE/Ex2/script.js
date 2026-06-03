document.addEventListener('DOMContentLoaded', () => {
    const columns = document.querySelectorAll('.column');

    columns.forEach(column => {
        const input = column.querySelector('.add-task-input');
        const addBtn = column.querySelector('.add-task-btn');

        const handleAddTask = () => {
            const title = input.value.trim();
            if (title) {
                createTaskCard(column, title);
                input.value = '';
            }
        };

        addBtn.addEventListener('click', handleAddTask);

        input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                handleAddTask();
            }
        });
    });

    const board = document.querySelector('.kanban-board');
    board.addEventListener('click', (e) => {
        if (e.target.classList.contains('btn-move')) {
            moveTask(e.target);
        } else if (e.target.classList.contains('btn-delete')) {
            deleteTask(e.target);
        }
    });

    function createTaskCard(column, title) {
        const taskList = column.querySelector('.task-list');
        const colId = column.dataset.column;

        const card = document.createElement('div');
        card.className = 'task-card';

        const titleLabel = document.createElement('div');
        titleLabel.className = 'task-title-label';
        titleLabel.textContent = 'Title';

        const titleDiv = document.createElement('div');
        titleDiv.className = 'task-title';
        titleDiv.textContent = title;

        const actionsDiv = document.createElement('div');
        actionsDiv.className = 'task-actions';

        let buttonsHTML = '';
        if (colId === 'todo') {
            buttonsHTML = `
                <button class="task-btn btn-primary">Start</button>
                <button class="task-btn">Edit</button>
                <button class="task-btn btn-move">Move ▾</button>
            `;
        } else if (colId === 'progress') {
            buttonsHTML = `
                <button class="task-btn">Edit</button>
                <button class="task-btn btn-move">Move ▾</button>
                <button class="task-btn btn-success">Done</button>
            `;
        } else if (colId === 'done') {
            buttonsHTML = `
                <button class="task-btn">Edit</button>
                <button class="task-btn">Archive</button>
                <button class="task-btn btn-delete">Delete</button>
            `;
        }
        actionsDiv.innerHTML = buttonsHTML;
        card.appendChild(titleLabel);
        card.appendChild(titleDiv);
        card.appendChild(actionsDiv);

        taskList.appendChild(card);
    }

    function moveTask(btn) {
        const card = btn.parentNode.parentNode;
        const currentList = card.parentNode;
        const currentColumn = currentList.parentNode;
        const colId = currentColumn.dataset.column;

        let nextColId = '';
        if (colId === 'todo') {
            nextColId = 'progress';
        } else if (colId === 'progress') {
            nextColId = 'done';
        }

        if (nextColId) {
            const nextColumn = document.querySelector(`.column[data-column="${nextColId}"]`);
            const nextList = nextColumn.querySelector('.task-list');

            currentList.removeChild(card);
            nextList.appendChild(card);

            const actionsDiv = card.querySelector('.task-actions');
            if (nextColId === 'progress') {
                actionsDiv.innerHTML = `
                    <button class="task-btn">Edit</button>
                    <button class="task-btn btn-move">Move ▾</button>
                    <button class="task-btn btn-success">Done</button>
                `;
            } else if (nextColId === 'done') {
                actionsDiv.innerHTML = `
                    <button class="task-btn">Edit</button>
                    <button class="task-btn">Archive</button>
                    <button class="task-btn btn-delete">Delete</button>
                `;
            }
        }
    }

    function deleteTask(btn) {
        const card = btn.parentNode.parentNode;
        const currentList = card.parentNode;
        currentList.removeChild(card);
    }
});

/**
 * app.js - Native JavaScript Implementation for Kanban Lite
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Get references to forms, input fields, and lists for each column
    const todoForm = document.getElementById('todo-form');
    const todoInput = document.getElementById('todo-input');
    const todoList = document.getElementById('todo-list');

    const inprogressForm = document.getElementById('inprogress-form');
    const inprogressInput = document.getElementById('inprogress-input');
    const inprogressList = document.getElementById('inprogress-list');

    const doneForm = document.getElementById('done-form');
    const doneInput = document.getElementById('done-input');
    const doneList = document.getElementById('done-list');

    function updateCardButtons(card, columnId) {
        const actionsDiv = card.querySelector('.card-actions');
        if (!actionsDiv) return;
        actionsDiv.innerHTML = ''; // Clear existing buttons

        const titleSpan = card.querySelector('.task-title');

        if (columnId === 'todo-list') {
            // buttons: start, edit, move
            const startBtn = document.createElement('button');
            startBtn.textContent = 'Start';
            startBtn.className = 'start-btn';
            startBtn.onclick = () => {
                const targetColumn = document.getElementById('inprogress-list');
                targetColumn.appendChild(card);
                updateCardButtons(card, 'inprogress-list');
            };
            actionsDiv.appendChild(startBtn);

            const editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.className = 'edit-btn';
            editBtn.onclick = () => {
                const newTitle = prompt('Edit task title:', titleSpan.textContent);
                if (newTitle !== null && newTitle.trim() !== '') {
                    titleSpan.textContent = newTitle.trim();
                }
            };
            actionsDiv.appendChild(editBtn);

            const moveBtn = document.createElement('button');
            moveBtn.textContent = 'Move';
            moveBtn.className = 'move-btn';
            moveBtn.onclick = () => {
                const targetColumn = document.getElementById('inprogress-list');
                targetColumn.appendChild(card);
                updateCardButtons(card, 'inprogress-list');
            };
            actionsDiv.appendChild(moveBtn);
        } else if (columnId === 'inprogress-list') {
            // buttons: edit, move, done
            const editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.className = 'edit-btn';
            editBtn.onclick = () => {
                const newTitle = prompt('Edit task title:', titleSpan.textContent);
                if (newTitle !== null && newTitle.trim() !== '') {
                    titleSpan.textContent = newTitle.trim();
                }
            };
            actionsDiv.appendChild(editBtn);

            const moveBtn = document.createElement('button');
            moveBtn.textContent = 'Move';
            moveBtn.className = 'move-btn';
            moveBtn.onclick = () => {
                const targetColumn = document.getElementById('done-list');
                targetColumn.appendChild(card);
                updateCardButtons(card, 'done-list');
            };
            actionsDiv.appendChild(moveBtn);

            const doneBtn = document.createElement('button');
            doneBtn.textContent = 'Done';
            doneBtn.className = 'done-btn';
            doneBtn.onclick = () => {
                const targetColumn = document.getElementById('done-list');
                targetColumn.appendChild(card);
                updateCardButtons(card, 'done-list');
            };
            actionsDiv.appendChild(doneBtn);
        } else if (columnId === 'done-list') {
            // buttons: edit, archive, delete
            const editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.className = 'edit-btn';
            editBtn.onclick = () => {
                const newTitle = prompt('Edit task title:', titleSpan.textContent);
                if (newTitle !== null && newTitle.trim() !== '') {
                    titleSpan.textContent = newTitle.trim();
                }
            };
            actionsDiv.appendChild(editBtn);

            const archiveBtn = document.createElement('button');
            archiveBtn.textContent = 'Archive';
            archiveBtn.className = 'archive-btn';
            archiveBtn.onclick = () => {
                addArchivedTask(titleSpan.textContent);
                card.remove();
            };
            actionsDiv.appendChild(archiveBtn);

            const deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.className = 'delete-btn';
            deleteBtn.onclick = () => {
                card.remove();
            };
            actionsDiv.appendChild(deleteBtn);
        }
    }

    /**
     * Create a task card element dynamically
     * @param {string} text - The title/content of the task
     * @param {string} initialColumnId - The ID of the column list where the task starts
     * @returns {HTMLDivElement} - The fully constructed card element
     */
    function createTaskCard(text, initialColumnId) {
        // Create the card container div
        const card = document.createElement('div');
        card.className = 'task-card';

        // Create the text element for the card
        const titleSpan = document.createElement('span');
        titleSpan.className = 'task-title';
        titleSpan.textContent = text;
        card.appendChild(titleSpan);

        // Create container for action buttons
        const actionsDiv = document.createElement('div');
        actionsDiv.className = 'card-actions';
        card.appendChild(actionsDiv);

        // Setup the initial button layout
        updateCardButtons(card, initialColumnId);

        return card;
    }

    /**
     * Setup form submission handler for a specific column
     * @param {HTMLFormElement} form - The column's form element
     * @param {HTMLInputElement} input - The column's input field
     * @param {HTMLDivElement} listContainer - The container list where cards are placed
     * @param {string} columnId - The ID of the column list
     */
    function setupFormHandler(form, input, listContainer, columnId) {
        form.addEventListener('submit', (event) => {
            // Prevent page refresh on submit
            event.preventDefault();
            
            const taskText = input.value.trim();
            if (taskText) {
                // Create a card and append it to the current column list
                const card = createTaskCard(taskText, columnId);
                listContainer.appendChild(card);
                
                // Clear the input field
                input.value = '';
            }
        });
    }

    // Initialize forms for all three columns
    setupFormHandler(todoForm, todoInput, todoList, 'todo-list');
    setupFormHandler(inprogressForm, inprogressInput, inprogressList, 'inprogress-list');
    setupFormHandler(doneForm, doneInput, doneList, 'done-list');

    // Archive Logic
    const archiveBtnToggle = document.getElementById('view-archive-btn');
    const archiveBtnClose = document.getElementById('close-archive-btn');
    const drawerOverlay = document.getElementById('drawer-overlay');
    const archiveDrawer = document.getElementById('archive-drawer');
    const archiveList = document.getElementById('archive-list');
    const archiveCount = document.getElementById('archive-count');

    function getArchivedTasks() {
        const stored = localStorage.getItem('kanban_archived_tasks');
        return stored ? JSON.parse(stored) : [];
    }

    function saveArchivedTasks(tasks) {
        localStorage.setItem('kanban_archived_tasks', JSON.stringify(tasks));
    }

    function renderArchiveList() {
        const tasks = getArchivedTasks();
        archiveCount.textContent = tasks.length;
        archiveList.innerHTML = '';

        if (tasks.length === 0) {
            const emptyMsg = document.createElement('div');
            emptyMsg.className = 'empty-message';
            emptyMsg.style.textAlign = 'center';
            emptyMsg.style.color = '#a0aec0';
            emptyMsg.style.padding = '20px 0';
            emptyMsg.textContent = 'No archived tasks';
            archiveList.appendChild(emptyMsg);
            return;
        }

        tasks.forEach((taskText, index) => {
            const card = document.createElement('div');
            card.className = 'archived-card';

            const title = document.createElement('span');
            title.className = 'task-title';
            title.textContent = taskText;
            card.appendChild(title);

            const actions = document.createElement('div');
            actions.className = 'card-actions';

            const restoreBtn = document.createElement('button');
            restoreBtn.className = 'restore-btn';
            restoreBtn.textContent = 'Restore';
            restoreBtn.onclick = () => {
                // Restore to 'Done' column
                const doneListContainer = document.getElementById('done-list');
                const restoredCard = createTaskCard(taskText, 'done-list');
                doneListContainer.appendChild(restoredCard);

                // Remove from archive
                tasks.splice(index, 1);
                saveArchivedTasks(tasks);
                renderArchiveList();
            };
            actions.appendChild(restoreBtn);

            const deleteBtn = document.createElement('button');
            deleteBtn.className = 'delete-btn';
            deleteBtn.textContent = 'Delete';
            deleteBtn.onclick = () => {
                tasks.splice(index, 1);
                saveArchivedTasks(tasks);
                renderArchiveList();
            };
            actions.appendChild(deleteBtn);

            card.appendChild(actions);
            archiveList.appendChild(card);
        });
    }

    function addArchivedTask(taskText) {
        const tasks = getArchivedTasks();
        tasks.push(taskText);
        saveArchivedTasks(tasks);
        renderArchiveList();
    }

    // Toggle Drawer
    if (archiveBtnToggle) {
        archiveBtnToggle.onclick = () => {
            archiveDrawer.classList.add('open');
            drawerOverlay.classList.add('show');
        };
    }

    if (archiveBtnClose) {
        archiveBtnClose.onclick = () => {
            archiveDrawer.classList.remove('open');
            drawerOverlay.classList.remove('show');
        };
    }

    if (drawerOverlay) {
        drawerOverlay.onclick = () => {
            archiveDrawer.classList.remove('open');
            drawerOverlay.classList.remove('show');
        };
    }

    // Initial load
    renderArchiveList();
});

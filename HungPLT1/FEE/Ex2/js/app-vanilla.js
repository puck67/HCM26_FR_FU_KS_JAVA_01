/**
 * FEE - Exercise 2: Kanban Board Lite (Native JS Version)
 * Author: HungPLT1
 * Description: Manages task creation, DOM movement, and deletion using native DOM APIs.
 */

document.addEventListener('DOMContentLoaded', () => {
    // DOM Elements - Inputs & Buttons
    const inputTodo = document.getElementById('input-todo');
    const btnAddTodo = document.getElementById('btn-add-todo');
    const inputInprogress = document.getElementById('input-inprogress');
    const btnAddInprogress = document.getElementById('btn-add-inprogress');
    const inputDone = document.getElementById('input-done');
    const btnAddDone = document.getElementById('btn-add-done');

    // DOM Elements - List Containers
    const listTodo = document.getElementById('list-todo');
    const listInprogress = document.getElementById('list-inprogress');
    const listDone = document.getElementById('list-done');

    // DOM Elements - Column Counts
    const countTodo = document.getElementById('count-todo');
    const countInprogress = document.getElementById('count-inprogress');
    const countDone = document.getElementById('count-done');

    // Helper to update column task counters and placeholders
    const updateColumnStates = () => {
        const todoCardsCount = listTodo.querySelectorAll('.kanban-card').length;
        const inprogressCardsCount = listInprogress.querySelectorAll('.kanban-card').length;
        const doneCardsCount = listDone.querySelectorAll('.kanban-card').length;

        // Update badge text
        countTodo.textContent = todoCardsCount;
        countInprogress.textContent = inprogressCardsCount;
        countDone.textContent = doneCardsCount;

        // Toggle empty placeholder for TO DO
        togglePlaceholder(listTodo, todoCardsCount, 'Không có công việc cần làm');
        // Toggle empty placeholder for IN PROGRESS
        togglePlaceholder(listInprogress, inprogressCardsCount, 'Không có công việc đang thực hiện');
        // Toggle empty placeholder for DONE
        togglePlaceholder(listDone, doneCardsCount, 'Không có công việc đã hoàn thành');
    };

    // Toggle empty placeholder helper
    const togglePlaceholder = (container, count, message) => {
        let placeholder = container.querySelector('.no-tasks');
        if (count === 0) {
            if (!placeholder) {
                placeholder = document.createElement('div');
                placeholder.className = 'no-tasks';
                placeholder.textContent = message;
                container.appendChild(placeholder);
            }
        } else {
            if (placeholder) {
                container.removeChild(placeholder);
            }
        }
    };

    /**
     * Create a new Kanban card element
     * @param {string} titleText - The title of the task
     * @param {string} initialColumn - 'todo', 'inprogress', or 'done'
     */
    const createCardElement = (titleText, initialColumn) => {
        // Create card container
        const card = document.createElement('div');
        card.className = 'kanban-card';

        // Meta tag (e.g., Task ID or prefix)
        const meta = document.createElement('div');
        meta.className = 'card-meta';
        meta.textContent = `Task #${Date.now().toString().slice(-4)}`;
        card.appendChild(meta);

        // Card Title
        const title = document.createElement('div');
        title.className = 'card-title';
        title.textContent = titleText;
        card.appendChild(title);

        // Actions Container
        const actions = document.createElement('div');
        actions.className = 'card-actions';

        // Active Edit button with inline editing logic
        const btnEdit = document.createElement('button');
        btnEdit.className = 'btn-card btn-edit';
        btnEdit.textContent = 'Edit';
        
        let isEditing = false;
        let editInput = null;

        btnEdit.addEventListener('click', (e) => {
            e.stopPropagation();
            if (!isEditing) {
                // Chuyển sang chế độ chỉnh sửa (Edit Mode)
                isEditing = true;
                btnEdit.textContent = 'Save';
                btnEdit.style.backgroundColor = 'var(--done-accent)'; // Màu xanh lá cho nút Save
                btnEdit.style.color = 'white';

                const currentText = title.textContent;
                editInput = document.createElement('input');
                editInput.type = 'text';
                editInput.className = 'card-edit-input';
                editInput.value = currentText;
                
                title.textContent = '';
                title.appendChild(editInput);
                editInput.focus();

                // Nhấn Enter trong ô nhập để lưu lại
                editInput.addEventListener('keydown', (evt) => {
                    if (evt.key === 'Enter') {
                        evt.preventDefault();
                        evt.stopPropagation();
                        btnEdit.click(); // Kích hoạt sự kiện lưu
                    }
                });
            } else {
                // Chế độ lưu (Save Mode)
                const newText = editInput.value.trim();
                
                // Validation ô sửa đổi
                if (newText === '') {
                    triggerInputError(editInput);
                    return;
                }
                if (newText.length > 100) {
                    alert('Tên công việc quá dài (tối đa 100 ký tự)!');
                    triggerInputError(editInput);
                    return;
                }
                
                title.textContent = newText;
                isEditing = false;
                btnEdit.textContent = 'Edit';
                btnEdit.style.backgroundColor = ''; // Trả lại màu mặc định
                btnEdit.style.color = '';
                editInput = null;
            }
        });
        actions.appendChild(btnEdit);

        // Move or Delete button depending on the column
        if (initialColumn === 'done') {
            const btnDelete = document.createElement('button');
            btnDelete.className = 'btn-card btn-delete';
            btnDelete.textContent = 'Delete';
            btnDelete.addEventListener('click', () => {
                // Remove card from its parent container (done list)
                const parent = card.parentNode;
                if (parent) {
                    parent.removeChild(card);
                    updateColumnStates();
                }
            });
            actions.appendChild(btnDelete);
        } else {
            const btnMove = document.createElement('button');
            btnMove.className = 'btn-card btn-move';
            btnMove.textContent = 'Move';
            btnMove.addEventListener('click', () => {
                moveCard(card);
            });
            actions.appendChild(btnMove);
        }

        card.appendChild(actions);
        return card;
    };

    /**
     * Moves a card to the next column: To Do -> In Progress -> Done
     * @param {HTMLElement} card - The card to move
     */
    const moveCard = (card) => {
        const currentList = card.parentNode;
        
        if (currentList === listTodo) {
            // Remove from To Do, append to In Progress
            listTodo.removeChild(card);
            listInprogress.appendChild(card);
        } else if (currentList === listInprogress) {
            // Remove from In Progress, append to Done
            listInprogress.removeChild(card);
            listDone.appendChild(card);

            // Replace "Move" button with "Delete" button
            const actionsContainer = card.querySelector('.card-actions');
            const btnMove = actionsContainer.querySelector('.btn-move');
            if (btnMove) {
                actionsContainer.removeChild(btnMove);

                const btnDelete = document.createElement('button');
                btnDelete.className = 'btn-card btn-delete';
                btnDelete.textContent = 'Delete';
                btnDelete.addEventListener('click', () => {
                    const parent = card.parentNode;
                    if (parent) {
                        parent.removeChild(card);
                        updateColumnStates();
                    }
                });
                actionsContainer.appendChild(btnDelete);
            }
        }
        updateColumnStates();
    };

    const triggerInputError = (inputElement) => {
        inputElement.classList.add('input-error');
        inputElement.focus();
        setTimeout(() => {
            inputElement.classList.remove('input-error');
        }, 300);
    };

    /**
     * Handler to add a task to a specific column
     * @param {HTMLInputElement} inputElement - Input field
     * @param {HTMLElement} listElement - List container to append
     * @param {string} columnName - Column identifier ('todo', 'inprogress', 'done')
     */
    const handleAddTask = (inputElement, listElement, columnName) => {
        const text = inputElement.value.trim();
        
        // Validation 1: Rỗng hoặc chỉ chứa dấu cách
        if (text === '') {
            triggerInputError(inputElement);
            return;
        }

        // Validation 2: Quá dài (tối đa 100 ký tự)
        if (text.length > 100) {
            alert('Tên công việc quá dài (tối đa 100 ký tự)!');
            triggerInputError(inputElement);
            return;
        }

        const newCard = createCardElement(text, columnName);
        listElement.appendChild(newCard);

        inputElement.value = '';
        updateColumnStates();
    };

    // Event Listeners for Adding Tasks (Click & Enter)
    // To Do Column
    btnAddTodo.addEventListener('click', () => handleAddTask(inputTodo, listTodo, 'todo'));
    inputTodo.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputTodo, listTodo, 'todo');
        }
    });

    // In Progress Column
    btnAddInprogress.addEventListener('click', () => handleAddTask(inputInprogress, listInprogress, 'inprogress'));
    inputInprogress.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputInprogress, listInprogress, 'inprogress');
        }
    });

    // Done Column
    btnAddDone.addEventListener('click', () => handleAddTask(inputDone, listDone, 'done'));
    inputDone.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputDone, listDone, 'done');
        }
    });

    // Initial load states
    updateColumnStates();
});

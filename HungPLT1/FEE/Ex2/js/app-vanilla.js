document.addEventListener('DOMContentLoaded', () => {
    const inputTodo = document.getElementById('input-todo');
    const btnAddTodo = document.getElementById('btn-add-todo');
    const inputInprogress = document.getElementById('input-inprogress');
    const btnAddInprogress = document.getElementById('btn-add-inprogress');
    const inputDone = document.getElementById('input-done');
    const btnAddDone = document.getElementById('btn-add-done');

    const listTodo = document.getElementById('list-todo');
    const listInprogress = document.getElementById('list-inprogress');
    const listDone = document.getElementById('list-done');

    const countTodo = document.getElementById('count-todo');
    const countInprogress = document.getElementById('count-inprogress');
    const countDone = document.getElementById('count-done');

    const updateColumnStates = () => {
        const todoCardsCount = listTodo.querySelectorAll('.kanban-card').length;
        const inprogressCardsCount = listInprogress.querySelectorAll('.kanban-card').length;
        const doneCardsCount = listDone.querySelectorAll('.kanban-card').length;

        countTodo.textContent = todoCardsCount;
        countInprogress.textContent = inprogressCardsCount;
        countDone.textContent = doneCardsCount;

        togglePlaceholder(listTodo, todoCardsCount, 'Không có công việc cần làm');
        togglePlaceholder(listInprogress, inprogressCardsCount, 'Không có công việc đang thực hiện');
        togglePlaceholder(listDone, doneCardsCount, 'Không có công việc đã hoàn thành');
    };

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

    const createCardElement = (titleText, initialColumn) => {
        const card = document.createElement('div');
        card.className = 'kanban-card';

        const meta = document.createElement('div');
        meta.className = 'card-meta';
        meta.textContent = `Task #${Date.now().toString().slice(-4)}`;
        card.appendChild(meta);

        const title = document.createElement('div');
        title.className = 'card-title';
        title.textContent = titleText;
        card.appendChild(title);

        const actions = document.createElement('div');
        actions.className = 'card-actions';

        const btnEdit = document.createElement('button');
        btnEdit.className = 'btn-card btn-edit';
        btnEdit.textContent = 'Edit';
        
        let isEditing = false;
        let editInput = null;

        btnEdit.addEventListener('click', (e) => {
            e.stopPropagation();
            if (!isEditing) {
                isEditing = true;
                btnEdit.textContent = 'Save';
                btnEdit.style.backgroundColor = 'var(--done-accent)';
                btnEdit.style.color = 'white';

                const currentText = title.textContent;
                editInput = document.createElement('input');
                editInput.type = 'text';
                editInput.className = 'card-edit-input';
                editInput.value = currentText;
                
                title.textContent = '';
                title.appendChild(editInput);
                editInput.focus();

                editInput.addEventListener('keydown', (evt) => {
                    if (evt.key === 'Enter') {
                        evt.preventDefault();
                        evt.stopPropagation();
                        btnEdit.click();
                    }
                });
            } else {
                const newText = editInput.value.trim();
                
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
                btnEdit.style.backgroundColor = '';
                btnEdit.style.color = '';
                editInput = null;
            }
        });
        actions.appendChild(btnEdit);

        if (initialColumn === 'done') {
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

    const moveCard = (card) => {
        const currentList = card.parentNode;
        
        if (currentList === listTodo) {
            listTodo.removeChild(card);
            listInprogress.appendChild(card);
        } else if (currentList === listInprogress) {
            listInprogress.removeChild(card);
            listDone.appendChild(card);

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

    const handleAddTask = (inputElement, listElement, columnName) => {
        const text = inputElement.value.trim();
        
        if (text === '') {
            triggerInputError(inputElement);
            return;
        }

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

    btnAddTodo.addEventListener('click', () => handleAddTask(inputTodo, listTodo, 'todo'));
    inputTodo.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputTodo, listTodo, 'todo');
        }
    });

    btnAddInprogress.addEventListener('click', () => handleAddTask(inputInprogress, listInprogress, 'inprogress'));
    inputInprogress.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputInprogress, listInprogress, 'inprogress');
        }
    });

    btnAddDone.addEventListener('click', () => handleAddTask(inputDone, listDone, 'done'));
    inputDone.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            handleAddTask(inputDone, listDone, 'done');
        }
    });

    updateColumnStates();
});

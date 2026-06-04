document.addEventListener('DOMContentLoaded', () => {
    // Column card lists
    const todoList = document.getElementById('cards-todo');
    const progressList = document.getElementById('cards-progress');
    const doneList = document.getElementById('cards-done');

    // Column input groups
    const todoInput = document.getElementById('input-todo');
    const todoBtn = document.getElementById('btn-add-todo');

    const progressInput = document.getElementById('input-progress');
    const progressBtn = document.getElementById('btn-add-progress');

    const doneInput = document.getElementById('input-done');
    const doneBtn = document.getElementById('btn-add-done');

    // Prepopulate board with items from the specification screenshot
    createAndAddCard("Website Homepage Redesign", todoList);
    createAndAddCard("Market Research Report", todoList);
    createAndAddCard("User Testing Sessions", progressList);
    createAndAddCard("Completed Features List", doneList);

    // Setup input listeners for adding tasks (click + enter key)
    setupAddInput(todoInput, todoBtn, todoList);
    setupAddInput(progressInput, progressBtn, progressList);
    setupAddInput(doneInput, doneBtn, doneList);

    function setupAddInput(inputEl, btnEl, containerEl) {
        // Handle click on '+' button
        btnEl.addEventListener('click', () => {
            const taskText = inputEl.value.trim();
            if (taskText) {
                createAndAddCard(taskText, containerEl);
                inputEl.value = '';
            }
        });

        // Handle Enter keypress
        inputEl.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                const taskText = inputEl.value.trim();
                if (taskText) {
                    createAndAddCard(taskText, containerEl);
                    inputEl.value = '';
                }
            }
        });
    }

    // Main helper to create a card using standard DOM methods
    function createAndAddCard(titleText, containerEl) {
        const card = document.createElement('div');
        card.className = 'task-card';

        // Add Label
        const label = document.createElement('div');
        label.className = 'card-label';
        label.textContent = 'Title';
        card.appendChild(label);

        // Add Title Text
        const title = document.createElement('div');
        title.className = 'card-title';
        title.textContent = titleText;
        card.appendChild(title);

        // Add Actions container
        const actions = document.createElement('div');
        actions.className = 'card-actions';
        card.appendChild(actions);

        // Render appropriate buttons based on the column
        updateCardControls(card, actions, containerEl);

        // Append the card to target column list
        containerEl.appendChild(card);
    }

    // Helper to update action buttons depending on where the card is located
    function updateCardControls(card, actionsContainer, currentContainer) {
        // Clear existing actions using DOM API
        while (actionsContainer.firstChild) {
            actionsContainer.removeChild(actionsContainer.firstChild);
        }

        const titleEl = card.querySelector('.card-title');

        // Edit Button (Common for all columns)
        const btnEdit = document.createElement('button');
        btnEdit.className = 'btn-card';
        btnEdit.innerHTML = '<i class="fa-regular fa-pen-to-square"></i> Edit';
        btnEdit.addEventListener('click', () => {
            const newTitleText = prompt("Edit task name:", titleEl.textContent);
            if (newTitleText && newTitleText.trim() !== '') {
                titleEl.textContent = newTitleText.trim();
            }
        });

        if (currentContainer.id === 'cards-todo') {
            // "Start" Button (Blue shortcut button)
            const btnStart = document.createElement('button');
            btnStart.className = 'btn-card btn-card-start';
            btnStart.innerHTML = '<i class="fa-solid fa-play"></i> Start';
            btnStart.addEventListener('click', () => {
                moveCard(card, progressList);
            });
            actionsContainer.appendChild(btnStart);

            // Add Edit Button
            actionsContainer.appendChild(btnEdit);

            // "Move" Button (standard migration)
            const btnMove = document.createElement('button');
            btnMove.className = 'btn-card';
            btnMove.innerHTML = '<i class="fa-solid fa-arrow-right"></i> Move';
            btnMove.addEventListener('click', () => {
                moveCard(card, progressList);
            });
            actionsContainer.appendChild(btnMove);

        } else if (currentContainer.id === 'cards-progress') {
            // Add Edit Button
            actionsContainer.appendChild(btnEdit);

            // "Move" Button (standard migration)
            const btnMove = document.createElement('button');
            btnMove.className = 'btn-card';
            btnMove.innerHTML = '<i class="fa-solid fa-arrow-right"></i> Move';
            btnMove.addEventListener('click', () => {
                moveCard(card, doneList);
            });
            actionsContainer.appendChild(btnMove);

            // "Done" Button (Green shortcut button)
            const btnDone = document.createElement('button');
            btnDone.className = 'btn-card btn-card-done';
            btnDone.innerHTML = '<i class="fa-solid fa-check"></i> Done';
            btnDone.addEventListener('click', () => {
                moveCard(card, doneList);
            });
            actionsContainer.appendChild(btnDone);

        } else if (currentContainer.id === 'cards-done') {
            // Add Edit Button
            actionsContainer.appendChild(btnEdit);

            // "Archive" Button (matches screenshot, optional but premium)
            const btnArchive = document.createElement('button');
            btnArchive.className = 'btn-card';
            btnArchive.innerHTML = '<i class="fa-solid fa-box-archive"></i> Archive';
            btnArchive.addEventListener('click', () => {
                if (confirm("Archive this task? It will be removed from the board.")) {
                    card.parentNode.removeChild(card);
                }
            });
            actionsContainer.appendChild(btnArchive);

            // "Delete" Button (Replaces "Move" button as specified in Done column)
            const btnDelete = document.createElement('button');
            btnDelete.className = 'btn-card btn-card-delete';
            btnDelete.innerHTML = '<i class="fa-regular fa-trash-can"></i> Delete';
            btnDelete.addEventListener('click', () => {
                card.parentNode.removeChild(card);
            });
            actionsContainer.appendChild(btnDelete);
        }
    }

    // Helper to migrate cards using standard DOM API methods (parentNode, removeChild, appendChild)
    function moveCard(cardElement, destinationContainer) {
        // 1. Remove from current parent
        const parent = cardElement.parentNode;
        if (parent) {
            parent.removeChild(cardElement);
        }

        // 2. Append to new parent
        destinationContainer.appendChild(cardElement);

        // 3. Update the controls corresponding to the new column
        const actionsContainer = cardElement.querySelector('.card-actions');
        updateCardControls(cardElement, actionsContainer, destinationContainer);
    }
});

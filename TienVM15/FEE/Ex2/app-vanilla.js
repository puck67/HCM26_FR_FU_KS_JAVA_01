(function() {
    window.initVanilla = function() {
        // Clear jQuery delegated event listeners on the board to avoid duplicate events and conflicts in Vanilla mode
        if (typeof jQuery !== 'undefined') {
            jQuery('.kanban-board').off('click');
        }

        const board = document.querySelector('.kanban-board');
        const columns = {
            'todo': document.querySelector('.kanban-column.todo .tasks-list'),
            'progress': document.querySelector('.kanban-column.progress .tasks-list'),
            'done': document.querySelector('.kanban-column.done .tasks-list')
        };
        const searchInput = document.querySelector('.search-input');

        // Reset/clean active state
        clearAllListeners();

        // 1. Setup Input forms for all columns
        const inputWrappers = document.querySelectorAll('.column-input-wrapper');
        inputWrappers.forEach(wrapper => {
            const input = wrapper.querySelector('.column-input');
            const btn = wrapper.querySelector('.btn-add-task');

            // Form Submit / Click Event using Vanilla JS
            const addTaskHandler = function() {
                const text = input.value.trim();
                if (text === '') return;
                
                const colType = getColumnType(wrapper.parentNode);
                createTaskVanilla(text, colType);
                input.value = '';
            };

            btn.onclick = addTaskHandler;
            input.onkeypress = function(e) {
                if (e.key === 'Enter') {
                    addTaskHandler();
                }
            };
        });

        // 2. Setup Search Filter (Vanilla JS version)
        searchInput.oninput = function() {
            const query = searchInput.value.toLowerCase().trim();
            const cards = document.querySelectorAll('.task-card');
            cards.forEach(card => {
                const title = card.querySelector('.task-title').textContent.toLowerCase();
                if (title.includes(query)) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        };

        function getColumnType(colEl) {
            if (colEl.classList.contains('todo')) return 'todo';
            if (colEl.classList.contains('progress')) return 'progress';
            return 'done';
        }

        // Vanilla function to create card
        function createTaskVanilla(text, colType) {
            const targetContainer = columns[colType];
            
            // Remove empty indicator
            removeEmptyMessage(targetContainer);

            // Create Elements
            const card = document.createElement('div');
            card.className = 'task-card';

            const titleLabel = document.createElement('div');
            titleLabel.className = 'task-title-label';
            titleLabel.textContent = 'Title';

            const titleText = document.createElement('div');
            titleText.className = 'task-title';
            titleText.textContent = text;

            const actionsDiv = document.createElement('div');
            actionsDiv.className = 'task-actions';

            card.appendChild(titleLabel);
            card.appendChild(titleText);
            card.appendChild(actionsDiv);

            setupActionsVanilla(card, colType);
            targetContainer.appendChild(card);
            
            // Trigger search filter in case a search is active
            triggerSearchVanilla();
        }

        function setupActionsVanilla(cardEl, colType) {
            const actionsDiv = cardEl.querySelector('.task-actions');
            actionsDiv.innerHTML = ''; // Clear existing actions

            if (colType === 'done') {
                // Delete button
                const btnDelete = document.createElement('button');
                btnDelete.type = 'button';
                btnDelete.className = 'btn-task-action delete';
                btnDelete.innerHTML = '🗑 Delete';
                btnDelete.onclick = function() {
                    const list = cardEl.parentNode;
                    list.removeChild(cardEl);
                    checkEmptyMessage(list);
                };
                actionsDiv.appendChild(btnDelete);
            } else {
                // Move button
                const btnMove = document.createElement('button');
                btnMove.type = 'button';
                btnMove.className = 'btn-task-action move';
                btnMove.innerHTML = 'Move ➔';
                btnMove.onclick = function() {
                    moveCardVanilla(cardEl, colType);
                };
                actionsDiv.appendChild(btnMove);
            }
        }

        function moveCardVanilla(cardEl, currentColType) {
            let nextColType = 'progress';
            if (currentColType === 'progress') {
                nextColType = 'done';
            }

            const currentList = columns[currentColType];
            const nextList = columns[nextColType];

            // Pure DOM manipulation movement
            currentList.removeChild(cardEl);
            checkEmptyMessage(currentList);

            removeEmptyMessage(nextList);
            nextList.appendChild(cardEl);

            // Re-setup actions depending on new column
            setupActionsVanilla(cardEl, nextColType);
        }

        function checkEmptyMessage(listEl) {
            if (listEl.querySelectorAll('.task-card').length === 0) {
                if (!listEl.querySelector('.empty-column-message')) {
                    const msg = document.createElement('div');
                    msg.className = 'empty-column-message';
                    msg.textContent = 'No tasks in this list';
                    listEl.appendChild(msg);
                }
            }
        }

        function removeEmptyMessage(listEl) {
            const msg = listEl.querySelector('.empty-column-message');
            if (msg) {
                listEl.removeChild(msg);
            }
        }

        function triggerSearchVanilla() {
            const event = new Event('input');
            searchInput.dispatchEvent(event);
        }

        function clearAllListeners() {
            // Clean inline event handlers on inputs & button clones to avoid memory leaks / ghost clicks
            const inputs = document.querySelectorAll('.column-input');
            const btns = document.querySelectorAll('.btn-add-task');
            
            inputs.forEach(input => {
                input.value = '';
                input.onkeypress = null;
            });
            btns.forEach(btn => btn.onclick = null);
            searchInput.oninput = null;
            searchInput.value = '';

            // Clean columns list and restore empty states
            Object.values(columns).forEach(list => {
                list.innerHTML = '';
                checkEmptyMessage(list);
            });
        }

        // Initialize empty states
        Object.values(columns).forEach(list => checkEmptyMessage(list));
    };
})();

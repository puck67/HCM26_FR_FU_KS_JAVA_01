// Problem 02 - Pure JavaScript Implementation
document.addEventListener('DOMContentLoaded', () => {
    
    // Helper to create a card DOM element
    function createTaskCard(titleText, colStatus) {
        const card = document.createElement('div');
        card.className = 'task-card';

        const label = document.createElement('span');
        label.className = 'task-title-label';
        label.textContent = 'Title';

        const title = document.createElement('div');
        title.className = 'task-title';
        title.textContent = titleText;

        const actions = document.createElement('div');
        actions.className = 'task-actions';

        if (colStatus !== 'done') {
            const moveBtn = document.createElement('button');
            moveBtn.className = 'action-btn btn-move';
            moveBtn.textContent = 'Move ▸';
            
            moveBtn.addEventListener('click', function() {
                // Find current column and container
                const currentCardsContainer = card.parentNode;
                const currentColumn = currentCardsContainer.closest('.kanban-col');
                const status = currentColumn.getAttribute('data-status');
                
                currentCardsContainer.removeChild(card); // Remove from current
                
                let nextColId = '';
                let nextStatus = '';
                if (status === 'todo') {
                    nextColId = 'col-inprogress';
                    nextStatus = 'inprogress';
                } else if (status === 'inprogress') {
                    nextColId = 'col-done';
                    nextStatus = 'done';
                }

                // Re-create the card for the next status (or morph it)
                if (nextColId) {
                    const newCard = createTaskCard(titleText, nextStatus);
                    document.getElementById(nextColId).querySelector('.cards-container').appendChild(newCard);
                }
            });
            actions.appendChild(moveBtn);
        } else {
            const deleteBtn = document.createElement('button');
            deleteBtn.className = 'action-btn btn-delete';
            deleteBtn.textContent = 'Delete ✘';
            
            deleteBtn.addEventListener('click', function() {
                card.parentNode.removeChild(card);
            });
            actions.appendChild(deleteBtn);
        }

        card.appendChild(label);
        card.appendChild(title);
        card.appendChild(actions);

        return card;
    }

    // Add Task event listeners for all columns
    const columns = document.querySelectorAll('.kanban-col');
    columns.forEach(col => {
        const input = col.querySelector('.task-input');
        const addBtn = col.querySelector('.add-btn');
        const cardsContainer = col.querySelector('.cards-container');
        const status = col.getAttribute('data-status');

        function addTask() {
            const val = input.value.trim();
            if (val) {
                const card = createTaskCard(val, status);
                cardsContainer.appendChild(card);
                input.value = '';
            }
        }

        addBtn.addEventListener('click', addTask);
        input.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                addTask();
            }
        });
    });
});

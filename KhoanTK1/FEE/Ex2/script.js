document.addEventListener('DOMContentLoaded', () => {
    const todoInput = document.getElementById('todo-input');
    const todoAddBtn = document.getElementById('todo-add-btn');
    const todoList = document.getElementById('todo-list');

    const progressInput = document.getElementById('progress-input');
    const progressAddBtn = document.getElementById('progress-add-btn');
    const progressList = document.getElementById('progress-list');

    const doneInput = document.getElementById('done-input');
    const doneAddBtn = document.getElementById('done-add-btn');
    const doneList = document.getElementById('done-list');

    const searchBar = document.getElementById('search-bar');

    function createCard(text, isDone) {
        const card = document.createElement('div');
        card.className = 'card';

        const span = document.createElement('span');
        span.textContent = text;
        card.appendChild(span);

        if (isDone) {
            const deleteBtn = document.createElement('button');
            deleteBtn.className = 'delete-btn';
            deleteBtn.textContent = 'Delete';
            deleteBtn.addEventListener('click', () => {
                card.parentNode.removeChild(card);
            });
            card.appendChild(deleteBtn);
        } else {
            const moveBtn = document.createElement('button');
            moveBtn.className = 'move-btn';
            moveBtn.textContent = 'Move';
            moveBtn.addEventListener('click', () => {
                if (card.parentNode === todoList) {
                    progressList.appendChild(card);
                } else if (card.parentNode === progressList) {
                    doneList.appendChild(card);
                    card.removeChild(moveBtn);
                    
                    const deleteBtn = document.createElement('button');
                    deleteBtn.className = 'delete-btn';
                    deleteBtn.textContent = 'Delete';
                    deleteBtn.addEventListener('click', () => {
                        card.parentNode.removeChild(card);
                    });
                    card.appendChild(deleteBtn);
                }
            });
            card.appendChild(moveBtn);
        }

        return card;
    }

    todoAddBtn.addEventListener('click', () => {
        const val = todoInput.value.trim();
        if (val) {
            const card = createCard(val, false);
            todoList.appendChild(card);
            todoInput.value = '';
            filterCards();
        }
    });

    progressAddBtn.addEventListener('click', () => {
        const val = progressInput.value.trim();
        if (val) {
            const card = createCard(val, false);
            progressList.appendChild(card);
            progressInput.value = '';
            filterCards();
        }
    });

    doneAddBtn.addEventListener('click', () => {
        const val = doneInput.value.trim();
        if (val) {
            const card = createCard(val, true);
            doneList.appendChild(card);
            doneInput.value = '';
            filterCards();
        }
    });

    function filterCards() {
        const query = searchBar.value.toLowerCase();
        const cards = document.querySelectorAll('.card');
        cards.forEach(card => {
            const text = card.querySelector('span').textContent.toLowerCase();
            if (text.includes(query)) {
                card.style.display = 'flex';
            } else {
                card.style.display = 'none';
            }
        });
    }

    searchBar.addEventListener('input', filterCards);
});

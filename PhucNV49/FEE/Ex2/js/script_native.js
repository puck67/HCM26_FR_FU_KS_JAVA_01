document.addEventListener('DOMContentLoaded', () => {
  // Select column inputs and buttons
  const columns = ['todo', 'inprogress', 'done'];

  // Helper to clear error state on typing
  columns.forEach(col => {
    const input = document.getElementById(`input-${col}`);
    const errorMsg = document.getElementById(`error-${col}`);
    if (input && errorMsg) {
      input.addEventListener('input', () => {
        input.classList.remove('is-invalid');
        errorMsg.classList.remove('active');
      });
    }
  });

  // Re-renders the action buttons for a task card based on its current column
  function renderCardActions(cardEl, titleText, currentColumn) {
    const actionsContainer = cardEl.querySelector('.card-actions');
    // Clear existing buttons
    actionsContainer.innerHTML = '';

    if (currentColumn === 'todo') {
      // Buttons: Start (blue), Edit (white), Move (white)
      const btnStart = document.createElement('button');
      btnStart.type = 'button';
      btnStart.className = 'btn-card btn-start';
      btnStart.innerText = 'Start';
      btnStart.addEventListener('click', () => moveToColumn(cardEl, titleText, 'inprogress'));

      const btnEdit = document.createElement('button');
      btnEdit.type = 'button';
      btnEdit.className = 'btn-card btn-edit';
      btnEdit.innerText = 'Edit';

      const btnMove = document.createElement('button');
      btnMove.type = 'button';
      btnMove.className = 'btn-card btn-move';
      btnMove.innerText = 'Move ▾';
      btnMove.addEventListener('click', () => moveToColumn(cardEl, titleText, 'inprogress'));

      actionsContainer.appendChild(btnStart);
      actionsContainer.appendChild(btnEdit);
      actionsContainer.appendChild(btnMove);

    } else if (currentColumn === 'inprogress') {
      // Buttons: Edit (white), Move (white), Done (green)
      const btnEdit = document.createElement('button');
      btnEdit.type = 'button';
      btnEdit.className = 'btn-card btn-edit';
      btnEdit.innerText = 'Edit';

      const btnMove = document.createElement('button');
      btnMove.type = 'button';
      btnMove.className = 'btn-card btn-move';
      btnMove.innerText = 'Move ▾';
      btnMove.addEventListener('click', () => moveToColumn(cardEl, titleText, 'done'));

      const btnDone = document.createElement('button');
      btnDone.type = 'button';
      btnDone.className = 'btn-card btn-done';
      btnDone.innerText = 'Done';
      btnDone.addEventListener('click', () => moveToColumn(cardEl, titleText, 'done'));

      actionsContainer.appendChild(btnEdit);
      actionsContainer.appendChild(btnMove);
      actionsContainer.appendChild(btnDone);

    } else if (currentColumn === 'done') {
      // Buttons: Edit (white), Archive (white), Delete (red)
      const btnEdit = document.createElement('button');
      btnEdit.type = 'button';
      btnEdit.className = 'btn-card btn-edit';
      btnEdit.innerText = 'Edit';

      const btnArchive = document.createElement('button');
      btnArchive.type = 'button';
      btnArchive.className = 'btn-card btn-archive';
      btnArchive.innerText = 'Archive';

      // Move is replaced by Delete in Done column
      const btnDelete = document.createElement('button');
      btnDelete.type = 'button';
      btnDelete.className = 'btn-card btn-delete';
      btnDelete.innerText = 'Delete';
      btnDelete.addEventListener('click', () => {
        // Remove element from DOM using standard DOM API
        const parent = cardEl.parentNode;
        if (parent) {
          parent.removeChild(cardEl);
        }
      });

      actionsContainer.appendChild(btnEdit);
      actionsContainer.appendChild(btnArchive);
      actionsContainer.appendChild(btnDelete);
    }
  }

  // Moves a card from its current column list to the target column list
  function moveToColumn(cardEl, titleText, targetColumn) {
    const targetList = document.getElementById(`list-${targetColumn}`);
    if (targetList) {
      // Append card using standard DOM API
      targetList.appendChild(cardEl);
      // Re-render its action buttons to match new column capabilities
      renderCardActions(cardEl, titleText, targetColumn);
    }
  }

  // Creates a brand new task card element
  function createCardElement(titleText, initialColumn) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'task-card';

    const labelDiv = document.createElement('div');
    labelDiv.className = 'card-label';
    labelDiv.innerText = 'Title';

    const titleH3 = document.createElement('h3');
    titleH3.className = 'card-title';
    titleH3.innerText = titleText;

    const actionsDiv = document.createElement('div');
    actionsDiv.className = 'card-actions';

    cardDiv.appendChild(labelDiv);
    cardDiv.appendChild(titleH3);
    cardDiv.appendChild(actionsDiv);

    // Initial render of button controls
    renderCardActions(cardDiv, titleText, initialColumn);

    return cardDiv;
  }

  // Function to add a task in a specific column
  function addTask(column) {
    const input = document.getElementById(`input-${column}`);
    const errorMsg = document.getElementById(`error-${column}`);
    const list = document.getElementById(`list-${column}`);

    if (!input || !list || !errorMsg) return;

    const taskTitle = input.value.trim();

    // Validate (not empty)
    if (taskTitle === '') {
      input.classList.add('is-invalid');
      errorMsg.classList.add('active');
      return;
    }

    // Create and append the new task card
    const cardEl = createCardElement(taskTitle, column);
    list.appendChild(cardEl);

    // Clean inputs and focus back
    input.value = '';
    input.focus();
  }

  // Bind input add actions
  columns.forEach(col => {
    const input = document.getElementById(`input-${col}`);
    const btn = document.querySelector(`.btn-add-task[data-target="${col}"]`);

    if (input) {
      // Enter key press triggers task addition
      input.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
          e.preventDefault();
          addTask(col);
        }
      });
    }

    if (btn) {
      // Plus button click triggers task addition
      btn.addEventListener('click', () => {
        addTask(col);
      });
    }
  });

  // Initialize event listeners on pre-populated cards loaded in the HTML
  function initializePreloadedCards() {
    columns.forEach(col => {
      const list = document.getElementById(`list-${col}`);
      if (!list) return;

      const cards = list.getElementsByClassName('task-card');
      // Loop backwards because modifying nodes in place can affect live collections
      for (let i = cards.length - 1; i >= 0; i--) {
        const card = cards[i];
        const titleText = card.querySelector('.card-title').innerText;
        // Re-render actions to attach listeners correctly
        renderCardActions(card, titleText, col);
      }
    });
  }

  // Kick off initialization
  initializePreloadedCards();
});

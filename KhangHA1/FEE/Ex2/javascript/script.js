document.addEventListener('DOMContentLoaded', () => {
  // 1. Initialize Default Cards matching the provided mockup
  initDefaultCards();

  // 2. Setup event listeners for Task Inputs
  setupInputListeners();
});

// Initial default data
const defaultTasks = {
  'todo-column': [
    'Website Homepage Redesign',
    'Market Research Report'
  ],
  'progress-column': [
    'User Testing Sessions'
  ],
  'done-column': [
    'Completed Features List'
  ]
};

/**
 * Initializes the default tasks onto the board columns.
 */
function initDefaultCards() {
  for (const [columnId, tasks] of Object.entries(defaultTasks)) {
    const columnElement = document.getElementById(columnId);
    if (!columnElement) continue;

    const cardsList = columnElement.querySelector('.cards-list');
    tasks.forEach(taskText => {
      const card = createCard(taskText, columnId);
      cardsList.appendChild(card);
    });
  }
}

/**
 * Attaches event listeners to input fields and add buttons in each column.
 */
function setupInputListeners() {
  const columns = document.querySelectorAll('.column');
  
  columns.forEach(column => {
    const input = column.querySelector('.task-input');
    const addBtn = column.querySelector('.add-task-btn');
    const columnId = column.id;

    const handleAddTask = () => {
      const taskText = input.value.trim();
      if (taskText) {
        const card = createCard(taskText, columnId);
        const cardsList = column.querySelector('.cards-list');
        cardsList.appendChild(card);
        input.value = '';
      } else {
        // Show validation warning border
        const formContainer = input.closest('.add-task-form');
        if (formContainer) {
          formContainer.style.borderColor = '#ef4444';
          formContainer.style.boxShadow = '0 0 0 3px rgba(239, 68, 68, 0.15)';
          setTimeout(() => {
            formContainer.style.borderColor = '';
            formContainer.style.boxShadow = '';
          }, 1000);
        }
      }
    };

    // Add task on button click
    addBtn.addEventListener('click', handleAddTask);

    // Add task on Enter keypress
    input.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        handleAddTask();
      }
    });
  });
}

/**
 * Creates a sticky note card element using native DOM API methods.
 * @param {string} titleText - The title of the task.
 * @param {string} currentColumnId - The ID of the current column.
 * @returns {HTMLElement} - The created card element.
 */
function createCard(titleText, currentColumnId) {
  // Use standard DOM API methods: document.createElement
  const card = document.createElement('div');
  card.className = 'card';

  // Label "Title" at the top of the card
  const label = document.createElement('div');
  label.className = 'card-header-label';
  label.textContent = 'Title';
  card.appendChild(label);

  // Title heading (editable)
  const title = document.createElement('h3');
  title.className = 'card-title';
  title.textContent = titleText;
  card.appendChild(title);

  // Actions row
  const actions = document.createElement('div');
  actions.className = 'card-actions';
  card.appendChild(actions);

  // Create & append Edit Button (common for all stages)
  const editBtn = document.createElement('button');
  editBtn.className = 'card-btn btn-edit';
  editBtn.textContent = 'Edit';
  
  // Inline edit feature
  editBtn.addEventListener('click', () => {
    title.contentEditable = 'true';
    title.focus();
    
    // Select all text when focusing
    const range = document.createRange();
    range.selectNodeContents(title);
    const selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
  });

  // Finish editing on blur or Enter
  title.addEventListener('blur', () => {
    title.contentEditable = 'false';
  });

  title.addEventListener('keydown', (event) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      title.blur();
    }
  });

  actions.appendChild(editBtn);

  // Attach appropriate column-specific buttons
  setupCardButtons(card, currentColumnId);

  return card;
}

/**
 * Configures the action buttons on a card based on its current column.
 * @param {HTMLElement} card - The card element.
 * @param {string} columnId - The ID of the column the card is in.
 */
function setupCardButtons(card, columnId) {
  const actionsContainer = card.querySelector('.card-actions');
  const editBtn = actionsContainer.querySelector('.btn-edit');

  // Clear all buttons except Edit
  // Standard DOM API method: parentNode, removeChild, childNodes
  const childNodes = Array.from(actionsContainer.childNodes);
  childNodes.forEach(child => {
    if (child !== editBtn) {
      actionsContainer.removeChild(child);
    }
  });

  if (columnId === 'todo-column') {
    // Solid "Start" button to move task (matches visual layout of the mock)
    const startBtn = document.createElement('button');
    startBtn.className = 'card-btn btn-start';
    startBtn.textContent = 'Start';
    startBtn.addEventListener('click', () => {
      moveCard(card, 'progress-column');
    });
    // Insert before Edit to match visual alignment in the mockup
    actionsContainer.insertBefore(startBtn, editBtn);

    // "Move" button with dropdown arrow style
    const moveBtn = document.createElement('button');
    moveBtn.className = 'card-btn btn-move';
    moveBtn.innerHTML = 'Move ▾';
    moveBtn.addEventListener('click', () => {
      moveCard(card, 'progress-column');
    });
    actionsContainer.appendChild(moveBtn);

  } else if (columnId === 'progress-column') {
    // "Move" button
    const moveBtn = document.createElement('button');
    moveBtn.className = 'card-btn btn-move';
    moveBtn.innerHTML = 'Move ▾';
    moveBtn.addEventListener('click', () => {
      moveCard(card, 'done-column');
    });
    actionsContainer.appendChild(moveBtn);

    // Solid "Done" button (matches visual layout of the mock)
    const doneBtn = document.createElement('button');
    doneBtn.className = 'card-btn btn-done-indicator';
    doneBtn.textContent = 'Done';
    doneBtn.addEventListener('click', () => {
      moveCard(card, 'done-column');
    });
    actionsContainer.appendChild(doneBtn);

  } else if (columnId === 'done-column') {
    // Archive button for aesthetic consistency
    const archiveBtn = document.createElement('button');
    archiveBtn.className = 'card-btn';
    archiveBtn.textContent = 'Archive';
    archiveBtn.addEventListener('click', () => {
      deleteCard(card);
    });
    actionsContainer.insertBefore(archiveBtn, editBtn);

    // Delete button (Replaces the "Move" button constraint)
    const deleteBtn = document.createElement('button');
    deleteBtn.className = 'card-btn btn-delete';
    deleteBtn.textContent = 'Delete';
    deleteBtn.addEventListener('click', () => {
      deleteCard(card);
    });
    actionsContainer.appendChild(deleteBtn);
  }
}

/**
 * Moves a card element to another column using standard DOM methods.
 * @param {HTMLElement} card - The card to move.
 * @param {string} targetColumnId - The ID of the destination column.
 */
function moveCard(card, targetColumnId) {
  const targetColumn = document.getElementById(targetColumnId);
  if (!targetColumn) return;

  const targetList = targetColumn.querySelector('.cards-list');
  
  // Standard DOM API Constraint: parentNode.removeChild & parentNode.appendChild
  const currentParent = card.parentNode;
  if (currentParent) {
    currentParent.removeChild(card);
  }
  
  targetList.appendChild(card);

  // Update card buttons to reflect new column state
  setupCardButtons(card, targetColumnId);
}

/**
 * Deletes a card element from the board with a visual fadeout effect.
 * @param {HTMLElement} card - The card to delete.
 */
function deleteCard(card) {
  // Fadeout and scale animation
  card.style.transition = 'opacity 0.3s cubic-bezier(0.4, 0, 0.2, 1), transform 0.3s cubic-bezier(0.4, 0, 0.2, 1)';
  card.style.opacity = '0';
  card.style.transform = 'scale(0.85) translateY(10px)';
  
  setTimeout(() => {
    // Standard DOM API Constraint: parentNode.removeChild
    const parent = card.parentNode;
    if (parent) {
      parent.removeChild(card);
    }
  }, 300);
}

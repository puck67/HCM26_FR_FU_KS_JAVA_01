$(document).ready(() => {
  // 1. Initialize Default Cards matching the mockup
  initDefaultCards();

  // 2. Setup Delegated Event Listeners on the Board Container
  setupDelegatedListeners();

  // 3. Setup Global Real-Time Search Filter
  setupSearchFilter();
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
 * Initializes the default tasks using jQuery appendTo.
 */
function initDefaultCards() {
  for (const [columnId, tasks] of Object.entries(defaultTasks)) {
    tasks.forEach(taskText => {
      createAndAppendCard(taskText, columnId);
    });
  }
}

/**
 * Sets up event delegation on the root `#kanban-board` container.
 * This guarantees that new dynamically created elements still trigger actions.
 */
function setupDelegatedListeners() {
  const $board = $('#kanban-board');

  // Event Delegation: Adding a task by clicking "+"
  $board.on('click', '.add-task-btn', function() {
    const $column = $(this).closest('.column');
    const $input = $column.find('.task-input');
    const taskText = $input.val().trim();
    const columnId = $column.attr('id');

    if (taskText) {
      createAndAppendCard(taskText, columnId);
      $input.val('');
    } else {
      // Input validation styling
      const $form = $column.find('.add-task-form');
      $form.css({
        borderColor: '#ef4444',
        boxShadow: '0 0 0 3px rgba(239, 68, 68, 0.15)'
      });
      setTimeout(() => {
        $form.css({ borderColor: '', boxShadow: '' });
      }, 1000);
    }
  });

  // Event Delegation: Adding a task by pressing Enter in input
  $board.on('keydown', '.task-input', function(event) {
    if (event.key === 'Enter') {
      event.preventDefault();
      $(this).siblings('.add-task-btn').trigger('click');
    }
  });

  // Event Delegation: Move right (handles Move, Start, Done buttons)
  $board.on('click', '.btn-move, .btn-start, .btn-done-indicator', function() {
    const $card = $(this).closest('.card');
    const currentColumnId = $card.closest('.column').attr('id');
    let targetColumnId = '';

    if (currentColumnId === 'todo-column') {
      targetColumnId = 'progress-column';
    } else if (currentColumnId === 'progress-column') {
      targetColumnId = 'done-column';
    }

    if (targetColumnId) {
      moveCard($card, targetColumnId);
    }
  });

  // Event Delegation: Delete & Archive
  $board.on('click', '.btn-delete, .btn-archive', function() {
    const $card = $(this).closest('.card');
    deleteCard($card);
  });

  // Event Delegation: Edit Card Title
  $board.on('click', '.btn-edit', function() {
    const $card = $(this).closest('.card');
    const $title = $card.find('.card-title');
    $title.attr('contenteditable', 'true').focus();

    // Select text content inside title
    const range = document.createRange();
    range.selectNodeContents($title[0]);
    const selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
  });

  // Event Delegation: Finishing inline edit on Blur
  $board.on('blur', '.card-title', function() {
    $(this).attr('contenteditable', 'false');
  });

  // Event Delegation: Finishing inline edit on Enter
  $board.on('keydown', '.card-title', function(event) {
    if (event.key === 'Enter') {
      event.preventDefault();
      $(this).blur();
    }
  });
}

/**
 * Sets up global real-time search box key/input filtering.
 */
function setupSearchFilter() {
  $('#global-search').on('input', function() {
    filterCards();
  });
}

/**
 * Filters all cards on the page based on the search input query.
 */
function filterCards() {
  const query = $('#global-search').val().toLowerCase();

  $('.card').each(function() {
    const $card = $(this);
    const text = $card.find('.card-title').text().toLowerCase();

    if (text.indexOf(query) !== -1) {
      // Show card if text matches
      $card.show();
    } else {
      // Hide card using jQuery .hide()
      $card.hide();
    }
  });
}

/**
 * Creates a sticky note card element and appends it to a column using jQuery.
 * @param {string} titleText - The title text.
 * @param {string} columnId - The column container ID.
 * @returns {jQuery} - The created card jQuery element.
 */
function createAndAppendCard(titleText, columnId) {
  const $card = $('<div>').addClass('card');
  
  $('<div>').addClass('card-header-label').text('Title').appendTo($card);
  $('<h3>').addClass('card-title').text(titleText).appendTo($card);
  
  const $actions = $('<div>').addClass('card-actions').appendTo($card);
  $('<button>').addClass('card-btn btn-edit').text('Edit').appendTo($actions);

  setupCardButtons($card, columnId);

  // Append card to target column using .appendTo()
  const $targetList = $('#' + columnId).find('.cards-list');
  $card.appendTo($targetList);

  // If a filter is currently active, ensure the new card respects it
  applySearchFilterToCard($card);

  return $card;
}

/**
 * Installs the correct action buttons inside a card depending on the column.
 * @param {jQuery} $card - The card jQuery object.
 * @param {string} columnId - The target column ID.
 */
function setupCardButtons($card, columnId) {
  const $actions = $card.find('.card-actions');
  const $editBtn = $actions.find('.btn-edit');

  // Remove buttons other than Edit
  $actions.find('.card-btn').not('.btn-edit').remove();

  if (columnId === 'todo-column') {
    // Add aesthetic Start button
    const $startBtn = $('<button>').addClass('card-btn btn-start').text('Start');
    $startBtn.insertBefore($editBtn);

    // Standard Move button
    $('<button>').addClass('card-btn btn-move').html('Move ▾').appendTo($actions);

  } else if (columnId === 'progress-column') {
    // Standard Move button
    $('<button>').addClass('card-btn btn-move').html('Move ▾').appendTo($actions);

    // Done button (moves card to done)
    $('<button>').addClass('card-btn btn-done-indicator').text('Done').appendTo($actions);

  } else if (columnId === 'done-column') {
    // Archive button
    const $archiveBtn = $('<button>').addClass('card-btn btn-archive').text('Archive');
    $archiveBtn.insertBefore($editBtn);

    // Delete button (Replaces the "Move" button constraint)
    $('<button>').addClass('card-btn btn-delete').text('Delete').appendTo($actions);
  }
}

/**
 * Moves a card using jQuery .appendTo().
 * @param {jQuery} $card - The card jQuery object.
 * @param {string} targetColumnId - The destination column ID.
 */
function moveCard($card, targetColumnId) {
  const $targetList = $('#' + targetColumnId).find('.cards-list');
  
  // Constraint: Use jQuery's .appendTo() to move elements between columns
  $card.appendTo($targetList);

  // Refresh buttons for the new state
  setupCardButtons($card, targetColumnId);

  // Make sure search filter hides/shows appropriately
  applySearchFilterToCard($card);
}

/**
 * Deletes a card using jQuery .fadeOut() and removal.
 * @param {jQuery} $card - The card jQuery object.
 */
function deleteCard($card) {
  $card.fadeOut(300, function() {
    $(this).remove();
  });
}

/**
 * Hides or shows a single card based on the current search input text.
 * @param {jQuery} $card - The card jQuery object.
 */
function applySearchFilterToCard($card) {
  const query = $('#global-search').val().toLowerCase();
  const text = $card.find('.card-title').text().toLowerCase();

  if (text.indexOf(query) !== -1) {
    $card.show();
  } else {
    $card.hide();
  }
}

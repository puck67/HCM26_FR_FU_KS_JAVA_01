$(document).ready(() => {
  let cardCounter = 4; // Start counter after the 4 pre-filled cards

  // --- HTML SANITIZER HELPERS (XSS PREVENTION) ---
  function escapeHTML(str) {
    return $('<div>').text(str).html();
  }

  // --- CARD HTML GENERATOR ---
  function createCardHTML(id, title, columnType) {
    const isDone = columnType === 'done';
    const titleClass = isDone ? 'sticky-card__title sticky-card__title--done' : 'sticky-card__title';
    const cardClass = isDone ? 'sticky-card sticky-card--done' : 'sticky-card';
    const escapedTitle = escapeHTML(title);
    
    let actionsHTML = '';
    if (columnType === 'todo') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--start">Start</button>
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    } else if (columnType === 'inprogress') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
        <button class="sticky-card__btn sticky-card__btn--done">Done</button>
      `;
    } else if (columnType === 'done') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <button class="sticky-card__btn sticky-card__btn--archive">Archive</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    } else {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    }
    
    return `
      <div class="sticky-card ${cardClass}" data-card-id="${id}">
        <span class="sticky-card__label">Title</span>
        <h4 class="${titleClass}">${escapedTitle}</h4>
        <div class="sticky-card__actions">
          ${actionsHTML}
        </div>
      </div>
    `;
  }

  // --- REBUILD CARD ACTIONS AND STYLES ON MOVE ---
  function updateCardActions($card, columnType) {
    const isDone = columnType === 'done';
    
    if (isDone) {
      $card.addClass('sticky-card--done');
      $card.find('h4').addClass('sticky-card__title--done');
    } else {
      $card.removeClass('sticky-card--done');
      $card.find('h4').removeClass('sticky-card__title--done');
    }
    
    let actionsHTML = '';
    if (columnType === 'todo') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--start">Start</button>
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    } else if (columnType === 'inprogress') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
        <button class="sticky-card__btn sticky-card__btn--done">Done</button>
      `;
    } else if (columnType === 'done') {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <button class="sticky-card__btn sticky-card__btn--archive">Archive</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    } else {
      actionsHTML = `
        <button class="sticky-card__btn sticky-card__btn--edit">Edit</button>
        <div class="sticky-card__move-wrapper">
          <button class="sticky-card__btn sticky-card__btn--move">
            Move <span class="material-symbols-outlined">arrow_drop_down</span>
          </button>
          <div class="move-dropdown" style="display: none;"></div>
        </div>
      `;
    }
    
    $card.find('.sticky-card__actions').html(actionsHTML);
  }

  // --- RECALCULATE COLUMN COUNTERS ---
  function updateCounters() {
    $('.kanban-column').each(function() {
      const colId = $(this).attr('data-column');
      const count = $(this).find('.sticky-card').length;
      $(`#badge-${colId}`).text(count);
    });
  }

  // --- MOVEMENT BUTTON DELEGATION HANDLERS ---
  
  // Shortcut: Start button (moves to inprogress)
  $('.kanban-grid').on('click', '.sticky-card__btn--start', function(e) {
    e.preventDefault();
    const $card = $(this).closest('.sticky-card');
    const $targetList = $('.kanban-column[data-column="inprogress"] .cards-list');
    if ($targetList.length > 0) {
      $card.appendTo($targetList);
      updateCardActions($card, 'inprogress');
      updateCounters();
    }
  });

  // Shortcut: Done button (moves to done)
  $('.kanban-grid').on('click', '.sticky-card__btn--done', function(e) {
    e.preventDefault();
    const $card = $(this).closest('.sticky-card');
    const $targetList = $('.kanban-column[data-column="done"] .cards-list');
    if ($targetList.length > 0) {
      $card.appendTo($targetList);
      updateCardActions($card, 'done');
      updateCounters();
    }
  });

  // Shortcut: Archive button (deletes task card)
  $('.kanban-grid').on('click', '.sticky-card__btn--archive', function(e) {
    e.preventDefault();
    const $card = $(this).closest('.sticky-card');
    $card.remove();
    updateCounters();
  });

  // Toggle Move Dropdown Overlay
  $('.kanban-grid').on('click', '.sticky-card__btn--move', function(e) {
    e.preventDefault();
    e.stopPropagation();
    
    const $btn = $(this);
    const $card = $btn.closest('.sticky-card');
    const $currentCol = $card.closest('.kanban-column');
    const currentColId = $currentCol.attr('data-column');
    const $dropdown = $btn.siblings('.move-dropdown');
    
    // Hide all other open dropdowns
    $('.move-dropdown').not($dropdown).hide();
    
    // Clean and rebuild dropdown items dynamically
    $dropdown.empty();
    
    $('.kanban-column').each(function() {
      const colId = $(this).attr('data-column');
      if (colId !== currentColId) {
        const colTitle = $(this).find('.kanban-column__title').text();
        const $item = $('<div class="move-dropdown__item"></div>')
          .text(`Move to ${colTitle}`)
          .attr('data-target-column', colId);
        $dropdown.append($item);
      }
    });
    
    $dropdown.fadeToggle(100);
  });

  // Close dropdowns on clicking outside
  $(document).on('click', function(e) {
    if (!$(e.target).closest('.sticky-card__move-wrapper').length) {
      $('.move-dropdown').hide();
    }
  });

  // Handle dropdown target item selection
  $('.kanban-grid').on('click', '.move-dropdown__item', function(e) {
    e.preventDefault();
    e.stopPropagation();
    
    const $item = $(this);
    const targetColId = $item.attr('data-target-column');
    const $card = $item.closest('.sticky-card');
    const $targetCol = $(`.kanban-column[data-column="${targetColId}"]`);
    
    if ($targetCol.length > 0) {
      const $targetList = $targetCol.find('.cards-list');
      $card.appendTo($targetList);
      updateCardActions($card, targetColId);
      $('.move-dropdown').hide();
      updateCounters();
    }
  });

  // --- EDIT DIALOG MODAL & REAL-TIME VALIDATION ---
  let $editingCard = null;

  $('.kanban-grid').on('click', '.sticky-card__btn--edit', function(e) {
    e.preventDefault();
    $editingCard = $(this).closest('.sticky-card');
    const currentTitle = $editingCard.find('h4').text();
    $('#edit-task-input').val(currentTitle).removeClass('edit-modal__input--invalid');
    $('#modal-error').hide();
    $('#edit-save-btn').prop('disabled', false); // Reset disabled state
    $('#edit-modal').fadeIn(150);
    $('#edit-task-input').focus();
  });

  $('#edit-cancel-btn, .edit-modal__backdrop').on('click', () => {
    $('#edit-modal').fadeOut(100);
    $editingCard = null;
  });

  // Real-time error checking for edit input
  $('#edit-task-input').on('input', function() {
    const val = $(this).val().trim();
    if (!val) {
      $(this).addClass('edit-modal__input--invalid');
      $('#modal-error').show();
      $('#edit-save-btn').prop('disabled', true); // Prevent empty submit
    } else {
      $(this).removeClass('edit-modal__input--invalid');
      $('#modal-error').hide();
      $('#edit-save-btn').prop('disabled', false);
    }
  });

  const saveTaskEdit = () => {
    const newTitle = $('#edit-task-input').val().trim();
    if (!newTitle) {
      $('#edit-task-input').addClass('edit-modal__input--invalid');
      $('#modal-error').show();
      return;
    }
    
    // Prevent double submits by disabling save button temporarily
    $('#edit-save-btn').prop('disabled', true);
    
    if ($editingCard) {
      $editingCard.find('h4').text(newTitle);
    }
    
    $('#edit-modal').fadeOut(100);
    $editingCard = null;
  };

  $('#edit-save-btn').on('click', saveTaskEdit);
  
  $('#edit-task-input').on('keypress', (e) => {
    if (e.which === 13) {
      saveTaskEdit();
    }
  });

  // --- QUICK ADD REAL-TIME VALIDATION & PROCESSING ---

  function validateQuickAdd($input) {
    const $container = $input.closest('.kanban-column');
    const $error = $container.find('.quick-add__error');
    const val = $input.val().trim();
    
    if (!val) {
      $input.addClass('edit-modal__input--invalid');
      $error.text('Task title cannot be empty.').show();
      $container.find('.quick-add__btn').prop('disabled', true);
      return false;
    } else {
      $input.removeClass('edit-modal__input--invalid');
      $error.hide();
      $container.find('.quick-add__btn').prop('disabled', false);
      return true;
    }
  }

  // Real-time input checking for quick add inputs
  $('.kanban-grid').on('input', '.quick-add__input', function() {
    validateQuickAdd($(this));
  });

  $('.kanban-grid').on('click', '.quick-add__btn', function() {
    const $input = $(this).siblings('.quick-add__input');
    const columnType = $(this).closest('.kanban-column').attr('data-column');
    handleAdd($input, columnType);
  });

  $('.kanban-grid').on('keypress', '.quick-add__input', function(e) {
    if (e.which === 13) {
      const columnType = $(this).closest('.kanban-column').attr('data-column');
      handleAdd($(this), columnType);
    }
  });

  function handleAdd($input, columnType) {
    const isValid = validateQuickAdd($input);
    if (!isValid) {
      $input.focus();
      return;
    }
    
    const title = $input.val().trim();
    const $btn = $input.siblings('.quick-add__btn');
    
    // Disable button to prevent double task addition
    $btn.prop('disabled', true);
    
    cardCounter++;
    const cardHTML = createCardHTML(cardCounter, title, columnType);
    const $targetList = $input.closest('.kanban-column').find('.cards-list');
    $targetList.append(cardHTML);
    $input.val('');
    
    $btn.prop('disabled', false);
    updateCounters();
  }

  // --- ADD DYNAMIC NEW COLUMN (INSERTED BEFORE DONE) ---
  $('#sidebar-new-column-btn, #mobile-add-task-btn').on('click', () => {
    const colName = prompt('Enter new column name:');
    if (!colName || !colName.trim()) {
      return;
    }
    
    const escapedColName = escapeHTML(colName.trim());
    const columnId = 'col-' + Date.now();
    const listId = 'list-' + columnId;
    
    const colHTML = `
      <div class="kanban-column" data-column="${columnId}">
        <div class="kanban-column__header kanban-column__header--custom">
          <span class="kanban-column__title">${escapedColName}</span>
          <span class="kanban-column__badge" id="badge-${columnId}">0</span>
        </div>
        <div class="kanban-column__content">
          <div class="quick-add">
            <input class="quick-add__input" placeholder="Add a task to ${escapedColName}" type="text">
            <button class="quick-add__btn" aria-label="Add task to ${escapedColName}">
              <span class="material-symbols-outlined">add</span>
            </button>
          </div>
          <p class="quick-add__error" style="display: none;"></p>
          <div class="cards-list" id="${listId}"></div>
        </div>
      </div>
    `;
    
    // Insert new columns before Done to maintain To Do -> In Progress -> Custom -> Done flow
    $(colHTML).insertBefore($('.kanban-column[data-column="done"]'));
    
    // Dynamically adjust grid CSS template for added column
    const colCount = $('.kanban-column').length;
    if (window.innerWidth > 1024) {
      $('.kanban-grid').css('grid-template-columns', `repeat(${colCount}, 1fr)`);
    }
    
    updateCounters();
  });

  // --- REAL-TIME FILTER SEARCH ---
  $('#global-search').on('input', function() {
    const query = $(this).val().trim().toLowerCase();
    
    $('.sticky-card').each(function() {
      const title = $(this).find('h4').text().toLowerCase();
      if (title.indexOf(query) !== -1) {
        $(this).show(); // jQuery show
      } else {
        $(this).hide(); // jQuery hide
      }
    });
  });

  // --- CARD SCALE/ROTATE MICRO-INTERACTIONS ---
  // Managed cleanly via CSS class addition/removal rather than direct DOM styles injection
  $('.kanban-grid').on('mousedown', '.sticky-card', function() {
    $(this).addClass('sticky-card--pressed');
  }).on('mouseup mouseleave', '.sticky-card', function() {
    $(this).removeClass('sticky-card--pressed');
  });

  // Expose functions for programmatic testing
  window.BoardFlow = {
    escapeHTML,
    createCardHTML,
    updateCardActions,
    updateCounters,
    validateQuickAdd
  };

  // Calculate and display initial badges counters
  updateCounters();
});

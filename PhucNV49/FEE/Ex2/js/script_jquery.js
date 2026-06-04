$(document).ready(function() {
  const columns = ['todo', 'inprogress', 'done'];

  // Handle live inputs to clear errors on typing
  columns.forEach(col => {
    $(`#input-${col}`).on('input', function() {
      $(this).removeClass('is-invalid');
      $(`#error-${col}`).removeClass('active');
    });
  });

  // Helper to build action buttons inside card actions container based on state
  function renderCardActions($card, column) {
    const $actions = $card.find('.card-actions');
    $actions.empty();

    if (column === 'todo') {
      $actions.append('<button type="button" class="btn-card btn-start">Start</button>');
      $actions.append('<button type="button" class="btn-card btn-edit">Edit</button>');
      $actions.append('<button type="button" class="btn-card btn-move">Move ▾</button>');
    } else if (column === 'inprogress') {
      $actions.append('<button type="button" class="btn-card btn-edit">Edit</button>');
      $actions.append('<button type="button" class="btn-card btn-move">Move ▾</button>');
      $actions.append('<button type="button" class="btn-card btn-done">Done</button>');
    } else if (column === 'done') {
      $actions.append('<button type="button" class="btn-card btn-edit">Edit</button>');
      $actions.append('<button type="button" class="btn-card btn-archive">Archive</button>');
      $actions.append('<button type="button" class="btn-card btn-delete">Delete</button>');
    }
  }

  // Move element between columns effectively using jQuery .appendTo()
  function moveToColumn($card, targetCol) {
    const $targetList = $(`#list-${targetCol}`);
    if ($targetList.length > 0) {
      // DOM Movement: jQuery appendTo()
      $card.appendTo($targetList);
      // Update buttons
      renderCardActions($card, targetCol);
      // Run search filter to ensure correct visibility state for moved card
      runSearchFilter();
    }
  }

  // Add Task function
  function addTask(column) {
    const $input = $(`#input-${column}`);
    const $error = $(`#error-${column}`);
    const $list = $(`#list-${column}`);

    if ($input.length === 0 || $list.length === 0) return;

    const taskTitle = $.trim($input.val());

    // Validate (not empty)
    if (taskTitle === '') {
      $input.addClass('is-invalid');
      $error.addClass('active');
      return;
    }

    // Create task card element
    const $card = $('<div class="task-card"></div>');
    $card.append('<div class="card-label">Title</div>');
    $card.append($('<h3 class="card-title"></h3>').text(taskTitle));
    $card.append('<div class="card-actions"></div>');

    // DOM placement: appendTo()
    $card.appendTo($list);
    renderCardActions($card, column);

    // Run search filter to apply current search queries to the new card
    runSearchFilter();

    // Clear and focus back
    $input.val('');
    $input.focus();
  }

  // Event listener for inputs and buttons using jQuery .on()
  columns.forEach(col => {
    $(`#input-${col}`).on('keydown', function(e) {
      if (e.key === 'Enter') {
        e.preventDefault();
        addTask(col);
      }
    });

    $(`.btn-add-task[data-target="${col}"]`).on('click', function() {
      addTask(col);
    });
  });

  // Event Delegation: Use jQuery's .on('click', ...) on the board container
  // This handles actions on newly added cards automatically!
  const $board = $('#board-container');

  // Trigger migration from To Do to In Progress
  $board.on('click', '.col-todo .btn-start, .col-todo .btn-move', function() {
    const $card = $(this).closest('.task-card');
    moveToColumn($card, 'inprogress');
  });

  // Trigger migration from In Progress to Done
  $board.on('click', '.col-inprogress .btn-move, .col-inprogress .btn-done', function() {
    const $card = $(this).closest('.task-card');
    moveToColumn($card, 'done');
  });

  // Trigger deletion inside Done column
  $board.on('click', '.col-done .btn-delete', function() {
    const $card = $(this).closest('.task-card');
    // Remove element using jQuery's .remove()
    $card.remove();
  });

  // Global Real-time Search Filter Logic
  const $searchInput = $('#search-tasks');

  function runSearchFilter() {
    const query = $.trim($searchInput.val()).toLowerCase();
    
    $('.task-card').each(function() {
      const titleText = $(this).find('.card-title').text().toLowerCase();
      // If matches query, show card. Otherwise hide card.
      if (titleText.indexOf(query) !== -1) {
        $(this).show(); // jQuery show
      } else {
        $(this).hide(); // jQuery hide
      }
    });
  }

  // Bind search input typing events
  $searchInput.on('keyup input', function() {
    runSearchFilter();
  });

  // Initialize event listeners / renders on preloaded HTML cards
  function initializePreloadedCards() {
    columns.forEach(col => {
      const $list = $(`#list-${col}`);
      $list.find('.task-card').each(function() {
        renderCardActions($(this), col);
      });
    });
  }

  // Initialize
  initializePreloadedCards();
});

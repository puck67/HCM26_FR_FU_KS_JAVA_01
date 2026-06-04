$(document).ready(function() {
  const $toastContainer = $('#toast-container');
  const $searchInput = $('#search-input');

  function showToast(message) {
    const $toast = $('<div class="toast"></div>');
    const $content = $('<div class="toast-content"></div>');
    const $icon = $('<div class="toast-icon">⚠️</div>');
    const $msgSpan = $('<span class="toast-message"></span>').text(message);

    $content.append($icon).append($msgSpan);

    const $closeBtn = $('<button class="toast-close">&times;</button>');
    $closeBtn.on('click', function() {
      $toast.removeClass('show');
      setTimeout(function() {
        $toast.remove();
      }, 400);
    });

    $toast.append($content).append($closeBtn);
    $toastContainer.append($toast);

    setTimeout(function() {
      $toast.addClass('show');
    }, 10);

    setTimeout(function() {
      if ($toast.hasClass('show')) {
        $toast.removeClass('show');
        setTimeout(function() {
          $toast.remove();
        }, 400);
      }
    }, 4000);
  }

  function isDuplicate(title, $excludeInput = null) {
    let duplicate = false;
    $('.task-card').each(function() {
      const $titleEl = $(this).find('.task-card-title');
      const $editInputEl = $(this).find('.card-edit-input');
      let currentTitle = '';

      if ($titleEl.length) {
        currentTitle = $titleEl.text();
      } else if ($editInputEl.length) {
        if ($excludeInput && $editInputEl[0] === $excludeInput[0]) {
          return;
        }
        currentTitle = $editInputEl.val();
      }

      if (currentTitle.trim().toLowerCase() === title.trim().toLowerCase()) {
        duplicate = true;
        return false;
      }
    });
    return duplicate;
  }

  function updateCounters() {
    $('#todo-count').text($('#todo-list').children().length);
    $('#inprogress-count').text($('#inprogress-list').children().length);
    $('#done-count').text($('#done-list').children().length);
  }

  function runSearch() {
    const query = $searchInput.val().toLowerCase().trim();
    $('.task-card').each(function() {
      const $titleEl = $(this).find('.task-card-title');
      const $editInputEl = $(this).find('.card-edit-input');
      let title = '';

      if ($titleEl.length) {
        title = $titleEl.text().toLowerCase();
      } else if ($editInputEl.length) {
        title = $editInputEl.val().toLowerCase();
      }

      if (title.indexOf(query) !== -1) {
        $(this).show();
      } else {
        $(this).hide();
      }
    });
  }

  function fillCardActions($container, columnId) {
    $container.empty();
    if (columnId === 'todo') {
      const $startBtn = $('<button class="action-btn start-btn">Start</button>');
      const $editBtn = $('<button class="action-btn edit-btn">Edit</button>');
      const $moveBtn = $('<button class="action-btn move-btn">Move</button>');
      $container.append($startBtn).append($editBtn).append($moveBtn);
    } else if (columnId === 'inprogress') {
      const $moveBtn = $('<button class="action-btn move-btn">Move</button>');
      const $doneBtn = $('<button class="action-btn done-btn">Done</button>');
      $container.append($moveBtn).append($doneBtn);
    } else if (columnId === 'done') {
      const $archiveBtn = $('<button class="action-btn archive-btn">Archive</button>');
      const $deleteBtn = $('<button class="action-btn delete-btn">Delete</button>');
      $container.append($archiveBtn).append($deleteBtn);
    }
  }

  function createTaskCard(title, columnId) {
    const $card = $('<div class="task-card"></div>');
    const $header = $('<div class="task-card-header">Title</div>');
    const $titleDiv = $('<div class="task-card-title"></div>').text(title);
    const $actions = $('<div class="task-card-actions"></div>');

    fillCardActions($actions, columnId);
    $card.append($header).append($titleDiv).append($actions);
    return $card;
  }

  const forms = [
    { formId: '#todo-form', inputId: '#todo-input', listId: '#todo-list', columnId: 'todo' },
    { formId: '#inprogress-form', inputId: '#inprogress-input', listId: '#inprogress-list', columnId: 'inprogress' },
    { formId: '#done-form', inputId: '#done-input', listId: '#done-list', columnId: 'done' }
  ];

  forms.forEach(function(item) {
    $(item.formId).on('submit', function(e) {
      e.preventDefault();
      const val = $(item.inputId).val();

      if (!val || val.trim() === '') {
        showToast('Task title cannot be empty.');
        return;
      }
      if (val.trim().length < 3) {
        showToast('Task title must be at least 3 characters long.');
        return;
      }
      if (val.trim().length > 100) {
        showToast('Task title cannot exceed 100 characters.');
        return;
      }
      if (val.indexOf('<') !== -1 || val.indexOf('>') !== -1) {
        showToast('HTML tags are not allowed in the task name.');
        return;
      }
      if (isDuplicate(val)) {
        showToast('A task with this title already exists.');
        return;
      }

      const $card = createTaskCard(val.trim(), item.columnId);
      $(item.listId).append($card);
      $(item.inputId).val('');
      updateCounters();
      runSearch();
    });
  });

  $('.board-layout').on('click', '.action-btn', function() {
    const $btn = $(this);
    const $actions = $btn.parent();
    const $card = $actions.parent();
    const $currentList = $card.parent();

    if ($btn.hasClass('start-btn') || ($btn.hasClass('move-btn') && $currentList.attr('id') === 'todo-list')) {
      $card.appendTo('#inprogress-list');
      fillCardActions($actions, 'inprogress');
      updateCounters();
      runSearch();
    } else if ($btn.hasClass('done-btn') || ($btn.hasClass('move-btn') && $currentList.attr('id') === 'inprogress-list')) {
      $card.appendTo('#done-list');
      fillCardActions($actions, 'done');
      updateCounters();
      runSearch();
    } else if ($btn.hasClass('delete-btn') || $btn.hasClass('archive-btn')) {
      $card.remove();
      updateCounters();
    } else if ($btn.hasClass('edit-btn')) {
      const $titleDiv = $card.find('.task-card-title');
      const currentTitle = $titleDiv.text();

      const $input = $('<input type="text" class="card-edit-input">').val(currentTitle);
      $titleDiv.replaceWith($input);

      $btn.removeClass('edit-btn').addClass('save-btn').text('Save');

      $actions.find('.action-btn').each(function() {
        if (!$(this).hasClass('save-btn')) {
          $(this).hide();
        }
      });
    } else if ($btn.hasClass('save-btn')) {
      const $input = $card.find('.card-edit-input');
      const val = $input.val();

      if (!val || val.trim() === '') {
        showToast('Task title cannot be empty.');
        return;
      }
      if (val.trim().length < 3) {
        showToast('Task title must be at least 3 characters long.');
        return;
      }
      if (val.trim().length > 100) {
        showToast('Task title cannot exceed 100 characters.');
        return;
      }
      if (val.indexOf('<') !== -1 || val.indexOf('>') !== -1) {
        showToast('HTML tags are not allowed in the task name.');
        return;
      }
      if (isDuplicate(val, $input)) {
        showToast('A task with this title already exists.');
        return;
      }

      const $titleDiv = $('<div class="task-card-title"></div>').text(val.trim());
      $input.replaceWith($titleDiv);

      $btn.removeClass('save-btn').addClass('edit-btn').text('Edit');

      $actions.find('.action-btn').show();
      runSearch();
    }
  });

  $searchInput.on('input', runSearch);

  updateCounters();
  runSearch();
});

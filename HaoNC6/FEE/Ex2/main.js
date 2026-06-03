
const COLUMN_ORDER = ['todo-list', 'prog-list', 'done-list'];


function buildActions(listId) {
  const actions = document.createElement('div');
  actions.className = 'card-actions';

  if (listId === 'todo-list') {
    const btnStart = document.createElement('button');
    btnStart.className = 'btn-start';
    btnStart.textContent = 'Start';

    const btnEdit = document.createElement('button');
    btnEdit.className = 'btn-edit';
    btnEdit.textContent = 'Edit';

    const btnMove = document.createElement('button');
    btnMove.className = 'btn-move';
    btnMove.textContent = 'Move →';

    actions.appendChild(btnStart);
    actions.appendChild(btnEdit);
    actions.appendChild(btnMove);

  } else if (listId === 'prog-list') {

    const btnEdit = document.createElement('button');
    btnEdit.className = 'btn-edit';
    btnEdit.textContent = 'Edit';

    const btnMove = document.createElement('button');
    btnMove.className = 'btn-move';
    btnMove.textContent = 'Move →';

    const btnDone = document.createElement('button');
    btnDone.className = 'btn-done-col';
    btnDone.textContent = 'Done';

    actions.appendChild(btnEdit);
    actions.appendChild(btnMove);
    actions.appendChild(btnDone);

  } else {
    const btnEdit = document.createElement('button');
    btnEdit.className = 'btn-edit';
    btnEdit.textContent = 'Edit';

    const btnArchive = document.createElement('button');
    btnArchive.className = 'btn-archive';
    btnArchive.textContent = 'Archive';

    const btnMove = document.createElement('button');
    btnMove.className = 'btn-move btn-move-done';
    btnMove.textContent = 'Move →';

    actions.appendChild(btnEdit);
    actions.appendChild(btnArchive);
    actions.appendChild(btnMove);
  }

  return actions;
}


function createCard(title, listId) {
  const card = document.createElement('div');
  card.className = 'task-card';

  const label = document.createElement('div');
  label.className = 'card-label';
  label.textContent = 'Title';

  const titleEl = document.createElement('div');
  titleEl.className = 'card-title';
  titleEl.textContent = title;

  const actions = buildActions(listId);

  card.appendChild(label);
  card.appendChild(titleEl);
  card.appendChild(actions);

  return card;
}


function addTask(listEl, inputEl) {
  const title = inputEl.value.trim();
  if (!title) { inputEl.focus(); return; }

  const card = createCard(title, listEl.id);
  listEl.appendChild(card);
  inputEl.value = '';
  inputEl.focus();
}


function getNextListId(currentListId) {
  const idx = COLUMN_ORDER.indexOf(currentListId);
  if (idx === -1 || idx >= COLUMN_ORDER.length - 1) return null;
  return COLUMN_ORDER[idx + 1];
}

function refreshCardActions(card, newListId) {
  const oldActions = card.querySelector('.card-actions');
  const newActions = buildActions(newListId);
  card.removeChild(oldActions);
  card.appendChild(newActions);
}



$(function () {

  $('#board').on('click', '.btn-add-task', function () {
    const $col = $(this).closest('.column');
    const input = $col.find('.add-task-input')[0];
    const list = $col.find('.task-list')[0];
    addTask(list, input);
  });

  $('#board').on('keydown', '.add-task-input', function (e) {
    if (e.key === 'Enter') {
      const $col = $(this).closest('.column');
      const list = $col.find('.task-list')[0];
      addTask(list, this);
    }
  });

  $('#board').on('click', '.btn-move:not(.btn-move-done)', function () {
    const $card = $(this).closest('.task-card');
    const currentListId = $card.closest('.task-list').attr('id');
    const nextListId = getNextListId(currentListId);
    if (!nextListId) return;

    $card.appendTo($('#' + nextListId));

    refreshCardActions($card[0], nextListId);
  });

  $('#board').on('click', '.btn-start', function () {
    const $card = $(this).closest('.task-card');
    $card.appendTo($('#prog-list'));
    refreshCardActions($card[0], 'prog-list');
  });

  $('#board').on('click', '.btn-done-col', function () {
    const $card = $(this).closest('.task-card');
    $card.appendTo($('#done-list'));
    refreshCardActions($card[0], 'done-list');
  });

  $('#board').on('click', '.btn-archive', function () {
    const card = $(this).closest('.task-card')[0];
    const parent = card.parentNode;
    parent.removeChild(card);
  });

  $('#board').on('click', '.btn-edit', function () {
    const $card = $(this).closest('.task-card');
    const $titleEl = $card.find('.card-title');
    const current = $titleEl.text();
    const newTitle = prompt('Edit task title:', current);
    if (newTitle && newTitle.trim()) {
      $titleEl.text(newTitle.trim());
    }
  });

  $('#searchInput').on('input', function () {
    const query = $(this).val().trim().toLowerCase();

    $('.task-card').each(function () {
      const title = $(this).find('.card-title').text().toLowerCase();
      if (!query || title.includes(query)) {
        $(this).show();
      } else {
        $(this).hide();
      }
    });
  });

}); 

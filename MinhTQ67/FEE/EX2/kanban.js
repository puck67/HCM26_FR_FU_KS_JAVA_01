/**
 * kanban.js — Interactive Kanban Task Board
 *
 * Problem 02: Native DOM API — parentNode, removeChild, appendChild
 * Problem 03: jQuery refactoring — .appendTo(), .on() delegation, .hide()/.show()
 */

$(document).ready(function () {

  /* =====================================================
     Cấu hình thứ tự cột
     ===================================================== */
  const COLUMN_ORDER = ['todo', 'inprogress', 'done'];

  const COLUMN_LABELS = {
    todo:       'To Do',
    inprogress: 'In Progress',
    done:       'Done'
  };

  /* =====================================================
     Problem 02: Tạo card bằng document.createElement
     ===================================================== */
  function createTaskCard(title, columnId) {
    // Dùng document.createElement (Problem 02 constraint)
    const card = document.createElement('div');
    card.className = 'card border shadow-sm task-card';
    card.setAttribute('data-column', columnId);

    // Card body
    const cardBody = document.createElement('div');
    cardBody.className = 'card-body p-2';

    // Label "Title"
    const label = document.createElement('p');
    label.className = 'text-muted mb-1';
    label.style.fontSize = '0.7rem';
    label.style.textTransform = 'uppercase';
    label.style.letterSpacing = '0.05em';
    label.textContent = 'Title';

    // Task title text
    const titleEl = document.createElement('p');
    titleEl.className = 'fw-bold mb-2 task-title';
    titleEl.style.fontSize = '0.95rem';
    titleEl.textContent = title;

    // Button group
    const btnGroup = document.createElement('div');
    btnGroup.className = 'd-flex gap-1 flex-wrap';

    // Edit button (tất cả các cột đều có)
    const btnEdit = document.createElement('button');
    btnEdit.className = 'btn btn-outline-secondary btn-sm btn-edit';
    btnEdit.innerHTML = '<i class="bi bi-pencil me-1"></i>Edit';

    btnGroup.appendChild(btnEdit);

    // Move button (chỉ Todo & In Progress) hoặc Delete (Done)
    if (columnId === 'done') {
      // Problem 02: Done column → Delete button thay Move
      const btnDelete = document.createElement('button');
      btnDelete.className = 'btn btn-danger btn-sm btn-delete';
      btnDelete.innerHTML = '<i class="bi bi-trash me-1"></i>Delete';
      btnGroup.appendChild(btnDelete);
    } else {
      // Move button với dropdown (To Do → In Progress, In Progress → Done)
      const nextCol   = COLUMN_ORDER[COLUMN_ORDER.indexOf(columnId) + 1];
      const nextLabel = COLUMN_LABELS[nextCol];

      const btnMove = document.createElement('button');
      btnMove.className = 'btn btn-primary btn-sm btn-move';
      btnMove.setAttribute('data-next', nextCol);
      btnMove.innerHTML = `<i class="bi bi-arrow-right me-1"></i>Move → ${nextLabel}`;

      btnGroup.appendChild(btnMove);
    }

    // Gắn vào card
    cardBody.appendChild(label);
    cardBody.appendChild(titleEl);
    cardBody.appendChild(btnGroup);
    card.appendChild(cardBody);

    return card;
  }

  /* =====================================================
     Problem 02 + 03: Thêm task vào cột
     ===================================================== */
  function addTask(columnId) {
    const $input = $(`.task-input[data-column="${columnId}"]`);
    const title  = $input.val().trim();

    if (title === '') {
      $input.addClass('is-invalid').focus();
      setTimeout(() => $input.removeClass('is-invalid'), 1500);
      return;
    }

    // Tạo card bằng DOM API (Problem 02)
    const card = createTaskCard(title, columnId);

    // Problem 03: .appendTo() để thêm vào đúng cột
    $(card).appendTo(`#col-${columnId}`);

    $input.val('').focus();
  }

  /* =====================================================
     Problem 02: Move card dùng parentNode / appendChild
     ===================================================== */
  function moveCard(cardEl, nextColumnId) {
    const newCard = createTaskCard(
      cardEl.querySelector('.task-title').textContent,
      nextColumnId
    );

    // Problem 02: removeChild từ cột cũ
    const oldParent = cardEl.parentNode;
    oldParent.removeChild(cardEl);

    // Problem 02: appendChild vào cột mới
    const newParent = document.getElementById('col-' + nextColumnId);
    newParent.appendChild(newCard);
  }

  /* =====================================================
     Problem 03: jQuery event delegation .on('click', ...)
     Bind một lần trên #kanban-board → hoạt động với
     tất cả card được tạo động sau này
     ===================================================== */

  // ---- Nút Add Task (click) ----
  $('#kanban-board').on('click', '.btn-add-task', function () {
    const columnId = $(this).data('column');
    addTask(columnId);
  });

  // ---- Enter key trong input ----
  $('#kanban-board').on('keydown', '.task-input', function (e) {
    if (e.key === 'Enter') {
      const columnId = $(this).data('column');
      addTask(columnId);
    }
  });

  // ---- Nút Move → cột tiếp theo ----
  $('#kanban-board').on('click', '.btn-move', function () {
    const cardEl   = $(this).closest('.task-card')[0]; // native element
    const nextCol  = $(this).data('next');
    moveCard(cardEl, nextCol);

    // Nếu đang search → chạy lại filter
    triggerSearch();
  });

  // ---- Nút Delete (chỉ ở cột Done) ----
  $('#kanban-board').on('click', '.btn-delete', function () {
    const $card = $(this).closest('.task-card');
    // Problem 03: .remove()
    $card.fadeOut(200, function () { $(this).remove(); });
  });

  // ---- Nút Edit: inline edit title ----
  $('#kanban-board').on('click', '.btn-edit', function () {
    const $card      = $(this).closest('.task-card');
    const $titleEl   = $card.find('.task-title');
    const currentVal = $titleEl.text();

    // Nếu đang edit rồi thì bỏ qua
    if ($card.find('.edit-input').length) return;

    // Thay text bằng input
    const $input = $('<input>')
      .addClass('form-control form-control-sm edit-input mb-2')
      .val(currentVal);

    $titleEl.replaceWith($input);
    $input.focus().select();

    // Đổi nút Edit → Save
    $(this)
      .removeClass('btn-outline-secondary btn-edit')
      .addClass('btn-success btn-save')
      .html('<i class="bi bi-check-lg me-1"></i>Save');
  });

  // ---- Nút Save (sau Edit) ----
  $('#kanban-board').on('click', '.btn-save', function () {
    const $card   = $(this).closest('.task-card');
    const $input  = $card.find('.edit-input');
    const newVal  = $input.val().trim();

    if (newVal === '') {
      $input.addClass('is-invalid');
      return;
    }

    // Tạo lại thẻ <p> title
    const $newTitle = $('<p>')
      .addClass('fw-bold mb-2 task-title')
      .css('font-size', '0.95rem')
      .text(newVal);

    $input.replaceWith($newTitle);

    $(this)
      .removeClass('btn-success btn-save')
      .addClass('btn-outline-secondary btn-edit')
      .html('<i class="bi bi-pencil me-1"></i>Edit');
  });

  /* =====================================================
     Problem 03: Real-time Search — .hide() / .show()
     ===================================================== */
  function triggerSearch() {
    const keyword = $('#search-input').val().trim().toLowerCase();

    $('.task-card').each(function () {
      const title = $(this).find('.task-title').text().toLowerCase();
      if (keyword === '' || title.includes(keyword)) {
        $(this).show();
      } else {
        // Problem 03: .hide() cards không khớp
        $(this).hide();
      }
    });
  }

  $('#search-input').on('input', function () {
    triggerSearch();
  });

  /* =====================================================
     Dữ liệu mẫu ban đầu
     ===================================================== */
  const sampleTasks = [
    { title: 'Website Homepage Redesign', col: 'todo'       },
    { title: 'Market Research Report',    col: 'todo'       },
    { title: 'User Testing Sessions',     col: 'inprogress' },
    { title: 'Completed Features List',   col: 'done'       },
  ];

  sampleTasks.forEach(function (t) {
    const card = createTaskCard(t.title, t.col);
    $(card).appendTo(`#col-${t.col}`);
  });

});

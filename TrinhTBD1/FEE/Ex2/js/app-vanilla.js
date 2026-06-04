document.addEventListener('DOMContentLoaded', () => {
  const toastContainer = document.getElementById('toast-container');
  const searchInput = document.getElementById('search-input');

  function showToast(message) {
    const toast = document.createElement('div');
    toast.className = 'toast';

    const content = document.createElement('div');
    content.className = 'toast-content';

    const icon = document.createElement('div');
    icon.className = 'toast-icon';
    icon.textContent = '⚠️';

    const msgSpan = document.createElement('span');
    msgSpan.className = 'toast-message';
    msgSpan.textContent = message;

    content.appendChild(icon);
    content.appendChild(msgSpan);

    const closeBtn = document.createElement('button');
    closeBtn.className = 'toast-close';
    closeBtn.innerHTML = '&times;';
    closeBtn.addEventListener('click', () => {
      toast.classList.remove('show');
      setTimeout(() => {
        if (toast.parentNode === toastContainer) {
          toastContainer.removeChild(toast);
        }
      }, 400);
    });

    toast.appendChild(content);
    toast.appendChild(closeBtn);
    toastContainer.appendChild(toast);

    setTimeout(() => {
      toast.classList.add('show');
    }, 10);

    setTimeout(() => {
      if (toast.classList.contains('show')) {
        toast.classList.remove('show');
        setTimeout(() => {
          if (toast.parentNode === toastContainer) {
            toastContainer.removeChild(toast);
          }
        }, 400);
      }
    }, 4000);
  }

  function isDuplicate(title, excludeInput = null) {
    let duplicate = false;
    const cards = document.querySelectorAll('.task-card');
    cards.forEach(card => {
      const titleEl = card.querySelector('.task-card-title');
      const editInputEl = card.querySelector('.card-edit-input');
      let currentTitle = '';

      if (titleEl) {
        currentTitle = titleEl.textContent;
      } else if (editInputEl) {
        if (editInputEl === excludeInput) {
          return;
        }
        currentTitle = editInputEl.value;
      }

      if (currentTitle.trim().toLowerCase() === title.trim().toLowerCase()) {
        duplicate = true;
      }
    });
    return duplicate;
  }

  function updateCounters() {
    const todoList = document.getElementById('todo-list');
    const inprogressList = document.getElementById('inprogress-list');
    const doneList = document.getElementById('done-list');

    document.getElementById('todo-count').textContent = todoList.children.length;
    document.getElementById('inprogress-count').textContent = inprogressList.children.length;
    document.getElementById('done-count').textContent = doneList.children.length;
  }

  function runSearch() {
    const query = searchInput.value.toLowerCase().trim();
    const cards = document.querySelectorAll('.task-card');
    cards.forEach(card => {
      const titleEl = card.querySelector('.task-card-title');
      const editInputEl = card.querySelector('.card-edit-input');
      let title = '';

      if (titleEl) {
        title = titleEl.textContent.toLowerCase();
      } else if (editInputEl) {
        title = editInputEl.value.toLowerCase();
      }

      if (title.includes(query)) {
        card.style.display = '';
      } else {
        card.style.display = 'none';
      }
    });
  }

  function fillCardActions(container, columnId) {
    container.innerHTML = '';
    if (columnId === 'todo') {
      const startBtn = document.createElement('button');
      startBtn.className = 'action-btn start-btn';
      startBtn.textContent = 'Start';

      const editBtn = document.createElement('button');
      editBtn.className = 'action-btn edit-btn';
      editBtn.textContent = 'Edit';

      const moveBtn = document.createElement('button');
      moveBtn.className = 'action-btn move-btn';
      moveBtn.textContent = 'Move';

      container.appendChild(startBtn);
      container.appendChild(editBtn);
      container.appendChild(moveBtn);
    } else if (columnId === 'inprogress') {
      const moveBtn = document.createElement('button');
      moveBtn.className = 'action-btn move-btn';
      moveBtn.textContent = 'Move';

      const doneBtn = document.createElement('button');
      doneBtn.className = 'action-btn done-btn';
      doneBtn.textContent = 'Done';

      container.appendChild(moveBtn);
      container.appendChild(doneBtn);
    } else if (columnId === 'done') {
      const archiveBtn = document.createElement('button');
      archiveBtn.className = 'action-btn archive-btn';
      archiveBtn.textContent = 'Archive';

      const deleteBtn = document.createElement('button');
      deleteBtn.className = 'action-btn delete-btn';
      deleteBtn.textContent = 'Delete';

      container.appendChild(archiveBtn);
      container.appendChild(deleteBtn);
    }
  }

  function createTaskCard(title, columnId) {
    const card = document.createElement('div');
    card.className = 'task-card';

    const header = document.createElement('div');
    header.className = 'task-card-header';
    header.textContent = 'Title';

    const titleDiv = document.createElement('div');
    titleDiv.className = 'task-card-title';
    titleDiv.textContent = title;

    const actions = document.createElement('div');
    actions.className = 'task-card-actions';

    fillCardActions(actions, columnId);

    card.appendChild(header);
    card.appendChild(titleDiv);
    card.appendChild(actions);

    return card;
  }

  const forms = [
    { formId: 'todo-form', inputId: 'todo-input', listId: 'todo-list', columnId: 'todo' },
    { formId: 'inprogress-form', inputId: 'inprogress-input', listId: 'inprogress-list', columnId: 'inprogress' },
    { formId: 'done-form', inputId: 'done-input', listId: 'done-list', columnId: 'done' }
  ];

  forms.forEach(item => {
    const form = document.getElementById(item.formId);
    const input = document.getElementById(item.inputId);
    const list = document.getElementById(item.listId);

    form.addEventListener('submit', (e) => {
      e.preventDefault();
      const val = input.value;

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
      if (val.includes('<') || val.includes('>')) {
        showToast('HTML tags are not allowed in the task name.');
        return;
      }
      if (isDuplicate(val)) {
        showToast('A task with this title already exists.');
        return;
      }

      const card = createTaskCard(val.trim(), item.columnId);
      list.appendChild(card);
      input.value = '';
      updateCounters();
      runSearch();
    });
  });

  document.querySelector('.board-layout').addEventListener('click', (e) => {
    const btn = e.target;
    if (!btn.classList.contains('action-btn')) {
      return;
    }

    const actions = btn.parentNode;
    const card = actions.parentNode;
    const currentList = card.parentNode;

    if (btn.classList.contains('start-btn') || (btn.classList.contains('move-btn') && currentList.id === 'todo-list')) {
      const targetList = document.getElementById('inprogress-list');
      currentList.removeChild(card);
      targetList.appendChild(card);
      fillCardActions(actions, 'inprogress');
      updateCounters();
      runSearch();
    } else if (btn.classList.contains('done-btn') || (btn.classList.contains('move-btn') && currentList.id === 'inprogress-list')) {
      const targetList = document.getElementById('done-list');
      currentList.removeChild(card);
      targetList.appendChild(card);
      fillCardActions(actions, 'done');
      updateCounters();
      runSearch();
    } else if (btn.classList.contains('delete-btn') || btn.classList.contains('archive-btn')) {
      currentList.removeChild(card);
      updateCounters();
    } else if (btn.classList.contains('edit-btn')) {
      const titleDiv = card.querySelector('.task-card-title');
      const currentTitle = titleDiv.textContent;

      const input = document.createElement('input');
      input.type = 'text';
      input.className = 'card-edit-input';
      input.value = currentTitle;

      card.insertBefore(input, titleDiv);
      card.removeChild(titleDiv);

      btn.className = 'action-btn save-btn';
      btn.textContent = 'Save';

      const siblingButtons = actions.querySelectorAll('.action-btn');
      siblingButtons.forEach(b => {
        if (b !== btn) {
          b.style.display = 'none';
        }
      });
    } else if (btn.classList.contains('save-btn')) {
      const input = card.querySelector('.card-edit-input');
      const val = input.value;

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
      if (val.includes('<') || val.includes('>')) {
        showToast('HTML tags are not allowed in the task name.');
        return;
      }
      if (isDuplicate(val, input)) {
        showToast('A task with this title already exists.');
        return;
      }

      const titleDiv = document.createElement('div');
      titleDiv.className = 'task-card-title';
      titleDiv.textContent = val.trim();

      card.insertBefore(titleDiv, input);
      card.removeChild(input);

      btn.className = 'action-btn edit-btn';
      btn.textContent = 'Edit';

      const siblingButtons = actions.querySelectorAll('.action-btn');
      siblingButtons.forEach(b => {
        b.style.display = '';
      });
      runSearch();
    }
  });

  searchInput.addEventListener('input', runSearch);

  updateCounters();
  runSearch();
});

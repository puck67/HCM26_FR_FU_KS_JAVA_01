// DOM operations using native JavaScript DOM APIs

// Rebuild buttons inside a card depending on its column
function buildButtons(card, colKey) {
  const actions = card.querySelector('.card__actions');

  // Clear existing buttons
  while (actions.firstChild) {
    actions.removeChild(actions.firstChild);
  }

  if (colKey === 'todo') {
    const startBtn = _makeBtn('btn btn--move', _arrowIcon() + ' Start');
    startBtn.title = 'Move to In Progress';
    actions.appendChild(startBtn);
    actions.appendChild(_makeBtn('btn btn--outline', '✏️ Edit'));

  } else if (colKey === 'inprogress') {
    actions.appendChild(_makeBtn('btn btn--done',    '✓ Done'));
    actions.appendChild(_makeBtn('btn btn--outline', '✏️ Edit'));
    actions.appendChild(_makeBtn('btn btn--move-ip', _arrowIcon() + ' Move'));

  } else if (colKey === 'done') {
    actions.appendChild(_makeBtn('btn btn--outline', '✏️ Edit'));
    actions.appendChild(_makeBtn('btn btn--archive', '📦 Archive'));
    actions.appendChild(_makeBtn('btn btn--delete',  '🗑 Delete'));
  }
}

// Helper to create button elements
function _makeBtn(className, html) {
  const btn = document.createElement('button');
  btn.className = className;
  btn.innerHTML = html;
  return btn;
}

// Inline SVG arrow icon
function _arrowIcon() {
  return `<svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
    <path d="M5 12h14M12 5l7 7-7 7"/>
  </svg>`;
}

// Create a card element with the given title
function createCard(title, colKey) {
  const card = document.createElement('div');
  card.className = 'card';
  card.setAttribute('data-title', title.toLowerCase());

  const meta = document.createElement('p');
  meta.className = 'card__meta';
  meta.textContent = 'Title';
  card.appendChild(meta);

  const titleEl = document.createElement('h3');
  titleEl.className = 'card__title';
  titleEl.textContent = title;
  card.appendChild(titleEl);

  const actions = document.createElement('div');
  actions.className = 'card__actions';
  card.appendChild(actions);

  buildButtons(card, colKey);
  return card;
}

// Add a card to the target column
function addCard(colKey, title) {
  const cardsEl = document.getElementById(COLUMNS[colKey].cards);
  const card    = createCard(title, colKey);
  cardsEl.appendChild(card);
  refreshCounts();
}

// Turn card title into an input to rename in place
function editCard(card) {
  if (card.querySelector('input.card__edit-input')) return;

  const titleEl = card.querySelector('.card__title');
  const original = titleEl.textContent;

  const input = document.createElement('input');
  input.className = 'card__edit-input';
  input.value = original;

  titleEl.replaceWith(input);
  input.focus();
  input.select();

  const save = () => {
    const newText  = input.value.trim() || original;
    const newTitle = document.createElement('h3');
    newTitle.className  = 'card__title';
    newTitle.textContent = newText;
    card.setAttribute('data-title', newText.toLowerCase());
    input.replaceWith(newTitle);
    showToast('Task renamed ✔');
  };

  input.addEventListener('blur', save);
  input.addEventListener('keydown', e => {
    if (e.key === 'Enter')  input.blur();
    if (e.key === 'Escape') { input.value = original; input.blur(); }
  });
}

// Handle clicking the "+" add button
document.querySelectorAll('.column__add .btn-add').forEach(btn => {
  btn.addEventListener('click', () => {
    const input = btn.previousElementSibling;
    const val   = input.value.trim();
    if (!val) return;
    addCard(COL_KEY[btn.dataset.target], val);
    input.value = '';
    input.focus();
  });
});

// Handle pressing Enter inside column input
document.querySelectorAll('.column__add input').forEach(input => {
  input.addEventListener('keydown', e => {
    if (e.key !== 'Enter') return;
    const val = input.value.trim();
    if (!val) return;
    addCard(COL_KEY[input.dataset.target], val);
    input.value = '';
  });
});

// Board columns definition and workflow pipeline
const COLUMNS = {
  todo: {
    id:    'col-todo',
    cards: 'cards-todo',
    next:  'inprogress',
  },
  inprogress: {
    id:    'col-inprogress',
    cards: 'cards-inprogress',
    next:  'done',
  },
  done: {
    id:    'col-done',
    cards: 'cards-done',
    next:  null,
  },
};

// Map column element IDs back to keys (e.g., 'col-todo' -> 'todo')
const COL_KEY = Object.fromEntries(
  Object.entries(COLUMNS).map(([key, cfg]) => [cfg.id, key])
);

// Default tasks for first load
const SEED_TASKS = [
  { col: 'todo',       title: 'Website Homepage Redesign' },
  { col: 'todo',       title: 'Market Research Report' },
  { col: 'inprogress', title: 'User Testing Sessions' },
  { col: 'done',       title: 'Completed Features List' },
];

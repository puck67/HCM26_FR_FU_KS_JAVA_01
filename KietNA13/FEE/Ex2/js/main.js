// Application entry point
(function init() {
  // Populate the board with default tasks
  SEED_TASKS.forEach(({ col, title }) => addCard(col, title));
})();

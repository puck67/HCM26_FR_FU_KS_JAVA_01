// Show a temporary toast message in the bottom-right corner
function showToast(message, duration = 2400) {
  const container = document.getElementById('toast-container');

  const toast = document.createElement('div');
  toast.className = 'toast';
  toast.textContent = message;
  container.appendChild(toast);

  setTimeout(() => {
    toast.classList.add('toast--exit');
    toast.addEventListener('animationend', () => toast.remove(), { once: true });
  }, duration);
}

// Recalculate badge counts for each column and the global total
function refreshCounts() {
  let total = 0;

  Object.keys(COLUMNS).forEach(key => {
    const cardsEl = document.getElementById(COLUMNS[key].cards);
    const count   = cardsEl.querySelectorAll('.card').length;

    document.querySelector(`[data-badge="${key}"]`).textContent = count;
    total += count;
  });

  document.getElementById('total-count').textContent = total;
  refreshEmptyStates();
}

// Toggle the "No tasks yet" placeholder depending on card count
function refreshEmptyStates() {
  Object.keys(COLUMNS).forEach(key => {
    const cardsEl = document.getElementById(COLUMNS[key].cards);
    const existing = cardsEl.querySelector('.empty-state');
    const hasCards  = cardsEl.querySelectorAll('.card').length > 0;

    if (hasCards) {
      if (existing) existing.remove();
    } else {
      if (!existing) {
        const placeholder = document.createElement('div');
        placeholder.className = 'empty-state';
        placeholder.innerHTML = `
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <rect x="3" y="3" width="18" height="18" rx="3"/>
            <path d="M9 12h6M12 9v6"/>
          </svg>
          <div>No tasks yet</div>`;
        cardsEl.appendChild(placeholder);
      }
    }
  });
}

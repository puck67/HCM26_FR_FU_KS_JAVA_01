document.addEventListener('DOMContentLoaded', () => {
  const cards = document.querySelectorAll('.gallery-card');
  const avgRatingValue = document.getElementById('avg-rating-value');
  const avgRatingCount = document.getElementById('avg-rating-count');
  const totalFavValue = document.getElementById('total-fav-value');
  const toastContainer = document.getElementById('toast-container');

  function showError(title, message) {
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.innerHTML = `
      <div class="toast-icon">
        <svg viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg>
      </div>
      <div class="toast-content">
        <div class="toast-title">${title}</div>
        <div class="toast-message">${message}</div>
      </div>
      <button class="toast-close">
        <svg viewBox="0 0 24 24"><path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>
      </button>
    `;
    toastContainer.appendChild(toast);

    requestAnimationFrame(() => {
      toast.classList.add('show');
    });

    const closeBtn = toast.querySelector('.toast-close');
    closeBtn.addEventListener('click', () => {
      toast.classList.remove('show');
      setTimeout(() => {
        toast.remove();
      }, 300);
    });

    setTimeout(() => {
      if (toast.parentNode) {
        toast.classList.remove('show');
        setTimeout(() => {
          toast.remove();
        }, 300);
      }
    }, 4000);
  }

  function validateCardData(card) {
    const id = card.getAttribute('data-id');
    const ratingStr = card.getAttribute('data-rating');
    const favoriteStr = card.getAttribute('data-favorite');

    if (!id) {
      showError('Validation Error', 'A gallery card is missing a data-id attribute.');
      return false;
    }

    const rating = parseFloat(ratingStr);
    if (isNaN(rating) || rating < 0 || rating > 5) {
      showError('Invalid Data', `Card ${id} has an invalid rating: "${ratingStr}". Defaulting to 0.`);
      card.setAttribute('data-rating', '0');
    }

    if (favoriteStr !== 'true' && favoriteStr !== 'false') {
      showError('Invalid Data', `Card ${id} has an invalid favorite value: "${favoriteStr}". Defaulting to false.`);
      card.setAttribute('data-favorite', 'false');
    }

    return true;
  }

  function renderCardState(card) {
    const rating = parseInt(card.getAttribute('data-rating') || '0', 10);
    const isFavorite = card.getAttribute('data-favorite') === 'true';

    const stars = card.querySelectorAll('.star-btn');
    stars.forEach(star => {
      const val = parseInt(star.getAttribute('data-value'), 10);
      if (val <= rating) {
        star.classList.add('filled');
      } else {
        star.classList.remove('filled');
      }
    });

    const favBtn = card.querySelector('.favorite-btn');
    if (isFavorite) {
      favBtn.classList.add('active');
    } else {
      favBtn.classList.remove('active');
    }
  }

  function updateFooter() {
    let totalItems = 0;
    let sumRatings = 0;
    let totalFavorites = 0;

    const currentCards = document.querySelectorAll('.gallery-card');

    if (currentCards.length === 0) {
      showError('System Warning', 'No gallery cards found. Calculations cannot be performed.');
      avgRatingValue.textContent = '0.0';
      avgRatingCount.textContent = '(based on 0 items)';
      totalFavValue.textContent = '0';
      return;
    }

    currentCards.forEach(card => {
      const id = card.getAttribute('data-id');
      const rating = parseFloat(card.getAttribute('data-rating') || '0');
      const isFav = card.getAttribute('data-favorite') === 'true';

      if (isNaN(rating) || rating < 0 || rating > 5) {
        showError('Calculation Error', `Card ${id || 'unknown'} has invalid rating data during calculation.`);
        return;
      }

      sumRatings += rating;
      if (isFav) {
        totalFavorites += 1;
      }
      totalItems += 1;
    });

    const avg = totalItems > 0 ? (sumRatings / totalItems).toFixed(1) : '0.0';

    avgRatingValue.textContent = avg;
    avgRatingCount.textContent = `(based on ${totalItems} items)`;
    totalFavValue.textContent = totalFavorites;
  }

  cards.forEach(card => {
    validateCardData(card);
    renderCardState(card);
  });

  updateFooter();

  cards.forEach(card => {
    const stars = card.querySelectorAll('.star-btn');
    stars.forEach(star => {
      star.addEventListener('click', (e) => {
        e.stopPropagation();
        const valueStr = star.getAttribute('data-value');
        const value = parseInt(valueStr, 10);

        if (isNaN(value) || value < 1 || value > 5) {
          showError('Interaction Error', 'Attempted to set an invalid star rating.');
          return;
        }

        card.setAttribute('data-rating', valueStr);
        renderCardState(card);
        updateFooter();
      });
    });

    const favBtn = card.querySelector('.favorite-btn');
    favBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      const currentFav = card.getAttribute('data-favorite');
      const nextFav = currentFav === 'true' ? 'false' : 'true';

      card.setAttribute('data-favorite', nextFav);
      renderCardState(card);
      updateFooter();
    });
  });
});

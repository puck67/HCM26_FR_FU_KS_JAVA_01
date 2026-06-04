document.addEventListener('DOMContentLoaded', () => {
  const cards = document.querySelectorAll('.image-card');
  const avgRatingSpan = document.getElementById('avg-rating');
  const ratedItemsCountSpan = document.getElementById('rated-items-count');
  const totalFavsSpan = document.getElementById('total-favs');

  // Calculates average rating and favorites count, then updates footer elements manually
  function updateSummary() {
    let totalStars = 0;
    let ratedItemsCount = 0;
    let totalFavorites = 0;

    cards.forEach(card => {
      const rating = parseInt(card.getAttribute('data-rating')) || 0;
      const isFavorite = card.getAttribute('data-favorite') === 'true';

      if (rating > 0) {
        totalStars += rating;
        ratedItemsCount++;
      }

      if (isFavorite) {
        totalFavorites++;
      }
    });

    const averageRating = ratedItemsCount > 0 ? (totalStars / ratedItemsCount).toFixed(1) : '0.0';

    // Update summary footer elements
    avgRatingSpan.innerText = averageRating;
    ratedItemsCountSpan.innerText = ratedItemsCount.toString();
    totalFavsSpan.innerText = totalFavorites.toString();
  }

  // Set up event listeners for each card
  cards.forEach(card => {
    const stars = card.querySelectorAll('.star-icon');
    const favBtn = card.querySelector('.btn-favorite');
    const favIcon = favBtn.querySelector('i');

    // Star clicks
    stars.forEach(star => {
      star.addEventListener('click', () => {
        const clickedValue = parseInt(star.getAttribute('data-value')) || 0;
        
        // Update card's data attribute
        card.setAttribute('data-rating', clickedValue.toString());

        // Update visual stars
        stars.forEach(s => {
          const sValue = parseInt(s.getAttribute('data-value')) || 0;
          if (sValue <= clickedValue) {
            s.classList.add('active');
            s.classList.replace('fa-regular', 'fa-solid');
          } else {
            s.classList.remove('active');
            s.classList.replace('fa-solid', 'fa-regular');
          }
        });

        // Recalculate summary stats
        updateSummary();
      });
    });

    // Favorite Button toggle
    favBtn.addEventListener('click', () => {
      const isFavorite = card.getAttribute('data-favorite') === 'true';
      const newFavState = !isFavorite;

      // Toggle card's data attribute
      card.setAttribute('data-favorite', newFavState.toString());

      // Toggle UI button classes and heart icons
      if (newFavState) {
        favBtn.classList.add('active');
        favIcon.classList.replace('fa-regular', 'fa-solid');
      } else {
        favBtn.classList.remove('active');
        favIcon.classList.replace('fa-solid', 'fa-regular');
      }

      // Recalculate summary stats
      updateSummary();
    });
  });

  // Calculate summary stats on load
  updateSummary();
});

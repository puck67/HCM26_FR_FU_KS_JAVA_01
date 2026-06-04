document.addEventListener('DOMContentLoaded', function () {
  const cards = document.querySelectorAll('.card');

  function updateTotals() {
    let totalStars = 0;
    let ratedCount = 0;
    let favoritesCount = 0;

    cards.forEach(function (card) {
      const rating = parseInt(card.getAttribute('data-rating') || '0');
      if (rating > 0) {
        totalStars += rating;
        ratedCount++;
      }

      const heart = card.querySelector('.heart');
      if (heart && heart.classList.contains('favorited')) {
        favoritesCount++;
      }
    });

    const average = ratedCount > 0 ? (totalStars / ratedCount).toFixed(1) : '0.0';
    document.getElementById('average-rating-val').textContent = average + ' Stars';
    document.getElementById('average-count-val').textContent = '(based on ' + ratedCount + ' items)';
    document.getElementById('total-favorites-val').textContent = favoritesCount;
  }

  cards.forEach(function (card) {
    const stars = card.querySelectorAll('.star');
    const heartBox = card.querySelector('.heart-box');
    const heart = card.querySelector('.heart');

    stars.forEach(function (star) {
      star.addEventListener('click', function () {
        const clickedValue = parseInt(star.getAttribute('data-value'));
        card.setAttribute('data-rating', clickedValue);

        stars.forEach(function (s) {
          const val = parseInt(s.getAttribute('data-value'));
          if (val <= clickedValue) {
            // doi mau
            s.textContent = '★';
            // doi mau
            s.classList.remove('empty');
          } else {
            // doi mau
            s.textContent = '☆';
            // doi mau
            s.classList.add('empty');
          }
        });

        updateTotals();
      });
    });

    if (heartBox && heart) {
      heartBox.addEventListener('click', function () {
        if (heart.classList.contains('favorited')) {
          // doi mau
          heart.classList.remove('favorited');
          // doi mau
          heart.textContent = '♡';
        } else {
          // doi mau
          heart.classList.add('favorited');
          // doi mau
          heart.textContent = '♥';
        }
        updateTotals();
      });
    }
  });

  updateTotals();
});

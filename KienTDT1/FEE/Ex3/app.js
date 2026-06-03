document.addEventListener('DOMContentLoaded', () => {
    const cards = Array.from(document.querySelectorAll('.gallery-card'));
    const avgRatingEl = document.querySelector('.avg-rating strong');
    const totalFavsEl = document.querySelector('.total-favorites strong');

    cards.forEach(card => {
        const stars = Array.from(card.querySelectorAll('.stars i'));
        const favBtn = card.querySelector('.fav-btn');

        function renderStars(rating) {
            stars.forEach(star => {
                const starValue = parseInt(star.dataset.value, 10);
                if (starValue <= rating) {
                    star.classList.add('checked', 'fa-solid');
                    star.classList.remove('fa-regular');
                } else {
                    star.classList.remove('checked', 'fa-solid');
                    star.classList.add('fa-regular');
                }
            });
        }

        function getCardRating() {
            return parseInt(card.getAttribute('data-rating'), 10) || 0;
        }

        renderStars(getCardRating());

        stars.forEach(star => {
            const starValue = parseInt(star.dataset.value, 10);

            star.addEventListener('mouseover', () => {
                renderStars(starValue);
            });

            star.addEventListener('mouseout', () => {
                renderStars(getCardRating());
            });

            star.addEventListener('click', () => {
                card.setAttribute('data-rating', starValue);
                renderStars(starValue);
                updateSummaryFooter();
            });
        });

        if (favBtn) {
            favBtn.addEventListener('click', () => {
                const isFav = card.classList.toggle('is-favorite');
                const heartIcon = favBtn.querySelector('i');

                if (heartIcon) {
                    heartIcon.className = isFav ? 'fa-solid fa-heart' : 'fa-regular fa-heart';
                }

                favBtn.style.color = isFav ? '#e74c3c' : '#555';
                updateSummaryFooter();
            });
        }
    });

    function updateSummaryFooter() {
        let totalRating = 0;
        let ratedCardsCount = 0;
        let totalFavorites = 0;

        cards.forEach(card => {
            const rating = parseInt(card.getAttribute('data-rating'), 10);
            if (!Number.isNaN(rating) && rating > 0) {
                totalRating += rating;
                ratedCardsCount++;
            }

            if (card.classList.contains('is-favorite')) {
                totalFavorites++;
            }
        });

        const average = ratedCardsCount > 0 ? (totalRating / ratedCardsCount).toFixed(1) : '0.0';
        avgRatingEl.textContent = `AVERAGE RATING: ${average} Stars`;
        totalFavsEl.textContent = `TOTAL FAVORITES: ${totalFavorites}`;
    }

    updateSummaryFooter();
});
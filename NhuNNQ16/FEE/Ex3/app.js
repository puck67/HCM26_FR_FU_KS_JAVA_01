document.addEventListener('DOMContentLoaded', () => {
    // Select necessary DOM elements
    const cards = document.querySelectorAll('.card');
    const avgRatingEl = document.getElementById('avg-rating');
    const ratedCountEl = document.getElementById('rated-count');
    const totalFavsEl = document.getElementById('total-favs');

    // Function to calculate and update the summary footer
    function updateSummary() {
        let totalRating = 0;
        let ratedItemsCount = 0;
        let totalFavorites = 0;

        // Loop through all cards to calculate values
        cards.forEach(card => {
            // Check rating
            const starsContainer = card.querySelector('.stars-container');
            const rating = parseInt(starsContainer.getAttribute('data-rating')) || 0;
            if (rating > 0) {
                totalRating += rating;
                ratedItemsCount++;
            }

            // Check favorite
            const favBtn = card.querySelector('.fav-btn');
            if (favBtn.classList.contains('active')) {
                totalFavorites++;
            }
        });

        // Calculate average
        const average = ratedItemsCount > 0 ? (totalRating / ratedItemsCount).toFixed(1) : '0.0';

        // Update footer text content manually using DOM API
        avgRatingEl.textContent = `${average} Stars`;
        ratedCountEl.textContent = `(based on ${ratedItemsCount} items)`;
        totalFavsEl.textContent = totalFavorites;
    }

    // Attach Event Listeners to each card
    cards.forEach(card => {
        const starsContainer = card.querySelector('.stars-container');
        const starButtons = starsContainer.querySelectorAll('.star-btn');
        const favBtn = card.querySelector('.fav-btn');
        const tooltip = card.querySelector('.tooltip');

        // Rating Star Click Handler
        starButtons.forEach(btn => {
            btn.addEventListener('click', () => {
                const clickedValue = parseInt(btn.getAttribute('data-value'));
                
                // Update parent container data-rating attribute
                starsContainer.setAttribute('data-rating', clickedValue);

                // Update visual state of all stars in this card
                starButtons.forEach(star => {
                    const starVal = parseInt(star.getAttribute('data-value'));
                    if (starVal <= clickedValue) {
                        star.classList.add('filled');
                    } else {
                        star.classList.remove('filled');
                    }
                });

                // Trigger dynamic recalculation
                updateSummary();
            });
        });

        // Favorite Toggle Handler
        favBtn.addEventListener('click', () => {
            // Toggle active class
            const isFav = favBtn.classList.toggle('active');

            // Update tooltip text and button label/attributes
            if (isFav) {
                tooltip.textContent = 'Remove from Favorites';
                favBtn.setAttribute('aria-label', 'Remove from Favorites');
            } else {
                tooltip.textContent = 'Add to Favorites';
                favBtn.setAttribute('aria-label', 'Add to Favorites');
            }

            // Trigger dynamic recalculation
            updateSummary();
        });
    });

    // Run initial calculation to synchronize footer with initial HTML layout
    updateSummary();
});

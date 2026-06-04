// Problem 02 - Pure JavaScript Implementation
document.addEventListener('DOMContentLoaded', () => {
    
    // Select all cards
    const cards = document.querySelectorAll('.card');
    
    // Select footer elements
    const avgRatingValue = document.getElementById('avgRatingValue');
    const totalRatedItems = document.getElementById('totalRatedItems');
    const totalFavoritesValue = document.getElementById('totalFavoritesValue');

    function updateFooter() {
        let totalStars = 0;
        let ratedCount = 0;
        let favCount = 0;

        cards.forEach(card => {
            // Check rating
            const currentRating = parseInt(card.getAttribute('data-rating') || '0');
            if (currentRating > 0) {
                totalStars += currentRating;
                ratedCount++;
            }

            // Check favorite
            const favBtn = card.querySelector('.fav-btn');
            if (favBtn.classList.contains('active')) {
                favCount++;
            }
        });

        // Calculate average
        let avg = 0;
        if (ratedCount > 0) {
            avg = totalStars / ratedCount;
        }

        // Update DOM
        avgRatingValue.textContent = avg.toFixed(1);
        totalRatedItems.textContent = ratedCount;
        totalFavoritesValue.textContent = favCount;
    }

    cards.forEach(card => {
        const stars = card.querySelectorAll('.star');
        const favBtn = card.querySelector('.fav-btn');

        // Rating Logic
        stars.forEach(star => {
            star.addEventListener('click', function() {
                const val = parseInt(this.getAttribute('data-val'));
                
                // Save rating to card
                card.setAttribute('data-rating', val);

                // Update visual stars
                stars.forEach(s => {
                    if (parseInt(s.getAttribute('data-val')) <= val) {
                        s.classList.add('filled');
                    } else {
                        s.classList.remove('filled');
                    }
                });

                // Update Footer manually
                updateFooter();
            });
        });

        // Favorite Toggle Logic
        favBtn.addEventListener('click', function() {
            if (this.classList.contains('active')) {
                this.classList.remove('active');
                this.textContent = '♡';
            } else {
                this.classList.add('active');
                this.textContent = '♥';
            }
            
            // Update Footer manually
            updateFooter();
        });
    });

});

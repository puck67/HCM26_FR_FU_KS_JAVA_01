document.addEventListener('DOMContentLoaded', () => {
    const galleryGrid = document.getElementById('gallery-grid');
    const avgRatingSection = document.getElementById('avg-rating-section');
    const totalFavSection = document.getElementById('total-fav-section');

    // Prepopulated cards data to match the screenshot average and favorites count
    const initialCards = [
        { id: 1, gradient: 'linear-gradient(135deg, #a7f3d0, #34d399)', rating: 5, favorite: true },
        { id: 2, gradient: 'linear-gradient(135deg, #fde68a, #f59e0b)', rating: 4, favorite: false },
        { id: 3, gradient: 'linear-gradient(135deg, #93c5fd, #1d4ed8)', rating: 4, favorite: true },
        { id: 4, gradient: 'linear-gradient(135deg, #67e8f9, #0891b2)', rating: 5, favorite: false },
        { id: 5, gradient: 'linear-gradient(135deg, #fbcfe8, #db2777)', rating: 3, favorite: true },
        { id: 6, gradient: 'linear-gradient(135deg, #fca5a5, #dc2626)', rating: 4, favorite: false },
        { id: 7, gradient: 'linear-gradient(135deg, #a5f3fc, #0d9488)', rating: 5, favorite: true },
        { id: 8, gradient: 'linear-gradient(135deg, #c6f6d5, #38a169)', rating: 4, favorite: false },
        { id: 9, gradient: 'linear-gradient(135deg, #fecaca, #e11d48)', rating: 4, favorite: true },
        { id: 10, gradient: 'linear-gradient(135deg, #6ee7b7, #2563eb)', rating: 0, favorite: false }
    ];

    // Keep track of runtime state
    const cardsState = [...initialCards];

    // Render cards using standard DOM API
    cardsState.forEach(cardData => {
        const card = document.createElement('div');
        card.className = 'gallery-card';
        card.setAttribute('data-id', cardData.id);

        // Color block placeholder
        const colorBlock = document.createElement('div');
        colorBlock.className = 'image-placeholder';
        colorBlock.style.background = cardData.gradient;

        // Overlay Badge
        const badge = document.createElement('div');
        badge.className = 'fav-badge';
        badge.textContent = 'Add to Favorites';
        colorBlock.appendChild(badge);
        card.appendChild(colorBlock);

        // Rating bar (Stars & Heart)
        const ratingBar = document.createElement('div');
        ratingBar.className = 'rating-bar';

        // Stars container
        const starsContainer = document.createElement('div');
        starsContainer.className = 'stars-container';

        // Render 5 stars
        for (let i = 1; i <= 5; i++) {
            const star = document.createElement('i');
            star.className = 'fa-star ' + (i <= cardData.rating ? 'fa-solid active' : 'fa-regular');
            star.setAttribute('data-value', i);
            
            // Native Event click to change rating
            star.addEventListener('click', () => {
                const currentRating = cardData.rating;
                let newRating = i;
                
                // If they click the same rating, toggle it to 0 (optional but nice)
                if (currentRating === i) {
                    newRating = 0;
                }

                cardData.rating = newRating;
                updateStarDisplay(starsContainer, newRating);
                updateSummaryFooter();
            });

            starsContainer.appendChild(star);
        }
        ratingBar.appendChild(starsContainer);

        // Favorite toggle button
        const btnFav = document.createElement('button');
        btnFav.className = 'btn-fav' + (cardData.favorite ? ' active' : '');
        btnFav.innerHTML = '<i class="' + (cardData.favorite ? 'fa-solid' : 'fa-regular') + ' fa-heart"></i>';
        
        btnFav.addEventListener('click', () => {
            cardData.favorite = !cardData.favorite;
            
            // Toggle active classes
            if (cardData.favorite) {
                btnFav.classList.add('active');
                btnFav.innerHTML = '<i class="fa-solid fa-heart"></i>';
            } else {
                btnFav.classList.remove('active');
                btnFav.innerHTML = '<i class="fa-regular fa-heart"></i>';
            }

            updateSummaryFooter();
        });

        ratingBar.appendChild(btnFav);
        card.appendChild(ratingBar);
        galleryGrid.appendChild(card);
    });

    // Initial footer update
    updateSummaryFooter();

    // Helper: update stars coloring inside a card
    function updateStarDisplay(container, rating) {
        const stars = container.querySelectorAll('.fa-star');
        stars.forEach((star, index) => {
            const value = index + 1;
            if (value <= rating) {
                star.className = 'fa-star fa-solid active';
            } else {
                star.className = 'fa-star fa-regular';
            }
        });
    }

    // Helper: recalculate math and update summary footer text manually
    function updateSummaryFooter() {
        let ratedCount = 0;
        let sumRating = 0;
        let favCount = 0;

        cardsState.forEach(card => {
            if (card.rating > 0) {
                ratedCount++;
                sumRating += card.rating;
            }
            if (card.favorite) {
                favCount++;
            }
        });

        const avgRating = ratedCount > 0 ? (sumRating / ratedCount) : 0;
        
        // Manual updates using textContent & innerHTML (Standard DOM API)
        avgRatingSection.innerHTML = 'AVERAGE RATING: ' + avgRating.toFixed(1) + ' Stars <span class="based-text">(based on ' + ratedCount + ' items)</span>';
        totalFavSection.innerHTML = 'TOTAL FAVORITES: ' + favCount + ' <i class="fa-solid fa-heart footer-heart ml-2"></i>';
    }
});

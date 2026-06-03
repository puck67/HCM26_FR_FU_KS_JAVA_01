document.addEventListener('DOMContentLoaded', () => {
    const avgRatingEl = document.getElementById('avg-rating');
    const ratingCountEl = document.getElementById('rating-count');
    const totalFavsEl = document.getElementById('total-favs');

    let cardsData = [];
    const cards = document.querySelectorAll('.card');
    
    cards.forEach((card, index) => {
        cardsData.push({ id: index, rating: 0, favorite: false });
        
        const cardStars = card.querySelectorAll('.star');
        const starsContainer = card.querySelector('.stars');
        
        cardStars.forEach(star => {
            star.addEventListener('mousemove', (e) => {
                const rect = star.getBoundingClientRect();
                const isHalf = (e.clientX - rect.left) < (rect.width / 2);
                const baseVal = parseInt(star.getAttribute('data-value'));
                const hoverVal = isHalf ? baseVal - 0.5 : baseVal;
                renderStars(cardStars, hoverVal);
            });
            
            star.addEventListener('click', (e) => {
                const rect = star.getBoundingClientRect();
                const isHalf = (e.clientX - rect.left) < (rect.width / 2);
                const baseVal = parseInt(star.getAttribute('data-value'));
                cardsData[index].rating = isHalf ? baseVal - 0.5 : baseVal;
                renderStars(cardStars, cardsData[index].rating);
                updateSummary();
            });
        });
        
        starsContainer.addEventListener('mouseleave', () => {
            renderStars(cardStars, cardsData[index].rating);
        });

        const favBtn = card.querySelector('.favorite-btn');
        favBtn.addEventListener('click', () => {
            cardsData[index].favorite = !cardsData[index].favorite;
            if (cardsData[index].favorite) {
                favBtn.classList.add('active');
                favBtn.innerHTML = '&#9829;'; 
            } else {
                favBtn.classList.remove('active');
                favBtn.innerHTML = '&#9825;';
            }
            updateSummary();
        });
    });

    function renderStars(starsNodes, value) {
        starsNodes.forEach(s => {
            const sVal = parseInt(s.getAttribute('data-value'));
            s.classList.remove('filled', 'half-filled');
            if (sVal <= value) {
                s.classList.add('filled');
            } else if (sVal - 0.5 === value) {
                s.classList.add('half-filled');
            }
        });
    }

    function updateSummary() {
        let totalRating = 0;
        let ratedCount = 0;
        let totalFavs = 0;

        cardsData.forEach(data => {
            if (data.rating > 0) {
                totalRating += data.rating;
                ratedCount++;
            }
            if (data.favorite) {
                totalFavs++;
            }
        });

        let avg = ratedCount > 0 ? (totalRating / ratedCount).toFixed(1) : "0.0";
        avgRatingEl.textContent = `${avg} Stars`;
        ratingCountEl.textContent = `(based on ${ratedCount} items)`;
        totalFavsEl.textContent = totalFavs.toString();
    }
});

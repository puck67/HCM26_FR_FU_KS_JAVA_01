document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.gallery-card');
    const avgVal = document.getElementById('average-rating');
    const favVal = document.getElementById('total-favorites');

    const updateAverageRating = () => {
        let sum = 0;
        cards.forEach(card => {
            const widget = card.querySelector('.rating-widget');
            sum += parseInt(widget.getAttribute('data-rating') || '0', 10);
        });
        const average = (sum / cards.length).toFixed(1);
        avgVal.textContent = average;
    };

    const updateTotalFavorites = () => {
        const activeFavs = document.querySelectorAll('.btn-fav.active').length;
        favVal.textContent = activeFavs;
    };

    cards.forEach(card => {
        const widget = card.querySelector('.rating-widget');
        const stars = widget.querySelectorAll('.star');
        const favBtn = card.querySelector('.btn-fav');

        stars.forEach(star => {
            star.addEventListener('click', () => {
                const rating = parseInt(star.getAttribute('data-value'), 10);
                widget.setAttribute('data-rating', rating);

                stars.forEach(s => {
                    const val = parseInt(s.getAttribute('data-value'), 10);
                    if (val <= rating) {
                        s.classList.add('filled');
                    } else {
                        s.classList.remove('filled');
                    }
                });

                updateAverageRating();
            });
        });

        favBtn.addEventListener('click', () => {
            favBtn.classList.toggle('active');
            updateTotalFavorites();
        });
    });

    updateAverageRating();
    updateTotalFavorites();
});

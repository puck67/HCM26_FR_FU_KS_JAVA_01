$(document).ready(function () {
    /**
     * ==========================================
     * PROBLEM 03: JQUERY IMPLEMENTATION
     * Event Handling, Visual Effects, Animations
     * ==========================================
     */

    // Recalculate summary footer with fade animation
    function updateGallerySummary() {
        let totalItems = $('.card').length;
        let totalStars = 0;
        let ratedItemsCount = 0;
        let totalFavorites = 0;

        $('.card').each(function () {
            let currentRating = parseInt($(this).attr('data-rating')) || 0;
            let isFavorited = $(this).attr('data-favorited') === 'true';

            if (currentRating > 0) {
                totalStars += currentRating;
                ratedItemsCount++;
            }
            if (isFavorited) {
                totalFavorites++;
            }
        });

        // Average is based on rated items only (matches sample UI)
        let averageRating = ratedItemsCount > 0
            ? (totalStars / ratedItemsCount).toFixed(1)
            : '0.0';

        // Animate average rating text
        $('#avgRatingText').animate({ opacity: 0.2 }, 120, function () {
            $(this).text(averageRating + ' Stars').animate({ opacity: 1 }, 120);
        });

        // Update "based on X items" — show only rated count
        $('#totalItemsText').text(ratedItemsCount);

        // Animate favorites count
        $('#totalFavsText').animate({ opacity: 0.2 }, 120, function () {
            $(this).text(totalFavorites).animate({ opacity: 1 }, 120);
        });
    }

    // Visual Effect: Star hover pre-fill
    $('.star').on('mouseenter', function () {
        let hoverValue = parseInt($(this).data('value'));
        let $stars = $(this).closest('.rating-controls').find('.star');
        $stars.each(function () {
            if (parseInt($(this).data('value')) <= hoverValue) {
                $(this).addClass('hovered');
            } else {
                $(this).removeClass('hovered');
            }
        });
    });

    $('.rating-controls').on('mouseleave', function () {
        $(this).find('.star').removeClass('hovered');
    });

    // Event: Star click — fill rating
    $('.star').on('click', function () {
        let clickedValue = parseInt($(this).data('value'));
        let $card = $(this).closest('.card');
        let $allStars = $(this).closest('.rating-controls').find('.star');

        $card.attr('data-rating', clickedValue);

        $allStars.each(function () {
            if (parseInt($(this).data('value')) <= clickedValue) {
                $(this).addClass('filled').removeClass('fa-regular').addClass('fa-solid');
            } else {
                $(this).removeClass('filled fa-solid').addClass('fa-regular');
            }
        });

        updateGallerySummary();
    });

    // Event: Favorite toggle
    $('.fav-btn').on('click', function () {
        let $card = $(this).closest('.card');
        let isCurrentlyFav = $card.attr('data-favorited') === 'true';

        $card.attr('data-favorited', String(!isCurrentlyFav));
        $(this).toggleClass('active');

        let $icon = $(this).find('i');
        if (!isCurrentlyFav) {
            $icon.removeClass('fa-regular').addClass('fa-solid');
        } else {
            $icon.removeClass('fa-solid').addClass('fa-regular');
        }

        updateGallerySummary();
    });

    // Initialize
    updateGallerySummary();
});


/*
 * =========================================================================
 * PROBLEM 02: VANILLA JAVASCRIPT IMPLEMENTATION (uncomment to use)
 * Remove the jQuery block above and uncomment this section.
 * =========================================================================

document.addEventListener('DOMContentLoaded', function () {
    const cards = document.querySelectorAll('.card');
    const avgRatingText = document.getElementById('avgRatingText');
    const totalFavsText = document.getElementById('totalFavsText');
    const totalItemsText = document.getElementById('totalItemsText');

    function updateSummaryVanilla() {
        let totalStars = 0;
        let ratedItemsCount = 0;
        let totalFavorites = 0;

        cards.forEach(function (card) {
            let rating = parseInt(card.getAttribute('data-rating')) || 0;
            let isFav = card.getAttribute('data-favorited') === 'true';

            if (rating > 0) {
                totalStars += rating;
                ratedItemsCount++;
            }
            if (isFav) totalFavorites++;
        });

        let average = ratedItemsCount > 0
            ? (totalStars / ratedItemsCount).toFixed(1)
            : '0.0';

        avgRatingText.innerText = average + ' Stars';
        totalItemsText.innerText = ratedItemsCount;
        totalFavsText.innerText = totalFavorites;
    }

    // Star hover pre-fill
    document.querySelectorAll('.rating-controls').forEach(function (group) {
        let stars = group.querySelectorAll('.star');

        stars.forEach(function (star) {
            star.addEventListener('mouseenter', function () {
                let hoverVal = parseInt(this.getAttribute('data-value'));
                stars.forEach(function (s) {
                    if (parseInt(s.getAttribute('data-value')) <= hoverVal) {
                        s.classList.add('hovered');
                    } else {
                        s.classList.remove('hovered');
                    }
                });
            });
        });

        group.addEventListener('mouseleave', function () {
            stars.forEach(function (s) { s.classList.remove('hovered'); });
        });
    });

    // Star click
    document.querySelectorAll('.star').forEach(function (star) {
        star.addEventListener('click', function () {
            let value = parseInt(this.getAttribute('data-value'));
            let card = this.closest('.card');
            card.setAttribute('data-rating', value);

            let allStars = card.querySelectorAll('.star');
            allStars.forEach(function (s) {
                let sVal = parseInt(s.getAttribute('data-value'));
                if (sVal <= value) {
                    s.classList.add('filled', 'fa-solid');
                    s.classList.remove('fa-regular');
                } else {
                    s.classList.remove('filled', 'fa-solid');
                    s.classList.add('fa-regular');
                }
            });

            updateSummaryVanilla();
        });
    });

    // Favorite toggle
    document.querySelectorAll('.fav-btn').forEach(function (btn) {
        btn.addEventListener('click', function () {
            let card = this.closest('.card');
            let isFav = card.getAttribute('data-favorited') === 'true';
            card.setAttribute('data-favorited', String(!isFav));

            this.classList.toggle('active');

            let icon = this.querySelector('i');
            if (!isFav) {
                icon.className = 'fa-solid fa-heart';
            } else {
                icon.className = 'fa-regular fa-heart';
            }

            updateSummaryVanilla();
        });
    });

    updateSummaryVanilla();
});
*/

$(document).ready(function() {
    $('.star').on('mousemove', function(e) {
        const rect = this.getBoundingClientRect();
        const isHalf = (e.clientX - rect.left) < (rect.width / 2);
        const baseVal = parseInt($(this).data('value'));
        const hoverVal = isHalf ? baseVal - 0.5 : baseVal;
        renderStarsJQ($(this).parent(), hoverVal);
    });

    $('.stars').on('mouseleave', function() {
        const cardRating = $(this).closest('.card').data('rating') || 0;
        renderStarsJQ($(this), cardRating);
    });

    $('.star').on('click', function(e) {
        const rect = this.getBoundingClientRect();
        const isHalf = (e.clientX - rect.left) < (rect.width / 2);
        const baseVal = parseInt($(this).data('value'));
        const ratingVal = isHalf ? baseVal - 0.5 : baseVal;
        
        $(this).closest('.card').data('rating', ratingVal);
        renderStarsJQ($(this).parent(), ratingVal);
        updateSummaryJQ();
    });

    $('.favorite-btn').on('click', function() {
        $(this).toggleClass('active');
        if ($(this).hasClass('active')) {
            $(this).html('&#9829;');
            $(this).closest('.card').data('favorite', true);
        } else {
            $(this).html('&#9825;');
            $(this).closest('.card').data('favorite', false);
        }
        updateSummaryJQ();
    });

    function renderStarsJQ($starsContainer, value) {
        $starsContainer.find('.star').each(function() {
            const sVal = parseInt($(this).data('value'));
            $(this).removeClass('filled half-filled');
            if (sVal <= value) {
                $(this).addClass('filled');
            } else if (sVal - 0.5 === value) {
                $(this).addClass('half-filled');
            }
        });
    }

    function updateSummaryJQ() {
        let totalRating = 0;
        let ratedCount = 0;
        let totalFavs = 0;

        $('.card').each(function() {
            const rating = $(this).data('rating') || 0;
            const fav = $(this).data('favorite') || false;

            if (rating > 0) {
                totalRating += rating;
                ratedCount++;
            }
            if (fav) {
                totalFavs++;
            }
        });

        let avg = ratedCount > 0 ? (totalRating / ratedCount).toFixed(1) : "0.0";
        
        $('#avg-rating').fadeOut(150, function() {
            $(this).text(`${avg} Stars`).fadeIn(150);
        });
        $('#rating-count').fadeOut(150, function() {
            $(this).text(`(based on ${ratedCount} items)`).fadeIn(150);
        });
        $('#total-favs').fadeOut(150, function() {
            $(this).text(totalFavs).fadeIn(150);
        });
    }
});

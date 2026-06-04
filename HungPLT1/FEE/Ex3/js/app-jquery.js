$(document).ready(function() {
    const cards = $('.gallery-card');
    const avgVal = $('#average-rating');
    const favVal = $('#total-favorites');

    function animateValueChange(element, newValue) {
        const currentVal = element.text();
        if (currentVal !== newValue) {
            element.fadeOut(150, function() {
                element.text(newValue).fadeIn(150);
            });
        }
    }

    function updateAverageRating() {
        let sum = 0;
        cards.each(function() {
            const rating = parseInt($(this).find('.rating-widget').attr('data-rating') || '0', 10);
            sum += rating;
        });
        const average = (sum / cards.length).toFixed(1);
        animateValueChange(avgVal, average);
    }

    function updateTotalFavorites() {
        const activeFavs = $('.btn-fav.active').length;
        animateValueChange(favVal, activeFavs.toString());
    }

    $('#gallery-grid').on('click', '.star', function(e) {
        e.stopPropagation();
        const star = $(this);
        const rating = parseInt(star.attr('data-value'), 10);
        const widget = star.closest('.rating-widget');

        widget.attr('data-rating', rating);

        widget.find('.star').each(function() {
            const val = parseInt($(this).attr('data-value'), 10);
            if (val <= rating) {
                $(this).addClass('filled');
            } else {
                $(this).removeClass('filled');
            }
        });

        updateAverageRating();
    });

    $('#gallery-grid').on('mouseenter', '.star', function() {
        const star = $(this);
        const val = parseInt(star.attr('data-value'), 10);
        const stars = star.siblings().addBack();

        stars.each(function() {
            const sVal = parseInt($(this).attr('data-value'), 10);
            if (sVal <= val) {
                $(this).addClass('hover');
            } else {
                $(this).removeClass('hover');
            }
        });
    });

    $('#gallery-grid').on('mouseleave', '.rating-widget', function() {
        $(this).find('.star').removeClass('hover');
    });

    $('#gallery-grid').on('click', '.btn-fav', function(e) {
        e.stopPropagation();
        $(this).toggleClass('active');
        updateTotalFavorites();
    });

    updateAverageRating();
    updateTotalFavorites();
});

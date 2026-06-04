$(document).ready(function() {
    var $galleryGrid = $('#gallery-grid');
    var $avgSection = $('#avg-rating-section');
    var $favSection = $('#total-fav-section');

    // Default mock cards matching exact colors and ratings from screenshot
    var initialCards = [
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

    // Track previous summary states to avoid redundant fade animations
    var lastAvgHtml = '';
    var lastFavHtml = '';

    // Render all cards on load using jQuery's iteration method
    $.each(initialCards, function(index, card) {
        var starsHtml = '';
        for (var i = 1; i <= 5; i++) {
            var activeClass = (i <= card.rating) ? 'fa-solid active' : 'fa-regular';
            starsHtml += '<i class="fa-star ' + activeClass + '" data-value="' + i + '"></i>';
        }

        var favActive = card.favorite ? ' active' : '';
        var favIcon = card.favorite ? 'fa-solid' : 'fa-regular';

        var cardHtml = '<div class="gallery-card" data-id="' + card.id + '" data-rating="' + card.rating + '">' +
            '<div class="image-placeholder" style="background: ' + card.gradient + '">' +
                '<div class="fav-badge">Add to Favorites</div>' +
            '</div>' +
            '<div class="rating-bar">' +
                '<div class="stars-container">' + starsHtml + '</div>' +
                '<button type="button" class="btn-fav' + favActive + '">' +
                    '<i class="' + favIcon + ' fa-heart"></i>' +
                '</button>' +
            '</div>' +
        '</div>';

        $galleryGrid.append(cardHtml);
    });

    // Run initial calculation to update footer
    recalculateTotals(true); // true to skip initial fade-in animation

    // 1. Event Delegation using .on('click')
    // Click star to change rating
    $galleryGrid.on('click', '.fa-star', function() {
        var $card = $(this).closest('.gallery-card');
        var clickedValue = parseInt($(this).data('value'));
        var currentRating = parseInt($card.attr('data-rating')) || 0;

        // Toggle rating to 0 if clicked same star
        var newRating = (currentRating === clickedValue) ? 0 : clickedValue;
        
        $card.attr('data-rating', newRating);
        updateStarsMarkup($card.find('.stars-container'), newRating);
        recalculateTotals(false);
    });

    // Toggle favorite state on click
    $galleryGrid.on('click', '.btn-fav', function() {
        var $btn = $(this);
        var $icon = $btn.find('i');

        if ($btn.hasClass('active')) {
            $btn.removeClass('active');
            $icon.removeClass('fa-solid').addClass('fa-regular');
        } else {
            $btn.addClass('active');
            $icon.removeClass('fa-regular').addClass('fa-solid');
        }

        recalculateTotals(false);
    });

    // 2. Visual Effects: Hover stars to preview rating
    $galleryGrid.on('mouseenter', '.fa-star', function() {
        var hoverValue = parseInt($(this).data('value'));
        var $stars = $(this).siblings().addBack();
        
        $stars.each(function() {
            var val = parseInt($(this).data('value'));
            if (val <= hoverValue) {
                $(this).addClass('fa-solid active preview').removeClass('fa-regular');
            } else {
                $(this).removeClass('fa-solid active preview').addClass('fa-regular');
            }
        });
    });

    // Reset stars preview when mouse leaves stars-container
    $galleryGrid.on('mouseleave', '.stars-container', function() {
        var $card = $(this).closest('.gallery-card');
        var rating = parseInt($card.attr('data-rating')) || 0;
        updateStarsMarkup($(this), rating);
    });

    // Helper: update stars class states
    function updateStarsMarkup($container, rating) {
        $container.find('.fa-star').each(function() {
            var val = parseInt($(this).data('value'));
            $(this).removeClass('preview');
            if (val <= rating) {
                $(this).addClass('fa-solid active').removeClass('fa-regular');
            } else {
                $(this).removeClass('fa-solid active').addClass('fa-regular');
            }
        });
    }

    // Helper: math iteration and calculations using $.each/each()
    function recalculateTotals(isInitialLoad) {
        var ratedCount = 0;
        var sumRating = 0;
        var favCount = 0;

        // Iterate through all gallery cards using jQuery
        $('.gallery-card').each(function() {
            var rating = parseInt($(this).attr('data-rating')) || 0;
            var isFav = $(this).find('.btn-fav').hasClass('active');

            if (rating > 0) {
                ratedCount++;
                sumRating += rating;
            }
            if (isFav) {
                favCount++;
            }
        });

        var avgRating = ratedCount > 0 ? (sumRating / ratedCount) : 0;
        updateSummaryPanel(avgRating, ratedCount, favCount, isInitialLoad);
    }

    // Helper: apply fadeIn/fadeOut animations to visual numbers in footer
    function updateSummaryPanel(avg, ratedCount, favCount, isInitialLoad) {
        var avgHtml = 'AVERAGE RATING: ' + avg.toFixed(1) + ' Stars <span class="based-text">(based on ' + ratedCount + ' items)</span>';
        var favHtml = 'TOTAL FAVORITES: ' + favCount + ' <i class="fa-solid fa-heart footer-heart ml-2"></i>';

        // Average section update with animations
        if (avgHtml !== lastAvgHtml) {
            if (isInitialLoad) {
                $avgSection.html(avgHtml);
            } else {
                $avgSection.fadeOut(150, function() {
                    $(this).html(avgHtml).fadeIn(150);
                });
            }
            lastAvgHtml = avgHtml;
        }

        // Favorites section update with animations
        if (favHtml !== lastFavHtml) {
            if (isInitialLoad) {
                $favSection.html(favHtml);
            } else {
                $favSection.fadeOut(150, function() {
                    $(this).html(favHtml).fadeIn(150);
                });
            }
            lastFavHtml = favHtml;
        }
    }
});

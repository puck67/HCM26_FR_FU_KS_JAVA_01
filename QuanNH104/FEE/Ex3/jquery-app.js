$(document).ready(function() {

    // =========================================================================
    // 1. EVENT HANDLING FOR RATING CLICKS & HOVER EFFECTS
    // =========================================================================

    // Hover effect: Hovering over a star "pre-fills" it and all stars to its left
    $('.rating-stars').on('mouseenter', '.star', function() {
        let hoveredIndex = $(this).index();
        let $stars = $(this).closest('.rating-stars').find('.star');
        
        $stars.each(function(index) {
            if (index <= hoveredIndex) {
                $(this).addClass('pre-filled');
            } else {
                $(this).removeClass('pre-filled');
            }
        });
    });

    // Hover out: Reset the pre-filled stars when mouse leaves the stars container
    $('.rating-stars').on('mouseleave', function() {
        $(this).find('.star').removeClass('pre-filled');
    });

    // Rating logic: Clicking a star selects the rating
    $('.rating-stars').on('click', '.star', function() {
        let clickedIndex = $(this).index();
        let $stars = $(this).closest('.rating-stars').find('.star');
        
        // Fill all stars up to the clicked star, empty the rest
        $stars.each(function(index) {
            if (index <= clickedIndex) {
                $(this).addClass('active');
            } else {
                $(this).removeClass('active');
            }
        });

        // Recalculate average rating and animate changes
        updateAverageRating();
    });

    // =========================================================================
    // 2. EVENT HANDLING FOR FAVORITE TOGGLES
    // =========================================================================

    // Intersect both favorite buttons (the top right text button and bottom heart icon)
    $('.btn-add-favorite, .btn-heart').on('click', function() {
        let $card = $(this).closest('.gallery-card');
        let $btnHeart = $card.find('.btn-heart');
        let $btnAddFav = $card.find('.btn-add-favorite');

        // Toggle active class on both buttons in this card to synchronize them
        $btnHeart.toggleClass('active');
        $btnAddFav.toggleClass('active');

        // Update display contents based on state
        if ($btnHeart.hasClass('active')) {
            $btnHeart.text('♥');
            $btnAddFav.text('Remove from Favorites');
        } else {
            $btnHeart.text('♡');
            $btnAddFav.text('Add to Favorites');
        }

        // Recalculate total favorites count and animate changes
        updateTotalFavorites();
    });

    // =========================================================================
    // 3. STATISTICAL CALCULATIONS & FOOTER UPDATES WITH ANIMATIONS
    // =========================================================================

    /**
     * Calculates the average rating of all cards and updates the footer using
     * jQuery selectors and iteration methods.
     */
    function updateAverageRating(skipAnimation) {
        let totalRating = 0;
        let cardCount = 0;

        // Iterate through each gallery card using jQuery .each()
        $('.gallery-card').each(function() {
            // Count active stars in current card
            let rating = $(this).find('.rating-stars .star.active').length;
            totalRating += rating;
            cardCount++;
        });

        let avgRating = cardCount > 0 ? (totalRating / cardCount) : 0;
        let avgStr = avgRating.toFixed(1);

        let $summaryBox = $('.average-rating');
        let $strong = $summaryBox.find('.summary-text strong');
        let $span = $summaryBox.find('.summary-text span');

        let newText = `AVERAGE RATING: ${avgStr} Stars`;
        let newSpan = `(based on ${cardCount} items)`;

        if (skipAnimation) {
            $strong.text(newText);
            $span.text(newSpan);
        } else {
            let currentText = $strong.text();
            if (currentText !== newText) {
                // Visual feedback using jQuery animation: fadeOut -> update -> fadeIn
                $summaryBox.find('.summary-text').fadeOut(150, function() {
                    $strong.text(newText);
                    $span.text(newSpan);
                    $(this).fadeIn(150);
                });
            }
        }
    }

    /**
     * Calculates the total number of favorited items and updates the footer
     * using jQuery selectors, .each() iteration, and animate effects.
     */
    function updateTotalFavorites(skipAnimation) {
        let favoriteCount = 0;

        // Iterate through all gallery cards using .each()
        $('.gallery-card').each(function() {
            if ($(this).find('.btn-heart').hasClass('active')) {
                favoriteCount++;
            }
        });

        let $summaryBox = $('.total-favorites');
        let $strong = $summaryBox.find('.summary-text strong');
        let newText = `TOTAL FAVORITES: ${favoriteCount}`;

        if (skipAnimation) {
            $strong.text(newText);
        } else {
            let currentText = $strong.text();
            if (currentText !== newText) {
                // Visual feedback using jQuery animate: pulse size from 15px to 18px and fade opacity
                $strong.animate({
                    opacity: 0.3,
                    fontSize: '18px'
                }, 120, function() {
                    $(this).text(newText);
                }).animate({
                    opacity: 1,
                    fontSize: '15px'
                }, 120);
            }
        }
    }

    // =========================================================================
    // 4. INITIALIZATION
    // =========================================================================

    // Synchronize initial states of buttons (ensures text matches active classes if any are preset)
    $('.gallery-card').each(function() {
        let $btnHeart = $(this).find('.btn-heart');
        let $btnAddFav = $(this).find('.btn-add-favorite');

        if ($btnHeart.hasClass('active') || $btnAddFav.hasClass('active')) {
            $btnHeart.addClass('active').text('♥');
            $btnAddFav.addClass('active').text('Remove from Favorites');
        } else {
            $btnHeart.removeClass('active').text('♡');
            $btnAddFav.removeClass('active').text('Add to Favorites');
        }
    });

    // Run initial calculations on page load (skip animation on startup)
    updateAverageRating(true);
    updateTotalFavorites(true);
});

$(document).ready(function() {
    // Keep track of the current values to avoid animating when there's no change
    let currentAvg = '';
    let currentCount = -1;
    let currentFavs = -1;

    // Function to calculate totals and update the footer summary with jQuery animations
    function updateSummary() {
        let totalRating = 0;
        let ratedItemsCount = 0;
        let totalFavorites = 0;

        // Use jQuery selector and iteration (.each) to calculate grand totals
        $('.card').each(function() {
            const $card = $(this);
            
            // Calculate rating contribution
            const $starsContainer = $card.find('.stars-container');
            const rating = parseInt($starsContainer.attr('data-rating')) || 0;
            if (rating > 0) {
                totalRating += rating;
                ratedItemsCount++;
            }

            // Calculate favorites contribution
            const $favBtn = $card.find('.fav-btn');
            if ($favBtn.hasClass('active')) {
                totalFavorites++;
            }
        });

        // Compute average
        const average = ratedItemsCount > 0 ? (totalRating / ratedItemsCount).toFixed(1) : '0.0';
        const avgText = average + ' Stars';
        const countText = `(based on ${ratedItemsCount} items)`;

        // Update Average Rating with fade animation if value changed
        if (currentAvg !== avgText || currentCount !== ratedItemsCount) {
            currentAvg = avgText;
            currentCount = ratedItemsCount;

            $('#avg-rating').fadeOut(200, function() {
                $(this).text(avgText).fadeIn(200);
            });
            $('#rated-count').fadeOut(200, function() {
                $(this).text(countText).fadeIn(200);
            });
        }

        // Update Total Favorites with fade animation if value changed
        if (currentFavs !== totalFavorites) {
            currentFavs = totalFavorites;

            $('#total-favs').fadeOut(200, function() {
                $(this).text(totalFavorites).fadeIn(200);
            });
        }
    }

    // --- EVENT HANDLING USING JQUERY ---

    // 1. Rating Clicks
    $('.stars-container').on('click', '.star-btn', function() {
        const $btn = $(this);
        const $container = $btn.parent();
        const clickedValue = parseInt($btn.attr('data-value'));

        // Save rating to container attribute
        $container.attr('data-rating', clickedValue);

        // Update filled classes
        $container.find('.star-btn').each(function() {
            const starValue = parseInt($(this).attr('data-value'));
            if (starValue <= clickedValue) {
                $(this).addClass('filled');
            } else {
                $(this).removeClass('filled');
            }
        });

        // Trigger dynamic footer calculation
        updateSummary();
    });

    // 2. Rating Hover Pre-fill Effects (mouseenter and mouseleave)
    $('.stars-container').on('mouseenter', '.star-btn', function() {
        const $btn = $(this);
        const $container = $btn.parent();
        const hoverValue = parseInt($btn.attr('data-value'));

        // Show potential rating on hover (add hovered, remove filled temporarily)
        $container.find('.star-btn').each(function() {
            const starValue = parseInt($(this).attr('data-value'));
            if (starValue <= hoverValue) {
                $(this).addClass('hovered').removeClass('filled');
            } else {
                $(this).removeClass('hovered').removeClass('filled');
            }
        });
    });

    // Restore original rating when mouse leaves the star container
    $('.stars-container').on('mouseleave', function() {
        const $container = $(this);
        const savedRating = parseInt($container.attr('data-rating')) || 0;

        // Restore visual state to match saved data-rating attribute
        $container.find('.star-btn').each(function() {
            const starValue = parseInt($(this).attr('data-value'));
            $(this).removeClass('hovered');
            if (starValue <= savedRating) {
                $(this).addClass('filled');
            } else {
                $(this).removeClass('filled');
            }
        });
    });

    // 3. Favorite Toggle
    $('.card').on('click', '.fav-btn', function() {
        const $favBtn = $(this);
        const $tooltip = $favBtn.siblings('.tooltip');
        
        // Toggle favorited state
        const isFav = $favBtn.toggleClass('active').hasClass('active');

        // Update tooltip and accessibility labels
        if (isFav) {
            $tooltip.text('Remove from Favorites');
            $favBtn.attr('aria-label', 'Remove from Favorites');
        } else {
            $tooltip.text('Add to Favorites');
            $favBtn.attr('aria-label', 'Add to Favorites');
        }

        // Trigger dynamic footer calculation
        updateSummary();
    });

    // Initialize totals on load without fading animations (sets starting state)
    let initialTotalRating = 0;
    let initialRatedCount = 0;
    let initialFavCount = 0;

    $('.card').each(function() {
        const rating = parseInt($(this).find('.stars-container').attr('data-rating')) || 0;
        if (rating > 0) {
            initialTotalRating += rating;
            initialRatedCount++;
        }
        if ($(this).find('.fav-btn').hasClass('active')) {
            initialFavCount++;
        }
    });

    const initialAvg = initialRatedCount > 0 ? (initialTotalRating / initialRatedCount).toFixed(1) : '0.0';
    
    // Set current states so initial load does not trigger transition animations
    currentAvg = initialAvg + ' Stars';
    currentCount = initialRatedCount;
    currentFavs = initialFavCount;

    $('#avg-rating').text(currentAvg);
    $('#rated-count').text(`(based on ${initialRatedCount} items)`);
    $('#total-favs').text(initialFavCount);
});

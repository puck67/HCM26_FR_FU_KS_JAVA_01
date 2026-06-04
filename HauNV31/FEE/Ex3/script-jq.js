// Problem 03 - jQuery Implementation
$(document).ready(function() {

    function updateFooter() {
        let totalStars = 0;
        let ratedCount = 0;
        let favCount = 0;

        // Use jQuery .each() to iterate and calculate grand totals
        $('.card').each(function() {
            let rating = parseInt($(this).data('rating') || 0);
            if (rating > 0) {
                totalStars += rating;
                ratedCount++;
            }

            if ($(this).find('.fav-btn').hasClass('active')) {
                favCount++;
            }
        });

        let avg = ratedCount > 0 ? (totalStars / ratedCount) : 0;
        
        // jQuery Animation: fadeOut, update text, fadeIn to provide visual feedback
        $('#avgRatingValue').fadeOut(150, function() {
            $(this).text(avg.toFixed(1)).fadeIn(150);
        });

        $('#totalRatedItems').fadeOut(150, function() {
            $(this).text(ratedCount).fadeIn(150);
        });

        $('#totalFavoritesValue').fadeOut(150, function() {
            $(this).text(favCount).fadeIn(150);
        });
    }

    // Rating Event Handling & Hover Effects
    $('.stars').on('mouseenter', '.star', function() {
        // Pre-fill stars on hover using .addClass() and .removeClass()
        let val = parseInt($(this).data('val'));
        let siblings = $(this).parent().find('.star');
        
        siblings.each(function() {
            if (parseInt($(this).data('val')) <= val) {
                $(this).addClass('hover-filled');
            } else {
                $(this).removeClass('hover-filled');
            }
        });
    }).on('mouseleave', '.star', function() {
        // Remove hover effect
        $(this).parent().find('.star').removeClass('hover-filled');
    }).on('click', '.star', function() {
        let val = parseInt($(this).data('val'));
        let card = $(this).closest('.card');
        let siblings = $(this).parent().find('.star');
        
        // Save rating to data attribute
        card.data('rating', val);

        // Fill clicked stars
        siblings.each(function() {
            if (parseInt($(this).data('val')) <= val) {
                $(this).addClass('filled');
            } else {
                $(this).removeClass('filled');
            }
        });

        updateFooter();
    });

    // Favorite Toggle Event Handling
    $('.card').on('click', '.fav-btn', function() {
        $(this).toggleClass('active');
        
        // Change icon based on state
        if ($(this).hasClass('active')) {
            $(this).text('♥');
        } else {
            $(this).text('♡');
        }

        updateFooter();
    });

});

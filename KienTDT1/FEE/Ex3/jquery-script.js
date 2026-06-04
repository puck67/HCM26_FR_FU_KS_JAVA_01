$(document).ready(function() {

    // --- 1. Hover Effect & Preview Engine ---
    $('.stars i').on('mouseenter', function() {
        let currentStarVal = $(this).data('value');
        let $starsInCard = $(this).closest('.stars').find('i');

        // Light preview shade on hover tracking leftwards
        $starsInCard.each(function() {
            if ($(this).data('value') <= currentStarVal) {
                $(this).addClass('fa-solid').removeClass('fa-regular');
            } else {
                $(this).removeClass('fa-solid').addClass('fa-regular');
            }
        });
    }).on('mouseleave', function() {
        let $card = $(this).closest('.gallery-card');
        let currentSavedRating = $card.data('rating') || 0;
        let $starsInCard = $(this).closest('.stars').find('i');

        // Restore permanent rating layout when cursor leaves
        $starsInCard.each(function() {
            if ($(this).data('value') <= currentSavedRating) {
                $(this).addClass('fa-solid checked').removeClass('fa-regular');
            } else {
                $(this).removeClass('fa-solid checked').addClass('fa-regular');
            }
        });
    });

    // --- 2. Event Handling: Star Clicks ---
    $('.stars i').on('click', function() {
        let clickedValue = $(this).data('value');
        let $card = $(this).closest('.gallery-card');
        
        // Commit rating state to card element data instance
        $card.data('rating', clickedValue);
        
        // Run Math/UI Calculation refresh
        updateSummaryFooterWithAnimation();
    });

    // --- 3. Event Handling: Favorite Toggles ---
    $('.fav-btn').on('click', function() {
        let $card = $(this).closest('.gallery-card');
        let $heartIcon = $(this).find('i');
        
        $card.toggleClass('is-favorite');

        if ($card.hasClass('is-favorite')) {
            $heartIcon.attr('class', 'fa-solid fa-heart');
            $(this).css('color', '#e74c3c');
        } else {
            $heartIcon.attr('class', 'fa-regular fa-heart');
            $(this).css('color', '#555');
        }

        updateSummaryFooterWithAnimation();
    });

    // --- 4. Query Selectors, Loops, & Animations ---
    function updateSummaryFooterWithAnimation() {
        let totalRating = 0;
        let ratedCardsCount = 0;
        let totalFavorites = 0;

        // Iterate collections using jQuery .each()
        $('.gallery-card').each(function() {
            let rating = $(this).data('rating');
            if (rating) {
                totalRating += parseInt(rating);
                ratedCardsCount++;
            }

            if ($(this).hasClass('is-favorite')) {
                totalFavorites++;
            }
        });

        let average = ratedCardsCount > 0 ? (totalRating / ratedCardsCount).toFixed(1) : "0.0";

        // Target Summary nodes
        let $avgContainer = $('.avg-rating strong');
        let $favContainer = $('.total-favorites strong');

        // Visual feedback animations using fade sequences on value shifts
        if($avgContainer.text() !== `AVERAGE RATING: ${average} Stars`) {
            $avgContainer.fadeOut(150, function() {
                $(this).text(`AVERAGE RATING: ${average} Stars`).fadeIn(150);
            });
        }

        if($favContainer.text() !== `TOTAL FAVORITES: ${totalFavorites}`) {
            // Alternative layout animation shift utilizing jQuery opacity manipulation
            $favContainer.animate({ opacity: 0.2 }, 100, function() {
                $(this).text(`TOTAL FAVORITES: ${totalFavorites}`).animate({ opacity: 1 }, 100);
            });
        }
    }

    // Set layout defaults initially
    updateSummaryFooterWithAnimation();
});
$(document).ready(function() {
  const $gallery = $('#gallery');
  
  /**
   * Updates the average rating and total items count in the footer.
   * Calculates dynamic average using jQuery `.each()` iteration.
   * Adds fadeIn/fadeOut animation when values change.
   * @param {boolean} animate - Whether to animate the update
   */
  function updateAverageRating(animate = false) {
    let totalRating = 0;
    let ratedCount = 0;
    const $cards = $('.card');

    $cards.each(function() {
      const rating = parseFloat($(this).attr('data-rating'));
      if (!isNaN(rating) && rating > 0) {
        totalRating += rating;
        ratedCount++;
      }
    });

    const average = ratedCount > 0 ? (totalRating / ratedCount).toFixed(1) : '0.0';

    const $avgVal = $('#average-rating-val');
    const $totalVal = $('#total-items-val');

    if (animate) {
      $avgVal.fadeOut(150, function() {
        $(this).text(average).fadeIn(150);
      });
      $totalVal.fadeOut(150, function() {
        $(this).text($cards.length).fadeIn(150);
      });
    } else {
      $avgVal.text(average);
      $totalVal.text($cards.length);
    }
  }

  /**
   * Updates the total favorites count in the footer.
   * Adds fadeIn/fadeOut animation when value changes.
   * @param {boolean} animate - Whether to animate the update
   */
  function updateFavoritesCount(animate = false) {
    const favoriteCount = $('.card__favorite-icon--active').length;
    const $favCountElement = $('#total-favorites-count');

    if (animate) {
      $favCountElement.fadeOut(150, function() {
        $(this).text(favoriteCount).fadeIn(150);
      });
    } else {
      $favCountElement.text(favoriteCount);
    }
  }

  /**
   * Renders the filled state of stars based on the parent card's data-rating.
   * @param {jQuery} $card - The card element to update stars for
   */
  function renderCardStars($card) {
    const rating = parseInt($card.attr('data-rating')) || 0;
    $card.find('.card__rating-star').each(function() {
      const starValue = parseInt($(this).attr('data-value'));
      if (starValue <= rating) {
        $(this).addClass('card__rating-star--filled');
      } else {
        $(this).removeClass('card__rating-star--filled');
      }
    });
  }

  // --- Initial Render ---
  $('.card').each(function() {
    renderCardStars($(this));
  });
  updateAverageRating(false);
  updateFavoritesCount(false);

  // --- Event Delegation on Gallery ---

  // 1. Star Click Event
  $gallery.on('click', '.card__rating-star', function() {
    const $star = $(this);
    const $card = $star.closest('.card');
    const selectedValue = $star.attr('data-value');

    // Update rating value on card
    $card.attr('data-rating', selectedValue);
    
    // Rerender stars
    renderCardStars($card);

    // Recalculate average with animation
    updateAverageRating(true);
  });

  // 2. Star Hover Event (Mouseenter and Mouseleave)
  $gallery.on('mouseenter', '.card__rating-star', function() {
    const $star = $(this);
    const $card = $star.closest('.card');
    const hoverValue = parseInt($star.attr('data-value'));

    // Temporary hover pre-fill effect
    $card.find('.card__rating-star').each(function() {
      const starValue = parseInt($(this).attr('data-value'));
      if (starValue <= hoverValue) {
        $(this).addClass('card__rating-star--hovered').removeClass('card__rating-star--filled');
      } else {
        $(this).removeClass('card__rating-star--hovered').removeClass('card__rating-star--filled');
      }
    });
  });

  $gallery.on('mouseleave', '.card__rating-star', function() {
    const $star = $(this);
    const $card = $star.closest('.card');

    // Remove hover styles and restore active stars
    $card.find('.card__rating-star').removeClass('card__rating-star--hovered');
    renderCardStars($card);
  });

  // 3. Favorite Toggle Button Click
  $gallery.on('click', '.card__favorite-btn', function() {
    const $btn = $(this);
    const $icon = $btn.find('.card__favorite-icon');

    // Toggle active state
    $icon.toggleClass('card__favorite-icon--active');

    // Recalculate favorites count with animation
    updateFavoritesCount(true);
  });
});

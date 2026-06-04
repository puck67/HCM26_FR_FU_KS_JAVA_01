$(function() {
  const $cards = $('.gallery-card');
  const $avgRatingValue = $('#avg-rating-value');
  const $avgRatingCount = $('#avg-rating-count');
  const $totalFavValue = $('#total-fav-value');
  const $toastContainer = $('#toast-container');

  function showError(title, message) {
    const $toast = $('<div class="toast">' +
      '<div class="toast-icon">' +
        '<svg viewBox="0 0 24 24"><path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z"/></svg>' +
      '</div>' +
      '<div class="toast-content">' +
        '<div class="toast-title"></div>' +
        '<div class="toast-message"></div>' +
      '</div>' +
      '<button class="toast-close">' +
        '<svg viewBox="0 0 24 24"><path d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/></svg>' +
      '</button>' +
    '</div>');

    $toast.find('.toast-title').text(title);
    $toast.find('.toast-message').text(message);
    
    $toastContainer.append($toast);
    
    setTimeout(() => {
      $toast.addClass('show');
    }, 50);

    $toast.find('.toast-close').on('click', function() {
      $toast.removeClass('show');
      setTimeout(() => {
        $toast.remove();
      }, 300);
    });

    setTimeout(() => {
      if ($toast.parent().length) {
        $toast.removeClass('show');
        setTimeout(() => {
          $toast.remove();
        }, 300);
      }
    }, 4000);
  }

  function validateCardData($card) {
    const id = $card.attr('data-id');
    const ratingStr = $card.attr('data-rating');
    const favoriteStr = $card.attr('data-favorite');

    if (!id) {
      showError('Validation Error', 'A gallery card is missing a data-id attribute.');
      return false;
    }

    const rating = parseFloat(ratingStr);
    if (isNaN(rating) || rating < 0 || rating > 5) {
      showError('Invalid Data', 'Card ' + id + ' has an invalid rating: "' + ratingStr + '". Defaulting to 0.');
      $card.attr('data-rating', '0');
    }

    if (favoriteStr !== 'true' && favoriteStr !== 'false') {
      showError('Invalid Data', 'Card ' + id + ' has an invalid favorite value: "' + favoriteStr + '". Defaulting to false.');
      $card.attr('data-favorite', 'false');
    }

    return true;
  }

  function renderCardState($card) {
    const rating = parseInt($card.attr('data-rating') || '0', 10);
    const isFavorite = $card.attr('data-favorite') === 'true';

    $card.find('.star-btn').each(function() {
      const val = parseInt($(this).attr('data-value'), 10);
      if (val <= rating) {
        $(this).addClass('filled');
      } else {
        $(this).removeClass('filled');
      }
    });

    const $favBtn = $card.find('.favorite-btn');
    if (isFavorite) {
      $favBtn.addClass('active');
    } else {
      $favBtn.removeClass('active');
    }
  }

  function animateValueChange($el, newVal) {
    const oldVal = $el.text();
    if (oldVal !== newVal.toString()) {
      $el.fadeOut(150, function() {
        $el.text(newVal).fadeIn(150);
      });
    } else {
      $el.text(newVal);
    }
  }

  function updateFooter() {
    let totalItems = 0;
    let sumRatings = 0;
    let totalFavorites = 0;

    const $currentCards = $('.gallery-card');

    if ($currentCards.length === 0) {
      showError('System Warning', 'No gallery cards found. Calculations cannot be performed.');
      animateValueChange($avgRatingValue, '0.0');
      $avgRatingCount.text('(based on 0 items)');
      animateValueChange($totalFavValue, '0');
      return;
    }

    $currentCards.each(function() {
      const $card = $(this);
      const id = $card.attr('data-id');
      const rating = parseFloat($card.attr('data-rating') || '0');
      const isFav = $card.attr('data-favorite') === 'true';

      if (isNaN(rating) || rating < 0 || rating > 5) {
        showError('Calculation Error', 'Card ' + (id || 'unknown') + ' has invalid rating data during calculation.');
        return;
      }

      sumRatings += rating;
      if (isFav) {
        totalFavorites += 1;
      }
      totalItems += 1;
    });

    const avg = totalItems > 0 ? (sumRatings / totalItems).toFixed(1) : '0.0';

    animateValueChange($avgRatingValue, avg);
    $avgRatingCount.text('(based on ' + totalItems + ' items)');
    animateValueChange($totalFavValue, totalFavorites);
  }

  $cards.each(function() {
    const $card = $(this);
    validateCardData($card);
    renderCardState($card);
  });

  updateFooter();

  $('.gallery-grid').on('click', '.star-btn', function(e) {
    e.stopPropagation();
    const $star = $(this);
    const $card = $star.closest('.gallery-card');
    const valueStr = $star.attr('data-value');
    const value = parseInt(valueStr, 10);

    if (isNaN(value) || value < 1 || value > 5) {
      showError('Interaction Error', 'Attempted to set an invalid star rating.');
      return;
    }

    $card.attr('data-rating', valueStr);
    renderCardState($card);
    updateFooter();
  });

  $('.gallery-grid').on('click', '.favorite-btn', function(e) {
    e.stopPropagation();
    const $favBtn = $(this);
    const $card = $favBtn.closest('.gallery-card');
    const currentFav = $card.attr('data-favorite');
    const nextFav = currentFav === 'true' ? 'false' : 'true';

    $card.attr('data-favorite', nextFav);
    renderCardState($card);
    updateFooter();
  });

  $('.gallery-grid').on('mouseenter', '.star-btn', function() {
    const currentVal = parseInt($(this).attr('data-value'), 10);
    const $stars = $(this).siblings().addBack();
    $stars.each(function() {
      const val = parseInt($(this).attr('data-value'), 10);
      if (val <= currentVal) {
        $(this).addClass('hover-filled');
      } else {
        $(this).removeClass('hover-filled');
      }
    });
  });

  $('.gallery-grid').on('mouseleave', '.rating-stars', function() {
    $(this).find('.star-btn').removeClass('hover-filled');
  });
});

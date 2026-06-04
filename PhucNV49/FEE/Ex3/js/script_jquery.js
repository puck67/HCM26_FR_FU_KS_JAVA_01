$(document).ready(function() {
  const $cards = $('.image-card');
  const $avgRating = $('#avg-rating');
  const $ratedItemsCount = $('#rated-items-count');
  const $totalFavs = $('#total-favs');

  // Calculates stats and triggers fade animations on change
  function updateSummary(animate = true) {
    let totalStars = 0;
    let ratedItemsCount = 0;
    let totalFavorites = 0;

    // Iteration method: jQuery each()
    $('.image-card').each(function() {
      const rating = parseInt($(this).attr('data-rating')) || 0;
      const isFavorite = $(this).attr('data-favorite') === 'true';

      if (rating > 0) {
        totalStars += rating;
        ratedItemsCount++;
      }

      if (isFavorite) {
        totalFavorites++;
      }
    });

    const averageRating = ratedItemsCount > 0 ? (totalStars / ratedItemsCount).toFixed(1) : '0.0';

    if (animate) {
      // Average Rating Text Animation
      if ($avgRating.text() !== averageRating) {
        $avgRating.fadeOut(120, function() {
          $(this).text(averageRating).fadeIn(120);
        });
      }

      // Rated Items Count Animation
      if ($ratedItemsCount.text() !== ratedItemsCount.toString()) {
        $ratedItemsCount.fadeOut(120, function() {
          $(this).text(ratedItemsCount).fadeIn(120);
        });
      }

      // Total Favorites Text Animation
      if ($totalFavs.text() !== totalFavorites.toString()) {
        $totalFavs.fadeOut(120, function() {
          $(this).text(totalFavorites).fadeIn(120);
        });
      }
    } else {
      // Immediate update on page load without animation delay
      $avgRating.text(averageRating);
      $ratedItemsCount.text(ratedItemsCount);
      $totalFavs.text(totalFavorites);
    }
  }

  // Star Icon Click Handler
  $('.star-icon').on('click', function() {
    const clickedVal = parseInt($(this).data('value')) || 0;
    const $card = $(this).closest('.image-card');
    const $stars = $card.find('.star-icon');

    // Update data attribute using jQuery
    $card.attr('data-rating', clickedVal.toString());

    // Update visual states
    $stars.each(function() {
      const sVal = parseInt($(this).data('value')) || 0;
      if (sVal <= clickedVal) {
        $(this).addClass('active').removeClass('fa-regular').addClass('fa-solid');
      } else {
        $(this).removeClass('active').removeClass('fa-solid').addClass('fa-regular');
      }
    });

    updateSummary();
  });

  // Star Icon Hover pre-fill visual effects
  $('.star-icon').on('mouseenter', function() {
    const hoverVal = parseInt($(this).data('value')) || 0;
    const $stars = $(this).closest('.star-rating').find('.star-icon');

    $stars.each(function() {
      const sVal = parseInt($(this).data('value')) || 0;
      if (sVal <= hoverVal) {
        $(this).addClass('hover-fill');
        if ($(this).hasClass('fa-regular')) {
          $(this).removeClass('fa-regular').addClass('fa-solid');
        }
      }
    });
  });

  // Star Icon Hover Leave
  $('.star-icon').on('mouseleave', function() {
    const $card = $(this).closest('.image-card');
    const cardRating = parseInt($card.attr('data-rating')) || 0;
    const $stars = $(this).closest('.star-rating').find('.star-icon');

    $stars.each(function() {
      const sVal = parseInt($(this).data('value')) || 0;
      $(this).removeClass('hover-fill');
      
      // Reset back to card's exact rating state
      if (sVal <= cardRating) {
        $(this).addClass('active').removeClass('fa-regular').addClass('fa-solid');
      } else {
        $(this).removeClass('active').removeClass('fa-solid').addClass('fa-regular');
      }
    });
  });

  // Favorite Button Toggle Click Handler
  $('.btn-favorite').on('click', function() {
    const $card = $(this).closest('.image-card');
    const $favIcon = $(this).find('i');
    const isFavorite = $card.attr('data-favorite') === 'true';
    const newFavState = !isFavorite;

    // Toggle card attribute
    $card.attr('data-favorite', newFavState.toString());

    // Toggle UI styles
    if (newFavState) {
      $(this).addClass('active');
      $favIcon.removeClass('fa-regular').addClass('fa-solid');
    } else {
      $(this).removeClass('active');
      $favIcon.removeClass('fa-solid').addClass('fa-regular');
    }

    updateSummary();
  });

  // Initialize summary on load without animating
  updateSummary(false);
});

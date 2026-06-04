$(document).ready(function () {
  function updateTotals() {
    let totalStars = 0;
    let ratedCount = 0;
    let favoritesCount = 0;

    $('.card').each(function () {
      const rating = parseInt($(this).attr('data-rating') || '0');
      if (rating > 0) {
        totalStars += rating;
        ratedCount++;
      }

      const heart = $(this).find('.heart');
      if (heart.hasClass('favorited')) {
        favoritesCount++;
      }
    });

    const average = ratedCount > 0 ? (totalStars / ratedCount).toFixed(1) : '0.0';
    const newAvgVal = average + ' Stars';
    const newAvgCount = '(based on ' + ratedCount + ' items)';
    const newFavVal = String(favoritesCount);

    const avgValEl = $('#average-rating-val');
    const avgCountEl = $('#average-count-val');
    const favValEl = $('#total-favorites-val');

    if (avgValEl.text() !== newAvgVal) {
      avgValEl.fadeOut(200, function () {
        $(this).text(newAvgVal).fadeIn(200);
      });
    }

    if (avgCountEl.text() !== newAvgCount) {
      avgCountEl.fadeOut(200, function () {
        $(this).text(newAvgCount).fadeIn(200);
      });
    }

    if (favValEl.text() !== newFavVal) {
      favValEl.fadeOut(200, function () {
        $(this).text(newFavVal).fadeIn(200);
      });
    }
  }

  $('.star').on('click', function () {
    const card = $(this).closest('.card');
    const clickedValue = parseInt($(this).attr('data-value'));
    card.attr('data-rating', clickedValue);

    card.find('.star').each(function () {
      const val = parseInt($(this).attr('data-value'));
      if (val <= clickedValue) {
        // doi mau
        $(this).removeClass('empty');
        // doi mau
        $(this).text('★');
      } else {
        // doi mau
        $(this).addClass('empty');
        // doi mau
        $(this).text('☆');
      }
    });

    updateTotals();
  });

  $('.star').on('mouseenter', function () {
    const card = $(this).closest('.card');
    const hoveredValue = parseInt($(this).attr('data-value'));

    card.find('.star').each(function () {
      const val = parseInt($(this).attr('data-value'));
      if (val <= hoveredValue) {
        // doi mau
        $(this).removeClass('empty');
        // doi mau
        $(this).text('★');
      } else {
        // doi mau
        $(this).addClass('empty');
        // doi mau
        $(this).text('☆');
      }
    });
  });

  $('.stars').on('mouseleave', function () {
    const card = $(this).closest('.card');
    const rating = parseInt(card.attr('data-rating') || '0');

    card.find('.star').each(function () {
      const val = parseInt($(this).attr('data-value'));
      if (val <= rating) {
        // doi mau
        $(this).removeClass('empty');
        // doi mau
        $(this).text('★');
      } else {
        // doi mau
        $(this).addClass('empty');
        // doi mau
        $(this).text('☆');
      }
    });
  });

  $('.heart-box').on('click', function () {
    const heart = $(this).find('.heart');
    // doi mau
    heart.toggleClass('favorited');
    if (heart.hasClass('favorited')) {
      // doi mau
      heart.text('♥');
    } else {
      // doi mau
      heart.text('♡');
    }
    updateTotals();
  });

  updateTotals();
});

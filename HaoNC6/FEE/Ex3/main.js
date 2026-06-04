
var ratings = {};
var favorites = {};

(function initData() {
  document.querySelectorAll('.card').forEach(function (card) {
    var id = card.getAttribute('data-id');
    ratings[id] = 0;
    favorites[id] = false;
  });
})();

function renderStars(cardId, ratingValue) {
  var container = document.querySelector('.stars[data-id="' + cardId + '"]');
  if (!container) return;
  container.querySelectorAll('.star').forEach(function (star) {
    var v = parseInt(star.getAttribute('data-value'), 10);
    star.classList.toggle('filled', v <= ratingValue);
    star.classList.remove('hovered');
  });
}

function renderHeart(cardId) {
  var btn = document.querySelector('.btn-heart[data-id="' + cardId + '"]');
  if (!btn) return;
  if (favorites[cardId]) {
    btn.classList.add('active');
    btn.textContent = '♥';
  } else {
    btn.classList.remove('active');
    btn.textContent = '♡';
  }
}


function recalculateStats() {
  var ratedIds = Object.keys(ratings).filter(function (id) { return ratings[id] > 0; });
  var count = ratedIds.length;
  var sum = 0;
  ratedIds.forEach(function (id) { sum += ratings[id]; });
  var avg = count > 0 ? sum / count : 0;

  document.getElementById('avgRating').textContent = avg.toFixed(1);
  document.getElementById('ratedCount').textContent = '(based on ' + count + ' items)';

  var favCount = Object.keys(favorites).filter(function (id) { return favorites[id]; }).length;
  document.getElementById('totalFavorites').textContent = favCount;
}

document.querySelectorAll('.stars').forEach(function (container) {
  var cardId = container.getAttribute('data-id');

  container.querySelectorAll('.star').forEach(function (star) {

    star.addEventListener('click', function () {
      var val = parseInt(this.getAttribute('data-value'), 10);
      ratings[cardId] = val;
      renderStars(cardId, val);
      recalculateStats();
    });
  });
});

document.querySelectorAll('.btn-heart').forEach(function (btn) {
  btn.addEventListener('click', function () {
    var cardId = this.getAttribute('data-id');
    favorites[cardId] = !favorites[cardId];
    renderHeart(cardId);
    recalculateStats();
  });
});

document.querySelectorAll('.btn-favorite').forEach(function (btn) {
  btn.addEventListener('click', function () {
    var cardId = this.getAttribute('data-id');
    favorites[cardId] = true;
    renderHeart(cardId);
    recalculateStats();
  });
});

recalculateStats();



$(function () {
  $('.btn-heart, .btn-favorite, .star').each(function () {
    var clone = this.cloneNode(true);
    this.parentNode.replaceChild(clone, this);
  });


  function jqUpdateFooter() {
    var count = 0;
    var sum = 0;
    $.each(ratings, function (id, val) {
      if (val > 0) { count++; sum += val; }
    });
    var avg = count > 0 ? sum / count : 0;
    var favCount = 0;
    $.each(favorites, function (id, val) { if (val) favCount++; });

    $('#avgRating').fadeOut(150, function () {
      $(this).text(avg.toFixed(1)).fadeIn(200);
    });
    $('#ratedCount').fadeOut(120, function () {
      $(this).text('(based on ' + count + ' items)').fadeIn(180);
    });

    $('#totalFavorites').fadeOut(150, function () {
      $(this).text(favCount).fadeIn(200);
    });

    $('#summaryFooter')
      .stop(true)
      .css('border-color', '#f5a623')
      .animate({ opacity: 1 }, {
        duration: 400,
        complete: function () {
          $(this).css('border-color', '#1a2a3a');
        }
      });
  }

  $('.stars').each(function () {
    var $container = $(this);
    var cardId = String($container.data('id'));
    var $stars = $container.find('.star');

    $stars.on('mouseenter', function () {
      var hoverVal = parseInt($(this).data('value'), 10);
      $stars.each(function () {
        if (parseInt($(this).data('value'), 10) <= hoverVal) {
          $(this).addClass('hovered');
        } else {
          $(this).removeClass('hovered');
        }
      });
    });

    $container.on('mouseleave', function () {
      $stars.removeClass('hovered');
      var current = ratings[cardId] || 0;
      $stars.each(function () {
        var v = parseInt($(this).data('value'), 10);
        $(this).toggleClass('filled', v <= current);
      });
    });

    $stars.on('click', function () {
      var val = parseInt($(this).data('value'), 10);
      ratings[cardId] = val;
      renderStars(cardId, val);
      jqUpdateFooter();
    });
  });


  $(document).off('click', '.btn-heart').on('click', '.btn-heart', function () {
    var cardId = String($(this).data('id'));
    favorites[cardId] = !favorites[cardId];
    renderHeart(cardId);
    jqUpdateFooter();
  });

  $(document).off('click', '.btn-favorite').on('click', '.btn-favorite', function () {
    var cardId = String($(this).data('id'));
    favorites[cardId] = true;
    renderHeart(cardId);
    jqUpdateFooter();
  });

  jqUpdateFooter();

});

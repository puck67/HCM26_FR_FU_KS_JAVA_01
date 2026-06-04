/**
 * Problem 03 — jQuery Refactored
 * Dynamic Image Gallery with 5-Star Rating, Hover Effects & Animated Footer
 *
 * Implements the same features as Problem 02 but uses:
 *   - jQuery selectors: $()
 *   - Event handling: .on('click', ...) and .on('mouseenter/mouseleave', ...)
 *   - DOM manipulation: .addClass(), .removeClass(), .html(), .text()
 *   - Iteration: .each()
 *   - Animations: .fadeOut() / .fadeIn() for footer value transitions
 */

'use strict';

/* =========================================
   DATA — 10 image card definitions
   Mutable state stored on each object.
   ========================================= */
const IMAGES = [
  { id: 1,  gradient: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)', label: 'Sunset Pastel', rating: 0, isFavorite: false },
  { id: 2,  gradient: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', label: 'Golden Hour',   rating: 0, isFavorite: false },
  { id: 3,  gradient: 'linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)', label: 'Ocean Depth',   rating: 0, isFavorite: false },
  { id: 4,  gradient: 'linear-gradient(135deg, #0093E9 0%, #80D0C7 100%)', label: 'Arctic Breeze', rating: 0, isFavorite: false },
  { id: 5,  gradient: 'linear-gradient(135deg, #ee9ca7 0%, #ffdde1 100%)', label: 'Rose Garden',   rating: 0, isFavorite: false },
  { id: 6,  gradient: 'linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%)', label: 'Ember Glow',    rating: 0, isFavorite: false },
  { id: 7,  gradient: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)', label: 'Cloud Nine',    rating: 0, isFavorite: false },
  { id: 8,  gradient: 'linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%)', label: 'Forest Mist',   rating: 0, isFavorite: false },
  { id: 9,  gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', label: 'Cosmic Pink',   rating: 0, isFavorite: false },
  { id: 10, gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', label: 'Electric Sky',  rating: 0, isFavorite: false },
];

/* =========================================
   GALLERY RENDERING
   ========================================= */

/**
 * Builds an HTML string for a single image card.
 * jQuery's $() will parse this into DOM nodes.
 * @param {Object} imgData
 * @returns {string} HTML string for one card
 */
function buildCardHTML(imgData) {
  let starsHTML = '';
  for (let i = 1; i <= 5; i++) {
    starsHTML += `<span class="star"
                        data-value="${i}"
                        data-img-id="${imgData.id}"
                        role="radio"
                        tabindex="0"
                        aria-label="${i} star${i > 1 ? 's' : ''}">★</span>`;
  }

  return `
    <article class="image-card" id="card-${imgData.id}">
      <div class="card-image">
        <div class="card-image-bg"
             style="background: ${imgData.gradient};"
             role="img"
             aria-label="${imgData.label}"></div>
        <button class="btn-fav-overlay" id="fav-overlay-${imgData.id}"
                aria-label="Favorite ${imgData.label}">
          <span class="fav-icon">♡</span>
          <span class="fav-text">Favorite</span>
        </button>
      </div>
      <div class="card-body">
        <div class="star-rating" id="stars-${imgData.id}" role="radiogroup"
             aria-label="Rate ${imgData.label}">
          ${starsHTML}
        </div>
        <button class="btn-fav-inline" id="fav-inline-${imgData.id}"
                aria-label="Toggle favorite for ${imgData.label}">♡</button>
      </div>
    </article>`;
}

/**
 * Renders all cards into #gallery-grid using jQuery.
 */
function renderGallery() {
  const $grid = $('#gallery-grid');
  $grid.empty(); // jQuery DOM clear

  $.each(IMAGES, function(index, imgData) {
    const cardHTML = buildCardHTML(imgData);
    $grid.append(cardHTML); // jQuery append
  });
}

/* =========================================
   STAR DISPLAY HELPERS
   ========================================= */

/**
 * Updates star fill classes for a given image using jQuery.
 * @param {number} imgId
 * @param {number} fillUpTo - fill stars 1 through fillUpTo
 * @param {string} cssClass - 'filled' for permanent, 'hover-fill' for preview
 */
function applyStarClasses(imgId, fillUpTo, cssClass) {
  $(`#stars-${imgId} .star`).each(function() {
    const val = parseInt($(this).data('value'), 10);
    if (val <= fillUpTo) {
      $(this).addClass(cssClass);
    } else {
      $(this).removeClass(cssClass);
    }
  });
}

/**
 * Refreshes the permanent 'filled' class based on stored rating.
 * @param {number} imgId
 */
function refreshStarFill(imgId) {
  const imgData = IMAGES.find(img => img.id === imgId);
  $(`#stars-${imgId} .star`).removeClass('filled');
  if (imgData && imgData.rating > 0) {
    applyStarClasses(imgId, imgData.rating, 'filled');
  }
}

/* =========================================
   FAVORITE DISPLAY HELPERS
   ========================================= */

/**
 * Updates both favorite button states for an image using jQuery.
 * @param {number} imgId
 */
function refreshFavDisplay(imgId) {
  const imgData    = IMAGES.find(img => img.id === imgId);
  const $overlay   = $(`#fav-overlay-${imgId}`);
  const $inline    = $(`#fav-inline-${imgId}`);

  if (imgData.isFavorite) {
    $overlay.addClass('active');
    $inline.addClass('active');
    $overlay.find('.fav-icon').text('♥');
    $overlay.find('.fav-text').text('Favorited');
    $inline.text('♥');
  } else {
    $overlay.removeClass('active');
    $inline.removeClass('active');
    $overlay.find('.fav-icon').text('♡');
    $overlay.find('.fav-text').text('Favorite');
    $inline.text('♡');
  }
}

/* =========================================
   SUMMARY CALCULATION (jQuery .each())
   ========================================= */

/**
 * Recalculates the grand totals using jQuery .each() iteration
 * and animates the footer values with .fadeOut() / .fadeIn().
 */
function updateSummaryAnimated() {
  // --- Calculate Average Rating using jQuery .each() ---
  let totalRating  = 0;
  let ratedCount   = 0;

  // Iterate over all star rating containers in the DOM
  $('.star-rating').each(function() {
    const imgId   = parseInt($(this).attr('id').replace('stars-', ''), 10);
    const imgData = IMAGES.find(img => img.id === imgId);
    if (imgData && imgData.rating > 0) {
      totalRating += imgData.rating;
      ratedCount++;
    }
  });

  const avgRating = ratedCount > 0
    ? (totalRating / ratedCount).toFixed(1)
    : '0.0';

  const avgSubText = ratedCount > 0
    ? `based on ${ratedCount} rated item${ratedCount !== 1 ? 's' : ''}`
    : 'no ratings yet';

  // --- Calculate Total Favorites using jQuery .each() ---
  let totalFavs = 0;
  $('.btn-fav-inline').each(function() {
    if ($(this).hasClass('active')) {
      totalFavs++;
    }
  });

  // --- Animate footer update: fadeOut → change value → fadeIn ---
  const $avgVal = $('#avg-rating-val');
  const $avgSub = $('#avg-rating-sub');
  const $favVal = $('#total-favs-val');

  // Only animate if value has changed
  if ($avgVal.text() !== `${avgRating} ★`) {
    $avgVal.fadeOut(150, function() {
      $(this).text(`${avgRating} ★`).fadeIn(200);
    });
    $avgSub.fadeOut(150, function() {
      $(this).text(avgSubText).fadeIn(200);
    });
  }

  if ($favVal.text() !== String(totalFavs)) {
    $favVal.fadeOut(150, function() {
      $(this).text(totalFavs).fadeIn(200);
    });
  }
}

/* =========================================
   EVENT BINDING — Problem 03 Requirements
   ========================================= */

/**
 * Binds all events using jQuery .on() method.
 * Uses event delegation for dynamically rendered elements.
 */
function bindEvents() {
  const $grid = $('#gallery-grid');

  /* -------------------------------------------------------
     STAR CLICK — .on('click', selector, handler)
     Fills the clicked star and all stars to its left.
     ------------------------------------------------------- */
  $grid.on('click', '.star', function() {
    const $star   = $(this);
    const imgId   = parseInt($star.data('img-id'), 10);
    const value   = parseInt($star.data('value'), 10);
    const imgData = IMAGES.find(img => img.id === imgId);

    if (!imgData) return;

    // Toggle: clicking the same star a second time clears the rating
    imgData.rating = imgData.rating === value ? 0 : value;

    // Clear hover-fill and apply permanent fill
    $(`#stars-${imgId} .star`).removeClass('hover-fill filled');
    if (imgData.rating > 0) {
      applyStarClasses(imgId, imgData.rating, 'filled');
    }

    // Animated footer update
    updateSummaryAnimated();
  });

  /* -------------------------------------------------------
     STAR HOVER — mouseenter / mouseleave
     Pre-fills stars to show potential rating (jQuery effect).
     ------------------------------------------------------- */
  $grid.on('mouseenter', '.star', function() {
    const $star = $(this);
    const imgId = parseInt($star.data('img-id'), 10);
    const value = parseInt($star.data('value'), 10);

    // Remove current hover-fill, apply new one up to hovered star
    $(`#stars-${imgId} .star`).removeClass('hover-fill');
    applyStarClasses(imgId, value, 'hover-fill');
  });

  $grid.on('mouseleave', '.star-rating', function() {
    // When mouse leaves the rating container, remove all hover-fills
    const imgId = parseInt($(this).attr('id').replace('stars-', ''), 10);
    $(`#stars-${imgId} .star`).removeClass('hover-fill');
  });

  /* -------------------------------------------------------
     FAVORITE OVERLAY BUTTON — .on('click', ...)
     ------------------------------------------------------- */
  $grid.on('click', '.btn-fav-overlay', function(e) {
    e.stopPropagation();
    const imgId = parseInt($(this).attr('id').replace('fav-overlay-', ''), 10);
    toggleFavorite(imgId);
  });

  /* -------------------------------------------------------
     FAVORITE INLINE BUTTON — .on('click', ...)
     ------------------------------------------------------- */
  $grid.on('click', '.btn-fav-inline', function(e) {
    e.stopPropagation();
    const imgId = parseInt($(this).attr('id').replace('fav-inline-', ''), 10);
    toggleFavorite(imgId);
  });

  /* -------------------------------------------------------
     KEYBOARD ACCESSIBILITY — Enter / Space for stars
     ------------------------------------------------------- */
  $grid.on('keydown', '.star', function(e) {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      $(this).trigger('click');
    }
  });
}

/**
 * Toggles the favorite state using jQuery and updates the animated summary.
 * @param {number} imgId
 */
function toggleFavorite(imgId) {
  const imgData = IMAGES.find(img => img.id === imgId);
  if (!imgData) return;

  imgData.isFavorite = !imgData.isFavorite;
  refreshFavDisplay(imgId);
  updateSummaryAnimated();
}

/* =========================================
   DOCUMENT READY — jQuery entry point
   ========================================= */
$(function() {
  renderGallery();          // Build the gallery grid using jQuery
  bindEvents();             // Attach all events via .on()
  updateSummaryAnimated();  // Set initial footer state
});

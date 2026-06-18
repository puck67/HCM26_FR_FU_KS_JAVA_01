/**
 * Problem 01 & 02 — Vanilla JavaScript
 * Dynamic Image Gallery with 5-Star Rating & Favorites
 *
 * Uses: standard DOM API (createElement, appendChild, addEventListener, etc.)
 * No libraries or frameworks.
 */

'use strict';

/* =========================================
   DATA — 10 image card definitions
   Each has a CSS gradient for the placeholder,
   plus mutable state: rating and isFavorite.
   ========================================= */
const IMAGES = [
  { id: 1, gradient: 'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)', label: 'Sunset Pastel',   rating: 0, isFavorite: false },
  { id: 2, gradient: 'linear-gradient(135deg, #f6d365 0%, #fda085 100%)', label: 'Golden Hour',     rating: 0, isFavorite: false },
  { id: 3, gradient: 'linear-gradient(135deg, #1e3c72 0%, #2a5298 100%)', label: 'Ocean Depth',     rating: 0, isFavorite: false },
  { id: 4, gradient: 'linear-gradient(135deg, #0093E9 0%, #80D0C7 100%)', label: 'Arctic Breeze',   rating: 0, isFavorite: false },
  { id: 5, gradient: 'linear-gradient(135deg, #ee9ca7 0%, #ffdde1 100%)', label: 'Rose Garden',     rating: 0, isFavorite: false },
  { id: 6, gradient: 'linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%)', label: 'Ember Glow',      rating: 0, isFavorite: false },
  { id: 7, gradient: 'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)', label: 'Cloud Nine',      rating: 0, isFavorite: false },
  { id: 8, gradient: 'linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%)', label: 'Forest Mist',     rating: 0, isFavorite: false },
  { id: 9, gradient: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)', label: 'Cosmic Pink',     rating: 0, isFavorite: false },
  { id: 10,gradient: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)', label: 'Electric Sky',    rating: 0, isFavorite: false },
];

/* =========================================
   DOM REFERENCES
   ========================================= */
const galleryGrid    = document.getElementById('gallery-grid');
const avgRatingVal   = document.getElementById('avg-rating-val');
const avgRatingSub   = document.getElementById('avg-rating-sub');
const totalFavsVal   = document.getElementById('total-favs-val');

/* =========================================
   PROBLEM 01: BUILD GALLERY GRID WITH CSS
   ========================================= */

/**
 * Creates a single image card element from a data object.
 * @param {Object} imgData - one entry from IMAGES array
 * @returns {HTMLElement} the fully built card element
 */
function createCard(imgData) {
  // Card wrapper
  const card = document.createElement('article');
  card.className = 'image-card';
  card.id = `card-${imgData.id}`;

  /* --- Image placeholder --- */
  const cardImage = document.createElement('div');
  cardImage.className = 'card-image';

  const imageBg = document.createElement('div');
  imageBg.className = 'card-image-bg';
  imageBg.style.background = imgData.gradient;
  imageBg.setAttribute('role', 'img');
  imageBg.setAttribute('aria-label', imgData.label);

  /* --- Overlay favorite button --- */
  const btnFavOverlay = document.createElement('button');
  btnFavOverlay.className = 'btn-fav-overlay';
  btnFavOverlay.id = `fav-overlay-${imgData.id}`;
  btnFavOverlay.setAttribute('aria-label', `Add ${imgData.label} to favorites`);

  const favIcon = document.createElement('span');
  favIcon.className = 'fav-icon';
  favIcon.textContent = '♡';

  const favText = document.createElement('span');
  favText.className = 'fav-text';
  favText.textContent = 'Favorite';

  btnFavOverlay.appendChild(favIcon);
  btnFavOverlay.appendChild(favText);

  cardImage.appendChild(imageBg);
  cardImage.appendChild(btnFavOverlay);

  /* --- Card body: stars + inline heart --- */
  const cardBody = document.createElement('div');
  cardBody.className = 'card-body';

  // Star rating container
  const starRating = document.createElement('div');
  starRating.className = 'star-rating';
  starRating.id = `stars-${imgData.id}`;
  starRating.setAttribute('role', 'radiogroup');
  starRating.setAttribute('aria-label', `Rate ${imgData.label}`);

  for (let i = 1; i <= 5; i++) {
    const star = document.createElement('span');
    star.className = 'star';
    star.dataset.value = i;
    star.dataset.imgId = imgData.id;
    star.textContent = '★';
    star.setAttribute('role', 'radio');
    star.setAttribute('aria-label', `${i} star${i > 1 ? 's' : ''}`);
    star.setAttribute('tabindex', '0');
    starRating.appendChild(star);
  }

  // Inline favorite heart button
  const btnFavInline = document.createElement('button');
  btnFavInline.className = 'btn-fav-inline';
  btnFavInline.id = `fav-inline-${imgData.id}`;
  btnFavInline.setAttribute('aria-label', `Toggle favorite for ${imgData.label}`);
  btnFavInline.textContent = '♡';

  cardBody.appendChild(starRating);
  cardBody.appendChild(btnFavInline);

  card.appendChild(cardImage);
  card.appendChild(cardBody);

  return card;
}

/**
 * Renders all cards into the gallery grid.
 */
function renderGallery() {
  galleryGrid.innerHTML = '';
  IMAGES.forEach(imgData => {
    const card = createCard(imgData);
    galleryGrid.appendChild(card);
  });
}

/* =========================================
   PROBLEM 02: RATING LOGIC (Vanilla DOM)
   ========================================= */

/**
 * Updates the visual star fill state for a given image.
 * @param {number} imgId
 */
function updateStarDisplay(imgId) {
  const imgData = IMAGES.find(img => img.id === imgId);
  const starContainer = document.getElementById(`stars-${imgId}`);
  if (!starContainer) return;

  const stars = starContainer.querySelectorAll('.star');
  stars.forEach(star => {
    const val = parseInt(star.dataset.value, 10);
    if (val <= imgData.rating) {
      star.classList.add('filled');
    } else {
      star.classList.remove('filled');
    }
  });
}

/**
 * Updates both favorite buttons for a given image.
 * @param {number} imgId
 */
function updateFavDisplay(imgId) {
  const imgData    = IMAGES.find(img => img.id === imgId);
  const overlay    = document.getElementById(`fav-overlay-${imgId}`);
  const inline     = document.getElementById(`fav-inline-${imgId}`);

  if (imgData.isFavorite) {
    overlay?.classList.add('active');
    inline?.classList.add('active');
    if (overlay) overlay.querySelector('.fav-icon').textContent = '♥';
    if (overlay) overlay.querySelector('.fav-text').textContent = 'Favorited';
    if (inline) inline.textContent = '♥';
  } else {
    overlay?.classList.remove('active');
    inline?.classList.remove('active');
    if (overlay) overlay.querySelector('.fav-icon').textContent = '♡';
    if (overlay) overlay.querySelector('.fav-text').textContent = 'Favorite';
    if (inline) inline.textContent = '♡';
  }
}

/**
 * Recalculates and updates the summary footer.
 * Uses standard DOM API to update the display manually.
 */
function updateSummary() {
  // Average Rating: only count images that have been rated (rating > 0)
  const ratedImages = IMAGES.filter(img => img.rating > 0);
  const totalRating = ratedImages.reduce((sum, img) => sum + img.rating, 0);
  const avgRating   = ratedImages.length > 0
    ? (totalRating / ratedImages.length).toFixed(1)
    : '0.0';

  avgRatingVal.textContent = `${avgRating} ★`;
  avgRatingSub.textContent = ratedImages.length > 0
    ? `based on ${ratedImages.length} rated item${ratedImages.length !== 1 ? 's' : ''}`
    : 'no ratings yet';

  // Total Favorites
  const totalFavs = IMAGES.filter(img => img.isFavorite).length;
  totalFavsVal.textContent = totalFavs;
}

/* =========================================
   EVENT BINDING (using standard DOM API)
   ========================================= */

/**
 * Binds all click events using event delegation on the gallery grid.
 * This is a clean, scalable pattern using addEventListener.
 */
function bindEvents() {
  // Use event delegation — one listener on the grid handles all child events
  galleryGrid.addEventListener('click', function(event) {
    const target = event.target;

    /* --- Star click: rate the image --- */
    if (target.classList.contains('star')) {
      const imgId = parseInt(target.dataset.imgId, 10);
      const value  = parseInt(target.dataset.value, 10);

      const imgData = IMAGES.find(img => img.id === imgId);
      if (imgData) {
        // Toggle off if clicking the same star (re-click deselects)
        imgData.rating = imgData.rating === value ? 0 : value;

        // Update star display
        updateStarDisplay(imgId);

        // Recalculate grand totals in the summary footer
        updateSummary();
      }
    }

    /* --- Favorite button click (overlay or inline) --- */
    if (target.closest('.btn-fav-overlay') || target.classList.contains('btn-fav-overlay')) {
      const btn   = target.closest('.btn-fav-overlay');
      if (!btn) return;
      const imgId = parseInt(btn.id.replace('fav-overlay-', ''), 10);
      toggleFavorite(imgId);
    }

    if (target.classList.contains('btn-fav-inline') || target.closest('.btn-fav-inline')) {
      const btn   = target.closest('.btn-fav-inline');
      if (!btn) return;
      const imgId = parseInt(btn.id.replace('fav-inline-', ''), 10);
      toggleFavorite(imgId);
    }
  });

  // Keyboard accessibility for stars (Enter / Space to rate)
  galleryGrid.addEventListener('keydown', function(event) {
    if (event.key === 'Enter' || event.key === ' ') {
      const target = event.target;
      if (target.classList.contains('star')) {
        event.preventDefault();
        target.click();
      }
    }
  });
}

/**
 * Toggles the favorite state for an image, updates display & summary.
 * @param {number} imgId
 */
function toggleFavorite(imgId) {
  const imgData = IMAGES.find(img => img.id === imgId);
  if (!imgData) return;

  imgData.isFavorite = !imgData.isFavorite;
  updateFavDisplay(imgId);
  updateSummary();
}

/* =========================================
   INITIALISE
   ========================================= */
(function init() {
  renderGallery();   // Problem 01: build the grid
  bindEvents();      // Problem 02: attach interaction logic
  updateSummary();   // Set initial footer state
})();

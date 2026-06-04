// gallery.js — Problem 02
// Handles everything with plain DOM API, no jQuery involved.
// Renders cards from GALLERY_DATA, manages star ratings, favorites, and the footer totals.

// ── Rendering ───────────────────────────────────────────

// Returns the HTML string for one star button
function createStarHTML(position, cardId) {
  return `<button
      class="star-rating__star"
      data-card="${cardId}"
      data-value="${position}"
      aria-label="Rate ${position} star${position > 1 ? 's' : ''}"
      title="${position} star${position > 1 ? 's' : ''}"
    >★</button>`;
}

// Builds the full HTML for a single card
function createCardHTML(item) {
  const stars = [1, 2, 3, 4, 5]
    .map((n) => createStarHTML(n, item.id))
    .join("");

  return `
    <article class="card" id="${item.id}" data-rating="0" data-favorite="false">
      <div class="card__image-wrap">
        <img src="${item.image}" alt="${item.title}" loading="lazy">
        <div class="card__image-overlay"></div>
        <span class="card__badge">${item.category}</span>
      </div>

      <div class="card__body">
        <h2 class="card__title">${item.title}</h2>
        <p class="card__author">by ${item.author}</p>
      </div>

      <div class="card__footer">
        <div class="star-rating" aria-label="Star rating for ${item.title}">
          ${stars}
          <span class="star-rating__label" id="${item.id}-label">0</span>
        </div>

        <button
          class="btn-favorite"
          id="${item.id}-fav"
          data-card="${item.id}"
          aria-pressed="false"
          aria-label="Add ${item.title} to favorites"
        >
          <span class="btn-favorite__icon" aria-hidden="true">♡</span>
          <span class="btn-favorite__text">Save</span>
        </button>
      </div>
    </article>`;
}

// Dumps all cards into the grid container
function renderGallery() {
  const grid = document.getElementById("gallery-grid");
  grid.innerHTML = GALLERY_DATA.map(createCardHTML).join("");
}

// ── Star Rating ─────────────────────────────────────────

// Fills stars up to the given value, empties the rest
function updateStarDisplay(cardId, value) {
  const stars = document.querySelectorAll(`.star-rating__star[data-card="${cardId}"]`);
  stars.forEach((star) => {
    const starVal = parseInt(star.dataset.value, 10);
    if (starVal <= value) {
      star.classList.add("filled");
    } else {
      star.classList.remove("filled");
    }
  });

  // also update the little number next to the stars
  const label = document.getElementById(`${cardId}-label`);
  if (label) label.textContent = value;
}

function onStarClick(e) {
  const star = e.currentTarget;
  const cardId = star.dataset.card;
  const value  = parseInt(star.dataset.value, 10);
  const card   = document.getElementById(cardId);

  if (!card) return;

  card.dataset.rating = value;
  updateStarDisplay(cardId, value);

  // replay the bounce animation — we have to remove then re-add the class
  // because CSS animations don't restart if the class is already there
  star.classList.remove("pulse");
  void star.offsetWidth; // forces the browser to reflow before re-adding
  star.classList.add("pulse");

  recalculateSummary();
}

// ── Favorite Toggle ─────────────────────────────────────

function onFavoriteClick(e) {
  const btn    = e.currentTarget;
  const cardId = btn.dataset.card;
  const card   = document.getElementById(cardId);

  if (!card) return;

  const isFav = card.dataset.favorite === "true";
  const next  = !isFav;

  card.dataset.favorite = next;

  btn.classList.toggle("is-active", next);
  btn.setAttribute("aria-pressed", next);

  const icon = btn.querySelector(".btn-favorite__icon");
  const text = btn.querySelector(".btn-favorite__text");

  icon.textContent = next ? "♥" : "♡";
  text.textContent = next ? "Saved" : "Save";

  recalculateSummary();
}

// ── Summary Footer ──────────────────────────────────────

// Loops through every card, adds up the ratings and counts favorites,
// then pushes the new numbers into the footer
function recalculateSummary() {
  const cards = document.querySelectorAll(".card");

  let totalRating    = 0;
  let ratedCount     = 0;
  let totalFavorites = 0;

  cards.forEach((card) => {
    const rating = parseInt(card.dataset.rating, 10);
    if (rating > 0) {
      totalRating += rating;
      ratedCount++;
    }
    if (card.dataset.favorite === "true") {
      totalFavorites++;
    }
  });

  const avgRating = ratedCount > 0
    ? (totalRating / ratedCount).toFixed(1)
    : "—";

  const avgEl = document.getElementById("stat-avg-rating");
  const favEl = document.getElementById("stat-total-favorites");

  if (avgEl) avgEl.textContent = avgRating;
  if (favEl) favEl.textContent = totalFavorites;

  // flash both stats so the user notices the change
  [avgEl, favEl].forEach((el) => {
    if (!el) return;
    el.classList.remove("stat-flash");
    void el.offsetWidth;
    el.classList.add("stat-flash");
  });
}

// ── Event Binding ───────────────────────────────────────

function bindEvents() {
  document.querySelectorAll(".star-rating__star").forEach((star) => {
    star.addEventListener("click", onStarClick);
  });

  document.querySelectorAll(".btn-favorite").forEach((btn) => {
    btn.addEventListener("click", onFavoriteClick);
  });
}

// ── Init ────────────────────────────────────────────────

document.addEventListener("DOMContentLoaded", () => {
  renderGallery();
  bindEvents();
  recalculateSummary();
});

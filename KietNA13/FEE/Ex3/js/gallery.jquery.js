// gallery.jquery.js — Problem 03
// Same gallery, rewritten with jQuery.
// Key differences from gallery.js: hover star preview, .on() for events, fadeIn/fadeOut on the footer.
//
// Note: only load this OR gallery.js — not both.
// index-jquery.html uses this file, index.html uses gallery.js.

// ── Rendering ───────────────────────────────────────────

// Returns a single star as a jQuery element
function createStarEl(position, cardId) {
  return $("<button>")
    .addClass("star-rating__star")
    .attr({
      "data-card":  cardId,
      "data-value": position,
      "aria-label": `Rate ${position} star${position > 1 ? "s" : ""}`,
      "title":      `${position} star${position > 1 ? "s" : ""}`,
    })
    .text("★");
}

// Builds a full card as a jQuery element (no innerHTML here, all DOM nodes)
function createCardEl(item) {
  const $imageWrap = $("<div>").addClass("card__image-wrap").append(
    $("<img>").attr({ src: item.image, alt: item.title, loading: "lazy" }),
    $("<div>").addClass("card__image-overlay"),
    $("<span>").addClass("card__badge").text(item.category)
  );

  const $stars = $([1, 2, 3, 4, 5].map((n) => createStarEl(n, item.id).get(0)));
  const $label = $("<span>")
    .addClass("star-rating__label")
    .attr("id", `${item.id}-label`)
    .text("0");

  const $starRating = $("<div>")
    .addClass("star-rating")
    .attr("aria-label", `Star rating for ${item.title}`)
    .append($stars, $label);

  const $favBtn = $("<button>")
    .addClass("btn-favorite")
    .attr({
      id:             `${item.id}-fav`,
      "data-card":    item.id,
      "aria-pressed": "false",
      "aria-label":   `Add ${item.title} to favorites`,
    })
    .append(
      $("<span>").addClass("btn-favorite__icon").attr("aria-hidden", "true").text("♡"),
      $("<span>").addClass("btn-favorite__text").text("Save")
    );

  const $body = $("<div>").addClass("card__body").append(
    $("<h2>").addClass("card__title").text(item.title),
    $("<p>").addClass("card__author").text(`by ${item.author}`)
  );

  const $footer = $("<div>").addClass("card__footer").append($starRating, $favBtn);

  return $("<article>")
    .addClass("card")
    .attr({
      id:              item.id,
      "data-rating":   "0",
      "data-favorite": "false",
    })
    .append($imageWrap, $body, $footer);
}

function renderGallery() {
  const $grid = $("#gallery-grid");
  $grid.empty();
  GALLERY_DATA.forEach((item) => {
    $grid.append(createCardEl(item));
  });
}

// ── Star Rating ─────────────────────────────────────────

// Fills or empties stars for a card based on the given value
function updateStarDisplay(cardId, value) {
  $(`.star-rating__star[data-card="${cardId}"]`).each(function () {
    const starVal = parseInt($(this).data("value"), 10);
    $(this).toggleClass("filled", starVal <= value);
  });
  $(`#${cardId}-label`).text(value);
}

// ── Hover Preview ────────────────────────────────────────
// Shows which stars would be selected before the user clicks

function bindStarHover() {
  // highlight stars up to the one being hovered
  $(document).on("mouseenter", ".star-rating__star", function () {
    const cardId   = $(this).data("card");
    const hoverVal = parseInt($(this).data("value"), 10);

    $(`.star-rating__star[data-card="${cardId}"]`).each(function () {
      const starVal = parseInt($(this).data("value"), 10);
      if (starVal <= hoverVal) {
        $(this).addClass("hovered");
      } else {
        $(this).removeClass("hovered");
      }
    });
  });

  // clear the preview when the mouse leaves
  $(document).on("mouseleave", ".star-rating__star", function () {
    const cardId = $(this).data("card");
    $(`.star-rating__star[data-card="${cardId}"]`).removeClass("hovered");
  });
}

// ── Event Handling ───────────────────────────────────────

function bindStarClick() {
  $(document).on("click", ".star-rating__star", function () {
    const $star  = $(this);
    const cardId = $star.data("card");
    const value  = parseInt($star.data("value"), 10);
    const $card  = $(`#${cardId}`);

    $card.attr("data-rating", value);
    updateStarDisplay(cardId, value);

    // drop the hover highlight once the user has actually clicked
    $(`.star-rating__star[data-card="${cardId}"]`).removeClass("hovered");

    // same animation trick as gallery.js — remove, wait a tick, re-add
    $star.removeClass("pulse");
    setTimeout(() => $star.addClass("pulse"), 10);

    recalculateSummary();
  });
}

function bindFavoriteClick() {
  $(document).on("click", ".btn-favorite", function () {
    const $btn   = $(this);
    const cardId = $btn.data("card");
    const $card  = $(`#${cardId}`);
    const isFav  = $card.attr("data-favorite") === "true";
    const next   = !isFav;

    $card.attr("data-favorite", next);
    $btn.toggleClass("is-active", next).attr("aria-pressed", next);

    $btn.find(".btn-favorite__icon").text(next ? "♥" : "♡");
    $btn.find(".btn-favorite__text").text(next ? "Saved" : "Save");

    recalculateSummary();
  });
}

// ── Summary Footer ───────────────────────────────────────

// Uses .each() to walk all cards and tally up ratings + favorites
function recalculateSummary() {
  let totalRating    = 0;
  let ratedCount     = 0;
  let totalFavorites = 0;

  $(".card").each(function () {
    const rating = parseInt($(this).attr("data-rating"), 10);
    if (rating > 0) {
      totalRating += rating;
      ratedCount++;
    }
    if ($(this).attr("data-favorite") === "true") {
      totalFavorites++;
    }
  });

  const avgRating = ratedCount > 0
    ? parseFloat((totalRating / ratedCount).toFixed(1))
    : null;

  animateStatUpdate($("#stat-avg-rating"), avgRating === null ? "—" : avgRating.toString());
  animateStatUpdate($("#stat-total-favorites"), totalFavorites.toString());
}

// Fades the number out, swaps the text, then fades it back in
// Skips the animation if the value didn't actually change
function animateStatUpdate($el, newVal) {
  if ($el.text() === newVal) return;

  $el.fadeOut(120, function () {
    $(this).text(newVal).fadeIn(200);
  });
}

// ── Init ─────────────────────────────────────────────────

$(function () {
  renderGallery();
  bindStarHover();
  bindStarClick();
  bindFavoriteClick();
  recalculateSummary();
});

/**
 * gallery.js — Dynamic Image Gallery & Rating System
 *
 * Problem 02: Native DOM API — querySelector, classList, textContent
 * Problem 03: jQuery — .on(), .each(), .addClass()/.removeClass(),
 *             .fadeIn()/.fadeOut()/.animate(), $() selectors
 */

$(document).ready(function () {

  /* =====================================================
     Dữ liệu ảnh — màu gradient làm placeholder
     ===================================================== */
  const IMAGES = [
    { id: 1, label: 'Photo 01', bg: 'linear-gradient(135deg,#a8edea,#fed6e3)' },
    { id: 2, label: 'Photo 02', bg: 'linear-gradient(135deg,#f9a825,#ef5350)' },
    { id: 3, label: 'Photo 03', bg: 'linear-gradient(135deg,#1a237e,#1565c0)' },
    { id: 4, label: 'Photo 04', bg: 'linear-gradient(135deg,#00838f,#006064)' },
    { id: 5, label: 'Photo 05', bg: 'linear-gradient(135deg,#e91e63,#f06292)' },
    { id: 6, label: 'Photo 06', bg: 'linear-gradient(135deg,#c62828,#e57373)' },
    { id: 7, label: 'Photo 07', bg: 'linear-gradient(135deg,#0288d1,#4fc3f7)' },
    { id: 8, label: 'Photo 08', bg: 'linear-gradient(135deg,#2e7d32,#a5d6a7)' },
    { id: 9, label: 'Photo 09', bg: 'linear-gradient(135deg,#b71c1c,#ff8a80)' },
    { id: 10,label: 'Photo 10', bg: 'linear-gradient(135deg,#00695c,#80cbc4)' },
  ];

  /* =====================================================
     Problem 02: Tạo card bằng document.createElement
     ===================================================== */
  function createCard(img) {
    // Wrapper col
    const col = document.createElement('div');
    col.className = 'col';

    // Card
    const card = document.createElement('div');
    card.className = 'card h-100 border-0 shadow-sm';
    card.setAttribute('data-id', img.id);
    card.setAttribute('data-rating', '0');
    card.setAttribute('data-favorite', 'false');

    // --- Image placeholder ---
    const imgDiv = document.createElement('div');
    imgDiv.className = 'img-placeholder';
    imgDiv.style.background = img.bg;

    // "Add to Favorites" overlay text
    const overlay = document.createElement('span');
    overlay.className = 'fav-overlay';
    overlay.textContent = 'Add to Favorites';
    imgDiv.appendChild(overlay);

    // --- Card body ---
    const cardBody = document.createElement('div');
    cardBody.className = 'card-body p-2';

    // Label
    const labelEl = document.createElement('p');
    labelEl.className = 'text-muted mb-1 fw-semibold';
    labelEl.style.fontSize = '0.72rem';
    labelEl.textContent = img.label;

    // Stars row
    const starsDiv = document.createElement('div');
    starsDiv.className = 'd-flex align-items-center justify-content-between';

    const starsWrapper = document.createElement('div');
    starsWrapper.className = 'stars d-flex gap-1';

    // 5 stars
    for (let i = 1; i <= 5; i++) {
      const star = document.createElement('i');
      star.className = 'bi bi-star-fill star';
      star.setAttribute('data-value', i);
      starsWrapper.appendChild(star);
    }

    // Favorite (heart) button
    const favBtn = document.createElement('button');
    favBtn.className = 'btn-favorite';
    favBtn.innerHTML = '<i class="bi bi-heart-fill"></i>';

    starsDiv.appendChild(starsWrapper);
    starsDiv.appendChild(favBtn);

    cardBody.appendChild(labelEl);
    cardBody.appendChild(starsDiv);

    card.appendChild(imgDiv);
    card.appendChild(cardBody);
    col.appendChild(card);

    return col;
  }

  // Render tất cả cards vào gallery
  IMAGES.forEach(function (img) {
    const col = createCard(img);
    // Problem 02: appendChild
    document.getElementById('gallery-grid').appendChild(col);
  });

  /* =====================================================
     Problem 02: Tính và cập nhật summary footer
     bằng DOM API thuần
     ===================================================== */
  function updateSummaryDOM() {
    const cards = document.querySelectorAll('#gallery-grid .card');
    let totalRating = 0;
    let ratedCount  = 0;
    let favCount    = 0;

    cards.forEach(function (card) {
      const rating = parseInt(card.getAttribute('data-rating'));
      const fav    = card.getAttribute('data-favorite') === 'true';
      if (rating > 0) { totalRating += rating; ratedCount++; }
      if (fav) favCount++;
    });

    const avg = ratedCount > 0 ? (totalRating / ratedCount).toFixed(1) : '0.0';

    // Problem 02: cập nhật textContent trực tiếp
    document.getElementById('avg-rating-val').textContent = avg;
    document.getElementById('avg-based').textContent =
      ` (based on ${ratedCount} item${ratedCount !== 1 ? 's' : ''})`;
    document.getElementById('total-fav-val').textContent = favCount;
  }

  /* =====================================================
     Problem 03: jQuery — cập nhật footer với .animate()
     ===================================================== */
  function updateSummaryJQ() {
    // Tính bằng $.each() (Problem 03)
    let totalRating = 0;
    let ratedCount  = 0;
    let favCount    = 0;

    $('#gallery-grid .card').each(function () {
      const rating = parseInt($(this).data('rating'));
      const fav    = $(this).data('favorite') === true || $(this).attr('data-favorite') === 'true';
      if (rating > 0) { totalRating += rating; ratedCount++; }
      if (fav) favCount++;
    });

    const avg = ratedCount > 0 ? (totalRating / ratedCount).toFixed(1) : '0.0';

    // Problem 03: .animate() flash effect khi số thay đổi
    const $avgEl = $('#avg-rating-val');
    const $favEl = $('#total-fav-val');

    if ($avgEl.text() !== avg) {
      $avgEl.fadeOut(150, function () {
        $(this).text(avg).fadeIn(150);
      });
    }

    const favStr = String(favCount);
    if ($favEl.text() !== favStr) {
      $favEl.fadeOut(150, function () {
        $(this).text(favStr).fadeIn(150);
      });
    }

    $('#avg-based').text(
      ` (based on ${ratedCount} item${ratedCount !== 1 ? 's' : ''})`
    );
  }

  /* =====================================================
     Problem 03: jQuery Event Delegation .on('click', ...)
     ===================================================== */

  // ---- Click Star: rating logic ----
  // Problem 02: fill stars to the left
  // Problem 03: .on() delegation + .addClass()/.removeClass()
  $('#gallery-grid').on('click', '.star', function () {
    const $star   = $(this);
    const $card   = $star.closest('.card');
    const value   = parseInt($star.data('value'));

    // Lưu rating vào data attribute (Problem 02)
    $card.attr('data-rating', value);

    // Cập nhật màu sao — fill tất cả sao ≤ value
    $card.find('.star').each(function () {
      if (parseInt($(this).data('value')) <= value) {
        // Problem 03: .addClass()
        $(this).addClass('filled').removeClass('hover-fill');
      } else {
        // Problem 03: .removeClass()
        $(this).removeClass('filled hover-fill');
      }
    });

    updateSummaryJQ();
  });

  // ---- Hover Star: pre-fill effect (Problem 03) ----
  $('#gallery-grid').on('mouseenter', '.star', function () {
    const $star  = $(this);
    const $card  = $star.closest('.card');
    const hValue = parseInt($star.data('value'));

    $card.find('.star').each(function () {
      if (parseInt($(this).data('value')) <= hValue) {
        $(this).addClass('hover-fill');
      } else {
        $(this).removeClass('hover-fill');
      }
    });
  });

  $('#gallery-grid').on('mouseleave', '.stars', function () {
    const $card = $(this).closest('.card');
    const rated = parseInt($card.attr('data-rating'));

    $card.find('.star').each(function () {
      $(this).removeClass('hover-fill');
      // Giữ lại filled nếu đã rate
      if (parseInt($(this).data('value')) <= rated) {
        $(this).addClass('filled');
      }
    });
  });

  // ---- Click Favorite heart button ----
  $('#gallery-grid').on('click', '.btn-favorite', function () {
    const $btn  = $(this);
    const $card = $btn.closest('.card');
    const isFav = $card.attr('data-favorite') === 'true';

    // Toggle state
    $card.attr('data-favorite', String(!isFav));

    if (!isFav) {
      // Thêm yêu thích
      $btn.addClass('active');
      // Problem 03: cập nhật overlay text
      $card.find('.fav-overlay').text('♥ Favorited');

      // Problem 03: .animate() bounce nhỏ
      $btn.animate({ fontSize: '1.6rem' }, 100)
          .animate({ fontSize: '1.3rem' }, 100);
    } else {
      // Bỏ yêu thích
      $btn.removeClass('active');
      $card.find('.fav-overlay').text('Add to Favorites');
    }

    updateSummaryJQ();
  });

  /* =====================================================
     Khởi tạo ban đầu
     ===================================================== */
  updateSummaryDOM();
});

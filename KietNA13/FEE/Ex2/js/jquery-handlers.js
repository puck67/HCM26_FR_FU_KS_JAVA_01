// jQuery event handlers and DOM management via event delegation
$(function () {

  // Move card to next column on Start / Move / Done click
  $('#board').on('click', '.card .btn--move, .card .btn--move-ip, .card .btn--done', function () {
    _moveCard($(this).closest('.card'));
  });

  // Delete card with animation
  $('#board').on('click', '.card .btn--delete', function () {
    _deleteCard($(this).closest('.card'));
  });

  // Archive card (remove from Done column)
  $('#board').on('click', '.card .btn--archive', function () {
    _archiveCard($(this).closest('.card'));
  });

  // Edit card title
  $('#board').on('click', '.card .btn--outline', function () {
    editCard($(this).closest('.card')[0]);
  });

  // Move card element to next column's container
  function _moveCard($card) {
    const currentKey = COL_KEY[$card.parent().parent().attr('id')];
    const nextKey    = COLUMNS[currentKey]?.next;
    if (!nextKey) return;

    const label = nextKey === 'inprogress' ? 'In Progress' : 'Done';

    $card.addClass('removing');
    $card.one('animationend', function () {
      $card.removeClass('removing');
      $card.appendTo('#' + COLUMNS[nextKey].cards);
      _rebuildButtons($card, nextKey);
      refreshCounts();
      showToast(`Moved to ${label} ✔`);
    });
  }

  // Delete card from DOM
  function _deleteCard($card) {
    $card.addClass('removing');
    $card.one('animationend', function () {
      $card.remove();
      refreshCounts();
      showToast('Task deleted.');
    });
  }

  // Archive card from DOM
  function _archiveCard($card) {
    $card.addClass('removing');
    $card.one('animationend', function () {
      $card.remove();
      refreshCounts();
      showToast('Task archived 📦');
    });
  }

  // Re-render buttons for the card based on its new column
  function _rebuildButtons($card, colKey) {
    const $actions = $card.find('.card__actions').empty();
    const arrowIcon = `<svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><path d="M5 12h14M12 5l7 7-7 7"/></svg>`;

    if (colKey === 'todo') {
      $actions.append(
        $('<button>').addClass('btn btn--move').html(`${arrowIcon} Start`).attr('title', 'Move to In Progress'),
        $('<button>').addClass('btn btn--outline').text('✏️ Edit')
      );
    } else if (colKey === 'inprogress') {
      $actions.append(
        $('<button>').addClass('btn btn--done').text('✓ Done'),
        $('<button>').addClass('btn btn--outline').text('✏️ Edit'),
        $('<button>').addClass('btn btn--move-ip').html(`${arrowIcon} Move`)
      );
    } else if (colKey === 'done') {
      $actions.append(
        $('<button>').addClass('btn btn--outline').text('✏️ Edit'),
        $('<button>').addClass('btn btn--archive').text('📦 Archive'),
        $('<button>').addClass('btn btn--delete').text('🗑 Delete')
      );
    }
  }

  // Real-time task search filtering
  $('#global-search').on('input', function () {
    const query = $(this).val().toLowerCase().trim();
    let visibleCount = 0;

    $('.card').each(function () {
      const title = $(this).attr('data-title') || '';

      if (!query || title.includes(query)) {
        $(this).show();
        visibleCount++;
      } else {
        $(this).hide();
      }
    });

    // Toggle no results banner
    if (query && visibleCount === 0) {
      $('#no-results').fadeIn(200);
    } else {
      $('#no-results').fadeOut(150);
    }
  });

});

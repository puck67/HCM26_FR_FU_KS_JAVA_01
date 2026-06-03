/**
 * list.js — List page logic
 * - Tabs: Active / Drafts / Closed
 * - Render polls per tab from localStorage
 * - View results (modal), Close poll, Delete (confirm → AJAX)
 * Requires: app.js, jQuery, Bootstrap 4
 */

$(function () {
  var $tabContent  = $('#tab-content');
  var $noPolls     = $('#no-polls-msg');
  var currentTab   = 'active';

  /* ============================================================
     RENDER TABLE FOR A STATUS TAB
     ============================================================ */
  function renderTab(status) {
    currentTab = status;
    var polls = PollApp.Store.getByStatus(status);
    $tabContent.empty();
    $noPolls.hide();

    if (polls.length === 0) {
      $noPolls.text('No ' + status + ' polls found.').show();
      return;
    }

    var $table = $(
      '<table class="table table-bordered table-hover mb-0">' +
        '<thead>' +
          '<tr>' +
            '<th style="width:60px">#</th>' +
            '<th>The list of questions and issues within</th>' +
            '<th style="width:220px">Management</th>' +
          '</tr>' +
        '</thead>' +
        '<tbody id="poll-tbody"></tbody>' +
      '</table>'
    );

    var $tbody = $table.find('#poll-tbody');

    polls.forEach(function (poll) {
      var $tr = $('<tr data-poll-id="' + poll.id + '"></tr>');
      var $tdId   = $('<td></td>').text(poll.id);
      var $tdName = $('<td></td>').text(poll.name);
      var $tdMgmt = $('<td></td>');

      var $btnGroup = $('<div class="btn-group-manage d-flex gap-1 flex-wrap"></div>');

      // View results
      var $btnView = $('<button type="button" class="btn btn-sm btn-view-results">View results</button>')
        .attr('data-poll-id', poll.id);

      $tdMgmt.append($btnGroup);

      if (status === 'active') {
        // Close poll button
        var $btnClose = $('<button type="button" class="btn btn-sm btn-close-poll">Close poll</button>')
          .attr('data-poll-id', poll.id);
        // Delete button
        var $btnDelete = $('<button type="button" class="btn btn-sm btn-delete-poll">Delete</button>')
          .attr('data-poll-id', poll.id);
        $btnGroup.append($btnView, $btnClose, $btnDelete);
      } else if (status === 'draft') {
        var $btnActivate = $('<button type="button" class="btn btn-sm btn-activate-poll">Activate</button>')
          .attr('data-poll-id', poll.id);
        var $btnDeleteD = $('<button type="button" class="btn btn-sm btn-delete-poll">Delete</button>')
          .attr('data-poll-id', poll.id);
        $btnGroup.append($btnView, $btnActivate, $btnDeleteD);
      } else {
        // closed
        var $btnDeleteC = $('<button type="button" class="btn btn-sm btn-delete-poll">Delete</button>')
          .attr('data-poll-id', poll.id);
        $btnGroup.append($btnView, $btnDeleteC);
      }

      $tr.append($tdId, $tdName, $tdMgmt);
      $tbody.append($tr);
    });

    $tabContent.append($table);
  }

  /* ============================================================
     TAB SWITCHING
     ============================================================ */
  $('.tab-btn').on('click', function () {
    $('.tab-btn').removeClass('active');
    $(this).addClass('active');
    renderTab($(this).data('status'));
  });

  /* ============================================================
     DELEGATED BUTTON EVENTS
     ============================================================ */

  /* ---- View Results ---- */
  $tabContent.on('click', '.btn-view-results', function () {
    var id   = parseInt($(this).data('poll-id'), 10);
    var poll = PollApp.Store.getById(id);
    if (!poll) return;
    showResultsModal(poll);
  });

  /* ---- Close Poll ---- */
  $tabContent.on('click', '.btn-close-poll', function () {
    var id = parseInt($(this).data('poll-id'), 10);
    if (!confirm('Are you sure you want to close this poll?')) return;
    ajaxUpdateStatus(id, 'closed', function () {
      renderTab(currentTab);
    });
  });

  /* ---- Activate Poll (draft → active) ---- */
  $tabContent.on('click', '.btn-activate-poll', function () {
    var id = parseInt($(this).data('poll-id'), 10);
    if (!confirm('Activate this poll?')) return;
    ajaxUpdateStatus(id, 'active', function () {
      renderTab(currentTab);
    });
  });

  /* ---- Delete Poll ---- */
  $tabContent.on('click', '.btn-delete-poll', function () {
    var id   = parseInt($(this).data('poll-id'), 10);
    var poll = PollApp.Store.getById(id);
    if (!poll) return;

    if (!confirm('Are you sure you want to delete "' + poll.name + '"? This cannot be undone.')) return;

    ajaxDelete(id, function () {
      // Animate row removal
      $tabContent.find('tr[data-poll-id="' + id + '"]').fadeOut(250, function () {
        $(this).remove();
        // Re-render if table is empty
        if (PollApp.Store.getByStatus(currentTab).length === 0) {
          renderTab(currentTab);
        }
      });
    });
  });

  /* ============================================================
     AJAX HELPERS (simulated — no real server)
     ============================================================ */

  /**
   * Simulate AJAX POST to update poll status.
   */
  function ajaxUpdateStatus(id, newStatus, callback) {
    // Simulate network delay
    setTimeout(function () {
      var ok = PollApp.Store.updateStatus(id, newStatus);
      if (ok) {
        callback();
      } else {
        showToast('Failed to update poll status.', 'danger');
      }
    }, 300);
  }

  /**
   * Simulate AJAX POST to delete a poll.
   * In a real app: $.ajax({ url: '/api/polls/' + id, type: 'DELETE', ... })
   */
  function ajaxDelete(id, callback) {
    // Simulated AJAX request object
    var fakeAjax = {
      url: 'delete-poll.php',
      type: 'POST',
      data: { id: id, _method: 'DELETE' },
      success: null,
      error: null
    };

    // Simulate: delay → "server" removes from store → callback
    setTimeout(function () {
      var removed = PollApp.Store.remove(id);
      if (removed) {
        if (typeof fakeAjax.success === 'function') fakeAjax.success();
        callback();
        showToast('Poll deleted successfully.', 'success');
      } else {
        showToast('Could not delete poll.', 'danger');
      }
    }, 350);
  }

  /* ============================================================
     RESULTS MODAL
     ============================================================ */
  function showResultsModal(poll) {
    var $body = $('#results-body');
    $body.empty();

    $body.append('<h6 class="font-weight-bold mb-3">' + poll.name + '</h6>');
    poll.questions.forEach(function (q, idx) {
      var $qBlock = $('<div class="mb-3"></div>');
      $qBlock.append(
        '<p class="mb-1" style="font-size:0.85rem;color:#555;">' +
        (idx + 1) + '. ' + q.text + '</p>'
      );
      q.answers.forEach(function (a) {
        // Random percentage for demo results
        var pct = Math.floor(Math.random() * 80) + 5;
        $qBlock.append(
          '<div class="d-flex align-items-center mb-1" style="font-size:0.8rem;">' +
            '<span style="width:100px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;" title="' + a + '">' + a + '</span>' +
            '<div class="flex-grow-1 bg-light mx-2" style="height:14px;border-radius:3px;overflow:hidden;">' +
              '<div style="height:100%;width:' + pct + '%;background:var(--color-primary);transition:width .5s;"></div>' +
            '</div>' +
            '<span>' + pct + '%</span>' +
          '</div>'
        );
      });
      $body.append($qBlock);
    });

    $('#resultsModal').modal('show');
  }

  /* ============================================================
     TOAST
     ============================================================ */
  function showToast(message, type) {
    var colors = {
      success: { bg: '#155724', bdr: '#28a745' },
      warning: { bg: '#856404', bdr: '#ffc107' },
      danger:  { bg: '#721c24', bdr: '#dc3545' }
    };
    var c = colors[type] || colors.success;
    var $toast = $(
      '<div style="position:fixed;bottom:1.5rem;right:1.5rem;z-index:9999;' +
      'background:#fff;border-left:4px solid ' + c.bdr + ';border-radius:4px;' +
      'padding:0.65rem 1rem;box-shadow:0 4px 12px rgba(0,0,0,.15);' +
      'font-size:0.82rem;color:' + c.bg + ';max-width:280px;">' +
      message + '</div>'
    );
    $('body').append($toast);
    setTimeout(function () { $toast.fadeOut(300, function () { $toast.remove(); }); }, 3000);
  }

  /* ---- Init ---- */
  renderTab('active');
});

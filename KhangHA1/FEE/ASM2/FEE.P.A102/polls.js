/**
 * polls.js
 * jQuery logic for the Polls application (ASM2)
 * Features:
 *  - Login modal trigger
 *  - Vote form submit feedback
 *  - Create form: jQuery validation + dynamic questions/answers
 *  - List page: tab switching + AJAX delete with confirm dialog
 */

$(document).ready(function () {

  /* ============================================================
     1. LOGIN MODAL
     ============================================================ */
  $('#btn-login-trigger').on('click', function () {
    $('#loginModal').modal('show');
  });

  // Login form submit (simulate — no real backend)
  $('#login-form').on('submit', function (e) {
    e.preventDefault();
    var alias    = $.trim($('#login-alias').val());
    var password = $.trim($('#login-password').val());

    if (!alias || !password) {
      alert('Please enter both Alias and Password.');
      return;
    }
    // Simulate successful login
    $('#loginModal').modal('hide');
    alert('Signed in as "' + alias + '".');
  });


  /* ============================================================
     2. VOTE PAGE — Retain button feedback
     ============================================================ */
  $('#vote-form').on('submit', function (e) {
    e.preventDefault();
    $('#vote-success').addClass('show');
    $('html, body').animate({ scrollTop: $('#vote-success').offset().top - 80 }, 300);
  });


  /* ============================================================
     3. CREATE PAGE — jQuery Validation
     ============================================================ */

  /**
   * Validate a single field.
   * @param {jQuery} $field  - the input element
   * @param {string} label   - human-readable field name for error messages
   * @param {number} min     - minimum character length
   * @param {number} max     - maximum character length
   * @returns {string|null}  - error string or null if valid
   */
  function validateField($field, label, min, max) {
    var val = $.trim($field.val());
    $field.removeClass('is-invalid is-valid');

    if (!val) {
      $field.addClass('is-invalid');
      return label + ' is required.';
    }
    if (val.length < min) {
      $field.addClass('is-invalid');
      return label + ' must be at least ' + min + ' characters.';
    }
    if (val.length > max) {
      $field.addClass('is-invalid');
      return label + ' must be no more than ' + max + ' characters.';
    }
    $field.addClass('is-valid');
    return null;
  }

  /**
   * Clear validation state from a field.
   */
  function clearFieldError($field) {
    $field.removeClass('is-invalid is-valid');
  }

  // Live clear on input
  $(document).on('input', '.form-control', function () {
    clearFieldError($(this));
  });

  // Create form submission
  $('#create-form').on('submit', function (e) {
    e.preventDefault();

    var errors = [];

    // Reset all error states
    $('#create-form .form-control').removeClass('is-invalid is-valid');
    $('#error-summary').removeClass('show');
    $('#error-list').empty();
    $('#create-success').removeClass('show');

    // --- Validate poll name ---
    var pollNameErr = validateField($('#poll-name'), 'Name poll', 3, 255);
    if (pollNameErr) errors.push(pollNameErr);

    // --- Validate each question block ---
    $('#questions-container .question-block').each(function (qIdx) {
      var $block      = $(this);
      var qNum        = qIdx + 1;
      var $qInput     = $block.find('.question-input');
      var qErr        = validateField($qInput, 'Question ' + qNum, 3, 255);
      if (qErr) errors.push(qErr);

      // Validate each answer input in this block
      $block.find('.answer-input').each(function (aIdx) {
        var $aInput = $(this);
        var aErr    = validateField($aInput, 'Question ' + qNum + ' — Answer ' + (aIdx + 1), 3, 200);
        if (aErr) errors.push(aErr);
      });
    });

    // --- Show errors or success ---
    if (errors.length > 0) {
      $.each(errors, function (i, msg) {
        $('#error-list').append('<li>' + msg + '</li>');
      });
      $('#error-summary').addClass('show');
      $('html, body').animate({ scrollTop: $('#error-summary').offset().top - 80 }, 300);
      return;
    }

    // All valid — show success
    $('#create-success').addClass('show');
    $('html, body').animate({ scrollTop: 0 }, 300);
    // Optionally reset the form after a delay
    setTimeout(function () {
      $('#create-form')[0].reset();
      $('#create-form .form-control').removeClass('is-valid');
      // Reset to one question block
      var $firstBlock = $('#questions-container .question-block').first();
      $('#questions-container .question-block').not(':first').remove();
      $firstBlock.find('.answer-row').not(':first').remove();
      $firstBlock.find('.form-control').val('').removeClass('is-valid is-invalid');
      $firstBlock.find('input[type=checkbox]').prop('checked', false);
    }, 2000);
  });


  /* ============================================================
     4. CREATE PAGE — Dynamic: Add Answer row
     ============================================================ */
  $(document).on('click', '.btn-add-answer-btn', function () {
    var $answersWrapper = $(this).closest('.answers-wrapper');
    var $firstRow       = $answersWrapper.find('.answer-row').first();

    // Clone a blank answer row (without the "Possible answers" label and + button)
    var $newRow = $('<div class="answer-row"></div>');
    var $input  = $('<input type="text" class="form-control answer-input" name="answers[]" maxlength="200" autocomplete="off" />')
                    .attr('placeholder', 'Type your answer');
    $newRow.append($input);

    // Insert before the error div (or at the end of the wrapper)
    var $errorDiv = $answersWrapper.find('.answer-error');
    if ($errorDiv.length) {
      $errorDiv.before($newRow);
    } else {
      $answersWrapper.append($newRow);
    }

    $input.focus();
  });


  /* ============================================================
     5. CREATE PAGE — Dynamic: Add Question block
     ============================================================ */
  var questionCount = 1;

  $('#btn-add-question').on('click', function () {
    questionCount++;
    var idx = questionCount;

    var $block = $('<div class="question-block" data-question-index="' + idx + '"></div>');

    $block.html(
      '<button type="button" class="remove-question-btn" title="Remove question">&times;</button>' +

      '<div class="form-group">' +
        '<input type="text" class="form-control question-input" name="question[]"' +
          ' placeholder="Enter your question" maxlength="255" autocomplete="off" />' +
        '<div class="invalid-feedback">Question is required (min 3, max 255 characters).</div>' +
      '</div>' +

      '<div class="form-check">' +
        '<input class="form-check-input" type="checkbox" id="mandatory-' + idx + '" name="mandatory[]" />' +
        '<label class="form-check-label" for="mandatory-' + idx + '">Mandatory</label>' +
      '</div>' +

      '<div class="form-check mb-2">' +
        '<input class="form-check-input" type="checkbox" id="multiple-' + idx + '" name="multiple[]" />' +
        '<label class="form-check-label" for="multiple-' + idx + '">You can select multiple options</label>' +
      '</div>' +

      '<div class="answers-wrapper">' +
        '<div class="answer-row">' +
          '<span class="answers-label">Possible answers</span>' +
          '<input type="text" class="form-control answer-input" name="answers[]"' +
            ' placeholder="Type your answer" maxlength="200" autocomplete="off" />' +
          '<button type="button" class="btn btn-add-answer btn-add-answer-btn" title="Add answer">+</button>' +
        '</div>' +
        '<div class="invalid-feedback answer-error" style="margin-left:118px;">' +
          'Answer is required (min 3, max 200 characters).' +
        '</div>' +
      '</div>'
    );

    $('#questions-container').append($block);
    $block.find('.question-input').focus();

    // Smooth scroll to the new block
    $('html, body').animate({ scrollTop: $block.offset().top - 100 }, 300);
  });


  /* ============================================================
     6. CREATE PAGE — Remove question block
     ============================================================ */
  $(document).on('click', '.remove-question-btn', function () {
    var $block     = $(this).closest('.question-block');
    var totalBlocks = $('#questions-container .question-block').length;

    if (totalBlocks <= 1) {
      alert('A poll must have at least one question.');
      return;
    }
    $block.fadeOut(200, function () {
      $(this).remove();
    });
  });


  /* ============================================================
     7. LIST PAGE — Tab switching
     ============================================================ */
  $('#poll-tab-nav').on('click', '.nav-link', function (e) {
    e.preventDefault();

    var tabKey = $(this).data('tab');

    // Update tab link styles
    $('#poll-tab-nav .nav-link').removeClass('active-tab');
    $(this).addClass('active-tab');

    // Show corresponding panel
    $('.tab-panel').removeClass('active');
    $('#panel-' + tabKey).addClass('active');
  });


  /* ============================================================
     8. LIST PAGE — AJAX Delete with confirm dialog
     ============================================================ */
  $(document).on('click', '.btn-delete', function () {
    var $btn    = $(this);
    var pollId  = $btn.data('poll-id');
    var $row    = $btn.closest('tr');
    var pollName = $row.find('td:nth-child(2)').text();

    // Show browser confirm dialog as specified in the assignment
    var confirmed = confirm('Are you sure you want to delete this item?');

    if (!confirmed) return;

    // Simulate AJAX call to delete the item
    $btn.prop('disabled', true).text('Deleting...');

    $.ajax({
      url: '#',                // No real server — simulated
      method: 'POST',
      data: { pollId: pollId, action: 'delete' },
      complete: function () {
        // Whether success or error, remove the row (simulation)
        $row.addClass('row-deleting');
        setTimeout(function () {
          $row.remove();
          // Show feedback
          $('#delete-feedback').text('"' + pollName + '" deleted successfully.').addClass('show');
          setTimeout(function () {
            $('#delete-feedback').removeClass('show');
          }, 3000);
        }, 400);
      }
    });
  });


  /* ============================================================
     9. LIST PAGE — Close poll button
     ============================================================ */
  $(document).on('click', '.btn-close-poll', function () {
    var $btn  = $(this);
    var $row  = $btn.closest('tr');
    var name  = $row.find('td:nth-child(2)').text();

    $btn.prop('disabled', true).text('Closed');
    $row.css('opacity', '0.6');

    $('#delete-feedback')
      .text('"' + name + '" has been closed.')
      .addClass('show');
    setTimeout(function () {
      $('#delete-feedback').removeClass('show');
    }, 3000);
  });


  /* ============================================================
     10. LIST PAGE — View details button (simple alert)
     ============================================================ */
  $(document).on('click', '.btn-view-details', function () {
    var $row = $(this).closest('tr');
    var name = $row.find('td:nth-child(2)').text();
    alert('Viewing details for: "' + name + '"');
  });

});

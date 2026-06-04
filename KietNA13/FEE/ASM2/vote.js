/**
 * vote.js — Home / Vote page logic
 * - AJAX load poll-data.html → parse body → sanitize → render
 * - Render questions as radio/checkbox dynamically
 * - Validate mandatory questions on Retain
 * - Show success message
 * Requires: app.js, jQuery, Bootstrap 4
 */

$(function () {
  /* ---- Config ---- */
  var DATA_URL = 'poll-data.html';
  var $container   = $('#vote-poll-container');
  var $summary     = $('#vote-validation-summary');
  var $success     = $('#vote-success-banner');
  var $btnRetain   = $('#btn-vote-retain');
  var $pollTitle   = $('#vote-poll-title');

  /* ---- Load poll via AJAX ---- */
  function loadPoll() {
    $container.html(PollApp.UI.spinner());

    $.ajax({
      url: DATA_URL,
      type: 'GET',
      cache: false,
      success: function (responseText) {
        // Extract <body>…</body> content only — no <head>, no <style>, no <link>
        var bodyContent = extractBody(responseText);
        // Sanitize: strip any <style>, <link>, <script> tags from extracted content
        var sanitized = sanitize(bodyContent);
        // Parse and render
        renderPoll(sanitized);
      },
      error: function () {
        // AJAX bị block (file:// protocol) hoặc file không tồn tại
        // → tự động fallback về localStorage (PollApp.Store)
        renderFromStore();
      }
    });
  }

  /**
   * Extract content inside <body>…</body> from raw HTML string.
   * Falls back to using the whole string if no body tag found.
   */
  function extractBody(html) {
    var match = html.match(/<body[^>]*>([\s\S]*?)<\/body>/i);
    return match ? match[1] : html;
  }

  /**
   * Remove <style>, <link rel="stylesheet">, and <script> tags from html string.
   */
  function sanitize(html) {
    return html
      .replace(/<style[^>]*>[\s\S]*?<\/style>/gi, '')
      .replace(/<link[^>]+rel=["']?stylesheet["']?[^>]*\/?>/gi, '')
      .replace(/<script[^>]*>[\s\S]*?<\/script>/gi, '');
  }

  /**
   * Parse the sanitized HTML string and render questions into the page.
   */
  function renderPoll(html) {
    // Parse as DOM tree
    var $doc = $($.parseHTML(html));
    var $pollContainer = $doc.find('#poll-container');

    if ($pollContainer.length === 0) {
      // Fallback — try loading from PollApp.Store
      renderFromStore();
      return;
    }

    var pollName = $pollContainer.data('poll-name') || 'Poll';
    $pollTitle.text(pollName);
    $container.empty();

    $pollContainer.find('.poll-question').each(function (idx) {
      var $q        = $(this);
      var qId       = $q.data('question-id') || (idx + 1);
      var qText     = $.trim($q.find('.question-text').text());
      var mandatory = $q.data('mandatory') === true || $q.data('mandatory') === 'true';
      var multiple  = $q.data('multiple')  === true || $q.data('multiple')  === 'true';
      var answers   = [];

      $q.find('.poll-answer').each(function () {
        answers.push($.trim($(this).text()));
      });

      $container.append(buildQuestionBlock(qId, idx + 1, qText, mandatory, multiple, answers));
    });

    bindLiveValidation();
  }

  /**
   * Fallback: render first active poll from localStorage.
   */
  function renderFromStore() {
    var actives = PollApp.Store.getByStatus('active');
    if (actives.length === 0) {
      $container.html('<p class="text-muted">No active polls available.</p>');
      return;
    }
    var poll = actives[0];
    $pollTitle.text(poll.name);
    $container.empty();

    poll.questions.forEach(function (q, idx) {
      $container.append(
        buildQuestionBlock(idx + 1, idx + 1, q.text, q.mandatory, q.multipleChoice, q.answers)
      );
    });

    bindLiveValidation();
  }

  /**
   * Build a question block DOM element.
   */
  function buildQuestionBlock(qId, displayNum, qText, mandatory, multipleChoice, answers) {
    var inputType = multipleChoice ? 'checkbox' : 'radio';
    var groupName = 'question_' + qId;

    var $block = $('<div class="question-block"></div>').attr('data-question-id', qId);
    var $label = $('<span class="question-label"></span>');
    $label.text(displayNum + '. ' + qText);
    if (mandatory) {
      $label.append(' <span class="required-star" title="Required">*</span>');
    }
    $block.append($label);

    var $optionsWrap = $('<div class="options-wrap"></div>');
    answers.forEach(function (ans, aIdx) {
      var inputId = groupName + '_ans_' + aIdx;
      var $row = $('<div class="answer-option"></div>');
      var $input = $('<input>')
        .attr({ type: inputType, name: groupName, id: inputId, value: ans })
        .addClass('mr-1');
      var $lbl = $('<label></label>').attr('for', inputId).text(ans).css('cursor', 'pointer');
      $row.append($input, $lbl);
      $optionsWrap.append($row);
    });

    $block.append($optionsWrap);
    return $block;
  }

  /**
   * Live validation: when a mandatory question is answered, clear its error.
   */
  function bindLiveValidation() {
    $container.on('change', 'input[type=radio], input[type=checkbox]', function () {
      var $block = $(this).closest('.question-block');
      $block.removeClass('is-invalid-block');
      $block.find('.question-label').removeClass('invalid-label');
      $block.find('.question-inline-error').remove();
      // Rebuild summary without this question
      updateSummary();
    });
  }

  function updateSummary() {
    var errors = collectErrors();
    if (errors.length > 0) {
      PollApp.Validator.showSummary($summary, errors);
    } else {
      PollApp.Validator.hideSummary($summary);
    }
  }

  /**
   * Collect errors from all mandatory unanswered questions.
   * @returns {string[]}
   */
  function collectErrors() {
    var errors = [];
    $container.find('.question-block').each(function () {
      var $block = $(this);
      var qId    = $block.data('question-id');
      var isMandatory = $('[name="question_' + qId + '"]').length > 0 &&
                        $block.find('.required-star').length > 0;
      if (isMandatory) {
        var answered = $('[name="question_' + qId + '"]:checked').length > 0;
        if (!answered) {
          var qText = $.trim($block.find('.question-label').clone().children().remove().end().text());
          errors.push('Please answer: "' + qText + '"');
        }
      }
    });
    return errors;
  }

  /* ---- Retain button ---- */
  $btnRetain.on('click', function (e) {
    e.preventDefault();
    PollApp.Validator.hideSummary($summary);
    PollApp.UI.hideSuccess($success);

    // Clear previous inline errors
    $container.find('.question-inline-error').remove();
    $container.find('.question-label').removeClass('invalid-label');
    $container.find('.question-block').removeClass('is-invalid-block');

    var errors = [];
    var invalidBlocks = [];

    $container.find('.question-block').each(function () {
      var $block  = $(this);
      var qId     = $block.data('question-id');
      var $inputs = $('[name="question_' + qId + '"]');
      if ($inputs.length === 0) return;

      var mandatory = $block.find('.required-star').length > 0;
      if (!mandatory) return;

      var answered = $inputs.filter(':checked').length > 0;
      if (!answered) {
        var qText = $.trim($block.find('.question-label').clone().children().remove().end().text());
        errors.push('Please answer the required question: "' + qText + '"');
        $block.addClass('is-invalid-block');
        $block.find('.question-label').addClass('invalid-label');
        $block.append(
          '<div class="question-inline-error invalid-feedback">' +
          'This field is required.</div>'
        );
        invalidBlocks.push($block);
      }
    });

    if (errors.length > 0) {
      PollApp.Validator.showSummary($summary, errors);
      return;
    }

    // All valid — simulate AJAX submit
    $btnRetain.prop('disabled', true).text('Submitting...');
    setTimeout(function () {
      $btnRetain.prop('disabled', false).text('Retain');
      PollApp.UI.showSuccess($success, 'Thank you! Your vote has been submitted successfully.');
    }, 600);
  });

  /* ---- Init ---- */
  loadPoll();
});

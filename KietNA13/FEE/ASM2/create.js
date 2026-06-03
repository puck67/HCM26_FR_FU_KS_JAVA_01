/**
 * create.js — Create Interview page logic
 * - Dynamic add/remove question blocks
 * - Dynamic add/remove answer fields inside each question
 * - Full jQuery validation (required, minLen, maxLen)
 * - Error summary banner at top of form + inline red labels
 * - maxlength enforced on inputs to prevent overflow
 * Requires: app.js, jQuery, Bootstrap 4
 */

$(function () {
  /* ---- Validation rules ---- */
  var RULES = {
    namePoll:  { required: true, minLen: 3, maxLen: 255, label: 'Name poll' },
    question:  { required: true, minLen: 3, maxLen: 255, label: 'Question' },
    answer:    { required: true, minLen: 3, maxLen: 200, label: 'Answer' }
  };

  /* ---- DOM refs ---- */
  var $summary   = $('#create-validation-summary');
  var $success   = $('#create-success-banner');
  var $namePoll  = $('#input-poll-name');
  var $btnRetain = $('#btn-create-retain');
  var $btnAddQ   = $('#btn-add-question');
  var $questionsWrap = $('#questions-wrap');

  var questionCounter = 0;  // unique ID for question blocks

  /* ---- maxlength enforcement ---- */
  $namePoll.attr('maxlength', RULES.namePoll.maxLen);

  /* ---- Live validation on name poll field ---- */
  $namePoll.on('input', function () {
    var err = PollApp.Validator.validateField($(this).val(), RULES.namePoll);
    if (err) {
      PollApp.Validator.markInvalid($(this), err);
    } else {
      PollApp.Validator.clearInvalid($(this));
    }
  });

  /* ============================================================
     BUILD QUESTION BLOCK
     ============================================================ */
  function buildQuestionBlock(qNum) {
    var qId = ++questionCounter;
    var $card = $('<div class="question-card" data-qid="' + qId + '"></div>');

    /* Remove question button */
    var $removeBtn = $('<button type="button" class="remove-question-btn" title="Remove question">✕</button>');

    /* Question header */
    var $header = $('<div class="d-flex justify-content-between align-items-center mb-2"></div>');
    var $qLabel = $('<span style="font-weight:600;font-size:0.85rem;color:#555;">Question ' + qNum + '</span>');
    $header.append($qLabel, $removeBtn);

    /* Question text input */
    var $questionGroup = $('<div class="form-group mb-2"></div>');
    var qInputId = 'q-text-' + qId;
    var $qLabel2 = $('<label for="' + qInputId + '" class="small mb-1">Question text</label>');
    var $qInput  = $('<input type="text" class="form-control form-control-sm q-text-input">')
      .attr({ id: qInputId, placeholder: 'Enter your question', maxlength: RULES.question.maxLen });
    $questionGroup.append($qLabel2, $qInput);

    /* Checkboxes row */
    var $checksRow = $('<div class="form-row mb-2"></div>');
    var mandatoryId  = 'q-mandatory-'  + qId;
    var multipleId   = 'q-multiple-'   + qId;

    var $mandatoryWrap = $('<div class="col-auto form-check"></div>');
    var $mandatoryChk  = $('<input type="checkbox" class="form-check-input q-mandatory">').attr('id', mandatoryId);
    var $mandatoryLbl  = $('<label class="form-check-label small" style="cursor:pointer;">Mandatory</label>').attr('for', mandatoryId);
    $mandatoryWrap.append($mandatoryChk, $mandatoryLbl);

    var $multipleWrap  = $('<div class="col-auto form-check"></div>');
    var $multipleChk   = $('<input type="checkbox" class="form-check-input q-multiple">').attr('id', multipleId);
    var $multipleLbl   = $('<label class="form-check-label small" style="cursor:pointer;">You can select <strong>multiple</strong> options</label>').attr('for', multipleId);
    $multipleWrap.append($multipleChk, $multipleLbl);

    $checksRow.append($mandatoryWrap, $multipleWrap);

    /* Answers section */
    var $answersGroup = $('<div class="form-group mb-0"></div>');
    var $answersLabel = $('<label class="small mb-1">Possible answers</label>');
    var $answersList  = $('<div class="answers-list"></div>');
    var $addAnswerRow = $('<div class="mt-1 d-flex align-items-center gap-2"></div>');

    /* Add Answer button "+" */
    var $btnAddAnswer = $('<button type="button" class="btn btn-add-answer ml-auto" title="Add another answer">+</button>');
    $addAnswerRow.append($btnAddAnswer);
    $answersGroup.append($answersLabel, $answersList, $addAnswerRow);

    // Initial first answer row
    $answersList.append(buildAnswerRow(qId, true));

    /* Assemble card */
    $card.append($header, $questionGroup, $checksRow, $answersGroup);

    /* ---- Events for this card ---- */

    // Remove question card
    $removeBtn.on('click', function () {
      if ($questionsWrap.find('.question-card').length <= 1) {
        alert('You must have at least one question.');
        return;
      }
      $card.fadeOut(200, function () {
        $card.remove();
        reNumberQuestions();
      });
    });

    // Add answer row
    $btnAddAnswer.on('click', function () {
      var answerCount = $answersList.find('.answer-row').length;
      if (answerCount >= 10) {
        // prevent unbounded answers
        PollApp.UI.showSuccess($('<div id="tmp-warn"></div>').appendTo('body'), '');
        showToast('Maximum 10 answers per question.', 'warning');
        return;
      }
      var $newRow = buildAnswerRow(qId, false);
      $answersList.append($newRow);
      $newRow.find('input').focus();
    });

    // Live validation on question text
    $qInput.on('input', function () {
      var err = PollApp.Validator.validateField($(this).val(), RULES.question);
      if (err) PollApp.Validator.markInvalid($(this), err);
      else PollApp.Validator.clearInvalid($(this));
    });

    return $card;
  }

  /* ============================================================
     BUILD ANSWER ROW
     ============================================================ */
  function buildAnswerRow(qId, isFirst) {
    var $row     = $('<div class="answer-row"></div>');
    var $input   = $('<input type="text" class="form-control form-control-sm answer-input">')
      .attr({ placeholder: 'Type your answer', maxlength: RULES.answer.maxLen });
    var $removeBtn = $('<button type="button" class="remove-answer-btn" title="Remove answer">✕</button>');

    $row.append($input, $removeBtn);

    // Remove answer — keep at least one
    $removeBtn.on('click', function () {
      var $list = $row.closest('.answers-list');
      if ($list.find('.answer-row').length <= 1) {
        showToast('Each question must have at least one answer.', 'warning');
        return;
      }
      $row.remove();
    });

    // Live validation
    $input.on('input', function () {
      var err = PollApp.Validator.validateField($(this).val(), RULES.answer);
      if (err) PollApp.Validator.markInvalid($(this), err);
      else PollApp.Validator.clearInvalid($(this));
    });

    return $row;
  }

  /* ---- Renumber questions after removal ---- */
  function reNumberQuestions() {
    $questionsWrap.find('.question-card').each(function (idx) {
      $(this).find('.q-card-num').first().text('Question ' + (idx + 1));
    });
  }

  /* ---- Add Question button ---- */
  $btnAddQ.on('click', function () {
    var qCount = $questionsWrap.find('.question-card').length;
    if (qCount >= 20) {
      showToast('Maximum 20 questions per poll.', 'warning');
      return;
    }
    var $newCard = buildQuestionBlock(qCount + 1);
    $questionsWrap.append($newCard);
    $newCard.find('.q-text-input').focus();
    // Smooth scroll to new card
    $('html, body').animate({ scrollTop: $newCard.offset().top - 80 }, 300);
  });

  /* ============================================================
     VALIDATION — Full form
     ============================================================ */
  function validateForm() {
    var errors = [];
    var $allControls = $();

    PollApp.Validator.hideSummary($summary);
    PollApp.UI.hideSuccess($success);

    /* 1. Name poll */
    PollApp.Validator.clearInvalid($namePoll);
    var nameErr = PollApp.Validator.validateField($namePoll.val(), RULES.namePoll);
    if (nameErr) {
      errors.push(nameErr);
      PollApp.Validator.markInvalid($namePoll, nameErr);
    }

    /* 2. Each question block */
    $questionsWrap.find('.question-card').each(function (qIdx) {
      var $card   = $(this);
      var qNum    = qIdx + 1;
      var $qInput = $card.find('.q-text-input');

      // Clear previous
      PollApp.Validator.clearInvalid($qInput);
      var qErr = PollApp.Validator.validateField($qInput.val(), {
        required: true, minLen: RULES.question.minLen, maxLen: RULES.question.maxLen,
        label: 'Question ' + qNum
      });
      if (qErr) {
        errors.push(qErr);
        PollApp.Validator.markInvalid($qInput, qErr);
      }

      /* 3. Each answer in this question */
      $card.find('.answer-input').each(function (aIdx) {
        var $ans = $(this);
        PollApp.Validator.clearInvalid($ans);
        var aErr = PollApp.Validator.validateField($ans.val(), {
          required: true, minLen: RULES.answer.minLen, maxLen: RULES.answer.maxLen,
          label: 'Question ' + qNum + ', Answer ' + (aIdx + 1)
        });
        if (aErr) {
          errors.push(aErr);
          PollApp.Validator.markInvalid($ans, aErr);
        }
      });
    });

    return errors;
  }

  /* ============================================================
     RETAIN (Submit)
     ============================================================ */
  $btnRetain.on('click', function (e) {
    e.preventDefault();
    var errors = validateForm();

    if (errors.length > 0) {
      PollApp.Validator.showSummary($summary, errors);
      return;
    }

    // Collect data
    var pollData = {
      name: $.trim($namePoll.val()),
      status: 'active',
      questions: []
    };

    $questionsWrap.find('.question-card').each(function () {
      var $card   = $(this);
      var answers = [];
      $card.find('.answer-input').each(function () {
        var v = $.trim($(this).val());
        if (v) answers.push(v);
      });

      pollData.questions.push({
        text:           $.trim($card.find('.q-text-input').val()),
        mandatory:      $card.find('.q-mandatory').is(':checked'),
        multipleChoice: $card.find('.q-multiple').is(':checked'),
        answers:        answers
      });
    });

    // Simulate AJAX save (would be $.ajax POST to server)
    $btnRetain.prop('disabled', true).text('Saving...');

    setTimeout(function () {
      PollApp.Store.add(pollData);
      $btnRetain.prop('disabled', false).text('Retain');
      PollApp.UI.showSuccess($success, 'Poll "' + pollData.name + '" has been created successfully!');
      resetForm();
    }, 600);
  });

  /* ---- Reset form after success ---- */
  function resetForm() {
    $namePoll.val('');
    PollApp.Validator.clearInvalid($namePoll);
    $questionsWrap.empty();
    questionCounter = 0;
    $questionsWrap.append(buildQuestionBlock(1));
  }

  /* ============================================================
     TOAST helper (lightweight, no library)
     ============================================================ */
  function showToast(message, type) {
    var bg = type === 'warning' ? '#856404' : '#155724';
    var bdr = type === 'warning' ? '#ffc107' : '#28a745';
    var $toast = $(
      '<div style="position:fixed;bottom:1.5rem;right:1.5rem;z-index:9999;' +
      'background:#fff;border-left:4px solid ' + bdr + ';border-radius:4px;' +
      'padding:0.65rem 1rem;box-shadow:0 4px 12px rgba(0,0,0,.15);' +
      'font-size:0.82rem;color:' + bg + ';max-width:280px;">' +
      message + '</div>'
    );
    $('body').append($toast);
    setTimeout(function () { $toast.fadeOut(300, function () { $toast.remove(); }); }, 3000);
  }

  /* ---- Init — render first question block ---- */
  $questionsWrap.append(buildQuestionBlock(1));
});

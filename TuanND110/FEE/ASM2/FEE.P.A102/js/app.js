/**
 * FEE.P.A102 — dynamic questions/answers, Ajax delete, Ajax HTML load.
 */
(function ($) {
  'use strict';

  let questionCounter = 0;

  function sanitizeHtml(htmlString) {
    const $wrapper = $('<div></div>').html(htmlString);
    $wrapper.find('script, style, link, iframe, object, embed').remove();
    $wrapper.find('*').each(function () {
      const el = this;
      if (!el.attributes) {
        return;
      }
      $.each(el.attributes, function () {
        const attrName = (this.name || '').toLowerCase();
        if (attrName.indexOf('on') === 0) {
          $(el).removeAttr(this.name);
        }
      });
    });
    return $wrapper.html();
  }

  function extractBodyContent(htmlString) {
    const bodyMatch = htmlString.match(/<body[^>]*>([\s\S]*)<\/body>/i);
    const raw = bodyMatch ? bodyMatch[1] : htmlString;
    return sanitizeHtml(raw);
  }

  function buildAnswerRow(questionIndex, answerIndex, showRemove, isFirst) {
    const inputId = 'answer-' + questionIndex + '-' + answerIndex;
    const placeholder = isFirst ? 'Possible answers' : 'Type your answer';
    const removeBtn = showRemove
      ? '<button type="button" class="button answer-row__remove remove-answer-btn" aria-label="Remove answer">&minus;</button>'
      : '';

    return (
      '<div class="poll-form__row answer-row" data-answer-index="' +
      answerIndex +
      '">' +
      '<input type="text" id="' +
      inputId +
      '" name="questions[' +
      questionIndex +
      '][answers][]" class="form-control poll-form__input poll-form__input--grow answer-text" placeholder="' +
      placeholder +
      '" required minlength="3" maxlength="200" data-answer-input data-min="3" data-max="200" aria-required="true"/>' +
      '<button type="button" class="button button--add-answer" aria-label="Add answer">+</button>' +
      removeBtn +
      '</div>'
    );
  }

  function buildQuestionBlock(index) {
    const removeQuestionBtn =
      index > 0
        ? '<div class="form-group row"><div class="col-sm-9 offset-sm-3">' +
          '<button type="button" class="button poll-question-block__remove remove-question-btn">Remove question</button></div></div>'
        : '';

    return (
      '<div class="poll-form__section poll-question-block" data-question-index="' +
      index +
      '">' +
      removeQuestionBtn +
      '<div class="form-group row">' +
      '<label for="question-' +
      index +
      '" class="col-sm-3 col-form-label form-label--required">Your question</label>' +
      '<div class="col-sm-9">' +
      '<input type="text" id="question-' +
      index +
      '" name="questions[' +
      index +
      '][text]" class="form-control poll-form__input question-text" placeholder="Enter your question" required minlength="3" maxlength="255" data-question-input data-min="3" data-max="255" aria-required="true"/>' +
      '<span class="field-error" data-for="question-' +
      index +
      '"></span></div></div>' +
      '<div class="form-group row"><div class="col-sm-9 offset-sm-3">' +
      '<div class="form-check poll-form__option"><input type="checkbox" class="form-check-input" id="mandatory-' +
      index +
      '" name="questions[' +
      index +
      '][mandatory]"/>' +
      '<label class="form-check-label" for="mandatory-' +
      index +
      '">Mandatory</label></div>' +
      '<div class="form-check poll-form__option"><input type="checkbox" class="form-check-input" id="multiple-' +
      index +
      '" name="questions[' +
      index +
      '][multiple]"/>' +
      '<label class="form-check-label" for="multiple-' +
      index +
      '">You can select multiple options</label></div></div></div>' +
      '<div class="form-group row">' +
      '<span class="col-sm-3 col-form-label form-label--required">Possible answers</span>' +
      '<div class="col-sm-9"><div class="poll-form__answers" data-answers-container">' +
      buildAnswerRow(index, 0, false, true) +
      '</div><span class="field-error" data-for="answers-' +
      index +
      '"></span></div></div></div>'
    );
  }

  function reindexQuestions() {
    $('#questionsContainer .poll-question-block').each(function (i) {
      const $block = $(this);
      $block.attr('data-question-index', i);
      $block.find('[data-question-input]').attr({
        id: 'question-' + i,
        name: 'questions[' + i + '][text]'
      });
      $block.find('label[for^="question-"]').attr('for', 'question-' + i);
      $block.find('[data-for^="question-"]').attr('data-for', 'question-' + i);
      $block.find('[id^="mandatory-"]').attr({ id: 'mandatory-' + i, name: 'questions[' + i + '][mandatory]' });
      $block.find('label[for^="mandatory-"]').attr('for', 'mandatory-' + i);
      $block.find('[id^="multiple-"]').attr({ id: 'multiple-' + i, name: 'questions[' + i + '][multiple]' });
      $block.find('label[for^="multiple-"]').attr('for', 'multiple-' + i);
      $block.find('.field-error[data-for^="answers-"]').attr('data-for', 'answers-' + i);

      $block.find('.answer-row').each(function (j) {
        $(this).attr('data-answer-index', j);
        const $input = $(this).find('[data-answer-input]');
        $input.attr({
          id: 'answer-' + i + '-' + j,
          name: 'questions[' + i + '][answers][]'
        });
        $input.attr('placeholder', j > 0 ? 'Type your answer' : 'Possible answers');
        $(this).find('.remove-answer-btn').toggle(j > 0);
      });
    });
    questionCounter = $('#questionsContainer .poll-question-block').length - 1;
  }

  function initCreatePage() {
    const $container = $('#questionsContainer');
    if (!$container.length) {
      return;
    }

    questionCounter = $container.find('.poll-question-block').length - 1;

    $('#addQuestionBtn').on('click', function () {
      questionCounter += 1;
      $container.append(buildQuestionBlock(questionCounter));
      reindexQuestions();
    });

    $container.on('click', '.button--add-answer', function () {
      const $block = $(this).closest('.poll-question-block');
      const qIndex = $block.data('question-index');
      const $answersContainer = $block.find('[data-answers-container]');
      const nextIndex = $answersContainer.find('.answer-row').length;
      $answersContainer.append(buildAnswerRow(qIndex, nextIndex, true, false));
      reindexQuestions();
    });

    $container.on('click', '.remove-answer-btn', function () {
      const $block = $(this).closest('.poll-question-block');
      if ($block.find('.answer-row').length <= 1) {
        return;
      }
      $(this).closest('.answer-row').remove();
      reindexQuestions();
    });

    $container.on('click', '.remove-question-btn', function () {
      $(this).closest('.poll-question-block').remove();
      reindexQuestions();
    });
  }

  function requestDelete(pollId) {
    return $.ajax({
      url: 'api/delete.php',
      type: 'DELETE',
      dataType: 'json',
      data: { id: pollId }
    }).fail(function () {
      return $.ajax({
        url: 'api/delete.php',
        type: 'POST',
        dataType: 'json',
        data: { id: pollId }
      });
    }).fail(function () {
      return $.ajax({
        url: 'mock/delete.json',
        type: 'GET',
        dataType: 'json'
      });
    });
  }

  function showListMessage(text, isError) {
    const $message = $('#listAjaxMessage');
    $message
      .removeAttr('hidden')
      .toggleClass('alert-success', !isError)
      .toggleClass('alert-danger', !!isError)
      .text(text);
  }

  function initListPage() {
    const $tbody = $('#pollTableBody');
    if (!$tbody.length) {
      return;
    }

    $tbody.on('click', '.delete-btn', function () {
      const $row = $(this).closest('tr');
      const pollId = $(this).data('poll-id');

      if (!window.confirm('Are you sure you want to delete this item?')) {
        return;
      }

      requestDelete(pollId)
        .done(function (response) {
          if (response && response.success) {
            $row.fadeOut(300, function () {
              $(this).remove();
            });
            showListMessage(response.message || 'Item deleted successfully.', false);
            return;
          }
          showListMessage('Delete failed. Please try again.', true);
        })
        .fail(function () {
          showListMessage('Could not reach the server. Run: php -S localhost:8080', true);
        });
    });

    $('.view-results-btn').on('click', function () {
      const $btn = $(this);
      const pollId = $btn.data('poll-id');

      $btn.attr('data-loading', 'true');

      $.ajax({
        url: 'mock/poll-detail.html',
        type: 'GET',
        dataType: 'html'
      })
        .done(function (html) {
          $('#pollDetailContent').html(extractBodyContent(html));
          $('#pollDetailPanel').removeAttr('hidden').attr('data-loaded-poll', pollId);
          $('html, body').animate({ scrollTop: $('#pollDetailPanel').offset().top - 20 }, 300);
        })
        .always(function () {
          $btn.removeAttr('data-loading');
        });
    });

    $('.poll-list__tab').on('click', function () {
      $('.poll-list__tab').removeClass('poll-list__tab--active');
      $(this).addClass('poll-list__tab--active');
    });
  }

  $(document).ready(function () {
    initCreatePage();
    initListPage();
  });
})(jQuery);

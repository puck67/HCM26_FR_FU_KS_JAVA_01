/**
 * Form validation — PDF spec: loop fields, min/max length, errors at top.
 */
(function ($) {
  'use strict';

  const RULES = {
    pollName: { label: 'Name poll', min: 3, max: 255 },
    question: { label: 'Your question', min: 3, max: 255 },
    answer: { label: 'Answer', min: 3, max: 200 }
  };

  function getFieldLabel($field) {
    const id = $field.attr('id');
    if (id === 'pollName') {
      return RULES.pollName.label;
    }
    if ($field.is('[data-question-input]')) {
      return RULES.question.label;
    }
    if ($field.is('[data-answer-input]')) {
      return RULES.answer.label;
    }
    if (id === 'loginAlias') {
      return 'Alias';
    }
    if (id === 'loginPassword') {
      return 'Password';
    }
    return 'Field';
  }

  function clearValidationState($form) {
    $form.find('.poll-form__input--invalid').removeClass('poll-form__input--invalid');
    $form.find('.form-label--invalid').removeClass('form-label--invalid');
    $form.find('.poll-question--invalid').removeClass('poll-question--invalid');
    $form.find('.field-error').text('');
  }

  function markFieldInvalid($field, message) {
    $field.addClass('poll-form__input--invalid');
    const id = $field.attr('id');
    if (id) {
      $('label[for="' + id + '"]').addClass('form-label--invalid');
      $('[data-for="' + id + '"]').text(message);
    }
  }

  function validateFieldLength($field) {
    const value = ($field.val() || '').trim();
    const min = parseInt($field.attr('data-min') || $field.attr('minlength'), 10);
    const max = parseInt($field.attr('data-max') || $field.attr('maxlength'), 10);
    const label = getFieldLabel($field);

    if (!value) {
      return label + ' is required.';
    }
    if (!isNaN(min) && value.length < min) {
      return label + ' must be at least ' + min + ' characters.';
    }
    if (!isNaN(max) && value.length > max) {
      return label + ' must not exceed ' + max + ' characters.';
    }
    return null;
  }

  function collectEmptyFields($form) {
    const errors = [];
    const invalidFields = [];

    $form.find('input, textarea, select').each(function () {
      const $field = $(this);
      const type = ($field.attr('type') || '').toLowerCase();

      if (type === 'button' || type === 'submit' || type === 'hidden' || type === 'checkbox') {
        return;
      }
      if (type === 'radio') {
        return;
      }

      const isRequired =
        $field.prop('required') ||
        $field.is('[data-question-input], [data-answer-input], #pollName, #loginAlias, #loginPassword');

      if (!isRequired) {
        return;
      }

      const lengthError = validateFieldLength($field);
      if (lengthError) {
        errors.push(lengthError);
        invalidFields.push({ $field, message: lengthError });
      }
    });

    return { errors, invalidFields };
  }

  function showFormErrors($errorBox, errors) {
    if (!errors.length) {
      return;
    }

    const uniqueErrors = [...new Set(errors)];
    const $list = $('<ul></ul>');
    uniqueErrors.forEach(function (msg) {
      $list.append($('<li></li>').text(msg));
    });

    $errorBox
      .removeAttr('hidden')
      .removeClass('form-success page-alerts--success')
      .addClass('form-errors page-alerts--error')
      .html(
        $('<p class="form-errors__title"></p>').text(
          'Please correct the following errors before continuing:'
        )
      )
      .append($list);

    if ($errorBox.offset()) {
      $('html, body').animate({ scrollTop: $errorBox.offset().top - 20 }, 200);
    }
  }

  function validatePollForm($form) {
    clearValidationState($form);

    const $errorBox = $('#formErrors');
    const $successBox = $('#formSuccess');
    $errorBox.attr('hidden', true).empty();
    $successBox.attr('hidden', true).empty();

    const { errors, invalidFields } = collectEmptyFields($form);

    $form.find('.poll-question-block').each(function () {
      const $block = $(this);
      const qIndex = $block.data('question-index');
      const $questionInput = $block.find('[data-question-input]').first();
      const $answers = $block.find('[data-answer-input]');
      let hasAnswer = false;

      $answers.each(function () {
        if (($(this).val() || '').trim()) {
          hasAnswer = true;
        }
      });

      if ($questionInput.length && ($questionInput.val() || '').trim() && !hasAnswer) {
        const msg = 'Each question must have at least one answer.';
        errors.push(msg);
        $('[data-for="answers-' + qIndex + '"]').text(msg);
        $answers.first().addClass('poll-form__input--invalid');
        $block.find('.form-label--required').last().addClass('form-label--invalid');
      }
    });

    invalidFields.forEach(function (item) {
      markFieldInvalid(item.$field, item.message);
    });

    if (errors.length) {
      showFormErrors($errorBox, errors);
      return false;
    }

    return true;
  }

  function validateVoteForm($form) {
    clearValidationState($form);

    const $errorBox = $('#voteFormErrors');
    const $successBox = $('#voteFormSuccess');
    $errorBox.attr('hidden', true).empty();
    $successBox.attr('hidden', true).empty();

    const errors = [];

    if (!$form.find('input[name="necessary"]:checked').length) {
      errors.push('Question 1 (it is necessary to you?) requires an answer.');
      $('#questionNecessary').addClass('poll-question--invalid');
      $('[data-for="necessary"]').text('Please select Yes or No.');
    }

    if (errors.length) {
      showFormErrors($errorBox, errors);
      return false;
    }

    $successBox.removeAttr('hidden').text('Your vote was submitted successfully.');
    return true;
  }

  function validateLoginForm($form) {
    clearValidationState($form);
    const $errorBox = $('#loginFormErrors');
    $errorBox.attr('hidden', true).empty();

    const { errors, invalidFields } = collectEmptyFields($form);
    invalidFields.forEach(function (item) {
      markFieldInvalid(item.$field, item.message);
    });

    if (errors.length) {
      showFormErrors($errorBox, errors);
      return false;
    }

    return true;
  }

  function submitPollToServer($form, $submit) {
    $.ajax({
      url: $form.attr('action') || 'api/validate-poll.php',
      type: 'POST',
      dataType: 'json',
      data: $form.serialize()
    })
      .done(function (response) {
        if (response && response.success) {
          $('#formSuccess')
            .removeAttr('hidden')
            .text(response.message || 'The form was completed successfully.');
          $('#formErrors').attr('hidden', true).empty();
          return;
        }
        const serverErrors = response && response.errors ? response.errors : [response.message];
        showFormErrors($('#formErrors'), serverErrors);
      })
      .fail(function (xhr) {
        let serverErrors = ['Server validation could not be reached. Run: php -S localhost:8080'];
        if (xhr.responseJSON && xhr.responseJSON.errors) {
          serverErrors = xhr.responseJSON.errors;
        }
        showFormErrors($('#formErrors'), serverErrors);
      })
      .always(function () {
        $submit.prop('disabled', false);
      });
  }

  function submitLoginToServer($form) {
    $.ajax({
      url: 'api/login.php',
      type: 'POST',
      dataType: 'json',
      data: $form.serialize()
    })
      .done(function (response) {
        if (response && response.success) {
          $('#loginFormErrors')
            .removeAttr('hidden')
            .removeClass('form-errors')
            .addClass('form-success')
            .text(response.message || 'Login successful.');
          setTimeout(function () {
            $('#loginModal').modal('hide');
            $('#loginFormErrors').attr('hidden', true).removeClass('form-success').addClass('form-errors');
            $form[0].reset();
          }, 800);
          return;
        }
        const errs = response && response.errors ? response.errors : [response.message];
        showFormErrors($('#loginFormErrors'), errs);
      })
      .fail(function (xhr) {
        if (xhr.responseJSON && xhr.responseJSON.errors) {
          showFormErrors($('#loginFormErrors'), xhr.responseJSON.errors);
          return;
        }
        if (!validateLoginForm($form)) {
          return;
        }
        showFormErrors($('#loginFormErrors'), ['Cannot reach api/login.php. Use PHP server.']);
      });
  }

  $(document).ready(function () {
    $('#poll-form').on('submit', function (e) {
      e.preventDefault();
      const $form = $(this);
      if (!validatePollForm($form)) {
        return;
      }
      const $submit = $form.find('[type="submit"]');
      $submit.prop('disabled', true);
      submitPollToServer($form, $submit);
    });

    $('#voteForm').on('submit', function (e) {
      e.preventDefault();
      validateVoteForm($(this));
    });

    $('#loginForm').on('submit', function (e) {
      e.preventDefault();
      const $form = $(this);
      if (!validateLoginForm($form)) {
        return;
      }
      submitLoginToServer($form);
    });

    $('#loginModal').on('hidden.bs.modal', function () {
      const $form = $('#loginForm');
      $form[0].reset();
      $('#loginFormErrors').attr('hidden', true).empty();
      clearValidationState($form);
    });

    $(document).on('input change', '#poll-form input, #loginForm input, #voteForm input', function () {
      const $field = $(this);
      $field.removeClass('poll-form__input--invalid');
      const id = $field.attr('id');
      if (id) {
        $('label[for="' + id + '"]').removeClass('form-label--invalid');
        $('[data-for="' + id + '"]').text('');
      }
      if ($field.attr('name') === 'necessary') {
        $('#questionNecessary').removeClass('poll-question--invalid');
        $('[data-for="necessary"]').text('');
      }
    });
  });
})(jQuery);

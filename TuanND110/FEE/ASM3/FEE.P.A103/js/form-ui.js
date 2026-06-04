/**
 * Form feedback UI — classList only, no inline styles (project standards)
 */
(function (window, $) {
  'use strict';

  const INVALID_INPUT = 'visitor-form__input--invalid';
  const INVALID_LABEL = 'visitor-form__label--invalid';
  const INVALID_GROUP = 'visitor-form__group--invalid';

  function getErrorEl(fieldId) {
    return $('[data-error-for="' + fieldId + '"]');
  }

  function clearFieldState($field) {
    $field.removeClass(INVALID_INPUT).removeAttr('aria-invalid');
    const id = $field.attr('id');
    if (id) {
      $('label[for="' + id + '"]').removeClass(INVALID_LABEL);
      getErrorEl(id).text('').attr('hidden', 'hidden');
    }
  }

  function clearGroupState(groupName) {
    $('[data-group="' + groupName + '"]').removeClass(INVALID_GROUP);
    getErrorEl(groupName).text('').attr('hidden', 'hidden');
  }

  function clearFormState($form) {
    $form.find('.visitor-form__input').each(function () {
      clearFieldState($(this));
    });
    clearGroupState('region');
  }

  function setFieldError($field, message) {
    $field.addClass(INVALID_INPUT).attr('aria-invalid', 'true');
    const id = $field.attr('id');
    if (!id) {
      return;
    }
    $('label[for="' + id + '"]').addClass(INVALID_LABEL);
    getErrorEl(id).text(message).removeAttr('hidden').attr('aria-hidden', 'false');
  }

  function setGroupError(groupName, message) {
    $('[data-group="' + groupName + '"]').addClass(INVALID_GROUP);
    getErrorEl(groupName).text(message).removeAttr('hidden').attr('aria-hidden', 'false');
  }

  function applyFieldErrors(fieldErrors) {
    Object.keys(fieldErrors).forEach(function (fieldName) {
      const message = fieldErrors[fieldName];
      if (fieldName === 'region') {
        setGroupError('region', message);
        return;
      }
      const $field = $('#' + fieldName);
      if ($field.length) {
        setFieldError($field, message);
      }
    });
  }

  function showSummary($box, errors) {
    const unique = [...new Set(errors)];
    const $list = $('<ul class="visitor-form__errors-list"></ul>');
    unique.forEach(function (msg) {
      $list.append($('<li></li>').text(msg));
    });

    $box
      .removeAttr('hidden')
      .html(
        $('<p class="visitor-form__errors-title"></p>').text(
          'Please correct the following errors before continuing:'
        )
      )
      .append($list);

    if ($box.offset()) {
      $('html, body').animate({ scrollTop: $box.offset().top - 20 }, 200);
    }
  }

  function hideSummary($box) {
    $box.attr('hidden', true).empty();
  }

  function showSuccess($box, message) {
    $box.removeAttr('hidden').text(message);
  }

  function hideSuccess($box) {
    $box.attr('hidden', true).empty();
  }

  function updateCharCounter($textarea) {
    const max = parseInt($textarea.attr('maxlength'), 10) || 200;
    const len = ($textarea.val() || '').length;
    const counterId = $textarea.attr('aria-describedby');
    if (counterId) {
      $('#' + counterId.split(' ').pop()).text(len + ' / ' + max);
    }
  }

  window.VisitorFormUI = {
    clearFormState: clearFormState,
    clearFieldState: clearFieldState,
    clearGroupState: clearGroupState,
    setFieldError: setFieldError,
    setGroupError: setGroupError,
    applyFieldErrors: applyFieldErrors,
    showSummary: showSummary,
    hideSummary: hideSummary,
    showSuccess: showSuccess,
    hideSuccess: hideSuccess,
    updateCharCounter: updateCharCounter
  };
})(window, jQuery);

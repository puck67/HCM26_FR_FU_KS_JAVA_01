/**
 * person.html — Register validation with blur/input + submit (FEE.P.A103)
 */
(function ($) {
  'use strict';

  const V = window.VisitorValidators;
  const UI = window.VisitorFormUI;

  function validateSingleField($field) {
    const fieldName = $field.attr('id');
    if (!fieldName || !V) {
      return true;
    }

    let raw = $field.val();
    if (fieldName === 'gender') {
      raw = $field.val();
    }

    const result = V.validateFieldByName(fieldName, raw);
    if (!result.valid) {
      UI.setFieldError($field, result.message);
      return false;
    }
    UI.clearFieldState($field);
    return true;
  }

  function validateRegionGroup($form) {
    const region = $form.find('input[name="region"]:checked').val() || '';
    const result = V.validateFieldByName('region', region);
    if (!result.valid) {
      UI.setGroupError('region', result.message);
      return false;
    }
    UI.clearGroupState('region');
    return true;
  }

  function handleBlur(e) {
    const $field = $(e.target);
    if ($field.is('input[name="region"]')) {
      validateRegionGroup($('#visitorForm'));
      return;
    }
    if (!$field.is('.visitor-form__input[data-validate]')) {
      return;
    }
    validateSingleField($field);
  }

  function handleInput(e) {
    const $field = $(e.target);
    if ($field.is('#description')) {
      UI.updateCharCounter($field);
    }
    if ($field.is('.visitor-form__input')) {
      if ($field.is('input[name="region"]')) {
        UI.clearGroupState('region');
      } else {
        UI.clearFieldState($field);
      }
    }
  }

  function buildPayloadFromForm($form) {
    const hobbies = [];
    $form.find('input[name="hobbies"]:checked').each(function () {
      hobbies.push($(this).val());
    });

    return {
      firstName: $form.find('#firstName').val(),
      lastName: $form.find('#lastName').val(),
      gender: $form.find('#gender').val(),
      telephone: $form.find('#telephone').val(),
      email: $form.find('#email').val(),
      region: $form.find('input[name="region"]:checked').val() || '',
      hobbies: hobbies,
      description: $form.find('#description').val()
    };
  }

  function collectFormErrors($form) {
    return V.validatePersonPayload(buildPayloadFromForm($form));
  }

  function handleSubmit(e) {
    e.preventDefault();
    const $form = $(this);
    const $submit = $form.find('[type="submit"]');
    const $errors = $('#formErrors');
    const $success = $('#formSuccess');

    UI.hideSummary($errors);
    UI.hideSuccess($success);
    UI.clearFormState($form);

    $submit.prop('disabled', true).addClass('visitor-btn--disabled');

    const result = collectFormErrors($form);

    if (!result.valid) {
      UI.applyFieldErrors(result.fieldErrors);
      UI.showSummary($errors, result.errors);
      $submit.prop('disabled', false).removeClass('visitor-btn--disabled');
      return;
    }

    if (window.VisitorData) {
      window.VisitorData.addVisitor(result.data);
    }

    UI.showSuccess(
      $success,
      'Visitor registered successfully. You can search on the Search page.'
    );

    $form[0].reset();
    $form.find('#gender').val('Male');
    UI.updateCharCounter($form.find('#description'));
    $submit.prop('disabled', false).removeClass('visitor-btn--disabled');
  }

  $(document).ready(function () {
    const $form = $('#visitorForm');
    if (!$form.length || !V || !UI) {
      return;
    }

    UI.updateCharCounter($form.find('#description'));

    $form.on('submit', handleSubmit);
    $form.on('blur', '.visitor-form__input[data-validate], input[name="region"]', handleBlur);
    $form.on('input change', '.visitor-form__input, input[name="region"]', handleInput);
  });
})(jQuery);

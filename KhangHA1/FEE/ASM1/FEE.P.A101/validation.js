/**
 * validation.js — Shared jQuery form validation for ASM1 CMS
 * Assignment: Front-End Essentials
 */

$(function () {

  /* ============================================================
     HELPERS
     ============================================================ */

  /**
   * Mark a field as invalid and show message.
   * @param {jQuery} $field
   * @param {string} msg
   */
  function setInvalid($field, msg) {
    $field.addClass('is-invalid').removeClass('is-valid');
    var $fb = $field.siblings('.invalid-feedback');
    if ($fb.length === 0) {
      $fb = $('<div class="invalid-feedback"></div>').insertAfter($field);
    }
    $fb.text(msg).show();
  }

  /**
   * Mark a field as valid.
   * @param {jQuery} $field
   */
  function setValid($field) {
    $field.removeClass('is-invalid').addClass('is-valid');
    $field.siblings('.invalid-feedback').hide();
  }

  /**
   * Validate a single field by rules.
   * rules: { required, minLen, maxLen, isEmail, matchField, matchLabel }
   * Returns true if valid.
   */
  function validateField($field, rules) {
    var val = $field.val().trim();
    var label = $field.data('label') || $field.attr('placeholder') || 'Field';

    if (rules.required && val === '') {
      setInvalid($field, label + ' is required.');
      return false;
    }

    if (val !== '') {
      if (rules.isEmail) {
        var emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRe.test(val)) {
          setInvalid($field, 'Please enter a valid email address.');
          return false;
        }
      }

      if (rules.minLen && val.length < rules.minLen) {
        setInvalid($field, label + ' must be at least ' + rules.minLen + ' characters.');
        return false;
      }

      if (rules.maxLen && val.length > rules.maxLen) {
        setInvalid($field, label + ' must not exceed ' + rules.maxLen + ' characters.');
        return false;
      }

      if (rules.matchField) {
        var matchVal = $(rules.matchField).val().trim();
        if (val !== matchVal) {
          setInvalid($field, (rules.matchLabel || 'Fields') + ' do not match.');
          return false;
        }
      }
    }

    setValid($field);
    return true;
  }

  /* ============================================================
     LOGIN PAGE  (#login-form)
     ============================================================ */
  $('#login-form').on('submit', function (e) {
    e.preventDefault();
    var ok = true;

    ok = validateField($('#login-email'), {
      required: true, minLen: 5, maxLen: 50, isEmail: true
    }) && ok;

    ok = validateField($('#login-password'), {
      required: true, minLen: 8, maxLen: 30
    }) && ok;

    if (ok) {
      // Simulate successful login — redirect to cms.html
      window.location.href = 'cms.html';
    }
  });

  // Live validation on blur
  $('#login-email').on('blur', function () {
    validateField($(this), { required: true, minLen: 5, maxLen: 50, isEmail: true });
  });
  $('#login-password').on('blur', function () {
    validateField($(this), { required: true, minLen: 8, maxLen: 30 });
  });

  /* ============================================================
     REGISTER PAGE  (#register-form)
     ============================================================ */
  $('#register-form').on('submit', function (e) {
    e.preventDefault();
    var ok = true;

    ok = validateField($('#reg-username'), {
      required: true, minLen: 3, maxLen: 30
    }) && ok;

    ok = validateField($('#reg-email'), {
      required: true, minLen: 5, isEmail: true
    }) && ok;

    ok = validateField($('#reg-password'), {
      required: true, minLen: 8, maxLen: 30
    }) && ok;

    ok = validateField($('#reg-repassword'), {
      required: true, minLen: 8, maxLen: 30,
      matchField: '#reg-password', matchLabel: 'Passwords'
    }) && ok;

    if (ok) {
      // Show success and redirect to login
      alert('Registration successful! Please log in.');
      window.location.href = 'login.html';
    }
  });

  $('#reg-username').on('blur', function () {
    validateField($(this), { required: true, minLen: 3, maxLen: 30 });
  });
  $('#reg-email').on('blur', function () {
    validateField($(this), { required: true, minLen: 5, isEmail: true });
  });
  $('#reg-password').on('blur', function () {
    validateField($(this), { required: true, minLen: 8, maxLen: 30 });
  });
  $('#reg-repassword').on('blur', function () {
    validateField($(this), {
      required: true, minLen: 8, maxLen: 30,
      matchField: '#reg-password', matchLabel: 'Passwords'
    });
  });

  /* ============================================================
     EDIT PROFILE FORM  (#profile-form)  — AJAX submit
     ============================================================ */
  $(document).on('submit', '#profile-form', function (e) {
    e.preventDefault();
    var ok = true;

    ok = validateField($('#prof-firstname'), { required: true, minLen: 3, maxLen: 30 }) && ok;
    ok = validateField($('#prof-lastname'),  { required: true, minLen: 3, maxLen: 30 }) && ok;
    ok = validateField($('#prof-email'),     { required: false, isEmail: true })         && ok;
    ok = validateField($('#prof-phone'),     { required: true, minLen: 9, maxLen: 13 })  && ok;
    ok = validateField($('#prof-desc'),      { required: false, maxLen: 200 })            && ok;

    if (!ok) return;

    // Simulate AJAX update
    var $btn    = $(this).find('[type="submit"]');
    var $alert  = $('#profile-alert');
    $btn.prop('disabled', true).text('Saving...');

    setTimeout(function () {
      $btn.prop('disabled', false).text('Submit Button');
      $alert
        .removeClass('alert-danger')
        .addClass('alert alert-success ajax-alert')
        .text('✓ Profile updated successfully!')
        .fadeIn(300)
        .delay(3000)
        .fadeOut(400);
    }, 1200);
  });

  // Live blur for profile
  $(document).on('blur', '#prof-firstname', function () {
    validateField($(this), { required: true, minLen: 3, maxLen: 30 });
  });
  $(document).on('blur', '#prof-lastname', function () {
    validateField($(this), { required: true, minLen: 3, maxLen: 30 });
  });
  $(document).on('blur', '#prof-email', function () {
    if ($(this).val().trim()) validateField($(this), { isEmail: true });
  });
  $(document).on('blur', '#prof-phone', function () {
    validateField($(this), { required: true, minLen: 9, maxLen: 13 });
  });
  $(document).on('blur', '#prof-desc', function () {
    validateField($(this), { required: false, maxLen: 200 });
  });

  // Reset profile form
  $(document).on('click', '#profile-reset', function () {
    var $form = $('#profile-form');
    $form[0].reset();
    $form.find('.form-control').removeClass('is-invalid is-valid');
    $form.find('.invalid-feedback').hide();
    $('#profile-alert').hide();
  });

  /* ============================================================
     ADD CONTENT FORM  (#content-form)
     ============================================================ */
  $(document).on('submit', '#content-form', function (e) {
    e.preventDefault();
    var ok = true;

    ok = validateField($('#cont-title'),   { required: true, minLen: 10, maxLen: 200 }) && ok;
    ok = validateField($('#cont-brief'),   { required: true, minLen: 30, maxLen: 150 }) && ok;
    ok = validateField($('#cont-content'), { required: true, minLen: 50, maxLen: 1000 }) && ok;

    if (!ok) return;

    // Save to localStorage and refresh view-contents table
    var entries = JSON.parse(localStorage.getItem('cms_entries') || '[]');
    entries.push({
      title:   $('#cont-title').val().trim(),
      brief:   $('#cont-brief').val().trim(),
      content: $('#cont-content').val().trim(),
      date:    new Date().toLocaleDateString('en-GB', {
                 day: '2-digit', month: '2-digit', year: 'numeric',
                 hour: '2-digit', minute: '2-digit'
               })
    });
    localStorage.setItem('cms_entries', JSON.stringify(entries));

    // Show success
    var $alert = $('#content-alert');
    $alert
      .removeClass('alert-danger')
      .addClass('alert alert-success ajax-alert')
      .text('✓ Content added successfully!')
      .fadeIn(300)
      .delay(3000)
      .fadeOut(400);

    // Reset form
    this.reset();
    $(this).find('.form-control').removeClass('is-invalid is-valid');
    $(this).find('.invalid-feedback').hide();
  });

  $(document).on('blur', '#cont-title', function () {
    validateField($(this), { required: true, minLen: 10, maxLen: 200 });
  });
  $(document).on('blur', '#cont-brief', function () {
    validateField($(this), { required: true, minLen: 30, maxLen: 150 });
  });
  $(document).on('blur', '#cont-content', function () {
    validateField($(this), { required: true, minLen: 50, maxLen: 1000 });
  });

  // Reset content form
  $(document).on('click', '#content-reset', function () {
    var $form = $('#content-form');
    $form[0].reset();
    $form.find('.form-control').removeClass('is-invalid is-valid');
    $form.find('.invalid-feedback').hide();
    $('#content-alert').hide();
  });

});

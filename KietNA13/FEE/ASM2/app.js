/**
 * app.js — Shared utilities, navbar active link, Login modal
 * Polls Web Application
 * Requires: jQuery 3.x, Bootstrap 4
 */

/* ============================================================
   Namespace / Module Pattern — reusable across pages
   ============================================================ */
var PollApp = PollApp || {};

/* -------- PollApp.Store — localStorage wrapper -------- */
PollApp.Store = (function () {
  var KEY = 'pollapp_polls';
  var _nextId = null;

  function _load() {
    try {
      return JSON.parse(localStorage.getItem(KEY)) || [];
    } catch (e) {
      return [];
    }
  }

  function _save(data) {
    localStorage.setItem(KEY, JSON.stringify(data));
  }

  function _getNextId() {
    if (_nextId === null) {
      var polls = _load();
      _nextId = polls.length > 0
        ? Math.max.apply(null, polls.map(function (p) { return p.id; })) + 1
        : 1;
    }
    return _nextId++;
  }

  return {
    getAll: function () { return _load(); },

    getByStatus: function (status) {
      return _load().filter(function (p) { return p.status === status; });
    },

    getById: function (id) {
      return _load().find(function (p) { return p.id === id; }) || null;
    },

    add: function (poll) {
      var polls = _load();
      poll.id = _getNextId();
      poll.createdAt = new Date().toISOString();
      polls.push(poll);
      _save(polls);
      return poll;
    },

    updateStatus: function (id, status) {
      var polls = _load();
      var found = false;
      polls = polls.map(function (p) {
        if (p.id === id) { p.status = status; found = true; }
        return p;
      });
      if (found) _save(polls);
      return found;
    },

    remove: function (id) {
      var polls = _load();
      var filtered = polls.filter(function (p) { return p.id !== id; });
      _save(filtered);
      return filtered.length < polls.length;
    },

    seed: function () {
      // Insert demo data if store is empty
      if (_load().length === 0) {
        PollApp.Store.add({
          name: 'Great poll',
          status: 'active',
          questions: [
            {
              text: 'it is necessary to you?',
              mandatory: true,
              multipleChoice: false,
              answers: ['Yes', 'No']
            },
            {
              text: 'Often pass polls?',
              mandatory: false,
              multipleChoice: false,
              answers: ['Once a month', 'Once a week']
            },
            {
              text: 'How old are you?',
              mandatory: false,
              multipleChoice: true,
              answers: ['18-20', '21-23']
            }
          ]
        });
        PollApp.Store.add({
          name: 'Draft Survey',
          status: 'draft',
          questions: [
            {
              text: 'Sample question?',
              mandatory: false,
              multipleChoice: false,
              answers: ['Option A', 'Option B']
            }
          ]
        });
        PollApp.Store.add({
          name: 'Old Feedback',
          status: 'closed',
          questions: [
            {
              text: 'Was it useful?',
              mandatory: true,
              multipleChoice: false,
              answers: ['Yes', 'No', 'Maybe']
            }
          ]
        });
      }
    }
  };
}());


/* -------- PollApp.Validator — generic form validator -------- */
PollApp.Validator = (function () {

  /**
   * Validate a single value against a rule set.
   * @param {string} value
   * @param {object} rules — { required, minLen, maxLen, label }
   * @returns {string|null} error message or null
   */
  function validateField(value, rules) {
    var v = $.trim(value);
    if (rules.required && v.length === 0) {
      return (rules.label || 'Field') + ' is required.';
    }
    if (v.length > 0 && rules.minLen && v.length < rules.minLen) {
      return (rules.label || 'Field') + ' must be at least ' + rules.minLen + ' characters.';
    }
    if (rules.maxLen && v.length > rules.maxLen) {
      return (rules.label || 'Field') + ' must not exceed ' + rules.maxLen + ' characters.';
    }
    return null;
  }

  /**
   * Mark a field as invalid, showing inline error and red label.
   * @param {jQuery} $input
   * @param {string} message
   */
  function markInvalid($input, message) {
    $input.addClass('is-invalid');
    // mark associated label
    var id = $input.attr('id');
    if (id) {
      $('label[for="' + id + '"]').addClass('invalid-label');
    }
    // show/update inline error
    var $feedback = $input.next('.invalid-feedback');
    if ($feedback.length === 0) {
      $feedback = $('<div class="invalid-feedback"></div>');
      $input.after($feedback);
    }
    $feedback.text(message);
  }

  /**
   * Clear invalid state from a field.
   */
  function clearInvalid($input) {
    $input.removeClass('is-invalid');
    var id = $input.attr('id');
    if (id) {
      $('label[for="' + id + '"]').removeClass('invalid-label');
    }
    $input.next('.invalid-feedback').remove();
  }

  /**
   * Show validation summary banner above the form.
   * @param {jQuery} $summary
   * @param {string[]} errors
   */
  function showSummary($summary, errors) {
    var html = '<strong>Please fix the following errors:</strong><ul>';
    errors.forEach(function (e) { html += '<li>' + e + '</li>'; });
    html += '</ul>';
    $summary.html(html).addClass('show').get(0).scrollIntoView({ behavior: 'smooth' });
  }

  function hideSummary($summary) {
    $summary.removeClass('show').html('');
  }

  return {
    validateField: validateField,
    markInvalid: markInvalid,
    clearInvalid: clearInvalid,
    showSummary: showSummary,
    hideSummary: hideSummary
  };
}());


/* -------- PollApp.UI — shared UI helpers -------- */
PollApp.UI = (function () {

  /**
   * Show a dismissible success banner.
   * @param {jQuery} $el
   * @param {string} message
   */
  function showSuccess($el, message) {
    $el.html(
      '<span>✔ ' + message + '</span>'
    ).addClass('show').get(0).scrollIntoView({ behavior: 'smooth' });
  }

  function hideSuccess($el) {
    $el.removeClass('show').html('');
  }

  /**
   * Highlight the active nav link based on current page filename.
   */
  function setActiveNav() {
    var path = window.location.pathname;
    var filename = path.substring(path.lastIndexOf('/') + 1) || 'index.html';
    $('.nav-link').each(function () {
      var href = $(this).attr('href') || '';
      if (href === filename || (filename === 'index.html' && href === 'index.html')) {
        $(this).addClass('active');
      } else {
        $(this).removeClass('active');
      }
    });
  }

  /**
   * Build a spinner element.
   */
  function spinner() {
    return $('<div class="spinner-wrap"><div class="spinner-border text-success" role="status">' +
             '<span class="sr-only">Loading...</span></div></div>');
  }

  return { showSuccess: showSuccess, hideSuccess: hideSuccess, setActiveNav: setActiveNav, spinner: spinner };
}());


/* -------- PollApp.Login — Login modal -------- */
PollApp.Login = (function () {

  /* Very simple in-memory "auth" — in a real app this would be AJAX */
  var DEMO_USERS = [
    { alias: 'admin', password: 'admin123' },
    { alias: 'JoeSixpack', password: 'password' }
  ];

  var _currentUser = null;

  function init() {
    // Restore session
    try { _currentUser = JSON.parse(sessionStorage.getItem('pollapp_user')); } catch (e) {}
    _updateNavButton();
    _bindEvents();
  }

  function _bindEvents() {
    // Open modal
    $(document).on('click', '#btn-login-nav', function () {
      _resetModal();
      $('#loginModal').modal('show');
    });

    // Logout
    $(document).on('click', '#btn-logout-nav', function () {
      _currentUser = null;
      sessionStorage.removeItem('pollapp_user');
      _updateNavButton();
    });

    // Sign In
    $(document).on('click', '#btn-sign-in', function (e) {
      e.preventDefault();
      _attemptLogin();
    });

    // Enter key in password field
    $(document).on('keypress', '#login-password', function (e) {
      if (e.which === 13) { _attemptLogin(); }
    });

    // Live clear on typing
    $(document).on('input', '#login-alias, #login-password', function () {
      PollApp.Validator.clearInvalid($(this));
      $('#login-error').hide().text('');
    });
  }

  function _attemptLogin() {
    var alias = $.trim($('#login-alias').val());
    var password = $('#login-password').val();
    var errors = [];

    PollApp.Validator.clearInvalid($('#login-alias'));
    PollApp.Validator.clearInvalid($('#login-password'));

    var aliasErr = PollApp.Validator.validateField(alias, { required: true, minLen: 2, maxLen: 100, label: 'Alias' });
    if (aliasErr) { errors.push(aliasErr); PollApp.Validator.markInvalid($('#login-alias'), aliasErr); }

    var passErr = PollApp.Validator.validateField(password, { required: true, minLen: 4, maxLen: 100, label: 'Password' });
    if (passErr) { errors.push(passErr); PollApp.Validator.markInvalid($('#login-password'), passErr); }

    if (errors.length > 0) return;

    // Simulate server check
    var matched = DEMO_USERS.find(function (u) {
      return u.alias.toLowerCase() === alias.toLowerCase() && u.password === password;
    });

    if (!matched) {
      $('#login-error').text('Invalid alias or password. Please try again.').show();
      return;
    }

    _currentUser = { alias: matched.alias };
    if ($('#login-remember').is(':checked')) {
      sessionStorage.setItem('pollapp_user', JSON.stringify(_currentUser));
    }

    $('#loginModal').modal('hide');
    _updateNavButton();
  }

  function _updateNavButton() {
    if (_currentUser) {
      $('#btn-login-nav').hide();
      $('#btn-logout-nav').text('Logout (' + _currentUser.alias + ')').show();
    } else {
      $('#btn-login-nav').show();
      $('#btn-logout-nav').hide();
    }
  }

  function _resetModal() {
    $('#login-alias').val('');
    $('#login-password').val('');
    $('#login-remember').prop('checked', false);
    $('#login-error').hide().text('');
    PollApp.Validator.clearInvalid($('#login-alias'));
    PollApp.Validator.clearInvalid($('#login-password'));
  }

  return { init: init };
}());


/* -------- DOM Ready -------- */
$(function () {
  PollApp.Store.seed();       // seed demo data once
  PollApp.UI.setActiveNav();  // highlight current nav link
  PollApp.Login.init();       // login modal
});

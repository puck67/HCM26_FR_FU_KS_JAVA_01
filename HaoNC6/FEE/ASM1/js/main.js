$(document).ready(function () {
  // ==========================================
  // 1. Sidebar Navigation Active Link Toggler
  // ==========================================
  // Automatically activate the correct sidebar item based on current URL path
  var currentPath = window.location.pathname;
  var pageName = currentPath.substring(currentPath.lastIndexOf('/') + 1);

  if (pageName === 'view-content.html') {
    $('.sidebar-nav-item a[href="view-content.html"]').addClass('active');
  } else if (pageName === 'add-content.html') {
    $('.sidebar-nav-item a[href="add-content.html"]').addClass('active');
  } else if (pageName === 'edit-profile.html') {
    // If we are on Edit Profile, we don't have a direct sidebar link, but it's part of the CMS layout.
    // We can highlight profile or keep them inactive. Let's leave them inactive or active if designed.
  }

  // Dynamic click handler as requested
  $('.sidebar-nav-item a').on('click', function () {  
    $('.sidebar-nav-item a').removeClass('active');
    $(this).addClass('active');
  });

  // Mobile Hamburger Toggle
  $('#mobile-toggle').on('click', function (e) {
    e.stopPropagation();
    $('.dashboard-sidebar').toggleClass('show');
  });

  // Close sidebar on mobile when clicking outside
  $(document).on('click', function (e) {
    if (!$(e.target).closest('.dashboard-sidebar').length && !$(e.target).closest('#mobile-toggle').length) {
      $('.dashboard-sidebar').removeClass('show');
    }
  });

  // ==========================================
  // 2. Helper Validation Functions
  // ==========================================
  function isValidEmail(email) {
    var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
  }

  function showError(inputElement, message) {
    inputElement.addClass('is-invalid');
    var feedback = inputElement.siblings('.invalid-feedback');
    if (feedback.length === 0) {
      inputElement.after('<div class="invalid-feedback">' + message + '</div>');
    } else {
      feedback.text(message);
    }
  }

  function clearError(inputElement) {
    inputElement.removeClass('is-invalid');
    inputElement.siblings('.invalid-feedback').remove();
  }

  // Clear all errors from a form
  function clearFormErrors(form) {
    form.find('.is-invalid').removeClass('is-invalid');
    form.find('.invalid-feedback').remove();
    form.find('.alert').remove();
  }

  // ==========================================
  // 3. Form Validation and Mock AJAX Submissions
  // ==========================================

  // --- LOGIN FORM VALIDATION ---
  $('#login-form').on('submit', function (e) {
    e.preventDefault();
    var form = $(this);
    clearFormErrors(form);

    var emailInput = $('#email');
    var passwordInput = $('#password');
    var isValid = true;

    // Email Check
    if ($.trim(emailInput.val()) === '') {
      showError(emailInput, 'Email field is required.');
      isValid = false;
    } else if (!isValidEmail(emailInput.val())) {
      showError(emailInput, 'Please enter a valid email address.');
      isValid = false;
    } else {
      clearError(emailInput);
    }

    // Password Check
    if ($.trim(passwordInput.val()) === '') {
      showError(passwordInput, 'Password field is required.');
      isValid = false;
    } else {
      clearError(passwordInput);
    }

    if (isValid) {
      simulateAjaxSubmit(form, 'POST', 'Login successful! Redirecting to Edit Profile...', function() {
        window.location.href = 'edit-profile.html';
      });
    }
  });

  // --- REGISTER FORM VALIDATION ---
  $('#register-form').on('submit', function (e) {
    e.preventDefault();
    var form = $(this);
    clearFormErrors(form);

    var usernameInput = $('#username');
    var emailInput = $('#email');
    var passwordInput = $('#password');
    var repasswordInput = $('#repassword');
    var isValid = true;

    // Username Check
    if ($.trim(usernameInput.val()) === '') {
      showError(usernameInput, 'Username field is required.');
      isValid = false;
    } else {
      clearError(usernameInput);
    }

    // Email Check
    if ($.trim(emailInput.val()) === '') {
      showError(emailInput, 'Email field is required.');
      isValid = false;
    } else if (!isValidEmail(emailInput.val())) {
      showError(emailInput, 'Please enter a valid email address.');
      isValid = false;
    } else {
      clearError(emailInput);
    }

    // Password Check
    if ($.trim(passwordInput.val()) === '') {
      showError(passwordInput, 'Password field is required.');
      isValid = false;
    } else if (passwordInput.val().length < 6) {
      showError(passwordInput, 'Password must be at least 6 characters long.');
      isValid = false;
    } else {
      clearError(passwordInput);
    }

    // Re Password Check
    if ($.trim(repasswordInput.val()) === '') {
      showError(repasswordInput, 'Please repeat your password.');
      isValid = false;
    } else if (repasswordInput.val() !== passwordInput.val()) {
      showError(repasswordInput, 'Passwords do not match.');
      isValid = false;
    } else {
      clearError(repasswordInput);
    }

    if (isValid) {
      simulateAjaxSubmit(form, 'POST', 'Registration successful! Redirecting to Login...', function() {
        window.location.href = 'login.html';
      });
    }
  });

  // --- EDIT PROFILE FORM VALIDATION ---
  $('#edit-profile-form').on('submit', function (e) {
    e.preventDefault();
    var form = $(this);
    clearFormErrors(form);

    var firstName = $('#firstname');
    var lastName = $('#lastname');
    var emailInput = $('#email');
    var phoneInput = $('#phone');
    var isValid = true;

    if ($.trim(firstName.val()) === '') {
      showError(firstName, 'First Name field is required.');
      isValid = false;
    } else {
      clearError(firstName);
    }

    if ($.trim(lastName.val()) === '') {
      showError(lastName, 'Last Name field is required.');
      isValid = false;
    } else {
      clearError(lastName);
    }

    if ($.trim(emailInput.val()) === '') {
      showError(emailInput, 'Email field is required.');
      isValid = false;
    } else if (!isValidEmail(emailInput.val())) {
      showError(emailInput, 'Please enter a valid email address.');
      isValid = false;
    } else {
      clearError(emailInput);
    }

    if ($.trim(phoneInput.val()) === '') {
      showError(phoneInput, 'Phone number field is required.');
      isValid = false;
    } else {
      clearError(phoneInput);
    }

    if (isValid) {
      simulateAjaxSubmit(form, 'POST', 'Profile updated successfully!', null);
    }
  });

  // --- ADD CONTENT FORM VALIDATION ---
  $('#add-content-form').on('submit', function (e) {
    e.preventDefault();
    var form = $(this);
    clearFormErrors(form);

    var titleInput = $('#title');
    var briefInput = $('#brief');
    var contentInput = $('#content');
    var isValid = true;

    if ($.trim(titleInput.val()) === '') {
      showError(titleInput, 'Title field is required.');
      isValid = false;
    } else {
      clearError(titleInput);
    }

    if ($.trim(briefInput.val()) === '') {
      showError(briefInput, 'Brief description field is required.');
      isValid = false;
    } else {
      clearError(briefInput);
    }

    if ($.trim(contentInput.val()) === '') {
      showError(contentInput, 'Content field is required.');
      isValid = false;
    } else {
      clearError(contentInput);
    }

    if (isValid) {
      simulateAjaxSubmit(form, 'POST', 'Content added successfully! Redirecting to list...', function() {
        window.location.href = 'view-content.html';
      });
    }
  });

  // ==========================================
  // 4. Reset Button Action Handler
  // ==========================================
  // Standard HTML reset clears value, but we also want to remove error messages and alerts
  $('button[type="reset"]').on('click', function () {
    var form = $(this).closest('form');
    // We delay the manual clean slightly so HTML native reset can do its work first,
    // or we do it explicitly.
    setTimeout(function() {
      clearFormErrors(form);
      form.find('input, textarea').removeClass('is-invalid');
    }, 50);
  });

  // ==========================================
  // 5. AJAX Mock Server Request Simulation
  // ==========================================
  function simulateAjaxSubmit(form, method, successMessage, callback) {
    var submitBtn = form.find('button[type="submit"]');
    var originalBtnHtml = submitBtn.html();
    
    // Disable button & show spinner
    submitBtn.prop('disabled', true);
    submitBtn.html('<span class="spinner-border spinner-border-sm mr-2" role="status" aria-hidden="true"></span>Processing...');

    // Collect data to simulate payload
    var formData = form.serialize();
    console.log('Sending AJAX request (' + method + '):', formData);

    // Mock $.ajax request
    $.ajax({
      url: 'https://jsonplaceholder.typicode.com/posts', // Free safe mock REST API URL
      method: method,
      data: formData,
      success: function (response) {
        // Success handler
        setTimeout(function () {
          submitBtn.prop('disabled', false);
          submitBtn.html(originalBtnHtml);

          // Prepend alert message to the card body/form
          var alertHtml = '<div class="alert alert-success alert-dismissible fade show" role="alert">' +
                          successMessage +
                          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                          '<span aria-hidden="true">&times;</span>' +
                          '</button>' +
                          '</div>';
          form.prepend(alertHtml);

          if (callback) {
            setTimeout(callback, 1500);
          }
        }, 1200); // Realistic network delay
      },
      error: function (xhr, status, error) {
        // Error handler
        setTimeout(function () {
          submitBtn.prop('disabled', false);
          submitBtn.html(originalBtnHtml);

          var alertHtml = '<div class="alert alert-danger alert-dismissible fade show" role="alert">' +
                          'An error occurred: ' + error +
                          '<button type="button" class="close" data-dismiss="alert" aria-label="Close">' +
                          '<span aria-hidden="true">&times;</span>' +
                          '</button>' +
                          '</div>';
          form.prepend(alertHtml);
        }, 1200);
      }
    });
  }
});

/**
 * scripts.js - Main JavaScript & jQuery file (Structured in /js folder)
 * Contains Form Validation, AJAX Navigation, Dynamic Seeding, and Session Management
 */

$(document).ready(function () {
  // 1. Initial Seeding of Database in LocalStorage (if not present)
  seedDatabase();

  // 2. Setup Session State
  let currentSession = JSON.parse(localStorage.getItem('cms_session'));

  // 3. Handle Page-Specific Initialization
  detectAndInitPage(currentSession);

  // 4. Bind Global Navigation Events (Sidebar and Dropdown menu)
  bindNavigationEvents();
});

/**
 * Seeds mock users and contents in localStorage if not already set.
 */
function seedDatabase() {
  if (!localStorage.getItem('cms_users')) {
    const defaultUsers = [
      {
        email: 'your_email@example.com',
        username: 'admin',
        password: 'Password123',
        firstName: 'Admin',
        lastName: 'User',
        phone: '0987654321',
        description: 'Default System Administrator account for FEE.P.A101 Content Management System.'
      }
    ];
    localStorage.setItem('cms_users', JSON.stringify(defaultUsers));
  }

  if (!localStorage.getItem('cms_contents')) {
    const defaultContents = [
      {
        id: 1,
        title: 'HTML5 Semantic Structure',
        brief: 'An overview of HTML5 semantic tags and layout best practices.',
        content: 'HTML5 introduced semantic elements like header, footer, section, article, nav, and aside. These elements clarify the structural purpose of different parts of a webpage, which helps search engine optimization (SEO) and web accessibility (a11y). Using clean semantic layouts is a fundamental skill in modern front-end web engineering.',
        createdDate: '2026-06-01 14:32:00'
      },
      {
        id: 2,
        title: 'Bootstrap 4 Responsive Layout Grid',
        brief: 'Learn how to master the responsive grid system of Bootstrap 4.',
        content: 'Bootstrap 4 utilizes a flexible 12-column grid layout built on Flexbox. By using container, row, and column classes (.col-*, .col-sm-*, .col-md-*, .col-lg-*, .col-xl-*), engineers can create dynamic fluid layouts that adapt seamlessly to mobile, tablet, and desktop viewports. Media queries are managed under the hood, ensuring speed of development.',
        createdDate: '2026-06-02 09:15:00'
      },
      {
        id: 3,
        title: 'Client-side Form Validation with jQuery',
        brief: 'Why client-side validation is important and how to implement it.',
        content: 'Validation on the client-side helps reduce server load and provides immediate feedback to users, improving user experience (UX). By looping through input fields with jQuery and checking requirements like length, empty values, matching passwords, and email format regexes, developers can catch and highlight entry errors in milliseconds before any network request occurs.',
        createdDate: '2026-06-02 10:20:00'
      }
    ];
    localStorage.setItem('cms_contents', JSON.stringify(defaultContents));
  }
}

/**
 * Checks the current HTML page loaded and runs relevant setup.
 */
function detectAndInitPage(session) {
  const path = window.location.pathname;
  const page = path.substring(path.lastIndexOf('/') + 1);

  // If we are on dashboard (index.html) and not logged in, redirect to login
  if (page === 'index.html' || page === '') {
    if (!session) {
      window.location.href = 'login.html';
      return;
    }
    // Set logged-in username in header
    updateHeaderUserProfile(session);
    // Load standard View Contents by default via mock AJAX
    loadMainContentFragment('templates/view-contents.html', false);
  }

  // If on login/register and already logged in, redirect to index
  if (page === 'login.html') {
    if (session) {
      window.location.href = 'index.html';
      return;
    }
    initLoginPage();
  }

  if (page === 'register.html') {
    if (session) {
      window.location.href = 'index.html';
      return;
    }
    initRegisterPage();
  }
}

/**
 * Updates the user profile name display in the navbar.
 */
function updateHeaderUserProfile(session) {
  if (!session) return;
  const fullName = (session.firstName && session.lastName) 
    ? `${session.firstName} ${session.lastName}` 
    : session.username || 'User Profile';
  
  $('#user-profile-name').text(fullName);
}

/**
 * Binds sidebar navigation and profile links to handle dynamic dynamic mock AJAX loading with 5 seconds screen.
 */
function bindNavigationEvents() {
  // Sidebar Search function
  $(document).on('keyup', '#sidebar-search-input', function() {
    const query = $(this).val().toLowerCase();
    filterContentsTable(query);
  });
  
  $(document).on('click', '#sidebar-search-btn', function(e) {
    e.preventDefault();
    const query = $('#sidebar-search-input').val().toLowerCase();
    filterContentsTable(query);
  });

  // Sidebar Links: View contents and Form content
  $('.sidebar-nav-link').on('click', function (e) {
    e.preventDefault();
    const targetFile = $(this).data('target');
    
    // De-activate other links, activate current
    $('.sidebar-nav-link').removeClass('active');
    $(this).addClass('active');

    // Load page fragment with 5s delay
    loadMainContentFragment(targetFile, true);
  });

  // Header Dropdown Links: User Profile & Logout
  $(document).on('click', '#nav-edit-profile', function (e) {
    e.preventDefault();
    $('.sidebar-nav-link').removeClass('active');
    loadMainContentFragment('templates/edit-profile.html', true);
  });

  $(document).on('click', '#nav-logout', function (e) {
    e.preventDefault();
    localStorage.removeItem('cms_session');
    window.location.href = 'login.html';
  });
}

/**
 * Filters the View Contents table based on Search Input (Title or Brief)
 */
function filterContentsTable(query) {
  // If the view contents page is currently loaded
  if ($('#view-contents-table-body').length > 0) {
    $('#view-contents-table-body tr').each(function() {
      const titleText = $(this).find('td:nth-child(2)').text().toLowerCase();
      const briefText = $(this).find('td:nth-child(3)').text().toLowerCase();
      
      if (titleText.indexOf(query) !== -1 || briefText.indexOf(query) !== -1) {
        $(this).show();
      } else {
        $(this).hide();
      }
    });
  }
}

/**
 * Displays the full-page 5-second simulated AJAX loading overlay, fetches the requested fragment, and injects it.
 * @param {string} fragmentFile - The page fragment file (e.g. templates/view-contents.html)
 * @param {boolean} showLoading - Whether to show the 5s loading screen or load instantly
 */
function loadMainContentFragment(fragmentFile, showLoading = true) {
  const container = $('#main-content');
  const overlay = $('#loading-overlay');
  
  if (showLoading) {
    // Show 5s simulated AJAX loader overlay
    overlay.addClass('show');
    
    // We execute the AJAX call immediately but keep spinner up for exactly 5 seconds
    let ajaxData = null;
    let ajaxError = null;

    $.ajax({
      url: fragmentFile,
      type: 'GET',
      dataType: 'html',
      success: function(response) {
        ajaxData = response;
      },
      error: function(xhr, status, error) {
        console.warn(`Local AJAX failed for ${fragmentFile}. Falling back to hardcoded templates.`);
        ajaxError = error;
      }
    });

    setTimeout(function() {
      overlay.removeClass('show');
      
      if (ajaxData) {
        container.html(ajaxData);
        initializeFragment(fragmentFile);
      } else {
        // Fallback mockup generator (in case of CORS on direct file:// protocol)
        const mockHTML = getFallbackTemplate(fragmentFile);
        container.html(mockHTML);
        initializeFragment(fragmentFile);
      }
    }, 5000); // 5000 milliseconds = 5 seconds

  } else {
    // Instant load without spinner (e.g., initial load on dashboard)
    $.ajax({
      url: fragmentFile,
      type: 'GET',
      dataType: 'html',
      success: function(response) {
        container.html(response);
        initializeFragment(fragmentFile);
      },
      error: function() {
        const mockHTML = getFallbackTemplate(fragmentFile);
        container.html(mockHTML);
        initializeFragment(fragmentFile);
      }
    });
  }
}

/**
 * Runs initialization routines on loaded fragments.
 */
function initializeFragment(fragmentFile) {
  const session = JSON.parse(localStorage.getItem('cms_session'));

  if (fragmentFile.indexOf('view-contents.html') !== -1) {
    renderContentsTable();
  } else if (fragmentFile.indexOf('edit-profile.html') !== -1) {
    populateEditProfileForm(session);
    initEditProfilePage();
  } else if (fragmentFile.indexOf('add-content.html') !== -1) {
    initAddContentPage();
  }
}

/* ==========================================================================
   PAGE: Login Page Scripts
   ========================================================================== */
function initLoginPage() {
  $('#login-form').on('submit', function (e) {
    e.preventDefault();
    
    // 1. Reset invalid statuses
    $('.form-control').removeClass('is-invalid');
    
    // 2. Loop through all fields to check for empty values
    let hasEmptyField = false;
    $('#login-form .form-control').each(function() {
      if ($.trim($(this).val()) === '') {
        $(this).addClass('is-invalid');
        $(this).siblings('.invalid-feedback').text('This field is required.');
        hasEmptyField = true;
      }
    });

    if (hasEmptyField) return false;

    // 3. Specific validation logic
    const emailInput = $('#login-email');
    const passwordInput = $('#login-password');
    const emailVal = $.trim(emailInput.val());
    const passwordVal = $.trim(passwordInput.val());

    let isValid = true;

    // Email Pattern check & Length Check (5-50)
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(emailVal)) {
      emailInput.addClass('is-invalid');
      emailInput.siblings('.invalid-feedback').text('Please enter a valid e-mail address.');
      isValid = false;
    } else if (emailVal.length < 5 || emailVal.length > 50) {
      emailInput.addClass('is-invalid');
      emailInput.siblings('.invalid-feedback').text('E-mail must be between 5 and 50 characters.');
      isValid = false;
    }

    // Password Length Check (8-30)
    if (passwordVal.length < 8 || passwordVal.length > 30) {
      passwordInput.addClass('is-invalid');
      passwordInput.siblings('.invalid-feedback').text('Password must be between 8 and 30 characters.');
      isValid = false;
    }

    if (!isValid) return false;

    // 4. Authenticate User
    const users = JSON.parse(localStorage.getItem('cms_users')) || [];
    const foundUser = users.find(u => u.email.toLowerCase() === emailVal.toLowerCase() && u.password === passwordVal);

    if (foundUser) {
      // Set session & redirect
      localStorage.setItem('cms_session', JSON.stringify(foundUser));
      window.location.href = 'index.html';
    } else {
      // Generic invalid login message
      emailInput.addClass('is-invalid');
      passwordInput.addClass('is-invalid');
      $('#login-error-alert').removeClass('d-none').text('Invalid E-mail or Password. Try again or Register!');
    }
  });

  // Real-time error clean
  $('#login-form .form-control').on('input focus', function() {
    $(this).removeClass('is-invalid');
    $('#login-error-alert').addClass('d-none');
  });
}

/* ==========================================================================
   PAGE: Register Page Scripts
   ========================================================================== */
function initRegisterPage() {
  $('#register-form').on('submit', function (e) {
    e.preventDefault();

    // 1. Reset invalid statuses
    $('.form-control').removeClass('is-invalid');

    // 2. Loop through all fields to check for empty values
    let hasEmptyField = false;
    $('#register-form .form-control').each(function() {
      if ($.trim($(this).val()) === '') {
        $(this).addClass('is-invalid');
        $(this).siblings('.invalid-feedback').text('This field is required.');
        hasEmptyField = true;
      }
    });

    if (hasEmptyField) return false;

    // 3. Specific validation logic
    const usernameInput = $('#reg-username');
    const emailInput = $('#reg-email');
    const passwordInput = $('#reg-password');
    const repasswordInput = $('#reg-repassword');

    const usernameVal = $.trim(usernameInput.val());
    const emailVal = $.trim(emailInput.val());
    const passwordVal = $.trim(passwordInput.val());
    const repasswordVal = $.trim(repasswordInput.val());

    let isValid = true;

    // Username Length Check (3-30)
    if (usernameVal.length < 3 || usernameVal.length > 30) {
      usernameInput.addClass('is-invalid');
      usernameInput.siblings('.invalid-feedback').text('User name must be between 3 and 30 characters.');
      isValid = false;
    }

    // Email Pattern check & Length Check (min 5)
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(emailVal)) {
      emailInput.addClass('is-invalid');
      emailInput.siblings('.invalid-feedback').text('Please enter a valid e-mail address.');
      isValid = false;
    } else if (emailVal.length < 5) {
      emailInput.addClass('is-invalid');
      emailInput.siblings('.invalid-feedback').text('E-mail must be at least 5 characters.');
      isValid = false;
    }

    // Password Length Check (8-30)
    if (passwordVal.length < 8 || passwordVal.length > 30) {
      passwordInput.addClass('is-invalid');
      passwordInput.siblings('.invalid-feedback').text('Password must be between 8 and 30 characters.');
      isValid = false;
    }

    // Re Password Length Check (8-30) and match check
    if (repasswordVal.length < 8 || repasswordVal.length > 30) {
      repasswordInput.addClass('is-invalid');
      repasswordInput.siblings('.invalid-feedback').text('Re Password must be between 8 and 30 characters.');
      isValid = false;
    } else if (passwordVal !== repasswordVal) {
      repasswordInput.addClass('is-invalid');
      repasswordInput.siblings('.invalid-feedback').text('Confirm Password does not match Password.');
      isValid = false;
    }

    if (!isValid) return false;

    // 4. Check if Email already exists
    const users = JSON.parse(localStorage.getItem('cms_users')) || [];
    const emailExists = users.some(u => u.email.toLowerCase() === emailVal.toLowerCase());

    if (emailExists) {
      emailInput.addClass('is-invalid');
      emailInput.siblings('.invalid-feedback').text('This e-mail is already registered.');
      return false;
    }

    // 5. Register User
    const newUser = {
      email: emailVal,
      username: usernameVal,
      password: passwordVal,
      firstName: '',
      lastName: '',
      phone: '',
      description: ''
    };
    
    users.push(newUser);
    localStorage.setItem('cms_users', JSON.stringify(users));

    // Show success alert and redirect to login page
    $('#register-success-alert').removeClass('d-none');
    setTimeout(function() {
      window.location.href = 'login.html';
    }, 2000);
  });

  // Real-time error clean
  $('#register-form .form-control').on('input focus', function() {
    $(this).removeClass('is-invalid');
  });
}

/* ==========================================================================
   PAGE: View Contents Scripts
   ========================================================================== */
function renderContentsTable() {
  const contents = JSON.parse(localStorage.getItem('cms_contents')) || [];
  const tbody = $('#view-contents-table-body');
  tbody.empty();

  if (contents.length === 0) {
    tbody.append(`
      <tr>
        <td colspan="4" class="text-center text-muted py-4">No content items found. Click 'Form content' to add.</td>
      </tr>
    `);
    return;
  }

  contents.forEach((item, index) => {
    tbody.append(`
      <tr data-id="${item.id}">
        <td class="font-weight-medium">${index + 1}</td>
        <td class="font-weight-medium">${escapeHtml(item.title)}</td>
        <td class="text-muted text-xs">${escapeHtml(item.brief)}</td>
        <td>${item.createdDate}</td>
      </tr>
    `);
  });
}

function deleteContentItem(id) {
  let contents = JSON.parse(localStorage.getItem('cms_contents')) || [];
  contents = contents.filter(item => item.id !== id);
  localStorage.setItem('cms_contents', JSON.stringify(contents));
  renderContentsTable();
}

function editContentItem(id) {
  // Load templates/add-content.html fragment first, then pre-populate with target details
  const overlay = $('#loading-overlay');
  overlay.addClass('show');

  $.ajax({
    url: 'templates/add-content.html',
    type: 'GET',
    dataType: 'html',
    success: function(response) {
      setTimeout(function() {
        overlay.removeClass('show');
        $('#main-content').html(response);
        
        // Populating content inputs
        const contents = JSON.parse(localStorage.getItem('cms_contents')) || [];
        const item = contents.find(c => c.id === id);
        
        if (item) {
          $('#content-id-hidden').val(item.id);
          $('#content-title').val(item.title);
          $('#content-brief').val(item.brief);
          $('#content-textarea').val(item.content);
          
          $('#content-card-header-title').text('Edit Content Form');
          $('#content-submit-btn').text('Update');
          
          initAddContentPage();
        }
      }, 1000);
    },
    error: function() {
      // Fallback
      setTimeout(function() {
        overlay.removeClass('show');
        const mockHTML = getFallbackTemplate('templates/add-content.html');
        $('#main-content').html(mockHTML);
        
        const contents = JSON.parse(localStorage.getItem('cms_contents')) || [];
        const item = contents.find(c => c.id === id);
        
        if (item) {
          $('#content-id-hidden').val(item.id);
          $('#content-title').val(item.title);
          $('#content-brief').val(item.brief);
          $('#content-textarea').val(item.content);
          
          $('#content-card-header-title').text('Edit Content Form');
          $('#content-submit-btn').text('Update');
          
          initAddContentPage();
        }
      }, 1000);
    }
  });
}

/* ==========================================================================
   PAGE: Form Content (Add / Edit) Scripts
   ========================================================================== */
function initAddContentPage() {
  $('#add-content-form').off('submit').on('submit', function (e) {
    e.preventDefault();

    // 1. Reset invalid statuses
    $('.form-control').removeClass('is-invalid');

    // 2. Loop through all fields to check for empty values
    let hasEmptyField = false;
    $('#add-content-form .form-control').not('#content-id-hidden').each(function() {
      if ($.trim($(this).val()) === '') {
        $(this).addClass('is-invalid');
        $(this).siblings('.invalid-feedback').text('This field is required.');
        hasEmptyField = true;
      }
    });

    if (hasEmptyField) return false;

    // 3. Specific validation logic
    const titleInput = $('#content-title');
    const briefInput = $('#content-brief');
    const contentInput = $('#content-textarea');

    const titleVal = $.trim(titleInput.val());
    const briefVal = $.trim(briefInput.val());
    const contentVal = $.trim(contentInput.val());

    let isValid = true;

    // Title Length (10-200)
    if (titleVal.length < 10 || titleVal.length > 200) {
      titleInput.addClass('is-invalid');
      titleInput.siblings('.invalid-feedback').text('Title must be between 10 and 200 characters.');
      isValid = false;
    }

    // Brief Length (30-150)
    if (briefVal.length < 30 || briefVal.length > 150) {
      briefInput.addClass('is-invalid');
      briefInput.siblings('.invalid-feedback').text('Brief must be between 30 and 150 characters.');
      isValid = false;
    }

    // Content Length (50-1000)
    if (contentVal.length < 50 || contentVal.length > 1000) {
      contentInput.addClass('is-invalid');
      contentInput.siblings('.invalid-feedback').text('Content must be between 50 and 1000 characters.');
      isValid = false;
    }

    if (!isValid) return false;

    // 4. Save/Update via mock AJAX
    const idVal = $('#content-id-hidden').val();
    let contents = JSON.parse(localStorage.getItem('cms_contents')) || [];
    
    const overlay = $('#loading-overlay');
    overlay.addClass('show');

    // Simulate Server Request (2 seconds)
    setTimeout(function() {
      overlay.removeClass('show');

      if (idVal) {
        // Mode: Update Existing
        const index = contents.findIndex(c => c.id === parseInt(idVal));
        if (index !== -1) {
          contents[index].title = titleVal;
          contents[index].brief = briefVal;
          contents[index].content = contentVal;
          localStorage.setItem('cms_contents', JSON.stringify(contents));
        }
      } else {
        // Mode: Add New
        const newId = contents.length > 0 ? Math.max(...contents.map(c => c.id)) + 1 : 1;
        const newContent = {
          id: newId,
          title: titleVal,
          brief: briefVal,
          content: contentVal,
          createdDate: getCurrentDateTimeString()
        };
        contents.push(newContent);
        localStorage.setItem('cms_contents', JSON.stringify(contents));
      }

      // Show success screen and load templates/view-contents fragment
      showSuccessToast('Content saved successfully!');
      
      // Update sidebar nav active link
      $('.sidebar-nav-link').removeClass('active');
      $('.sidebar-nav-link[data-target="templates/view-contents.html"]').addClass('active');
      
      loadMainContentFragment('templates/view-contents.html', false);
    }, 1500);
  });

  // Real-time error clean
  $('#add-content-form .form-control').on('input focus', function() {
    $(this).removeClass('is-invalid');
  });

  // Bind Reset button
  $('#content-reset-btn').on('click', function(e) {
    e.preventDefault();
    $('#add-content-form')[0].reset();
    $('.form-control').removeClass('is-invalid');
  });
}

/* ==========================================================================
   PAGE: Edit Profile Scripts
   ========================================================================== */
function populateEditProfileForm(session) {
  if (!session) return;
  
  $('#profile-firstname').val(session.firstName || '');
  $('#profile-lastname').val(session.lastName || '');
  $('#profile-email').text(session.email || 'your_email@example.com');
  $('#profile-phone').val(session.phone || '');
  $('#profile-description').val(session.description || '');
}

function initEditProfilePage() {
  $('#edit-profile-form').on('submit', function (e) {
    e.preventDefault();

    // 1. Reset invalid statuses
    $('.form-control').removeClass('is-invalid');

    // 2. Loop through all fields to check for empty values (except description which is optional)
    let hasEmptyField = false;
    $('#edit-profile-form .form-control').not('#profile-description').each(function() {
      if ($.trim($(this).val()) === '') {
        $(this).addClass('is-invalid');
        $(this).siblings('.invalid-feedback').text('This field is required.');
        hasEmptyField = true;
      }
    });

    if (hasEmptyField) return false;

    // 3. Specific validation logic
    const firstnameInput = $('#profile-firstname');
    const lastnameInput = $('#profile-lastname');
    const phoneInput = $('#profile-phone');
    const descInput = $('#profile-description');

    const firstnameVal = $.trim(firstnameInput.val());
    const lastnameVal = $.trim(lastnameInput.val());
    const phoneVal = $.trim(phoneInput.val());
    const descVal = $.trim(descInput.val());

    let isValid = true;

    // First Name Length (3-30)
    if (firstnameVal.length < 3 || firstnameVal.length > 30) {
      firstnameInput.addClass('is-invalid');
      firstnameInput.siblings('.invalid-feedback').text('First Name must be between 3 and 30 characters.');
      isValid = false;
    }

    // Last Name Length (3-30)
    if (lastnameVal.length < 3 || lastnameVal.length > 30) {
      lastnameInput.addClass('is-invalid');
      lastnameInput.siblings('.invalid-feedback').text('Last Name must be between 3 and 30 characters.');
      isValid = false;
    }

    // Phone Length (9-13 digits) and regex /^\d+$/
    const phonePattern = /^\d+$/;
    if (!phonePattern.test(phoneVal)) {
      phoneInput.addClass('is-invalid');
      phoneInput.siblings('.invalid-feedback').text('Phone number must contain only numeric digits.');
      isValid = false;
    } else if (phoneVal.length < 9 || phoneVal.length > 13) {
      phoneInput.addClass('is-invalid');
      phoneInput.siblings('.invalid-feedback').text('Phone number must be between 9 and 13 digits.');
      isValid = false;
    }

    // Description Length (max 200)
    if (descVal.length > 200) {
      descInput.addClass('is-invalid');
      descInput.siblings('.invalid-feedback').text('Description must not exceed 200 characters.');
      isValid = false;
    }

    if (!isValid) return false;

    // 4. Update Profile via Mock AJAX call
    const session = JSON.parse(localStorage.getItem('cms_session'));
    const users = JSON.parse(localStorage.getItem('cms_users')) || [];

    // Find and update session user
    const updatedUser = {
      ...session,
      firstName: firstnameVal,
      lastName: lastnameVal,
      phone: phoneVal,
      description: descVal
    };

    // Update in users array
    const userIndex = users.findIndex(u => u.email.toLowerCase() === session.email.toLowerCase());
    if (userIndex !== -1) {
      users[userIndex] = updatedUser;
    } else {
      users.push(updatedUser);
    }

    const overlay = $('#loading-overlay');
    overlay.addClass('show');

    // Simulate AJAX update (1.5 seconds delay)
    setTimeout(function() {
      overlay.removeClass('show');
      
      // Save back to storage
      localStorage.setItem('cms_session', JSON.stringify(updatedUser));
      localStorage.setItem('cms_users', JSON.stringify(users));

      // Update UI elements
      updateHeaderUserProfile(updatedUser);
      
      // Show dynamic custom alert inside fragment
      $('#profile-alert-container').html(`
        <div class="alert-custom alert-custom-success">
          <i class="fas fa-check-circle"></i>
          <div>Profile updated successfully! Information saved via AJAX.</div>
        </div>
      `);
      
      // Auto-hide alert after 3 seconds
      setTimeout(function() {
        $('#profile-alert-container').empty();
      }, 3000);

    }, 1500);
  });

  // Real-time error clean
  $('#edit-profile-form .form-control').on('input focus', function() {
    $(this).removeClass('is-invalid');
  });

  // Bind Reset button
  $('#profile-reset-btn').on('click', function(e) {
    e.preventDefault();
    const session = JSON.parse(localStorage.getItem('cms_session'));
    populateEditProfileForm(session);
    $('.form-control').removeClass('is-invalid');
    $('#profile-alert-container').empty();
  });
}

/* ==========================================================================
   HEPLERS & FALLBACK TEMPLATES
   ========================================================================== */
function getCurrentDateTimeString() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

function escapeHtml(text) {
  return text
    ? text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;")
    : '';
}

function showSuccessToast(message) {
  const toastHtml = `
    <div style="position: fixed; top: 70px; right: 20px; z-index: 9999; min-width: 250px; background-color: #ecfdf5; border: 1px solid #a7f3d0; border-radius: 6px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); padding: 1rem; color: #065f46; display: flex; align-items: center; transition: opacity 0.5s ease;" id="success-toast">
      <i class="fas fa-check-circle mr-2" style="font-size: 1.25rem;"></i>
      <span class="font-weight-medium">${message}</span>
    </div>
  `;
  $('body').append(toastHtml);
  
  setTimeout(function() {
    $('#success-toast').css('opacity', 0);
    setTimeout(function() {
      $('#success-toast').remove();
    }, 500);
  }, 2500);
}

/**
 * Returns hardcoded HTML strings for local file:// mode templates so the app works without a server.
 */
function getFallbackTemplate(filename) {
  if (filename.indexOf('view-contents.html') !== -1) {
    return `
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 font-weight-bold text-dark">View Content</h1>
      </div>
      <div class="card cms-card">
        <div class="card-header cms-card-header">
          Content List Elements
        </div>
        <div class="card-body cms-card-body p-0">
          <div class="table-responsive">
            <table class="table cms-table">
              <thead>
                <tr>
                  <th style="width: 60px;">#</th>
                  <th style="width: 250px;">Title</th>
                  <th>Brief</th>
                  <th style="width: 180px;">Created Date</th>
                  <th style="width: 180px;">Actions</th>
                </tr>
              </thead>
              <tbody id="view-contents-table-body">
                <!-- Loaded dynamically by jQuery -->
              </tbody>
            </table>
          </div>
        </div>
      </div>
    `;
  }

  if (filename.indexOf('add-content.html') !== -1) {
    return `
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 font-weight-bold text-dark" id="content-card-header-title">Add Content</h1>
      </div>
      <div class="card cms-card">
        <div class="card-header cms-card-header">
          Content Form Elements
        </div>
        <div class="card-body cms-card-body">
          <form id="add-content-form">
            <input type="hidden" id="content-id-hidden" value="">
            
            <div class="form-group mb-4">
              <label for="content-title" class="font-weight-semibold text-dark">Title</label>
              <input type="text" class="form-control" id="content-title" placeholder="Enter the title" maxlength="200">
              <div class="invalid-feedback">Title must be between 10 and 200 characters.</div>
            </div>

            <div class="form-group mb-4">
              <label for="content-brief" class="font-weight-semibold text-dark">Brief</label>
              <textarea class="form-control" id="content-brief" rows="3" placeholder="Enter the brief" maxlength="150"></textarea>
              <div class="invalid-feedback">Brief must be between 30 and 150 characters.</div>
            </div>

            <div class="form-group mb-4">
              <label for="content-textarea" class="font-weight-semibold text-dark">Content</label>
              <textarea class="form-control" id="content-textarea" rows="8" placeholder="Enter the content" maxlength="1000"></textarea>
              <div class="invalid-feedback">Content must be between 50 and 1000 characters.</div>
            </div>

            <div class="form-group mb-0">
              <button type="submit" class="btn btn-green px-4 mr-2" id="content-submit-btn">Submit Button</button>
              <button type="button" class="btn btn-reset px-4" id="content-reset-btn">Reset Button</button>
            </div>
          </form>
        </div>
      </div>
    `;
  }

  if (filename.indexOf('edit-profile.html') !== -1) {
    return `
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h1 class="h3 font-weight-bold text-dark">Edit Profile</h1>
      </div>
      
      <div id="profile-alert-container"></div>

      <div class="card cms-card">
        <div class="card-header cms-card-header">
          Profile Form Elements
        </div>
        <div class="card-body cms-card-body">
          <form id="edit-profile-form">
            <div class="form-group mb-4">
              <label for="profile-firstname" class="font-weight-semibold text-dark">First Name</label>
              <input type="text" class="form-control" id="profile-firstname" placeholder="Enter the first name" maxlength="30">
              <div class="invalid-feedback">First Name is required (3-30 chars).</div>
            </div>

            <div class="form-group mb-4">
              <label for="profile-lastname" class="font-weight-semibold text-dark">Last Name</label>
              <input type="text" class="form-control" id="profile-lastname" placeholder="Enter the last name" maxlength="30">
              <div class="invalid-feedback">Last Name is required (3-30 chars).</div>
            </div>

            <div class="form-group mb-4">
              <label class="font-weight-semibold text-dark d-block">Email</label>
              <div class="text-muted py-2 font-weight-medium" id="profile-email" style="font-size: 0.95rem;">your_email@example.com</div>
            </div>

            <div class="form-group mb-4">
              <label for="profile-phone" class="font-weight-semibold text-dark">Phone</label>
              <input type="text" class="form-control" id="profile-phone" placeholder="Enter your phone number" maxlength="13">
              <div class="invalid-feedback">Phone is required (9-13 numeric digits).</div>
            </div>

            <div class="form-group mb-4">
              <label for="profile-description" class="font-weight-semibold text-dark">Description</label>
              <textarea class="form-control" id="profile-description" rows="4" placeholder="Enter details about yourself" maxlength="200"></textarea>
              <div class="invalid-feedback">Description must not exceed 200 characters.</div>
            </div>

            <div class="form-group mb-0">
              <button type="submit" class="btn btn-green px-4 mr-2" id="profile-submit-btn">Submit Button</button>
              <button type="button" class="btn btn-reset px-4" id="profile-reset-btn">Reset Button</button>
            </div>
          </form>
        </div>
      </div>
    `;
  }
  return '';
}

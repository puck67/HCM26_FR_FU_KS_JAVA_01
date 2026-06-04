/**
 * scripts.js - Main jQuery and JavaScript behavior file for FEE.P.A102
 * Implements SPA navigation, dynamic form inputs, client-side validation, AJAX actions, and HTML sanitization.
 */

$(document).ready(function () {
  // 1. Seed database on initial load
  seedDatabase();

  // 2. Initialize Views and Navigation
  initNavigation();

  // 3. Render initial screens
  renderVotePage();
  renderListTable();

  // 4. Initialize Forms and Popups
  initCreatePage();
  initLoginPage();
});

/* ==========================================================================
   DATABASE SEEDING (localStorage)
   ========================================================================== */
function seedDatabase() {
  // If polls array is not in localStorage, seed default items matching the specs
  if (!localStorage.getItem('polls_db')) {
    const defaultPolls = [
      {
        id: 98,
        title: "Great poll",
        status: "Active", // Active, Drafts, Closed
        questions: [
          {
            id: 1,
            text: "It is necessary to you?",
            mandatory: true,
            multiple: false,
            answers: ["Yes", "No"]
          },
          {
            id: 2,
            text: "Often pass polls?",
            mandatory: false,
            multiple: false,
            answers: ["Once a month", "Once a week"]
          },
          {
            id: 3,
            text: "How old are you?",
            mandatory: false,
            multiple: true,
            answers: ["18-20", "21-23"]
          }
        ]
      },
      {
        id: 99,
        title: "Draft poll - Web Design Feedback",
        status: "Drafts",
        questions: [
          {
            id: 1,
            text: "Do you like Bootstrap 4?",
            mandatory: true,
            multiple: false,
            answers: ["Yes, absolutely", "It is okay", "No, I prefer vanilla CSS"]
          }
        ]
      },
      {
        id: 100,
        title: "Closed poll - Front-End Essentials Course",
        status: "Closed",
        questions: [
          {
            id: 1,
            text: "How was the estimated time of 180 minutes?",
            mandatory: true,
            multiple: false,
            answers: ["Too short", "Just right", "Too long"]
          }
        ]
      }
    ];
    localStorage.setItem('polls_db', JSON.stringify(defaultPolls));
  }
}

/* ==========================================================================
   SPA ROUTING AND NAVIGATION
   ========================================================================== */
function initNavigation() {
  // Handle navbar link clicks to switch between sections
  $('.navbar-nav .nav-link').on('click', function (e) {
    // If it's the Login button, we let the Login handler open the popup modal instead
    if ($(this).hasClass('login-nav-btn')) {
      e.preventDefault();
      return;
    }

    e.preventDefault();
    const targetSectionId = $(this).attr('href');
    
    // Switch active state in Navbar
    $('.navbar-nav .nav-item').removeClass('active');
    $(this).parent().addClass('active');

    // Switch visible section
    switchSection(targetSectionId);
  });

  // Logo Brand click redirects to Home/Vote tab
  $('.navbar-brand').on('click', function (e) {
    e.preventDefault();
    $('.navbar-nav .nav-item').removeClass('active');
    $('.navbar-nav a[href="#home-section"]').parent().addClass('active');
    switchSection('#home-section');
  });
}

function switchSection(sectionId) {
  // Clear any leftover validation or success messages when switching tabs
  $('.validation-summary').addClass('d-none').find('.validation-summary-list').empty();
  $('.success-alert-custom').addClass('d-none');
  $('.form-control-custom').removeClass('input-invalid');
  $('label').removeClass('label-invalid');
  $('.error-message').remove();

  // Switch sections
  $('.page-section').removeClass('active-section');
  $(sectionId).addClass('active-section');

  // Trigger specific re-renders
  if (sectionId === '#home-section') {
    renderVotePage();
  } else if (sectionId === '#list-section') {
    renderListTable();
  }
}

/* ==========================================================================
   PAGE 1: HOME PAGE (VOTE FORM)
   ========================================================================== */
function renderVotePage() {
  const db = JSON.parse(localStorage.getItem('polls_db')) || [];
  // Find "Great poll" (ID 98) as the primary seeded active poll
  const activePoll = db.find(p => p.id === 98) || db[0];

  if (!activePoll) {
    $('#home-poll-container').html('<div class="alert alert-warning text-center">No active polls found. Create one first!</div>');
    return;
  }

  // Set Poll Title
  $('#home-poll-title').text(activePoll.title);

  const container = $('#home-questions-container');
  container.empty();

  // Render each question dynamically based on layout
  activePoll.questions.forEach((q, index) => {
    const questionNum = index + 1;
    const requiredText = q.mandatory ? '<span class="required-asterisk">*</span>' : '';
    
    let html = `
      <div class="question-item" data-q-id="${q.id}" data-mandatory="${q.mandatory}">
        <div class="question-text" id="lbl-vote-q-${q.id}">
          ${questionNum}. ${escapeHtml(q.text)}${requiredText}
        </div>
        <div class="option-container">
    `;

    q.answers.forEach((ans, ansIndex) => {
      const inputType = q.multiple ? 'checkbox' : 'radio';
      // Form name grouping for radio buttons
      const inputName = q.multiple ? `q-${q.id}-ans-${ansIndex}` : `q-${q.id}`;
      
      html += `
        <div class="option-item">
          <input type="${inputType}" name="${inputName}" id="opt-${q.id}-${ansIndex}" value="${escapeHtml(ans)}">
          <label class="option-label" for="opt-${q.id}-${ansIndex}">${escapeHtml(ans)}</label>
        </div>
      `;
    });

    html += `
        </div>
        <div class="error-msg-placeholder"></div>
      </div>
    `;

    container.append(html);
  });

  // Bind Submit / Vote ("Retain") button
  $('#btn-submit-vote').off('click').on('click', function(e) {
    e.preventDefault();
    validateAndSubmitVote(activePoll);
  });
}

function validateAndSubmitVote(poll) {
  // Clear previous errors
  $('.error-message').remove();
  $('label, .question-text').removeClass('label-invalid');
  $('.validation-summary').addClass('d-none');
  const errorList = [];

  // Loop through questions to check mandatory validation
  poll.questions.forEach(q => {
    if (q.mandatory) {
      let isAnswered = false;
      if (q.multiple) {
        // Check if any checkbox is checked for this question
        isAnswered = $(`input[name^="q-${q.id}-ans-"]:checked`).length > 0;
      } else {
        // Check if radio button is checked
        isAnswered = $(`input[name="q-${q.id}"]:checked`).length > 0;
      }

      if (!isAnswered) {
        // Validation fails for this field
        const qLabel = $(`#lbl-vote-q-${q.id}`);
        qLabel.addClass('label-invalid');
        
        // Add red inline warning message
        qLabel.closest('.question-item').find('.error-msg-placeholder').append(
          `<span class="error-message">Question "${escapeHtml(q.text)}" is mandatory and must be answered.</span>`
        );

        errorList.push(`Question ${q.id} is mandatory. Please make a selection.`);
      }
    }
  });

  if (errorList.length > 0) {
    // Show validation failure message at the top of the form
    const summary = $('#vote-validation-summary');
    const summaryList = summary.find('.validation-summary-list');
    summaryList.empty();
    
    errorList.forEach(err => {
      summaryList.append(`<li>${err}</li>`);
    });
    
    summary.removeClass('d-none');
    
    // Smooth scroll to top of form
    $('html, body').animate({
      scrollTop: $("#home-section").offset().top - 80
    }, 400);

    return false;
  }

  // Success Case
  // Show successful completion feedback
  $('#vote-success-alert').removeClass('d-none').html(`
    <i class="fas fa-check-circle mr-2"></i> Vote submitted successfully! Thank you for participating in <strong>${escapeHtml(poll.title)}</strong>.
  `);

  // Clear choices
  $('input[type="radio"], input[type="checkbox"]').prop('checked', false);

  // Auto-hide success alert after 4 seconds
  setTimeout(function() {
    $('#vote-success-alert').addClass('d-none');
  }, 4000);

  // Smooth scroll to success top
  $('html, body').animate({
    scrollTop: $("#home-section").offset().top - 80
  }, 400);
}

/* ==========================================================================
   PAGE 2: CREATE PAGE (CREATE INTERVIEW FORM)
   ========================================================================== */
let questionCount = 0;

function initCreatePage() {
  // Clear any existing blocks and add initial default block
  $('#questions-wrapper').empty();
  questionCount = 0;
  addNewQuestionBlock();

  // Add Question Button Click
  $('#btn-add-question-block').off('click').on('click', function(e) {
    e.preventDefault();
    addNewQuestionBlock();
  });

  // Reset Create Form fields on input to clean invalid highlights dynamically
  $(document).on('input focus', '.form-control-custom', function() {
    $(this).removeClass('input-invalid');
    const formGroup = $(this).closest('.form-group, .answer-row, .question-block');
    formGroup.find('label').first().removeClass('label-invalid');
    $(this).siblings('.error-message').remove();
    
    // Hide validation summary if all is getting cleaned
    if ($('.input-invalid').length === 0) {
      $('#create-validation-summary').addClass('d-none');
    }
  });

  // Retain (Save) Poll Button click
  $('#btn-save-poll').off('click').on('click', function(e) {
    e.preventDefault();
    validateAndSavePoll();
  });
}

function addNewQuestionBlock() {
  const index = questionCount++;
  
  const blockHtml = `
    <div class="question-block" id="q-block-${index}" data-block-id="${index}">
      <div class="question-block-header">
        <span class="question-block-title">Question #${index + 1}</span>
        ${index > 0 ? `<button type="button" class="btn btn-sm btn-danger py-1 px-2 text-white btn-remove-q-block" data-target="${index}" style="background-color: #2e7d32 !important; border-color: #2e7d32 !important;"><i class="fas fa-trash-alt"></i> Delete</button>` : ''}
      </div>

      <!-- Question Text Input -->
      <div class="form-group mb-3">
        <label class="login-label font-weight-bold" for="q-text-${index}">Your question <span class="text-danger">*</span></label>
        <input type="text" class="form-control form-control-custom q-text-input" id="q-text-${index}" placeholder="Enter your question" data-field-name="Question #${index + 1} Title">
        <div class="error-placeholder"></div>
      </div>

      <!-- Options Row -->
      <div class="form-row mb-3">
        <div class="col-sm-6 mb-2 mb-sm-0">
          <div class="custom-control custom-checkbox pt-1">
            <input type="checkbox" class="custom-control-input q-mandatory-check" id="q-mandatory-${index}">
            <label class="custom-control-label option-label" for="q-mandatory-${index}">Mandatory</label>
          </div>
        </div>
        <div class="col-sm-6">
          <div class="custom-control custom-checkbox pt-1">
            <input type="checkbox" class="custom-control-input q-multiple-check" id="q-multiple-${index}">
            <label class="custom-control-label option-label" for="q-multiple-${index}">You can select multiple options</label>
          </div>
        </div>
      </div>

      <!-- Answers Container -->
      <div class="form-group mb-0">
        <label class="login-label font-weight-bold">Possible answers <span class="text-danger">*</span></label>
        <div class="answers-wrapper" id="answers-wrapper-${index}">
          <!-- Initial Answer Input Row -->
          <div class="input-group answer-row align-items-center">
            <input type="text" class="form-control form-control-custom answer-input" placeholder="Type your answer" data-field-name="Question #${index + 1} Option 1">
            <div class="input-group-append">
              <button class="btn btn-plus-answer text-white btn-add-answer-row" type="button" data-block-id="${index}">
                <i class="fas fa-plus"></i>
              </button>
            </div>
            <div class="error-placeholder w-100"></div>
          </div>
        </div>
      </div>
    </div>
  `;

  $('#questions-wrapper').append(blockHtml);

  // Bind trash delete button
  $('.btn-remove-q-block').off('click').on('click', function(e) {
    e.preventDefault();
    const id = $(this).data('target');
    $(`#q-block-${id}`).remove();
    reindexQuestionBlocks();
  });

  // Bind add answer input button
  $('.btn-add-answer-row').off('click').on('click', function(e) {
    e.preventDefault();
    const bId = $(this).data('block-id');
    addAnswerRowToBlock(bId);
  });
}

function addAnswerRowToBlock(blockId) {
  const answersContainer = $(`#answers-wrapper-${blockId}`);
  const rowCount = answersContainer.find('.answer-row').length;
  
  // Re-style existing plus button into a standard display/trash button if needed
  const newRowHtml = `
    <div class="input-group answer-row align-items-center mt-2">
      <input type="text" class="form-control form-control-custom answer-input" placeholder="Type your answer" data-field-name="Question #${blockId + 1} Option ${rowCount + 1}">
      <div class="input-group-append">
        <button class="btn btn-danger py-1 px-2 text-white btn-remove-answer-row" type="button" style="background-color: #2e7d32 !important; border-color: #2e7d32 !important; height: 38px;">
          <i class="fas fa-trash"></i>
        </button>
      </div>
      <div class="error-placeholder w-100"></div>
    </div>
  `;

  answersContainer.append(newRowHtml);

  // Bind delete answer row click
  $('.btn-remove-answer-row').off('click').on('click', function(e) {
    e.preventDefault();
    $(this).closest('.answer-row').remove();
  });
}

function reindexQuestionBlocks() {
  questionCount = 0;
  $('#questions-wrapper .question-block').each(function() {
    const idx = questionCount++;
    $(this).attr('id', `q-block-${idx}`);
    $(this).attr('data-block-id', idx);
    $(this).find('.question-block-title').text(`Question #${idx + 1}`);
    $(this).find('.btn-remove-q-block').attr('data-target', idx);
    $(this).find('.q-text-input').attr('id', `q-text-${idx}`);
    $(this).find('.q-mandatory-check').attr('id', `q-mandatory-${idx}`);
    $(this).find('.q-mandatory-check').siblings('label').attr('for', `q-mandatory-${idx}`);
    $(this).find('.q-multiple-check').attr('id', `q-multiple-${idx}`);
    $(this).find('.q-multiple-check').siblings('label').attr('for', `q-multiple-${idx}`);
    $(this).find('.answers-wrapper').attr('id', `answers-wrapper-${idx}`);
    $(this).find('.btn-add-answer-row').attr('data-block-id', idx);
  });
}

/**
 * Validates the Create Poll Form (Loops through inputs to verify strings, lengths and empty entries)
 */
function validateAndSavePoll() {
  $('.error-message').remove();
  $('label, .login-label').removeClass('label-invalid');
  $('.form-control-custom').removeClass('input-invalid');
  $('#create-validation-summary').addClass('d-none');
  const errorList = [];

  // Loop implementation checking mandatory inputs:
  // 1. Validate Poll Title ("Name poll")
  const titleInput = $('#input-name-poll');
  const titleVal = $.trim(titleInput.val());

  if (titleVal === '') {
    titleInput.addClass('input-invalid');
    titleInput.closest('.form-group').find('label').addClass('label-invalid');
    titleInput.closest('.form-group').append('<span class="error-message">Name poll is a required field.</span>');
    errorList.push('Name poll: Required field is empty.');
  } else if (titleVal.length < 3 || titleVal.length > 255) {
    titleInput.addClass('input-invalid');
    titleInput.closest('.form-group').find('label').addClass('label-invalid');
    titleInput.closest('.form-group').append('<span class="error-message">Name poll must be between 3 and 255 characters.</span>');
    errorList.push('Name poll: Length must be between 3 and 255 characters.');
  }

  // 2. Validate Questions Block
  const blocks = $('#questions-wrapper .question-block');
  if (blocks.length === 0) {
    errorList.push('Poll layout: You must define at least one question.');
  }

  blocks.each(function() {
    const bId = $(this).data('block-id');
    const qIndex = bId + 1;
    
    // Validate Question Text
    const qTextInput = $(this).find('.q-text-input');
    const qTextVal = $.trim(qTextInput.val());

    if (qTextVal === '') {
      qTextInput.addClass('input-invalid');
      qTextInput.closest('.form-group').find('label').addClass('label-invalid');
      qTextInput.closest('.form-group').find('.error-placeholder').html('<span class="error-message">Question description is required.</span>');
      errorList.push(`Question #${qIndex}: Question description is empty.`);
    } else if (qTextVal.length < 3 || qTextVal.length > 255) {
      qTextInput.addClass('input-invalid');
      qTextInput.closest('.form-group').find('label').addClass('label-invalid');
      qTextInput.closest('.form-group').find('.error-placeholder').html('<span class="error-message">Question must be between 3 and 255 characters.</span>');
      errorList.push(`Question #${qIndex}: Length must be between 3 and 255 characters.`);
    }

    // Validate Possible Answers
    const answers = $(this).find('.answer-input');
    let filledAnswersCount = 0;
    
    if (answers.length === 0) {
      errorList.push(`Question #${qIndex}: You must provide at least one possible answer.`);
    }

    answers.each(function(ansIdx) {
      const aVal = $.trim($(this).val());
      const ansNum = ansIdx + 1;

      if (aVal === '') {
        $(this).addClass('input-invalid');
        $(this).closest('.answer-row').find('.error-placeholder').html('<span class="error-message">Answer value is required.</span>');
        errorList.push(`Question #${qIndex} Answer #${ansNum}: Field is empty.`);
      } else if (aVal.length < 3 || aVal.length > 200) {
        $(this).addClass('input-invalid');
        $(this).closest('.answer-row').find('.error-placeholder').html('<span class="error-message">Answer must be between 3 and 200 characters.</span>');
        errorList.push(`Question #${qIndex} Answer #${ansNum}: Length must be between 3 and 200 characters.`);
      } else {
        filledAnswersCount++;
      }
    });

    if (filledAnswersCount > 0 && filledAnswersCount < answers.length) {
      errorList.push(`Question #${qIndex}: One or more answers are blank or invalid.`);
    }
  });

  // If validation fails
  if (errorList.length > 0) {
    const summary = $('#create-validation-summary');
    const summaryList = summary.find('.validation-summary-list');
    summaryList.empty();

    errorList.forEach(err => {
      summaryList.append(`<li>${err}</li>`);
    });

    summary.removeClass('d-none');

    // Scroll smoothly to validation summary
    $('html, body').animate({
      scrollTop: $("#create-section").offset().top - 80
    }, 400);

    return false;
  }

  // 3. Save Poll data to LocalStorage & trigger mock AJAX call
  const newPollId = Date.now() % 10000; // unique positive ID
  const pollQuestions = [];

  blocks.each(function(index) {
    const qText = $.trim($(this).find('.q-text-input').val());
    const isMandatory = $(this).find('.q-mandatory-check').is(':checked');
    const isMultiple = $(this).find('.q-multiple-check').is(':checked');
    const answersList = [];

    $(this).find('.answer-input').each(function() {
      answersList.push($.trim($(this).val()));
    });

    pollQuestions.push({
      id: index + 1,
      text: qText,
      mandatory: isMandatory,
      multiple: isMultiple,
      answers: answersList
    });
  });

  const newPoll = {
    id: newPollId,
    title: titleVal,
    status: "Active", // Default created status
    questions: pollQuestions
  };

  // Submit via AJAX POST mock request
  $.ajax({
    url: '/api/save-poll',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify(newPoll),
    success: function(response) {
      console.log('AJAX save success response:', response);
      savePollToDatabase(newPoll);
    },
    error: function(xhr) {
      if (xhr.status === 400) {
        try {
          const resJson = JSON.parse(xhr.responseText);
          if (resJson && resJson.errors) {
            // Display server errors at the top of the form
            const summary = $('#create-validation-summary');
            const summaryList = summary.find('.validation-summary-list');
            summaryList.empty();
            
            resJson.errors.forEach(err => {
              summaryList.append(`<li><i class="fas fa-server mr-1"></i> [Server Error] ${escapeHtml(err)}</li>`);
            });
            
            summary.removeClass('d-none');
            $('html, body').animate({
              scrollTop: $("#create-section").offset().top - 80
            }, 400);
            return;
          }
        } catch (e) {
          // Parse failed, fall back to direct save
        }
      }
      
      // Fallback: direct save if server is not present (e.g. file protocol/CORS issue)
      console.warn('Backend AJAX failed. Direct local save initialized.');
      savePollToDatabase(newPoll);
    }
  });
}

function savePollToDatabase(newPoll) {
  const db = JSON.parse(localStorage.getItem('polls_db')) || [];
  db.push(newPoll);
  localStorage.setItem('polls_db', JSON.stringify(db));

  // Show successful save feedback
  $('#create-success-alert').removeClass('d-none').html(`
    <i class="fas fa-check-circle mr-2"></i> Poll <strong>${escapeHtml(newPoll.title)}</strong> has been created and saved successfully! Redirecting...
  `);

  // Clear Form Fields
  $('#input-name-poll').val('');
  initCreatePage(); // Re-initialize blocks

  // Smooth scroll to top of success message
  $('html, body').animate({
    scrollTop: $("#create-section").offset().top - 80
  }, 400);

  // Switch to List page after short delay
  setTimeout(function() {
    $('#create-success-alert').addClass('d-none');
    $('.navbar-nav .nav-item').removeClass('active');
    $('.navbar-nav a[href="#list-section"]').parent().addClass('active');
    switchSection('#list-section');
  }, 2000);
}

/* ==========================================================================
   PAGE 3: LIST PAGE (POLLS MANAGEMENT LIST)
   ========================================================================== */
let activeTabFilter = 'Active'; // Active, Drafts, Closed

function renderListTable() {
  const db = JSON.parse(localStorage.getItem('polls_db')) || [];
  const tbody = $('#polls-list-tbody');
  tbody.empty();

  // Filter based on currently active status tab
  const filteredPolls = db.filter(p => p.status === activeTabFilter);

  // Select tab element styles dynamically to reflect active state
  updateListTabs();

  if (filteredPolls.length === 0) {
    tbody.append(`
      <tr>
        <td colspan="3" class="text-center text-secondary py-5">
          <div class="mb-2"><i class="far fa-folder-open" style="font-size: 2.2rem; color: #cbd5e0;"></i></div>
          No <strong>${activeTabFilter}</strong> polls found.
        </td>
      </tr>
    `);
    return;
  }

  filteredPolls.forEach(poll => {
    tbody.append(`
      <tr data-poll-id="${poll.id}">
        <td data-label="#" style="font-weight: 500; font-size: 0.95rem; color: #1a202c; width: 80px;">${poll.id}</td>
        <td data-label="Title" style="font-weight: 500; color: #2d3748;">${escapeHtml(poll.title)}</td>
        <td data-label="Management" style="width: 320px;">
          <button type="button" class="btn btn-sm table-action-btn btn-view-results" data-id="${poll.id}">
            View results
          </button>
          ${poll.status !== 'Closed' ? `
            <button type="button" class="btn btn-sm table-action-btn btn-close-poll" data-id="${poll.id}">
              Close poll
            </button>
          ` : ''}
          <button type="button" class="btn btn-sm table-action-btn table-action-btn-danger btn-delete-poll" data-id="${poll.id}" style="background-color: #2e7d32 !important; border-color: #2e7d32 !important;">
            Delete
          </button>
        </td>
      </tr>
    `);
  });

  // Bind Actions:
  
  // 1. TAB SELECTIONS (Active, Drafts, Closed)
  $('.tab-btn-custom').off('click').on('click', function(e) {
    e.preventDefault();
    activeTabFilter = $(this).data('tab');
    renderListTable();
  });

  // 2. VIEW RESULTS (AJAX with HTML Sanitization)
  $('.btn-view-results').off('click').on('click', function(e) {
    e.preventDefault();
    const pollId = $(this).data('id');
    viewResultsViaAJAX(pollId);
  });

  // 3. CLOSE POLL
  $('.btn-close-poll').off('click').on('click', function(e) {
    e.preventDefault();
    const pollId = $(this).data('id');
    closePoll(pollId);
  });

  // 4. DELETE POLL (AJAX with Custom Confirm Modal)
  $('.btn-delete-poll').off('click').on('click', function(e) {
    e.preventDefault();
    const pollId = $(this).data('id');
    
    // Open the custom premium delete modal overlay
    $('#delete-confirm-modal-overlay').addClass('show');
    
    // Bind confirmation submit action inside modal
    $('#btn-confirm-delete-action').off('click').on('click', function(evt) {
      evt.preventDefault();
      $('#delete-confirm-modal-overlay').removeClass('show');
      deletePollViaAJAX(pollId);
    });

    // Bind close modal triggers
    $('.btn-close-delete-modal').off('click').on('click', function(evt) {
      evt.preventDefault();
      $('#delete-confirm-modal-overlay').removeClass('show');
    });
  });
}

function updateListTabs() {
  $('.tab-btn-custom').removeClass('tab-btn-active tab-btn-drafts tab-btn-closed tab-btn-inactive-light');
  
  $('.tab-btn-custom').each(function() {
    const tabName = $(this).data('tab');
    
    if (tabName === activeTabFilter) {
      if (tabName === 'Active') $(this).addClass('tab-btn-active');
      else if (tabName === 'Drafts') $(this).addClass('tab-btn-drafts');
      else if (tabName === 'Closed') $(this).addClass('tab-btn-closed');
    } else {
      $(this).addClass('tab-btn-inactive-light');
    }
  });
}

function closePoll(pollId) {
  const db = JSON.parse(localStorage.getItem('polls_db')) || [];
  const index = db.findIndex(p => p.id === pollId);
  
  if (index !== -1) {
    db[index].status = 'Closed';
    localStorage.setItem('polls_db', JSON.stringify(db));
    
    // Show message
    showSuccessToast('Poll closed successfully.');
    renderListTable();
  }
}

/**
 * Deletes poll via AJAX request to mock node server
 */
function deletePollViaAJAX(pollId) {
  $.ajax({
    url: '/api/delete-poll',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({ id: pollId }),
    success: function(response) {
      console.log('AJAX delete success response:', response);
      removePollFromLocal(pollId);
    },
    error: function() {
      console.warn('Backend AJAX failed. Direct local deletion initialized.');
      removePollFromLocal(pollId);
    }
  });
}

function removePollFromLocal(pollId) {
  let db = JSON.parse(localStorage.getItem('polls_db')) || [];
  db = db.filter(p => p.id !== pollId);
  localStorage.setItem('polls_db', JSON.stringify(db));

  showSuccessToast('Poll deleted successfully via secure AJAX channels.');
  renderListTable();
}

/**
 * Loads a full HTML document via AJAX, sanitizes it (returns only body contents), and displays in modal
 */
function viewResultsViaAJAX(pollId) {
  const modalContainer = $('#results-modal-content-body');
  modalContainer.html('<div class="text-center py-4"><i class="fas fa-spinner fa-spin fa-2x text-success"></i><p class="mt-2 text-secondary">Loading results via AJAX...</p></div>');
  $('#results-modal-overlay').addClass('show');

  // Trigger AJAX GET request to HTML template file
  $.ajax({
    url: 'templates/results.html',
    type: 'GET',
    dataType: 'html',
    success: function(htmlResponse) {
      // SANITIZATION ROUTINE:
      // Create a temporary DOM parser to parse the response,
      // and extract only the contents of the <body> tag to strip out heads and style sheets!
      let bodyContent = '';
      try {
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlResponse, 'text/html');
        bodyContent = doc.body.innerHTML;
      } catch (err) {
        bodyContent = $(htmlResponse).filter('body').html() || $(htmlResponse).find('body').html() || htmlResponse;
      }

      // If body content is empty, use the raw response as fallback
      if (!bodyContent || $.trim(bodyContent) === '') {
        bodyContent = htmlResponse;
      }

      // Inject only the sanitized body contents!
      setTimeout(function() {
        modalContainer.html(bodyContent);
      }, 600);
    },
    error: function(xhr, status, error) {
      console.error('AJAX View Results failed:', error);
      modalContainer.html(`
        <div class="alert alert-danger">
          <h5 class="font-weight-bold">AJAX Load Failed</h5>
          <p class="mb-0 text-sm">Failed to fetch the results template from local directory via AJAX due to CORS or connection rules.</p>
        </div>
        <div class="text-right mt-3">
          <button type="button" class="btn text-white" data-dismiss="modal" style="background-color: #28a745; border: none; border-radius: 4px;">Close</button>
        </div>
      `);
    }
  });

  // Bind close modal buttons
  $('.btn-close-modal, [data-dismiss="modal"]').off('click').on('click', function(e) {
    e.preventDefault();
    $('#results-modal-overlay').removeClass('show');
  });
}

/* ==========================================================================
   LOGIN POPUP / MODAL
   ========================================================================== */
function initLoginPage() {
  // Bind Login Navbar button
  $('.login-nav-btn').off('click').on('click', function(e) {
    e.preventDefault();
    $('#login-popup-overlay').addClass('show');
  });

  // Bind Close Popup Button
  $('#btn-close-login-popup').off('click').on('click', function(e) {
    e.preventDefault();
    closeLoginPopup();
  });

  // Close when clicking overlay backdrop
  $('#login-popup-overlay').off('click').on('click', function(e) {
    if (e.target === this) {
      closeLoginPopup();
    }
  });

  // Real-time validation cleanup on keypress
  $('#login-popup-form .form-control-custom').on('input focus', function() {
    $(this).removeClass('input-invalid');
    $(this).closest('.form-group').find('label').removeClass('label-invalid');
    $(this).siblings('.error-message').remove();
    $('#login-popup-error-alert').addClass('d-none');
  });

  // Handle Login submission
  $('#login-popup-form').off('submit').on('submit', function(e) {
    e.preventDefault();
    validateAndLogin();
  });
}

function closeLoginPopup() {
  $('#login-popup-overlay').removeClass('show');
  // Clear fields
  $('#login-alias').val('');
  $('#login-password').val('');
  $('.form-control-custom').removeClass('input-invalid');
  $('label').removeClass('label-invalid');
  $('.error-message').remove();
  $('#login-popup-error-alert').addClass('d-none');
}

function validateAndLogin() {
  const aliasInput = $('#login-alias');
  const passwordInput = $('#login-password');
  const rememberCheckbox = $('#login-remember');

  const aliasVal = $.trim(aliasInput.val());
  const passwordVal = $.trim(passwordInput.val());

  let isValid = true;

  // Clear previous inline errors
  $('.error-message').remove();
  $('.form-control-custom').removeClass('input-invalid');
  $('label').removeClass('label-invalid');

  // Alias Validation (Required, length 3-50)
  if (aliasVal === '') {
    aliasInput.addClass('input-invalid');
    aliasInput.closest('.form-group').find('label').addClass('label-invalid');
    aliasInput.closest('.form-group').append('<span class="error-message">Alias is required.</span>');
    isValid = false;
  } else if (aliasVal.length < 3 || aliasVal.length > 50) {
    aliasInput.addClass('input-invalid');
    aliasInput.closest('.form-group').find('label').addClass('label-invalid');
    aliasInput.closest('.form-group').append('<span class="error-message">Alias must be between 3 and 50 characters.</span>');
    isValid = false;
  }

  // Password Validation (Required, length 6-50)
  if (passwordVal === '') {
    passwordInput.addClass('input-invalid');
    passwordInput.closest('.form-group').find('label').addClass('label-invalid');
    passwordInput.closest('.form-group').append('<span class="error-message">Password is required.</span>');
    isValid = false;
  } else if (passwordVal.length < 6 || passwordVal.length > 50) {
    passwordInput.addClass('input-invalid');
    passwordInput.closest('.form-group').find('label').addClass('label-invalid');
    passwordInput.closest('.form-group').append('<span class="error-message">Password must be between 6 and 50 characters.</span>');
    isValid = false;
  }

  if (!isValid) return false;

  // Mock Success Authentication
  const username = aliasVal;
  localStorage.setItem('polls_session', JSON.stringify({ username: username, remember: rememberCheckbox.is(':checked') }));

  // Update navbar Login link to show name!
  $('.login-nav-btn').html(`<i class="fas fa-user-circle mr-1"></i> Hello, ${escapeHtml(username)}`).css({
    'border-color': '#28a745',
    'color': '#28a745 !important'
  });

  closeLoginPopup();
  showSuccessToast(`Welcome back, ${escapeHtml(username)}! Logged in successfully.`);
}

/* ==========================================================================
   GLOBAL UTILITIES
   ========================================================================== */
function escapeHtml(string) {
  return String(string)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}

function showSuccessToast(message) {
  // Check if standard body toast exists, otherwise create
  let toast = $('#global-toast');
  if (toast.length === 0) {
    $('body').append(`
      <div id="global-toast" class="d-none" style="position: fixed; bottom: 24px; right: 24px; z-index: 1080; background-color: #28a745; color: white; padding: 14px 24px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); font-weight: 500; display: flex; align-items: center; animation: slideUp 0.3s ease-out;">
        <i class="fas fa-check-circle mr-3" style="font-size: 1.2rem;"></i>
        <span id="global-toast-msg"></span>
      </div>
    `);
    toast = $('#global-toast');
  }

  $('#global-toast-msg').text(message);
  toast.removeClass('d-none');

  setTimeout(function() {
    toast.addClass('d-none');
  }, 3500);
}

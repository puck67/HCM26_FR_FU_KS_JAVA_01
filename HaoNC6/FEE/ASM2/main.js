// Application State
let pollsList = [
  {
    id: 93,
    name: "Great poll",
    status: "active",
    questions: [
      {
        text: "It is necessary to you?",
        mandatory: true,
        multiple: false,
        answers: ["Yes", "No"]
      },
      {
        text: "Often pass polls?",
        mandatory: false,
        multiple: false,
        answers: ["Once a month", "Once a week"]
      },
      {
        text: "How old are you?",
        mandatory: false,
        multiple: true,
        answers: ["18-20", "21-23"]
      }
    ]
  }
];

let currentTab = "active";
let activePollForVoting = pollsList[0]; // Default active poll

$(document).ready(function() {
  // --- Navigation & Routing ---
  function switchSection(sectionId, navLinkId) {
    $(".app-section").addClass("d-none");
    $(sectionId).removeClass("d-none");
    
    $(".navbar-nav .nav-item").removeClass("active");
    $(navLinkId).closest("li").addClass("active");
  }

  $("#nav-vote").click(function(e) {
    e.preventDefault();
    switchSection("#vote-section", "#nav-vote");
    renderVotePage();
  });

  $("#nav-create").click(function(e) {
    e.preventDefault();
    switchSection("#create-section", "#nav-create");
    clearCreateForm();
  });

  $("#nav-list").click(function(e) {
    e.preventDefault();
    switchSection("#list-section", "#nav-list");
    renderPollsTable(currentTab);
  });

  $("#brand-logo").click(function(e) {
    e.preventDefault();
    $("#nav-vote").click();
  });

  // --- Login Modal (AJAX Loading) ---
  $("#btn-login-trigger").click(function() {
    $.ajax({
      url: "login.html",
      type: "GET",
      dataType: "html",
      success: function(data) {
        let bodyContent = "";
        let bodyStart = data.indexOf("<body>");
        let bodyEnd = data.indexOf("</body>");
        
        if (bodyStart !== -1 && bodyEnd !== -1) {
          bodyContent = data.substring(bodyStart + 6, bodyEnd);
        } else {
          let tempDiv = $("<div>").html(data);
          bodyContent = tempDiv.find("body").html() || data;
        }

        $("#login-modal-content").html(bodyContent);

        $("#loginModal").modal("show");

        bindLoginFormEvents();
      },
      error: function(xhr, status, error) {
        console.error("AJAX Error loading login form:", error);
        alert("Could not load the login form from the server. Check your file path.");
      }
    });
  });

  function bindLoginFormEvents() {
    $("#login-form").on("submit", function(e) {
      e.preventDefault();
      
      $("#login-error-container").empty();
      $("#login-form .custom-input").removeClass("is-invalid-custom");
      $("#login-form label").removeClass("invalid-label-custom");
      $("#login-form .invalid-feedback-custom").text("");

      let hasErrors = false;
      let errorMessages = [];

      $("#login-form input[type='text'], #login-form input[type='password']").each(function() {
        let val = $(this).val().trim();
        let fieldName = $(this).attr("name");
        let labelText = $(this).siblings("label").text().replace(":", "").trim();

        if (val === "") {
          hasErrors = true;
          $(this).addClass("is-invalid-custom");
          $(this).siblings("label").addClass("invalid-label-custom");
          $(this).siblings(".invalid-feedback-custom").text(`${labelText} is required.`);
          errorMessages.push(`${labelText} cannot be blank.`);
        }
      });

      if (hasErrors) {
        let errorHtml = `
          <div class="alert alert-danger" role="alert">
            <h6 class="font-weight-bold">Validation Failed</h6>
            <p class="mb-0">Please check the required fields highlighted in red below and fill in the missing data.</p>
          </div>
        `;
        $("#login-error-container").html(errorHtml);
      } else {
        let alias = $("#login-alias").val().trim();
        let successHtml = `
          <div class="alert alert-success" role="alert">
            Signed in successfully as <strong>${alias}</strong>!
          </div>
        `;
        $("#login-error-container").html(successHtml);

        $("#btn-login-trigger").text(`Sign out (${alias})`).removeClass("btn-outline-light").addClass("btn-success");
        
        $("#btn-login-trigger").off("click").click(function() {
          if (confirm("Do you want to sign out?")) {
            location.reload();
          }
        });

        setTimeout(function() {
          $("#loginModal").modal("hide");
        }, 1200);
      }
    });
  }

  let questionCounter = 1;

  $(document).on("click", ".btn-add-answer", function() {
    let wrapper = $(this).closest(".answers-wrapper");
    let qIndex = wrapper.data("qindex");
    
    let newRow = $(`
      <div class="answer-item-row d-flex align-items-center mb-2">
        <div class="flex-grow-1 mr-2 grp-answer-text">
          <input type="text" class="form-control custom-input" name="questions[${qIndex}][answers][]" placeholder="Type your answer">
          <div class="invalid-feedback-custom"></div>
        </div>
      </div>
    `);

    $(this).remove();

    let plusBtn = $('<button type="button" class="btn btn-primary btn-add-answer font-weight-bold" style="border-radius: 4px; height: 38px; width: 38px;">+</button>');
    newRow.append(plusBtn);

    wrapper.append(newRow);
  });

  $("#btn-add-question").click(function() {
    let qIndex = questionCounter++;
    
    let questionHtml = `
      <div class="card card-question mb-4 p-4 border border-light shadow-xs bg-light-gray animate-fade" data-index="${qIndex}">
        <div class="d-flex justify-content-between align-items-center mb-3">
          <h6 class="font-weight-bold text-dark mb-0">Question ${qIndex + 1}</h6>
          <button type="button" class="btn btn-sm btn-danger btn-remove-question py-1 px-2" style="background-color: #dc3545 !important; border-color: #dc3545 !important;">Remove</button>
        </div>
        
        <div class="form-group mb-3 grp-question-text">
          <input type="text" class="form-control custom-input" name="questions[${qIndex}][text]" placeholder="Enter your question">
          <div class="invalid-feedback-custom"></div>
        </div>

        <div class="form-row mb-3">
          <div class="col-md-6 mb-2">
            <div class="custom-control custom-checkbox">
              <input type="checkbox" class="custom-control-input" id="mandatory-${qIndex}" name="questions[${qIndex}][mandatory]">
              <label class="custom-control-label text-secondary" for="mandatory-${qIndex}">Mandatory</label>
            </div>
          </div>
          <div class="col-md-6 mb-2">
            <div class="custom-control custom-checkbox">
              <input type="checkbox" class="custom-control-input" id="multiple-${qIndex}" name="questions[${qIndex}][multiple]">
              <label class="custom-control-label text-secondary" for="multiple-${qIndex}">You can select multiple options</label>
            </div>
          </div>
        </div>

        <div class="answers-wrapper" data-qindex="${qIndex}">
          <label class="form-label font-weight-bold text-secondary text-sm">Possible answers</label>
          <div class="answer-item-row d-flex align-items-center mb-2">
            <div class="flex-grow-1 mr-2 grp-answer-text">
              <input type="text" class="form-control custom-input" name="questions[${qIndex}][answers][]" placeholder="Type your answer">
              <div class="invalid-feedback-custom"></div>
            </div>
            <button type="button" class="btn btn-primary btn-add-answer font-weight-bold" style="border-radius: 4px; height: 38px; width: 38px;">+</button>
          </div>
        </div>
      </div>
    `;

    $("#create-questions-wrapper").append(questionHtml);
  });

  $(document).on("click", ".btn-remove-question", function() {
    $(this).closest(".card-question").remove();
    reindexQuestions();
  });

  function reindexQuestions() {
    $("#create-questions-wrapper .card-question").each(function(index) {
      $(this).attr("data-index", index);
      $(this).find("h6").first().text(`Question ${index + 1}`);
      
      // Update inputs names and IDs
      $(this).find("input[name*='questions']").each(function() {
        let currentName = $(this).attr("name");
        let updatedName = currentName.replace(/questions\[\d+\]/, `questions[${index}]`);
        $(this).attr("name", updatedName);
      });

      // Update checkboxes attributes
      let mandatoryCheckbox = $(this).find("input[name*='[mandatory]']");
      mandatoryCheckbox.attr("id", `mandatory-${index}`);
      mandatoryCheckbox.next("label").attr("for", `mandatory-${index}`);

      let multipleCheckbox = $(this).find("input[name*='[multiple]']");
      multipleCheckbox.attr("id", `multiple-${index}`);
      multipleCheckbox.next("label").attr("for", `multiple-${index}`);

      // Update answers wrapper data attribute
      $(this).find(".answers-wrapper").attr("data-qindex", index);
    });
    
    // Update the counter
    questionCounter = $("#create-questions-wrapper .card-question").length;
  }

  // --- Create Poll Validation & Submission ---
  $("#create-poll-form").on("submit", function(e) {
    e.preventDefault();

    // Reset styles
    $("#create-error-container").empty();
    $(".custom-input").removeClass("is-invalid-custom");
    $(".form-group, .grp-question-text, .grp-answer-text").removeClass("invalid-label-custom");
    $(".invalid-feedback-custom").text("");

    let errorMessages = [];
    let hasErrors = false;

    // 1. Validate Poll Name
    let pollNameEl = $("#poll-name");
    let pollNameVal = pollNameEl.val().trim();
    if (pollNameVal === "") {
      hasErrors = true;
      pollNameEl.addClass("is-invalid-custom");
      $("#grp-poll-name").addClass("invalid-label-custom");
      pollNameEl.siblings(".invalid-feedback-custom").text("Name poll is required.");
      errorMessages.push("Name poll is required and must not be empty.");
    } else if (pollNameVal.length < 3) {
      hasErrors = true;
      pollNameEl.addClass("is-invalid-custom");
      $("#grp-poll-name").addClass("invalid-label-custom");
      pollNameEl.siblings(".invalid-feedback-custom").text("Name poll must be at least 3 characters.");
      errorMessages.push("Name poll must be between 3 and 255 characters.");
    } else if (pollNameVal.length > 255) {
      hasErrors = true;
      pollNameEl.addClass("is-invalid-custom");
      $("#grp-poll-name").addClass("invalid-label-custom");
      pollNameEl.siblings(".invalid-feedback-custom").text("Name poll must not exceed 255 characters.");
      errorMessages.push("Name poll must be between 3 and 255 characters.");
    }

    // 2. Validate Questions and Answers
    $(".card-question").each(function() {
      let qIndex = $(this).attr("data-index");
      let questionInput = $(this).find(`input[name='questions[${qIndex}][text]']`);
      let questionVal = questionInput.val().trim();
      let qNumText = parseInt(qIndex) + 1;

      // Validate question text
      if (questionVal === "") {
        hasErrors = true;
        questionInput.addClass("is-invalid-custom");
        questionInput.siblings(".invalid-feedback-custom").text("Question text is required.");
        errorMessages.push(`Question ${qNumText}: Question text is required.`);
      } else if (questionVal.length < 3 || questionVal.length > 255) {
        hasErrors = true;
        questionInput.addClass("is-invalid-custom");
        questionInput.siblings(".invalid-feedback-custom").text("Question text must be between 3 and 255 characters.");
        errorMessages.push(`Question ${qNumText}: Question text must be between 3 and 255 characters.`);
      }

      // Validate answers
      let answersWrapper = $(this).find(".answers-wrapper");
      let answerInputs = answersWrapper.find("input[type='text']");
      let filledAnswersCount = 0;

      answerInputs.each(function(ansIndex) {
        let ansVal = $(this).val().trim();
        let ansNumText = ansIndex + 1;

        if (ansVal === "") {
          hasErrors = true;
          $(this).addClass("is-invalid-custom");
          $(this).siblings(".invalid-feedback-custom").text("Answer is required.");
          errorMessages.push(`Question ${qNumText}, Answer ${ansNumText}: Answer cannot be blank.`);
        } else if (ansVal.length < 3 || ansVal.length > 200) {
          hasErrors = true;
          $(this).addClass("is-invalid-custom");
          $(this).siblings(".invalid-feedback-custom").text("Answer must be between 3 and 200 characters.");
          errorMessages.push(`Question ${qNumText}, Answer ${ansNumText}: Answer must be between 3 and 200 characters.`);
        } else {
          filledAnswersCount++;
        }
      });

      if (filledAnswersCount === 0 && !hasErrors) {
        hasErrors = true;
        errorMessages.push(`Question ${qNumText}: At least one answer option is required.`);
      }
    });

    if (hasErrors) {
      // Display errors at the top of the form
      let errorsListHtml = errorMessages.map(msg => `<li>${msg}</li>`).join("");
      let errorContainerHtml = `
        <div class="alert alert-danger" role="alert">
          <h5 class="font-weight-bold">Form Validation Failed</h5>
          <p>Please fix the following issues to submit the poll successfully:</p>
          <ul class="pl-3 mb-0">${errorsListHtml}</ul>
        </div>
      `;
      $("#create-error-container").html(errorContainerHtml);
      
      // Scroll to the top of the card/form to make error visible
      $("html, body").animate({ scrollTop: $("#create-error-container").offset().top - 100 }, 300);
    } else {
      // Form is valid! Build new poll object
      let newPoll = {
        id: Math.floor(Math.random() * 900) + 100, // Generate random ID between 100 and 999
        name: pollNameVal,
        status: "active",
        questions: []
      };

      $(".card-question").each(function() {
        let qIndex = $(this).attr("data-index");
        let questionVal = $(this).find(`input[name='questions[${qIndex}][text]']`).val().trim();
        let isMandatory = $(this).find(`input[name='questions[${qIndex}][mandatory]']`).is(":checked");
        let isMultiple = $(this).find(`input[name='questions[${qIndex}][multiple]']`).is(":checked");
        
        let answers = [];
        $(this).find(".answers-wrapper input[type='text']").each(function() {
          answers.push($(this).val().trim());
        });

        newPoll.questions.push({
          text: questionVal,
          mandatory: isMandatory,
          multiple: isMultiple,
          answers: answers
        });
      });

      pollsList.push(newPoll);
      activePollForVoting = newPoll; // Set the newly created poll as active for vote view

      // Show success feedback
      let successHtml = `
        <div class="alert alert-success alert-dismissible fade show" role="alert">
          <strong>Success!</strong> Poll "<strong>${newPoll.name}</strong>" has been created successfully.
          <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
      `;
      $("#create-error-container").html(successHtml);
      
      // Clear/Reset form
      clearCreateForm();
      
      // Navigate to List page to view the new item
      setTimeout(function() {
        $("#nav-list").click();
      }, 1500);
    }
  });

  function clearCreateForm() {
    $("#create-poll-form")[0].reset();
    
    // Clear question list to keep only one initial question with one initial answer
    $("#create-questions-wrapper").html(`
      <div class="card card-question mb-4 p-4 border border-light shadow-xs bg-light-gray" data-index="0">
        <div class="form-group mb-3 grp-question-text">
          <input type="text" class="form-control custom-input" name="questions[0][text]" placeholder="Enter your question">
          <div class="invalid-feedback-custom"></div>
        </div>

        <div class="form-row mb-3">
          <div class="col-md-6 mb-2">
            <div class="custom-control custom-checkbox">
              <input type="checkbox" class="custom-control-input" id="mandatory-0" name="questions[0][mandatory]">
              <label class="custom-control-label text-secondary" for="mandatory-0">Mandatory</label>
            </div>
          </div>
          <div class="col-md-6 mb-2">
            <div class="custom-control custom-checkbox">
              <input type="checkbox" class="custom-control-input" id="multiple-0" name="questions[0][multiple]">
              <label class="custom-control-label text-secondary" for="multiple-0">You can select multiple options</label>
            </div>
          </div>
        </div>

        <div class="answers-wrapper" data-qindex="0">
          <label class="form-label font-weight-bold text-secondary text-sm">Possible answers</label>
          <div class="answer-item-row d-flex align-items-center mb-2">
            <div class="flex-grow-1 mr-2 grp-answer-text">
              <input type="text" class="form-control custom-input" name="questions[0][answers][]" placeholder="Type your answer">
              <div class="invalid-feedback-custom"></div>
            </div>
            <button type="button" class="btn btn-primary btn-add-answer font-weight-bold" style="border-radius: 4px; height: 38px; width: 38px;">+</button>
          </div>
        </div>
      </div>
    `);
    
    questionCounter = 1;
  }

  // --- List Page: Render Polls Table & AJAX Delete ---
  $(".btn-tab").click(function() {
    $(".btn-tab").removeClass("active");
    $(this).addClass("active");
    
    currentTab = $(this).data("status");
    renderPollsTable(currentTab);
  });

  function renderPollsTable(status) {
    let tbody = $("#polls-table-body");
    tbody.empty();

    let filteredPolls = pollsList.filter(p => p.status === status);

    if (filteredPolls.length === 0) {
      tbody.append(`
        <tr>
          <td colspan="3" class="text-center text-muted py-4">No ${status} polls found. Create one to get started!</td>
        </tr>
      `);
      return;
    }

    filteredPolls.forEach(poll => {
      let rowHtml = `
        <tr data-id="${poll.id}">
          <td class="align-middle font-weight-bold">${poll.id}</td>
          <td class="align-middle font-weight-bold text-secondary">${poll.name}</td>
          <td class="text-center align-middle">
            <button class="btn btn-sm btn-green btn-view-results mx-1 font-weight-bold text-white" data-id="${poll.id}">View results</button>
            <button class="btn btn-sm btn-green btn-clear-poll mx-1 font-weight-bold text-white" data-id="${poll.id}">Clear poll</button>
            <button class="btn btn-sm btn-green btn-delete-poll mx-1 font-weight-bold text-white" style="background-color: #dc3545 !important; border-color: #dc3545 !important;" data-id="${poll.id}">Delete</button>
          </td>
        </tr>
      `;
      tbody.append(rowHtml);
    });
  }

  // Handle Poll Actions
  $(document).on("click", ".btn-view-results", function() {
    let pollId = parseInt($(this).data("id"));
    let poll = pollsList.find(p => p.id === pollId);
    if (!poll) return;

    // Fill results modal
    $("#resultsModalLabel").text(`Results for: ${poll.name}`);
    let resultsHtml = `
      <h6 class="font-weight-bold mb-3 text-secondary">Structure: ${poll.questions.length} question(s)</h6>
      <hr>
    `;

    poll.questions.forEach((q, idx) => {
      resultsHtml += `
        <div class="mb-4">
          <p class="font-weight-bold text-dark mb-2">${idx + 1}. ${q.text} ${q.mandatory ? '<span class="text-danger">*</span>' : ''}</p>
          <ul class="list-group">
            ${q.answers.map(ans => `
              <li class="list-group-item d-flex justify-content-between align-items-center py-2">
                <span>${ans}</span>
                <span class="badge badge-success badge-pill font-weight-bold">0 votes (0%)</span>
              </li>
            `).join("")}
          </ul>
        </div>
      `;
    });

    $("#results-modal-body").html(resultsHtml);
    $("#resultsModal").modal("show");
  });

  $(document).on("click", ".btn-clear-poll", function() {
    let pollId = $(this).data("id");
    alert(`Poll #${pollId} responses cleared successfully.`);
  });

  $(document).on("click", ".btn-delete-poll", function() {
    let pollId = parseInt($(this).data("id"));
    let poll = pollsList.find(p => p.id === pollId);
    if (!poll) return;

    // Confirm box: "Are you sure you want to delete this item?"
    if (confirm("Are you sure you want to delete this item?")) {
      // Send data to server to delete this item via Ajax
      $.ajax({
        url: "delete_mock.json",
        type: "GET", // In a real server this would likely be POST or DELETE
        dataType: "json",
        success: function(response) {
          if (response.success) {
            // Remove from list
            pollsList = pollsList.filter(p => p.id !== pollId);
            
            // Re-render
            renderPollsTable(currentTab);

            // Display successful deletion alert
            let deleteSuccessHtml = `
              <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>Deleted!</strong> Poll "${poll.name}" has been deleted successfully via AJAX.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                  <span aria-hidden="true">&times;</span>
                </button>
              </div>
            `;
            $("#list-messages").html(deleteSuccessHtml);
          } else {
            alert("Failed to delete poll from server.");
          }
        },
        error: function(xhr, status, error) {
          console.error("AJAX Delete Error:", error);
          alert("Error sending delete request via AJAX.");
        }
      });
    }
  });

  // --- Vote Section: Render active poll & submit ---
  function renderVotePage() {
    $("#vote-messages").empty();
    
    if (pollsList.length === 0) {
      $("#vote-poll-title").text("No active polls");
      $("#vote-form").addClass("d-none");
      return;
    } else {
      $("#vote-form").removeClass("d-none");
    }

    // Set default vote poll if active one is deleted
    if (!pollsList.includes(activePollForVoting)) {
      activePollForVoting = pollsList[0];
    }

    $("#vote-poll-title").text(activePollForVoting.name);
    let container = $("#vote-questions-container");
    container.empty();

    activePollForVoting.questions.forEach((q, qIndex) => {
      let mandatoryHtml = q.mandatory ? ' <span class="text-danger">*</span>' : '';
      let questionBlock = $(`
        <div class="vote-question-block mb-4" data-index="${qIndex}" data-required="${q.mandatory}">
          <h6 class="font-weight-bold text-secondary mb-2">${qIndex + 1}. ${q.text}${mandatoryHtml}</h6>
        </div>
      `);

      q.answers.forEach((ans, ansIndex) => {
        let inputType = q.multiple ? "checkbox" : "radio";
        let inputName = q.multiple ? `v_q${qIndex}[]` : `v_q${qIndex}`;
        let inputId = `v_ans_${qIndex}_${ansIndex}`;

        let itemHtml = `
          <div class="form-check mb-2">
            <input class="form-check-input" type="${inputType}" name="${inputName}" id="${inputId}" value="${ans}">
            <label class="form-check-label text-dark" for="${inputId}">${ans}</label>
          </div>
        `;
        questionBlock.append(itemHtml);
      });

      container.append(questionBlock);
    });
  }

  $("#vote-form").on("submit", function(e) {
    e.preventDefault();
    $("#vote-messages").empty();
    $(".vote-question-block h6").removeClass("invalid-label-custom");

    let hasErrors = false;
    let missingQuestions = [];

    // Loop through each question in active poll
    activePollForVoting.questions.forEach((q, qIndex) => {
      if (q.mandatory) {
        let nameAttr = q.multiple ? `v_q${qIndex}[]` : `v_q${qIndex}`;
        let isChecked = false;

        // Check if any check/radio is selected for this question
        if (q.multiple) {
          isChecked = $(`input[name="v_q${qIndex}[]"]:checked`).length > 0;
        } else {
          isChecked = $(`input[name="v_q${qIndex}"]:checked`).length > 0;
        }

        if (!isChecked) {
          hasErrors = true;
          missingQuestions.push(qIndex + 1);
          $(`.vote-question-block[data-index="${qIndex}"] h6`).addClass("invalid-label-custom");
        }
      }
    });

    if (hasErrors) {
      let errorHtml = `
        <div class="alert alert-danger" role="alert">
          Please answer the mandatory questions (Question ${missingQuestions.join(", ")}) before submitting.
        </div>
      `;
      $("#vote-messages").html(errorHtml);
      $("html, body").animate({ scrollTop: $("#vote-messages").offset().top - 100 }, 200);
    } else {
      let successHtml = `
        <div class="alert alert-success" role="alert">
          <strong>Thank you!</strong> Your vote in "<strong>${activePollForVoting.name}</strong>" has been recorded successfully.
        </div>
      `;
      $("#vote-messages").html(successHtml);
      $("#vote-form")[0].reset();
      
      // Scroll to message
      $("html, body").animate({ scrollTop: $("#vote-messages").offset().top - 100 }, 200);
    }
  });

  // Initial load
  renderVotePage();
  renderPollsTable("active");
});

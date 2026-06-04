
// app.js - Polls Management Application Logic



// 1. IN-MEMORY STATE (LOCAL DATABASE)
let polls = [
    {
        id: 1,
        name: "Great poll",
        status: "active",
        questions: [
            {
                text: "it is necessary to you?",
                mandatory: true,
                allowMultiple: false,
                answers: ["Yes", "No"]
            },
            {
                text: "Often pass polls?",
                mandatory: false,
                allowMultiple: false,
                answers: ["Once a month", "Once a week"]
            },
            {
                text: "How old are you?",
                mandatory: false,
                allowMultiple: false,
                answers: ["18-20", "21-23"]
            }
        ]
    },
    {
        id: 2,
        name: "Website Feedback Survey",
        status: "draft",
        questions: [
            {
                text: "How do you rate the visual design?",
                mandatory: true,
                allowMultiple: false,
                answers: ["Excellent", "Good", "Average", "Poor"]
            },
            {
                text: "Which features do you use most?",
                mandatory: false,
                allowMultiple: true,
                answers: ["Vote page", "Create page", "Manage list", "Login popup"]
            }
        ]
    },
    {
        id: 3,
        name: "Workplace Amenities Poll",
        status: "closed",
        questions: [
            {
                text: "Which coffee flavor is your favorite?",
                mandatory: true,
                allowMultiple: true,
                answers: ["Arabica", "Robusta", "Cappuccino", "Latte"]
            }
        ]
    }
];

// Current logged-in user state
let currentUser = null;

// Track active view and active poll in voting view
let activePollId = 1;
let currentListFilter = "active";


// 2. DOCUMENT READY INITIALIZER
$(document).ready(function () {

    switchView("vote"); // Start by displaying the Vote (Home) page

    //  Navigation & Login Modal
    $("#login-btn").on("click", function (e) {
        e.preventDefault();
        if (currentUser) {
            // If already logged in, clicking the button triggers log out
            logoutUser();
        } else {
            // Otherwise, open the login popup modal
            openLoginModal();
        }
    });

    // Close modal
    $("#login-modal-close-x, #login-modal-close-btn").on("click", function () {
        closeLoginModal();
    });

    // Close modal when clicking outside the modal-card
    $("#login-modal-backdrop").on("click", function (e) {
        if ($(e.target).is("#login-modal-backdrop")) {
            closeLoginModal();
        }
    });

    // Handle Login Form Submit
    $("#login-form").on("submit", function (e) {
        e.preventDefault();
        handleLoginSubmit();
    });

    //Vote Page Submit
    $("#vote-form").on("submit", function (e) {
        e.preventDefault();
        handleVoteSubmit();
    });

    //Create Poll Page
    resetCreateForm();

    // Add a new question block in the builder
    $("#btn-add-question-block").on("click", function () {
        addQuestionBlock();
    });

    // Handle Create Poll Form Submit
    $("#create-poll-form").on("submit", function (e) {
        e.preventDefault();
        handleCreatePollSubmit();
    });
});


// 3. SINGLE PAGE ROUTING (VIEW SWITCHING)
function switchView(viewName) {
    // Remove active class from all navbar items
    $(".navbar-nav .nav-item").removeClass("active");

    // Hide all view sections
    $(".view-section").removeClass("active-view");

    // Add active class to corresponding navbar item and show view section
    if (viewName === "vote") {
        $("#nav-vote").addClass("active");
        $("#vote-view").addClass("active-view");
        renderVoteView();
    } else if (viewName === "create") {
        $("#nav-create").addClass("active");
        $("#create-view").addClass("active-view");
    } else if (viewName === "list") {
        $("#nav-list").addClass("active");
        $("#list-view").addClass("active-view");
        renderPollList(currentListFilter);
    }
}

// Show global alert message at top of application
function showGlobalAlert(messageHtml, alertType = "success", duration = 5000) {
    let alertId = "alert-" + Date.now();
    let alertHtml = `
        <div id="${alertId}" class="alert alert-${alertType} alert-dismissible fade show" role="alert">
            ${messageHtml}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    `;

    $("#global-alert-container").append(alertHtml);

    // Auto remove after duration
    setTimeout(function () {
        $(`#${alertId}`).alert("close");
    }, duration);
}


// 4. LOGIN POPUP & AUTHENTICATION FLOW
function openLoginModal() {
    // Clear previous error messages & inputs
    $("#login-error-container").hide().text("");
    $("#login-alias").val("").removeClass("input-invalid");
    $("#login-password").val("").removeClass("input-invalid");
    $("#err-login-alias").text("");
    $("#err-login-password").text("");

    // Show modal by adding bootstrap backdrop class and css styling
    $("#login-modal-backdrop").addClass("show-modal");
    $("body").css("overflow", "hidden"); // Prevent scrolling main page while modal is open
}

function closeLoginModal() {
    $("#login-modal-backdrop").removeClass("show-modal");
    $("body").css("overflow", "auto");
}

function handleLoginSubmit() {
    let alias = $("#login-alias").val().trim();
    let password = $("#login-password").val().trim();
    let isValid = true;

    // Reset error styling
    $("#login-alias").removeClass("input-invalid");
    $("#err-login-alias").text("");
    $("#login-password").removeClass("input-invalid");
    $("#err-login-password").text("");
    $("#login-error-container").hide();

    // Validate Alias
    if (!alias) {
        $("#login-alias").addClass("input-invalid");
        $("#err-login-alias").text("Alias (username) is required.");
        isValid = false;
    } else if (alias.length < 3) {
        $("#login-alias").addClass("input-invalid");
        $("#err-login-alias").text("Alias must be at least 3 characters.");
        isValid = false;
    }

    // Validate Password
    if (!password) {
        $("#login-password").addClass("input-invalid");
        $("#err-login-password").text("Password is required.");
        isValid = false;
    } else if (password.length < 6) {
        $("#login-password").addClass("input-invalid");
        $("#err-login-password").text("Password must be at least 6 characters.");
        isValid = false;
    }

    if (!isValid) {
        // Show notification at the top of modal
        $("#login-error-container").text("Please correct the invalid fields below.").show();
        return;
    }

    // Mock successful authentication
    currentUser = { alias: alias };
    closeLoginModal();

    // Update navbar login indicator
    $("#login-btn").html(`<i class="fas fa-user-circle mr-1"></i> Logout (${alias})`);

    showGlobalAlert(`<strong>Welcome back, ${alias}!</strong> You have successfully signed in.`, "success");
}

function logoutUser() {
    let oldAlias = currentUser ? currentUser.alias : "";
    currentUser = null;
    $("#login-btn").html(`<i class="fas fa-sign-in-alt mr-1"></i> Login`);
    showGlobalAlert(`Goodbye, ${oldAlias}. You have logged out.`, "info");
}


// 5. VOTE VIEW FUNCTIONALITY

function renderVoteView() {
    let voteContainer = $("#vote-questions-container");
    voteContainer.empty();

    // Get the selected active poll (or the first active poll in database)
    let activePoll = polls.find(p => p.id === activePollId && p.status === "active");

    // Fallback if the selected poll is not active or not found
    if (!activePoll) {
        activePoll = polls.find(p => p.status === "active");
    }

    if (!activePoll) {
        voteContainer.html(`
            <div class="text-center py-5">
                <i class="fas fa-ban fa-3x text-muted mb-3"></i>
                <h4 class="text-muted">No active polls available at the moment.</h4>
                <p class="text-muted">Go to the <strong>Create</strong> tab to make a new poll, then set it to active!</p>
            </div>
        `);
        $("#vote-form button[type='submit']").hide();
        $("#vote-poll-title").text("Surveys");
        return;
    }

    // Update the UI title
    activePollId = activePoll.id;
    $("#vote-poll-title").text(activePoll.name);
    $("#vote-form button[type='submit']").show();

    // Render questions
    activePoll.questions.forEach((q, index) => {
        let questionHtml = `
            <div class="form-group mb-4 p-3 bg-light rounded border-left border-success vote-question-card" 
                 data-mandatory="${q.mandatory}" 
                 data-question-index="${index}">
                
                <label class="h5 font-weight-600 d-block vote-question-label">
                    ${index + 1}. ${q.text} 
                    ${q.mandatory ? '<span class="required-star">*</span>' : ''}
                </label>
                <div class="invalid-msg mb-2" id="err-vote-q-${index}"></div>
                <div class="answers-choices-list mt-3">
        `;

        q.answers.forEach((ans, ansIndex) => {
            let inputId = `choice-${activePoll.id}-${index}-${ansIndex}`;
            let inputType = q.allowMultiple ? "checkbox" : "radio";
            let inputName = `question-${activePoll.id}-${index}`;

            questionHtml += `
                <div class="custom-control custom-${inputType} mb-2">
                    <input type="${inputType}" 
                           class="custom-control-input vote-input-choice" 
                           id="${inputId}" 
                           name="${inputName}" 
                           value="${ans}">
                    <label class="custom-control-label font-weight-normal" for="${inputId}">
                        ${ans}
                    </label>
                </div>
            `;
        });

        questionHtml += `
                </div>
            </div>
        `;
        voteContainer.append(questionHtml);
    });
}

function handleVoteSubmit() {
    let activePoll = polls.find(p => p.id === activePollId);
    if (!activePoll) return;

    let hasErrors = false;

    // Clear previous error messages & labels
    $(".vote-question-card").css("border-color", "#27ae60");
    $(".vote-question-label").removeClass("label-invalid");
    $(".vote-question-card .invalid-msg").text("");

    let errorMessages = [];

    // Loop through each question card and check validation
    $(".vote-question-card").each(function () {
        let card = $(this);
        let qIndex = card.data("question-index");
        let isMandatory = card.data("mandatory") === true;
        let question = activePoll.questions[qIndex];

        if (isMandatory) {
            // Check if any radio/checkbox option is checked inside this card
            let checkedOptions = card.find(".vote-input-choice:checked");
            if (checkedOptions.length === 0) {
                hasErrors = true;

                // Styling the invalid fields as requested (red line/border, red label, clear message)
                card.css("border-color", "#e74c3c");
                card.find(".vote-question-label").addClass("label-invalid");
                card.find(".invalid-msg").text("This field is mandatory. Please select an option.");

                errorMessages.push(`Question ${qIndex + 1} ("${question.text}") is required.`);
            }
        }
    });

    if (hasErrors) {
        // Show validation alert summary at top of voting card (simulating form feedback guidelines)
        let errorSummaryHtml = `
            <div class="alert alert-danger" id="vote-error-summary">
                <h5><i class="fas fa-exclamation-triangle mr-2"></i> Error: Missing required responses</h5>
                <ul class="mb-0">
                    ${errorMessages.map(msg => `<li>${msg}</li>`).join("")}
                </ul>
            </div>
        `;
        // Put at top of vote form
        $("#vote-error-summary").remove(); // remove older
        $("#vote-form").prepend(errorSummaryHtml);

        // Scroll form to top to see error message
        $('html, body').animate({
            scrollTop: $("#vote-view").offset().top - 20
        }, 500);
        return;
    }

    // Success response!
    $("#vote-error-summary").remove();
    showGlobalAlert("<strong>Success!</strong> Your responses have been recorded. Thank you for voting!", "success");

    // Reset inputs
    $(".vote-input-choice").prop("checked", false);
}

// 6. CREATE INTERVIEW / POLL BUILDER VIEW
let questionCounter = 0;

function resetCreateForm() {
    questionCounter = 0;
    $("#questions-builder-container").empty();
    $("#poll-name").val("").removeClass("input-invalid");
    $("#err-poll-name").text("");
    $("#validation-summary").hide();
    $("#validation-summary-list").empty();

    // Add default first question block
    addQuestionBlock();
}

// Dynamically appends a complete question config block to the builder
function addQuestionBlock() {
    questionCounter++;
    let qId = questionCounter;

    let questionBlockHtml = `
        <div class="question-block" id="q-block-${qId}" data-id="${qId}">
            <button type="button" class="btn btn-sm btn-remove-question" onclick="removeQuestionBlock(${qId})">
                <i class="fas fa-trash-alt mr-1"></i> Remove
            </button>
            
            <h4 class="font-weight-bold text-success mb-3">Question #${qId}</h4>
            
            <!-- Question text input -->
            <div class="form-group mb-3">
                <label for="q-text-${qId}" class="font-weight-600 q-label">
                    Question Text <span class="required-star">*</span>
                </label>
                <input type="text" class="form-control q-text-input" id="q-text-${qId}" placeholder="Enter your question">
                <div class="invalid-msg" id="err-q-text-${qId}"></div>
            </div>

            <!-- Configuration checkboxes -->
            <div class="form-row mb-3">
                <div class="col-sm-6 mb-2">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input q-mandatory" id="q-mandatory-${qId}">
                        <label class="custom-control-label font-weight-500" for="q-mandatory-${qId}">Mandatory (Required to answer)</label>
                    </div>
                </div>
                <div class="col-sm-6 mb-2">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input q-multiple" id="q-multiple-${qId}">
                        <label class="custom-control-label font-weight-500" for="q-multiple-${qId}">You can select multiple options</label>
                    </div>
                </div>
            </div>

            <!-- Answers Builder Section -->
            <div class="answers-section">
                <label class="font-weight-600 q-answers-label">Possible Answers <span class="required-star">*</span></label>
                <div class="invalid-msg mb-2" id="err-q-answers-${qId}"></div>
                
                <!-- Container for already added answers -->
                <div class="answers-list mb-3" id="answers-list-${qId}">
                    <!-- Appended rows go here -->
                </div>
                
                <!-- Row for adding a new answer option -->
                <div class="input-group">
                    <input type="text" class="form-control new-answer-input" id="new-answer-input-${qId}" 
                           placeholder="Type your answer" 
                           onkeypress="handleAnswerKeyPress(event, ${qId})">
                    <div class="input-group-append">
                        <button type="button" class="btn btn-add-answer px-3" onclick="addAnswerRow(${qId})">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;

    $("#questions-builder-container").append(questionBlockHtml);

    // Add two initial blank answers by default to help user start
    addAnswerRowWithValue(qId, "Yes");
    addAnswerRowWithValue(qId, "No");
}

function removeQuestionBlock(qId) {
    // Allow deleting only if there is more than 1 question block
    if ($(".question-block").length <= 1) {
        alert("You must include at least one question in your poll.");
        return;
    }
    $(`#q-block-${qId}`).remove();
    reindexQuestionLabels();
}

function reindexQuestionLabels() {
    let index = 1;
    $(".question-block").each(function () {
        $(this).find("h4").text(`Question #${index}`);
        index++;
    });
}

// Add answer when pressing Enter key in the answer input field
function handleAnswerKeyPress(event, qId) {
    if (event.key === "Enter") {
        event.preventDefault();
        addAnswerRow(qId);
    }
}

// Adds an answer text input row dynamically from the add input box
function addAnswerRow(qId) {
    let inputField = $(`#new-answer-input-${qId}`);
    let answerVal = inputField.val().trim();

    if (!answerVal) {
        inputField.addClass("input-invalid");
        setTimeout(() => inputField.removeClass("input-invalid"), 1000);
        return;
    }

    addAnswerRowWithValue(qId, answerVal);
    inputField.val(""); // Clear input box
}

// Helper to append the answer row to the HTML list container
function addAnswerRowWithValue(qId, value) {
    let listContainer = $(`#answers-list-${qId}`);
    let optionId = "ans-" + qId + "-" + Date.now() + Math.floor(Math.random() * 100);

    let answerRowHtml = `
        <div class="answer-item mb-2" id="${optionId}">
            <input type="text" class="form-control ans-value-input" value="${value}" placeholder="Answer option">
            <button type="button" class="btn btn-danger btn-remove-answer btn-sm" onclick="removeAnswerRow('${optionId}')">
                <i class="fas fa-times"></i>
            </button>
        </div>
    `;
    listContainer.append(answerRowHtml);
}

function removeAnswerRow(optionId) {
    $(`#${optionId}`).remove();
}

// Validation logic for the create poll form
function handleCreatePollSubmit() {
    let pollName = $("#poll-name").val().trim();
    let isValid = true;
    let errorMsgs = [];

    // 1. Reset visual errors
    $("#poll-name").removeClass("input-invalid");
    $("#grp-poll-name label").removeClass("label-invalid");
    $("#err-poll-name").text("");

    $(".question-block").each(function () {
        let block = $(this);
        block.find(".q-text-input").removeClass("input-invalid");
        block.find(".q-label").removeClass("label-invalid");
        block.find(".invalid-msg").text("");
        block.find(".ans-value-input").removeClass("input-invalid");
        block.find(".q-answers-label").removeClass("label-invalid");
    });

    $("#validation-summary").hide();
    $("#validation-summary-list").empty();

    // 2. Validate Poll Name
    if (!pollName) {
        $("#poll-name").addClass("input-invalid");
        $("#grp-poll-name label").addClass("label-invalid");
        $("#err-poll-name").text("Poll name is required.");
        errorMsgs.push("Poll Name: Cannot be empty.");
        isValid = false;
    } else if (pollName.length < 3 || pollName.length > 255) {
        $("#poll-name").addClass("input-invalid");
        $("#grp-poll-name label").addClass("label-invalid");
        $("#err-poll-name").text("Poll name must be between 3 and 255 characters.");
        errorMsgs.push("Poll Name: Must be between 3 and 255 characters long.");
        isValid = false;
    }

    // 3. Validate Question Blocks
    let activeBlocks = $(".question-block");
    if (activeBlocks.length === 0) {
        errorMsgs.push("Form: You must build at least one question block.");
        isValid = false;
    }

    activeBlocks.each(function (index) {
        let block = $(this);
        let qId = block.data("id");
        let labelIndex = index + 1;

        let qText = block.find(".q-text-input").val().trim();
        let mandatory = block.find(".q-mandatory").is(":checked");
        let allowMultiple = block.find(".q-multiple").is(":checked");

        // Validate Question Text
        if (!qText) {
            block.find(".q-text-input").addClass("input-invalid");
            block.find(".q-label").addClass("label-invalid");
            block.find(`#err-q-text-${qId}`).text("Question text is required.");
            errorMsgs.push(`Question #${labelIndex}: Question text cannot be empty.`);
            isValid = false;
        } else if (qText.length < 3 || qText.length > 255) {
            block.find(".q-text-input").addClass("input-invalid");
            block.find(".q-label").addClass("label-invalid");
            block.find(`#err-q-text-${qId}`).text("Question text must be between 3 and 255 characters.");
            errorMsgs.push(`Question #${labelIndex}: Question text must be between 3 and 255 characters.`);
            isValid = false;
        }

        // Validate Answers
        let answerInputs = block.find(".ans-value-input");
        let answersArr = [];

        if (answerInputs.length < 2) {
            block.find(".q-answers-label").addClass("label-invalid");
            block.find(`#err-q-answers-${qId}`).text("At least 2 possible answers are required.");
            errorMsgs.push(`Question #${labelIndex}: Must have at least 2 answers.`);
            isValid = false;
        } else {
            answerInputs.each(function (ansIdx) {
                let ansVal = $(this).val().trim();
                if (!ansVal) {
                    $(this).addClass("input-invalid");
                    block.find(".q-answers-label").addClass("label-invalid");
                    block.find(`#err-q-answers-${qId}`).text("Answer option fields cannot be blank.");
                    errorMsgs.push(`Question #${labelIndex} Answer ${ansIdx + 1}: Cannot be empty.`);
                    isValid = false;
                } else if (ansVal.length < 3 || ansVal.length > 200) {
                    $(this).addClass("input-invalid");
                    block.find(".q-answers-label").addClass("label-invalid");
                    block.find(`#err-q-answers-${qId}`).text("Answer option must be between 3 and 200 characters.");
                    errorMsgs.push(`Question #${labelIndex} Answer ${ansIdx + 1}: Must be between 3 and 200 characters.`);
                    isValid = false;
                } else {
                    answersArr.push(ansVal);
                }
            });
        }
    });

    if (!isValid) {
        // Render failure messages in list at the top of the form
        let summaryList = $("#validation-summary-list");
        errorMsgs.forEach(function (msg) {
            summaryList.append(`<li>${msg}</li>`);
        });
        $("#validation-summary").show();

        // Smooth scroll to top of form validation summary
        $('html, body').animate({
            scrollTop: $("#create-view").offset().top - 20
        }, 500);
        return;
    }

    // --- FORM IS VALID: SAVE DATA ---
    // Compile and push the new poll data structure
    let newPoll = {
        id: polls.length + 1,
        name: pollName,
        status: "active", // Default new polls as active
        questions: []
    };

    activeBlocks.each(function () {
        let block = $(this);
        let answersArr = [];
        block.find(".ans-value-input").each(function () {
            answersArr.push($(this).val().trim());
        });

        newPoll.questions.push({
            text: block.find(".q-text-input").val().trim(),
            mandatory: block.find(".q-mandatory").is(":checked"),
            allowMultiple: block.find(".q-multiple").is(":checked"),
            answers: answersArr
        });
    });

    // Save into database state
    polls.push(newPoll);

    // Success notification and redirect to List page
    showGlobalAlert(`<strong>Success!</strong> Poll "${newPoll.name}" has been created successfully with ${newPoll.questions.length} questions.`, "success");

    resetCreateForm();
    switchView("list");
}


// 7. LIST / MANAGE VIEW (FILTERING & AJAX DELETION)

function filterPolls(filter, buttonElement) {
    currentListFilter = filter;

    // Toggle active class on filter tab buttons
    $(".tab-filters .btn-tab").removeClass("active-tab");
    $(buttonElement).addClass("active-tab");

    renderPollList(filter);
}

function renderPollList(filter = "active") {
    let tbody = $("#polls-list-tbody");
    tbody.empty();

    // Filter local poll list
    let filteredList = polls.filter(p => p.status === filter);

    if (filteredList.length === 0) {
        tbody.append(`
            <tr>
                <td colspan="3" class="text-center text-muted py-4">
                    No polls found in the <strong>${filter}</strong> category.
                </td>
            </tr>
        `);
        return;
    }

    filteredList.forEach((poll, index) => {
        let rowHtml = `
            <tr id="poll-row-${poll.id}">
                <td><strong>${index + 1}</strong></td>
                <td>
                    <div class="font-weight-bold text-dark">${poll.name}</div>
                    <small class="text-muted">Contains ${poll.questions.length} question(s) &bull; Status: <span class="text-capitalize">${poll.status}</span></small>
                </td>
                <td class="text-right">
                    <button type="button" class="btn btn-sm btn-success mr-1" onclick="viewPollResults(${poll.id})">
                        <i class="fas fa-eye"></i> View
                    </button>
                    ${poll.status === 'active' ? `
                    <button type="button" class="btn btn-sm btn-warning mr-1" onclick="closePoll(${poll.id})" style="background-color: #f39c12 !important; border-color: #f39c12 !important;">
                        <i class="fas fa-lock"></i> Close
                    </button>` : ''}
                    <button type="button" class="btn btn-sm btn-danger" onclick="deletePoll(${poll.id})">
                        <i class="fas fa-trash-alt"></i> Delete
                    </button>
                </td>
            </tr>
        `;
        tbody.append(rowHtml);
    });
}

function viewPollResults(pollId) {
    // Simply sets the active view back to voting and loads this poll
    activePollId = pollId;
    switchView("vote");
}

function closePoll(pollId) {
    let poll = polls.find(p => p.id === pollId);
    if (poll) {
        poll.status = "closed";
        showGlobalAlert(`Poll "${poll.name}" has been successfully closed.`, "info");
        renderPollList(currentListFilter);
    }
}

// AJAX deletion flow implementation
function deletePoll(pollId) {
    let poll = polls.find(p => p.id === pollId);
    if (!poll) return;

    // Show a standard JavaScript confirmation dialog as requested
    let confirmDelete = confirm(`Are you sure you want to delete this item?\nPoll: "${poll.name}"`);

    if (confirmDelete) {
        // Send a dynamic request to server via AJAX (fetching delete_response.html locally)
        $.ajax({
            url: "delete_response.html",
            type: "GET",
            dataType: "html",
            success: function (responseHtml) {
                // Parse the HTML document and extract only its body content (spec requirement)
                // We create an offscreen jQuery element to load and query the parsed HTML
                let parsedDom = $("<div>").append($.parseHTML(responseHtml));

                // Get the inner body content (excluding scripts/head/styling elements)
                let bodyInnerHtml = parsedDom.find("body").html();

                // Fallback: If no body element was parsed, get the container's outer content
                if (!bodyInnerHtml) {
                    bodyInnerHtml = responseHtml;
                }

                // Remove the poll from local JavaScript array
                polls = polls.filter(p => p.id !== pollId);

                // Re-render table items
                renderPollList(currentListFilter);

                // Inform users of successful action using loaded AJAX content
                showGlobalAlert(bodyInnerHtml, "success", 6000);
            },
            error: function (xhr, status, error) {
                // If fetching fails, show error alert
                showGlobalAlert(`<strong>Error!</strong> Failed to communicate with server via AJAX: ${status} - ${error}`, "danger");
            }
        });
    }
}

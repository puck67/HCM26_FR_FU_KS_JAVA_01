
// data structure containing default survey items
let pollDataList = [
    {
        id: 1,
        name: "IT Service Feedback",
        status: "active",
        questions: [
            {
                text: "is the campus wifi speed satisfactory?",
                mandatory: true,
                allowMultiple: false,
                answers: ["Yes", "No"]
            },
            {
                text: "how often do you submit IT support tickets?",
                mandatory: false,
                allowMultiple: false,
                answers: ["Daily", "Weekly", "Rarely"]
            },
            {
                text: "select your department:",
                mandatory: false,
                allowMultiple: false,
                answers: ["IT", "Business", "Language"]
            }
        ]
    },
    {
        id: 2,
        name: "Student Satisfaction Survey",
        status: "draft",
        questions: [
            {
                text: "rate your overall learning experience.",
                mandatory: true,
                allowMultiple: false,
                answers: ["Excellent", "Good", "Satisfactory", "Poor"]
            },
            {
                text: "which learning resources do you use most?",
                mandatory: false,
                allowMultiple: true,
                answers: ["Online Library", "Lab Computers", "Study Room", "LMS Portal"]
            }
        ]
    },
    {
        id: 3,
        name: "Library Resource Usage",
        status: "closed",
        questions: [
            {
                text: "which books do you borrow most frequently?",
                mandatory: true,
                allowMultiple: true,
                answers: ["Programming", "Mathematics", "Science Fiction", "Reference Manuals"]
            }
        ]
    }
];

// session tracking variables
let loggedUser = null;
let selectedPollId = 1;
let activeTabFilter = "active";

// page load initialization
$(document).ready(function () {

    toggleSection("vote"); // start at voting view

    // click event for login button
    $("#auth-toggle-btn").on("click", function (e) {
        e.preventDefault();
        if (loggedUser) {
            userSignOut();
        } else {
            showLoginBox();
        }
    });

    // close login overlay
    $("#close-icon-btn, #close-footer-btn").on("click", function () {
        hideLoginBox();
    });

    // close modal if user clicks gray area
    $("#auth-modal-wrapper").on("click", function (e) {
        if ($(e.target).is("#auth-modal-wrapper")) {
            hideLoginBox();
        }
    });

    // login submit handler
    $("#auth-form-submit").on("submit", function (e) {
        e.preventDefault();
        processLogin();
    });

    // submit answers inside voting form
    $("#voting-form").on("submit", function (e) {
        e.preventDefault();
        submitAnswers();
    });

    // clear the fields on form builder page
    clearCreateForm();

    // add another question block
    $("#add-question-section-btn").on("click", function () {
        insertQuestionSection();
    });

    // submit created poll form
    $("#poll-builder-form").on("submit", function (e) {
        e.preventDefault();
        processCreatePoll();
    });
});

// tab switcher navigation
function toggleSection(viewName) {
    $(".navbar-nav .nav-item").removeClass("active");
    $(".app-view-panel").removeClass("visible-panel");

    if (viewName === "vote") {
        $("#tab-vote").addClass("active");
        $("#voting-section").addClass("visible-panel");
        populateVoteScreen();
    } else if (viewName === "create") {
        $("#tab-create").addClass("active");
        $("#builder-section").addClass("visible-panel");
    } else if (viewName === "list") {
        $("#tab-list").addClass("active");
        $("#manage-section").addClass("visible-panel");
        displayPollTable(activeTabFilter);
    }
}

// display alert at the top of page
function displayMessage(messageHtml, alertType = "success", duration = 5000) {
    let alertId = "alert-" + Date.now();
    let alertHtml = `
        <div id="${alertId}" class="alert alert-${alertType} alert-dismissible fade show" role="alert">
            ${messageHtml}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    `;

    $("#message-box-area").append(alertHtml);

    // remove banner after timer
    setTimeout(function () {
        $(`#${alertId}`).alert("close");
    }, duration);
}

// open login container
function showLoginBox() {
    $("#auth-err-summary").hide().text("");
    $("#alias-input").val("").removeClass("invalid-input-style");
    $("#pwd-input").val("").removeClass("invalid-input-style");
    $("#error-alias-input").text("");
    $("#error-pwd-input").text("");

    $("#auth-modal-wrapper").addClass("overlay-visible");
    $("body").css("overflow", "hidden"); // lock body scroll
}

// close login container
function hideLoginBox() {
    $("#auth-modal-wrapper").removeClass("overlay-visible");
    $("body").css("overflow", "auto");
}

// process login values
function processLogin() {
    let alias = $("#alias-input").val().trim();
    let password = $("#pwd-input").val().trim();
    let isValid = true;

    $("#alias-input").removeClass("invalid-input-style");
    $("#error-alias-input").text("");
    $("#pwd-input").removeClass("invalid-input-style");
    $("#error-pwd-input").text("");
    $("#auth-err-summary").hide();

    // name check
    if (!alias) {
        $("#alias-input").addClass("invalid-input-style");
        $("#error-alias-input").text("Alias (username) is required.");
        isValid = false;
    } else if (alias.length < 3) {
        $("#alias-input").addClass("invalid-input-style");
        $("#error-alias-input").text("Alias must be at least 3 characters.");
        isValid = false;
    }

    // password check
    if (!password) {
        $("#pwd-input").addClass("invalid-input-style");
        $("#error-pwd-input").text("Password is required.");
        isValid = false;
    } else if (password.length < 6) {
        $("#pwd-input").addClass("invalid-input-style");
        $("#error-pwd-input").text("Password must be at least 6 characters.");
        isValid = false;
    }

    if (!isValid) {
        $("#auth-err-summary").text("Please correct the invalid fields below.").show();
        return;
    }

    // mock login action
    loggedUser = { alias: alias };
    hideLoginBox();

    // change nav button text
    $("#auth-toggle-btn").html(`<i class="fas fa-user-circle mr-1"></i> Logout (${alias})`);

    displayMessage(`<strong>Welcome back, ${alias}!</strong> You have successfully signed in.`, "success");
}

// log out action
function userSignOut() {
    let oldAlias = loggedUser ? loggedUser.alias : "";
    loggedUser = null;
    $("#auth-toggle-btn").html(`<i class="fas fa-sign-in-alt mr-1"></i> Login`);
    displayMessage(`Goodbye, ${oldAlias}. You have logged out.`, "info");
}

// load active survey data to page
function populateVoteScreen() {
    let voteContainer = $("#vote-q-list");
    voteContainer.empty();

    let activePoll = pollDataList.find(p => p.id === selectedPollId && p.status === "active");

    // find first active fallback
    if (!activePoll) {
        activePoll = pollDataList.find(p => p.status === "active");
    }

    if (!activePoll) {
        voteContainer.html(`
            <div class="text-center py-5">
                <i class="fas fa-ban fa-3x text-muted mb-3"></i>
                <h4 class="text-muted">No active polls available at the moment.</h4>
                <p class="text-muted">Go to the <strong>Create</strong> tab to make a new poll, then set it to active!</p>
            </div>
        `);
        $("#voting-form button[type='submit']").hide();
        $("#vote-title-text").text("Surveys");
        return;
    }

    selectedPollId = activePoll.id;
    $("#vote-title-text").text(activePoll.name);
    $("#voting-form button[type='submit']").show();

    // render the list of questions
    activePoll.questions.forEach((q, index) => {
        let questionHtml = `
            <div class="form-group mb-4 p-3 bg-light rounded border-left border-success vote-item-card" 
                 data-mandatory="${q.mandatory}" 
                 data-question-index="${index}">
                
                <label class="h5 font-weight-600 d-block vote-item-label">
                    ${index + 1}. ${q.text} 
                    ${q.mandatory ? '<span class="mandatory-mark">*</span>' : ''}
                </label>
                <div class="field-error-msg mb-2" id="error-vote-q-${index}"></div>
                <div class="choices-group mt-3">
        `;

        q.answers.forEach((ans, ansIndex) => {
            let inputId = `choice-${activePoll.id}-${index}-${ansIndex}`;
            let inputType = q.allowMultiple ? "checkbox" : "radio";
            let inputName = `question-${activePoll.id}-${index}`;

            questionHtml += `
                <div class="custom-control custom-${inputType} mb-2">
                    <input type="${inputType}" 
                           class="custom-control-input choice-input-item" 
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

// submit responses
function submitAnswers() {
    let activePoll = pollDataList.find(p => p.id === selectedPollId);
    if (!activePoll) return;

    let hasErrors = false;
    $(".vote-item-card").css("border-color", "#3498db"); // reset to theme blue border
    $(".vote-item-label").removeClass("invalid-label-style");
    $(".vote-item-card .field-error-msg").text("");

    let errorMessages = [];

    // validate each question card
    $(".vote-item-card").each(function () {
        let card = $(this);
        let qIndex = card.data("question-index");
        let isMandatory = card.data("mandatory") === true;
        let question = activePoll.questions[qIndex];

        if (isMandatory) {
            let checkedOptions = card.find(".choice-input-item:checked");
            if (checkedOptions.length === 0) {
                hasErrors = true;
                card.css("border-color", "#e74c3c");
                card.find(".vote-item-label").addClass("invalid-label-style");
                card.find(".field-error-msg").text("This field is mandatory. Please select an option.");
                errorMessages.push(`Question ${qIndex + 1} ("${question.text}") is required.`);
            }
        }
    });

    if (hasErrors) {
        let errorSummaryHtml = `
            <div class="alert alert-danger" id="vote-validation-box">
                <h5><i class="fas fa-exclamation-triangle mr-2"></i> Error: Missing required responses</h5>
                <ul class="mb-0">
                    ${errorMessages.map(msg => `<li>${msg}</li>`).join("")}
                </ul>
            </div>
        `;
        $("#vote-validation-box").remove();
        $("#voting-form").prepend(errorSummaryHtml);

        // scroll to error
        $('html, body').animate({
            scrollTop: $("#voting-section").offset().top - 20
        }, 500);
        return;
    }

    $("#vote-validation-box").remove();
    displayMessage("<strong>Success!</strong> Your responses have been recorded. Thank you for voting!", "success");
    $(".choice-input-item").prop("checked", false);
}

// builder state counter
let itemIndex = 0;

// clear form elements
function clearCreateForm() {
    itemIndex = 0;
    $("#question-blocks-area").empty();
    $("#input-poll-title").val("").removeClass("invalid-input-style");
    $("#error-poll-title").text("");
    $("#error-alert-list").hide();
    $("#error-details").empty();

    insertQuestionSection();
}

// insert builder fields block
function insertQuestionSection() {
    itemIndex++;
    let qId = itemIndex;

    let questionBlockHtml = `
        <div class="question-card-item" id="q-block-${qId}" data-id="${qId}">
            <button type="button" class="btn btn-sm btn-remove-question delete-question-btn" onclick="deleteQuestionSection(${qId})">
                <i class="fas fa-trash-alt mr-1"></i> Remove
            </button>
            
            <h4 class="font-weight-bold text-success mb-3">Question #${qId}</h4>
            
            <!-- text question field -->
            <div class="form-group mb-3">
                <label for="q-txt-${qId}" class="font-weight-600 question-label-txt">
                    Question Text <span class="mandatory-mark">*</span>
                </label>
                <input type="text" class="form-control question-text-input" id="q-txt-${qId}" placeholder="Enter your question">
                <div class="field-error-msg" id="error-q-txt-${qId}"></div>
            </div>

            <!-- check configs -->
            <div class="form-row mb-3">
                <div class="col-sm-6 mb-2">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input chk-mandatory" id="q-mandatory-${qId}">
                        <label class="custom-control-label font-weight-500" for="q-mandatory-${qId}">Mandatory (Required to answer)</label>
                    </div>
                </div>
                <div class="col-sm-6 mb-2">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input chk-multiple" id="q-multiple-${qId}">
                        <label class="custom-control-label font-weight-500" for="q-multiple-${qId}">You can select multiple options</label>
                    </div>
                </div>
            </div>

            <!-- options list builder -->
            <div class="options-builder-area">
                <label class="font-weight-600 options-label-txt">Possible Answers <span class="mandatory-mark">*</span></label>
                <div class="field-error-msg mb-2" id="error-options-${qId}"></div>
                
                <div class="answers-container mb-3" id="options-list-${qId}"></div>
                
                <div class="input-group">
                    <input type="text" class="form-control input-new-option" id="new-option-input-${qId}" 
                           placeholder="Type your answer" 
                           onkeypress="onAnswerKey(event, ${qId})">
                    <div class="input-group-append">
                        <button type="button" class="btn add-option-btn px-3" onclick="insertAnswerInput(${qId})">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `;

    $("#question-blocks-area").append(questionBlockHtml);

    // add two answers by default
    appendAnswerInputWithValue(qId, "Yes");
    appendAnswerInputWithValue(qId, "No");
}

// remove card block
function deleteQuestionSection(qId) {
    if ($(".question-card-item").length <= 1) {
        alert("You must include at least one question in your poll.");
        return;
    }
    $(`#q-block-${qId}`).remove();
    refreshQuestionNumbers();
}

// refresh headers count numbers
function refreshQuestionNumbers() {
    let index = 1;
    $(".question-card-item").each(function () {
        $(this).find("h4").text(`Question #${index}`);
        index++;
    });
}

// enter check
function onAnswerKey(event, qId) {
    if (event.key === "Enter") {
        event.preventDefault();
        insertAnswerInput(qId);
    }
}

// insert answer list row
function insertAnswerInput(qId) {
    let inputField = $(`#new-option-input-${qId}`);
    let answerVal = inputField.val().trim();

    if (!answerVal) {
        inputField.addClass("invalid-input-style");
        setTimeout(() => inputField.removeClass("invalid-input-style"), 1000);
        return;
    }

    appendAnswerInputWithValue(qId, answerVal);
    inputField.val("");
}

// append HTML markup row
function appendAnswerInputWithValue(qId, value) {
    let listContainer = $(`#options-list-${qId}`);
    let optionId = "opt-" + qId + "-" + Date.now() + Math.floor(Math.random() * 100);

    let answerRowHtml = `
        <div class="answer-row-item mb-2" id="${optionId}">
            <input type="text" class="form-control input-option-val" value="${value}" placeholder="Answer option">
            <button type="button" class="btn btn-danger delete-option-btn btn-sm" onclick="deleteAnswerInput('${optionId}')">
                <i class="fas fa-times"></i>
            </button>
        </div>
    `;
    listContainer.append(answerRowHtml);
}

// remove answer row
function deleteAnswerInput(optionId) {
    $(`#${optionId}`).remove();
}

// save and validate forms builder
function processCreatePoll() {
    let pollName = $("#input-poll-title").val().trim();
    let isValid = true;
    let errorMsgs = [];

    // clean validation flags
    $("#input-poll-title").removeClass("invalid-input-style");
    $("#form-group-title label").removeClass("invalid-label-style");
    $("#error-poll-title").text("");

    $(".question-card-item").each(function () {
        let block = $(this);
        block.find(".question-text-input").removeClass("invalid-input-style");
        block.find(".question-label-txt").removeClass("invalid-label-style");
        block.find(".field-error-msg").text("");
        block.find(".input-option-val").removeClass("invalid-input-style");
        block.find(".options-label-txt").removeClass("invalid-label-style");
    });

    $("#error-alert-list").hide();
    $("#error-details").empty();

    // title field check
    if (!pollName) {
        $("#input-poll-title").addClass("invalid-input-style");
        $("#form-group-title label").addClass("invalid-label-style");
        $("#error-poll-title").text("Poll name is required.");
        errorMsgs.push("Poll Name: Cannot be empty.");
        isValid = false;
    } else if (pollName.length < 3 || pollName.length > 255) {
        $("#input-poll-title").addClass("invalid-input-style");
        $("#form-group-title label").addClass("invalid-label-style");
        $("#error-poll-title").text("Poll name must be between 3 and 255 characters.");
        errorMsgs.push("Poll Name: Must be between 3 and 255 characters long.");
        isValid = false;
    }

    // question cards count check
    let activeBlocks = $(".question-card-item");
    if (activeBlocks.length === 0) {
        errorMsgs.push("Form: You must build at least one question block.");
        isValid = false;
    }

    activeBlocks.each(function (index) {
        let block = $(this);
        let qId = block.data("id");
        let labelIndex = index + 1;

        let qText = block.find(".question-text-input").val().trim();

        // text check
        if (!qText) {
            block.find(".question-text-input").addClass("invalid-input-style");
            block.find(".question-label-txt").addClass("invalid-label-style");
            block.find(`#error-q-txt-${qId}`).text("Question text is required.");
            errorMsgs.push(`Question #${labelIndex}: Question text cannot be empty.`);
            isValid = false;
        } else if (qText.length < 3 || qText.length > 255) {
            block.find(".question-text-input").addClass("invalid-input-style");
            block.find(".question-label-txt").addClass("invalid-label-style");
            block.find(`#error-q-txt-${qId}`).text("Question text must be between 3 and 255 characters.");
            errorMsgs.push(`Question #${labelIndex}: Question text must be between 3 and 255 characters.`);
            isValid = false;
        }

        // answer items check
        let answerInputs = block.find(".input-option-val");
        let answersArr = [];

        if (answerInputs.length < 2) {
            block.find(".options-label-txt").addClass("invalid-label-style");
            block.find(`#error-options-${qId}`).text("At least 2 possible answers are required.");
            errorMsgs.push(`Question #${labelIndex}: Must have at least 2 answers.`);
            isValid = false;
        } else {
            answerInputs.each(function (ansIdx) {
                let ansVal = $(this).val().trim();
                if (!ansVal) {
                    $(this).addClass("invalid-input-style");
                    block.find(".options-label-txt").addClass("invalid-label-style");
                    block.find(`#error-options-${qId}`).text("Answer option fields cannot be blank.");
                    errorMsgs.push(`Question #${labelIndex} Answer ${ansIdx + 1}: Cannot be empty.`);
                    isValid = false;
                } else if (ansVal.length < 3 || ansVal.length > 200) {
                    $(this).addClass("invalid-input-style");
                    block.find(".options-label-txt").addClass("invalid-label-style");
                    block.find(`#error-options-${qId}`).text("Answer option must be between 3 and 200 characters.");
                    errorMsgs.push(`Question #${labelIndex} Answer ${ansIdx + 1}: Must be between 3 and 200 characters.`);
                    isValid = false;
                } else {
                    answersArr.push(ansVal);
                }
            });
        }
    });

    if (!isValid) {
        let summaryList = $("#error-details");
        errorMsgs.forEach(function (msg) {
            summaryList.append(`<li>${msg}</li>`);
        });
        $("#error-alert-list").show();

        // scroll form
        $('html, body').animate({
            scrollTop: $("#builder-section").offset().top - 20
        }, 500);
        return;
    }

    // build new survey entry
    let newPoll = {
        id: pollDataList.length + 1,
        name: pollName,
        status: "active",
        questions: []
    };

    activeBlocks.each(function () {
        let block = $(this);
        let answersArr = [];
        block.find(".input-option-val").each(function () {
            answersArr.push($(this).val().trim());
        });

        newPoll.questions.push({
            text: block.find(".question-text-input").val().trim(),
            mandatory: block.find(".chk-mandatory").is(":checked"),
            allowMultiple: block.find(".chk-multiple").is(":checked"),
            answers: answersArr
        });
    });

    // save to lists
    pollDataList.push(newPoll);

    displayMessage(`<strong>Success!</strong> Poll "${newPoll.name}" has been created successfully with ${newPoll.questions.length} questions.`, "success");

    clearCreateForm();
    toggleSection("list");
}

// filter action from tabs
function applyStatusFilter(filter, buttonElement) {
    activeTabFilter = filter;

    $(".filter-buttons-group .btn-filter-option").removeClass("selected-filter");
    $(buttonElement).addClass("selected-filter");

    displayPollTable(filter);
}

// load rows in list view table
function displayPollTable(filter = "active") {
    let tbody = $("#table-body-polls");
    tbody.empty();

    let filteredList = pollDataList.filter(p => p.status === filter);

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
                    <button type="button" class="btn btn-sm btn-success mr-1" onclick="loadPollToVote(${poll.id})">
                        <i class="fas fa-eye"></i> View
                    </button>
                    ${poll.status === 'active' ? `
                    <button type="button" class="btn btn-sm btn-warning mr-1" onclick="lockPollItem(${poll.id})" style="background-color: #f39c12 !important; border-color: #f39c12 !important;">
                        <i class="fas fa-lock"></i> Close
                    </button>` : ''}
                    <button type="button" class="btn btn-sm btn-danger" onclick="removePollFromServer(${poll.id})">
                        <i class="fas fa-trash-alt"></i> Delete
                    </button>
                </td>
            </tr>
        `;
        tbody.append(rowHtml);
    });
}

// switch view to vote screen
function loadPollToVote(pollId) {
    selectedPollId = pollId;
    toggleSection("vote");
}

// lock target poll status
function lockPollItem(pollId) {
    let poll = pollDataList.find(p => p.id === pollId);
    if (poll) {
        poll.status = "closed";
        displayMessage(`Poll "${poll.name}" has been successfully closed.`, "info");
        displayPollTable(activeTabFilter);
    }
}

// delete item via ajax load response
function removePollFromServer(pollId) {
    let poll = pollDataList.find(p => p.id === pollId);
    if (!poll) return;

    let confirmDelete = confirm(`Are you sure you want to delete this item?\nPoll: "${poll.name}"`);

    if (confirmDelete) {
        $.ajax({
            url: "delete_response.html",
            type: "GET",
            dataType: "html",
            success: function (responseHtml) {
                // parse inner html
                let parsedDom = $("<div>").append($.parseHTML(responseHtml));
                let bodyInnerHtml = parsedDom.find("body").html();

                if (!bodyInnerHtml) {
                    bodyInnerHtml = responseHtml;
                }

                // filter out list item
                pollDataList = pollDataList.filter(p => p.id !== pollId);

                // refresh table list display
                displayPollTable(activeTabFilter);

                // show banner message
                displayMessage(bodyInnerHtml, "success", 6000);
            },
            error: function (xhr, status, error) {
                displayMessage(`<strong>Error!</strong> Failed to communicate with server via AJAX: ${status} - ${error}`, "danger");
            }
        });
    }
}

$(document).ready(function() {

    // =========================================================================
    // 1. STATE & DEFAULT DATA
    // =========================================================================
    let polls = [
        {
            id: 98,
            name: "Great poll",
            status: "active",
            questions: [
                {
                    text: "Is it necessary to you?",
                    mandatory: true,
                    multiselect: false,
                    answers: ["Yes", "No"]
                },
                {
                    text: "Often pass polls?",
                    mandatory: false,
                    multiselect: false,
                    answers: ["Once a month", "Once a week"]
                },
                {
                    text: "How old are you?",
                    mandatory: false,
                    multiselect: false,
                    answers: ["18-20", "21-23"]
                }
            ]
        }
    ];

    let currentListTab = "active"; // "active", "drafts", "closed"
    let questionCounter = 0; // To keep IDs unique

    // =========================================================================
    // 2. VIEW SWITCHING & NAVIGATION
    // =========================================================================
    function switchView(viewId) {
        $('.view-section').addClass('d-none');
        $(`#view-${viewId}`).removeClass('d-none');
        
        $('.navbar-nav .nav-link').removeClass('active');
        $(`#link-${viewId}`).addClass('active');

        // Clear any alerts
        $('#global-alert-container').empty();
        $('#ajax-results-container').empty();

        if (viewId === 'list') {
            renderPollsTable();
        } else if (viewId === 'vote') {
            renderVotePolls();
        }
    }

    $('#link-vote').on('click', function(e) { e.preventDefault(); switchView('vote'); });
    $('#link-create').on('click', function(e) { e.preventDefault(); switchView('create'); });
    $('#link-list').on('click', function(e) { e.preventDefault(); switchView('list'); });
    $('#nav-brand').on('click', function(e) { e.preventDefault(); switchView('vote'); });

    // Show alert function
    function showGlobalAlert(message, type = "success") {
        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show" role="alert">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `;
        $('#global-alert-container').html(alertHtml);
        // Scroll to top
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    // =========================================================================
    // 3. LOGIN MODAL VALIDATION
    // =========================================================================
    $('#login-form').on('submit', function(e) {
        e.preventDefault();
        
        // Reset styles
        resetValidationErrors('#login-form');
        $('#login-error-container').empty();

        const $alias = $('#login-alias');
        const $password = $('#login-password');
        let errors = [];

        // Check required fields by looping through
        $('#login-form input[required]').each(function() {
            const val = $(this).val().trim();
            const fieldId = $(this).attr('id');
            const fieldLabel = $(this).siblings('label').text().replace('*', '').trim();

            if (!val) {
                errors.push(`${fieldLabel} is required.`);
                markFieldInvalid($(this), `${fieldLabel} must not be empty.`);
            }
        });

        if (errors.length > 0) {
            const errorHtml = `
                <div class="alert alert-danger">
                    <strong>Validation failed:</strong>
                    <ul class="pl-3 mb-0">
                        ${errors.map(err => `<li>${err}</li>`).join('')}
                    </ul>
                </div>
            `;
            $('#login-error-container').html(errorHtml);
            return;
        }

        // Simulating successful login
        showLoader(800, function() {
            $('#loginModal').modal('hide');
            showGlobalAlert(`Welcome back, ${$alias.val()}! Login completed successfully.`, 'success');
            
            // Change Login button to Logout
            $('#btn-login-trigger').text('Logout').removeClass('btn-outline-success').addClass('btn-outline-danger');
            
            // Reset fields
            $alias.val('');
            $password.val('');
            $('#login-remember').prop('checked', false);
        });
    });

    // Handle Login/Logout toggle trigger
    $('#btn-login-trigger').on('click', function(e) {
        if ($(this).text() === 'Logout') {
            e.preventDefault();
            e.stopPropagation();
            $(this).text('Login');
            showGlobalAlert('You have logged out successfully.', 'info');
        }
    });

    // =========================================================================
    // 4. CREATE PAGE - DYNAMIC FORM BUILDER
    // =========================================================================
    
    // Add Question Function
    function addQuestionBlock() {
        questionCounter++;
        const qId = questionCounter;

        const questionHtml = `
            <div class="question-block" id="question-block-${qId}" data-q-id="${qId}">
                <button type="button" class="btn-remove-question" title="Remove question">&times;</button>
                
                <!-- Question Title -->
                <div class="form-group">
                    <label for="q-text-${qId}" class="q-label">Question Text <span class="text-danger">*</span></label>
                    <input type="text" class="form-control question-text-input" id="q-text-${qId}" placeholder="Enter your question" required>
                    <div class="error-feedback"></div>
                </div>

                <!-- Checkboxes Row -->
                <div class="form-row mb-3">
                    <div class="col-sm-6">
                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input q-mandatory" id="q-mandatory-${qId}">
                            <label class="custom-control-label" for="q-mandatory-${qId}">Mandatory</label>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="custom-control custom-checkbox">
                            <input type="checkbox" class="custom-control-input q-multiselect" id="q-multiselect-${qId}">
                            <label class="custom-control-label" for="q-multiselect-${qId}">You can select multiple options</label>
                        </div>
                    </div>
                </div>

                <!-- Possible Answers -->
                <div class="form-group mb-0">
                    <label class="ans-label">Possible answers <span class="text-danger">*</span></label>
                    <div class="answers-container-${qId}">
                        <!-- Answer elements -->
                        <div class="answer-input-group">
                            <input type="text" class="form-control answer-input" placeholder="Type your answer" required>
                            <button type="button" class="btn btn-add-answer">+</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        $('#create-questions-container').append(questionHtml);
        updateRemoveAnswerButtons(qId);
    }

    // Add first default question on load
    addQuestionBlock();

    // Add Question Button Click
    $('#btn-add-question').on('click', function() {
        addQuestionBlock();
    });

    // Remove Question Block Click (Delegated)
    $('#create-questions-container').on('click', '.btn-remove-question', function() {
        const $block = $(this).closest('.question-block');
        $block.slideUp(200, function() {
            $(this).remove();
        });
    });

    // Add Answer Input (Delegated)
    $('#create-questions-container').on('click', '.btn-add-answer', function() {
        const $block = $(this).closest('.question-block');
        const qId = $block.data('q-id');
        const $container = $(`.answers-container-${qId}`);

        const answerHtml = `
            <div class="answer-input-group mt-2">
                <input type="text" class="form-control answer-input" placeholder="Type your answer" required>
                <button type="button" class="btn btn-remove-answer">-</button>
            </div>
        `;
        $container.append(answerHtml);
        updateRemoveAnswerButtons(qId);
    });

    // Remove Answer Input (Delegated)
    $('#create-questions-container').on('click', '.btn-remove-answer', function() {
        const $block = $(this).closest('.question-block');
        const qId = $block.data('q-id');
        $(this).closest('.answer-input-group').remove();
        updateRemoveAnswerButtons(qId);
    });

    // Disable/Enable minus buttons depending on answer count
    function updateRemoveAnswerButtons(qId) {
        const $container = $(`.answers-container-${qId}`);
        const count = $container.find('.answer-input').length;
        if (count <= 1) {
            $container.find('.btn-remove-answer').hide();
        } else {
            $container.find('.btn-remove-answer').show();
        }
    }

    // =========================================================================
    // 5. CREATE PAGE - FORM VALIDATION & SUBMIT
    // =========================================================================
    $('#create-interview-form').on('submit', function(e) {
        e.preventDefault();

        // Reset errors
        resetValidationErrors('#create-interview-form');
        $('#create-error-summary-container').empty();

        let errors = [];

        // 1. Validate Poll Name
        const $pollName = $('#input-poll-name');
        const pollNameVal = $pollName.val().trim();
        if (!pollNameVal) {
            errors.push({ element: $pollName, message: "Name poll is required." });
        } else if (pollNameVal.length < 3) {
            errors.push({ element: $pollName, message: "Name poll must be at least 3 characters." });
        } else if (pollNameVal.length > 255) {
            errors.push({ element: $pollName, message: "Name poll must not exceed 255 characters." });
        }

        // 2. Validate Questions and Answers
        const $questionBlocks = $('.question-block');
        if ($questionBlocks.length === 0) {
            // General error
            errors.push({ element: null, message: "At least one question is required." });
        }

        $questionBlocks.each(function(index) {
            const qId = $(this).data('q-id');
            const $qInput = $(this).find('.question-text-input');
            const qText = $qInput.val().trim();
            const qLabel = `Question ${index + 1}`;

            // Validate question text
            if (!qText) {
                errors.push({ element: $qInput, message: `${qLabel} text is required.` });
            } else if (qText.length < 3) {
                errors.push({ element: $qInput, message: `${qLabel} text must be at least 3 characters.` });
            } else if (qText.length > 255) {
                errors.push({ element: $qInput, message: `${qLabel} text must not exceed 255 characters.` });
            }

            // Validate answers
            const $answers = $(this).find('.answer-input');
            let hasAnswers = false;
            $answers.each(function(aIndex) {
                const aText = $(this).val().trim();
                const aLabel = `${qLabel} Answer ${aIndex + 1}`;

                if (!aText) {
                    errors.push({ element: $(this), message: `${aLabel} text is required.` });
                } else if (aText.length < 3) {
                    errors.push({ element: $(this), message: `${aLabel} must be at least 3 characters.` });
                } else if (aText.length > 200) {
                    errors.push({ element: $(this), message: `${aLabel} must not exceed 200 characters.` });
                } else {
                    hasAnswers = true;
                }
            });
        });

        // 3. Process errors if any
        if (errors.length > 0) {
            let errorItemsHtml = '';
            errors.forEach(err => {
                errorItemsHtml += `<li>${err.message}</li>`;
                if (err.element) {
                    markFieldInvalid(err.element, err.message);
                }
            });

            const summaryHtml = `
                <div class="error-summary-box">
                    <strong>Please resolve the following validation errors:</strong>
                    <ul class="pl-3 mt-2 mb-0">
                        ${errorItemsHtml}
                    </ul>
                </div>
            `;
            $('#create-error-summary-container').html(summaryHtml);
            
            // Scroll to the error summary
            window.scrollTo({ top: 0, behavior: 'smooth' });
            return;
        }

        // 4. Construct poll object if validation passes
        const newPoll = {
            id: polls.length > 0 ? Math.max(...polls.map(p => p.id)) + 1 : 1,
            name: pollNameVal,
            status: "active", // New polls start as active
            questions: []
        };

        $questionBlocks.each(function() {
            const qId = $(this).data('q-id');
            const qText = $(this).find('.question-text-input').val().trim();
            const isMandatory = $(this).find('.q-mandatory').is(':checked');
            const isMultiselect = $(this).find('.q-multiselect').is(':checked');
            
            const answers = [];
            $(this).find('.answer-input').each(function() {
                answers.push($(this).val().trim());
            });

            newPoll.questions.push({
                text: qText,
                mandatory: isMandatory,
                multiselect: isMultiselect,
                answers: answers
            });
        });

        polls.push(newPoll);

        // Success message and reset
        showLoader(1000, function() {
            showGlobalAlert(`Poll "${newPoll.name}" created and published successfully!`, 'success');
            resetCreateForm();
            switchView('list');
        });
    });

    function resetCreateForm() {
        $('#input-poll-name').val('');
        $('#create-questions-container').empty();
        questionCounter = 0;
        addQuestionBlock(); // Re-add default question block
        resetValidationErrors('#create-interview-form');
        $('#create-error-summary-container').empty();
    }

    // =========================================================================
    // 6. VALIDATION HELPERS
    // =========================================================================
    function markFieldInvalid($field, message) {
        $field.addClass('border-error');
        $field.siblings('label, .q-label, .ans-label').addClass('label-error');
        $field.siblings('.error-feedback').text(message).show();
    }

    function resetValidationErrors(formSelector) {
        const $form = $(formSelector);
        $form.find('.border-error').removeClass('border-error');
        $form.find('.label-error').removeClass('label-error');
        $form.find('.error-feedback').empty().hide();
    }

    // =========================================================================
    // 7. LIST VIEW - TABS & RENDERING
    // =========================================================================
    $('#tab-active').on('click', function() { changeListTab('active'); });
    $('#tab-drafts').on('click', function() { changeListTab('drafts'); });
    $('#tab-closed').on('click', function() { changeListTab('closed'); });

    function changeListTab(tabName) {
        $('.list-tabs button').removeClass('active');
        $(`#tab-${tabName}`).addClass('active');
        currentListTab = tabName;
        renderPollsTable();
        $('#ajax-results-container').empty();
    }

    function renderPollsTable() {
        const $tbody = $('#polls-table-body');
        $tbody.empty();

        const filteredPolls = polls.filter(p => p.status === currentListTab);

        if (filteredPolls.length === 0) {
            $tbody.append(`
                <tr>
                    <td colspan="3" class="text-center text-muted font-italic py-4">
                        No ${currentListTab} polls found.
                    </td>
                </tr>
            `);
            return;
        }

        filteredPolls.forEach(poll => {
            const trHtml = `
                <tr data-poll-id="${poll.id}">
                    <td class="font-weight-bold align-middle">${poll.id}</td>
                    <td class="align-middle font-weight-bold text-dark">${poll.name}</td>
                    <td class="text-right align-middle">
                        <button type="button" class="btn btn-sm btn-info btn-view-results mr-1">View results</button>
                        ${poll.status === 'active' ? `<button type="button" class="btn btn-sm btn-warning btn-close-poll mr-1">Close poll</button>` : ''}
                        <button type="button" class="btn btn-sm btn-danger btn-delete-poll">Delete</button>
                    </td>
                </tr>
            `;
            $tbody.append(trHtml);
        });
    }

    // View Results Ajax call requirement
    $('#polls-table-body').on('click', '.btn-view-results', function() {
        const pollId = $(this).closest('tr').data('poll-id');
        const poll = polls.find(p => p.id === pollId);
        
        showLoader(600, function() {
            // Ajax to load HTML file
            $.ajax({
                url: 'poll-results.html',
                type: 'GET',
                dataType: 'html',
                success: function(htmlContent) {
                    // Requirement: Sanitizes the HTML document and returns ONLY the body content
                    // Parse HTML using jQuery, extract body content
                    const parser = new DOMParser();
                    const doc = parser.parseFromString(htmlContent, 'text/html');
                    const bodyContent = doc.body.innerHTML;

                    // Display results content (body content without external styling)
                    $('#ajax-results-container').html(`
                        <div class="card mt-3">
                            <h4 class="mb-3 text-secondary font-weight-bold">Results for Poll ID ${pollId}: ${poll.name}</h4>
                            <div>${bodyContent}</div>
                        </div>
                    `);
                    
                    // Smooth scroll to container
                    document.getElementById('ajax-results-container').scrollIntoView({ behavior: 'smooth' });
                },
                error: function(xhr, status, error) {
                    showGlobalAlert(`Error loading poll results: ${error}. Using fallback rendering.`, 'danger');
                    // Fallback local results
                    renderLocalResults(poll);
                }
            });
        });
    });

    function renderLocalResults(poll) {
        let resultsHtml = `<div class="card mt-3"><h4 class="mb-3 text-secondary font-weight-bold">Results for ${poll.name}</h4>`;
        poll.questions.forEach((q, index) => {
            resultsHtml += `<p class="font-weight-bold mt-3">${index + 1}. ${q.text}</p><ul class="list-group">`;
            q.answers.forEach(ans => {
                resultsHtml += `<li class="list-group-item d-flex justify-content-between align-content-center">${ans}<span class="badge badge-success badge-pill py-2 px-3">50% (100 Votes)</span></li>`;
            });
            resultsHtml += `</ul>`;
        });
        resultsHtml += `</div>`;
        $('#ajax-results-container').html(resultsHtml);
        document.getElementById('ajax-results-container').scrollIntoView({ behavior: 'smooth' });
    }

    // Close Poll action
    $('#polls-table-body').on('click', '.btn-close-poll', function() {
        const pollId = $(this).closest('tr').data('poll-id');
        const poll = polls.find(p => p.id === pollId);
        if (poll) {
            showLoader(500, function() {
                poll.status = 'closed';
                showGlobalAlert(`Poll ID ${pollId} is now closed.`, 'info');
                renderPollsTable();
                $('#ajax-results-container').empty();
            });
        }
    });

    // Delete Poll Ajax simulation requirement
    $('#polls-table-body').on('click', '.btn-delete-poll', function() {
        const pollId = $(this).closest('tr').data('poll-id');
        const pollName = $(this).closest('tr').find('td:nth-child(2)').text();
        
        // Requirement: Click delete button shows confirm box
        const confirmDelete = confirm(`Are you sure you want to delete this item?`);
        
        if (confirmDelete) {
            // Show Ajax loading overlay
            showLoader(1000, function() {
                // Perform Ajax post request to server simulation
                $.ajax({
                    url: 'delete-mock.json',
                    type: 'GET', // Using GET to fetch static local JSON mock file successfully
                    dataType: 'json',
                    success: function(response) {
                        if (response.success) {
                            // Remove from local in-memory array
                            polls = polls.filter(p => p.id !== pollId);
                            
                            showGlobalAlert(`Item "${pollName}" was deleted successfully. Server Response: ${response.message}`, 'success');
                            renderPollsTable();
                            $('#ajax-results-container').empty();
                        } else {
                            showGlobalAlert('Server rejected delete operation.', 'danger');
                        }
                    },
                    error: function(xhr, status, error) {
                        showGlobalAlert(`Failed to connect to delete endpoint: ${error}`, 'danger');
                    }
                });
            });
        }
    });

    // =========================================================================
    // 8. VOTE PAGE - RENDER & SUBMIT
    // =========================================================================
    function renderVotePolls() {
        const $container = $('#vote-questions-container');
        $container.empty();

        const activePolls = polls.filter(p => p.status === 'active');
        if (activePolls.length === 0) {
            $('#vote-poll-title').text("No active polls");
            $container.html('<p class="text-muted font-italic text-center py-4">No active polls are currently open for voting.</p>');
            $('#vote-form button[type="submit"]').hide();
            return;
        }

        // Show the first active poll
        const activePoll = activePolls[0];
        $('#vote-poll-title').text(activePoll.name);
        $('#vote-form button[type="submit"]').show();

        activePoll.questions.forEach((q, qIndex) => {
            let optionsHtml = '';
            q.answers.forEach((ans, aIndex) => {
                const inputId = `vote-q${qIndex}-opt${aIndex}`;
                
                if (q.multiselect) {
                    optionsHtml += `
                        <div class="custom-control custom-checkbox mb-2">
                            <input type="checkbox" id="${inputId}" name="vote-q${qIndex}" value="${ans}" class="custom-control-input" ${q.mandatory ? 'data-required="true"' : ''}>
                            <label class="custom-control-label" for="${inputId}">${ans}</label>
                        </div>
                    `;
                } else {
                    optionsHtml += `
                        <div class="custom-control custom-radio mb-2">
                            <input type="radio" id="${inputId}" name="vote-q${qIndex}" value="${ans}" class="custom-control-input" ${q.mandatory ? 'required' : ''}>
                            <label class="custom-control-label" for="${inputId}">${ans}</label>
                        </div>
                    `;
                }
            });

            const questionBlockHtml = `
                <div class="mb-4 vote-question-item" data-mandatory="${q.mandatory}" data-multiselect="${q.multiselect}" data-name="vote-q${qIndex}" data-title="${q.text}">
                    <p class="font-weight-bold">${qIndex + 1}. ${q.text} ${q.mandatory ? '<span class="text-danger">*</span>' : ''}</p>
                    ${optionsHtml}
                    <div class="error-feedback" style="display: none;"></div>
                </div>
            `;
            $container.append(questionBlockHtml);
        });
    }

    // Submit Vote
    $('#vote-form').on('submit', function(e) {
        e.preventDefault();
        
        let hasErrors = false;
        $('#vote-form .vote-question-item').each(function() {
            const isMandatory = $(this).data('mandatory');
            const isMultiselect = $(this).data('multiselect');
            const inputName = $(this).data('name');
            const qTitle = $(this).data('title');
            const $errFeedback = $(this).find('.error-feedback');

            // Reset error
            $errFeedback.empty().hide();
            $(this).find('p').removeClass('label-error');

            if (isMandatory) {
                let answered = false;
                if (isMultiselect) {
                    answered = $(`input[name="${inputName}"]:checked`).length > 0;
                } else {
                    answered = $(`input[name="${inputName}"]:checked`).val() !== undefined;
                }

                if (!answered) {
                    hasErrors = true;
                    $errFeedback.text("This question is mandatory. Please select an option.").show();
                    $(this).find('p').addClass('label-error');
                }
            }
        });

        if (hasErrors) {
            return;
        }

        // Simulating successful vote submission
        showLoader(800, function() {
            showGlobalAlert('Your vote has been submitted successfully. Thank you for participating!', 'success');
            renderVotePolls(); // Refresh page state
        });
    });

    // =========================================================================
    // 9. AJAX LOADING SPINNER HELPER
    // =========================================================================
    function showLoader(durationMs, callback) {
        $('#ajax-loader').css('display', 'flex');
        setTimeout(function() {
            $('#ajax-loader').hide();
            if (callback) callback();
        }, durationMs);
    }

    // Initial view render
    switchView('vote');
});

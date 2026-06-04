$(document).ready(function() {
    // Current logged-in user state
    let currentUser = null;

    // Active deletion state
    let deleteTargetId = null;

    // Question counter for dynamic form
    let questionCount = 1;

    // --- 1. Navbar Login Flow ---
    $('#navbar-login-btn').on('click', function(e) {
        e.preventDefault();
        $('#loginModal').modal('show');
    });

    $('#navbar-login-form').on('submit', function(e) {
        e.preventDefault();
        
        const $aliasInput = $('#login-alias');
        const $passInput = $('#login-password');
        const aliasVal = $aliasInput.val().trim();
        const passVal = $passInput.val().trim();
        
        let isValid = true;
        
        // Reset validation styling
        $aliasInput.removeClass('is-invalid').siblings('label').removeClass('is-invalid-label');
        $passInput.removeClass('is-invalid').siblings('label').removeClass('is-invalid-label');
        
        if (aliasVal === '') {
            $aliasInput.addClass('is-invalid').siblings('label').addClass('is-invalid-label');
            isValid = false;
        }
        
        if (passVal === '') {
            $passInput.addClass('is-invalid').siblings('label').addClass('is-invalid-label');
            isValid = false;
        }
        
        if (isValid) {
            currentUser = aliasVal;
            // Update Navbar UI
            $('#nav-login-wrapper').html(`
                <span class="nav-link text-success font-weight-bold d-flex align-items-center">
                    <i class="fas fa-user-circle mr-1"></i> Hello, ${currentUser}
                </span>
            `);
            $('#loginModal').modal('hide');
            $aliasInput.val('');
            $passInput.val('');
        }
    });

    // --- 2. SPA Navigation / Routing ---
    $('.polls-navbar .nav-link[data-page], #brand-link').on('click', function(e) {
        e.preventDefault();
        const pageName = $(this).data('page') || 'vote';
        loadPollsPage(pageName);
    });

    // Load initial page (Vote/Home)
    loadPollsPage('vote');

    function loadPollsPage(pageName) {
        // Highlight active navbar link
        $('.polls-navbar .nav-item').removeClass('active');
        $(`.polls-navbar .nav-link[data-page="${pageName}"]`).parent('.nav-item').addClass('active');

        // Show loading spinner overlay
        $('#loading-overlay').css('display', 'flex');
        $('#main-content').empty();

        // Perform AJAX request with a 1-second simulated delay for smooth transition
        $.ajax({
            url: pageName + '.html',
            method: 'GET',
            dataType: 'html'
        }).done(function(htmlContent) {
            setTimeout(function() {
                $('#loading-overlay').hide();
                $('#main-content').html(htmlContent);
                initializeSubpage(pageName);
            }, 1000);
        }).fail(function() {
            setTimeout(function() {
                $('#loading-overlay').hide();
                $('#main-content').html(`
                    <div class="alert alert-danger" role="alert">
                        <h4 class="alert-heading"><i class="fas fa-exclamation-triangle"></i> Access Error</h4>
                        <p>Unable to retrieve ${pageName}.html page fragment. Verify files exist and dev server is running.</p>
                    </div>
                `);
            }, 1000);
        });
    }

    // --- 3. Dynamic Sub-page Script Initializations ---
    function initializeSubpage(pageName) {
        if (pageName === 'vote') {
            bindVotePageEvents();
        } else if (pageName === 'create') {
            bindCreatePageEvents();
        } else if (pageName === 'list') {
            bindListPageEvents();
        }
    }

    // --- A. VOTE PAGE LOGIC ---
    function bindVotePageEvents() {
        $('#vote-poll-form').on('submit', function(e) {
            e.preventDefault();
            
            const q1Selected = $('input[name="q1"]:checked').val();
            const $q1Block = $('#vote-q1-block');
            const $q1Feedback = $('#q1-error');
            
            // Validate mandatory Question 1
            if (!q1Selected) {
                $q1Block.find('.question-label').addClass('is-invalid-label');
                $q1Feedback.removeClass('d-none');
                
                $('html, body').animate({
                    scrollTop: $q1Block.offset().top - 100
                }, 'smooth');
            } else {
                $q1Block.find('.question-label').removeClass('is-invalid-label');
                $q1Feedback.addClass('d-none');
                
                // Successful Vote Submit
                $('#vote-alert').removeClass('d-none').hide().fadeIn(300);
                $('#vote-submit-btn').prop('disabled', true);
                
                $('html, body').animate({
                    scrollTop: 0
                }, 'smooth');
            }
        });
    }

    // --- B. CREATE INTERVIEW DYNAMIC LOGIC ---
    function bindCreatePageEvents() {
        questionCount = 1; // reset count

        // Click "+ Add answer" within question block
        $(document).off('click', '.btn-add-answer').on('click', '.btn-add-answer', function(e) {
            e.preventDefault();
            const $btn = $(this);
            const $questionItem = $btn.closest('.create-question-item');
            const qIndex = $questionItem.data('qindex');
            const $answersList = $questionItem.find('.answers-inputs-list');
            
            // Build new answer input line
            const newAnsIndex = $answersList.children().length + 1;
            const newRow = $(`
                <div class="dynamic-answer-row mt-2">
                    <input 
                        type="text" 
                        class="form-control q-answer-input" 
                        name="q_${qIndex}_ans_${newAnsIndex}" 
                        placeholder="Type your answer" 
                        required 
                        minlength="3" 
                        maxlength="200"
                    >
                    <button type="button" class="btn btn-danger btn-remove-answer"><i class="fas fa-trash-alt"></i></button>
                </div>
            `);
            $answersList.append(newRow);
        });

        // Click "Remove Answer" button
        $(document).off('click', '.btn-remove-answer').on('click', '.btn-remove-answer', function(e) {
            e.preventDefault();
            $(this).closest('.dynamic-answer-row').remove();
        });

        // Click "+ Add question" button
        $('#btn-add-question').on('click', function(e) {
            e.preventDefault();
            questionCount++;
            
            const newQuestionHtml = $(`
                <div class="question-block create-question-item" data-qindex="${questionCount}">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <span class="font-weight-bold text-dark" style="font-size: 1.1rem;">
                            Question #<span class="question-display-number">${questionCount}</span>
                        </span>
                        <button type="button" class="btn btn-sm btn-remove-question btn-remove-answer">
                            <i class="fas fa-trash-alt mr-1"></i> Remove Question
                        </button>
                    </div>

                    <!-- Question Text -->
                    <div class="form-group">
                        <label class="q-text-label" for="q-text-${questionCount}">Your question <span class="required-star">*</span></label>
                        <input 
                            type="text" 
                            class="form-control q-text-input" 
                            id="q-text-${questionCount}" 
                            name="q_text_${questionCount}" 
                            placeholder="Enter your question" 
                            required 
                            minlength="3" 
                            maxlength="255"
                        >
                        <div class="invalid-field-feedback d-none">Your question must be between 3 and 255 characters.</div>
                    </div>

                    <!-- Configuration Checkboxes -->
                    <div class="form-group form-check mb-2">
                        <input type="checkbox" class="form-check-input q-mandatory-checkbox" id="q-mandatory-${questionCount}" name="q_mandatory_${questionCount}">
                        <label class="form-check-label text-muted" for="q-mandatory-${questionCount}">Mandatory</label>
                    </div>
                    <div class="form-group form-check mb-3">
                        <input type="checkbox" class="form-check-input q-multiple-checkbox" id="q-multiple-${questionCount}" name="q_multiple_${questionCount}">
                        <label class="form-check-label text-muted" for="q-multiple-${questionCount}">You can select multiple options</label>
                    </div>

                    <!-- Possible Answers -->
                    <div class="form-group">
                        <label class="q-answers-label">Possible answers <span class="required-star">*</span></label>
                        <div class="answers-inputs-list">
                            <div class="dynamic-answer-row">
                                <input 
                                    type="text" 
                                    class="form-control q-answer-input" 
                                    name="q_${questionCount}_ans_1" 
                                    placeholder="Type your answer" 
                                    required 
                                    minlength="3" 
                                    maxlength="200"
                                >
                                <button type="button" class="btn btn-success btn-add-answer"><i class="fas fa-plus"></i></button>
                            </div>
                        </div>
                        <div class="invalid-field-feedback d-none">Please add at least one answer (minimum 3 characters each).</div>
                    </div>
                </div>
            `);
            
            $('#create-questions-container').append(newQuestionHtml);
            updateRemoveQuestionButtons();
        });

        // Click "Remove Question" button
        $(document).off('click', '.btn-remove-question').on('click', '.btn-remove-question', function(e) {
            e.preventDefault();
            $(this).closest('.create-question-item').remove();
            
            // Re-index remaining questions
            let index = 1;
            $('.create-question-item').each(function() {
                const $item = $(this);
                $item.attr('data-qindex', index);
                $item.find('.question-display-number').text(index);
                
                // Update internal form input names/IDs for validation
                $item.find('.q-text-input').attr('id', `q-text-${index}`).attr('name', `q_text_${index}`);
                $item.find('.q-text-label').attr('for', `q-text-${index}`);
                $item.find('.q-mandatory-checkbox').attr('id', `q-mandatory-${index}`).attr('name', `q_mandatory_${index}`);
                $item.find('.q-mandatory-checkbox').siblings('label').attr('for', `q-mandatory-${index}`);
                $item.find('.q-multiple-checkbox').attr('id', `q-multiple-${index}`).attr('name', `q_multiple_${index}`);
                $item.find('.q-multiple-checkbox').siblings('label').attr('for', `q-multiple-${index}`);
                
                let ansIndex = 1;
                $item.find('.q-answer-input').each(function() {
                    $(this).attr('name', `q_${index}_ans_${ansIndex}`);
                    ansIndex++;
                });
                index++;
            });
            questionCount = index - 1;
            updateRemoveQuestionButtons();
        });

        // Form Submit Validation & Top Alert Summary block
        $('#create-poll-form').on('submit', function(e) {
            e.preventDefault();
            
            // Clear previous error styles and alerts
            $('#error-summary-wrapper').empty();
            $('.form-control').removeClass('is-invalid');
            $('.invalid-field-feedback').addClass('d-none');
            $('label, .q-answers-label').removeClass('is-invalid-label');

            const errors = [];
            
            // 1. Validate Poll Name
            const $pollName = $('#poll-name');
            const pollNameVal = $pollName.val().trim();
            if (pollNameVal === '') {
                errors.push("Poll Name is required.");
                $pollName.addClass('is-invalid');
                $('#label-poll-name').addClass('is-invalid-label');
                $('#error-poll-name').removeClass('d-none');
            } else if (pollNameVal.length < 3 || pollNameVal.length > 255) {
                errors.push("Poll Name must be between 3 and 255 characters.");
                $pollName.addClass('is-invalid');
                $('#label-poll-name').addClass('is-invalid-label');
                $('#error-poll-name').text("Name poll must be between 3 and 255 characters.").removeClass('d-none');
            }

            // 2. Loop & Validate each question block
            $('.create-question-item').each(function() {
                const $item = $(this);
                const qNum = $item.find('.question-display-number').text();
                
                // Validate Question Text
                const $qInput = $item.find('.q-text-input');
                const qTextVal = $qInput.val().trim();
                const $qLabel = $item.find('.q-text-label');
                
                if (qTextVal === '') {
                    errors.push(`Question #${qNum} text is required.`);
                    $qInput.addClass('is-invalid');
                    $qLabel.addClass('is-invalid-label');
                    $qInput.siblings('.invalid-field-feedback').text("Your question is required.").removeClass('d-none');
                } else if (qTextVal.length < 3 || qTextVal.length > 255) {
                    errors.push(`Question #${qNum} text must be between 3 and 255 characters.`);
                    $qInput.addClass('is-invalid');
                    $qLabel.addClass('is-invalid-label');
                    $qInput.siblings('.invalid-field-feedback').text("Your question must be between 3 and 255 characters.").removeClass('d-none');
                }

                // Validate Question Answers
                let answerInputCount = 0;
                let validAnswerCount = 0;
                const $answersList = $item.find('.answers-inputs-list');
                const $answersLabel = $item.find('.q-answers-label');
                const $answersFeedback = $item.find('.answers-inputs-list').parent().siblings('.invalid-field-feedback');

                $answersList.find('.q-answer-input').each(function() {
                    answerInputCount++;
                    const $ansInput = $(this);
                    const ansVal = $ansInput.val().trim();
                    
                    if (ansVal === '') {
                        $ansInput.addClass('is-invalid');
                    } else if (ansVal.length < 3 || ansVal.length > 200) {
                        $ansInput.addClass('is-invalid');
                        errors.push(`Question #${qNum}: Answer input #${answerInputCount} must be between 3 and 200 characters.`);
                    } else {
                        validAnswerCount++;
                    }
                });

                if (answerInputCount === 0 || validAnswerCount < answerInputCount) {
                    $answersLabel.addClass('is-invalid-label');
                    $answersFeedback.text("All answers are required (minimum 3 characters each).").removeClass('d-none');
                    if (answerInputCount === 0) {
                        errors.push(`Question #${qNum} must have at least one answer option.`);
                    } else {
                        errors.push(`Question #${qNum} has empty or invalid answer choices.`);
                    }
                }
            });

            // Handle errors
            if (errors.length > 0) {
                let errorHtml = `
                    <div class="alert alert-danger validation-error-summary mb-4" role="alert">
                        <h5 class="font-weight-bold mb-2">
                            <i class="fas fa-exclamation-circle mr-2"></i>Please fix the following validation errors:
                        </h5>
                        <ul class="mb-0 pl-3">
                `;
                errors.forEach(function(err) {
                    errorHtml += `<li>${err}</li>`;
                });
                errorHtml += `
                        </ul>
                    </div>
                `;
                
                $('#error-summary-wrapper').html(errorHtml);
                
                // Scroll to top of form
                $('html, body').animate({
                    scrollTop: $('#error-summary-wrapper').offset().top - 100
                }, 'smooth');
            } else {
                // Success submit
                $('#create-poll-success').removeClass('d-none').hide().fadeIn(300);
                $('#create-poll-form')[0].reset();
                
                // Reset questions container to only 1 question block
                $('.create-question-item').slice(1).remove();
                $('.create-question-item .answers-inputs-list').find('.dynamic-answer-row').slice(1).remove();
                questionCount = 1;
                updateRemoveQuestionButtons();

                $('html, body').animate({
                    scrollTop: 0
                }, 'smooth');

                setTimeout(function() {
                    $('#create-poll-success').fadeOut(500);
                }, 4000);
            }
        });

        function updateRemoveQuestionButtons() {
            const totalQuestions = $('.create-question-item').length;
            if (totalQuestions > 1) {
                $('.btn-remove-question').removeClass('d-none');
            } else {
                $('.btn-remove-question').addClass('d-none');
            }
        }
    }

    // --- C. LIST PAGE LOGIC & AJAX DELETION ---
    function bindListPageEvents() {
        // Trigger deletion modal
        $('.btn-delete-poll').on('click', function(e) {
            e.preventDefault();
            deleteTargetId = $(this).data('id');
            $('#deleteConfirmModal').modal('show');
        });

        // Confirm deletion button
        $('#delete-confirm-btn').off('click').on('click', function(e) {
            e.preventDefault();
            $('#deleteConfirmModal').modal('hide');

            if (deleteTargetId) {
                // Make AJAX call to delete targer poll
                $.ajax({
                    url: 'delete_poll.json',
                    method: 'GET', // Simulated DELETE/POST
                    dataType: 'json'
                }).done(function(response) {
                    // Success callback
                    $('#list-action-alert')
                        .text(response.message || "Poll deleted successfully!")
                        .removeClass('d-none').hide().fadeIn(300);
                    
                    // Fade out table row
                    $(`#poll-${deleteTargetId}`).fadeOut(600, function() {
                        $(this).remove();
                    });

                    $('html, body').animate({
                        scrollTop: 0
                    }, 'smooth');

                    setTimeout(function() {
                        $('#list-action-alert').fadeOut(500);
                    }, 4000);
                }).fail(function() {
                    alert('Error connecting to the server. Unable to delete poll.');
                }).always(function() {
                    deleteTargetId = null;
                });
            }
        });
    }
});

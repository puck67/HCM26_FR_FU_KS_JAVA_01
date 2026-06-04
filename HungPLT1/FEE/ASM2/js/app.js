$(document).ready(function() {
    $(document).on('click', '#login-nav-btn', function(e) {
        e.preventDefault();
        $.get('login-form.html', function(data) {
            $('#login-modal-container').html(data);
            $('#login-overlay-mask').fadeIn(200);
        });
    });

    $(document).on('click', '#login-close-btn', function() {
        $('#login-overlay-mask').fadeOut(200, function() {
            $('#login-modal-container').empty();
        });
    });

    $(document).on('submit', '#login-popup-form', function(e) {
        e.preventDefault();
        
        var alias = $('#alias');
        var password = $('#password');
        var isValid = true;
        var errorMessages = [];

        $('#login-popup-form .form-control').removeClass('input-invalid');
        $('#login-popup-form label').removeClass('label-invalid');
        $('#login-popup-form .inline-error').remove();
        $('#login-error-summary').empty();

        if (alias.val().trim() === '') {
            alias.addClass('input-invalid');
            alias.closest('.form-group').find('label').addClass('label-invalid');
            alias.after('<span class="inline-error">Alias is required</span>');
            errorMessages.push("Alias field cannot be empty.");
            isValid = false;
        }

        if (password.val().trim() === '') {
            password.addClass('input-invalid');
            password.closest('.form-group').find('label').addClass('label-invalid');
            password.after('<span class="inline-error">Password is required</span>');
            errorMessages.push("Password field cannot be empty.");
            isValid = false;
        }

        if (!isValid) {
            var alertHtml = '<div class="alert alert-danger font-weight-bold" style="font-size: 0.9rem;">';
            alertHtml += errorMessages.join('<br>');
            alertHtml += '</div>';
            $('#login-error-summary').html(alertHtml);
        } else {
            var successHtml = '<div class="alert alert-success font-weight-bold text-center">Login successful!</div>';
            $('#login-error-summary').html(successHtml);
            setTimeout(function() {
                $('#login-overlay-mask').fadeOut(200, function() {
                    $('#login-modal-container').empty();
                });
            }, 1000);
        }
    });

    var questionCount = 1;

    $(document).on('click', '.btn-add-answer', function() {
        var answersContainer = $(this).closest('.answers-container');
        var answerHtml = '<div class="answer-input-wrapper mt-2">';
        answerHtml += '<input type="text" class="form-control answer-text-input" placeholder="Type your answer" required minlength="3" maxlength="200">';
        answerHtml += '<button type="button" class="btn btn-sm btn-danger btn-remove-answer ml-2">-</button>';
        answerHtml += '</div>';
        answersContainer.append(answerHtml);
    });

    $(document).on('click', '.btn-remove-answer', function() {
        $(this).closest('.answer-input-wrapper').remove();
    });

    $('#btn-add-question').on('click', function() {
        questionCount++;
        var questionHtml = '<div class="question-block" data-question-index="' + questionCount + '">';
        questionHtml += '<div class="question-block-header">';
        questionHtml += '<h5 class="mb-0 font-weight-bold text-success">Question #' + questionCount + '</h5>';
        questionHtml += '<button type="button" class="btn btn-sm btn-danger btn-remove-question">Remove</button>';
        questionHtml += '</div>';
        questionHtml += '<div class="form-group">';
        questionHtml += '<label class="font-weight-bold question-label">Enter your question <span class="required-star text-danger">*</span></label>';
        questionHtml += '<input type="text" class="form-control question-text-input" placeholder="Enter your question" required minlength="3" maxlength="255">';
        questionHtml += '</div>';
        questionHtml += '<div class="form-group form-check mb-2">';
        questionHtml += '<input type="checkbox" class="form-check-input question-mandatory" id="mandatory-' + questionCount + '">';
        questionHtml += '<label class="form-check-label text-muted" for="mandatory-' + questionCount + '">Mandatory</label>';
        questionHtml += '</div>';
        questionHtml += '<div class="form-group form-check mb-3">';
        questionHtml += '<input type="checkbox" class="form-check-input question-multiple" id="multiple-' + questionCount + '">';
        questionHtml += '<label class="form-check-label text-muted" for="multiple-' + questionCount + '">You can select multiple options</label>';
        questionHtml += '</div>';
        questionHtml += '<div class="form-group mb-0">';
        questionHtml += '<label class="font-weight-bold answer-label">Possible answers <span class="required-star text-danger">*</span></label>';
        questionHtml += '<div class="answers-container">';
        questionHtml += '<div class="answer-input-wrapper">';
        questionHtml += '<input type="text" class="form-control answer-text-input" placeholder="Type your answer" required minlength="3" maxlength="200">';
        questionHtml += '<button type="button" class="btn btn-add-answer ml-2">+</button>';
        questionHtml += '</div>';
        questionHtml += '</div>';
        questionHtml += '</div>';
        questionHtml += '</div>';
        
        $('#questions-list').append(questionHtml);
    });

    $(document).on('click', '.btn-remove-question', function() {
        $(this).closest('.question-block').remove();
        reorderQuestions();
    });

    function reorderQuestions() {
        questionCount = 0;
        $('#questions-list .question-block').each(function() {
            questionCount++;
            $(this).attr('data-question-index', questionCount);
            $(this).find('.question-block-header h5').text('Question #' + questionCount);
            $(this).find('.question-mandatory').attr('id', 'mandatory-' + questionCount);
            $(this).find('.question-mandatory').next('label').attr('for', 'mandatory-' + questionCount);
            $(this).find('.question-multiple').attr('id', 'multiple-' + questionCount);
            $(this).find('.question-multiple').next('label').attr('for', 'multiple-' + questionCount);
        });
    }

    $('#create-interview-form').on('submit', function(e) {
        e.preventDefault();

        var isValid = true;
        var errorMessages = [];

        $('#create-interview-form .form-control').removeClass('input-invalid');
        $('#create-interview-form label').removeClass('label-invalid');
        $('#create-interview-form .inline-error').remove();
        $('#validation-alert-summary').empty();

        var pollName = $('#pollName');
        if (pollName.val().trim() === '') {
            pollName.addClass('input-invalid');
            pollName.closest('.form-group').find('label').addClass('label-invalid');
            pollName.after('<span class="inline-error">Poll name is required</span>');
            errorMessages.push("Name poll field is required.");
            isValid = false;
        } else if (pollName.val().trim().length < 3) {
            pollName.addClass('input-invalid');
            pollName.closest('.form-group').find('label').addClass('label-invalid');
            pollName.after('<span class="inline-error">Poll name must be at least 3 characters</span>');
            errorMessages.push("Name poll field must be at least 3 characters.");
            isValid = false;
        }

        $('#questions-list .question-block').each(function(qIdx) {
            var currentQNum = qIdx + 1;
            var qBlock = $(this);
            var qInput = qBlock.find('.question-text-input');
            var qValue = qInput.val().trim();

            if (qValue === '') {
                qInput.addClass('input-invalid');
                qBlock.find('.question-label').addClass('label-invalid');
                qInput.after('<span class="inline-error">Question text is required</span>');
                errorMessages.push("Question #" + currentQNum + ": Text is required.");
                isValid = false;
            } else if (qValue.length < 3) {
                qInput.addClass('input-invalid');
                qBlock.find('.question-label').addClass('label-invalid');
                qInput.after('<span class="inline-error">Question text must be at least 3 characters</span>');
                errorMessages.push("Question #" + currentQNum + ": Text must be at least 3 characters.");
                isValid = false;
            }

            var answerInputs = qBlock.find('.answer-text-input');
            var answerCount = answerInputs.length;
            
            if (answerCount === 0) {
                qBlock.find('.answer-label').addClass('label-invalid');
                qBlock.find('.answers-container').after('<span class="inline-error">At least one answer is required</span>');
                errorMessages.push("Question #" + currentQNum + ": At least one answer option must be provided.");
                isValid = false;
            } else {
                answerInputs.each(function(aIdx) {
                    var currentANum = aIdx + 1;
                    var aInput = $(this);
                    var aValue = aInput.val().trim();

                    if (aValue === '') {
                        aInput.addClass('input-invalid');
                        qBlock.find('.answer-label').addClass('label-invalid');
                        aInput.after('<span class="inline-error">Answer option #' + currentANum + ' is required</span>');
                        errorMessages.push("Question #" + currentQNum + " - Answer #" + currentANum + ": Option cannot be empty.");
                        isValid = false;
                    } else if (aValue.length < 3) {
                        aInput.addClass('input-invalid');
                        qBlock.find('.answer-label').addClass('label-invalid');
                        aInput.after('<span class="inline-error">Answer option #' + currentANum + ' must be at least 3 characters</span>');
                        errorMessages.push("Question #" + currentQNum + " - Answer #" + currentANum + ": Option must be at least 3 characters.");
                        isValid = false;
                    }
                });
            }
        });

        if (!isValid) {
            var alertHtml = '<div class="alert alert-danger font-weight-bold">';
            alertHtml += '<h5 class="alert-heading">Form validation failed!</h5>';
            alertHtml += '<ul class="mb-0 pl-3">';
            for (var i = 0; i < errorMessages.length; i++) {
                alertHtml += '<li>' + errorMessages[i] + '</li>';
            }
            alertHtml += '</ul>';
            alertHtml += '</div>';
            $('#validation-alert-summary').html(alertHtml);
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        } else {
            var successHtml = '<div class="alert alert-success font-weight-bold">';
            successHtml += '<h5 class="alert-heading">Success!</h5>';
            successHtml += '<p class="mb-0">The interview poll has been created and validated successfully.</p>';
            successHtml += '</div>';
            $('#validation-alert-summary').html(successHtml);
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        }
    });

    $(document).on('click', '.btn-delete-poll', function() {
        var row = $(this).closest('tr');
        var pollId = row.attr('data-poll-id');
        var pollName = row.find('td:eq(1)').text();

        if (confirm("Are you sure you want to delete this item?")) {
            $.ajax({
                url: 'mock/delete.json',
                type: 'GET',
                dataType: 'json',
                success: function(response) {
                    if (response.status === 'success') {
                        row.fadeOut(400, function() {
                            row.remove();
                        });
                    }
                },
                error: function() {
                    alert("Error: Failed to delete the item.");
                }
            });
        }
    });
});

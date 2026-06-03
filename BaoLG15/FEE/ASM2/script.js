$(document).ready(function () {
    function switchSection(sectionId, navLinkId) {
        $('.page-section').removeClass('active-section');
        $('#' + sectionId).addClass('active-section');

        $('.navbar-nav .nav-link').removeClass('active');
        $('#' + navLinkId).addClass('active');

        $('#globalAlertContainer').empty();
    }

    $('#linkVote, #navBrand').click(function (e) {
        e.preventDefault();
        switchSection('voteSection', 'linkVote');
    });

    $('#linkCreate').click(function (e) {
        e.preventDefault();
        switchSection('createSection', 'linkCreate');
    });

    $('#linkList').click(function (e) {
        e.preventDefault();
        switchSection('listSection', 'linkList');
    });

    let questionCounter = 0;

    function getAnswerRowHTML(placeholder, hasAddButton = false) {
        if (hasAddButton) {
            return `
                <div class="input-group mb-2 answer-row">
                    <input type="text" class="form-control answer-input" placeholder="${placeholder}" required>
                    <div class="input-group-append">
                        <button class="btn btn-success btn-add-answer" type="button"><i class="fas fa-plus"></i></button>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="input-group mb-2 answer-row">
                    <input type="text" class="form-control answer-input" placeholder="${placeholder}" required>
                    <div class="input-group-append">
                        <button class="btn btn-danger btn-remove-answer" type="button"><i class="fas fa-times"></i></button>
                    </div>
                </div>
            `;
        }
    }

    function getQuestionBlockHTML(index) {
        return `
            <div class="question-block" id="questionBlock_${index}">
                <button type="button" class="btn btn-danger btn-remove-question" title="Remove this question">
                    <i class="fas fa-trash-alt"></i>
                </button>
                
                <div class="form-group">
                    <label class="question-label font-weight-bold" for="qInput_${index}">Your question</label>
                    <input type="text" class="form-control question-input" id="qInput_${index}" placeholder="Enter your question" required>
                    <div class="invalid-feedback"></div>
                </div>

                <div class="form-group form-check mb-2">
                    <input type="checkbox" class="form-check-input question-mandatory" id="qMandatory_${index}">
                    <label class="form-check-label" for="qMandatory_${index}">Mandatory</label>
                </div>

                <div class="form-group form-check mb-3">
                    <input type="checkbox" class="form-check-input question-multiple" id="qMultiple_${index}">
                    <label class="form-check-label" for="qMultiple_${index}">You can select multiple options</label>
                </div>

                <div class="answers-section">
                    <label class="answers-title-label font-weight-bold">Possible answers</label>
                    <div class="answers-list-container">
                        ${getAnswerRowHTML("Type your answer", false)}
                        ${getAnswerRowHTML("Type your answer", false)}
                        ${getAnswerRowHTML("Type your answer", true)}
                    </div>
                </div>
            </div>
        `;
    }

    function initCreateForm() {
        $('#questionsContainer').empty();
        questionCounter = 1;
        $('#questionsContainer').append(getQuestionBlockHTML(questionCounter));
        toggleRemoveQuestionButtons();
    }

    function toggleRemoveQuestionButtons() {
        const blocks = $('.question-block');
        if (blocks.length <= 1) {
            $('.btn-remove-question').hide();
        } else {
            $('.btn-remove-question').show();
        }
    }

    $('#btnAddQuestion').click(function () {
        questionCounter++;
        $('#questionsContainer').append(getQuestionBlockHTML(questionCounter));
        toggleRemoveQuestionButtons();
        $([document.documentElement, document.body]).animate({
            scrollTop: $(`#questionBlock_${questionCounter}`).offset().top - 100
        }, 500);
    });

    $(document).on('click', '.btn-remove-question', function () {
        $(this).closest('.question-block').remove();
        toggleRemoveQuestionButtons();
        toggleRemoveQuestionButtons();
    });

    $(document).on('click', '.btn-add-answer', function () {
        const currentContainer = $(this).closest('.answers-list-container');
        const currentInput = $(this).closest('.answer-row').find('.answer-input');
        const currentVal = currentInput.val().trim();

        if (currentVal === "") {
            currentInput.addClass('is-invalid');
            currentInput.siblings('.invalid-feedback').remove();
            currentInput.parent().after('<div class="invalid-feedback d-block">Please fill in this answer before adding a new one.</div>');
            return;
        } else if (currentVal.length < 3 || currentVal.length > 200) {
            currentInput.addClass('is-invalid');
            currentInput.siblings('.invalid-feedback').remove();
            currentInput.parent().after('<div class="invalid-feedback d-block">Answer must be between 3 and 200 characters.</div>');
            return;
        }

        currentInput.removeClass('is-invalid');
        currentContainer.find('.invalid-feedback').remove();

        $(this).removeClass('btn-success btn-add-answer')
            .addClass('btn-danger btn-remove-answer')
            .html('<i class="fas fa-times"></i>');

        currentContainer.append(getAnswerRowHTML("Type your answer", true));
    });

    $(document).on('click', '.btn-remove-answer', function () {
        $(this).closest('.answer-row').remove();
    });


    function resetValidation() {
        $('#createErrorAlert').addClass('d-none');
        $('#createErrorMessage').empty();

        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-label').removeClass('invalid-label');
        $('.invalid-feedback').remove();
    }

    function highlightField(inputElement, errorMessage) {
        inputElement.addClass('is-invalid');

        const formGroup = inputElement.closest('.form-group, .input-group');
        let label = formGroup.find('label');
        if (label.length === 0) {
            label = inputElement.closest('.question-block').find('.question-label');
        }
        label.addClass('invalid-label');

        if (inputElement.parent().hasClass('input-group')) {
            inputElement.parent().after(`<div class="invalid-feedback d-block">${errorMessage}</div>`);
        } else {
            inputElement.after(`<div class="invalid-feedback d-block">${errorMessage}</div>`);
        }
    }

    $('#createInterviewForm').submit(function (e) {
        e.preventDefault();
        resetValidation();

        let errors = [];
        let isValid = true;

        const pollNameInput = $('#pollNameInput');
        const pollNameVal = pollNameInput.val().trim();
        if (pollNameVal === "") {
            errors.push("Name poll is mandatory and must not be empty.");
            highlightField(pollNameInput, "Name poll is required.");
            isValid = false;
        } else if (pollNameVal.length < 3 || pollNameVal.length > 255) {
            errors.push("Name poll must be between 3 and 255 characters.");
            highlightField(pollNameInput, "Must be between 3 and 255 characters.");
            isValid = false;
        }

        const questionBlocks = $('.question-block');
        if (questionBlocks.length === 0) {
            errors.push("You must create at least one question.");
            isValid = false;
        }

        questionBlocks.each(function (qIdx) {
            const blockNum = qIdx + 1;
            const qInput = $(this).find('.question-input');
            const qVal = qInput.val().trim();

            // Validate Question Text
            if (qVal === "") {
                errors.push(`Question #${blockNum} text is mandatory.`);
                highlightField(qInput, "Question text is required.");
                isValid = false;
            } else if (qVal.length < 3 || qVal.length > 255) {
                errors.push(`Question #${blockNum} text must be between 3 and 255 characters.`);
                highlightField(qInput, "Must be between 3 and 255 characters.");
                isValid = false;
            }

            const answerInputs = $(this).find('.answer-input');
            let filledAnswersCount = 0;

            answerInputs.each(function (aIdx) {
                const aVal = $(this).val().trim();
                if (aVal === "") {
                    errors.push(`Answer #${aIdx + 1} for Question #${blockNum} is mandatory.`);
                    highlightField($(this), "Answer is required.");
                    isValid = false;
                } else if (aVal.length < 3 || aVal.length > 200) {
                    errors.push(`Answer #${aIdx + 1} for Question #${blockNum} must be between 3 and 200 characters.`);
                    highlightField($(this), "Must be between 3 and 200 characters.");
                    isValid = false;
                } else {
                    filledAnswersCount++;
                }
            });

            if (filledAnswersCount < 2) {
                errors.push(`Question #${blockNum} must have at least 2 valid answers.`);
                isValid = false;
            }
        });

        if (!isValid) {
            $('#createErrorAlert').removeClass('d-none');

            let errorListHTML = '<ul class="mb-0 pl-3">';
            errors.forEach(function (err) {
                errorListHTML += `<li>${err}</li>`;
            });
            errorListHTML += '</ul>';

            $('#createErrorMessage').html(errorListHTML);

            $([document.documentElement, document.body]).animate({
                scrollTop: $('#createErrorAlert').offset().top - 100
            }, 400);
        } else {
            const newPollId = Math.floor(Math.random() * 900) + 100;

            const successMsg = `
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Success!</strong> Poll "${pollNameVal}" was created successfully.
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            `;
            $('#globalAlertContainer').html(successMsg);

            const newRow = `
                <tr data-id="${newPollId}">
                    <td>${newPollId}</td>
                    <td class="poll-title-cell">${pollNameVal}</td>
                    <td>
                        <div class="btn-group btn-group-sm" role="group">
                            <button type="button" class="btn btn-success mr-1 btn-view-results">View results</button>
                            <button type="button" class="btn btn-success mr-1 btn-close-poll">Close poll</button>
                            <button type="button" class="btn btn-success btn-delete-poll">Delete</button>
                        </div>
                    </td>
                </tr>
            `;
            $('#pollsTable tbody').append(newRow);

            $('#createInterviewForm')[0].reset();
            initCreateForm();

            switchSection('listSection', 'linkList');
            window.scrollTo({ top: 0, behavior: 'smooth' });
        }
    });

    $('#voteForm').submit(function (e) {
        e.preventDefault();

        const voteSuccess = `
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <strong>Vote Retained!</strong> Thank you for participating in the poll.
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `;
        $('#globalAlertContainer').html(voteSuccess);

        window.scrollTo({ top: 0, behavior: 'smooth' });
    });

    $(document).on('click', '.btn-delete-poll', function () {
        const row = $(this).closest('tr');
        const pollId = row.data('id');
        const pollTitle = row.find('.poll-title-cell').text();

        const confirmDelete = confirm("Are you sure you want to delete this item?");
        if (confirmDelete) {
            const deleteBtn = $(this);
            const originalText = deleteBtn.text();
            deleteBtn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin"></i>');

            $.ajax({
                url: 'https://jsonplaceholder.typicode.com/posts/1',
                type: 'DELETE',
                success: function (result) {
                    row.fadeOut(400, function () {
                        $(this).remove();
                    });

                    const deleteMsg = `
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <strong>Item Deleted!</strong> Poll #${pollId} ("${pollTitle}") was deleted successfully.
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    `;
                    $('#globalAlertContainer').html(deleteMsg);
                },
                error: function (xhr, status, error) {
                    deleteBtn.prop('disabled', false).text(originalText);
                    const errorMsg = `
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Error!</strong> Failed to delete item : ${error}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    `;
                    $('#globalAlertContainer').html(errorMsg);
                }
            });
        }
    });

    $(document).on('click', '.btn-close-poll', function () {
        alert("This poll has been closed.");
    });

    $(document).on('click', '.btn-view-results', function () {
        alert("Showing results for this poll (Feature coming soon).");
    });

    $('.list-tab').click(function () {
        $('.list-tab').removeClass('active');
        $(this).addClass('active');

        const status = $(this).data('status');

        if (status === 'Active') {
            $('#pollsTable tbody tr').show();
        } else {
            $('#pollsTable tbody tr').hide();
            if ($('#noDataRow').length === 0) {
                $('#pollsTable tbody').append(`
                    <tr id="noDataRow">
                        <td colspan="3" class="text-center text-muted py-4">No ${status.toLowerCase()} polls available.</td>
                    </tr>
                `);
            } else {
                $('#noDataRow td').text(`No ${status.toLowerCase()} polls available.`);
                $('#noDataRow').show();
            }
            return;
        }
        $('#noDataRow').remove();
    });

    $('#navLoginBtn').click(function () {
        $('#loginModal').modal('show');

        $('#loginModalBody').html(`
            <div class="text-center p-4">
                <div class="spinner-border text-success" role="status">
                    <span class="sr-only">Loading...</span>
                </div>
                <p class="mt-2 text-muted">Fetching login interface...</p>
            </div>
        `);

        $.get('login.html', function (htmlData) {
            const parser = new DOMParser();
            const doc = parser.parseFromString(htmlData, 'text/html');
            const sanitizedBody = doc.body.innerHTML;

            $('#loginModalBody').html(sanitizedBody);

            $('#loginForm').submit(function (event) {
                event.preventDefault();

                $('.login-container .is-invalid').removeClass('is-invalid');
                $('.login-container .invalid-feedback').remove();
                $('.login-container label').removeClass('invalid-label');

                const aliasInput = $('#aliasInput');
                const passwordInput = $('#passwordInput');
                let formValid = true;

                if (aliasInput.val().trim() === "") {
                    highlightField(aliasInput, "Alias is required.");
                    formValid = false;
                }
                if (passwordInput.val().trim() === "") {
                    highlightField(passwordInput, "Password is required.");
                    formValid = false;
                }

                if (formValid) {
                    $('#loginModal').modal('hide');

                    const loginSuccess = `
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <strong>Welcome back!</strong> Signed in successfully as "${aliasInput.val()}".
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    `;
                    $('#globalAlertContainer').html(loginSuccess);
                }
            });

        }).fail(function (xhr, status, error) {
            $('#loginModalBody').html(`
                <div class="alert alert-danger m-3 text-center">
                    <strong>Error!</strong> Failed to load login module from server.<br>
                    <small class="text-muted">${error}</small>
                </div>
                <div class="text-center pb-3">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                </div>
            `);
        });
    });


    initCreateForm();
});

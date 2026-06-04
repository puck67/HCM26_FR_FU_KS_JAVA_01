/**
 * script.js - All jQuery/JavaScript logic for Poll Project
 * Uses jQuery for DOM manipulation, validation, AJAX, and event binding.
 */

$(document).ready(function () {

    /* =========================================================
       1. LOGIN MODAL - Triggered by Navbar "Login" button
       ========================================================= */
    $('#btnLogin').on('click', function () {
        $('#loginModal').modal('show');
    });

    $('#loginModal .btn-close-modal').on('click', function () {
        $('#loginModal').modal('hide');
    });


    /* =========================================================
       2. CREATE PAGE LOGIC
       ========================================================= */

    /* --- 2a. Add new ANSWER row --- */
    // Use event delegation for dynamically added "+" buttons
    $(document).on('click', '.btn-add-answer', function () {
        var $answerBlock = $(this).closest('.answers-block');
        var $lastRow = $answerBlock.find('.answer-row').last();

        // Remove the "+" button from the previous last row
        $lastRow.find('.btn-add-answer').remove();

        // Create a new answer row without "+" button
        var $newRow = $('<div class="answer-row">' +
            '<input type="text" class="form-control answer-input" placeholder="Type your answer">' +
            '</div>');

        // Create a final row with "+" button
        var $newRowWithBtn = $('<div class="answer-row">' +
            '<input type="text" class="form-control answer-input" placeholder="Type your answer">' +
            '<button type="button" class="btn btn-add-answer">+</button>' +
            '</div>');

        $answerBlock.append($newRow);
        // Replace last appended with the one having "+"
        $answerBlock.find('.answer-row').last().replaceWith($newRowWithBtn);
    });

    /* --- 2b. Add new QUESTION block --- */
    $('#btnAddQuestion').on('click', function () {
        var $firstBlock = $('.question-block').first();
        var $cloned = $firstBlock.clone(true, true);

        // Clear all input values in clone
        $cloned.find('input[type="text"]').val('');
        $cloned.find('input[type="checkbox"]').prop('checked', false);

        // Keep only first answer row in cloned block
        var $answersBlock = $cloned.find('.answers-block');
        $answersBlock.find('.answer-row').not(':last').remove();
        $answersBlock.find('.answer-row:last input').val('');

        // Remove error styles from clone
        $cloned.find('input').removeClass('is-invalid-field');
        $cloned.find('.invalid-feedback-msg').remove();
        $cloned.find('label').removeClass('label-invalid');

        // Insert before the "Add question" button
        $(this).closest('.add-question-wrapper').before($cloned);
    });


    /* =========================================================
       3. FORM VALIDATION for Create Page
       ========================================================= */
    $('#btnRetainCreate').on('click', function (e) {
        e.preventDefault();
        var isValid = true;
        var errors = [];

        // Clear previous errors
        $('.is-invalid-field').removeClass('is-invalid-field');
        $('.invalid-feedback-msg').remove();
        $('.label-invalid').removeClass('label-invalid');
        $('#form-error-alert').hide().html('');

        // --- Validate Name Poll ---
        var $namePoll = $('#namePoll');
        var namePollVal = $namePoll.val().trim();
        if (namePollVal === '') {
            isValid = false;
            errors.push('Name poll is required.');
            markInvalid($namePoll, 'Name poll is required.');
        } else if (namePollVal.length < 3) {
            isValid = false;
            errors.push('Name poll must be at least 3 characters.');
            markInvalid($namePoll, 'Name poll must be at least 3 characters.');
        } else if (namePollVal.length > 255) {
            isValid = false;
            errors.push('Name poll must be at most 255 characters.');
            markInvalid($namePoll, 'Name poll must be at most 255 characters.');
        }

        // --- Validate each Question block ---
        $('.question-block').each(function (qIndex) {
            var $block = $(this);

            // Validate question input
            var $questionInput = $block.find('.question-input');
            var questionVal = $questionInput.val().trim();
            var questionLabel = $block.find('.question-label');

            if (questionVal === '') {
                isValid = false;
                var msg = 'Question ' + (qIndex + 1) + ' is required.';
                errors.push(msg);
                questionLabel.addClass('label-invalid');
                markInvalid($questionInput, msg);
            } else if (questionVal.length < 3) {
                isValid = false;
                var msg = 'Question ' + (qIndex + 1) + ' must be at least 3 characters.';
                errors.push(msg);
                questionLabel.addClass('label-invalid');
                markInvalid($questionInput, msg);
            } else if (questionVal.length > 255) {
                isValid = false;
                var msg = 'Question ' + (qIndex + 1) + ' must be at most 255 characters.';
                errors.push(msg);
                questionLabel.addClass('label-invalid');
                markInvalid($questionInput, msg);
            }

            // Validate each answer input
            $block.find('.answer-input').each(function (aIndex) {
                var $answerInput = $(this);
                var answerVal = $answerInput.val().trim();
                var answerLabel = $block.find('.answers-label');

                if (answerVal === '') {
                    isValid = false;
                    var msg = 'Answer ' + (aIndex + 1) + ' in question ' + (qIndex + 1) + ' is required.';
                    errors.push(msg);
                    answerLabel.addClass('label-invalid');
                    markInvalid($answerInput, msg);
                } else if (answerVal.length < 3) {
                    isValid = false;
                    var msg = 'Answer ' + (aIndex + 1) + ' in question ' + (qIndex + 1) + ' must be at least 3 characters.';
                    errors.push(msg);
                    answerLabel.addClass('label-invalid');
                    markInvalid($answerInput, msg);
                } else if (answerVal.length > 200) {
                    isValid = false;
                    var msg = 'Answer ' + (aIndex + 1) + ' in question ' + (qIndex + 1) + ' must be at most 200 characters.';
                    errors.push(msg);
                    answerLabel.addClass('label-invalid');
                    markInvalid($answerInput, msg);
                }
            });
        });

        // Show error summary at top
        if (!isValid) {
            var html = '<strong>Please fix the following errors:</strong><ul>';
            $.each(errors, function (i, err) {
                html += '<li>' + err + '</li>';
            });
            html += '</ul>';
            $('#form-error-alert').html(html).show();
            $('html, body').animate({ scrollTop: 0 }, 300);
        } else {
            // Success
            $('#form-error-alert')
                .removeClass('alert-danger')
                .addClass('alert alert-success')
                .html('<strong>Form submitted successfully!</strong>')
                .show();
            $('html, body').animate({ scrollTop: 0 }, 300);
        }
    });

    /**
     * Helper: Mark a field as invalid and append error message below it.
     */
    function markInvalid($input, message) {
        $input.addClass('is-invalid-field');
        $input.after('<span class="invalid-feedback-msg">' + message + '</span>');
    }


    /* =========================================================
       4. LIST PAGE - Delete with AJAX confirm
       ========================================================= */
    $(document).on('click', '.btn-delete', function () {
        var $row = $(this).closest('tr');
        var pollId = $row.data('id');

        var confirmed = confirm('Are you sure you want to delete this item?');
        if (confirmed) {
            // Simulated AJAX DELETE request
            $.ajax({
                url: '#',  // Replace with real endpoint, e.g. '/api/polls/' + pollId
                method: 'DELETE',
                success: function () {
                    $row.fadeOut(300, function () {
                        $(this).remove();
                    });
                },
                error: function () {
                    // Silently remove row for demo purposes
                    $row.fadeOut(300, function () {
                        $(this).remove();
                    });
                }
            });
        }
    });


    /* =========================================================
       5. AJAX - Load HTML content (body only, sanitized)
       ========================================================= */
    /**
     * Loads an HTML file via AJAX and extracts only the <body> content.
     * Strips <script> and <link>/<style> tags to prevent style/script injection.
     * @param {string} url - URL of the HTML file to load
     * @param {string} targetSelector - CSS selector of the container to inject content into
     */
    function loadHtmlContent(url, targetSelector) {
        $.ajax({
            url: url,
            method: 'GET',
            success: function (data) {
                // Parse the HTML string
                var parser = new DOMParser();
                var doc = parser.parseFromString(data, 'text/html');

                // Remove external scripts and styles
                $(doc).find('script, link[rel="stylesheet"], style').remove();

                // Get body innerHTML
                var bodyContent = $(doc).find('body').html();

                // Inject into target
                $(targetSelector).html(bodyContent);
            },
            error: function () {
                $(targetSelector).html('<p class="text-danger">Failed to load content.</p>');
            }
        });
    }

    // Demo: Load dummy-data into #ajax-content-area if the element exists on the page
    if ($('#ajax-content-area').length) {
        loadHtmlContent('dummy-data.html', '#ajax-content-area');
    }


    /* =========================================================
       6. LIST PAGE - Tab switching (Active / Drafts / Closed)
       ========================================================= */
    $(document).on('click', '.tab-btn', function () {
        $('.tab-btn').removeClass('btn-tab-active btn-tab-draft btn-tab-closed');

        var tab = $(this).data('tab');
        $('.tab-btn').each(function () {
            var t = $(this).data('tab');
            if (t === 'active') $(this).addClass('btn-tab-active');
            else if (t === 'drafts') $(this).addClass('btn-tab-draft');
            else if (t === 'closed') $(this).addClass('btn-tab-closed');
        });

        // Show/hide rows based on tab (using data-status attribute on <tr>)
        if (tab === 'active') {
            $('tr[data-status]').hide();
            $('tr[data-status="active"]').show();
        } else if (tab === 'drafts') {
            $('tr[data-status]').hide();
            $('tr[data-status="draft"]').show();
        } else if (tab === 'closed') {
            $('tr[data-status]').hide();
            $('tr[data-status="closed"]').show();
        }
    });

});

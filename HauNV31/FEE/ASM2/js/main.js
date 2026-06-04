$(document).ready(function() {

    // --- 1. Login Modal ---
    $('#loginBtn').on('click', function(e) {
        e.preventDefault();
        $('#loginModal').modal('show');
    });

    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        let isValid = true;
        
        // Reset validation
        $(this).find('.form-control').removeClass('is-invalid');
        $(this).find('label').removeClass('text-danger');
        $('#loginError').addClass('d-none');

        // Check username
        const username = $('#username').val().trim();
        if (username === '') {
            $('#username').addClass('is-invalid');
            $('label[for="username"]').addClass('text-danger');
            isValid = false;
        }

        // Check password
        const password = $('#password').val().trim();
        if (password === '') {
            $('#password').addClass('is-invalid');
            $('label[for="password"]').addClass('text-danger');
            isValid = false;
        }

        if (!isValid) {
            $('#loginError').removeClass('d-none');
        } else {
            // Success login
            alert('Login successful!');
            $('#loginModal').modal('hide');
        }
    });

    // --- 2. Create Interview/Poll Page ---
    
    let questionCount = 1;

    // Add new question
    $('#addQuestionBtn').on('click', function() {
        questionCount++;
        const newQuestionHtml = `
            <div class="question-block border-bottom pb-3 mb-3">
                <div class="form-group">
                    <label>Question ${questionCount} <span class="text-danger">*</span></label>
                    <input type="text" class="form-control question-input" placeholder="Enter question">
                    <div class="invalid-feedback">Question text is required.</div>
                </div>
                <div class="answers-container pl-4">
                    <div class="form-group row answer-row">
                        <label class="col-sm-2 col-form-label">Answer 1 <span class="text-danger">*</span></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control answer-input" placeholder="Enter answer">
                            <div class="invalid-feedback">Answer is required.</div>
                        </div>
                    </div>
                    <div class="form-group row answer-row">
                        <label class="col-sm-2 col-form-label">Answer 2 <span class="text-danger">*</span></label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control answer-input" placeholder="Enter answer">
                            <div class="invalid-feedback">Answer is required.</div>
                        </div>
                    </div>
                </div>
                <button type="button" class="btn btn-sm btn-outline-success add-answer-btn ml-4">+ Add Answer</button>
            </div>
        `;
        $('#questionsContainer').append(newQuestionHtml);
    });

    // Add new answer to a specific question (using event delegation)
    $('#questionsContainer').on('click', '.add-answer-btn', function() {
        const answersContainer = $(this).siblings('.answers-container');
        const answerCount = answersContainer.find('.answer-row').length + 1;
        
        const newAnswerHtml = `
            <div class="form-group row answer-row">
                <label class="col-sm-2 col-form-label">Answer ${answerCount} <span class="text-danger">*</span></label>
                <div class="col-sm-10">
                    <input type="text" class="form-control answer-input" placeholder="Enter answer">
                    <div class="invalid-feedback">Answer is required.</div>
                </div>
            </div>
        `;
        answersContainer.append(newAnswerHtml);
    });

    // Validate Create Form
    $('#createForm').on('submit', function(e) {
        e.preventDefault();
        let isValid = true;

        // Reset validation
        $(this).find('.form-control').removeClass('is-invalid');
        $(this).find('label').removeClass('text-danger');
        $('#createError, #createSuccess').addClass('d-none');

        // Check Poll Title
        if ($('#pollTitle').val().trim() === '') {
            $('#pollTitle').addClass('is-invalid');
            $('label[for="pollTitle"]').addClass('text-danger');
            isValid = false;
        }

        // Check Questions
        $('.question-input').each(function() {
            if ($(this).val().trim() === '') {
                $(this).addClass('is-invalid');
                $(this).siblings('label').addClass('text-danger');
                isValid = false;
            }
        });

        // Check Answers
        $('.answer-input').each(function() {
            if ($(this).val().trim() === '') {
                $(this).addClass('is-invalid');
                $(this).closest('.answer-row').find('label').addClass('text-danger');
                isValid = false;
            }
        });

        if (!isValid) {
            $('#createError').removeClass('d-none');
            // Scroll to top to show error
            window.scrollTo(0, 0);
        } else {
            $('#createSuccess').removeClass('d-none');
            window.scrollTo(0, 0);
            // Simulate saving data...
            setTimeout(() => {
                window.location.href = 'manage.html';
            }, 1500);
        }
    });

    // --- 3. Manage Polls Page ---

    // Load data via AJAX
    if (window.location.pathname.endsWith('manage.html') || window.location.pathname.includes('manage')) {
        loadPolls();
    }

    function loadPolls() {
        $.ajax({
            url: 'data/polls.json',
            type: 'GET',
            dataType: 'json',
            success: function(data) {
                let tbodyHtml = '';
                data.forEach(poll => {
                    tbodyHtml += `
                        <tr data-id="${poll.id}">
                            <td>${poll.id}</td>
                            <td>${poll.title}</td>
                            <td>${poll.date}</td>
                            <td>
                                <button class="btn btn-sm btn-primary edit-btn">Edit</button>
                                <button class="btn btn-sm btn-danger delete-btn">Delete</button>
                            </td>
                        </tr>
                    `;
                });
                $('#pollsTableBody').html(tbodyHtml);
            },
            error: function(err) {
                console.error("Failed to load data via Ajax", err);
                $('#pollsTableBody').html('<tr><td colspan="4" class="text-center text-danger">Failed to load data. Make sure you run this via a server to allow AJAX requests to local files.</td></tr>');
            }
        });
    }

    let deleteId = null;
    let deleteRow = null;

    // Show delete confirmation
    $('#pollsTableBody').on('click', '.delete-btn', function() {
        deleteRow = $(this).closest('tr');
        deleteId = deleteRow.data('id');
        $('#deleteModal').modal('show');
    });

    // Confirm Delete
    $('#confirmDeleteBtn').on('click', function() {
        if (deleteId) {
            // Simulate Ajax DELETE request to server
            // In real app: $.ajax({ url: '/api/polls/'+deleteId, type: 'DELETE', ... })
            
            // For this assignment demo:
            console.log("Sending delete request via Ajax for ID:", deleteId);
            deleteRow.remove();
            $('#deleteModal').modal('hide');
        }
    });

});

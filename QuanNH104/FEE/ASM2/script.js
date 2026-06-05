$(document).ready(function() {

    $(document).on('click', '#loginBtn', function(e) {
        e.preventDefault();
        if ($('#loginPopup').length === 0) {
            $.ajax({
                url: 'login.html',
                type: 'GET',
                dataType: 'html',
                success: function(data) {
                    $('#loginPopupContainer').html(data);
                    $('#loginPopup').css('display', 'flex');
                },
                error: function() {
                    alert('Error');
                }
            });
        } else {
            $('#loginPopup').css('display', 'flex');
        }
    });

    $(document).on('click', '#closeLoginBtn', function() {
        $('#loginPopup').css('display', 'none');
    });

    let questionCount = 1;

    $(document).on('click', '.add-answer-btn', function() {
        let answerRow = $(this).closest('.answer-row');
        let newAnswerRow = answerRow.clone();
        newAnswerRow.find('input').val('').removeClass('is-invalid');
        answerRow.after(newAnswerRow);
    });

    $('#addQuestionBtn').click(function() {
        questionCount++;
        let questionBlock = `
            <div class="question-block" data-question-index="${questionCount}">
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label"></label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control question-input" placeholder="Enter your question">
                        <div class="invalid-feedback">Question is required and must be between 3 and 255 characters.</div>
                    </div>
                </div>
                
                <div class="form-group row">
                    <label class="col-sm-3 col-form-label"></label>
                    <div class="col-sm-6">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" id="mandatory${questionCount}">
                            <label class="form-check-label text-muted" for="mandatory${questionCount}">Mandatory</label>
                        </div>
                        <div class="form-check mt-2">
                            <input class="form-check-input" type="checkbox" id="multiple${questionCount}">
                            <label class="form-check-label text-muted" for="multiple${questionCount}">You can select multiple options</label>
                        </div>
                    </div>
                </div>

                <div class="form-group row answer-group">
                    <label class="col-sm-3 col-form-label text-right">Possible answers</label>
                    <div class="col-sm-6 answers-container">
                        <div class="answer-row d-flex">
                            <input type="text" class="form-control answer-input flex-grow-1" placeholder="Type your answer">
                            <button type="button" class="btn btn-sm ml-2 add-answer-btn">+</button>
                            <div class="invalid-feedback w-100">Answer is required and must be between 3 and 200 characters.</div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        $('#questionsContainer').append(questionBlock);
    });

    $('#createPollForm').submit(function(e) {
        e.preventDefault();
        let isValid = true;
        let hasErrors = false;

        $('.is-invalid').removeClass('is-invalid');
        $('#errorSummary').hide();

        let pollName = $('#pollName').val().trim();
        if (pollName.length < 3 || pollName.length > 255) {
            $('#pollName').addClass('is-invalid');
            isValid = false;
            hasErrors = true;
        }

        $('.question-input').each(function() {
            let qText = $(this).val().trim();
            if (qText.length < 3 || qText.length > 255) {
                $(this).addClass('is-invalid');
                isValid = false;
                hasErrors = true;
            }
        });

        $('.answer-input').each(function() {
            let aText = $(this).val().trim();
            if (aText.length < 3 || aText.length > 200) {
                $(this).addClass('is-invalid');
                isValid = false;
                hasErrors = true;
            }
        });

        if (hasErrors) {
            $('#errorSummary').show();
            $('html, body').animate({ scrollTop: 0 }, 'fast');
        } else {
            let polls = JSON.parse(localStorage.getItem('polls')) || [];
            let newPoll = {
                id: Date.now(),
                name: pollName
            };
            polls.push(newPoll);
            localStorage.setItem('polls', JSON.stringify(polls));
            
            alert('Poll created successfully!');
            window.location.href = 'list.html';
        }
    });

    if ($('#active tbody').length > 0) {
        let polls = JSON.parse(localStorage.getItem('polls')) || [];
        let tbody = $('#active tbody');
        
        polls.forEach(function(poll, index) {
            let row = `
                <tr id="poll-row-${poll.id}">
                    <td>${poll.id}</td>
                    <td>${poll.name}</td>
                    <td>
                        <button class="btn btn-sm">View results</button>
                        <button class="btn btn-sm">Close poll</button>
                        <button class="btn btn-sm btn-delete" data-id="${poll.id}">Delete</button>
                    </td>
                </tr>
            `;
            tbody.append(row);
        });
    }

    $(document).on('click', '.btn-delete', function() {
        let itemId = $(this).data('id');
        let row = $(this).closest('tr');
        
        if (confirm('Are you sure you want to delete this item?')) {
            $.ajax({
                url: 'https://jsonplaceholder.typicode.com/posts/1',
                type: 'DELETE',
                success: function(result) {
                    row.remove();
                    let polls = JSON.parse(localStorage.getItem('polls')) || [];
                    polls = polls.filter(p => p.id != itemId);
                    localStorage.setItem('polls', JSON.stringify(polls));
                    alert('Item deleted successfully via Ajax.');
                },
                error: function(err) {
                    alert('Error');
                }
            });
        }
    });

});

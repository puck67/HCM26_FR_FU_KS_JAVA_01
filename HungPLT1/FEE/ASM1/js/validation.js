$(document).ready(function() {
    $('#login-form').on('submit', function(e) {
        let isValid = true;
        const email = $('#email');
        const password = $('#password');

        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email.val().trim())) {
            showError(email, "Please enter a valid email address");
            isValid = false;
        }

        if (password.val().length < 6) {
            showError(password, "Password must be at least 6 characters");
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });

    $('#register-form').on('submit', function(e) {
        let isValid = true;
        const username = $('#username');
        const email = $('#email');
        const password = $('#password');
        const repassword = $('#repassword');

        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        const userRegex = /^[a-zA-Z0-9]{3,30}$/;
        if (!userRegex.test(username.val().trim())) {
            showError(username, "Username must be 3-30 characters, alphanumeric");
            isValid = false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email.val().trim())) {
            showError(email, "Please enter a valid email address");
            isValid = false;
        }

        if (password.val().length < 6) {
            showError(password, "Password must be at least 6 characters");
            isValid = false;
        }

        if (repassword.val() !== password.val()) {
            showError(repassword, "Passwords do not match");
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });

    $('#profile-form').on('submit', function(e) {
        let isValid = true;
        const firstname = $('#firstname');
        const lastname = $('#lastname');
        const phone = $('#phone');
        const description = $('#description');

        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        if (firstname.val().trim().length < 3 || firstname.val().trim().length > 30) {
            showError(firstname, "First name must be 3-30 characters");
            isValid = false;
        }

        if (lastname.val().trim().length < 3 || lastname.val().trim().length > 30) {
            showError(lastname, "Last name must be 3-30 characters");
            isValid = false;
        }

        const phoneRegex = /^[0-9]{9,11}$/;
        if (!phoneRegex.test(phone.val().trim())) {
            showError(phone, "Phone number must be 9-11 digits");
            isValid = false;
        }

        if (description.val().length > 200) {
            showError(description, "Description cannot exceed 200 characters");
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });

    $('#content-form').on('submit', function(e) {
        let isValid = true;
        const title = $('#title');
        const brief = $('#brief');
        const content = $('#content');

        $('.is-invalid').removeClass('is-invalid');
        $('.invalid-feedback').remove();

        if (title.val().trim().length < 10 || title.val().trim().length > 200) {
            showError(title, "Title must be 10-200 characters");
            isValid = false;
        }

        if (brief.val().trim().length < 30 || brief.val().trim().length > 1500) {
            showError(brief, "Brief must be 30-1500 characters");
            isValid = false;
        }

        if (content.val().trim().length < 50 || content.val().trim().length > 5000) {
            showError(content, "Content must be 50-5000 characters");
            isValid = false;
        }

        if (!isValid) {
            e.preventDefault();
        }
    });

    function showError(element, message) {
        element.addClass('is-invalid');
        element.after('<div class="invalid-feedback">' + message + '</div>');
    }

    $('.sidebar-search input').on('keyup', function() {
        var value = $(this).val().toLowerCase();
        $('table tbody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
});


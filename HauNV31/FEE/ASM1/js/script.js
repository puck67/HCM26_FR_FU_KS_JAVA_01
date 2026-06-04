$(document).ready(function() {
    
    // --- Validation Helper Functions ---
    function validateEmail(email) {
        var re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    function showError(elementId, show) {
        if (show) {
            $('#' + elementId).show();
        } else {
            $('#' + elementId).hide();
        }
    }

    // --- Login Form Validation ---
    $(document).on('submit', '#loginForm', function(e) {
        e.preventDefault();
        var isValid = true;
        var email = $('#email').val().trim();
        var password = $('#password').val();

        if (email === '' || !validateEmail(email)) {
            showError('emailError', true);
            isValid = false;
        } else {
            showError('emailError', false);
        }

        if (password === '') {
            showError('passwordError', true);
            isValid = false;
        } else {
            showError('passwordError', false);
        }

        if (isValid) {
            window.location.href = 'index.html';
        }
    });

    // --- Register Form Validation ---
    $(document).on('submit', '#registerForm', function(e) {
        e.preventDefault();
        var isValid = true;
        var username = $('#username').val().trim();
        var email = $('#regEmail').val().trim();
        var password = $('#regPassword').val();
        var rePassword = $('#rePassword').val();

        if (username === '') {
            showError('usernameError', true);
            isValid = false;
        } else {
            showError('usernameError', false);
        }

        if (email === '' || !validateEmail(email)) {
            showError('regEmailError', true);
            isValid = false;
        } else {
            showError('regEmailError', false);
        }

        if (password === '') {
            showError('regPasswordError', true);
            isValid = false;
        } else {
            showError('regPasswordError', false);
        }

        if (rePassword === '' || rePassword !== password) {
            showError('rePasswordError', true);
            isValid = false;
        } else {
            showError('rePasswordError', false);
        }

        if (isValid) {
            window.location.href = 'login.html';
        }
    });

    // --- Edit Profile Form Validation & AJAX Submit ---
    $(document).on('submit', '#editProfileForm', function(e) {
        e.preventDefault();
        var isValid = true;
        var firstName = $('#firstName').val().trim();
        var lastName = $('#lastName').val().trim();
        var phone = $('#phone').val().trim();
        var description = $('#description').val().trim();

        if (firstName === '') {
            showError('firstNameError', true);
            isValid = false;
        } else {
            showError('firstNameError', false);
        }

        if (lastName === '') {
            showError('lastNameError', true);
            isValid = false;
        } else {
            showError('lastNameError', false);
        }

        var phoneRegex = /^[0-9]{9,15}$/;
        if (phone === '' || !phoneRegex.test(phone)) {
            showError('phoneError', true);
            isValid = false;
        } else {
            showError('phoneError', false);
        }

        if (description === '') {
            showError('descriptionError', true);
            isValid = false;
        } else {
            showError('descriptionError', false);
        }

        if (isValid) {
            // Mock AJAX call for updating profile
            $.ajax({
                url: 'pages/edit_profile.html', // Dummy endpoint for demo
                type: 'GET', // Normally POST
                success: function(response) {
                    $('#profileUpdateSuccess').show().delay(3000).fadeOut();
                },
                error: function() {
                    alert("Error updating profile.");
                }
            });
        }
    });

    // --- Add Content Form Validation ---
    $(document).on('submit', '#addContentForm', function(e) {
        e.preventDefault();
        var isValid = true;
        var title = $('#title').val().trim();
        var brief = $('#brief').val().trim();
        var contentBody = $('#contentBody').val().trim();

        if (title === '') {
            showError('titleError', true);
            isValid = false;
        } else {
            showError('titleError', false);
        }

        if (brief === '') {
            showError('briefError', true);
            isValid = false;
        } else {
            showError('briefError', false);
        }

        if (contentBody === '') {
            showError('contentBodyError', true);
            isValid = false;
        } else {
            showError('contentBodyError', false);
        }

        if (isValid) {
            $('#contentAddSuccess').show().delay(3000).fadeOut();
            $('#addContentForm')[0].reset();
        }
    });

    // --- AJAX Page Loading with 5s Delay ---
    $(document).on('click', '.ajax-link', function(e) {
        e.preventDefault();
        var url = $(this).attr('href');
        
        // Update active class in sidebar
        if($(this).closest('#sidebar').length > 0) {
            $('#sidebar ul li a').removeClass('active');
            $(this).addClass('active');
        }

        loadContent(url, true);
    });

});

// Function to load content via AJAX
function loadContent(url, showDelay) {
    if(!url || url === '#') return;

    $('#main-content').hide();
    
    if (showDelay) {
        $('#loader').show();
        setTimeout(function() {
            $.ajax({
                url: url,
                type: 'GET',
                success: function(response) {
                    $('#loader').hide();
                    $('#main-content').html(response).fadeIn();
                },
                error: function() {
                    $('#loader').hide();
                    $('#main-content').html('<p class="text-danger">Error loading content.</p>').show();
                }
            });
        }, 5000); // 5 seconds delay as requested
    } else {
        $.ajax({
            url: url,
            type: 'GET',
            success: function(response) {
                $('#main-content').html(response).show();
            },
            error: function() {
                $('#main-content').html('<p class="text-danger">Error loading content.</p>').show();
            }
        });
    }
}

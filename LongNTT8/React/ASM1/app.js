$(document).ready(function() {
    // Form Validation Loop
    $(document).on('submit', 'form', function(e) {
        let isValid = true;
        let errorMessages = [];
        let formId = $(this).attr('id');
        
        // Loop through each input and textarea in the form
        $(this).find('input, textarea').each(function() {
            let el = $(this);
            let val = el.val().trim();
            let min = el.attr('minlength');
            let max = el.attr('maxlength');
            let isRequired = el.prop('required');
            let type = el.attr('type');
            
            // Get field name from placeholder, label, or default
            let fieldName = el.attr('placeholder') || el.prev('label').text() || 'Field';

            // Check if required
            if (isRequired && val === "") {
                isValid = false;
                errorMessages.push(fieldName + " is required.");
            } else if (val !== "") {
                // Check min length
                if (min && val.length < parseInt(min)) {
                    isValid = false;
                    errorMessages.push(fieldName + " must be at least " + min + " characters long.");
                }
                // Check max length
                if (max && val.length > parseInt(max)) {
                    isValid = false;
                    errorMessages.push(fieldName + " must not exceed " + max + " characters.");
                }
                // Check email format
                if (type === 'email') {
                    let emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(val)) {
                        isValid = false;
                        errorMessages.push(fieldName + " must be a valid email address.");
                    }
                }
                // Check Re Password matches Password
                if (el.attr('placeholder') === 'Re Password') {
                    let pwd = $(this).closest('form').find('input[placeholder="Password"]').val();
                    if (val !== pwd) {
                        isValid = false;
                        errorMessages.push("Passwords do not match.");
                    }
                }
            }
        });

        if (!isValid) {
            e.preventDefault();
            Swal.fire({
                icon: 'error',
                title: 'Validation Error',
                html: errorMessages.join("<br>"),
                confirmButtonColor: '#5cb85c'
            });
            return false;
        }

        // Specific handling for beautiful notifications based on form ID
        if (formId === 'editProfileForm') {
            e.preventDefault();
            let submitBtn = $(this).find('button[type="submit"]');
            let originalText = submitBtn.text();
            submitBtn.text('Updating...').prop('disabled', true);
            
            // Simulate Ajax request to update information
            setTimeout(function() {
                Swal.fire({
                    icon: 'success',
                    title: 'Updated!',
                    text: 'Information updated successfully via Ajax.',
                    confirmButtonColor: '#5cb85c'
                });
                submitBtn.text(originalText).prop('disabled', false);
            }, 1000);
            
        } else if (formId === 'registerForm') {
            e.preventDefault();
            Swal.fire({
                icon: 'success',
                title: 'Registration Successful!',
                text: 'Your account has been created. Please log in.',
                confirmButtonColor: '#5cb85c'
            }).then(() => {
                window.location.href = 'login.html';
            });
            
        } else if (formId === 'loginForm') {
            e.preventDefault();
            Swal.fire({
                icon: 'success',
                title: 'Welcome Back!',
                text: 'You have logged in successfully.',
                confirmButtonColor: '#5cb85c',
                timer: 1500,
                showConfirmButton: false
            }).then(() => {
                window.location.href = 'edit_profile.html';
            });
            
        } else if (formId === 'addContentForm') {
            e.preventDefault();
            Swal.fire({
                icon: 'success',
                title: 'Content Added!',
                text: 'The content form has been submitted.',
                confirmButtonColor: '#5cb85c'
            });
        }
    });

    // Ajax loading for sidebar links
    $(document).on('click', '.sidebar .nav-link', function(e) {
        let href = $(this).attr('href');
        
        // Only intercept if it's an HTML page link (View contents or Form content)
        if (href && href.endsWith('.html')) {
            e.preventDefault();
            
            // Update active class in sidebar
            $('.sidebar .nav-link').removeClass('active');
            $(this).addClass('active');

            // Show Loading screen evidence
            $('main').html('<div class="d-flex justify-content-center align-items-center" style="height: 100%; min-height: 300px;"><h2 class="text-secondary">Loading</h2></div>');
            
            // Wait 5 seconds as requested
            setTimeout(function() {
                // Call corresponding HTML page via Ajax
                $.get(href, function(data) {
                    // Extract the main content from the loaded page
                    let newMainContent = $(data).find('main').html();
                    let newTitle = $(data).filter('title').text();
                    
                    if (newMainContent) {
                        $('main').html(newMainContent);
                        if (newTitle) {
                            document.title = newTitle;
                        }
                        // Update the browser URL without full reload
                        window.history.pushState({"html": data, "pageTitle": newTitle}, "", href);
                    } else {
                        // Fallback if parsing fails
                        window.location.href = href;
                    }
                }).fail(function() {
                    $('main').html('<h2>Error loading content via Ajax</h2>');
                });
            }, 500);
        }
    });
    
    // Handle browser back/forward buttons when using history state
    window.onpopstate = function(e){
        if(e.state && e.state.html){
            $('main').html($(e.state.html).find('main').html());
            document.title = e.state.pageTitle;
        }
    };
});

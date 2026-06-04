$(document).ready(function() {
    // Validation function
    function validateField(value, fieldName, minLength, maxLength, required) {
        if (required && (!value || value.trim() === '')) {
            return `${fieldName} is required`;
        }
        if (value && minLength && value.length < minLength) {
            return `${fieldName} must be at least ${minLength} characters`;
        }
        if (value && maxLength && value.length > maxLength) {
            return `${fieldName} must not exceed ${maxLength} characters`;
        }
        return null;
    }

    // Email validation
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(email);
    }

    // Login form submission
    $('#loginForm').on('submit', function(e) {
        e.preventDefault();
        const email = $('#email').val();
        const password = $('#password').val();
        const rememberMe = $('#rememberMe').is(':checked');
        
        // Validation with specific requirements
        const emailError = validateField(email, 'Email', 5, 50, true);
        const passwordError = validateField(password, 'Password', 8, 30, true);
        
        if (emailError) {
            alert(emailError);
            return;
        }
        if (!validateEmail(email)) {
            alert('Please enter a valid email address');
            return;
        }
        if (passwordError) {
            alert(passwordError);
            return;
        }
        
        alert('Login successful!\nEmail: ' + email + '\nRemember Me: ' + rememberMe);
        window.location.href = 'view_content.html';
    });

    // Registration form submission
    $('#registerForm').on('submit', function(e) {
        e.preventDefault();
        const userName = $('#userName').val();
        const email = $('#regEmail').val();
        const password = $('#regPassword').val();
        const confirmPassword = $('#confirmPassword').val();
        
        // Validation with specific requirements
        const userNameError = validateField(userName, 'User name', 3, 30, true);
        const emailError = validateField(email, 'Email', 5, null, true);
        const passwordError = validateField(password, 'Password', 8, 30, true);
        const confirmPasswordError = validateField(confirmPassword, 'Re Password', 8, 30, true);
        
        if (userNameError) {
            alert(userNameError);
            return;
        }
        if (emailError) {
            alert(emailError);
            return;
        }
        if (!validateEmail(email)) {
            alert('Please enter a valid email address');
            return;
        }
        if (passwordError) {
            alert(passwordError);
            return;
        }
        if (confirmPasswordError) {
            alert(confirmPasswordError);
            return;
        }
        if (password !== confirmPassword) {
            alert('Passwords do not match!');
            return;
        }
        
        alert('Registration successful!\nUser name: ' + userName);
        window.location.href = 'index.html';
    });

    // Edit Profile form submission with AJAX
    $('#editProfileForm').on('submit', function(e) {
        e.preventDefault();
        const firstName = $('#editFirstName').val();
        const lastName = $('#editLastName').val();
        const email = $('#editEmail').val();
        const phone = $('#editPhone').val();
        const description = $('#editDescription').val();
        
        // Validation with specific requirements
        const firstNameError = validateField(firstName, 'First Name', 3, 30, true);
        const lastNameError = validateField(lastName, 'Last Name', 3, 30, true);
        const emailError = validateField(email, 'Email', 5, 50, true);
        const phoneError = validateField(phone, 'Phone', 9, 13, true);
        const descriptionError = validateField(description, 'Description', null, 200, false);
        
        if (firstNameError) {
            alert(firstNameError);
            return;
        }
        if (lastNameError) {
            alert(lastNameError);
            return;
        }
        if (emailError) {
            alert(emailError);
            return;
        }
        if (!validateEmail(email)) {
            alert('Please enter a valid email address');
            return;
        }
        if (phoneError) {
            alert(phoneError);
            return;
        }
        if (descriptionError) {
            alert(descriptionError);
            return;
        }
        
        // AJAX call to update profile
        $.ajax({
            url: 'api/update-profile.php',
            method: 'POST',
            data: {
                firstName: firstName,
                lastName: lastName,
                email: email,
                phone: phone,
                description: description
            },
            success: function(response) {
                alert('Profile updated successfully via AJAX!');
            },
            error: function(error) {
                alert('Error updating profile. Please try again.');
                console.error(error);
            }
        });
    });

    // Add Content form submission
    $('#addContentForm').on('submit', function(e) {
        e.preventDefault();
        const title = $('#title').val();
        const brief = $('#brief').val();
        const content = $('#content').val();
        
        // Validation with specific requirements
        const titleError = validateField(title, 'Title', 10, 200, true);
        const briefError = validateField(brief, 'Brief', 30, 150, true);
        const contentError = validateField(content, 'Content', 50, 1000, true);
        
        if (titleError) {
            alert(titleError);
            return;
        }
        if (briefError) {
            alert(briefError);
            return;
        }
        if (contentError) {
            alert(contentError);
            return;
        }
        
        // Get current date
        const currentDate = new Date().toISOString().split('T')[0];
        
        // Store in localStorage for persistence
        let contents = JSON.parse(localStorage.getItem('contents') || '[]');
        contents.push({
            title: title,
            brief: brief,
            content: content,
            createdDate: currentDate
        });
        localStorage.setItem('contents', JSON.stringify(contents));
        
        alert('Content added successfully!');
        $('#addContentForm')[0].reset();
        window.location.href = 'view_content.html';
    });

    // Search functionality for View Content page
    $('#searchInput').on('keyup', function() {
        const searchText = $(this).val().toLowerCase();
        $('#contentTableBody tr').filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(searchText) > -1);
        });
    });

    // Loading screen for menu navigation
    $('.nav-link').on('click', function(e) {
        const href = $(this).attr('href');
        // Skip if it's a dropdown toggle or logout
        if (href === '#' || href === 'index.html') {
            return true;
        }
        
        // Show loading screen
        $('body').append('<div id="loadingScreen" style="position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(255,255,255,0.9);display:flex;justify-content:center;align-items:center;z-index:9999;"><div style="text-align:center;"><h2>Loading...</h2><div style="width:50px;height:50px;border:5px solid #f3f3f3;border-top:5px solid #28a745;border-radius:50%;animation:spin 1s linear infinite;margin:20px auto;"></div></div></div>');
        $('head').append('<style>@keyframes spin{0%{transform:rotate(0deg);}100%{transform:rotate(360deg);}}</style>');
        
        // Navigate after 5 seconds
        setTimeout(function() {
            window.location.href = href;
        }, 5000);
        
        e.preventDefault();
    });

    // Load contents from localStorage on View Content page
    if(window.location.pathname.includes('view_content.html')) {
        let contents = JSON.parse(localStorage.getItem('contents') || '[]');
        if(contents.length > 0) {
            let tableHTML = '';
            contents.forEach(function(item) {
                tableHTML += `
                    <tr>
                        <td>${item.title}</td>
                        <td>${item.brief || 'No brief provided'}</td>
                        <td>${item.createdDate}</td>
                    </tr>
                `;
            });
            // Add sample data if exists
            tableHTML += `
                <tr>
                    <td>Sample Content 1</td>
                    <td>This is a brief description of sample content 1</td>
                    <td>2024-01-15</td>
                </tr>
                <tr>
                    <td>Sample Content 2</td>
                    <td>This is a brief description of sample content 2</td>
                    <td>2024-01-16</td>
                </tr>
                <tr>
                    <td>Sample Content 3</td>
                    <td>This is a brief description of sample content 3</td>
                    <td>2024-01-17</td>
                </tr>
            `;
            $('#contentTableBody').html(tableHTML);
        }
    }

    // Toggle class example
    $('.btn').on('click', function() {
        $(this).toggleClass('active');
    });

    // AJAX example (commented out as it requires a backend)
    /*
    $.ajax({
        url: 'your-api-endpoint',
        method: 'GET',
        success: function(response) {
            console.log(response);
        },
        error: function(error) {
            console.error(error);
        }
    });
    */
});

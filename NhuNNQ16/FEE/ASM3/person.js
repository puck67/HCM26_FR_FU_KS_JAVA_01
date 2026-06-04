$(document).ready(function () {
    // Form submission event
    $('#registerForm').on('submit', function (e) {
        // Prevent actual submission
        e.preventDefault();

        // Clear previous validation states and error messages
        $('.form-control').removeClass('is-invalid');
        $('.error-message').text('').hide();

        let isValid = true;

        // 1. First name validation
        // Mandatory (Y), Max length 20, Must be characters and not contain numbers
        let firstName = $('#firstName').val().trim();
        if (!firstName) {
            $('#firstName').addClass('is-invalid');
            $('#firstNameError').text('First name is mandatory.').show();
            isValid = false;
        } else if (firstName.length > 20) {
            $('#firstName').addClass('is-invalid');
            $('#firstNameError').text('First name cannot exceed 20 characters.').show();
            isValid = false;
        } else if (!/^[a-zA-Z\sÀ-ỹ]+$/.test(firstName)) { // Accents supported for Vietnamese names
            $('#firstName').addClass('is-invalid');
            $('#firstNameError').text('First name must contain only characters and not numbers.').show();
            isValid = false;
        }

        // 2. Last name validation
        // Mandatory (Y), Max length 20, Must be characters and not contain numbers
        let lastName = $('#lastName').val().trim();
        if (!lastName) {
            $('#lastName').addClass('is-invalid');
            $('#lastNameError').text('Last name is mandatory.').show();
            isValid = false;
        } else if (lastName.length > 20) {
            $('#lastName').addClass('is-invalid');
            $('#lastNameError').text('Last name cannot exceed 20 characters.').show();
            isValid = false;
        } else if (!/^[a-zA-Z\sÀ-ỹ]+$/.test(lastName)) {
            $('#lastName').addClass('is-invalid');
            $('#lastNameError').text('Last name must contain only characters and not numbers.').show();
            isValid = false;
        }

        // 3. Telephone validation
        // Optional (N), Max length 11, Must be numbers
        let telephone = $('#telephone').val().trim();
        if (telephone) {
            if (telephone.length > 11) {
                $('#telephone').addClass('is-invalid');
                $('#telephoneError').text('Telephone number cannot exceed 11 digits.').show();
                isValid = false;
            } else if (!/^\d+$/.test(telephone)) {
                $('#telephone').addClass('is-invalid');
                $('#telephoneError').text('Telephone must be numbers only.').show();
                isValid = false;
            }
        }

        // 4. Email validation
        // Optional (N), Max length 50, Must be valid email format [a..Z][0..9]@[a..Z]
        let email = $('#email').val().trim();
        if (email) {
            if (email.length > 50) {
                $('#email').addClass('is-invalid');
                $('#emailError').text('Email cannot exceed 50 characters.').show();
                isValid = false;
            } else {
                // Standard email format validation (matches characters/numbers @ characters . domain)
                let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!emailRegex.test(email)) {
                    $('#email').addClass('is-invalid');
                    $('#emailError').text('Email is invalid (must be user@domain.com format).').show();
                    isValid = false;
                }
            }
        }

        // 5. "You are in" (Continent) validation
        // Mandatory (Y)
        let continentChecked = $('input[name="continent"]:checked').val();
        if (!continentChecked) {
            $('#continentError').text('You must select your location.').show();
            isValid = false;
        }

        // 6. Description validation
        // Optional (N), Max length 200
        let description = $('#description').val().trim();
        if (description && description.length > 200) {
            $('#description').addClass('is-invalid');
            $('#descriptionError').text('Description cannot exceed 200 characters.').show();
            isValid = false;
        }

        // If validation passes, save data to localStorage
        if (isValid) {
            // Get selected hobbies
            let hobbiesSelected = [];
            $('input[name="hobbies"]:checked').each(function () {
                hobbiesSelected.push($(this).val());
            });

            // Build visitor object
            let visitor = {
                firstName: firstName,
                lastName: lastName,
                gender: $('#gender').val(),
                telephone: telephone || 'N/A',
                email: email || 'N/A',
                continent: continentChecked,
                hobbies: hobbiesSelected.length > 0 ? hobbiesSelected.join(', ') : 'None',
                description: description || 'N/A'
            };

            // Retrieve existing visitors or initialize empty array
            let visitors = JSON.parse(localStorage.getItem('visitors')) || [];
            
            // Add new visitor
            visitors.push(visitor);
            
            // Store back to localStorage
            localStorage.setItem('visitors', JSON.stringify(visitors));

            // Show success message
            $('#successAlert').fadeIn().delay(3000).fadeOut();

            // Reset the form
            $('#registerForm')[0].reset();
        }
    });
});

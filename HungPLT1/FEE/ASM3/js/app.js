$(document).ready(function() {
    if ($('#register-visitor-form').length > 0) {
        $('#register-visitor-form').on('submit', function(e) {
            e.preventDefault();

            var isValid = true;
            var errorMessages = [];

            $('#register-visitor-form .form-control').removeClass('input-invalid');
            $('#register-visitor-form label').removeClass('label-invalid');
            $('#register-visitor-form .inline-error').remove();
            $('#validation-alert-summary').empty();

            var firstName = $('#firstName');
            var fNameVal = firstName.val().trim();
            var charRegex = /^[a-zA-Z\s]+$/;

            if (fNameVal === '') {
                firstName.addClass('input-invalid');
                firstName.closest('.form-group').find('label').addClass('label-invalid');
                firstName.after('<span class="inline-error">First name is required</span>');
                errorMessages.push("First name field is required.");
                isValid = false;
            } else if (fNameVal.length > 20) {
                firstName.addClass('input-invalid');
                firstName.closest('.form-group').find('label').addClass('label-invalid');
                firstName.after('<span class="inline-error">First name cannot exceed 20 characters</span>');
                errorMessages.push("First name cannot exceed 20 characters.");
                isValid = false;
            } else if (!charRegex.test(fNameVal)) {
                firstName.addClass('input-invalid');
                firstName.closest('.form-group').find('label').addClass('label-invalid');
                firstName.after('<span class="inline-error">First name must contain only characters and no numbers</span>');
                errorMessages.push("First name must be characters and not contain numbers.");
                isValid = false;
            }

            var lastName = $('#lastName');
            var lNameVal = lastName.val().trim();

            if (lNameVal === '') {
                lastName.addClass('input-invalid');
                lastName.closest('.form-group').find('label').addClass('label-invalid');
                lastName.after('<span class="inline-error">Last name is required</span>');
                errorMessages.push("Last name field is required.");
                isValid = false;
            } else if (lNameVal.length > 20) {
                lastName.addClass('input-invalid');
                lastName.closest('.form-group').find('label').addClass('label-invalid');
                lastName.after('<span class="inline-error">Last name cannot exceed 20 characters</span>');
                errorMessages.push("Last name cannot exceed 20 characters.");
                isValid = false;
            } else if (!charRegex.test(lNameVal)) {
                lastName.addClass('input-invalid');
                lastName.closest('.form-group').find('label').addClass('label-invalid');
                lastName.after('<span class="inline-error">Last name must contain only characters and no numbers</span>');
                errorMessages.push("Last name must be characters and not contain numbers.");
                isValid = false;
            }

            var telephone = $('#telephone');
            var telVal = telephone.val().trim();
            var numRegex = /^\d+$/;

            if (telVal !== '') {
                if (telVal.length > 11) {
                    telephone.addClass('input-invalid');
                    telephone.closest('.form-group').find('label').addClass('label-invalid');
                    telephone.after('<span class="inline-error">Telephone cannot exceed 11 digits</span>');
                    errorMessages.push("Telephone cannot exceed 11 digits.");
                    isValid = false;
                } else if (!numRegex.test(telVal)) {
                    telephone.addClass('input-invalid');
                    telephone.closest('.form-group').find('label').addClass('label-invalid');
                    telephone.after('<span class="inline-error">Telephone must be a number</span>');
                    errorMessages.push("Telephone must be a number.");
                    isValid = false;
                }
            }

            var email = $('#email');
            var emailVal = email.val().trim();
            var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

            if (emailVal !== '') {
                if (emailVal.length > 50) {
                    email.addClass('input-invalid');
                    email.closest('.form-group').find('label').addClass('label-invalid');
                    email.after('<span class="inline-error">Email cannot exceed 50 characters</span>');
                    errorMessages.push("Email cannot exceed 50 characters.");
                    isValid = false;
                } else if (!emailRegex.test(emailVal)) {
                    email.addClass('input-invalid');
                    email.closest('.form-group').find('label').addClass('label-invalid');
                    email.after('<span class="inline-error">Email must match standard email format</span>');
                    errorMessages.push("Email must match valid email format.");
                    isValid = false;
                }
            }

            var regionChecked = $('input[name="region"]:checked');
            if (regionChecked.length === 0) {
                $('input[name="region"]').closest('.form-group').find('.form-label-bold').addClass('label-invalid');
                $('#region-error-placement').html('<span class="inline-error">You must select a region</span>');
                errorMessages.push("You must select a region.");
                isValid = false;
            }

            var description = $('#description');
            var descVal = description.val().trim();
            if (descVal !== '' && descVal.length > 200) {
                description.addClass('input-invalid');
                description.closest('.form-group').find('label').addClass('label-invalid');
                description.after('<span class="inline-error">Description cannot exceed 200 characters</span>');
                errorMessages.push("Description cannot exceed 200 characters.");
                isValid = false;
            }

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
                var selectedHobbies = [];
                $('.hobby-checkbox:checked').each(function() {
                    selectedHobbies.push($(this).val());
                });

                var newVisitor = {
                    firstName: fNameVal,
                    lastName: lNameVal,
                    gender: $('#gender').val(),
                    telephone: telVal || 'N/A',
                    region: regionChecked.val(),
                    hobbies: selectedHobbies.length > 0 ? selectedHobbies.join(', ') : 'N/A',
                    description: descVal || 'N/A'
                };

                var visitors = JSON.parse(localStorage.getItem('visitors')) || [];
                visitors.push(newVisitor);
                localStorage.setItem('visitors', JSON.stringify(visitors));

                var successHtml = '<div class="alert alert-success font-weight-bold">';
                successHtml += '<h5 class="alert-heading">Success!</h5>';
                successHtml += '<p class="mb-0">Visitor registered successfully.</p>';
                successHtml += '</div>';
                $('#validation-alert-summary').html(successHtml);

                $('#register-visitor-form')[0].reset();
                $('html, body').animate({ scrollTop: 0 }, 'slow');
            }
        });
    }

    if ($('#visitors-table').length > 0) {
        var visitors = JSON.parse(localStorage.getItem('visitors')) || [];
        var tableBody = $('#visitors-table tbody');

        for (var i = 0; i < visitors.length; i++) {
            var v = visitors[i];
            var rowHtml = '<tr>';
            rowHtml += '<td>' + v.firstName + '</td>';
            rowHtml += '<td>' + v.lastName + '</td>';
            rowHtml += '<td>' + v.gender + '</td>';
            rowHtml += '<td>' + v.telephone + '</td>';
            rowHtml += '<td>' + v.region + '</td>';
            rowHtml += '<td>' + v.hobbies + '</td>';
            rowHtml += '<td>' + v.description + '</td>';
            rowHtml += '</tr>';
            tableBody.append(rowHtml);
        }

        $('#search-visitor-form').on('submit', function(e) {
            e.preventDefault();
            var keyword = $('#searchKeyword').val().trim().toLowerCase();

            $('#visitors-table tbody tr').each(function() {
                var row = $(this);
                var rowText = row.text().toLowerCase();

                if (keyword === '' || rowText.indexOf(keyword) > -1) {
                    row.show();
                } else {
                    row.hide();
                }
            });
        });
    }
});

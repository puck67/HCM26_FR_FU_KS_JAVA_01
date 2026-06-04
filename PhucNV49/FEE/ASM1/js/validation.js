/**
 * CMS Frontend Validation Engine
 * Loops through form fields to check for required status, length limits, and data formats (email, phone, etc.)
 */

function validateForm($form) {
    let isValid = true;

    // Clear previous invalid states
    $form.find('.form-control').removeClass('is-invalid');
    $form.find('.invalid-feedback').remove();

    // Loop through each input/textarea/select field in the form
    $form.find('input, textarea, select').each(function() {
        const $field = $(this);
        const val = $field.val().trim();
        const name = $field.attr('name');
        const type = $field.attr('type');
        const label = $form.find(`label[for="${$field.attr('id')}"]`).text().trim() || name || 'Field';

        // 1. Check Mandatory / Required check
        const isRequired = $field.prop('required') || $field.attr('required') !== undefined;
        if (isRequired && val === '') {
            showError($field, `${label} is required.`);
            isValid = false;
            return; // Skip further checks for this field
        }

        // Skip validation for empty optional fields
        if (val === '') {
            return;
        }

        // 2. Length limits check
        const minLength = parseInt($field.attr('minlength'));
        const maxLength = parseInt($field.attr('maxlength'));

        if (!isNaN(minLength) && val.length < minLength) {
            showError($field, `${label} must be at least ${minLength} characters.`);
            isValid = false;
            return;
        }

        if (!isNaN(maxLength) && val.length > maxLength) {
            showError($field, `${label} must not exceed ${maxLength} characters.`);
            isValid = false;
            return;
        }

        // 3. Format/Pattern checks based on type or name
        if (type === 'email' || name === 'email') {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(val)) {
                showError($field, `Please enter a valid email address.`);
                isValid = false;
                return;
            }
        }

        if (name === 'phone') {
            const phoneRegex = /^[0-9]+$/; // Checks that phone number contains only numbers
            if (!phoneRegex.test(val)) {
                showError($field, `Phone number must contain only digits.`);
                isValid = false;
                return;
            }
            // Additional check for length is handled by minlength/maxlength attributes
        }

        // 4. Password confirmation check
        if (name === 'repassword') {
            const $passwordField = $form.find('input[name="password"]');
            if ($passwordField.length > 0 && val !== $passwordField.val()) {
                showError($field, `Passwords do not match.`);
                isValid = false;
                return;
            }
        }
    });

    return isValid;
}

/**
 * Utility to display validation error feedback
 */
function showError($field, message) {
    $field.addClass('is-invalid');
    
    // Check if error feedback element already exists
    let $feedback = $field.siblings('.invalid-feedback');
    if ($feedback.length === 0) {
        $feedback = $('<div class="invalid-feedback"></div>');
        $field.after($feedback);
    }
    $feedback.text(message).show();
}

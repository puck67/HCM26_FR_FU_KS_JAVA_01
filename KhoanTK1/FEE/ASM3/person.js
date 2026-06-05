$(document).ready(function () {
    // helper to validate first name
    function checkGivenName() {
        let val = $('#input-given-name').val().trim();
        if (!val) {
            $('#input-given-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-given-name').text('First name is mandatory.').show();
            return false;
        } else if (val.length > 20) {
            $('#input-given-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-given-name').text('First name cannot exceed 20 characters.').show();
            return false;
        } else if (!/^[a-zA-Z\sÀ-ỹ]+$/.test(val)) {
            $('#input-given-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-given-name').text('First name must contain only characters and not numbers.').show();
            return false;
        } else {
            $('#input-given-name').removeClass('is-invalid').addClass('is-valid');
            $('#err-given-name').hide();
            return true;
        }
    }

    // helper to validate last name
    function checkFamilyName() {
        let val = $('#input-family-name').val().trim();
        if (!val) {
            $('#input-family-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-family-name').text('Last name is mandatory.').show();
            return false;
        } else if (val.length > 20) {
            $('#input-family-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-family-name').text('Last name cannot exceed 20 characters.').show();
            return false;
        } else if (!/^[a-zA-Z\sÀ-ỹ]+$/.test(val)) {
            $('#input-family-name').removeClass('is-valid').addClass('is-invalid');
            $('#err-family-name').text('Last name must contain only characters and not numbers.').show();
            return false;
        } else {
            $('#input-family-name').removeClass('is-invalid').addClass('is-valid');
            $('#err-family-name').hide();
            return true;
        }
    }

    // helper to validate phone
    function checkPhoneNum() {
        let val = $('#input-phone-num').val().trim();
        if (!val) {
            $('#input-phone-num').removeClass('is-invalid').removeClass('is-valid');
            $('#err-phone-num').hide();
            return true;
        } else if (val.length > 11) {
            $('#input-phone-num').removeClass('is-valid').addClass('is-invalid');
            $('#err-phone-num').text('Telephone number cannot exceed 11 digits.').show();
            return false;
        } else if (!/^\d+$/.test(val)) {
            $('#input-phone-num').removeClass('is-valid').addClass('is-invalid');
            $('#err-phone-num').text('Telephone must be numbers only.').show();
            return false;
        } else {
            $('#input-phone-num').removeClass('is-invalid').addClass('is-valid');
            $('#err-phone-num').hide();
            return true;
        }
    }

    // helper to validate email
    function checkMailboxAddr() {
        let val = $('#input-mailbox').val().trim();
        if (!val) {
            $('#input-mailbox').removeClass('is-invalid').removeClass('is-valid');
            $('#err-mailbox').hide();
            return true;
        } else if (val.length > 50) {
            $('#input-mailbox').removeClass('is-valid').addClass('is-invalid');
            $('#err-mailbox').text('Email cannot exceed 50 characters.').show();
            return false;
        } else {
            let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailRegex.test(val)) {
                $('#input-mailbox').removeClass('is-valid').addClass('is-invalid');
                $('#err-mailbox').text('Email is invalid (must be user@domain.com format).').show();
                return false;
            } else {
                $('#input-mailbox').removeClass('is-invalid').addClass('is-valid');
                $('#err-mailbox').hide();
                return true;
            }
        }
    }

    // helper to validate region
    function checkRegionLocale() {
        let checkedVal = $('input[name="region_locale"]:checked').val();
        if (!checkedVal) {
            $('#err-region-locale').text('You must select your location.').show();
            return false;
        } else {
            $('#err-region-locale').hide();
            return true;
        }
    }

    // helper to validate description
    function checkRemarksField() {
        let val = $('#input-remarks-field').val().trim();
        if (val.length > 200) {
            $('#input-remarks-field').removeClass('is-valid').addClass('is-invalid');
            $('#err-remarks-field').text('Description cannot exceed 200 characters.').show();
            return false;
        } else if (val.length > 0) {
            $('#input-remarks-field').removeClass('is-invalid').addClass('is-valid');
            $('#err-remarks-field').hide();
            return true;
        } else {
            $('#input-remarks-field').removeClass('is-invalid').removeClass('is-valid');
            $('#err-remarks-field').hide();
            return true;
        }
    }

    // real-time inputs validation
    $('#input-given-name').on('input blur', checkGivenName);
    $('#input-family-name').on('input blur', checkFamilyName);
    $('#input-phone-num').on('input blur', checkPhoneNum);
    $('#input-mailbox').on('input blur', checkMailboxAddr);
    $('input[name="region_locale"]').on('change', checkRegionLocale);
    $('#input-remarks-field').on('input blur', checkRemarksField);

    // handles form submit
    $('#visitor-entry-form').on('submit', function (e) {
        e.preventDefault();

        // check all inputs
        let isGNameValid = checkGivenName();
        let isFNameValid = checkFamilyName();
        let isPhoneValid = checkPhoneNum();
        let isMailValid = checkMailboxAddr();
        let isRegionValid = checkRegionLocale();
        let isRemarksValid = checkRemarksField();

        let passedChecks = isGNameValid && isFNameValid && isPhoneValid && isMailValid && isRegionValid && isRemarksValid;

        // save to store if clear
        if (passedChecks) {
            let chosenActivities = [];
            $('input[name="leisure_activities"]:checked').each(function () {
                chosenActivities.push($(this).val());
            });

            // construct guest structure
            let guestItem = {
                firstName: $('#input-given-name').val().trim(),
                lastName: $('#input-family-name').val().trim(),
                gender: $('#select-sex-type').val(),
                telephone: $('#input-phone-num').val().trim() || 'N/A',
                email: $('#input-mailbox').val().trim() || 'N/A',
                continent: $('input[name="region_locale"]:checked').val(),
                hobbies: chosenActivities.length > 0 ? chosenActivities.join(', ') : 'None',
                description: $('#input-remarks-field').val().trim() || 'N/A'
            };

            // get from local storage
            let guestList = JSON.parse(localStorage.getItem('guest_registry_data')) || [];
            
            // add to registry
            guestList.push(guestItem);
            
            // save back
            localStorage.setItem('guest_registry_data', JSON.stringify(guestList));

            // pop success alert
            $('#alert-notify-success').fadeIn().delay(3000).fadeOut();

            // clear all states
            $('#visitor-entry-form')[0].reset();
            $('.form-control').removeClass('is-valid').removeClass('is-invalid');
        }
    });
});

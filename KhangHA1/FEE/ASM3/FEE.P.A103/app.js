/**
 * app.js
 * Logic for Visitor Management App (ASM3)
 */

$(document).ready(function() {

    // ==========================================
    // PERSON.HTML - Validation Logic
    // ==========================================
    if ($('#visitorForm').length) {
        
        $('#btnRegister').click(function() {
            // Reset all errors and styles
            $('.error-msg').hide();
            $('.form-control').removeClass('is-invalid');
            $('#visitorForm .radio-grid').removeClass('is-invalid'); // Add logic for radio group if needed
            
            let isValid = true;

            // 1. First name: Mandatory Y, Max length 20, Must be characters and not contain number
            let firstName = $('#firstName').val().trim();
            if (firstName === '' || firstName.length > 20 || /[^a-zA-Z\s]/.test(firstName)) {
                $('#firstName').addClass('is-invalid');
                $('#err-firstName').show();
                isValid = false;
            }

            // 2. Last name: Mandatory Y, Max length 20, Must be characters and not contain number
            let lastName = $('#lastName').val().trim();
            if (lastName === '' || lastName.length > 20 || /[^a-zA-Z\s]/.test(lastName)) {
                $('#lastName').addClass('is-invalid');
                $('#err-lastName').show();
                isValid = false;
            }

            // 3. Telephone: Mandatory N, Max length 11, Must be number
            let telephone = $('#telephone').val().trim();
            if (telephone !== '') {
                if (telephone.length > 11 || /[^0-9]/.test(telephone)) {
                    $('#telephone').addClass('is-invalid');
                    $('#err-telephone').show();
                    isValid = false;
                }
            }

            // 4. Email: Mandatory N, Max length 50, standard email format
            let email = $('#email').val().trim();
            if (email !== '') {
                // simple email regex
                let emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
                if (email.length > 50 || !emailPattern.test(email)) {
                    $('#email').addClass('is-invalid');
                    $('#err-email').show();
                    isValid = false;
                }
            }

            // 5. You are in: Mandatory Y
            let regionChecked = $('input[name="region"]:checked').length > 0;
            if (!regionChecked) {
                $('#err-region').show();
                isValid = false;
            }

            // 6. Description: Mandatory N, Max length 200
            let description = $('#description').val().trim();
            if (description.length > 200) {
                $('#description').addClass('is-invalid');
                $('#err-description').show();
                isValid = false;
            }

            if (isValid) {
                alert('Registration successful!');
                // In a real app, we might save to localStorage or send an AJAX request here.
                $('#visitorForm')[0].reset();
            }
        });

        // Live validation clearing
        $('.form-control').on('input', function() {
            $(this).removeClass('is-invalid');
            $(this).siblings('.error-msg').hide();
        });
        $('input[name="region"]').on('change', function() {
            $('#err-region').hide();
        });
    }


    // ==========================================
    // SEARCH.HTML - Dynamic Search Logic
    // ==========================================
    if ($('#visitorTable').length) {

        $('#btnSearch').click(function() {
            performSearch();
        });

        // Optionally, search as you type or on Enter key press
        $('#searchInput').on('keyup', function(e) {
            if(e.key === 'Enter') {
                performSearch();
            }
        });

        function performSearch() {
            let keyword = $('#searchInput').val().trim().toLowerCase();
            
            // Remove highlight classes first
            $('#visitorTable tbody tr').removeClass('highlight-row');

            $('#visitorTable tbody tr').each(function() {
                let row = $(this);
                let rowText = row.text().toLowerCase();
                
                if (keyword === '') {
                    // if empty keyword, just show all, no highlight
                    row.show();
                } else if (rowText.indexOf(keyword) > -1) {
                    row.show();
                    row.addClass('highlight-row');
                } else {
                    row.hide();
                }
            });
        }
    }

});

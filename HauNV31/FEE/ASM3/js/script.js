$(document).ready(function () {
    
    // --- Validation logic for person.html ---
    $('#personForm').on('submit', function(e) {
        e.preventDefault();
        
        let isValid = true;
        
        // Validation rules
        const nameRegex = /^[A-Za-z\s]+$/; // Characters and spaces only (no numbers)
        const phoneRegex = /^[0-9]+$/; // Numbers only
        // Standard email regex or specific pattern required by assignment [a-z][0-9]@[a-z] but generally:
        const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;

        // 1. First name: Mandatory, Max length 20, characters only
        const firstName = $('#firstName').val().trim();
        if (firstName === "" || firstName.length > 20 || !nameRegex.test(firstName)) {
            $('#firstName').addClass('is-invalid');
            isValid = false;
        } else {
            $('#firstName').removeClass('is-invalid').addClass('is-valid');
        }

        // 2. Last name: Mandatory, Max length 20, characters only
        const lastName = $('#lastName').val().trim();
        if (lastName === "" || lastName.length > 20 || !nameRegex.test(lastName)) {
            $('#lastName').addClass('is-invalid');
            isValid = false;
        } else {
            $('#lastName').removeClass('is-invalid').addClass('is-valid');
        }

        // 3. Telephone: Optional, Max length 11, Must be number
        const phone = $('#telephone').val().trim();
        if (phone !== "" && (phone.length > 11 || !phoneRegex.test(phone))) {
            $('#telephone').addClass('is-invalid');
            isValid = false;
        } else {
            $('#telephone').removeClass('is-invalid');
            if (phone !== "") $('#telephone').addClass('is-valid');
        }

        // 4. Email: Optional, Max length 50, valid email pattern
        const email = $('#email').val().trim();
        if (email !== "" && (email.length > 50 || !emailRegex.test(email))) {
            $('#email').addClass('is-invalid');
            isValid = false;
        } else {
            $('#email').removeClass('is-invalid');
            if (email !== "") $('#email').addClass('is-valid');
        }

        // 5. You are in (Location): Mandatory
        const location = $('#location').val();
        if (location === "") {
            $('#location').addClass('is-invalid');
            isValid = false;
        } else {
            $('#location').removeClass('is-invalid').addClass('is-valid');
        }

        // 6. Description: Optional, Max length 200
        const description = $('#description').val().trim();
        if (description.length > 200) {
            $('#description').addClass('is-invalid');
            isValid = false;
        } else {
            $('#description').removeClass('is-invalid');
            if (description !== "") $('#description').addClass('is-valid');
        }

        // If all valid, show success
        if (isValid) {
            $('#alertSuccess').removeClass('d-none');
            // Reset form conceptually
            // $('#personForm')[0].reset(); 
            // $('.is-valid').removeClass('is-valid');
        } else {
            $('#alertSuccess').addClass('d-none');
        }
    });

    // Remove validation states on input
    $('#personForm input, #personForm select, #personForm textarea').on('input change', function() {
        $(this).removeClass('is-invalid is-valid');
        $('#alertSuccess').addClass('d-none');
    });


    // --- Search logic for search.html ---
    $('#btnSearch').on('click', function() {
        let keyword = $('#searchKeyword').val().toLowerCase().trim();
        let hasResults = false;

        if (keyword === "") {
            // Show all rows if keyword is empty
            $('#resultsTable tbody tr').show();
            $('#noResults').addClass('d-none');
            return;
        }

        $('#resultsTable tbody tr').each(function() {
            let rowText = $(this).text().toLowerCase();
            if (rowText.indexOf(keyword) > -1) {
                $(this).show();
                hasResults = true;
            } else {
                $(this).hide();
            }
        });

        if (!hasResults) {
            $('#noResults').removeClass('d-none');
        } else {
            $('#noResults').addClass('d-none');
        }
    });

    // Also trigger search on Enter key press
    $('#searchKeyword').on('keypress', function(e) {
        if(e.which == 13) {
            $('#btnSearch').click();
        }
    });
});

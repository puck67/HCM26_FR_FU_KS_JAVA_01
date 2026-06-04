$(document).ready(function() {
    // Mock default visitor list matching the figures
    const defaultVisitors = [
        {
            firstname: "John",
            lastname: "Terry",
            gender: "Male",
            phone: "0909090909",
            region: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstname: "John",
            lastname: "Terry",
            gender: "Male",
            phone: "0909090909",
            region: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstname: "John",
            lastname: "Terry",
            gender: "Male",
            phone: "0909090909",
            region: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        }
    ];

    // Initialize local storage database
    if (!localStorage.getItem('hrVisitors')) {
        localStorage.setItem('hrVisitors', JSON.stringify(defaultVisitors));
    }

    // --- A. REGISTRATION FORM LOGIC ---
    const $registerForm = $('#visitor-registration-form');
    if ($registerForm.length > 0) {
        $registerForm.on('submit', function(e) {
            e.preventDefault();
            
            // Clear previous errors
            $('#error-summary-wrapper').empty();
            $('.form-control').removeClass('is-invalid');
            $('.invalid-field-feedback').addClass('d-none');
            $('label').removeClass('is-invalid-label');
            $('#region-error').addClass('d-none');

            const errors = [];

            // 1. Validate First name
            const $firstname = $('#visitor-firstname');
            const firstnameVal = $firstname.val().trim();
            if (firstnameVal === '') {
                errors.push("First name is required.");
                markInvalid($firstname, '#lbl-firstname');
            } else if (firstnameVal.length > 20) {
                errors.push("First name must not exceed 20 characters.");
                markInvalid($firstname, '#lbl-firstname');
            } else if (/[0-9]/.test(firstnameVal)) {
                errors.push("First name must not contain numbers.");
                markInvalid($firstname, '#lbl-firstname');
            }

            // 2. Validate Last name
            const $lastname = $('#visitor-lastname');
            const lastnameVal = $lastname.val().trim();
            if (lastnameVal === '') {
                errors.push("Last name is required.");
                markInvalid($lastname, '#lbl-lastname');
            } else if (lastnameVal.length > 20) {
                errors.push("Last name must not exceed 20 characters.");
                markInvalid($lastname, '#lbl-lastname');
            } else if (/[0-9]/.test(lastnameVal)) {
                errors.push("Last name must not contain numbers.");
                markInvalid($lastname, '#lbl-lastname');
            }

            // 3. Validate Telephone (Optional, digits-only, max 11)
            const $phone = $('#visitor-phone');
            const phoneVal = $phone.val().trim();
            if (phoneVal !== '') {
                if (phoneVal.length > 11) {
                    errors.push("Telephone must not exceed 11 characters.");
                    markInvalid($phone, '#lbl-phone');
                } else if (!/^[0-9]+$/.test(phoneVal)) {
                    errors.push("Telephone must contain only digits.");
                    markInvalid($phone, '#lbl-phone');
                }
            }

            // 4. Validate Email (Optional, max 50, valid format)
            const $email = $('#visitor-email');
            const emailVal = $email.val().trim();
            if (emailVal !== '') {
                if (emailVal.length > 50) {
                    errors.push("Email must not exceed 50 characters.");
                    markInvalid($email, '#lbl-email');
                } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(emailVal)) {
                    errors.push("Email format is invalid. Please enter a valid email.");
                    markInvalid($email, '#lbl-email');
                }
            }

            // 5. Validate You are in (Region - Required)
            const regionSelected = $('input[name="region"]:checked').val();
            if (!regionSelected) {
                errors.push("You are in (Region selection) is required.");
                $('#lbl-region').addClass('is-invalid-label');
                $('#region-error').removeClass('d-none');
            }

            // 6. Validate Description (Optional, max 200)
            const $desc = $('#visitor-desc');
            const descVal = $desc.val().trim();
            if (descVal !== '' && descVal.length > 200) {
                errors.push("Description must not exceed 200 characters.");
                markInvalid($desc, '#lbl-desc');
            }

            // Handle errors or proceed
            if (errors.length > 0) {
                let errorHtml = `
                    <div class="alert alert-danger validation-error-summary" role="alert">
                        <h5 class="font-weight-bold mb-2">
                            <i class="fas fa-exclamation-circle mr-2"></i>Please fix the following validation errors:
                        </h5>
                        <ul class="mb-0 pl-3">
                `;
                errors.forEach(function(err) {
                    errorHtml += `<li>${err}</li>`;
                });
                errorHtml += `
                        </ul>
                    </div>
                `;
                
                $('#error-summary-wrapper').html(errorHtml);
                
                // Scroll page smoothly to error box
                $('html, body').animate({
                    scrollTop: 0
                }, 'smooth');
            } else {
                // Success - Save and Redirect
                const hobbiesList = [];
                $('input[name="hobbies"]:checked').each(function() {
                    hobbiesList.push($(this).val());
                });

                const newVisitor = {
                    firstname: firstnameVal,
                    lastname: lastnameVal,
                    gender: $('#visitor-gender').val(),
                    phone: phoneVal || 'N/A',
                    region: regionSelected,
                    hobbies: hobbiesList,
                    description: descVal || 'N/A'
                };

                const currentList = JSON.parse(localStorage.getItem('hrVisitors')) || [];
                currentList.push(newVisitor);
                localStorage.setItem('hrVisitors', JSON.stringify(currentList));

                // Disable submit button and redirect
                $('#btn-register-visitor').html('<i class="fas fa-spinner fa-spin mr-2"></i>Registering...').prop('disabled', true);
                
                setTimeout(function() {
                    window.location.href = 'search.html';
                }, 800);
            }
        });
    }

    // Helper to highlight invalid field
    function markInvalid($input, labelSelector) {
        $input.addClass('is-invalid');
        $input.siblings('.invalid-field-feedback').removeClass('d-none');
        $(labelSelector).addClass('is-invalid-label');
    }

    // --- B. SEARCH VISITORS LOGIC ---
    const $searchForm = $('#search-visitor-form');
    if ($searchForm.length > 0) {
        renderVisitorsTable();

        $searchForm.on('submit', function(e) {
            e.preventDefault();
            performVisitorSearch();
        });

        //Snappy real-time filtering on typing too
        $('#search-input').on('keyup', function() {
            performVisitorSearch();
        });
    }

    function renderVisitorsTable() {
        const visitors = JSON.parse(localStorage.getItem('hrVisitors')) || [];
        const $tableBody = $('#visitors-table-body');
        $tableBody.empty();

        if (visitors.length === 0) {
            $tableBody.append(`
                <tr>
                    <td colspan="7" class="text-center text-muted">No visitors registered in database.</td>
                </tr>
            `);
            return;
        }

        visitors.forEach(function(v) {
            const hobbiesText = v.hobbies.length > 0 ? v.hobbies.join(', ') : 'None';
            $tableBody.append(`
                <tr class="visitor-row">
                    <td class="font-weight-bold text-dark">${escapeHtml(v.firstname)}</td>
                    <td>${escapeHtml(v.lastname)}</td>
                    <td>${escapeHtml(v.gender)}</td>
                    <td>${escapeHtml(v.phone)}</td>
                    <td>${escapeHtml(v.region)}</td>
                    <td>${escapeHtml(hobbiesText)}</td>
                    <td class="text-muted text-wrap" style="max-width: 250px;">${escapeHtml(v.description)}</td>
                </tr>
            `);
        });
    }

    function performVisitorSearch() {
        const query = $('#search-input').val().trim().toLowerCase();
        let matchCount = 0;

        $('.visitor-row').each(function() {
            const $row = $(this);
            let rowMatches = false;

            if (query === '') {
                rowMatches = true;
            } else {
                // Loop through each table cell (td) in the row
                $row.find('td').each(function() {
                    const cellText = $(this).text().toLowerCase();
                    if (cellText.indexOf(query) !== -1) {
                        rowMatches = true;
                        return false; // Break loop
                    }
                });
            }

            if (rowMatches) {
                $row.show();
                matchCount++;
            } else {
                $row.hide();
            }
        });

        if (matchCount === 0) {
            $('#no-results-message').removeClass('d-none');
        } else {
            $('#no-results-message').addClass('d-none');
        }
    }

    // Helper to escape HTML characters
    function escapeHtml(text) {
        if (!text) return '';
        return text
            .toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});

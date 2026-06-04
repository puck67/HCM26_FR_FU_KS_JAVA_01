$(document).ready(function() {

    // =========================================================================
    // 1. STATE & STORAGE MANAGEMENT (search.html & person.html sharing)
    // =========================================================================
    const storageKey = 'hr_visitors';
    
    // Default Visitor Data matching page 5 screenshot
    const defaultVisitors = [
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            region: "Europe",
            hobbies: "Shopping, Cooking",
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            region: "Europe",
            hobbies: "Shopping, Cooking",
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            region: "Europe",
            hobbies: "Shopping, Cooking",
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        }
    ];

    function getVisitors() {
        const stored = localStorage.getItem(storageKey);
        const parsed = stored ? JSON.parse(stored) : [];
        // Combine default items with user entered items
        return [...defaultVisitors, ...parsed];
    }

    function saveVisitor(visitor) {
        const stored = localStorage.getItem(storageKey);
        const parsed = stored ? JSON.parse(stored) : [];
        parsed.push(visitor);
        localStorage.setItem(storageKey, JSON.stringify(parsed));
    }

    // =========================================================================
    // 2. REGISTRATION FORM VALIDATION (person.html)
    // =========================================================================
    if ($('#visitor-form').length > 0) {
        const $form = $('#visitor-form');
        const $firstName = $('#first-name');
        const $lastName = $('#last-name');
        const $telephone = $('#telephone');
        const $email = $('#email');
        const $description = $('#description');
        const $descCounter = $('#desc-counter');

        // Character counter for Description
        $description.on('input keyup', function() {
            const len = $(this).val().length;
            $descCounter.text(`${len} / 200 characters`);
            if (len > 200) {
                $descCounter.addClass('text-danger').removeClass('text-muted');
            } else {
                $descCounter.removeClass('text-danger').addClass('text-muted');
            }
        });

        // Form Submit
        $form.on('submit', function(e) {
            e.preventDefault();
            
            // Clear prior errors
            $('.form-control').removeClass('is-invalid');
            $('#region-error').text('').hide();
            $('#alert-container').empty();

            let isValid = true;

            // 1. First Name Validation
            const firstNameVal = $firstName.val().trim();
            const lettersOnlyRegex = /^[a-zA-Z\sÀ-ỹ]+$/; // Support letters and Vietnamese accents
            
            if (!firstNameVal) {
                isValid = false;
                markFieldInvalid($firstName, "First name is mandatory.");
            } else if (firstNameVal.length > 20) {
                isValid = false;
                markFieldInvalid($firstName, "First name must not exceed 20 characters.");
            } else if (!lettersOnlyRegex.test(firstNameVal)) {
                isValid = false;
                markFieldInvalid($firstName, "First name must contain letters only (no numbers or special characters).");
            }

            // 2. Last Name Validation
            const lastNameVal = $lastName.val().trim();
            if (!lastNameVal) {
                isValid = false;
                markFieldInvalid($lastName, "Last name is mandatory.");
            } else if (lastNameVal.length > 20) {
                isValid = false;
                markFieldInvalid($lastName, "Last name must not exceed 20 characters.");
            } else if (!lettersOnlyRegex.test(lastNameVal)) {
                isValid = false;
                markFieldInvalid($lastName, "Last name must contain letters only (no numbers or special characters).");
            }

            // 3. Telephone Validation (Optional, numbers only, max 11 digits)
            const telephoneVal = $telephone.val().trim();
            const numbersOnlyRegex = /^\d+$/;
            if (telephoneVal) {
                if (telephoneVal.length > 11) {
                    isValid = false;
                    markFieldInvalid($telephone, "Telephone must not exceed 11 digits.");
                } else if (!numbersOnlyRegex.test(telephoneVal)) {
                    isValid = false;
                    markFieldInvalid($telephone, "Telephone must be numbers only.");
                }
            }

            // 4. Email Validation (Optional, max 50, standard email format)
            const emailVal = $email.val().trim();
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (emailVal) {
                if (emailVal.length > 50) {
                    isValid = false;
                    markFieldInvalid($email, "Email must not exceed 50 characters.");
                } else if (!emailRegex.test(emailVal)) {
                    isValid = false;
                    markFieldInvalid($email, "Please enter a valid email address (e.g. name@domain.com).");
                }
            }

            // 5. You Are In (Geographic region) Validation
            const selectedRegion = $('input[name="region"]:checked').val();
            if (!selectedRegion) {
                isValid = false;
                $('#region-error').text("Please select the geographic region you are in.").show();
            }

            // 6. Description Validation (Optional, max 200)
            const descriptionVal = $description.val().trim();
            if (descriptionVal && descriptionVal.length > 200) {
                isValid = false;
                markFieldInvalid($description, "Description must not exceed 200 characters.");
            }

            // 7. Success Processing
            if (isValid) {
                // Collect Hobbies
                const hobbies = [];
                $('input[name="hobbies"]:checked').each(function() {
                    hobbies.push($(this).val());
                });

                const newVisitor = {
                    firstName: firstNameVal,
                    lastName: lastNameVal,
                    gender: $('#gender').val(),
                    telephone: telephoneVal || 'N/A',
                    region: selectedRegion,
                    hobbies: hobbies.length > 0 ? hobbies.join(', ') : 'None',
                    description: descriptionVal || 'N/A'
                };

                saveVisitor(newVisitor);

                // Display success alert
                const alertHtml = `
                    <div class="alert alert-success border-0 shadow-sm" role="alert">
                        <h5 class="alert-heading font-weight-bold"><i class="fas fa-check-circle mr-2"></i>Registration Successful!</h5>
                        <p class="mb-0">Visitor "${firstNameVal} ${lastNameVal}" has been registered. Redirecting to Search tab...</p>
                    </div>
                `;
                $('#alert-container').html(alertHtml);
                
                // Clear form
                $form[0].reset();
                $descCounter.text('0 / 200 characters');

                // Redirect to search.html
                setTimeout(function() {
                    window.location.href = 'search.html';
                }, 1500);
            }
        });

        function markFieldInvalid($field, message) {
            $field.addClass('is-invalid');
            $field.siblings('.invalid-feedback').text(message);
        }
    }

    // =========================================================================
    // 3. SEARCH & TABLE RENDERING (search.html)
    // =========================================================================
    if ($('#visitor-table-body').length > 0) {
        const $tableBody = $('#visitor-table-body');
        const $searchInput = $('#search-input');
        const $searchForm = $('#search-form');

        const allVisitors = getVisitors();

        function renderTable(filterQuery = '') {
            $tableBody.empty();
            
            const query = filterQuery.toLowerCase().trim();
            
            // Filter visitors matching query on ANY column/property
            const filtered = allVisitors.filter(visitor => {
                if (!query) return true;
                return (
                    visitor.firstName.toLowerCase().includes(query) ||
                    visitor.lastName.toLowerCase().includes(query) ||
                    visitor.gender.toLowerCase().includes(query) ||
                    visitor.telephone.toLowerCase().includes(query) ||
                    visitor.region.toLowerCase().includes(query) ||
                    visitor.hobbies.toLowerCase().includes(query) ||
                    visitor.description.toLowerCase().includes(query)
                );
            });

            if (filtered.length === 0) {
                $tableBody.append(`
                    <tr>
                        <td colspan="7" class="text-center text-muted font-italic py-4">
                            No matching visitors found.
                        </td>
                    </tr>
                `);
                return;
            }

            filtered.forEach(v => {
                const trHtml = `
                    <tr>
                        <td class="font-weight-bold text-dark">${escapeHtml(v.firstName)}</td>
                        <td>${escapeHtml(v.lastName)}</td>
                        <td>${escapeHtml(v.gender)}</td>
                        <td>${escapeHtml(v.telephone)}</td>
                        <td>${escapeHtml(v.region)}</td>
                        <td>${escapeHtml(v.hobbies)}</td>
                        <td class="text-muted" style="font-size: 0.85rem;">${escapeHtml(v.description)}</td>
                    </tr>
                `;
                $tableBody.append(trHtml);
            });
        }

        // Search Form Submit
        $searchForm.on('submit', function(e) {
            e.preventDefault();
            renderTable($searchInput.val());
        });

        // Optional: Real-time search filter for enhanced responsiveness
        $searchInput.on('input', function() {
            renderTable($(this).val());
        });

        // Initial table load
        renderTable();
    }

    // Helper for HTML escaping
    function escapeHtml(text) {
        if (typeof text !== 'string') return '';
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

});

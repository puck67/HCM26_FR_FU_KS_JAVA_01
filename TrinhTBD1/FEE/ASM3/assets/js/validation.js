$(document).ready(function() {
    // Seed localStorage if empty with John Terry records from Figure 2
    const seedVisitors = [
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            youAreIn: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            youAreIn: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            youAreIn: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        }
    ];

    if (!localStorage.getItem("visitors")) {
        localStorage.setItem("visitors", JSON.stringify(seedVisitors));
    }

    // Form elements
    const $form = $('#visitorForm');
    const $firstName = $('#firstName');
    const $lastName = $('#lastName');
    const $telephone = $('#telephone');
    const $email = $('#email');
    const $description = $('#description');

    // Validation functions
    function validateFirstName() {
        const value = $firstName.val().trim();
        if (value === "") {
            showError($firstName, "First name is mandatory and cannot be empty.");
            return false;
        }
        if (value.length > 20) {
            showError($firstName, "First name must not exceed 20 characters.");
            return false;
        }
        // Unicode letter and space regex to support international and Vietnamese names while rejecting numbers and symbols
        if (!/^[\p{L}\s]+$/u.test(value)) {
            showError($firstName, "First name must contain characters only and cannot contain numbers.");
            return false;
        }
        showSuccess($firstName);
        return true;
    }

    function validateLastName() {
        const value = $lastName.val().trim();
        if (value === "") {
            showError($lastName, "Last name is mandatory and cannot be empty.");
            return false;
        }
        if (value.length > 20) {
            showError($lastName, "Last name must not exceed 20 characters.");
            return false;
        }
        // Unicode letter and space regex
        if (!/^[\p{L}\s]+$/u.test(value)) {
            showError($lastName, "Last name must contain characters only and cannot contain numbers.");
            return false;
        }
        showSuccess($lastName);
        return true;
    }

    function validateTelephone() {
        const value = $telephone.val().trim();
        if (value === "") {
            clearFeedback($telephone);
            return true; // Optional field
        }
        if (value.length > 11) {
            showError($telephone, "Telephone must not exceed 11 digits.");
            return false;
        }
        if (!/^\d+$/.test(value)) {
            showError($telephone, "Telephone must be a number containing digits only.");
            return false;
        }
        showSuccess($telephone);
        return true;
    }

    function validateEmail() {
        const value = $email.val().trim();
        if (value === "") {
            clearFeedback($email);
            return true; // Optional field
        }
        if (value.length > 50) {
            showError($email, "Email must not exceed 50 characters.");
            return false;
        }
        // Email match pattern standard
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(value)) {
            showError($email, "Email format is invalid. Must be a valid email (e.g., example@domain.com).");
            return false;
        }
        showSuccess($email);
        return true;
    }

    function validateLocation() {
        const isChecked = $("input[name='youAreIn']:checked").length > 0;
        const $radioGroup = $('#location-error-anchor');
        if (!isChecked) {
            showGroupError($radioGroup, "Please select one region for 'You are in' location.");
            return false;
        }
        showGroupSuccess($radioGroup);
        return true;
    }

    function validateDescription() {
        const value = $description.val().trim();
        if (value === "") {
            clearFeedback($description);
            return true; // Optional field
        }
        if (value.length > 200) {
            showError($description, "Description must not exceed 200 characters.");
            return false;
        }
        showSuccess($description);
        return true;
    }

    // Helper functions for JQuery feedback using standard Bootstrap 4 classes
    function showError($input, message) {
        $input.removeClass('is-valid').addClass('is-invalid');
        const $errorDiv = $input.siblings('.invalid-feedback');
        if ($errorDiv.length) {
            $errorDiv.text(message).show();
        }
    }

    // Input changes back to normal on success
    function showSuccess($input) {
        $input.removeClass('is-invalid').addClass('is-valid');
        const $errorDiv = $input.siblings('.invalid-feedback');
        if ($errorDiv.length) {
            $errorDiv.hide();
        }
    }

    function clearFeedback($input) {
        $input.removeClass('is-invalid is-valid');
        const $errorDiv = $input.siblings('.invalid-feedback');
        if ($errorDiv.length) {
            $errorDiv.hide();
        }
    }

    function showGroupError($anchor, message) {
        const $errorDiv = $anchor.find('.invalid-feedback');
        if ($errorDiv.length) {
            $errorDiv.text(message).show();
        }
    }

    function showGroupSuccess($anchor) {
        const $errorDiv = $anchor.find('.invalid-feedback');
        if ($errorDiv.length) {
            $errorDiv.hide();
        }
    }

    // Bind real-time input checks
    $firstName.on('input blur', validateFirstName);
    $lastName.on('input blur', validateLastName);
    $telephone.on('input blur', validateTelephone);
    $email.on('input blur', validateEmail);
    $description.on('input blur', validateDescription);
    $("input[name='youAreIn']").on('change', validateLocation);

    // Form submission validation
    $form.on('submit', function(event) {
        event.preventDefault();

        const isFirstNameValid = validateFirstName();
        const isLastNameValid = validateLastName();
        const isTelephoneValid = validateTelephone();
        const isEmailValid = validateEmail();
        const isLocationValid = validateLocation();
        const isDescriptionValid = validateDescription();

        const isFormValid = isFirstNameValid && isLastNameValid && isTelephoneValid && isEmailValid && isLocationValid && isDescriptionValid;

        if (!isFormValid) {
            return;
        }

        // Gather hobbies
        const hobbies = [];
        $("input[name='hobbies']:checked").each(function() {
            hobbies.push($(this).val());
        });

        // Assemble data
        const newVisitor = {
            firstName: $firstName.val().trim(),
            lastName: $lastName.val().trim(),
            gender: $('#gender').val(),
            telephone: $telephone.val().trim() || "N/A",
            email: $email.val().trim() || "N/A",
            youAreIn: $("input[name='youAreIn']:checked").val(),
            hobbies: hobbies.length > 0 ? hobbies : ["None"],
            description: $description.val().trim() || "N/A"
        };

        // Save
        const visitors = JSON.parse(localStorage.getItem("visitors")) || [];
        visitors.push(newVisitor);
        localStorage.setItem("visitors", JSON.stringify(visitors));

        // Assemble dynamic summary HTML (clean, sleek, generic layout)
        const hobbiesText = newVisitor.hobbies.join(", ");
        const summaryHtml = `
            <p class="text-secondary mb-3" style="font-size: 14px;">The visitor has been registered successfully with the following details:</p>
            <table class="table table-sm table-borderless mb-0" style="font-size: 14px;">
                <tbody>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary" style="width: 35%;">Full Name:</td>
                        <td class="text-dark font-weight-bold">${newVisitor.firstName} ${newVisitor.lastName}</td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">Gender:</td>
                        <td class="text-dark">${newVisitor.gender}</td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">Telephone:</td>
                        <td class="text-dark">${newVisitor.telephone}</td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">Email:</td>
                        <td class="text-dark">${newVisitor.email}</td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">You are in:</td>
                        <td class="text-dark"><span class="badge badge-success px-2 py-1" style="background-color: var(--primary-green); font-size: 12px;">${newVisitor.youAreIn}</span></td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">Hobbies:</td>
                        <td class="text-dark">${hobbiesText}</td>
                    </tr>
                    <tr>
                        <td class="font-weight-bold pl-0 text-secondary">Description:</td>
                        <td class="text-dark text-break">${newVisitor.description}</td>
                    </tr>
                </tbody>
            </table>
        `;

        // Render summary inside modal body and show modal
        $('#successModalBody').html(summaryHtml);
        $('#successModal').modal('show');

        // Reset the form inputs
        $form[0].reset();
        $('.form-control').removeClass('is-valid is-invalid');
        $('.invalid-feedback').hide();
    });
});

/**
 * CMS Front-End Essentials Engine (script.js)
 * High-fidelity client-side state management, custom validators,
 * and AJAX simulators.
 */

$(document).ready(function () {

    // ---------------------------------------------------
    // A. INITIAL STATE & SEEDING (LOCALSTORAGE)
    // ---------------------------------------------------

    // Seed initial users if empty
    if (!localStorage.getItem('users')) {
        const defaultUsers = [
            {
                username: 'admin',
                email: 'admin@example.com',
                password: 'password123',
                firstName: 'Admin',
                lastName: 'User',
                phone: '0987654321',
                description: 'Super administrator for the CMS portal with full authorization controls.'
            }
        ];
        localStorage.setItem('users', JSON.stringify(defaultUsers));
    }

    // Seed initial contents if empty
    if (!localStorage.getItem('contents')) {
        const defaultContents = [
            {
                id: 1,
                title: 'Introduction to Front-End Essentials',
                brief: 'Explore the fundamental concepts of HTML5, CSS3, and JavaScript modern workflows in building standard web systems.',
                content: 'Front-end development focuses on the user interface and user experience of websites and web applications. It involves coding with core technologies such as HTML for markup structure, CSS for styling, layout, typography, and responsive presentation, and JavaScript to add complex functional behaviors and dynamics. Modern front-end engineering also incorporates framework setups, utility kits like Bootstrap 4, package managers, and automated task builders to optimize loading assets.',
                createdDate: '2026-06-01 10:30'
            },
            {
                id: 2,
                title: 'Building Responsive UIs with Bootstrap 4',
                brief: 'A deep-dive analysis on Bootstrap 4 grids, container systems, breakpoints, and pre-built styling components.',
                content: 'Bootstrap is one of the most widely adopted open-source CSS frameworks designed to streamline mobile-first website structures. By exploiting the flexible 12-column grid layout, predefined utilities, and an abundance of layout templates, developers can deliver outstanding cross-browser responsive interfaces with very little effort. In version 4, Bootstrap introduced a powerful Flexbox-based alignment engine, refined card mechanisms, spacing modifiers, and updated sass styling definitions.',
                createdDate: '2026-06-02 08:15'
            },
            {
                id: 3,
                title: 'Understanding AJAX and Web Communication APIs',
                brief: 'Understand client-server data transfer asynchronously utilizing jQuery AJAX and Fetch techniques without refreshing.',
                content: 'Asynchronous JavaScript and XML (AJAX) is a web design methodology that permits pages to communicate asynchronously with remote servers by dispatching small, light-weight JSON packages. By using jQuery’s $.ajax API, application models can post, get, and process server-side data models in the background, updating isolated segments of the DOM tree dynamically without demanding a complete page reload, resulting in fluid desktop-grade application flows.',
                createdDate: '2026-06-02 14:00'
            }
        ];
        localStorage.setItem('contents', JSON.stringify(defaultContents));
    }

    // Update statistics dashboard counts if they exist on the page
    updateDashboardStats();


    // ---------------------------------------------------
    // B. CENTRAL MANDATORY FIELD CHECKER (THE LOOP UTILITY)
    // ---------------------------------------------------

    /**
     * Requirement: "The form must be checked to make sure all the mandatory fields are filled in the input tag. 
     * It would require just a loop through each field in the form and check for data."
     * 
     * Loops through every field (input, textarea, select) within a form.
     * Marks missing fields as invalid and shows validation tags.
     */
    function checkMandatoryFields($form) {
        let isAllFilled = true;

        // Find all fields marked with "required"
        $form.find('input[required], textarea[required], select[required]').each(function () {
            const $field = $(this);
            const value = $field.val();

            // Trim if value is string
            const cleanedValue = typeof value === 'string' ? value.trim() : value;

            if (!cleanedValue) {
                // Field is empty: set invalid
                $field.addClass('is-invalid').removeClass('is-valid');
                isAllFilled = false;
            } else {
                // Field is filled: clean validation (subsequent criteria check follows later)
                $field.removeClass('is-invalid');
            }
        });

        return isAllFilled;
    }


    // ---------------------------------------------------
    // C. AUTHENTICATION PAGES LOGIC (LOGIN & REGISTER)
    // ---------------------------------------------------

    // 1. REGISTER FORM SUBMIT
    $('#registerForm').on('submit', function (e) {
        e.preventDefault();

        const $form = $(this);
        const $alert = $('#registerAlert');

        // Hide previous messages
        $alert.addClass('d-none').removeClass('alert-success alert-danger');

        // First, loop check mandatory fields
        const isMandatoryPassed = checkMandatoryFields($form);
        if (!isMandatoryPassed) {
            $alert.addClass('alert-danger').removeClass('d-none').text('Please fill in all the mandatory fields.');
            return;
        }

        // Custom validation ranges and structures
        const username = $('#registerUsername').val().trim();
        const email = $('#registerEmail').val().trim();
        const password = $('#registerPassword').val();
        const rePassword = $('#registerRePassword').val();

        let isValid = true;

        // User name length [3-30]
        if (username.length < 3 || username.length > 30) {
            $('#registerUsername').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#registerUsername').addClass('is-valid').removeClass('is-invalid');
        }

        // Email length [5-50] & Standard format validation
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (email.length < 5 || email.length > 50 || !emailRegex.test(email)) {
            $('#registerEmail').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#registerEmail').addClass('is-valid').removeClass('is-invalid');
        }

        // Password length [8-30]
        if (password.length < 8 || password.length > 30) {
            $('#registerPassword').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#registerPassword').addClass('is-valid').removeClass('is-invalid');
        }

        // Re-password match check
        if (rePassword !== password || !rePassword) {
            $('#registerRePassword').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#registerRePassword').addClass('is-valid').removeClass('is-invalid');
        }

        if (!isValid) {
            $alert.addClass('alert-danger').removeClass('d-none').text('Please correct the validation errors in the fields.');
            return;
        }

        // Check if user already exists
        const usersList = JSON.parse(localStorage.getItem('users')) || [];
        const isUserExist = usersList.some(u => u.email.toLowerCase() === email.toLowerCase());

        if (isUserExist) {
            $('#registerEmail').addClass('is-invalid').removeClass('is-valid');
            $alert.addClass('alert-danger').removeClass('d-none').text('This email address is already registered.');
            return;
        }

        // Seed new user account
        const newUser = {
            username: username,
            email: email,
            password: password,
            firstName: '',
            lastName: '',
            phone: '',
            description: ''
        };

        usersList.push(newUser);
        localStorage.setItem('users', JSON.stringify(usersList));

        // Save registered user temporarily in sessionStorage to autofill login screen
        sessionStorage.setItem('justRegisteredEmail', email);

        $alert.addClass('alert-success').removeClass('d-none').text('Registration successful! Redirecting to login page...');
        $form.find('input').removeClass('is-invalid is-valid');
        $form.find('button').prop('disabled', true);

        // Smooth delayed redirect
        setTimeout(function () {
            window.location.href = 'login.html';
        }, 1800);
    });

    // Autofill registered email if available on landing
    if (sessionStorage.getItem('justRegisteredEmail') && $('#loginEmail').length > 0) {
        $('#loginEmail').val(sessionStorage.getItem('justRegisteredEmail'));
        sessionStorage.removeItem('justRegisteredEmail');
    }

    // 2. LOGIN FORM SUBMIT
    $('#loginForm').on('submit', function (e) {
        e.preventDefault();

        const $form = $(this);
        const $alert = $('#loginAlert');

        $alert.addClass('d-none').removeClass('alert-success alert-danger');

        // Loop check mandatory fields
        const isMandatoryPassed = checkMandatoryFields($form);
        if (!isMandatoryPassed) {
            $alert.addClass('alert-danger').removeClass('d-none').text('Please enter all mandatory fields.');
            return;
        }

        const email = $('#loginEmail').val().trim();
        const password = $('#loginPassword').val();

        let isValid = true;

        // Email validation: length [5-50]
        if (email.length < 5 || email.length > 50) {
            $('#loginEmail').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#loginEmail').addClass('is-valid').removeClass('is-invalid');
        }

        // Password validation: length [8-30]
        if (password.length < 8 || password.length > 30) {
            $('#loginPassword').addClass('is-invalid').removeClass('is-valid');
            isValid = false;
        } else {
            $('#loginPassword').addClass('is-valid').removeClass('is-invalid');
        }

        if (!isValid) {
            return;
        }

        // Verify credentials against user store
        const usersList = JSON.parse(localStorage.getItem('users')) || [];
        const matchedUser = usersList.find(u => u.email.toLowerCase() === email.toLowerCase() && u.password === password);

        if (!matchedUser) {
            $('#loginEmail, #loginPassword').addClass('is-invalid').removeClass('is-valid');
            $alert.addClass('alert-danger').removeClass('d-none').text('Invalid Email or Password. Please try again.');
            return;
        }

        // Authentication Success
        localStorage.setItem('currentUser', JSON.stringify(matchedUser));

        // Remember me handler (visual state mock)
        if ($('#rememberMe').is(':checked')) {
            localStorage.setItem('rememberedEmail', email);
        } else {
            localStorage.removeItem('rememberedEmail');
        }

        $alert.addClass('alert-success').removeClass('d-none').text('Login successful! Welcome back.');
        $form.find('input').removeClass('is-invalid is-valid');
        $form.find('button').prop('disabled', true);

        setTimeout(function () {
            window.location.href = 'index.html';
        }, 1200);
    });

    // Populate remembered email if present
    if (localStorage.getItem('rememberedEmail') && $('#loginEmail').length > 0) {
        $('#loginEmail').val(localStorage.getItem('rememberedEmail'));
        $('#rememberMe').prop('checked', true);
    }


    // ---------------------------------------------------
    // D. CMS MAIN DASHBOARD CORE ENGINE (AJAX & TIMERS)
    // ---------------------------------------------------

    // Verify user is logged in when landing on index.html
    const isDashboardPage = $('#dynamicContentContainer').length > 0;

    if (isDashboardPage) {
        const currentUserStr = localStorage.getItem('currentUser');
        if (!currentUserStr) {
            // Not authorized: kick back to login
            window.location.href = 'login.html';
            return;
        }

        // Authorized: display user credentials
        const currentUser = JSON.parse(currentUserStr);
        updateUserHeaderDetails(currentUser);

        // Sidebar Navigation click router with 5 seconds Loading effect
        $('.nav-item-link').on('click', function (e) {
            e.preventDefault();

            const targetPage = $(this).data('page');
            const clickedElement = $(this);

            // Check if user is clicking the current page
            const currentSidebarActive = $('#sidebar ul li.active a').data('page');

            // Don't restart loading if we are already viewing it (unless profile dropdown load)
            if (targetPage === currentSidebarActive && !clickedElement.hasClass('dropdown-item')) {
                return;
            }

            // Close responsive mobile sidebar when clicked
            $('#sidebar').removeClass('active');

            // Determine if loading screen is required (5 seconds required for Sidebar menu clicks)
            const isSidebarItem = clickedElement.closest('#sidebar').length > 0;

            if (isSidebarItem && (targetPage === 'view-content' || targetPage === 'add-content')) {
                // TRIGGER 5 SECONDS COUNTDOWN LOADING SCREEN
                triggerPageLoader(5, function () {
                    // Update active CSS menu selection
                    $('#sidebar ul li').removeClass('active');
                    clickedElement.closest('li').addClass('active');

                    // Fetch and inject HTML page via Mock AJAX
                    loadPageContent(targetPage);
                });
            } else {
                // Non-left sidebar menu clicks (e.g. Profile Dropdown, Dashboard Home) - fast load
                // Update active CSS state (if clicking profile, remove active from sidebar)
                if (targetPage === 'edit-profile') {
                    $('#sidebar ul li').removeClass('active');
                } else if (targetPage === 'dashboard') {
                    $('#sidebar ul li').removeClass('active');
                    $('#menuDashboard').addClass('active');
                }

                loadPageContent(targetPage);
            }
        });

        // Logout Event handler
        $('#btnLogout').on('click', function (e) {
            e.preventDefault();
            localStorage.removeItem('currentUser');
            window.location.href = 'login.html';
        });

        // Sidebar Mobile Toggle button behavior
        $('#sidebarCollapse').on('click', function () {
            $('#sidebar').toggleClass('active');
        });
    }

    /**
     * Requirement: "Click on item in left menu, it will be loading in 5 seconds, after that it will call corresponding HTML page."
     * Runs a visual 5-second blurred glass overlay timer inside the content wrapper.
     */
    function triggerPageLoader(seconds, onFinish) {
        const $overlay = $('#loadingOverlay');
        const $timerText = $('#loadingTimerText');

        $overlay.css('display', 'flex'); // Show using flexbox centering

        let timeLeft = seconds;
        $timerText.html(`<i class="fas fa-spinner fa-spin mr-2"></i>Loading content... <strong>${timeLeft}s</strong>`);

        const timerInterval = setInterval(function () {
            timeLeft--;
            if (timeLeft <= 0) {
                clearInterval(timerInterval);
                $overlay.fadeOut(200, function () {
                    $overlay.css('display', 'none');
                    if (typeof onFinish === 'function') {
                        onFinish();
                    }
                });
            } else {
                $timerText.html(`<i class="fas fa-spinner fa-spin mr-2"></i>Loading content... <strong>${timeLeft}s</strong>`);
            }
        }, 1000);
    }

    /**
     * Requirement: "Use Ajax to load View Content page and update information at Edit Profile."
     * Loads the respective sub-HTML file via jQuery AJAX $.get() and binds dynamic behaviors.
     */
    function loadPageContent(pageName) {
        const $container = $('#dynamicContentContainer');

        if (pageName === 'dashboard') {
            // Re-render dashboard dashboard content
            const stats = JSON.parse(localStorage.getItem('contents')) || [];
            const dashHtml = `
                <div class="content-card">
                    <div class="content-card-title">
                        <i class="fas fa-chart-line"></i> Dashboard Overview
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-4">
                            <div class="card bg-success text-white shadow-sm border-0" style="background-color: green !important;">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="text-uppercase mb-1" style="font-size: 0.8rem; opacity: 0.8;">Total Contents</h6>
                                            <h3 class="font-weight-bold m-0" id="statTotalContents">${stats.length}</h3>
                                        </div>
                                        <i class="fas fa-file-alt fa-2x" style="opacity: 0.3;"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card bg-dark text-white shadow-sm border-0" style="background-color: #2e3e4e !important;">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="text-uppercase mb-1" style="font-size: 0.8rem; opacity: 0.8;">Account Age</h6>
                                            <h3 class="font-weight-bold m-0">Active</h3>
                                        </div>
                                        <i class="fas fa-user-clock fa-2x" style="opacity: 0.3;"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 mb-4">
                            <div class="card bg-info text-white shadow-sm border-0" style="background-color: #17a2b8 !important;">
                                <div class="card-body">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <h6 class="text-uppercase mb-1" style="font-size: 0.8rem; opacity: 0.8;">System Status</h6>
                                            <h3 class="font-weight-bold m-0">Online</h3>
                                        </div>
                                        <i class="fas fa-server fa-2x" style="opacity: 0.3;"></i>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="mt-4 p-4 border rounded bg-light">
                        <h5 class="font-weight-bold text-dark mb-3"><i class="fas fa-bullhorn text-success mr-2"></i>Welcome back to CMS Admin Portal!</h5>
                        <p class="text-muted mb-0">Use the left menu to view, search, and add fresh content blocks into your management portal. You can also view and edit your profile parameters using the upper-right corner dropdown menu.</p>
                    </div>
                </div>
            `;
            $container.html(dashHtml);
            updateDashboardStats();
            return;
        }

        // ACTUAL AJAX CALL to fetch pages: edit-profile.html, add-content.html, view-content.html
        $.ajax({
            url: `${pageName}.html`,
            type: 'GET',
            dataType: 'html',
            success: function (data) {
                // Inject retrieved page fragment
                $container.html(data);

                // Initialize page elements based on target
                if (pageName === 'edit-profile') {
                    bindEditProfileEvents();
                } else if (pageName === 'add-content') {
                    bindAddContentEvents();
                } else if (pageName === 'view-content') {
                    renderContentsTable();
                }
            },
            error: function (xhr, status, error) {
                console.error("AJAX Page Fetch Error:", error);
                $container.html(`
                    <div class="alert alert-danger" role="alert">
                        <h5 class="font-weight-bold"><i class="fas fa-exclamation-triangle mr-2"></i>Failed to load content</h5>
                        <p class="mb-0">An error occurred while loading the sub-page asynchronously. Error details: ${status} - ${error}</p>
                    </div>
                `);
            }
        });
    }

    // ---------------------------------------------------
    // E. EDIT PROFILE SUBPAGE LOGIC (AJAX SUBMIT)
    // ---------------------------------------------------
    function bindEditProfileEvents() {
        const currentUser = JSON.parse(localStorage.getItem('currentUser'));
        if (!currentUser) return;

        // Autofill current user parameters
        $('#profileFirstName').val(currentUser.firstName || '');
        $('#profileLastName').val(currentUser.lastName || '');
        $('#profileEmail').val(currentUser.email);
        $('#profilePhone').val(currentUser.phone || '');
        $('#profileDescription').val(currentUser.description || '');
        $('#charCount').text((currentUser.description || '').length);

        // Form Submit
        $('#profileForm').on('submit', function (e) {
            e.preventDefault();

            const $form = $(this);
            const $alert = $('#profileAlert');
            const $btn = $('#btnSubmitProfile');

            $alert.addClass('d-none').removeClass('alert-success alert-danger');

            // 1. Mandatory checks loop
            const isMandatoryPassed = checkMandatoryFields($form);
            if (!isMandatoryPassed) {
                $alert.addClass('alert-danger').removeClass('d-none').text('Please fill in all the mandatory fields.');
                return;
            }

            const firstName = $('#profileFirstName').val().trim();
            const lastName = $('#profileLastName').val().trim();
            const phone = $('#profilePhone').val().trim();
            const description = $('#profileDescription').val().trim();

            let isValid = true;

            // First Name validation [3-30]
            if (firstName.length < 3 || firstName.length > 30) {
                $('#profileFirstName').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#profileFirstName').addClass('is-valid').removeClass('is-invalid');
            }

            // Last Name validation [3-30]
            if (lastName.length < 3 || lastName.length > 30) {
                $('#profileLastName').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#profileLastName').addClass('is-valid').removeClass('is-invalid');
            }

            // Phone validation [9-13] digits
            const phoneRegex = /^\d{9,13}$/;
            if (!phoneRegex.test(phone)) {
                $('#profilePhone').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#profilePhone').addClass('is-valid').removeClass('is-invalid');
            }

            // Description length validation: max 200 chars
            if (description.length > 200) {
                $('#profileDescription').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#profileDescription').addClass('is-valid').removeClass('is-invalid');
            }

            if (!isValid) {
                $alert.addClass('alert-danger').removeClass('d-none').text('Please correct validation issues below.');
                return;
            }

            // Requirement: "In the Edit profile, if click to Submit button, it will call to update your information via Ajax."
            // Disable button and show spinner to denote loading
            $btn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin mr-2"></i>Saving changes...');

            // Mock AJAX POST
            $.ajax({
                url: '/api/update-profile', // Mock Endpoint
                type: 'POST',
                data: JSON.stringify({ firstName, lastName, phone, description }),
                contentType: 'application/json',
                beforeSend: function () {
                    console.log("Mock AJAX: Sending profile updates to Server...");
                },
                complete: function () {
                    // Simulate server network lag of 1 second
                    setTimeout(function () {
                        // Update User Record in Database (users list)
                        const usersList = JSON.parse(localStorage.getItem('users')) || [];
                        const userIndex = usersList.findIndex(u => u.email.toLowerCase() === currentUser.email.toLowerCase());

                        if (userIndex !== -1) {
                            usersList[userIndex].firstName = firstName;
                            usersList[userIndex].lastName = lastName;
                            usersList[userIndex].phone = phone;
                            usersList[userIndex].description = description;

                            localStorage.setItem('users', JSON.stringify(usersList));
                            localStorage.setItem('currentUser', JSON.stringify(usersList[userIndex]));

                            // Dynamically update upper right headers immediately
                            updateUserHeaderDetails(usersList[userIndex]);
                        }

                        $btn.prop('disabled', false).html('<i class="fas fa-save mr-2"></i>Submit');
                        $alert.addClass('alert-success').removeClass('d-none').text('Profile changes updated successfully!');
                        $form.find('input, textarea').removeClass('is-invalid is-valid');
                    }, 1000);
                }
            });
        });
    }


    // ---------------------------------------------------
    // F. ADD CONTENT SUBPAGE LOGIC (AJAX SUBMIT)
    // ---------------------------------------------------
    function bindAddContentEvents() {
        $('#contentForm').on('submit', function (e) {
            e.preventDefault();

            const $form = $(this);
            const $alert = $('#contentAlert');
            const $btn = $('#btnSubmitContent');

            $alert.addClass('d-none').removeClass('alert-success alert-danger');

            // 1. Mandatory check loop
            const isMandatoryPassed = checkMandatoryFields($form);
            if (!isMandatoryPassed) {
                $alert.addClass('alert-danger').removeClass('d-none').text('Please fill in all the mandatory fields.');
                return;
            }

            const title = $('#contentTitle').val().trim();
            const brief = $('#contentBrief').val().trim();
            const content = $('#contentBody').val().trim();

            let isValid = true;

            // Title validation [10-200]
            if (title.length < 10 || title.length > 200) {
                $('#contentTitle').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#contentTitle').addClass('is-valid').removeClass('is-invalid');
            }

            // Brief validation [30-150]
            if (brief.length < 30 || brief.length > 150) {
                $('#contentBrief').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#contentBrief').addClass('is-valid').removeClass('is-invalid');
            }

            // Content body validation [50-1000]
            if (content.length < 50 || content.length > 1000) {
                $('#contentBody').addClass('is-invalid').removeClass('is-valid');
                isValid = false;
            } else {
                $('#contentBody').addClass('is-valid').removeClass('is-invalid');
            }

            if (!isValid) {
                $alert.addClass('alert-danger').removeClass('d-none').text('Please correct validation issues below.');
                return;
            }

            // Mock AJAX POST to save contents
            $btn.prop('disabled', true).html('<i class="fas fa-spinner fa-spin mr-2"></i>Publishing...');

            $.ajax({
                url: '/api/add-content', // Mock Endpoint
                type: 'POST',
                data: JSON.stringify({ title, brief, content }),
                contentType: 'application/json',
                beforeSend: function () {
                    console.log("Mock AJAX: Transmitting content payload to Server...");
                },
                complete: function () {
                    // Simulate server network latency of 1 second
                    setTimeout(function () {
                        const contentsList = JSON.parse(localStorage.getItem('contents')) || [];

                        // Formatting current time as YYYY-MM-DD HH:MM
                        const now = new Date();
                        const year = now.getFullYear();
                        const month = String(now.getMonth() + 1).padStart(2, '0');
                        const day = String(now.getDate()).padStart(2, '0');
                        const hours = String(now.getHours()).padStart(2, '0');
                        const minutes = String(now.getMinutes()).padStart(2, '0');
                        const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;

                        const newContentObj = {
                            id: contentsList.length > 0 ? Math.max(...contentsList.map(c => c.id)) + 1 : 1,
                            title: title,
                            brief: brief,
                            content: content,
                            createdDate: formattedDate
                        };

                        contentsList.push(newContentObj);
                        localStorage.setItem('contents', JSON.stringify(contentsList));

                        $btn.prop('disabled', false).html('<i class="fas fa-check mr-2"></i>Submit');
                        $alert.addClass('alert-success').removeClass('d-none').text('Content successfully added! Loading list...');

                        // Automatically switch view back to Content List with the 5s loading effect!
                        setTimeout(function () {
                            triggerPageLoader(5, function () {
                                $('#sidebar ul li').removeClass('active');
                                $('#menuViewContents').addClass('active');
                                loadPageContent('view-content');
                            });
                        }, 1200);

                    }, 1000);
                }
            });
        });
    }


    // ---------------------------------------------------
    // G. VIEW CONTENT SUBPAGE LOGIC (RENDER DATA GRID)
    // ---------------------------------------------------
    function renderContentsTable() {
        const contentsList = JSON.parse(localStorage.getItem('contents')) || [];
        const $tbody = $('#contentsTableBody');

        $tbody.empty();
        $('#totalRecordsCount').text(`Total records: ${contentsList.length}`);

        if (contentsList.length === 0) {
            $tbody.html(`
                <tr>
                    <td colspan="4" class="text-center py-4 text-muted">
                        <i class="fas fa-folder-open fa-2x mb-2 d-block"></i>
                        No content documents published yet. Click <strong>"Form content"</strong> to publish one!
                    </td>
                </tr>
            `);
            return;
        }

        // Loop and inject contents in the table
        contentsList.forEach(function (item, index) {
            const rowHtml = `
                <tr>
                    <td><strong>${index + 1}</strong></td>
                    <td class="font-weight-bold text-dark">
                        <i class="far fa-file-alt text-success mr-2"></i>${escapeHtml(item.title)}
                    </td>
                    <td class="text-muted" style="max-width: 300px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="${escapeHtml(item.brief)}">
                        ${escapeHtml(item.brief)}
                    </td>
                    <td><span class="badge badge-light border py-2 px-3">${item.createdDate}</span></td>
                </tr>
            `;
            $tbody.append(rowHtml);
        });
    }


    // ---------------------------------------------------
    // H. AUXILIARY SYSTEM HELPER FUNCTIONS
    // ---------------------------------------------------

    // Updates dashboard dashboard numbers
    function updateDashboardStats() {
        const contentsList = JSON.parse(localStorage.getItem('contents')) || [];
        $('#statTotalContents').text(contentsList.length);
    }

    // Refresh upper navbar headers
    function updateUserHeaderDetails(userObj) {
        let displayName = userObj.username;
        if (userObj.firstName || userObj.lastName) {
            displayName = `${userObj.firstName} ${userObj.lastName}`.trim();
        }
        $('#userNameHeader').text(displayName);
    }

    // Simple HTML escaping helper for client safety
    function escapeHtml(str) {
        return str
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

});

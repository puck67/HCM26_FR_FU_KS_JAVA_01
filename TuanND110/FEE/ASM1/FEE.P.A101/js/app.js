/* app.js - Central Controller and Event Manager for CMS Dashboard (BEM Refactored) */

$(document).ready(function () {
    // ----------------------------------------------------
    // 1. AUTHENTICATION GUARD
    // ----------------------------------------------------
    let currentUser = JSON.parse(sessionStorage.getItem('currentUser'));
    if (!currentUser) {
        window.location.href = 'login.html';
        return;
    }

    // Initialize navbar details
    updateNavbarUserInfo();

    // ----------------------------------------------------
    // 2. MOCK DATABASE & DATA SEEDING
    // ----------------------------------------------------
    if (!localStorage.getItem('users')) {
        const defaultAdmin = {
            username: "AdminUser",
            email: "admin@cms.com",
            password: "admin123456",
            firstName: "System",
            lastName: "Administrator",
            phone: "0912345678",
            description: "Default Administrator Account"
        };
        localStorage.setItem('users', JSON.stringify([defaultAdmin]));
    }

    if (!localStorage.getItem('cms_contents')) {
        const seedContents = [
            {
                id: 1,
                title: "Lorem ipsum dolor sit amet",
                brief: "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                content: "HTML5 represents the major revision of the Hypertext Markup Language (HTML), the standard programming language of the World Web. This article provides an extensive look into semantic markup. Developers can use elements such as <header>, <footer>, <article>, and <section> to make document outlines clear and structured. This structure not only improves accessibility but also significantly enhances Search Engine Optimization (SEO) practices.",
                createdDate: "31/08/2016 12:03"
            },
            {
                id: 2,
                title: "Vestibulum tincidunt est vitae",
                brief: "Aliquam ornare lacus adipiscing, posuere lectus et, fringilla augue.",
                content: "CSS Grid Layout is the most powerful layout system available in CSS. It is a 2-dimensional system, meaning it can handle both columns and rows, unlike Flexbox which is largely a 1-dimensional system. We will study grid tracks, lines, template areas, gaps, and responsiveness utilizing media queries.",
                createdDate: "31/08/2016 13:13"
            },
            {
                id: 3,
                title: "Aliquam ornare lacus adipiscing",
                brief: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tincidunt est vitae ultrices accumsan.",
                content: "Bootstrap 4 is a powerful, mobile-first CSS framework designed to simplify front-end web development. It leverages Flexbox for modern alignment control. This article looks closely at container classes, row wraps, and col grids. We also survey utility variables for margins (mt-3, py-4), text colors, borders, and simple flex properties.",
                createdDate: "01/09/2016 15:09"
            },
            {
                id: 4,
                title: "Understanding AJAX and Server Requests",
                brief: "Connect front-end interfaces to remote servers using asynchronous JavaScript requests. Learn XMLHttpRequests and jQuery AJAX methods.",
                content: "Asynchronous JavaScript and XML (AJAX) allows web pages to be updated asynchronously by exchanging data with a web server behind the scenes. This means it is possible to update parts of a web page without reloading the entire page. We will implement $.ajax(), $.get(), and $.post() routines, handle headers, parse server responses, and elegantly display status indicators during network delays.",
                createdDate: "01/09/2016 16:45"
            },
            {
                id: 5,
                title: "High Performance JavaScript DOM Manipulation",
                brief: "Manipulate browser nodes efficiently. Learn to avoid expensive layouts and layout thrashing inside standard complex loops.",
                content: "The Document Object Model (DOM) is an application programming interface (API) for HTML and XML documents. It defines the logical structure of documents and the way a document is accessed and manipulated. Direct DOM operations are computationally expensive. We study document fragments, micro-tasks, local cached variable references, and events delegation to render beautiful interfaces at 60fps.",
                createdDate: "02/09/2016 09:20"
            }
        ];
        localStorage.setItem('cms_contents', JSON.stringify(seedContents));
    }

    // State management variables
    let contents = JSON.parse(localStorage.getItem('cms_contents'));
    let currentPage = 1;
    const itemsPerPage = 5;
    let searchQuery = "";

    // ----------------------------------------------------
    // 3. SPA ROUTING ENGINE WITH 5-SECOND LOADING DELAY
    // ----------------------------------------------------
    let loaderTimer = null;
    let currentNavPage = "";

    // Event Delegation for Sidebar Navigation Clicks
    $('#sidebarMenu').on('click', '.sidebar__link', function (e) {
        e.preventDefault();
        const targetPage = $(this).attr('data-page');

        if ($(this).parent().hasClass('sidebar__item--active') && $('#contentContainer').children().length > 0) {
            return;
        }

        navigateToPage(targetPage);
    });

    // Sidebar search filter trigger
    $('#sidebarSearch').on('keyup', function () {
        searchQuery = $(this).val().toLowerCase().trim();

        if (currentNavPage === 'view_content') {
            currentPage = 1;
            renderContentTable();
        }
    });

    // Edit Profile option inside dropdown
    $('#dropdownEditProfile').on('click', function (e) {
        e.preventDefault();
        navigateToPage('edit_profile');
    });

    // BEM Dropdown toggle helper
    $('#profileDropdown').on('click', function (e) {
        e.stopPropagation();
        $('#profileDropdownMenu').toggleClass('dropdown__menu--show');
    });

    $(document).on('click', function () {
        $('#profileDropdownMenu').removeClass('dropdown__menu--show');
    });

    // Central Navigator function with 5s Delay
    function navigateToPage(pageName) {
        if (loaderTimer) {
            clearInterval(loaderTimer);
        }

        currentNavPage = pageName;

        // Toggle BEM sidebar active class
        $('.sidebar__item').removeClass('sidebar__item--active');
        $(`.sidebar__link[data-page="${pageName}"]`).parent().addClass('sidebar__item--active');

        let displayTitle = "";
        let loadMsg = "";
        if (pageName === 'view_content') {
            displayTitle = "View Content";
            loadMsg = "Loading View Content screen...";
        } else if (pageName === 'add_content') {
            displayTitle = "Form Content";
            loadMsg = "Creating dynamic input fields and rendering form...";
        } else if (pageName === 'edit_profile') {
            displayTitle = "Edit Profile";
            loadMsg = "Loading active user profile session details...";
        }

        // Show the loader overlay via BEM state class toggle
        $('#loaderMessage').text(loadMsg);
        $('#loaderCountdown').text("5");
        $('#globalLoader').addClass('loading-screen--show').hide().fadeIn(300);

        let countdown = 5;
        loaderTimer = setInterval(function () {
            countdown--;
            $('#loaderCountdown').text(countdown);

            if (countdown <= 0) {
                clearInterval(loaderTimer);
                $('#globalLoader').fadeOut(300, function () {
                    $(this).removeClass('loading-screen--show');
                    loadPageContent(pageName);
                });
            }
        }, 1000);
    }

    // Load page html fragments
    function loadPageContent(pageName) {
        const url = `./${pageName}.html`;

        $.get({
            url: url,
            cache: false,
            success: function (htmlContent) {
                $('#contentContainer').html(htmlContent);

                if (pageName === 'view_content') {
                    initViewContent();
                } else if (pageName === 'add_content') {
                    initAddContent();
                } else if (pageName === 'edit_profile') {
                    initEditProfile();
                }
            },
            error: function (xhr, status, error) {
                const errorHTML = `
                    <div class="alert alert--danger mt-4">
                        <span>
                            <i class="fas fa-exclamation-triangle mr-2" aria-hidden="true"></i>
                            Failed to load view <b>${pageName}.html</b>. Error details: ${error || status}
                        </span>
                    </div>
                `;
                $('#contentContainer').html(errorHTML);
            }
        });
    }

    // ----------------------------------------------------
    // 4. VIEW CONTENT PAGE LOGIC
    // ----------------------------------------------------
    function initViewContent() {
        currentPage = 1;
        renderContentTable();

        // Pagination button clicks
        $('#contentPagination').off('click', '.pagination__btn').on('click', '.pagination__btn', function (e) {
            e.preventDefault();
            const targetPage = $(this).attr('data-page');

            if ($(this).hasClass('pagination__btn--disabled')) {
                return;
            }

            if (targetPage === 'prev') {
                currentPage = Math.max(currentPage - 1, 1);
            } else if (targetPage === 'next') {
                currentPage += 1;
            } else {
                const pageNum = parseInt(targetPage);
                if (!Number.isNaN(pageNum)) {
                    currentPage = pageNum;
                }
            }

            renderContentTable();
        });

        // Delegate Delete Button Clicks (Trash icon in BEM table)
        $('#contentTableBody').off('click', '.btn-delete').on('click', '.btn-delete', function (e) {
            e.preventDefault();
            e.stopPropagation();
            const idToDelete = parseInt($(this).attr('data-id'));

            if (confirm("Are you sure you want to delete this content item? This action is permanent.")) {
                contents = contents.filter(item => item.id !== idToDelete);
                localStorage.setItem('cms_contents', JSON.stringify(contents));

                // Show success BEM alert
                const alertHTML = `
                    <div class="alert alert--success">
                        <span>
                            <i class="fas fa-check-circle mr-2" aria-hidden="true"></i>
                            Content item ID <b>#${idToDelete}</b> was deleted successfully.
                        </span>
                        <button type="button" class="alert__close-btn" aria-label="Close alert" onclick="$(this).parent().remove()">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                `;
                $('#viewAlerts').html(alertHTML);

                renderContentTable();
            }
        });

        // Delegate Edit Link Clicks (Clicking Title link)
        $('#contentTableBody').off('click', '.btn-edit').on('click', '.btn-edit', function (e) {
            e.preventDefault();
            const idToEdit = parseInt($(this).attr('data-id'));
            const editItem = contents.find(item => item.id === idToEdit);

            if (editItem) {
                $('#loaderMessage').text(`Fetching Content item #${idToEdit} for editing...`);
                $('#loaderCountdown').text("5");
                $('#globalLoader').addClass('loading-screen--show').hide().fadeIn(300);

                let countdown = 5;
                loaderTimer = setInterval(function () {
                    countdown--;
                    $('#loaderCountdown').text(countdown);

                    if (countdown <= 0) {
                        clearInterval(loaderTimer);
                        $('#globalLoader').fadeOut(300, function () {
                            $(this).removeClass('loading-screen--show');
                            $.get('./add_content.html', function (htmlContent) {
                                $('#contentContainer').html(htmlContent);
                                $('#formContentTitle').text(`Edit Content #${idToEdit}`);

                                // Populate content form values
                                $('#contentId').val(editItem.id);
                                $('#title').val(editItem.title);
                                $('#brief').val(editItem.brief);
                                $('#content').val(editItem.content);

                                updateFormCharCounts();
                                initAddContent();
                            });
                        });
                    }
                }, 1000);
            }
        });
    }

    // Dynamic BEM table rendering
    function renderContentTable() {
        const filtered = contents.filter(item => {
            return item.title.toLowerCase().includes(searchQuery) ||
                item.brief.toLowerCase().includes(searchQuery);
        });

        const totalItems = filtered.length;
        const totalPages = Math.ceil(totalItems / itemsPerPage);

        if (totalItems === 0) {
            $('#contentTableBody').empty();
            $('#noDataPlaceholder').removeClass('d-none');
            $('#contentPagination').empty();
            return;
        } else {
            $('#noDataPlaceholder').addClass('d-none');
        }

        if (currentPage > totalPages) {
            currentPage = totalPages;
        }

        const startIndex = (currentPage - 1) * itemsPerPage;
        const pageItems = filtered.slice(startIndex, startIndex + itemsPerPage);

        let tableRowsHTML = "";
        pageItems.forEach((item, index) => {
            let displayBrief = item.brief;
            if (displayBrief.length > 250) {
                displayBrief = displayBrief.substring(0, 247) + "...";
            }

            tableRowsHTML += `
                <tr class="table__row animated-fade-in">
                    <td class="table__cell table__cell--center table__cell--bold table__cell--muted table__cell--narrow">${startIndex + index + 1}</td>
                    <td class="table__cell">
                        <a href="#" class="table__link btn-edit" data-id="${item.id}">
                            ${escapeHTML(item.title)}
                        </a>
                    </td>
                    <td class="table__cell table__cell--muted table__cell--small-text">${escapeHTML(displayBrief)}</td>
                    <td class="table__cell">
                        <div class="table__action">
                            <span class="table__cell--muted table__action-meta">${item.createdDate}</span>
                            <button class="table__action-btn table__action-btn--delete btn-delete" data-id="${item.id}" title="Delete Content">
                                <i class="fas fa-trash-alt" aria-hidden="true"></i>
                            </button>
                        </div>
                    </td>
                </tr>
            `;
        });
        $('#contentTableBody').html(tableRowsHTML);
        renderPagination(totalPages);
    }

    function renderPagination(totalPages) {
        const $pagination = $('#contentPagination');
        if (totalPages <= 1) {
            $pagination.empty();
            return;
        }

        const isPrevDisabled = currentPage === 1;
        const isNextDisabled = currentPage === totalPages;
        let paginationHTML = `
            <button class="pagination__btn ${isPrevDisabled ? 'pagination__btn--disabled' : ''}" data-page="prev" ${isPrevDisabled ? 'disabled' : ''}>Prev</button>
        `;

        for (let page = 1; page <= totalPages; page++) {
            paginationHTML += `
                <button class="pagination__btn ${page === currentPage ? 'pagination__btn--active' : ''}" data-page="${page}">${page}</button>
            `;
        }

        paginationHTML += `
            <button class="pagination__btn ${isNextDisabled ? 'pagination__btn--disabled' : ''}" data-page="next" ${isNextDisabled ? 'disabled' : ''}>Next</button>
        `;

        $pagination.html(paginationHTML);
    }

    // ----------------------------------------------------
    // 5. FORM CONTENT (ADD/EDIT) PAGE LOGIC
    // ----------------------------------------------------
    function initAddContent() {
        updateFormCharCounts();

        // Char count live bindings
        $('#title').off('input').on('input', function () {
            $('#titleCharCount').text(`${$(this).val().length} / 50`);
        });

        $('#brief').off('input').on('input', function () {
            $('#briefCharCount').text(`${$(this).val().length} / 1500`);
        });

        $('#content').off('input').on('input', function () {
            $('#contentCharCount').text(`${$(this).val().length} / 5000`);
        });

        // --- Real-time Validations ---
        $('#title').off('input blur').on('input blur', function() {
            validateTitle();
        });

        $('#brief').off('input blur').on('input blur', function() {
            validateBrief();
        });

        $('#content').off('input blur').on('input blur', function() {
            validateContent();
        });

        function validateTitle() {
            const titleVal = $('#title').val().trim();
            const $el = $('#title');
            if (!titleVal) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#titleFeedback').text('Title is mandatory.');
                return false;
            } else if (titleVal.length < 10 || titleVal.length > 50) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#titleFeedback').text('Title must be between 10 and 50 characters.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        function validateBrief() {
            const briefVal = $('#brief').val().trim();
            const $el = $('#brief');
            if (!briefVal) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#briefFeedback').text('Brief is mandatory.');
                return false;
            } else if (briefVal.length < 30 || briefVal.length > 1500) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#briefFeedback').text('Brief must be between 30 and 1500 characters.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        function validateContent() {
            const contentVal = $('#content').val().trim();
            const $el = $('#content');
            if (!contentVal) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#contentFeedback').text('Content is mandatory.');
                return false;
            } else if (contentVal.length < 50 || contentVal.length > 5000) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#contentFeedback').text('Content must be between 50 and 5000 characters.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        // Reset Button handler
        $('#resetContentBtn').off('click').on('click', function (e) {
            e.preventDefault();
            $('#contentForm')[0].reset();
            $('.form__control').removeClass('form__control--invalid form__control--valid');
            updateFormCharCounts();
            $('#formAlerts').empty();
        });

        // Submit Form Handler
        $('#contentForm').off('submit').on('submit', function (e) {
            e.preventDefault();

            $('#formAlerts').empty();

            const isTitleValid = validateTitle();
            const isBriefValid = validateBrief();
            const isContentValid = validateContent();

            if (isTitleValid && isBriefValid && isContentValid) {
                const editIdStr = $('#contentId').val();
                const titleVal = $('#title').val().trim();
                const briefVal = $('#brief').val().trim();
                const contentVal = $('#content').val().trim();
                const dateNow = getFormattedDateTime();

                if (editIdStr) {
                    // EDIT EXISTING MODE
                    const editId = parseInt(editIdStr);
                    const itemIndex = contents.findIndex(item => item.id === editId);

                    if (itemIndex > -1) {
                        contents[itemIndex].title = titleVal;
                        contents[itemIndex].brief = briefVal;
                        contents[itemIndex].content = contentVal;

                        localStorage.setItem('cms_contents', JSON.stringify(contents));
                        showFormAlert('success', `Content item <b>#${editId}</b> was updated successfully via Ajax!`);

                        $('html, body').animate({ scrollTop: 0 }, 300);

                        // Disable button briefly (throttle)
                        $('#submitContentBtn').prop('disabled', true);
                        setTimeout(function () {
                            $('#submitContentBtn').prop('disabled', false);
                            navigateToPage('view_content');
                        }, 1500);
                    }
                } else {
                    // ADD NEW CONTENT MODE
                    const newId = contents.length > 0 ? Math.max(...contents.map(item => item.id)) + 1 : 1;
                    const newContentItem = {
                        id: newId,
                        title: titleVal,
                        brief: briefVal,
                        content: contentVal,
                        createdDate: dateNow
                    };

                    contents.unshift(newContentItem);
                    localStorage.setItem('cms_contents', JSON.stringify(contents));

                    showFormAlert('success', `New content item <b>#${newId}</b> was created successfully!`);

                    $('#contentForm')[0].reset();
                    $('.form__control').removeClass('form__control--valid form__control--invalid');
                    updateFormCharCounts();

                    $('html, body').animate({ scrollTop: 0 }, 300);

                    $('#submitContentBtn').prop('disabled', true);
                    setTimeout(function () {
                        $('#submitContentBtn').prop('disabled', false);
                        navigateToPage('view_content');
                    }, 1500);
                }
            }
        });
    }

    function updateFormCharCounts() {
        $('#titleCharCount').text(`${$('#title').val() ? $('#title').val().length : 0} / 50`);
        $('#briefCharCount').text(`${$('#brief').val() ? $('#brief').val().length : 0} / 1500`);
        $('#contentCharCount').text(`${$('#content').val() ? $('#content').val().length : 0} / 5000`);
    }

    function showFormAlert(type, msg) {
        const alertHTML = `
            <div class="alert alert--${type}">
                <span>
                    <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'} mr-2" aria-hidden="true"></i>
                    ${msg}
                </span>
                <button type="button" class="alert__close-btn" aria-label="Close alert" onclick="$(this).parent().remove()">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `;
        $('#formAlerts').html(alertHTML);
    }

    // ----------------------------------------------------
    // 6. EDIT PROFILE PAGE LOGIC
    // ----------------------------------------------------
    function initEditProfile() {
        // Load active user values into BEM inputs
        $('#firstName').val(currentUser.firstName || "");
        $('#lastName').val(currentUser.lastName || "");
        $('#profileEmail').text(currentUser.email || "");
        $('#phone').val(currentUser.phone || "");
        $('#description').val(currentUser.description || "");

        // Counter text bio
        $('#descCharCount').text(`${$('#description').val().length} / 200`);
        $('#description').off('input').on('input', function () {
            $('#descCharCount').text(`${$(this).val().length} / 200`);
        });

        // --- Real-time input validations ---
        $('#firstName').off('input blur').on('input blur', function() {
            validateFirstName();
        });

        $('#lastName').off('input blur').on('input blur', function() {
            validateLastName();
        });

        $('#phone').off('input blur').on('input blur', function() {
            validatePhone();
        });

        function validateFirstName() {
            const fName = $('#firstName').val().trim();
            const $el = $('#firstName');
            if (!fName) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#firstNameFeedback').text('First name is mandatory.');
                return false;
            } else if (fName.length < 3 || fName.length > 30) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#firstNameFeedback').text('First name must be between 3 and 30 characters.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        function validateLastName() {
            const lName = $('#lastName').val().trim();
            const $el = $('#lastName');
            if (!lName) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#lastNameFeedback').text('Last name is mandatory.');
                return false;
            } else if (lName.length < 3 || lName.length > 30) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#lastNameFeedback').text('Last name must be between 3 and 30 characters.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        function validatePhone() {
            const phoneVal = $('#phone').val().trim();
            const $el = $('#phone');
            const isNumeric = /^\d+$/;
            if (!phoneVal) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#phoneFeedback').text('Phone number is mandatory.');
                return false;
            } else if (!isNumeric.test(phoneVal)) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#phoneFeedback').text('Phone number must contain digits only.');
                return false;
            } else if (phoneVal.length < 9 || phoneVal.length > 15) {
                $el.addClass('form__control--invalid').removeClass('form__control--valid');
                $('#phoneFeedback').text('Phone number must be between 9 and 15 digits in length.');
                return false;
            } else {
                $el.removeClass('form__control--invalid').addClass('form__control--valid');
                return true;
            }
        }

        // Reset Profile form
        $('#resetProfileBtn').off('click').on('click', function (e) {
            e.preventDefault();
            $('#firstName').val(currentUser.firstName || "");
            $('#lastName').val(currentUser.lastName || "");
            $('#phone').val(currentUser.phone || "");
            $('#description').val(currentUser.description || "");
            $('#descCharCount').text(`${$('#description').val().length} / 200`);
            $('.form__control').removeClass('form__control--invalid form__control--valid');
            $('#profileAlerts').empty();
        });

        // Profile submission
        $('#profileForm').off('submit').on('submit', function (e) {
            e.preventDefault();

            $('#profileAlerts').empty();

            const isFnameValid = validateFirstName();
            const isLnameValid = validateLastName();
            const isPhoneValid = validatePhone();

            if (isFnameValid && isLnameValid && isPhoneValid) {
                const fName = $('#firstName').val().trim();
                const lName = $('#lastName').val().trim();
                const phoneVal = $('#phone').val().trim();
                const descVal = $('#description').val().trim();

                // Submit via Simulated AJAX
                $('#submitProfileBtn').prop('disabled', true).html('<i class="fas fa-spinner fa-spin mr-2"></i> Saving...');

                $.ajax({
                    url: 'https://httpbin.org/post',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        email: currentUser.email,
                        firstName: fName,
                        lastName: lName,
                        phone: phoneVal,
                        description: descVal
                    }),
                    success: function (response) {
                        currentUser.firstName = fName;
                        currentUser.lastName = lName;
                        currentUser.phone = phoneVal;
                        currentUser.description = descVal;

                        sessionStorage.setItem('currentUser', JSON.stringify(currentUser));

                        const users = JSON.parse(localStorage.getItem('users') || '[]');
                        const userIndex = users.findIndex(u => u.email.toLowerCase() === currentUser.email.toLowerCase());
                        if (userIndex > -1) {
                            users[userIndex].firstName = fName;
                            users[userIndex].lastName = lName;
                            users[userIndex].phone = phoneVal;
                            users[userIndex].description = descVal;
                            localStorage.setItem('users', JSON.stringify(users));
                        }

                        $('#submitProfileBtn').prop('disabled', false).text('Submit Button');

                        const alertHTML = `
                            <div class="alert alert--success">
                                <span>
                                    <i class="fas fa-check-circle mr-2" aria-hidden="true"></i>
                                    <b>AJAX Transaction Successful!</b> Profile updated successfully.
                                </span>
                                <button type="button" class="alert__close-btn" aria-label="Close alert" onclick="$(this).parent().remove()">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        `;
                        $('#profileAlerts').html(alertHTML);

                        updateNavbarUserInfo();
                        $('html, body').animate({ scrollTop: 0 }, 300);
                    },
                    error: function (xhr, status, error) {
                        // Offline fallback
                        currentUser.firstName = fName;
                        currentUser.lastName = lName;
                        currentUser.phone = phoneVal;
                        currentUser.description = descVal;
                        sessionStorage.setItem('currentUser', JSON.stringify(currentUser));

                        const users = JSON.parse(localStorage.getItem('users') || '[]');
                        const userIndex = users.findIndex(u => u.email.toLowerCase() === currentUser.email.toLowerCase());
                        if (userIndex > -1) {
                            users[userIndex].firstName = fName;
                            users[userIndex].lastName = lName;
                            users[userIndex].phone = phoneVal;
                            users[userIndex].description = descVal;
                            localStorage.setItem('users', JSON.stringify(users));
                        }

                        $('#submitProfileBtn').prop('disabled', false).text('Submit Button');

                        const alertHTML = `
                            <div class="alert alert--success">
                                <span>
                                    <i class="fas fa-check-circle mr-2" aria-hidden="true"></i>
                                    Profile updated successfully offline in localStorage.
                                </span>
                                <button type="button" class="alert__close-btn" aria-label="Close alert" onclick="$(this).parent().remove()">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                        `;
                        $('#profileAlerts').html(alertHTML);
                        updateNavbarUserInfo();
                        $('html, body').animate({ scrollTop: 0 }, 300);
                    }
                });
            }
        });
    }

    // ----------------------------------------------------
    // 7. UTILITY FUNCTIONS
    // ----------------------------------------------------
    function updateNavbarUserInfo() {
        let fullName = "";
        if (currentUser.firstName || currentUser.lastName) {
            fullName = `${currentUser.firstName} ${currentUser.lastName}`.trim();
        } else {
            fullName = currentUser.username;
        }
        $('#navbarUsername').text(fullName);
    }

    function escapeHTML(str) {
        return str
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function getFormattedDateTime() {
        const d = new Date();
        const year = d.getFullYear();
        const month = String(d.getMonth() + 1).padStart(2, '0');
        const day = String(d.getDate()).padStart(2, '0');
        const hour = String(d.getHours()).padStart(2, '0');
        const minute = String(d.getMinutes()).padStart(2, '0');
        return `${day}/${month}/${year} ${hour}:${minute}`;
    }

    // ----------------------------------------------------
    // 8. LOGOUT HANDLER
    // ----------------------------------------------------
    $('#logoutBtn').on('click', function (e) {
        e.preventDefault();
        if (confirm("Are you sure you want to sign out?")) {
            sessionStorage.removeItem('currentUser');
            window.location.href = 'login.html';
        }
    });

    // ----------------------------------------------------
    // 9. DEFAULT SCREEN INITIALIZER ON LOAD
    // ----------------------------------------------------
    navigateToPage('view_content');
});

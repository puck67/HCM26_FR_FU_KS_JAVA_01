$(document).ready(function () {
    const isLoginPage = window.location.pathname.includes('login.html');
    const isRegisterPage = window.location.pathname.includes('register.html');
    const isIndexPage = !isLoginPage && !isRegisterPage;

    function getUsers() {
        const users = localStorage.getItem('cms_users');
        return users ? JSON.parse(users) : [];
    }

    function saveUsers(users) {
        localStorage.setItem('cms_users', JSON.stringify(users));
    }

    function getCurrentUser() {
        const user = localStorage.getItem('cms_current_user');
        return user ? JSON.parse(user) : null;
    }

    function setCurrentUser(user) {
        localStorage.setItem('cms_current_user', JSON.stringify(user));
    }

    function getContents() {
        const contents = localStorage.getItem('cms_contents');
        if (!contents) {
            const defaults = [
                {
                    id: 1,
                    title: "Lorem ipsum dolor sit amet",
                    brief: "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    createdDate: "31/08/2016 12:03"
                },
                {
                    id: 2,
                    title: "Vestibulum tincidunt est vitae",
                    brief: "Aliquam ornare lacus adipiscing, posuere lectus et, fringilla augue.",
                    createdDate: "31/08/2016 13:13"
                },
                {
                    id: 3,
                    title: "Aliquam ornare lacus adipiscing",
                    brief: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tincidunt est vitae ultrices accumsan.",
                    createdDate: "01/09/2016 15:09"
                }
            ];
            localStorage.setItem('cms_contents', JSON.stringify(defaults));
            return defaults;
        }
        return JSON.parse(contents);
    }

    function saveContents(contents) {
        localStorage.setItem('cms_contents', JSON.stringify(contents));
    }

    function highlightError(inputEl, message) {
        inputEl.addClass('is-invalid');
        const formGroup = inputEl.closest('.form-group');
        formGroup.find('label').addClass('invalid-label');
        formGroup.find('.invalid-feedback').remove();
        inputEl.after(`<div class="invalid-feedback">${message}</div>`);
    }

    function resetError(inputEl) {
        inputEl.removeClass('is-invalid');
        const formGroup = inputEl.closest('.form-group');
        formGroup.find('label').removeClass('invalid-label');
        formGroup.find('.invalid-feedback').remove();
    }

    function isValidEmail(email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    }

    if (isLoginPage) {
        if (getCurrentUser()) {
            window.location.href = 'index.html';
            return;
        }

        $('#loginForm').submit(function (e) {
            e.preventDefault();
            let isValid = true;

            const emailInput = $('#loginEmail');
            const passwordInput = $('#loginPassword');

            resetError(emailInput);
            resetError(passwordInput);

            const emailVal = emailInput.val().trim();
            const passwordVal = passwordInput.val();

            if (emailVal === "") {
                highlightError(emailInput, "Email is required.");
                isValid = false;
            } else if (emailVal.length < 5 || emailVal.length > 50) {
                highlightError(emailInput, "Email must be between 5 and 50 characters.");
                isValid = false;
            }

            if (passwordVal === "") {
                highlightError(passwordInput, "Password is required.");
                isValid = false;
            } else if (passwordVal.length < 8 || passwordVal.length > 30) {
                highlightError(passwordInput, "Password must be between 8 and 30 characters.");
                isValid = false;
            }

            if (!isValid) return;

            const users = getUsers();
            const foundUser = users.find(u => u.email.toLowerCase() === emailVal.toLowerCase() && u.password === passwordVal);

            if (foundUser) {
                setCurrentUser(foundUser);
                window.location.href = 'index.html';
            } else {
                $('#loginAlertContainer').html(`
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        Invalid Email or Password.
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                `);
            }
        });
    }

    if (isRegisterPage) {
        if (getCurrentUser()) {
            window.location.href = 'index.html';
            return;
        }

        $('#registerForm').submit(function (e) {
            e.preventDefault();
            let isValid = true;

            const usernameInput = $('#registerUsername');
            const emailInput = $('#registerEmail');
            const passwordInput = $('#registerPassword');
            const rePasswordInput = $('#registerRePassword');

            resetError(usernameInput);
            resetError(emailInput);
            resetError(passwordInput);
            resetError(rePasswordInput);

            const usernameVal = usernameInput.val().trim();
            const emailVal = emailInput.val().trim();
            const passwordVal = passwordInput.val();
            const rePasswordVal = rePasswordInput.val();

            if (usernameVal === "") {
                highlightError(usernameInput, "User name is required.");
                isValid = false;
            } else if (usernameVal.length < 3 || usernameVal.length > 30) {
                highlightError(usernameInput, "User name must be between 3 and 30 characters.");
                isValid = false;
            }

            if (emailVal === "") {
                highlightError(emailInput, "Email is required.");
                isValid = false;
            } else if (emailVal.length < 5) {
                highlightError(emailInput, "Email must be at least 5 characters.");
                isValid = false;
            } else if (!isValidEmail(emailVal)) {
                highlightError(emailInput, "Please enter a valid email address.");
                isValid = false;
            }

            if (passwordVal === "") {
                highlightError(passwordInput, "Password is required.");
                isValid = false;
            } else if (passwordVal.length < 8 || passwordVal.length > 30) {
                highlightError(passwordInput, "Password must be between 8 and 30 characters.");
                isValid = false;
            }

            if (rePasswordVal === "") {
                highlightError(rePasswordInput, "Confirm Password is required.");
                isValid = false;
            } else if (rePasswordVal.length < 8 || rePasswordVal.length > 30) {
                highlightError(rePasswordInput, "Confirm Password must be between 8 and 30 characters.");
                isValid = false;
            } else if (passwordVal !== rePasswordVal) {
                highlightError(rePasswordInput, "Passwords do not match.");
                isValid = false;
            }

            if (!isValid) return;

            const users = getUsers();
            const emailExists = users.some(u => u.email.toLowerCase() === emailVal.toLowerCase());

            if (emailExists) {
                $('#registerAlertContainer').html(`
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        Email already registered.
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                `);
                return;
            }

            const newUser = {
                username: usernameVal,
                email: emailVal,
                password: passwordVal,
                firstName: "",
                lastName: "",
                phone: "",
                description: ""
            };

            users.push(newUser);
            saveUsers(users);

            $('#registerAlertContainer').html(`
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Registration successful! Redirecting to login...
                </div>
            `);

            setTimeout(function () {
                window.location.href = 'login.html';
            }, 1500);
        });
    }

    if (isIndexPage) {
        const currentUser = getCurrentUser();
        if (!currentUser) {
            window.location.href = 'login.html';
            return;
        }

        $('#navUsername').text(currentUser.username);

        function loadSection(fragmentUrl, callback) {
            $('#mainContentArea').empty();
            $('#loadingOverlay').css('display', 'flex');

            setTimeout(function () {
                $.get(fragmentUrl, function (htmlContent) {
                    $('#loadingOverlay').css('display', 'none');
                    $('#mainContentArea').html(htmlContent);
                    if (callback) callback();
                }).fail(function () {
                    $('#loadingOverlay').css('display', 'none');
                    $('#mainContentArea').html(`
                        <div class="alert alert-danger mt-3">
                            Failed to load requested content.
                        </div>
                    `);
                });
            }, 5000);
        }

        function initViewContents() {
            $('.sidebar .nav-link').removeClass('active');
            $('#menuViewContents').addClass('active');

            loadSection('view_content.html', function () {
                const contents = getContents();
                const tbody = $('#viewContentTableBody');
                tbody.empty();

                if (contents.length === 0) {
                    tbody.append(`
                        <tr>
                            <td colspan="4" class="text-center text-muted">No content available.</td>
                        </tr>
                    `);
                    return;
                }

                contents.forEach(function (item, idx) {
                    tbody.append(`
                        <tr>
                            <td>${idx + 1}</td>
                            <td class="font-weight-bold text-success">${escapeHtml(item.title)}</td>
                            <td>${escapeHtml(item.brief)}</td>
                            <td>${item.createdDate}</td>
                        </tr>
                    `);
                });
            });
        }

        function initFormContent() {
            $('.sidebar .nav-link').removeClass('active');
            $('#menuFormContent').addClass('active');

            loadSection('add_content.html', function () {
                $('#contentForm').submit(function (e) {
                    e.preventDefault();
                    let isValid = true;

                    const titleInput = $('#contentTitle');
                    const briefInput = $('#contentBrief');
                    const mainInput = $('#contentMain');

                    resetError(titleInput);
                    resetError(briefInput);
                    resetError(mainInput);

                    const titleVal = titleInput.val().trim();
                    const briefVal = briefInput.val().trim();
                    const mainVal = mainInput.val().trim();

                    if (titleVal === "") {
                        highlightError(titleInput, "Title is required.");
                        isValid = false;
                    } else if (titleVal.length < 10 || titleVal.length > 200) {
                        highlightError(titleInput, "Title must be between 10 and 200 characters.");
                        isValid = false;
                    }

                    if (briefVal === "") {
                        highlightError(briefInput, "Brief is required.");
                        isValid = false;
                    } else if (briefVal.length < 30 || briefVal.length > 150) {
                        highlightError(briefInput, "Brief must be between 30 and 150 characters.");
                        isValid = false;
                    }

                    if (mainVal === "") {
                        highlightError(mainInput, "Content is required.");
                        isValid = false;
                    } else if (mainVal.length < 50 || mainVal.length > 1000) {
                        highlightError(mainInput, "Content must be between 50 and 1000 characters.");
                        isValid = false;
                    }

                    if (!isValid) return;

                    const now = new Date();
                    const day = String(now.getDate()).padStart(2, '0');
                    const month = String(now.getMonth() + 1).padStart(2, '0');
                    const year = now.getFullYear();
                    const hours = String(now.getHours()).padStart(2, '0');
                    const minutes = String(now.getMinutes()).padStart(2, '0');
                    const createdDate = `${day}/${month}/${year} ${hours}:${minutes}`;

                    const contents = getContents();
                    const newContent = {
                        id: contents.length ? Math.max(...contents.map(c => c.id)) + 1 : 1,
                        title: titleVal,
                        brief: briefVal,
                        content: mainVal,
                        createdDate: createdDate
                    };

                    contents.push(newContent);
                    saveContents(contents);

                    $('#contentAlertContainer').html(`
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            Content saved successfully!
                            <button type="button" class="close" data-dismiss="alert">
                                <span>&times;</span>
                            </button>
                        </div>
                    `);

                    $('#contentForm')[0].reset();
                });
            });
        }

        function initEditProfile() {
            $('.sidebar .nav-link').removeClass('active');

            loadSection('edit_profile.html', function () {
                const user = getCurrentUser();
                if (user) {
                    $('#firstName').val(user.firstName || '');
                    $('#lastName').val(user.lastName || '');
                    $('#profileEmail').val(user.email || '');
                    $('#phone').val(user.phone || '');
                    $('#description').val(user.description || '');
                }

                $('#profileForm').submit(function (e) {
                    e.preventDefault();
                    let isValid = true;

                    const firstNameInput = $('#firstName');
                    const lastNameInput = $('#lastName');
                    const phoneInput = $('#phone');
                    const descriptionInput = $('#description');

                    resetError(firstNameInput);
                    resetError(lastNameInput);
                    resetError(phoneInput);
                    resetError(descriptionInput);

                    const firstNameVal = firstNameInput.val().trim();
                    const lastNameVal = lastNameInput.val().trim();
                    const phoneVal = phoneInput.val().trim();
                    const descriptionVal = descriptionInput.val().trim();

                    if (firstNameVal === "") {
                        highlightError(firstNameInput, "First Name is required.");
                        isValid = false;
                    } else if (firstNameVal.length < 3 || firstNameVal.length > 30) {
                        highlightError(firstNameInput, "First Name must be between 3 and 30 characters.");
                        isValid = false;
                    }

                    if (lastNameVal === "") {
                        highlightError(lastNameInput, "Last Name is required.");
                        isValid = false;
                    } else if (lastNameVal.length < 3 || lastNameVal.length > 30) {
                        highlightError(lastNameInput, "Last Name must be between 3 and 30 characters.");
                        isValid = false;
                    }

                    if (phoneVal === "") {
                        highlightError(phoneInput, "Phone number is required.");
                        isValid = false;
                    } else if (phoneVal.length < 9 || phoneVal.length > 13) {
                        highlightError(phoneInput, "Phone number must be between 9 and 13 digits.");
                        isValid = false;
                    } else if (!/^\d+$/.test(phoneVal)) {
                        highlightError(phoneInput, "Phone number must contain digits only.");
                        isValid = false;
                    }

                    if (descriptionVal.length > 200) {
                        highlightError(descriptionInput, "Description cannot exceed 200 characters.");
                        isValid = false;
                    }

                    if (!isValid) return;

                    const submitButton = $('#btnSubmitProfile');
                    const originalText = submitButton.text();
                    submitButton.prop('disabled', true).text('Updating...');

                    $.ajax({
                        url: 'https://jsonplaceholder.typicode.com/posts/1',
                        type: 'PUT',
                        data: {
                            firstName: firstNameVal,
                            lastName: lastNameVal,
                            phone: phoneVal,
                            description: descriptionVal
                        },
                        success: function () {
                            submitButton.prop('disabled', false).text(originalText);

                            const updatedUser = getCurrentUser();
                            if (updatedUser) {
                                updatedUser.firstName = firstNameVal;
                                updatedUser.lastName = lastNameVal;
                                updatedUser.phone = phoneVal;
                                updatedUser.description = descriptionVal;

                                setCurrentUser(updatedUser);

                                const users = getUsers();
                                const userIndex = users.findIndex(u => u.email.toLowerCase() === updatedUser.email.toLowerCase());
                                if (userIndex !== -1) {
                                    users[userIndex] = updatedUser;
                                    saveUsers(users);
                                }
                            }

                            $('#profileAlertContainer').html(`
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    Profile updated successfully!
                                    <button type="button" class="close" data-dismiss="alert">
                                        <span>&times;</span>
                                    </button>
                                </div>
                            `);
                        },
                        error: function () {
                            submitButton.prop('disabled', false).text(originalText);
                            $('#profileAlertContainer').html(`
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    Failed to update profile. Please try again.
                                    <button type="button" class="close" data-dismiss="alert">
                                        <span>&times;</span>
                                    </button>
                                </div>
                            `);
                        }
                    });
                });
            });
        }

        $('#menuViewContents').click(function (e) {
            e.preventDefault();
            initViewContents();
        });

        $('#menuFormContent').click(function (e) {
            e.preventDefault();
            initFormContent();
        });

        $('#btnProfile').click(function (e) {
            e.preventDefault();
            initEditProfile();
        });

        $('#btnLogout').click(function (e) {
            e.preventDefault();
            localStorage.removeItem('cms_current_user');
            window.location.href = 'login.html';
        });

        $('#searchForm').submit(function (e) {
            e.preventDefault();
            const query = $('#searchInput').val().trim().toLowerCase();
            const tbody = $('#viewContentTableBody');

            if (tbody.length === 0) {
                initViewContents();
                return;
            }

            const contents = getContents();
            tbody.empty();

            const filtered = contents.filter(item =>
                item.title.toLowerCase().includes(query) ||
                item.brief.toLowerCase().includes(query)
            );

            if (filtered.length === 0) {
                tbody.append(`
                    <tr>
                        <td colspan="4" class="text-center text-muted">No matching records found.</td>
                    </tr>
                `);
                return;
            }

            filtered.forEach(function (item, idx) {
                tbody.append(`
                    <tr>
                        <td>${idx + 1}</td>
                        <td class="font-weight-bold text-success">${escapeHtml(item.title)}</td>
                        <td>${escapeHtml(item.brief)}</td>
                        <td>${item.createdDate}</td>
                    </tr>
                `);
            });
        });

        initViewContents();
    }

    function escapeHtml(string) {
        return String(string).replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }
});

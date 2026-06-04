$(document).ready(function() {
    // 1. Session Protection
    const currentUser = JSON.parse(localStorage.getItem('currentCmsUser'));
    if (!currentUser) {
        window.location.href = 'login.html';
        return;
    }

    // Set navbar username
    updateNavbarName();

    // Toggle sidebar
    $('#sidebarCollapse, #sidebarCollapseBtn').on('click', function() {
        $('#sidebar').toggleClass('active');
    });

    // 2. Routing Logic
    let currentPage = 'view_contents';
    let loadingTimerInterval = null;

    // Load default page
    loadPage('view_contents');

    // Sidebar navigation clicks
    $('#sidebar ul li a').on('click', function(e) {
        e.preventDefault();
        const page = $(this).data('page');
        
        // Remove active class from all menu items and add to parent li of this link
        $('#sidebar ul li').removeClass('active');
        $(this).parent('li').addClass('active');

        loadPage(page);
    });

    // Profile dropdown click
    $('#dropdownProfile').on('click', function(e) {
        e.preventDefault();
        // Remove active class from sidebar menu items since we navigate to profile
        $('#sidebar ul li').removeClass('active');
        loadPage('edit_profile');
    });

    // Logout click
    $('#btnLogout').on('click', function(e) {
        e.preventDefault();
        localStorage.removeItem('currentCmsUser');
        window.location.href = 'login.html';
    });

    // Search input handler
    $('#sidebarSearch').on('input', function() {
        if (currentPage === 'view_contents') {
            // Re-render table with search filter starting from page 1
            renderContentsTable(1);
        }
    });

    // Function to load page using AJAX after 5 seconds delay
    function loadPage(page) {
        currentPage = page;
        
        // Clear any running timers
        if (loadingTimerInterval) {
            clearInterval(loadingTimerInterval);
        }

        // Show loading screen overlay
        const overlay = $('#loadingOverlay');
        const timerText = $('#loadingTimer');
        overlay.css('display', 'flex'); // using flex to center content
        
        let secondsLeft = 5;
        timerText.text(`Loading page in ${secondsLeft}s...`);

        // Countdown timer
        loadingTimerInterval = setInterval(function() {
            secondsLeft--;
            if (secondsLeft > 0) {
                timerText.text(`Loading page in ${secondsLeft}s...`);
            } else {
                clearInterval(loadingTimerInterval);
                // Perform AJAX load
                $.ajax({
                    url: `${page}.html`,
                    type: 'GET',
                    dataType: 'html',
                    success: function(htmlFragment) {
                        overlay.hide();
                        $('#mainContentContainer').html(htmlFragment);
                        // Initialize page specific controls
                        initPageModule(page);
                    },
                    error: function(err) {
                        overlay.hide();
                        $('#mainContentContainer').html(`
                            <div class="alert alert-danger m-3" role="alert">
                                <i class="fas fa-exclamation-triangle mr-2"></i> Failed to load page fragment: ${page}.html
                            </div>
                        `);
                        console.error('AJAX load error:', err);
                    }
                });
            }
        }, 1000);
    }

    // Helper to update displayed user name in navbar
    function updateNavbarName() {
        const user = JSON.parse(localStorage.getItem('currentCmsUser'));
        if (user) {
            const displayName = (user.firstName && user.lastName) 
                ? `${user.firstName} ${user.lastName}` 
                : user.username;
            $('#navUsername').text(displayName);
        }
    }

    // 3. Page Modules Initialization
    function initPageModule(page) {
        if (page === 'view_contents') {
            initViewContents();
        } else if (page === 'form_content') {
            initFormContent();
        } else if (page === 'edit_profile') {
            initEditProfile();
        }
    }

    // ==========================================
    // MODULE: View Contents
    // ==========================================
    const ITEMS_PER_PAGE = 5;
    
    function initViewContents() {
        renderContentsTable(1);
    }

    function renderContentsTable(pageNumber) {
        const contents = JSON.parse(localStorage.getItem('cmsContents') || '[]');
        const searchQuery = $('#sidebarSearch').val().toLowerCase().trim();

        // Filter contents based on sidebar search input (Title or Brief)
        const filteredContents = contents.filter(item => {
            return item.title.toLowerCase().includes(searchQuery) || 
                   item.brief.toLowerCase().includes(searchQuery);
        });

        const totalItems = filteredContents.length;
        const totalPages = Math.ceil(totalItems / ITEMS_PER_PAGE) || 1;
        
        // Ensure page number is within valid range
        if (pageNumber < 1) pageNumber = 1;
        if (pageNumber > totalPages) pageNumber = totalPages;

        const startIndex = (pageNumber - 1) * ITEMS_PER_PAGE;
        const endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalItems);
        const pageItems = filteredContents.slice(startIndex, endIndex);

        // Generate Table Rows
        const tbody = $('#contentsTableBody');
        tbody.empty();

        if (pageItems.length === 0) {
            tbody.append(`
                <tr>
                    <td colspan="4" class="text-center text-muted py-4">No content items found.</td>
                </tr>
            `);
        } else {
            pageItems.forEach((item, index) => {
                const globalIndex = startIndex + index + 1;
                tbody.append(`
                    <tr>
                        <td>${globalIndex}</td>
                        <td class="font-weight-bold text-success">${escapeHTML(item.title)}</td>
                        <td>${escapeHTML(item.brief)}</td>
                        <td>${escapeHTML(item.createdDate)}</td>
                    </tr>
                `);
            });
        }

        // Update Pagination Info Text
        const displayStart = totalItems === 0 ? 0 : startIndex + 1;
        $('#paginationInfo').text(`Showing ${displayStart} to ${endIndex} of ${totalItems} entries`);

        // Generate Pagination Controls
        const pagination = $('#contentsPagination');
        pagination.empty();

        // Prev Button
        pagination.append(`
            <li class="page-item ${pageNumber === 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${pageNumber - 1}"><i class="fas fa-chevron-left"></i></a>
            </li>
        `);

        // Page Number Buttons
        for (let i = 1; i <= totalPages; i++) {
            pagination.append(`
                <li class="page-item ${pageNumber === i ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i}</a>
                </li>
            `);
        }

        // Next Button
        pagination.append(`
            <li class="page-item ${pageNumber === totalPages ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${pageNumber + 1}"><i class="fas fa-chevron-right"></i></a>
            </li>
        `);

        // Pagination Click Handler
        pagination.find('a').on('click', function(e) {
            e.preventDefault();
            const targetPage = $(this).data('page');
            if (targetPage >= 1 && targetPage <= totalPages) {
                renderContentsTable(targetPage);
            }
        });
    }

    // ==========================================
    // MODULE: Add Content
    // ==========================================
    function initFormContent() {
        $('#contentForm').on('submit', function(e) {
            e.preventDefault();
            
            const titleInput = $('#contentTitle')[0];
            const briefInput = $('#contentBrief')[0];
            const mainInput = $('#contentMain')[0];
            let isValid = true;

            // Reset validation states
            $('.form-control').removeClass('is-invalid');
            $('#contentSuccessAlert').addClass('d-none');

            // Validate Title (Required, min 10, max 200)
            if (!titleInput.checkValidity() || titleInput.value.length < 10 || titleInput.value.length > 200) {
                $('#contentTitle').addClass('is-invalid');
                isValid = false;
            }

            // Validate Brief (Required, min 30, max 150)
            if (!briefInput.checkValidity() || briefInput.value.length < 30 || briefInput.value.length > 150) {
                $('#contentBrief').addClass('is-invalid');
                isValid = false;
            }

            // Validate Content (Required, min 50, max 1000)
            if (!mainInput.checkValidity() || mainInput.value.length < 50 || mainInput.value.length > 1000) {
                $('#contentMain').addClass('is-invalid');
                isValid = false;
            }

            if (isValid) {
                const contents = JSON.parse(localStorage.getItem('cmsContents') || '[]');
                
                // Get next ID
                const nextId = contents.reduce((max, item) => item.id > max ? item.id : max, 0) + 1;
                
                // Format Date: DD/MM/YYYY HH:MM
                const now = new Date();
                const day = String(now.getDate()).padStart(2, '0');
                const month = String(now.getMonth() + 1).padStart(2, '0');
                const year = now.getFullYear();
                const hours = String(now.getHours()).padStart(2, '0');
                const minutes = String(now.getMinutes()).padStart(2, '0');
                const formattedDate = `${day}/${month}/${year} ${hours}:${minutes}`;

                const newContent = {
                    id: nextId,
                    title: $('#contentTitle').val().trim(),
                    brief: $('#contentBrief').val().trim(),
                    content: $('#contentMain').val().trim(),
                    createdDate: formattedDate
                };

                contents.push(newContent);
                localStorage.setItem('cmsContents', JSON.stringify(contents));

                // Show Success Alert
                $('#contentSuccessAlert').removeClass('d-none');
                
                // Reset form
                $('#contentForm')[0].reset();
            }
        });
    }

    // ==========================================
    // MODULE: Edit Profile
    // ==========================================
    function initEditProfile() {
        const user = JSON.parse(localStorage.getItem('currentCmsUser'));
        if (!user) return;

        // Prefill form
        $('#profileFirstName').val(user.firstName || '');
        $('#profileLastName').val(user.lastName || '');
        $('#profileEmail').val(user.email || '');
        $('#profilePhone').val(user.phone || '');
        $('#profileDesc').val(user.description || '');
        
        // Update character counter for description
        updateCharCount(user.description || '');

        // Character count listener
        $('#profileDesc').on('input', function() {
            updateCharCount($(this).val());
        });

        function updateCharCount(text) {
            const count = text.length;
            $('#descCharCount').text(`${count}/200 characters`);
        }

        // Form validation and submit
        $('#profileForm').on('submit', function(e) {
            e.preventDefault();

            const firstNameInput = $('#profileFirstName')[0];
            const lastNameInput = $('#profileLastName')[0];
            const phoneInput = $('#profilePhone')[0];
            const descInput = $('#profileDesc')[0];
            let isValid = true;

            // Reset validation states
            $('.form-control').removeClass('is-invalid');
            $('#profileSuccessAlert').addClass('d-none');

            // Validate First Name (Required, min 3, max 30)
            if (!firstNameInput.checkValidity() || firstNameInput.value.length < 3 || firstNameInput.value.length > 30) {
                $('#profileFirstName').addClass('is-invalid');
                isValid = false;
            }

            // Validate Last Name (Required, min 3, max 30)
            if (!lastNameInput.checkValidity() || lastNameInput.value.length < 3 || lastNameInput.value.length > 30) {
                $('#profileLastName').addClass('is-invalid');
                isValid = false;
            }

            // Validate Phone (Required, min 9, max 13, numeric only)
            const phoneVal = phoneInput.value.trim();
            const isNumeric = /^\d+$/.test(phoneVal);
            if (!phoneInput.checkValidity() || phoneVal.length < 9 || phoneVal.length > 13 || !isNumeric) {
                $('#profilePhone').addClass('is-invalid');
                if (!isNumeric && phoneVal.length > 0) {
                    $('#phoneFeedback').text('Phone number must contain digits only.');
                } else {
                    $('#phoneFeedback').text('Phone number is required (9 - 13 digits).');
                }
                isValid = false;
            }

            // Validate Description (Optional, max 200)
            if (descInput.value.length > 200) {
                $('#profileDesc').addClass('is-invalid');
                isValid = false;
            }

            if (isValid) {
                const updatedProfile = {
                    firstName: $('#profileFirstName').val().trim(),
                    lastName: $('#profileLastName').val().trim(),
                    phone: $('#profilePhone').val().trim(),
                    description: $('#profileDesc').val().trim()
                };

                // Perform real AJAX POST simulation (hitting a test REST API to simulate network update)
                // This fits "call to update your information via Ajax"
                const submitButton = $('#profileForm button[type="submit"]');
                submitButton.prop('disabled', true).text('Updating...');

                $.ajax({
                    url: 'https://jsonplaceholder.typicode.com/posts',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        userId: user.email,
                        title: 'Profile Update',
                        body: updatedProfile
                    }),
                    success: function(ajaxResponse) {
                        submitButton.prop('disabled', false).text('Submit Button');

                        // Save update to session user
                        const newUserSession = { ...user, ...updatedProfile };
                        localStorage.setItem('currentCmsUser', JSON.stringify(newUserSession));

                        // Save update to permanent user list
                        const users = JSON.parse(localStorage.getItem('cmsUsers') || '[]');
                        const userIndex = users.findIndex(u => u.email.toLowerCase() === user.email.toLowerCase());
                        if (userIndex !== -1) {
                            users[userIndex] = { ...users[userIndex], ...updatedProfile };
                            localStorage.setItem('cmsUsers', JSON.stringify(users));
                        }

                        // Update navbar header display name
                        updateNavbarName();

                        // Show Success banner
                        $('#profileSuccessAlert').removeClass('d-none');
                    },
                    error: function(err) {
                        submitButton.prop('disabled', false).text('Submit Button');
                        alert('Failed to simulate profile update via network AJAX.');
                        console.error(err);
                    }
                });
            }
        });
    }

    // Helper function to escape HTML to prevent XSS
    function escapeHTML(str) {
        return str.replace(/[&<>'"]/g, 
            tag => ({
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                "'": '&#39;',
                '"': '&quot;'
            }[tag] || tag)
        );
    }
});

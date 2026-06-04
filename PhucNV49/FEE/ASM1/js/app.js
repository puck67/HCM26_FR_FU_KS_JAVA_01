$(document).ready(function() {
    // 1. Initialize user info in top navbar
    const userEmail = localStorage.getItem('userEmail') || 'user@example.com';
    $('#nav-user-email').text(userEmail);

    // Keep track of any active AJAX fetch promise
    let activeFetch = null;
    let loadingInterval = null;

    // 2. Navigation click handlers
    $('.sidebar-link').on('click', function(e) {
        e.preventDefault();
        const targetPage = $(this).data('target');
        loadDashboardPage(targetPage);
    });

    $('#nav-profile-btn').on('click', function(e) {
        e.preventDefault();
        loadDashboardPage('edit_profile');
    });

    $('#logout-btn').on('click', function(e) {
        e.preventDefault();
        localStorage.removeItem('userEmail');
        window.location.href = 'login.html';
    });

    // 3. Load default page (View Contents) on first load
    loadDashboardPage('view_contents');

    /**
     * Orchestrates page load with a 1-second simulated loading animation
     */
    function loadDashboardPage(pageName) {
        // Clear previous interval/timer if user clicked rapidly
        if (loadingInterval) {
            clearInterval(loadingInterval);
        }

        // Highlight correct sidebar link
        $('.sidebar-link').removeClass('active');
        if (pageName === 'view_contents') {
            $('#menu-view-contents').addClass('active');
        } else if (pageName === 'add_content') {
            $('#menu-form-content').addClass('active');
        }

        // Clear content area and display loading overlay
        $('#main-content').empty();
        $('#loading-overlay').css('display', 'flex');
        
        let progress = 0;
        const $progressBar = $('#loading-progress');
        $progressBar.css('width', '0%');

        // Start HTML page request in background (Ajax call)
        const fetchPromise = $.ajax({
            url: pageName + '.html',
            method: 'GET',
            dataType: 'html'
        });

        // 1-second progress bar animation (1000ms)
        const duration = 1000;
        const intervalTime = 50; // updates every 50ms
        const increment = (intervalTime / duration) * 100; // 5% per step

        loadingInterval = setInterval(function() {
            progress += increment;
            if (progress >= 100) {
                progress = 100;
                clearInterval(loadingInterval);
                
                // Wait for both timer and AJAX call to resolve
                fetchPromise.done(function(htmlContent) {
                    $('#loading-overlay').hide();
                    $('#main-content').html(htmlContent);
                    initializePageScripts(pageName);
                }).fail(function() {
                    $('#loading-overlay').hide();
                    $('#main-content').html(`
                        <div class="alert alert-danger" role="alert">
                            <h4 class="alert-heading"><i class="fas fa-exclamation-triangle"></i> Error Loading Page</h4>
                            <p>Unable to retrieve ${pageName}.html. Make sure the file exists and you are running a local dev server.</p>
                        </div>
                    `);
                });
            }
            $progressBar.css('width', progress + '%');
        }, intervalTime);
    }

    /**
     * Binds validation rules and custom behaviors for dynamic pages
     */
    function initializePageScripts(pageName) {
        if (pageName === 'edit_profile') {
            // Prefill email field from session
            $('#profile-email').val(userEmail);

            // Handle edit profile submit
            $('#edit-profile-form').on('submit', function(e) {
                e.preventDefault();
                const $form = $(this);

                if (validateForm($form)) {
                    const $submitBtn = $('#edit-profile-submit-btn');
                    const originalText = $submitBtn.text();
                    
                    // Show button loading spinner
                    $submitBtn.html('<i class="fas fa-spinner fa-spin mr-2"></i>Updating...').prop('disabled', true);
                    
                    // Call update information via Ajax
                    $.ajax({
                        url: 'success.json',
                        method: 'GET', // Simulated POST
                        dataType: 'json'
                    }).done(function(response) {
                        $('#edit-profile-alert').removeClass('d-none').hide().fadeIn(300);
                        
                        // Scroll main panel to top to show alert
                        $('.dashboard-content-wrapper').animate({ scrollTop: 0 }, 'slow');
                        
                        // Fade out alert after 4 seconds
                        setTimeout(function() {
                            $('#edit-profile-alert').fadeOut(500);
                        }, 4000);
                    }).fail(function() {
                        alert('Error connecting to the server. Please try again.');
                    }).always(function() {
                        $submitBtn.text(originalText).prop('disabled', false);
                    });
                }
            });
        } 
        
        else if (pageName === 'add_content') {
            // Handle add content submit
            $('#add-content-form').on('submit', function(e) {
                e.preventDefault();
                const $form = $(this);

                if (validateForm($form)) {
                    const $submitBtn = $('#add-content-submit-btn');
                    const originalText = $submitBtn.text();
                    
                    $submitBtn.html('<i class="fas fa-spinner fa-spin mr-2"></i>Submitting...').prop('disabled', true);

                    // Mock submitting content
                    setTimeout(function() {
                        $('#add-content-alert').removeClass('d-none').hide().fadeIn(300);
                        $form[0].reset();
                        
                        // Remove is-invalid classes
                        $form.find('.form-control').removeClass('is-invalid');

                        $('.dashboard-content-wrapper').animate({ scrollTop: 0 }, 'slow');
                        $submitBtn.text(originalText).prop('disabled', false);

                        setTimeout(function() {
                            $('#add-content-alert').fadeOut(500);
                        }, 4000);
                    }, 800);
                }
            });
        }
    }
});

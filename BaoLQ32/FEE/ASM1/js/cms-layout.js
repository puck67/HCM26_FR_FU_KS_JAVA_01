/**
 * cms-layout.js
 * Injects the shared CMS navbar + sidebar and handles
 * AJAX menu navigation with 5-second loading overlay.
 */
$(function () {

    // ── Navbar ────────────────────────────────────────────
    const navbar = `
    <nav class="cms-navbar">
        <span class="brand">CMS</span>
        <div class="dropdown">
            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" data-toggle="dropdown">
                &#9650;
            </button>
            <div class="dropdown-menu dropdown-menu-right">
                <a class="dropdown-item" href="edit-profile.html">
                    <i>&#128100;</i> User Profile
                </a>
                <a class="dropdown-item" href="login.html">
                    &#8594; Logout
                </a>
            </div>
        </div>
    </nav>`;

    // ── Sidebar ───────────────────────────────────────────
    const sidebar = `
    <aside class="cms-sidebar">
        <div class="search-box">
            <div class="input-group input-group-sm">
                <input type="text" class="form-control" placeholder="Search...">
                <div class="input-group-append">
                    <span class="input-group-text">&#128269;</span>
                </div>
            </div>
        </div>
        <ul class="list-unstyled mb-0">
            <li class="nav-item">
                <a data-target="view-contents.html"
                   class="${location.pathname.includes('view-contents') ? 'active' : ''}">
                    &#9776; View contents
                </a>
            </li>
            <li class="nav-item">
                <a data-target="form-content.html"
                   class="${location.pathname.includes('form-content') ? 'active' : ''}">
                    &#9998; Form content
                </a>
            </li>
        </ul>
    </aside>`;

    // ── Loading overlay ───────────────────────────────────
    const overlay = `
    <div id="loading-overlay">
        <div class="spinner-border text-success" role="status"></div>
        <span>Loading…</span>
    </div>`;

    $('body').prepend(navbar + sidebar + overlay);

    // ── AJAX navigation with 5-second fake load ───────────
    $(document).on('click', '[data-target]', function (e) {
        e.preventDefault();
        const target = $(this).data('target');

        $('#loading-overlay').addClass('show');

        setTimeout(() => {
            $('#loading-overlay').removeClass('show');
            window.location.href = target;
        }, 5000);
    });
});

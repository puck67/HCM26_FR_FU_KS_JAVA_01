$(document).ready(function() {
    // Seed data default definition (fallback if localStorage was cleared)
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

    // Load visitors from localStorage, fallback to seed data
    function getVisitors() {
        const data = localStorage.getItem("visitors");
        if (data) {
            return JSON.parse(data);
        } else {
            localStorage.setItem("visitors", JSON.stringify(seedVisitors));
            return seedVisitors;
        }
    }

    // Render table rows
    function renderTable(visitorsList) {
        const $tbody = $('#visitorTableBody');
        $tbody.empty();

        if (visitorsList.length === 0) {
            $tbody.append(`
                <tr>
                    <td colspan="7" class="text-center text-muted py-4">
                        <i class="fas fa-search-minus fa-2x mb-2 d-block text-secondary"></i>
                        No visitors found matching your search term.
                    </td>
                </tr>
            `);
            return;
        }

        visitorsList.forEach(function(visitor) {
            const hobbiesText = Array.isArray(visitor.hobbies) ? visitor.hobbies.join(', ') : visitor.hobbies;
            $tbody.append(`
                <tr>
                    <td>${escapeHtml(visitor.firstName)}</td>
                    <td>${escapeHtml(visitor.lastName)}</td>
                    <td>${escapeHtml(visitor.gender)}</td>
                    <td>${escapeHtml(visitor.telephone)}</td>
                    <td>${escapeHtml(visitor.youAreIn)}</td>
                    <td>${escapeHtml(hobbiesText)}</td>
                    <td>${escapeHtml(visitor.description)}</td>
                </tr>
            `);
        });
    }

    // Advanced multi-column search
    function performSearch() {
        const query = $('#searchInput').val().trim().toLowerCase();
        const allVisitors = getVisitors();

        if (query === "") {
            renderTable(allVisitors);
            return;
        }

        const filtered = allVisitors.filter(function(visitor) {
            const fName = (visitor.firstName || "").toLowerCase();
            const lName = (visitor.lastName || "").toLowerCase();
            const gender = (visitor.gender || "").toLowerCase();
            const tel = (visitor.telephone || "").toLowerCase();
            const location = (visitor.youAreIn || "").toLowerCase();
            const hobbies = (Array.isArray(visitor.hobbies) ? visitor.hobbies.join(', ') : visitor.hobbies || "").toLowerCase();
            const desc = (visitor.description || "").toLowerCase();

            // Match query against ANY column (Day 3-4 Specification Requirement #2!)
            return fName.includes(query) || 
                   lName.includes(query) || 
                   gender.includes(query) || 
                   tel.includes(query) || 
                   location.includes(query) || 
                   hobbies.includes(query) || 
                   desc.includes(query);
        });

        renderTable(filtered);
    }

    // Event listeners for search
    $('#searchBtn').on('click', function() {
        performSearch();
    });

    $('#searchInput').on('keyup', function(e) {
        // Run search immediately on typing for smooth real-time performance,
        // and handle explicit Enter key press
        performSearch();
    });

    // Reset search
    $('#resetSearchBtn').on('click', function() {
        $('#searchInput').val("");
        renderTable(getVisitors());
    });

    // Interactive Row Click Highlight using JQuery event delegation
    // This allows highlights to work flawlessly even after rows are re-rendered by search filters!
    $(document).on('click', '#visitorTableBody tr', function() {
        // Toggle the custom highlighting class on clicked row
        $(this).toggleClass('row-highlight-active');
    });

    // Escape HTML to prevent XSS injection
    function escapeHtml(string) {
        return String(string).replace(/[&<>"']/g, function(s) {
            return {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#39;'
            }[s];
        });
    }

    // Initial table render
    renderTable(getVisitors());
});

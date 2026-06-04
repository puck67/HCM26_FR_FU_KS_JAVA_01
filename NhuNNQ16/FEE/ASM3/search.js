$(document).ready(function () {
    // Helper to get visitors from localStorage
    function getVisitors() {
        let visitors = localStorage.getItem('visitors');
        if (!visitors) {

            let dummyDesc = "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly.";
            let dummyData = [
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: dummyDesc
                },
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: dummyDesc
                },
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: dummyDesc
                },
                {
                    firstName: "Jane",
                    lastName: "Doe",
                    gender: "Female",
                    telephone: "0912345678",
                    continent: "Asia",
                    hobbies: "Swimming, Sport",
                    description: "Jane Doe is another visitor registered in Asia."
                }
            ];
            localStorage.setItem('visitors', JSON.stringify(dummyData));
            return dummyData;
        }
        return JSON.parse(visitors);
    }

    // Function to render table rows based on keyword filter
    function renderTable(keyword = "") {
        let visitors = getVisitors();
        let tbody = $('#visitorsTable tbody');
        tbody.empty();

        // Normalize keyword for comparison
        let term = keyword.trim().toLowerCase();

        let matchCount = 0;

        visitors.forEach(function (v) {
            // Check if keyword matches any field (First name, Last name, Gender, Telephone, Continent, Hobbies, Description)
            let isMatch = !term ||
                v.firstName.toLowerCase().includes(term) ||
                v.lastName.toLowerCase().includes(term) ||
                v.gender.toLowerCase().includes(term) ||
                v.telephone.toLowerCase().includes(term) ||
                v.continent.toLowerCase().includes(term) ||
                v.hobbies.toLowerCase().includes(term) ||
                v.description.toLowerCase().includes(term);

            if (isMatch) {
                matchCount++;
                let row = $('<tr>');

                // Append cells (Note: Do not define cell background color in CSS so row highlight works)
                row.append($('<td>').text(v.firstName));
                row.append($('<td>').text(v.lastName));
                row.append($('<td>').text(v.gender));
                row.append($('<td>').text(v.telephone));
                row.append($('<td>').text(v.continent));
                row.append($('<td>').text(v.hobbies));
                row.append($('<td>').text(v.description));

                tbody.append(row);
            }
        });

        if (matchCount === 0) {
            tbody.append('<tr><td colspan="7" class="text-center text-muted py-4">No matching visitors found.</td></tr>');
        }
    }

    // Initial render
    renderTable();

    // Search form submit handler
    $('#searchForm').on('submit', function (e) {
        e.preventDefault();
        let keyword = $('#searchKeyword').val();
        renderTable(keyword);
    });

    // Table row highlight logic using jQuery
    $(document).on('mouseenter', '#visitorsTable tbody tr', function () {
        // Only highlight if the row has data cells and is not the "No matching visitors found" row
        if ($(this).find('td').length > 1) {
            $(this).addClass('highlight-row');
        }
    }).on('mouseleave', '#visitorsTable tbody tr', function () {
        $(this).removeClass('highlight-row');
    });
});

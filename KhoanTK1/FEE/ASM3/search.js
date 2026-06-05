$(document).ready(function () {
    // helper to get data from local storage
    function fetchRegistryData() {
        let guestList = localStorage.getItem('guest_registry_data');
        if (!guestList) {
            let mockRemarksText = "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly.";
            let mockGuestList = [
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: mockRemarksText
                },
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: mockRemarksText
                },
                {
                    firstName: "John",
                    lastName: "Terry",
                    gender: "Male",
                    telephone: "0909090909",
                    continent: "Europe",
                    hobbies: "Shopping, Cooking",
                    description: mockRemarksText
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
            localStorage.setItem('guest_registry_data', JSON.stringify(mockGuestList));
            return mockGuestList;
        }
        return JSON.parse(guestList);
    }

    // draw grid rows based on filter
    function drawResultsGrid(keyword = "") {
        let guestList = fetchRegistryData();
        let tbody = $('#grid-registry-list tbody');
        tbody.empty();

        let term = keyword.trim().toLowerCase();
        let count = 0;

        guestList.forEach(function (v) {
            // check matches across fields
            let matches = !term ||
                v.firstName.toLowerCase().includes(term) ||
                v.lastName.toLowerCase().includes(term) ||
                v.gender.toLowerCase().includes(term) ||
                v.telephone.toLowerCase().includes(term) ||
                v.continent.toLowerCase().includes(term) ||
                v.hobbies.toLowerCase().includes(term) ||
                v.description.toLowerCase().includes(term);

            if (matches) {
                count++;
                let row = $('<tr>');

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

        if (count === 0) {
            tbody.append('<tr><td colspan="7" class="text-center text-muted py-4">No matching visitors found.</td></tr>');
        }
    }

    // initial render
    drawResultsGrid();

    // search form submit handler
    $('#form-query-filter').on('submit', function (e) {
        e.preventDefault();
        let keyword = $('#query-input').val();
        drawResultsGrid(keyword);
    });

    // live search with debounce
    let typingTimer = null;
    $('#query-input').on('keyup', function () {
        clearTimeout(typingTimer);
        let keyword = $(this).val();
        typingTimer = setTimeout(function () {
            drawResultsGrid(keyword);
        }, 300); // 300ms delay
    });

    // hover row highlighting
    $(document).on('mouseenter', '#grid-registry-list tbody tr', function () {
        if ($(this).find('td').length > 1) {
            $(this).addClass('focused-row-style');
        }
    }).on('mouseleave', '#grid-registry-list tbody tr', function () {
        $(this).removeClass('focused-row-style');
    });
});

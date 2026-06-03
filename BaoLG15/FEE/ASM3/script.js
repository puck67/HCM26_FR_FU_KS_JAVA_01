$(document).ready(function() {
    var defaultData = [
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            email: "john.terry@example.com",
            country: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            email: "john.terry@example.com",
            country: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        },
        {
            firstName: "John",
            lastName: "Terry",
            gender: "Male",
            telephone: "0909090909",
            email: "john.terry@example.com",
            country: "Europe",
            hobbies: ["Shopping", "Cooking"],
            description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly."
        }
    ];

    function getVisitors() {
        var data = localStorage.getItem("visitorData");
        if (!data) {
            localStorage.setItem("visitorData", JSON.stringify(defaultData));
            return defaultData;
        }
        return JSON.parse(data);
    }

    function saveVisitors(visitors) {
        localStorage.setItem("visitorData", JSON.stringify(visitors));
    }

    if ($("#visitor-form").length > 0) {
        $("#visitor-form").on("submit", function(e) {
            e.preventDefault();

            $(".text-danger-custom").hide().text("");
            $("#validation-alert").hide().text("");
            $("#success-alert").hide().text("");

            var isValid = true;
            var errors = [];

            var firstName = $("#firstName").val().trim();
            if (firstName === "") {
                $("#firstName-error").text("First name is mandatory.").show();
                errors.push("First name is mandatory.");
                isValid = false;
            } else if (firstName.length > 20) {
                $("#firstName-error").text("First name must be at most 20 characters.").show();
                errors.push("First name must be at most 20 characters.");
                isValid = false;
            } else if (!/^[a-zA-Z\s\u00C0-\u1EF9]+$/.test(firstName)) {
                $("#firstName-error").text("First name must be characters and not contain number.").show();
                errors.push("First name must be characters and not contain number.");
                isValid = false;
            }

            var lastName = $("#lastName").val().trim();
            if (lastName === "") {
                $("#lastName-error").text("Last name is mandatory.").show();
                errors.push("Last name is mandatory.");
                isValid = false;
            } else if (lastName.length > 20) {
                $("#lastName-error").text("Last name must be at most 20 characters.").show();
                errors.push("Last name must be at most 20 characters.");
                isValid = false;
            } else if (!/^[a-zA-Z\s\u00C0-\u1EF9]+$/.test(lastName)) {
                $("#lastName-error").text("Last name must be characters and not contain number.").show();
                errors.push("Last name must be characters and not contain number.");
                isValid = false;
            }

            var gender = $("#gender").val();

            var telephone = $("#telephone").val().trim();
            if (telephone !== "") {
                if (telephone.length > 11) {
                    $("#telephone-error").text("Telephone must be at most 11 characters.").show();
                    errors.push("Telephone must be at most 11 characters.");
                    isValid = false;
                } else if (!/^\d+$/.test(telephone)) {
                    $("#telephone-error").text("Telephone must be number.").show();
                    errors.push("Telephone must be number.");
                    isValid = false;
                }
            }

            var email = $("#email").val().trim();
            if (email !== "") {
                if (email.length > 50) {
                    $("#email-error").text("Email must be at most 50 characters.").show();
                    errors.push("Email must be at most 50 characters.");
                    isValid = false;
                } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
                    $("#email-error").text("Email format is invalid.").show();
                    errors.push("Email format is invalid.");
                    isValid = false;
                }
            }

            var country = $("input[name='country']:checked").val();
            if (!country) {
                $("#country-error").text("You are in is mandatory.").show();
                errors.push("You are in is mandatory.");
                isValid = false;
            }

            var hobbies = [];
            $("input[name='hobbies']:checked").each(function() {
                hobbies.push($(this).val());
            });

            var description = $("#description").val().trim();
            if (description !== "") {
                if (description.length > 200) {
                    $("#description-error").text("Description must be at most 200 characters.").show();
                    errors.push("Description must be at most 200 characters.");
                    isValid = false;
                }
            }

            if (!isValid) {
                var alertHtml = "<strong>Validation Errors:</strong><ul>";
                $.each(errors, function(index, value) {
                    alertHtml += "<li>" + value + "</li>";
                });
                alertHtml += "</ul>";
                $("#validation-alert").html(alertHtml).show();
                $("html, body").animate({ scrollTop: 0 }, "slow");
            } else {
                var visitors = getVisitors();
                var newVisitor = {
                    firstName: firstName,
                    lastName: lastName,
                    gender: gender,
                    telephone: telephone,
                    email: email,
                    country: country,
                    hobbies: hobbies,
                    description: description
                };
                visitors.push(newVisitor);
                saveVisitors(visitors);

                $("#success-alert").text("Visitor registered successfully!").show();
                $("#visitor-form")[0].reset();
                $("html, body").animate({ scrollTop: 0 }, "slow");
            }
        });
    }

    if ($("#visitor-table").length > 0) {
        function renderTable(filterKeyword) {
            var visitors = getVisitors();
            var tbody = $("#visitor-table-body");
            tbody.empty();

            var keyword = filterKeyword ? filterKeyword.toLowerCase() : "";

            $.each(visitors, function(index, visitor) {
                var match = false;
                var hobbiesStr = visitor.hobbies ? visitor.hobbies.join(", ") : "";

                if (keyword === "") {
                    match = true;
                } else {
                    if (visitor.firstName.toLowerCase().indexOf(keyword) !== -1 ||
                        visitor.lastName.toLowerCase().indexOf(keyword) !== -1 ||
                        visitor.gender.toLowerCase().indexOf(keyword) !== -1 ||
                        visitor.telephone.toLowerCase().indexOf(keyword) !== -1 ||
                        visitor.country.toLowerCase().indexOf(keyword) !== -1 ||
                        hobbiesStr.toLowerCase().indexOf(keyword) !== -1 ||
                        visitor.description.toLowerCase().indexOf(keyword) !== -1) {
                        match = true;
                    }
                }

                if (match) {
                    var tr = $("<tr></tr>");
                    tr.append($("<td></td>").text(visitor.firstName));
                    tr.append($("<td></td>").text(visitor.lastName));
                    tr.append($("<td></td>").text(visitor.gender));
                    tr.append($("<td></td>").text(visitor.telephone));
                    tr.append($("<td></td>").text(visitor.country));
                    tr.append($("<td></td>").text(hobbiesStr));
                    tr.append($("<td></td>").text(visitor.description));
                    tbody.append(tr);
                }
            });
        }

        renderTable();

        $("#search-form").on("submit", function(e) {
            e.preventDefault();
            var keyword = $("#searchKeyword").val().trim();
            renderTable(keyword);
        });

        $(document).on("click", "#visitor-table-body tr", function() {
            $(this).toggleClass("highlight-row");
        });
    }
});

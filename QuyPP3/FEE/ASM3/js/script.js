$(document).ready(function () {
    // === MOCK DATA KHỞI TẠO (Nếu chưa có dữ liệu trong máy) ===
    let defaultData = [
        { firstName: "John", lastName: "Terry", gender: "Male", telephone: "0909090909", region: "Europe", hobbies: "Shopping, Cooking", description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly." },
        { firstName: "John", lastName: "Terry", gender: "Male", telephone: "0909090909", region: "Europe", hobbies: "Shopping, Cooking", description: "One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that'll stop the row highlight code from working properly." },
        { firstName: "Jane", lastName: "Doe", gender: "Female", telephone: "0123456789", region: "Asia", hobbies: "Swimming, Dance", description: "Học viên lớp Web Design nâng cao với Bootstrap 4 và jQuery." }
    ];

    if (!localStorage.getItem("visitors")) {
        localStorage.setItem("visitors", JSON.stringify(defaultData));
    }

    $("#registrationForm").on("submit", function (e) {
        e.preventDefault();
        let isValid = true;

        $(".error-message").hide();
        $("input, textarea").removeClass("is-invalid-custom");

        let firstName = $("#firstName").val().trim();
        let nameRegex = /^([^0-9]*)$/; // Không chứa số
        if (firstName === "" || firstName.length > 20 || !nameRegex.test(firstName)) {
            $("#error-firstName").show();
            $("#firstName").addClass("is-invalid-custom");
            isValid = false;
        }

        let lastName = $("#lastName").val().trim();
        if (lastName === "" || lastName.length > 20 || !nameRegex.test(lastName)) {
            $("#error-lastName").show();
            $("#lastName").addClass("is-invalid-custom");
            isValid = false;
        }

        let telephone = $("#telephone").val().trim();
        let phoneRegex = /^[0-9]+$/;
        if (telephone !== "") {
            if (!phoneRegex.test(telephone) || telephone.length > 11) {
                $("#error-telephone").show();
                $("#telephone").addClass("is-invalid-custom");
                isValid = false;
            }
        }

        let email = $("#email").val().trim();
        let emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (email !== "") {
            if (email.length > 50 || !emailRegex.test(email)) {
                $("#error-email").show();
                $("#email").addClass("is-invalid-custom");
                isValid = false;
            }
        }

        let region = $("input[name='region']:checked").val();
        if (!region) {
            $("#error-region").show();
            isValid = false;
        }

        let description = $("#description").val().trim();
        if (description.length > 200) {
            $("#error-description").show();
            $("#description").addClass("is-invalid-custom");
            isValid = false;
        }

        if (isValid) {
            let hobbies = [];
            $("input[name='hobbies']:checked").each(function () {
                hobbies.push($(this).val());
            });

            let newVisitor = {
                firstName: firstName,
                lastName: lastName,
                gender: $("#gender").val(),
                telephone: telephone || "N/A",
                region: region,
                hobbies: hobbies.length > 0 ? hobbies.join(", ") : "None",
                description: description || "N/A"
            };

            let currentList = JSON.parse(localStorage.getItem("visitors"));
            currentList.push(newVisitor);
            localStorage.setItem("visitors", JSON.stringify(currentList));

            alert("Đăng ký thành công thông tin Visitor!");
            window.location.href = "search.html";
        }
    });


    if ($("#visitorTable").length > 0) {
        renderTable();

        $("#btnSearch").on("click", function () {
            let keyword = $("#searchKeyword").val().trim().toLowerCase();
            renderTable(keyword);
        });

        $("#searchKeyword").on("keypress", function (e) {
            if (e.which === 13) {
                let keyword = $(this).val().trim().toLowerCase();
                renderTable(keyword);
            }
        });
    }

    function renderTable(keyword = "") {
        let list = JSON.parse(localStorage.getItem("visitors")) || [];
        let tbody = $("#visitorTable tbody");
        tbody.empty();

        list.forEach(function (item) {
            let isMatched = keyword === "" ||
                item.firstName.toLowerCase().includes(keyword) ||
                item.lastName.toLowerCase().includes(keyword) ||
                item.gender.toLowerCase().includes(keyword) ||
                item.telephone.toLowerCase().includes(keyword) ||
                item.region.toLowerCase().includes(keyword) ||
                item.hobbies.toLowerCase().includes(keyword) ||
                item.description.toLowerCase().includes(keyword);

            if (isMatched) {
                let row = `<tr>
                    <td>${item.firstName}</td>
                    <td>${item.lastName}</td>
                    <td>${item.gender}</td>
                    <td>${item.telephone}</td>
                    <td>${item.region}</td>
                    <td>${item.hobbies}</td>
                    <td class="text-left">${item.description}</td>
                </tr>`;
                tbody.append(row);
            }
        });

        if (tbody.children().length === 0) {
            tbody.append(`<tr><td colspan="7" class="text-muted">Không tìm thấy dữ liệu visitor nào trùng khớp!</td></tr>`);
        }
    }
});

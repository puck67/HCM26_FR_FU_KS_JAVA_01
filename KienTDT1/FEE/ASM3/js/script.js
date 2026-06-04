$(document).ready(function () {

    loadVisitors();

    $("#btnRegister").click(function () {

        if (!validateForm()) {
            return;
        }

        let firstName = $("#firstName").val();
        let lastName = $("#lastName").val();
        let gender = $("#gender").val();
        let telephone = $("#telephone").val();

        let region =
            $("input[name='region']:checked").val();

        let hobbies = [];

        $("input[type='checkbox']:checked")
            .each(function () {

                hobbies.push($(this).val());

            });

        let description =
            $("#description").val();

        let email = $("#email").val();

        let visitor = {
            firstName,
            lastName,
            gender,
            telephone,
            email,
            region,
            hobbies: hobbies.join(", "),
            description
        };

        let visitors =
            JSON.parse(localStorage.getItem("visitors"))
            || [];

        visitors.push(visitor);

        localStorage.setItem(
            "visitors",
            JSON.stringify(visitors)
        );

        alert("Register Success");
    });

    $("#btnSearch").click(function () {

        let keyword =
            $("#txtSearch").val().toLowerCase();

        $("#visitorTable tr").each(function () {

            let text =
                $(this).text().toLowerCase();

            $(this).toggle(
                text.indexOf(keyword) > -1
            );

        });

    });

});

function loadVisitors() {

    let visitors =
        JSON.parse(localStorage.getItem("visitors"))
        || [];

    let html = "";

    visitors.forEach(function (v) {

        html += `
        <tr>
            <td>${v.firstName}</td>
            <td>${v.lastName}</td>
            <td>${v.gender}</td>
            <td>${v.telephone}</td>
            <td>${v.email}</td>
            <td>${v.region}</td>
            <td>${v.hobbies}</td>
            <td>${v.description}</td>
        </tr>
        `;

    });

    $("#visitorTable").html(html);

}

function validateForm() {

    let firstName = $("#firstName").val().trim();
    let lastName = $("#lastName").val().trim();
    let telephone = $("#telephone").val().trim();
    let email = $("#email").val().trim();
    let region = $("input[name='region']:checked").val();
    let description = $("#description").val().trim();

    let nameRegex = /^[A-Za-z\s]+$/;
    let phoneRegex = /^[0-9]+$/;
    let emailRegex =
        /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    if (firstName === "") {
        alert("First Name is required");
        return false;
    }

    if (firstName.length > 20) {
        alert("First Name max length is 20");
        return false;
    }

    if (!nameRegex.test(firstName)) {
        alert("First Name must contain only characters");
        return false;
    }

    if (lastName === "") {
        alert("Last Name is required");
        return false;
    }

    if (lastName.length > 20) {
        alert("Last Name max length is 20");
        return false;
    }

    if (!nameRegex.test(lastName)) {
        alert("Last Name must contain only characters");
        return false;
    }

    if (telephone !== "") {

        if (telephone.length > 11) {
            alert("Telephone max length is 11");
            return false;
        }

        if (!phoneRegex.test(telephone)) {
            alert("Telephone must be number");
            return false;
        }
    }

    if (email !== "") {

        if (email.length > 50) {
            alert("Email max length is 50");
            return false;
        }

        if (!emailRegex.test(email)) {
            alert("Invalid Email");
            return false;
        }
    }

    if (!region) {
        alert("Please select region");
        return false;
    }

    if (description.length > 200) {
        alert("Description max length is 200");
        return false;
    }

    return true;
}
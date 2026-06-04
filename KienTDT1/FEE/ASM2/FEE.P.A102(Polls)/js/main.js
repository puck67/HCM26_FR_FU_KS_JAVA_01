$(document).ready(function () {

    // Add Answer

    $(document).on("click", ".addAnswer", function () {

        let html = `
        <div class="input-group mb-2">

            <input type="text"
                   class="form-control answer"
                   placeholder="Type your answer">

            <div class="input-group-append">

                <button type="button"
                        class="btn btn-info addAnswer">
                    +
                </button>

            </div>

        </div>
        `;

        $(this)
            .closest(".answerContainer")
            .append(html);

    });

    // Add Question

    $("#addQuestion").click(function () {

        let block =
            $(".question-block")
                .first()
                .clone();

        block.find("input[type=text]").val("");

        block.find("input[type=checkbox]")
            .prop("checked", false);

        $("#questionContainer")
            .append(block);

    });

    // Validation

    $("#btnSave").click(function () {

        $(".is-invalid")
            .removeClass("is-invalid");

        let errors = [];

        let pollName =
            $("#pollName").val().trim();

        if (
            pollName.length < 3 ||
            pollName.length > 255
        ) {

            errors.push(
                "Name poll must be 3-255 characters"
            );

            $("#pollName")
                .addClass("is-invalid");
        }

        $(".question").each(function () {

            let value =
                $(this).val().trim();

            if (
                value.length < 3 ||
                value.length > 255
            ) {

                errors.push(
                    "Question must be 3-255 characters"
                );

                $(this)
                    .addClass("is-invalid");
            }

        });

        $(".answer").each(function () {

            let value =
                $(this).val().trim();

            if (
                value.length < 3 ||
                value.length > 200
            ) {

                errors.push(
                    "Answer must be 3-200 characters"
                );

                $(this)
                    .addClass("is-invalid");
            }

        });

        if (errors.length > 0) {

            $("#errorBox")
                .removeClass("d-none")
                .html(errors.join("<br>"));

            return;
        }

        $("#errorBox")
            .removeClass("alert-danger")
            .addClass("alert-success")
            .html("Create poll successfully.");

    });

    // Delete

    $(".btn-delete").click(function () {

        let row =
            $(this).closest("tr");

        if (
            confirm(
                "Are you sure you want to delete this item?"
            )
        ) {

            $.ajax({

                url: "#",

                type: "POST",

                success: function () {

                    row.remove();

                }

            });

        }

    });

});
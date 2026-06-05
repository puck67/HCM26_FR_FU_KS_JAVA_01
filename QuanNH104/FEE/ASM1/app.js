$(document).ready(function () {
    const defaultContents = [
        {
            id: 1,
            title: "Lorem ipsum dolor sit amet",
            brief: "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
            content: "Full content for Lorem ipsum...",
            createdDate: "03/05/2016 12:00"
        },
        {
            id: 2,
            title: "Vestibulum tincidunt est vitae",
            brief: "Aliquam ornare lacus adipiscing, posuere lectures et, fringilla augue.",
            content: "Full content for Vestibulum...",
            createdDate: "03/05/2016 13:15"
        },
        {
            id: 3,
            title: "Aliquam ornare lacus adipiscing",
            brief: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum tincidunt est vitae ultrices accumsan.",
            content: "Full content for Aliquam...",
            createdDate: "03/05/2016 15:00"
        }
    ];

    if (!localStorage.getItem("cms_contents")) {
        localStorage.setItem("cms_contents", JSON.stringify(defaultContents));
    }

    function getLoggedInUser() {
        return JSON.parse(sessionStorage.getItem("currentUser")) || JSON.parse(localStorage.getItem("rememberedUser"));
    }

    function checkAuth() {
        const currentUser = getLoggedInUser();
        const path = window.location.pathname;
        const isAuthPage = path.includes("login.html") || path.includes("register.html");

        if (!currentUser && !isAuthPage) {
            window.location.href = "login.html";
        } else if (currentUser && isAuthPage) {
            window.location.href = "view-content.html";
        }

        if (currentUser) {
            $("#navbar-username").text(currentUser.username || currentUser.email);
        }
    }

    checkAuth();

    if ($("#login-form").length) {
        $("#login-form").on("submit", function (e) {
            e.preventDefault();
            const email = $("#login-email").val().trim();
            const password = $("#login-password").val().trim();
            const rememberMe = $("#remember-me").is(":checked");

            if (!email || !password) {
                showAlert("danger", "Vui lòng điền đầy đủ Email và Mật khẩu!");
                return;
            }

            const users = JSON.parse(localStorage.getItem("cms_users")) || [];
            const user = users.find(u => u.email === email && u.password === password);
            const isAdminDefault = email === "admin@example.com" && password === "admin123";

            if (user || isAdminDefault) {
                const loggedInUser = user || { email: email, username: "Admin User", firstName: "Admin", lastName: "User" };
                sessionStorage.setItem("currentUser", JSON.stringify(loggedInUser));

                if (rememberMe) {
                    localStorage.setItem("rememberedUser", JSON.stringify(loggedInUser));
                }

                window.location.href = "view-content.html";
            } else {
                showAlert("danger", "Email hoặc Mật khẩu không chính xác!");
            }
        });
    }

    if ($("#register-form").length) {
        $("#register-form").on("submit", function (e) {
            e.preventDefault();
            const username = $("#register-username").val().trim();
            const email = $("#register-email").val().trim();
            const password = $("#register-password").val().trim();
            const rePassword = $("#register-repassword").val().trim();

            if (!username || !email || !password || !rePassword) {
                showAlert("danger", "Vui lòng điền đầy đủ các thông tin!");
                return;
            }

            if (password !== rePassword) {
                showAlert("danger", "Mật khẩu nhập lại không khớp!");
                return;
            }

            const users = JSON.parse(localStorage.getItem("cms_users")) || [];
            if (users.find(u => u.email === email)) {
                showAlert("danger", "Email này đã được đăng ký!");
                return;
            }

            const newUser = {
                username: username,
                email: email,
                password: password,
                firstName: "",
                lastName: "",
                phone: "",
                description: ""
            };
            users.push(newUser);
            localStorage.setItem("cms_users", JSON.stringify(users));

            showAlert("success", "Đăng ký thành công! Đang chuyển hướng đến trang đăng nhập...");
            setTimeout(function () {
                window.location.href = "login.html";
            }, 1500);
        });
    }

    if ($("#content-table-body").length) {
        loadContentsTable();
    }

    function loadContentsTable() {
        const contents = JSON.parse(localStorage.getItem("cms_contents")) || [];
        const $tableBody = $("#content-table-body");
        $tableBody.empty();

        if (contents.length === 0) {
            $tableBody.append(`<tr><td colspan="4" class="text-center">Không có nội dung nào</td></tr>`);
            return;
        }

        contents.forEach((item, index) => {
            $tableBody.append(`
                <tr class="content-row">
                    <td>${index + 1}</td>
                    <td class="content-title">${escapeHtml(item.title)}</td>
                    <td class="content-brief">${escapeHtml(item.brief)}</td>
                    <td>${item.createdDate}</td>
                </tr>
            `);
        });
    }

    if ($("#add-content-form").length) {
        $("#add-content-form").on("submit", function (e) {
            e.preventDefault();
            const title = $("#content-title-input").val().trim();
            const brief = $("#content-brief-input").val().trim();
            const contentDetail = $("#content-detail-input").val().trim();

            if (!title || !brief || !contentDetail) {
                showAlert("danger", "Vui lòng điền đầy đủ Tiêu đề, Tóm tắt và Nội dung!");
                return;
            }

            const contents = JSON.parse(localStorage.getItem("cms_contents")) || [];
            const now = new Date();
            const day = String(now.getDate()).padStart(2, '0');
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const year = now.getFullYear();
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            const formattedDate = `${day}/${month}/${year} ${hours}:${minutes}`;

            const newContent = {
                id: Date.now(),
                title: title,
                brief: brief,
                content: contentDetail,
                createdDate: formattedDate
            };

            contents.push(newContent);
            localStorage.setItem("cms_contents", JSON.stringify(contents));

            showAlert("success", "Thêm nội dung thành công! Đang chuyển hướng...");
            setTimeout(function () {
                window.location.href = "view-content.html";
            }, 1500);
        });

        $("#add-content-form").on("reset", function () {
            $(".alert").remove();
        });
    }

    if ($("#edit-profile-form").length) {
        const currentUser = getLoggedInUser();

        if (currentUser) {
            $("#profile-firstname").val(currentUser.firstName || "");
            $("#profile-lastname").val(currentUser.lastName || "");
            $("#profile-email").val(currentUser.email || "");
            $("#profile-phone").val(currentUser.phone || "");
            $("#profile-description").val(currentUser.description || "");
        }

        $("#edit-profile-form").on("submit", function (e) {
            e.preventDefault();
            const firstName = $("#profile-firstname").val().trim();
            const lastName = $("#profile-lastname").val().trim();
            const phone = $("#profile-phone").val().trim();
            const description = $("#profile-description").val().trim();

            const users = JSON.parse(localStorage.getItem("cms_users")) || [];
            const userIndex = users.findIndex(u => u.email === currentUser.email);

            currentUser.firstName = firstName;
            currentUser.lastName = lastName;
            currentUser.phone = phone;
            currentUser.description = description;

            if (userIndex !== -1) {
                users[userIndex] = { ...users[userIndex], ...currentUser };
                localStorage.setItem("cms_users", JSON.stringify(users));
            }

            if (sessionStorage.getItem("currentUser")) {
                sessionStorage.setItem("currentUser", JSON.stringify(currentUser));
            }
            if (localStorage.getItem("rememberedUser")) {
                localStorage.setItem("rememberedUser", JSON.stringify(currentUser));
            }

            $("#navbar-username").text(currentUser.username || currentUser.email);
            showAlert("success", "Cập nhật thông tin cá nhân thành công!");
        });

        $("#edit-profile-form").on("reset", function (e) {
            e.preventDefault();
            if (currentUser) {
                $("#profile-firstname").val(currentUser.firstName || "");
                $("#profile-lastname").val(currentUser.lastName || "");
                $("#profile-phone").val(currentUser.phone || "");
                $("#profile-description").val(currentUser.description || "");
            }
            $(".alert").remove();
        });
    }

    $("#sidebar-search-btn, #sidebar-search-input").on("keyup click", function () {
        const query = $("#sidebar-search-input").val().toLowerCase().trim();
        
        if ($("#content-table-body").length) {
            $(".content-row").each(function () {
                const title = $(this).find(".content-title").text().toLowerCase();
                const brief = $(this).find(".content-brief").text().toLowerCase();

                if (title.includes(query) || brief.includes(query)) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        }
    });

    $(document).on("click", "#logout-btn", function (e) {
        e.preventDefault();
        sessionStorage.removeItem("currentUser");
        localStorage.removeItem("rememberedUser");
        window.location.href = "login.html";
    });

    function showAlert(type, message) {
        $(".alert").remove();
        const alertHtml = `
            <div class="alert alert-${type} alert-dismissible fade show mt-3" role="alert">
                ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        `;
        if ($("form").length) {
            $("form").before(alertHtml);
        } else {
            $(".card-body").prepend(alertHtml);
        }
    }

    function escapeHtml(text) {
        if (!text) return "";
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});

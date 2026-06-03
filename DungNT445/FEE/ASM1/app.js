$(document).ready(function () {
    
    // ==========================================
    // HÀM VALIDATE CHUNG CHO TẤT CẢ CÁC FORM
    // ==========================================
    function validateForm(formId, rules) {
        let isValid = true;
        const $form = $(formId);
        
        // Reset các lỗi cũ trước khi kiểm tra
        $form.find('.form-control').removeClass('is-invalid');
        
        // Sử dụng vòng lặp duyệt qua từng trường dữ liệu cần check theo yêu cầu đặc tả
        for (let fieldName in rules) {
            const $field = $form.find(`[name="${fieldName}"]`);
            if (!$field.length) continue;
            
            const value = $field.val().trim();
            const rule = rules[fieldName];
            
            // 1. Kiểm tra trường bắt buộc (Required)
            if (rule.required && value === "") {
                $field.addClass('is-invalid');
                $field.siblings('.error-feedback').text(`${rule.label} không được để trống.`);
                isValid = false;
                continue;
            }
            
            // Nếu có dữ liệu thì mới check độ dài hoặc định dạng định danh
            if (value !== "") {
                // 2. Kiểm tra độ dài tối thiểu (Min length)
                if (rule.minLength && value.length < rule.minLength) {
                    $field.addClass('is-invalid');
                    $field.siblings('.error-feedback').text(`${rule.label} phải có tối thiểu ${rule.minLength} ký tự.`);
                    isValid = false;
                    continue;
                }
                
                // 3. Kiểm tra độ dài tối đa (Max length)
                if (rule.maxLength && value.length > rule.maxLength) {
                    $field.addClass('is-invalid');
                    $field.siblings('.error-feedback').text(`${rule.label} không được vượt quá ${rule.maxLength} ký tự.`);
                    isValid = false;
                    continue;
                }
                
                // 4. Kiểm tra định dạng Email chuẩn
                if (rule.type === 'email') {
                    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                    if (!emailRegex.test(value)) {
                        $field.addClass('is-invalid');
                        $field.siblings('.error-feedback').text(`Định dạng ${rule.label} không hợp lệ.`);
                        isValid = false;
                        continue;
                    }
                }

                // Kiểm tra định dạng Số điện thoại (Việt Nam)
                if (rule.type === 'phone') {
                    const phoneRegex = /^(0|\+84)(3|5|7|8|9)[0-9]{8}$/;
                    if (!phoneRegex.test(value)) {
                        $field.addClass('is-invalid');
                        $field.siblings('.error-feedback').text(`Định dạng ${rule.label} không hợp lệ (VD: 0912345678).`);
                        isValid = false;
                        continue;
                    }
                }
                
                // 5. Kiểm tra trùng khớp mật khẩu (Re-password)
                if (rule.equalTo) {
                    const matchValue = $(rule.equalTo).val().trim();
                    if (value !== matchValue) {
                        $field.addClass('is-invalid');
                        $field.siblings('.error-feedback').text(`${rule.label} không khớp với mật khẩu đã nhập.`);
                        isValid = false;
                        continue;
                    }
                }
            }
        }
        return isValid;
    }

    // ==========================================
    // 1. XỬ LÝ CHO TRANG LOGIN
    // ==========================================
    $('#loginForm').on('submit', function (e) {
        e.preventDefault();
        const loginRules = {
            email: { label: 'Email', required: true, minLength: 5, maxLength: 50, type: 'email' },
            password: { label: 'Password', required: true, minLength: 8, maxLength: 30 }
        };
        
        if (validateForm('#loginForm', loginRules)) {
            alert('Đăng nhập thành công! Hệ thống chuyển hướng sang CMS...');
            window.location.href = 'view-contents.html';
        }
    });

    // ==========================================
    // 2. XỬ LÝ CHO TRANG REGISTER
    // ==========================================
    $('#registerForm').on('submit', function (e) {
        e.preventDefault();
        const registerRules = {
            username: { label: 'User name', required: true, minLength: 3, maxLength: 30 },
            email: { label: 'Email', required: true, minLength: 5, type: 'email' },
            password: { label: 'Password', required: true, minLength: 8, maxLength: 30 },
            repassword: { label: 'Re Password', required: true, minLength: 8, maxLength: 30, equalTo: '[name="password"]' }
        };
        
        if (validateForm('#registerForm', registerRules)) {
            alert('Đăng ký tài khoản thành công!');
            window.location.href = 'login.html';
        }
    });

    // ==========================================
    // 3. XỬ LÝ CHO TRANG EDIT PROFILE (SUBMIT QUA AJAX)
    // ==========================================
    $('#editProfileForm').on('submit', function (e) {
        e.preventDefault();
        const profileRules = {
            firstname: { label: 'First Name', required: true, minLength: 3, maxLength: 30 },
            lastname: { label: 'Last Name', required: true, minLength: 3, maxLength: 30 },
            phone: { label: 'Phone', required: true, type: 'phone' },
            description: { label: 'Description', required: false, maxLength: 200 }
        };
        
        if (validateForm('#editProfileForm', profileRules)) {
            // Giả lập gửi cập nhật thông tin qua AJAX theo yêu cầu tài liệu đặc tả
            const submitBtn = $(this).find('button[type="submit"]');
            submitBtn.prop('disabled', true).text('Updating...');
            
            setTimeout(function() {
                alert('Cập nhật thông tin Profile thành công qua AJAX!');
                submitBtn.prop('disabled', false).text('Submit Button');
            }, 1000); // Phản hồi AJAX nhanh cho trải nghiệm người dùng tốt hơn
        }
    });

    // ==========================================
    // 4. XỬ LÝ TRANG ADD CONTENT
    // ==========================================
    $('#addContentForm').on('submit', function (e) {
        e.preventDefault();
        const contentRules = {
            title: { label: 'Title', required: true, minLength: 10, maxLength: 200 },
            brief: { label: 'Brief', required: true, minLength: 30, maxLength: 150 },
            content: { label: 'Content', required: true, minLength: 50, maxLength: 1000 }
        };
        
        if (validateForm('#addContentForm', contentRules)) {
            alert('Thêm nội dung Content thành công!');
            window.location.href = 'view-contents.html';
        }
    });

    // ==========================================
    // 5. GIẢ LẬP AJAX LOADING 5 GIÂY CHO MENU TRÁI
    // ==========================================
    $('.ajax-link').on('click', function (e) {
        e.preventDefault();
        const targetPage = $(this).attr('href');
        
        // Hiện màn hình Loading che vùng nội dung chính
        $('#main-content-area').html(`
            <div class="d-flex justify-content-center align-items-center" style="min-height: 400px; flex-direction: column;">
                <div class="spinner-border text-success mb-3" role="status" style="width: 3rem; height: 3rem;"></div>
                <h2 class="text-muted">Loading...</h2>
                <p class="text-secondary small">Vui lòng đợi trong 5 giây (Yêu cầu tài liệu đặc tả)...</p>
            </div>
        `);
        
        // Chạy hiệu ứng delay đúng 5000ms (5 giây) trước khi chuyển tiếp trang HTML tương ứng
        setTimeout(function () {
            window.location.href = targetPage;
        }, 5000);
    });
});

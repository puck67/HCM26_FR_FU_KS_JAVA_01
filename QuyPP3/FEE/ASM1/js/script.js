$(document).ready(function () {
    // Basic form validation for login
    $('#loginForm').on('submit', function(e) {
        let email = $('#email').val();
        let password = $('#password').val();
        if(!email || !password) {
            e.preventDefault();
            alert('Please enter both email and password.');
        }
    });

    // Basic form validation for register
    $('#registerForm').on('submit', function(e) {
        let password = $('#password').val();
        let rePassword = $('#rePassword').val();
        if(password !== rePassword) {
            e.preventDefault();
            alert('Passwords do not match!');
        }
    });
});

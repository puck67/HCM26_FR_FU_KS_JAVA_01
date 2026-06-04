$(document).ready(function () {
  let visitors = JSON.parse(localStorage.getItem('visitors')) || [];

  $('#registrationForm').on('submit', function (e) {
    e.preventDefault();

    let isValid = true;
    let errors = [];

    // Clear previous errors
    $('.field-error').removeClass('show').text('');
    $('.form-input, .form-textarea').removeClass('is-invalid-field');
    $('#error-summary').hide();
    $('#error-list').empty();

    // 1. First Name
    let firstName = $('#firstName').val().trim();
    if (!firstName) {
      isValid = false;
      let msg = 'First name is mandatory.';
      $('#firstName').addClass('is-invalid-field');
      $('#err-firstName').text(msg).addClass('show');
      errors.push(msg);
    } else if (firstName.length > 20) {
      isValid = false;
      let msg = 'First name must not exceed 20 characters.';
      $('#firstName').addClass('is-invalid-field');
      $('#err-firstName').text(msg).addClass('show');
      errors.push(msg);
    } else if (/[0-9]/.test(firstName)) {
      isValid = false;
      let msg = 'First name must only contain letters, not numbers.';
      $('#firstName').addClass('is-invalid-field');
      $('#err-firstName').text(msg).addClass('show');
      errors.push(msg);
    }

    // 2. Last Name
    let lastName = $('#lastName').val().trim();
    if (!lastName) {
      isValid = false;
      let msg = 'Last name is mandatory.';
      $('#lastName').addClass('is-invalid-field');
      $('#err-lastName').text(msg).addClass('show');
      errors.push(msg);
    } else if (lastName.length > 20) {
      isValid = false;
      let msg = 'Last name must not exceed 20 characters.';
      $('#lastName').addClass('is-invalid-field');
      $('#err-lastName').text(msg).addClass('show');
      errors.push(msg);
    } else if (/[0-9]/.test(lastName)) {
      isValid = false;
      let msg = 'Last name must only contain letters, not numbers.';
      $('#lastName').addClass('is-invalid-field');
      $('#err-lastName').text(msg).addClass('show');
      errors.push(msg);
    }

    // 3. Telephone
    let telephone = $('#telephone').val().trim();
    if (telephone) {
      if (telephone.length > 11) {
        isValid = false;
        let msg = 'Telephone must not exceed 11 digits.';
        $('#telephone').addClass('is-invalid-field');
        $('#err-telephone').text(msg).addClass('show');
        errors.push(msg);
      } else if (!/^\d+$/.test(telephone)) {
        isValid = false;
        let msg = 'Telephone must contain numbers only.';
        $('#telephone').addClass('is-invalid-field');
        $('#err-telephone').text(msg).addClass('show');
        errors.push(msg);
      }
    }

    // 4. Email
    let email = $('#email').val().trim();
    if (email) {
      if (email.length > 50) {
        isValid = false;
        let msg = 'Email must not exceed 50 characters.';
        $('#email').addClass('is-invalid-field');
        $('#err-email').text(msg).addClass('show');
        errors.push(msg);
      } else if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
        isValid = false;
        let msg = 'Email format is invalid (e.g. name@domain.com).';
        $('#email').addClass('is-invalid-field');
        $('#err-email').text(msg).addClass('show');
        errors.push(msg);
      }
    }

    // 5. Region
    let region = $('input[name="region"]:checked').val();
    if (!region) {
      isValid = false;
      let msg = 'You are in: please select a region.';
      $('#err-region').text(msg).addClass('show');
      errors.push(msg);
    }

    // 6. Description
    let description = $('#description').val().trim();
    if (description && description.length > 200) {
      isValid = false;
      let msg = 'Description must not exceed 200 characters.';
      $('#description').addClass('is-invalid-field');
      $('#err-description').text(msg).addClass('show');
      errors.push(msg);
    }

    if (!isValid) {
      errors.forEach(function (err) {
        $('#error-list').append('<li>' + err + '</li>');
      });
      $('#error-summary').show();
      $('html, body').animate({ scrollTop: 0 }, 300);
      return;
    }

    // Collect hobbies
    let hobbies = [];
    $('input[name="hobbies"]:checked').each(function () {
      hobbies.push($(this).val());
    });

    let newVisitor = {
      firstName: firstName,
      lastName: lastName,
      gender: $('#gender').val(),
      telephone: telephone || 'N/A',
      email: email || 'N/A',
      region: region,
      hobbies: hobbies.length > 0 ? hobbies.join(', ') : 'N/A',
      description: description || 'N/A'
    };

    visitors.push(newVisitor);
    localStorage.setItem('visitors', JSON.stringify(visitors));

    alert('Registration successful!');
    $('#registrationForm')[0].reset();
    $('.form-input, .form-textarea').removeClass('is-invalid-field');
    $('.field-error').removeClass('show').text('');
  });
});

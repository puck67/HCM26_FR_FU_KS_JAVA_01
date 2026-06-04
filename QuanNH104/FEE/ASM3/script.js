$(document).ready(function() {
    

    if(!localStorage.getItem('visitors')) {
        let sampleData = [
            {
                firstName: 'John',
                lastName: 'Terry',
                gender: 'Male',
                telephone: '0909090909',
                email: 'john@example.com',
                region: 'Europe',
                hobbies: 'Shopping, Cooking',
                description: 'One thing to note is you should make sure not to define the background color of a table cell in your stylesheet as that will stop the row highlight code from working properly.'
            }
        ];
        localStorage.setItem('visitors', JSON.stringify(sampleData));
    }

    function loadData(filterKeyword = '') {
        let visitorsStr = localStorage.getItem('visitors');
        let tbody = $('#visitorTable tbody');
        if(!tbody.length) return;
        
        tbody.empty();
        if(visitorsStr) {
            let visitors = JSON.parse(visitorsStr);
            visitors.forEach(function(v) {
                let matches = true;
                if(filterKeyword) {
                    let kw = filterKeyword.toLowerCase();
                    matches = (
                        (v.firstName && v.firstName.toLowerCase().includes(kw)) ||
                        (v.lastName && v.lastName.toLowerCase().includes(kw)) ||
                        (v.gender && v.gender.toLowerCase().includes(kw)) ||
                        (v.telephone && v.telephone.toLowerCase().includes(kw)) ||
                        (v.region && v.region.toLowerCase().includes(kw)) ||
                        (v.hobbies && v.hobbies.toLowerCase().includes(kw)) ||
                        (v.description && v.description.toLowerCase().includes(kw))
                    );
                }
                
                if(matches) {
                    let row = `<tr>
                        <td>${v.firstName || ''}</td>
                        <td>${v.lastName || ''}</td>
                        <td>${v.gender || ''}</td>
                        <td>${v.telephone || ''}</td>
                        <td>${v.region || ''}</td>
                        <td>${v.hobbies || ''}</td>
                        <td>${v.description || ''}</td>
                    </tr>`;
                    tbody.append(row);
                }
            });
        }
    }

    if($('#visitorTable').length > 0) {
        loadData();
    }

    $('#btnSearch').click(function() {
        let kw = $('#searchInput').val();
        loadData(kw);
    });

    $('#btnRegister').click(function() {
        let isValid = true;
        

        $('.error-msg').hide();
        
        let firstName = $('#firstName').val().trim();
        let lastName = $('#lastName').val().trim();
        let telephone = $('#telephone').val().trim();
        let email = $('#email').val().trim();
        let region = $('input[name="region"]:checked').val();
        let description = $('#description').val().trim();
        

        let nameRegex = /^[a-zA-Z\s]+$/;
        if(firstName === '' || firstName.length > 20 || !nameRegex.test(firstName)) {
            $('#firstNameError').show();
            isValid = false;
        }
        

        if(lastName === '' || lastName.length > 20 || !nameRegex.test(lastName)) {
            $('#lastNameError').show();
            isValid = false;
        }
        

        let phoneRegex = /^[0-9]+$/;
        if(telephone !== '') {
            if(telephone.length > 11 || !phoneRegex.test(telephone)) {
                $('#telephoneError').show();
                isValid = false;
            }
        }
        

        if(email !== '') {
            let emailRegex = /^[a-z0-9]+@[a-zA-Z.]+$/i; 
            if(email.length > 50 || !emailRegex.test(email)) {
                $('#emailError').show();
                isValid = false;
            }
        }
        

        if(!region) {
            $('#regionError').show();
            isValid = false;
        }
        

        if(description.length > 200) {
            $('#descriptionError').show();
            isValid = false;
        }
        
        if(isValid) {

            let hobbiesArr = [];
            $('input[name="hobbies"]:checked').each(function() {
                hobbiesArr.push($(this).val());
            });
            let hobbies = hobbiesArr.join(', ');
            
            let visitor = {
                firstName: firstName,
                lastName: lastName,
                gender: $('#gender').val(),
                telephone: telephone,
                email: email,
                region: region,
                hobbies: hobbies,
                description: description
            };
            
            let visitorsStr = localStorage.getItem('visitors');
            let visitors = visitorsStr ? JSON.parse(visitorsStr) : [];
            visitors.push(visitor);
            localStorage.setItem('visitors', JSON.stringify(visitors));
            
            alert('Registered successfully!');
            $('#personForm')[0].reset();
            window.location.href = 'search.html';
        }
    });
});

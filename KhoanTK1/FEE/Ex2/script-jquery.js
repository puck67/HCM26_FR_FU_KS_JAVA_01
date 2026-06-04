$(document).ready(function() {
    function filterCards() {
        const query = $('#search-bar').val().toLowerCase();
        $('.card').each(function() {
            const text = $(this).find('span').text().toLowerCase();
            if (text.indexOf(query) !== -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    $('.board').on('click', '#todo-add-btn', function() {
        const val = $('#todo-input').val().trim();
        if (val) {
            const card = $('<div class="card"><span></span><button class="move-btn">Move</button></div>');
            card.find('span').text(val);
            card.appendTo('#todo-list');
            $('#todo-input').val('');
            filterCards();
        }
    });

    $('.board').on('click', '#progress-add-btn', function() {
        const val = $('#progress-input').val().trim();
        if (val) {
            const card = $('<div class="card"><span></span><button class="move-btn">Move</button></div>');
            card.find('span').text(val);
            card.appendTo('#progress-list');
            $('#progress-input').val('');
            filterCards();
        }
    });

    $('.board').on('click', '#done-add-btn', function() {
        const val = $('#done-input').val().trim();
        if (val) {
            const card = $('<div class="card"><span></span><button class="delete-btn">Delete</button></div>');
            card.find('span').text(val);
            card.appendTo('#done-list');
            $('#done-input').val('');
            filterCards();
        }
    });

    $('.board').on('click', '.move-btn', function() {
        const card = $(this).closest('.card');
        const listId = card.parent().attr('id');
        
        if (listId === 'todo-list') {
            card.appendTo('#progress-list');
        } else if (listId === 'progress-list') {
            card.appendTo('#done-list');
            $(this).remove();
            card.append('<button class="delete-btn">Delete</button>');
        }
        filterCards();
    });

    $('.board').on('click', '.delete-btn', function() {
        $(this).closest('.card').remove();
    });

    $('#search-bar').on('input', filterCards);
});

// Problem 03 - jQuery Implementation
$(document).ready(function() {

    // HTML template for a card
    function getCardHtml(title, isDone) {
        let btnHtml = isDone 
            ? `<button class="action-btn btn-delete">Delete ✘</button>`
            : `<button class="action-btn btn-move">Move ▸</button>`;

        return `
            <div class="task-card">
                <span class="task-title-label">Title</span>
                <div class="task-title">${title}</div>
                <div class="task-actions">
                    ${btnHtml}
                </div>
            </div>
        `;
    }

    // Add Task (Event delegation or binding on specific columns)
    $('.kanban-board').on('click', '.add-btn', function() {
        const col = $(this).closest('.kanban-col');
        const input = col.find('.task-input');
        const title = input.val().trim();
        const isDone = col.data('status') === 'done';
        
        if (title) {
            const cardHtml = getCardHtml(title, isDone);
            col.find('.cards-container').append(cardHtml);
            input.val(''); // Clear input
        }
    });

    // Support Enter key for adding tasks
    $('.kanban-board').on('keypress', '.task-input', function(e) {
        if (e.which === 13) { // 13 is Enter
            $(this).siblings('.add-btn').click();
        }
    });

    // Move Task logic (Event delegation)
    $('.kanban-board').on('click', '.btn-move', function() {
        const card = $(this).closest('.task-card');
        const currentCol = card.closest('.kanban-col');
        const status = currentCol.data('status');

        let nextColId;
        if (status === 'todo') {
            nextColId = '#col-inprogress';
        } else if (status === 'inprogress') {
            nextColId = '#col-done';
        }

        if (nextColId) {
            // Use jQuery's .appendTo() to move the element in the DOM
            card.appendTo($(nextColId).find('.cards-container'));
            
            // If it reached "Done", we need to change the button to Delete
            if (nextColId === '#col-done') {
                $(this).replaceWith(`<button class="action-btn btn-delete">Delete ✘</button>`);
            }
        }
    });

    // Delete Task logic (Event delegation)
    $('.kanban-board').on('click', '.btn-delete', function() {
        $(this).closest('.task-card').remove();
    });

    // Global Search Filter (Real-time)
    $('#searchInput').on('input', function() {
        const searchTerm = $(this).val().toLowerCase();

        // Loop through all task cards
        $('.task-card').each(function() {
            const title = $(this).find('.task-title').text().toLowerCase();
            
            // Use .show() and .hide() based on match
            if (title.indexOf(searchTerm) > -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

});

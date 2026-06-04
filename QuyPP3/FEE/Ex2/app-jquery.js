// Problem 03 - jQuery
$(document).ready(function () {

    $('.btn-add').on('click', function () {
        const $column = $(this).closest('.kanban-column');
        addTaskFromColumn($column);
    });

    $('.task-input').on('keydown', function (e) {
        if (e.key === 'Enter') {
            const $column = $(this).closest('.kanban-column');
            addTaskFromColumn($column);
        }
    }).on('input', function () {
        $(this).removeClass('error');
    });

    function addTaskFromColumn($column) {
        const $input = $column.find('.task-input');
        const taskText = $input.val().trim();

        if (taskText === "") {
            $input.addClass('error').focus();
            return;
        }

        const status = $column.data('status');
        let actionButtonHtml = '';

        if (status === 'done') {
            actionButtonHtml = '<button class="btn-delete">Delete</button>';
        } else {
            actionButtonHtml = '<button class="btn-move">Move</button>';
        }

        const taskCardHtml = `
            <div class="task-card">
                <span class="label-title">Title</span>
                <div class="task-title">${taskText}</div>
                <div class="card-actions">
                    ${actionButtonHtml}
                </div>
            </div>
        `;

        $column.find('.task-list').append(taskCardHtml);
        $input.val("");

        triggerRealtimeSearch();
    }

    // --- 2. Event Delegation ---
    const $board = $('#board-container');

    $board.on('click', '.btn-move', function () {
        const $card = $(this).closest('.task-card');
        const currentStatus = $(this).closest('.kanban-column').data('status');

        if (currentStatus === 'todo') {
            $card.appendTo($('#col-progress .task-list'));
        } else if (currentStatus === 'progress') {
            $card.appendTo($('#col-done .task-list'));
            $card.find('.card-actions').html('<button class="btn-delete">Delete</button>');
        }
    });

    $board.on('click', '.btn-delete', function () {
        $(this).closest('.task-card').remove();
    });

    // --- 3. Real-time Search ---
    $('#global-search').on('input', function () {
        triggerRealtimeSearch();
    });

    function triggerRealtimeSearch() {
        const query = $('#global-search').val().toLowerCase().trim();

        $('.task-card').each(function () {
            const taskTitle = $(this).find('.task-title').text().toLowerCase();

            if (taskTitle.indexOf(query) !== -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }
});

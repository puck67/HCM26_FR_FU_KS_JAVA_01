$(document).ready(function () {
    $('.column').each(function () {
        const $column = $(this);
        const $input = $column.find('.add-task-input');
        const $addBtn = $column.find('.add-task-btn');

        const handleAddTask = () => {
            const title = $input.val().trim();
            if (title) {
                createTaskCard($column, title);
                $input.val('');
            }
        };

        $addBtn.on('click', handleAddTask);
        $input.on('keypress', function (e) {
            if (e.which === 13) {
                handleAddTask();
            }
        });
    });

    function createTaskCard($column, title) {
        const colId = $column.data('column');

        let buttonsHTML = '';
        if (colId === 'todo') {
            buttonsHTML = `
                <button class="task-btn btn-primary">Start</button>
                <button class="task-btn">Edit</button>
                <button class="task-btn btn-move">Move ▾</button>
            `;
        } else if (colId === 'progress') {
            buttonsHTML = `
                <button class="task-btn">Edit</button>
                <button class="task-btn btn-move">Move ▾</button>
                <button class="task-btn btn-success">Done</button>
            `;
        } else if (colId === 'done') {
            buttonsHTML = `
                <button class="task-btn">Edit</button>
                <button class="task-btn">Archive</button>
                <button class="task-btn btn-delete">Delete</button>
            `;
        }

        const $card = $('<div>', { class: 'task-card' });

        $card.append($('<div>', { class: 'task-title-label', text: 'Title' }));
        $card.append($('<div>', { class: 'task-title', text: title }));

        const $actions = $('<div>', { class: 'task-actions' }).html(buttonsHTML);
        $card.append($actions);

        $column.find('.task-list').append($card);
    }

    $('.kanban-board').on('click', '.btn-move', function () {
        const $btn = $(this);
        const $card = $btn.closest('.task-card');
        const $currentColumn = $card.closest('.column');
        const colId = $currentColumn.data('column');

        let nextColId = '';
        if (colId === 'todo') {
            nextColId = 'progress';
        } else if (colId === 'progress') {
            nextColId = 'done';
        }

        if (nextColId) {
            const $nextList = $(`.column[data-column="${nextColId}"]`).find('.task-list');

            $card.appendTo($nextList);

            if (nextColId === 'progress') {
                $card.find('.task-actions').html(`
                    <button class="task-btn">Edit</button>
                    <button class="task-btn btn-move">Move ▾</button>
                    <button class="task-btn btn-success">Done</button>
                `);
            } else if (nextColId === 'done') {
                $card.find('.task-actions').html(`
                    <button class="task-btn">Edit</button>
                    <button class="task-btn">Archive</button>
                    <button class="task-btn btn-delete">Delete</button>
                `);
            }
        }
    });

    $('.kanban-board').on('click', '.btn-delete', function () {
        $(this).closest('.task-card').remove();
    });
    $('#global-search').on('input', function () {
        const searchText = $(this).val().toLowerCase();

        $('.task-card').each(function () {
            const taskTitle = $(this).find('.task-title').text().toLowerCase();

            if (taskTitle.includes(searchText)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});

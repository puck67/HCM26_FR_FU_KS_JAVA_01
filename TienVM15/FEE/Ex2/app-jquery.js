(function() {
    window.initJQuery = function() {
        // Target elements using jQuery selectors
        const $board = $('.kanban-board');
        const $searchInput = $('.search-input');
        
        // Clear all previous handlers
        $board.off('click');
        $('.btn-add-task').off('click');
        $('.column-input').off('keypress');
        $searchInput.off('input');

        // Reset columns
        $('.tasks-list').empty();
        checkAllEmptyStates();

        // 1. Add Task handler using jQuery
        $('.btn-add-task').on('click', function() {
            addTaskFromInput($(this));
        });

        $('.column-input').on('keypress', function(e) {
            if (e.which === 13) { // Enter key
                addTaskFromInput($(this).siblings('.btn-add-task'));
            }
        });

        function addTaskFromInput($btn) {
            const $input = $btn.siblings('.column-input');
            const text = $input.val().trim();
            if (text === '') return;

            const $column = $btn.closest('.kanban-column');
            const colType = getColumnType($column);

            createTaskJQuery(text, colType);
            $input.val('').focus();
        }

        function getColumnType($col) {
            if ($col.hasClass('todo')) return 'todo';
            if ($col.hasClass('progress')) return 'progress';
            return 'done';
        }

        // 2. Create and Append task using jQuery
        function createTaskJQuery(text, colType) {
            const $targetList = $(`.kanban-column.${colType} .tasks-list`);
            
            // Remove empty column placeholder
            $targetList.find('.empty-column-message').remove();

            // Create Sticky Note card markup using jQuery
            const $card = $('<div class="task-card">')
                .data('col-type', colType);

            const $titleLabel = $('<div class="task-title-label">').text('Title');
            const $titleText = $('<div class="task-title">').text(text);
            const $actionsDiv = $('<div class="task-actions">');

            $card.append($titleLabel).append($titleText).append($actionsDiv);
            
            setupActionsJQuery($card, colType);
            $card.appendTo($targetList);

            // Apply active search filter
            filterTasks();
        }

        function setupActionsJQuery($card, colType) {
            const $actionsDiv = $card.find('.task-actions').empty();

            if (colType === 'done') {
                const $btnDelete = $('<button type="button" class="btn-task-action delete">')
                    .html('🗑 Delete');
                $actionsDiv.append($btnDelete);
            } else {
                const $btnMove = $('<button type="button" class="btn-task-action move">')
                    .html('Move ➔');
                $actionsDiv.append($btnMove);
            }
        }

        // 3. Event Delegation on the board container for Action buttons (Problem 03 specification)
        $board.on('click', '.btn-task-action.move', function() {
            const $card = $(this).closest('.task-card');
            const currentColType = $card.data('col-type');
            
            let nextColType = 'progress';
            if (currentColType === 'progress') {
                nextColType = 'done';
            }

            const $currentList = $card.parent();
            const $nextList = $(`.kanban-column.${nextColType} .tasks-list`);

            // Use jQuery's .appendTo() to move elements effectively (Problem 03 specification)
            $nextList.find('.empty-column-message').remove();
            $card.data('col-type', nextColType).appendTo($nextList);

            // Re-setup actions depending on the new column type
            setupActionsJQuery($card, nextColType);

            // Check empty state of the source list
            checkListEmptyState($currentList);
        });

        $board.on('click', '.btn-task-action.delete', function() {
            const $card = $(this).closest('.task-card');
            const $list = $card.parent();
            
            // Remove using jQuery's .remove()
            $card.remove();
            checkListEmptyState($list);
        });

        // 4. Real-time Search using jQuery's .hide() and .show() (Problem 03 specification)
        $searchInput.on('input', function() {
            filterTasks();
        });

        function filterTasks() {
            const query = $searchInput.val().toLowerCase().trim();
            $('.task-card').each(function() {
                const titleText = $(this).find('.task-title').text().toLowerCase();
                if (titleText.indexOf(query) !== -1) {
                    $(this).show(); // Use jQuery .show()
                } else {
                    $(this).hide(); // Use jQuery .hide()
                }
            });
        }

        // Empty state helpers
        function checkListEmptyState($list) {
            if ($list.children('.task-card').length === 0) {
                if ($list.find('.empty-column-message').length === 0) {
                    $('<div class="empty-column-message">')
                        .text('No tasks in this list')
                        .appendTo($list);
                }
            }
        }

        function checkAllEmptyStates() {
            $('.tasks-list').each(function() {
                checkListEmptyState($(this));
            });
        }
    };
})();

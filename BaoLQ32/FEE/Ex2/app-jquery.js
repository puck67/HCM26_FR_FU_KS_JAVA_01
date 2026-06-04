$(document).ready(function() {
    // Selectors
    var $board = $('#kanban-board');
    var $searchTasks = $('#search-tasks');

    // Prepopulate board matching specification mockup
    addCard("Website Homepage Redesign", "#cards-todo");
    addCard("Market Research Report", "#cards-todo");
    addCard("User Testing Sessions", "#cards-progress");
    addCard("Completed Features List", "#cards-done");

    // 1. Add Task Event Delegation
    // Handle Add click on '+' buttons
    $board.on('click', '.btn-add-task', function() {
        var $input = $(this).siblings('.task-input');
        var taskText = $.trim($input.val());
        var columnSelector = '#' + $(this).closest('.kanban-column').find('.cards-list').attr('id');
        
        if (taskText) {
            addCard(taskText, columnSelector);
            $input.val('');
            triggerSearchFilter(); // Re-apply search filter to newly added card
        }
    });

    // Handle Enter keypress inside input fields
    $board.on('keydown', '.task-input', function(e) {
        if (e.key === 'Enter') {
            var taskText = $.trim($(this).val());
            var columnSelector = '#' + $(this).closest('.kanban-column').find('.cards-list').attr('id');

            if (taskText) {
                addCard(taskText, columnSelector);
                $(this).val('');
                triggerSearchFilter();
            }
        }
    });

    // 2. Action Event Delegation (Edit, Start, Move, Done, Archive, Delete)
    // Edit task title
    $board.on('click', '.btn-card-edit', function() {
        var $card = $(this).closest('.task-card');
        var $titleEl = $card.find('.card-title');
        var newTitleText = prompt("Edit task name:", $titleEl.text());
        if (newTitleText && $.trim(newTitleText) !== '') {
            $titleEl.text($.trim(newTitleText));
        }
    });

    // Start / Move (moves To Do -> In Progress)
    $board.on('click', '.btn-card-start, .btn-card-todo-move', function() {
        var $card = $(this).closest('.task-card');
        // DOM Movement requirement: Use appendTo()
        $card.appendTo('#cards-progress');
        updateCardControls($card, 'cards-progress');
    });

    // Done / Move (moves In Progress -> Done)
    $board.on('click', '.btn-card-done, .btn-card-progress-move', function() {
        var $card = $(this).closest('.task-card');
        // DOM Movement requirement: Use appendTo()
        $card.appendTo('#cards-done');
        updateCardControls($card, 'cards-done');
    });

    // Delete task from Done
    $board.on('click', '.btn-card-delete', function() {
        var $card = $(this).closest('.task-card');
        $card.remove();
    });

    // Archive task from Done (Optional action)
    $board.on('click', '.btn-card-archive', function() {
        var $card = $(this).closest('.task-card');
        if (confirm("Archive this completed task? It will be removed from the board.")) {
            $card.remove();
        }
    });

    // 3. Real-time Search Input Filter
    $searchTasks.on('input keyup', function() {
        triggerSearchFilter();
    });

    // Filter implementation using hide() and show()
    function triggerSearchFilter() {
        var query = $.trim($searchTasks.val()).toLowerCase();
        $('.task-card').each(function() {
            var titleText = $(this).find('.card-title').text().toLowerCase();
            if (titleText.indexOf(query) !== -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    // Helper: Add a card to the target column
    function addCard(titleText, columnSelector) {
        var columnId = columnSelector.replace('#', '');
        var actionsHtml = getActionsHtml(columnId);

        var cardHtml = '<div class="task-card">' +
            '<div class="card-label">Title</div>' +
            '<div class="card-title">' + escapeHtml(titleText) + '</div>' +
            '<div class="card-actions">' + actionsHtml + '</div>' +
        '</div>';

        $(columnSelector).append(cardHtml);
    }

    // Helper: Update card buttons when a card is moved to a new column
    function updateCardControls($card, newColumnId) {
        var actionsHtml = getActionsHtml(newColumnId);
        $card.find('.card-actions').html(actionsHtml);
    }

    // Helper: Generate buttons HTML corresponding to current column
    function getActionsHtml(columnId) {
        var editBtn = '<button type="button" class="btn-card btn-card-edit"><i class="fa-regular fa-pen-to-square"></i> Edit</button>';
        
        if (columnId === 'cards-todo') {
            return '<button type="button" class="btn-card btn-card-start"><i class="fa-solid fa-play"></i> Start</button>' +
                   editBtn +
                   '<button type="button" class="btn-card btn-card-todo-move"><i class="fa-solid fa-arrow-right"></i> Move</button>';
        } else if (columnId === 'cards-progress') {
            return editBtn +
                   '<button type="button" class="btn-card btn-card-progress-move"><i class="fa-solid fa-arrow-right"></i> Move</button>' +
                   '<button type="button" class="btn-card btn-card-done"><i class="fa-solid fa-check"></i> Done</button>';
        } else if (columnId === 'cards-done') {
            // Replacement: No "Move" button here, replaced by "Delete"
            return editBtn +
                   '<button type="button" class="btn-card btn-card-archive"><i class="fa-solid fa-box-archive"></i> Archive</button>' +
                   '<button type="button" class="btn-card btn-card-delete"><i class="fa-regular fa-trash-can"></i> Delete</button>';
        }
        return '';
    }

    // Simple security escaping helper
    function escapeHtml(text) {
        return text
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
});

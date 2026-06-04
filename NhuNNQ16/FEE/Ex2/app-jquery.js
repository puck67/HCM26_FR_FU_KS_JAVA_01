

$(document).ready(function() {

    /**
     * Create a task card element using jQuery
     * @param {string} text - The title of the task
     * @param {string} columnId - The target column ID
     * @returns {jQuery} - The jQuery object containing the new task card HTML
     */
    function updateCardButtons($card, columnId) {
        const $actions = $card.find('.card-actions');
        $actions.empty();

        if (columnId === 'todo-list') {
            $actions.append('<button class="start-btn">Start</button>');
            $actions.append('<button class="edit-btn">Edit</button>');
            $actions.append('<button class="move-btn">Move</button>');
        } else if (columnId === 'inprogress-list') {
            $actions.append('<button class="edit-btn">Edit</button>');
            $actions.append('<button class="move-btn">Move</button>');
            $actions.append('<button class="done-btn">Done</button>');
        } else if (columnId === 'done-list') {
            $actions.append('<button class="edit-btn">Edit</button>');
            $actions.append('<button class="archive-btn">Archive</button>');
            $actions.append('<button class="delete-btn">Delete</button>');
        }
    }

    function createTaskCard(text, columnId) {
        const cardHtml = `
            <div class="task-card">
                <span class="task-title"></span>
                <div class="card-actions"></div>
            </div>
        `;
        const $card = $(cardHtml);
        $card.find('.task-title').text(text);
        updateCardButtons($card, columnId);
        return $card;
    }

    /**
     * Set up form handler for task submission using jQuery
     * @param {string} formId - CSS Selector of the form element
     * @param {string} inputId - CSS Selector of the text input element
     * @param {string} listId - CSS Selector of the target column list element
     * @param {string} columnId - Plain ID of the target column list
     */
    function setupFormHandler(formId, inputId, listId, columnId) {
        $(formId).on('submit', function(event) {
            event.preventDefault();

            const $input = $(inputId);
            const taskText = $input.val().trim();

            if (taskText) {

                const $card = createTaskCard(taskText, columnId);
                $(listId).append($card);

                $input.val('');

                filterTasks($('#global-search').val());
            }
        });
    }

    // Initialize forms for all three columns
    setupFormHandler('#todo-form', '#todo-input', '#todo-list', 'todo-list');
    setupFormHandler('#inprogress-form', '#inprogress-input', '#inprogress-list', 'inprogress-list');
    setupFormHandler('#done-form', '#done-input', '#done-list', 'done-list');


    // 1. Start button delegation
    $('#board').on('click', '.start-btn', function() {
        const $card = $(this).closest('.task-card');
        $card.appendTo('#inprogress-list');
        updateCardButtons($card, 'inprogress-list');
        filterTasks($('#global-search').val());
    });

    // 2. Edit button delegation
    $('#board').on('click', '.edit-btn', function() {
        const $card = $(this).closest('.task-card');
        const $title = $card.find('.task-title');
        const newTitle = prompt('Edit task title:', $title.text());
        if (newTitle !== null && newTitle.trim() !== '') {
            $title.text(newTitle.trim());
        }
    });

    // 3. Move button delegation
    $('#board').on('click', '.move-btn', function() {
        const $card = $(this).closest('.task-card');
        const $currentColumn = $card.closest('.column-list');

        if ($currentColumn.attr('id') === 'todo-list') {
            $card.appendTo('#inprogress-list');
            updateCardButtons($card, 'inprogress-list');
        } else if ($currentColumn.attr('id') === 'inprogress-list') {
            $card.appendTo('#done-list');
            updateCardButtons($card, 'done-list');
        }
        filterTasks($('#global-search').val());
    });

    // 4. Done button delegation
    $('#board').on('click', '.done-btn', function() {
        const $card = $(this).closest('.task-card');
        $card.appendTo('#done-list');
        updateCardButtons($card, 'done-list');
        filterTasks($('#global-search').val());
    });

    // 5. Archive button delegation
    $('#board').on('click', '.archive-btn', function() {
        const $card = $(this).closest('.task-card');
        const taskText = $card.find('.task-title').text();
        addArchivedTask(taskText);
        $card.remove();
    });

    // 6. Delete button delegation
    $('#board').on('click', '.delete-btn', function() {
        $(this).closest('.task-card').remove();
    });


    // Listen to changes in the search input
    $('#global-search').on('input', function() {
        const query = $(this).val();
        filterTasks(query);
    });

    /**
     * @param {string} query - The search string
     */
    function filterTasks(query) {
        const cleanQuery = query.toLowerCase().trim();
        $('.task-card').each(function() {
            const cardText = $(this).find('.task-title').text().toLowerCase();
            if (cardText.indexOf(cleanQuery) !== -1) {
                $(this).show(); // Using jQuery's .show() method
            } else {
                $(this).hide(); // Using jQuery's .hide() method
            }
        });
    }

    // Archive Logic using jQuery
    function getArchivedTasks() {
        const stored = localStorage.getItem('kanban_archived_tasks');
        return stored ? JSON.parse(stored) : [];
    }

    function saveArchivedTasks(tasks) {
        localStorage.setItem('kanban_archived_tasks', JSON.stringify(tasks));
    }

    function renderArchiveList() {
        const tasks = getArchivedTasks();
        $('#archive-count').text(tasks.length);
        const $archiveList = $('#archive-list');
        $archiveList.empty();

        if (tasks.length === 0) {
            $archiveList.append('<div class="empty-message" style="text-align: center; color: #a0aec0; padding: 20px 0;">No archived tasks</div>');
            return;
        }

        tasks.forEach((taskText, index) => {
            const $card = $(`
                <div class="archived-card">
                    <span class="task-title"></span>
                    <div class="card-actions">
                        <button class="restore-btn">Restore</button>
                        <button class="delete-btn">Delete</button>
                    </div>
                </div>
            `);
            $card.find('.task-title').text(taskText);

            // Bind restore action
            $card.find('.restore-btn').on('click', function() {
                // Restore to Done column
                const $restoredCard = createTaskCard(taskText, 'done-list');
                $('#done-list').append($restoredCard);

                // Remove from archive
                tasks.splice(index, 1);
                saveArchivedTasks(tasks);
                renderArchiveList();
            });

            // Bind delete action
            $card.find('.delete-btn').on('click', function() {
                tasks.splice(index, 1);
                saveArchivedTasks(tasks);
                renderArchiveList();
            });

            $archiveList.append($card);
        });
    }

    function addArchivedTask(taskText) {
        const tasks = getArchivedTasks();
        tasks.push(taskText);
        saveArchivedTasks(tasks);
        renderArchiveList();
    }

    // Toggle Drawer Events
    $('#view-archive-btn').on('click', function() {
        $('#archive-drawer').addClass('open');
        $('#drawer-overlay').addClass('show');
    });

    $('#close-archive-btn, #drawer-overlay').on('click', function() {
        $('#archive-drawer').removeClass('open');
        $('#drawer-overlay').removeClass('show');
    });

    // Initial load
    renderArchiveList();

});

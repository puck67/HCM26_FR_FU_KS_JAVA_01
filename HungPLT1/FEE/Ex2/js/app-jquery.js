/**
 * FEE - Exercise 2: Kanban Board Lite (jQuery Refactored Version)
 * Author: HungPLT1
 * Description: Manages task creation, DOM movement, and deletion using jQuery APIs.
 *              Includes event delegation and real-time title search filtering.
 */

$(document).ready(function() {
    // Column messages for empty state placeholders
    const emptyMessages = {
        todo: 'Không có công việc cần làm',
        inprogress: 'Không có công việc đang thực hiện',
        done: 'Không có công việc đã hoàn thành'
    };

    /**
     * Updates badge counts and toggles placeholders for each column
     */
    function updateColumnStates() {
        ['todo', 'inprogress', 'done'].forEach(col => {
            const list = $(`#list-${col}`);
            const cardCount = list.children('.kanban-card').length;
            
            // Update the counter badge
            $(`#count-${col}`).text(cardCount);
            
            // Handle placeholder logic
            const placeholder = list.children('.no-tasks');
            if (cardCount === 0) {
                if (placeholder.length === 0) {
                    list.append(`<div class="no-tasks">${emptyMessages[col]}</div>`);
                }
            } else {
                placeholder.remove();
            }
        });
    }

    /**
     * Filters all Kanban cards based on the global search input
     */
    function applySearchFilter() {
        const query = $('#global-search').val().toLowerCase().trim();
        $('.kanban-card').each(function() {
            const cardTitle = $(this).find('.card-title').text().toLowerCase();
            if (cardTitle.includes(query)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    /**
     * Helper to create a card element using jQuery
     * @param {string} titleText - The title of the task
     * @param {string} colName - Column name ('todo', 'inprogress', 'done')
     */
    function createCard(titleText, colName) {
        const taskId = Date.now().toString().slice(-4);
        
        // Construct the card HTML
        const card = $(`
            <div class="kanban-card">
                <div class="card-meta">Task #${taskId}</div>
                <div class="card-title"></div>
                <div class="card-actions">
                    <button class="btn-card btn-edit">Edit</button>
                </div>
            </div>
        `);
        
        // Use text() to safely escape potential HTML in title
        card.find('.card-title').text(titleText);
        
        // Append appropriate action button based on the column
        if (colName === 'done') {
            card.find('.card-actions').append('<button class="btn-card btn-delete">Delete</button>');
        } else {
            card.find('.card-actions').append('<button class="btn-card btn-move">Move</button>');
        }
        
        return card;
    }

    function triggerInputError(inputEl) {
        inputEl.addClass('input-error');
        inputEl.focus();
        setTimeout(function() {
            inputEl.removeClass('input-error');
        }, 300);
    }

    /**
     * Add Task handler
     * @param {jQuery} inputEl - The input element
     * @param {jQuery} listEl - The destination list element
     * @param {string} colName - Column identifier
     */
    function addTask(inputEl, listEl, colName) {
        const text = inputEl.val().trim();
        
        // Validation 1: Rỗng hoặc chứa toàn khoảng trắng
        if (text === '') {
            triggerInputError(inputEl);
            return;
        }

        // Validation 2: Tên nhiệm vụ quá dài (tối đa 100 ký tự)
        if (text.length > 100) {
            alert('Tên công việc quá dài (tối đa 100 ký tự)!');
            triggerInputError(inputEl);
            return;
        }
        
        const card = createCard(text, colName);
        listEl.append(card);
        
        // Clear input
        inputEl.val('');
        
        // Update column counters and check empty states
        updateColumnStates();
        
        // Re-apply search filter if user is actively searching
        applySearchFilter();
    }

    // ==========================================================================
    // Event Handlers for Adding Tasks (Click and Enter Key)
    // ==========================================================================
    
    // To Do Column Add
    $('#btn-add-todo').on('click', function() {
        addTask($('#input-todo'), $('#list-todo'), 'todo');
    });
    $('#input-todo').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-todo'), $('#list-todo'), 'todo');
        }
    });

    // In Progress Column Add
    $('#btn-add-inprogress').on('click', function() {
        addTask($('#input-inprogress'), $('#list-inprogress'), 'inprogress');
    });
    $('#input-inprogress').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-inprogress'), $('#list-inprogress'), 'inprogress');
        }
    });

    // Done Column Add
    $('#btn-add-done').on('click', function() {
        addTask($('#input-done'), $('#list-done'), 'done');
    });
    $('#input-done').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-done'), $('#list-done'), 'done');
        }
    });

    // ==========================================================================
    // Event Delegation on Board Container (Problem 3 spec constraint)
    // ==========================================================================
    
    // Delegate card movement click
    $('#kanban-board').on('click', '.btn-move', function(e) {
        e.stopPropagation();
        const card = $(this).closest('.kanban-card');
        const currentList = card.parent();
        
        if (currentList.attr('id') === 'list-todo') {
            // Move from To Do to In Progress using .appendTo()
            card.appendTo('#list-inprogress');
        } else if (currentList.attr('id') === 'list-inprogress') {
            // Move from In Progress to Done using .appendTo()
            card.appendTo('#list-done');
            
            // Replace Move button with Delete button
            card.find('.btn-move').remove();
            card.find('.card-actions').append('<button class="btn-card btn-delete">Delete</button>');
        }
        
        updateColumnStates();
        applySearchFilter(); // Update search visibility for moved card
    });

    // Delegate card deletion click
    $('#kanban-board').on('click', '.btn-delete', function(e) {
        e.stopPropagation();
        const card = $(this).closest('.kanban-card');
        card.remove();
        updateColumnStates();
    });

    // Delegate edit action click
    $('#kanban-board').on('click', '.btn-edit', function(e) {
        e.stopPropagation();
        const btn = $(this);
        const card = btn.closest('.kanban-card');
        const titleEl = card.find('.card-title');
        
        // Enter edit mode
        btn.text('Save').removeClass('btn-edit').addClass('btn-save');
        btn.css({
            'background-color': 'var(--done-accent)',
            'color': 'white'
        });
        
        const currentText = titleEl.text();
        const editInput = $('<input type="text" class="card-edit-input">').val(currentText);
        
        titleEl.empty().append(editInput);
        editInput.focus();
        
        // Press Enter to trigger save (clicks the Save button)
        editInput.on('keydown', function(evt) {
            if (evt.key === 'Enter') {
                evt.preventDefault();
                evt.stopPropagation();
                btn.click();
            }
        });
    });

    // Delegate save action click
    $('#kanban-board').on('click', '.btn-save', function(e) {
        e.stopPropagation();
        const btn = $(this);
        const card = btn.closest('.kanban-card');
        const titleEl = card.find('.card-title');
        const editInput = titleEl.find('.card-edit-input');
        
        const newText = editInput.val().trim();
        
        // Validation
        if (newText === '') {
            triggerInputError(editInput);
            return;
        }
        if (newText.length > 100) {
            alert('Tên công việc quá dài (tối đa 100 ký tự)!');
            triggerInputError(editInput);
            return;
        }
        
        titleEl.text(newText);
        btn.text('Edit').removeClass('btn-save').addClass('btn-edit');
        btn.css({
            'background-color': '',
            'color': ''
        });
        
        applySearchFilter(); // Re-apply search in case title changed
    });

    // ==========================================================================
    // Real-time Lọc Tìm Kiếm (Problem 3 spec constraint)
    // ==========================================================================
    $('#global-search').on('input', function() {
        applySearchFilter();
    });

    // Initial setup
    updateColumnStates();
});

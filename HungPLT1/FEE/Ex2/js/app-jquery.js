$(document).ready(function() {
    const emptyMessages = {
        todo: 'Không có công việc cần làm',
        inprogress: 'Không có công việc đang thực hiện',
        done: 'Không có công việc đã hoàn thành'
    };

    function updateColumnStates() {
        ['todo', 'inprogress', 'done'].forEach(col => {
            const list = $(`#list-${col}`);
            const cardCount = list.children('.kanban-card').length;
            
            $(`#count-${col}`).text(cardCount);
            
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

    function createCard(titleText, colName) {
        const taskId = Date.now().toString().slice(-4);
        
        const card = $(`
            <div class="kanban-card">
                <div class="card-meta">Task #${taskId}</div>
                <div class="card-title"></div>
                <div class="card-actions">
                    <button class="btn-card btn-edit">Edit</button>
                </div>
            </div>
        `);
        
        card.find('.card-title').text(titleText);
        
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

    function addTask(inputEl, listEl, colName) {
        const text = inputEl.val().trim();
        
        if (text === '') {
            triggerInputError(inputEl);
            return;
        }

        if (text.length > 100) {
            alert('Tên công việc quá dài (tối đa 100 ký tự)!');
            triggerInputError(inputEl);
            return;
        }
        
        const card = createCard(text, colName);
        listEl.append(card);
        
        inputEl.val('');
        
        updateColumnStates();
        applySearchFilter();
    }
    
    $('#btn-add-todo').on('click', function() {
        addTask($('#input-todo'), $('#list-todo'), 'todo');
    });
    $('#input-todo').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-todo'), $('#list-todo'), 'todo');
        }
    });

    $('#btn-add-inprogress').on('click', function() {
        addTask($('#input-inprogress'), $('#list-inprogress'), 'inprogress');
    });
    $('#input-inprogress').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-inprogress'), $('#list-inprogress'), 'inprogress');
        }
    });

    $('#btn-add-done').on('click', function() {
        addTask($('#input-done'), $('#list-done'), 'done');
    });
    $('#input-done').on('keydown', function(e) {
        if (e.key === 'Enter') {
            addTask($('#input-done'), $('#list-done'), 'done');
        }
    });
    
    $('#kanban-board').on('click', '.btn-move', function(e) {
        e.stopPropagation();
        const card = $(this).closest('.kanban-card');
        const currentList = card.parent();
        
        if (currentList.attr('id') === 'list-todo') {
            card.appendTo('#list-inprogress');
        } else if (currentList.attr('id') === 'list-inprogress') {
            card.appendTo('#list-done');
            
            card.find('.btn-move').remove();
            card.find('.card-actions').append('<button class="btn-card btn-delete">Delete</button>');
        }
        
        updateColumnStates();
        applySearchFilter();
    });

    $('#kanban-board').on('click', '.btn-delete', function(e) {
        e.stopPropagation();
        const card = $(this).closest('.kanban-card');
        card.remove();
        updateColumnStates();
    });

    $('#kanban-board').on('click', '.btn-edit', function(e) {
        e.stopPropagation();
        const btn = $(this);
        const card = btn.closest('.kanban-card');
        const titleEl = card.find('.card-title');
        
        btn.text('Save').removeClass('btn-edit').addClass('btn-save');
        btn.css({
            'background-color': 'var(--done-accent)',
            'color': 'white'
        });
        
        const currentText = titleEl.text();
        const editInput = $('<input type="text" class="card-edit-input">').val(currentText);
        
        titleEl.empty().append(editInput);
        editInput.focus();
        
        editInput.on('keydown', function(evt) {
            if (evt.key === 'Enter') {
                evt.preventDefault();
                evt.stopPropagation();
                btn.click();
            }
        });
    });

    $('#kanban-board').on('click', '.btn-save', function(e) {
        e.stopPropagation();
        const btn = $(this);
        const card = btn.closest('.kanban-card');
        const titleEl = card.find('.card-title');
        const editInput = titleEl.find('.card-edit-input');
        
        const newText = editInput.val().trim();
        
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
        
        applySearchFilter();
    });

    $('#global-search').on('input', function() {
        applySearchFilter();
    });

    updateColumnStates();
});

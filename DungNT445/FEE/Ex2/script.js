$(document).ready(function() {
    
    // Function to create a task card HTML string
    function createTaskCard(taskName, isDone) {
        let buttonsHtml = '';
        if (isDone) {
            buttonsHtml = `<button class="btn btn-delete">Delete</button>`;
        } else {
            buttonsHtml = `<button class="btn btn-move">Move &rarr;</button>`;
        }
        
        // Include an Edit button for bonus UX (like in the UI mockup)
        return `
            <div class="task-card">
                <div class="task-title">Title</div>
                <div class="task-name">${taskName}</div>
                <div class="task-actions">
                    <button class="btn btn-edit">Edit</button>
                    ${buttonsHtml}
                </div>
            </div>
        `;
    }

    // Add Task via Button Click
    $('.add-btn').on('click', function() {
        let inputField = $(this).siblings('.task-input');
        let taskName = inputField.val().trim();
        let columnList = $(this).closest('.column').find('.task-list');
        
        // Validation: Ensure input is not empty before adding
        if (taskName) {
            let isDone = columnList.attr('id') === 'doneList';
            let cardHtml = createTaskCard(taskName, isDone);
            columnList.append(cardHtml);
            inputField.val(''); // Clear input
        } else {
            alert("Vui lòng nhập tên công việc!");
        }
    });

    // Add Task via Enter Key
    $('.task-input').on('keypress', function(e) {
        if (e.which === 13) { // Enter key
            $(this).siblings('.add-btn').click();
        }
    });

    // Event Delegation for Move button
    $('#kanbanBoard').on('click', '.btn-move', function() {
        let card = $(this).closest('.task-card');
        let currentListId = card.closest('.task-list').attr('id');
        
        if (currentListId === 'todoList') {
            card.appendTo('#inProgressList');
        } else if (currentListId === 'inProgressList') {
            card.appendTo('#doneList');
            // Change Move button to Delete button
            $(this).replaceWith(`<button class="btn btn-delete">Delete</button>`);
        }
    });

    // Event Delegation for Delete button
    $('#kanbanBoard').on('click', '.btn-delete', function() {
        if (confirm("Bạn có chắc chắn muốn xóa công việc này?")) {
            $(this).closest('.task-card').remove();
        }
    });

    // Event Delegation for Edit button (Bonus feature based on UI Mockup)
    $('#kanbanBoard').on('click', '.btn-edit', function() {
        let card = $(this).closest('.task-card');
        let taskNameElement = card.find('.task-name');
        let currentName = taskNameElement.text();
        
        let newName = prompt("Chỉnh sửa tên công việc:", currentName);
        if (newName !== null && newName.trim() !== '') {
            taskNameElement.text(newName.trim());
        }
    });

    // Real-time Search functionality
    $('#searchInput').on('input', function() {
        let searchText = $(this).val().toLowerCase();
        
        $('.task-card').each(function() {
            let taskName = $(this).find('.task-name').text().toLowerCase();
            if (taskName.includes(searchText)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });
});

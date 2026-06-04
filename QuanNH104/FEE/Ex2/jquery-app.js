$(document).ready(function() {
    // Board container dùng cho Event Delegation
    const $board = $('.kanban-board');

    // Hàm cập nhật và đồng bộ các nút của card tương ứng với cột hiện tại
    function updateCardButtons($card) {
        const $column = $card.closest('.kanban-column');
        const $actions = $card.find('.card-actions');
        
        if ($column.hasClass('col-todo')) {
            $actions.html(`
                <button class="btn btn-blue">Start</button>
                <button class="btn btn-outline">Edit</button>
                <button class="btn btn-outline btn-move">Move ▾</button>
            `);
        } else if ($column.hasClass('col-progress')) {
            $actions.html(`
                <button class="btn btn-outline">Edit</button>
                <button class="btn btn-outline btn-move">Move ▾</button>
                <button class="btn btn-green">Done</button>
            `);
        } else if ($column.hasClass('col-done')) {
            $actions.html(`
                <button class="btn btn-outline">Edit</button>
                <button class="btn btn-outline">Archive</button>
                <button class="btn btn-red btn-delete">Delete</button>
            `);
        }
    }

    // 1. Quét và đồng bộ các nút của các card ban đầu dựa trên cột hiện tại của chúng
    $('.kanban-card').each(function() {
        updateCardButtons($(this));
    });

    // 2. Thêm Task mới (Add Task)
    function addTask($inputContainer) {
        const $input = $inputContainer.find('input');
        const taskText = $input.val().trim();
        if (taskText === '') return;

        const $column = $inputContainer.closest('.kanban-column');
        const $cardsContainer = $column.find('.cards-container');
        
        const newCardHTML = `
            <div class="kanban-card">
                <span class="card-label">Title</span>
                <h3 class="card-title"></h3>
                <div class="card-actions"></div>
            </div>
        `;

        const $newCard = $(newCardHTML);
        $newCard.find('.card-title').text(taskText); // Đảm bảo an toàn XSS

        // Append vào cột
        $cardsContainer.append($newCard);
        
        // Tạo các nút tương ứng với cột ban đầu
        updateCardButtons($newCard);

        $input.val(''); // Xóa nội dung input

        // Cập nhật tìm kiếm thời gian thực nếu đang gõ
        triggerSearch();
    }

    // Event Delegation: Nhấn nút Add
    $board.on('click', '.btn-add', function() {
        addTask($(this).parent());
    });

    // Event Delegation: Nhấn phím Enter trong input
    $board.on('keypress', '.input-container input', function(e) {
        if (e.which === 13) {
            addTask($(this).parent());
        }
    });

    // Event Delegation: Di chuyển task sang phải (Move Right)
    $board.on('click', '.btn-move', function() {
        const $card = $(this).closest('.kanban-card');
        const $currentColumn = $card.closest('.kanban-column');
        
        let $nextColumn = null;
        if ($currentColumn.hasClass('col-todo')) {
            $nextColumn = $('.col-progress');
        } else if ($currentColumn.hasClass('col-progress')) {
            $nextColumn = $('.col-done');
        }

        if ($nextColumn && $nextColumn.length > 0) {
            const $targetContainer = $nextColumn.find('.cards-container');
            // DOM Movement: Sử dụng appendTo() để di chuyển
            $card.appendTo($targetContainer);
            // Đồng bộ lại bộ nút tương ứng với cột mới
            updateCardButtons($card);
        }
    });

    // Event Delegation: Xóa task (Delete)
    $board.on('click', '.btn-delete', function() {
        const $card = $(this).closest('.kanban-card');
        $card.fadeOut(200, function() {
            $(this).remove();
        });
    });

    // Event Delegation: Chỉnh sửa tiêu đề Task (Edit)
    $board.on('click', 'button:contains("Edit")', function() {
        const $card = $(this).closest('.kanban-card');
        const $title = $card.find('.card-title');
        const currentTitle = $title.text();
        const newTitle = prompt("Nhập tiêu đề mới cho task:", currentTitle);
        
        if (newTitle !== null && newTitle.trim() !== '') {
            $title.text(newTitle.trim());
        }
    });

    // Event Delegation: Bắt đầu thực hiện (Start) -> Di chuyển nhanh sang In Progress
    $board.on('click', 'button:contains("Start")', function() {
        const $card = $(this).closest('.kanban-card');
        const $targetContainer = $('.col-progress .cards-container');
        if ($targetContainer.length > 0) {
            $card.appendTo($targetContainer);
            updateCardButtons($card);
        }
    });

    // Event Delegation: Hoàn thành (Done) -> Di chuyển nhanh sang Done
    $board.on('click', 'button:contains("Done")', function() {
        const $card = $(this).closest('.kanban-card');
        const $targetContainer = $('.col-done .cards-container');
        if ($targetContainer.length > 0) {
            $card.appendTo($targetContainer);
            updateCardButtons($card);
        }
    });

    // Event Delegation: Lưu trữ (Archive) -> Ẩn thẻ task
    $board.on('click', 'button:contains("Archive")', function() {
        const $card = $(this).closest('.kanban-card');
        $card.fadeOut(200, function() {
            $(this).remove();
        });
    });

    // 3. Tìm kiếm thời gian thực (Real-time Search)
    const $searchInput = $('#global-search');
    
    function triggerSearch() {
        const query = $searchInput.val().toLowerCase().trim();
        
        $('.kanban-card').each(function() {
            const cardTitleText = $(this).find('.card-title').text().toLowerCase();
            if (cardTitleText.indexOf(query) !== -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    $searchInput.on('input', function() {
        triggerSearch();
    });
});


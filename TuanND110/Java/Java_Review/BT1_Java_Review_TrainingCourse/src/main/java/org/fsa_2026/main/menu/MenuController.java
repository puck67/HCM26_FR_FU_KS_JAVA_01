package org.fsa_2026.main.menu;

import org.fsa_2026.main.handler.AddingHandler;
import org.fsa_2026.main.handler.DeletingHandler;
import org.fsa_2026.main.handler.InformationHandler;
import org.fsa_2026.main.handler.SearchingHandler;
import org.fsa_2026.main.handler.UpdatingHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class MenuController {
    private final Scanner scanner;

    // Lưu trữ các hành động dưới dạng Lambda để gọi chạy linh hoạt
    private final Map<String, Consumer<Scanner>> addingActions = new HashMap<>();
    private final Map<String, Consumer<Scanner>> searchingActions = new HashMap<>();
    private final Map<String, Consumer<Scanner>> updatingActions = new HashMap<>();
    private final Map<String, Consumer<Scanner>> deletingActions = new HashMap<>();
    private final Map<String, Runnable> infoActions = new HashMap<>();

    public MenuController(
            Scanner scanner,
            AddingHandler addingHandler,
            SearchingHandler searchingHandler,
            UpdatingHandler updatingHandler,
            DeletingHandler deletingHandler,
            InformationHandler informationHandler
    ) {
        this.scanner = scanner;

        // Đăng ký Lambda Expressions / Method References cho menu Thêm mới
        addingActions.put("1", addingHandler::addNewCourse);
        addingActions.put("2", addingHandler::addNewLearner);
        addingActions.put("3", addingHandler::addNewTrainer);

        // Đăng ký cho menu Tìm kiếm
        searchingActions.put("1", searchingHandler::searchCourse);
        searchingActions.put("2", searchingHandler::searchLearner);
        searchingActions.put("3", searchingHandler::searchTrainer);

        // Đăng ký cho menu Cập nhật
        updatingActions.put("1", updatingHandler::updateCourse);
        updatingActions.put("2", updatingHandler::updateLearner);
        updatingActions.put("3", updatingHandler::updateTrainer);

        // Đăng ký cho menu Xóa
        deletingActions.put("1", deletingHandler::deleteCourse);
        deletingActions.put("2", deletingHandler::deleteLearner);
        deletingActions.put("3", deletingHandler::deleteTrainer);

        // Đăng ký cho menu Hiển thị Thông tin (Vì các hàm này không nhận tham số Scanner nên dùng Runnable)
        infoActions.put("1", informationHandler::displayAllCourses);
        infoActions.put("2", informationHandler::displayAllLearners);
        infoActions.put("3", informationHandler::displayAllTrainers);
    }

    public void handleAddingSubmenu() {
        DisplayMenu.displayMenuAdding();
        executeActionWithScanner(addingActions);
    }

    public void handleSearchingSubmenu() {
        DisplayMenu.displayMenuSearching();
        executeActionWithScanner(searchingActions);
    }

    public void handleUpdatingSubmenu() {
        DisplayMenu.displayMenuUpdating();
        executeActionWithScanner(updatingActions);
    }

    public void handleDeletingSubmenu() {
        DisplayMenu.displayMenuDeleting();
        executeActionWithScanner(deletingActions);
    }

    public void handleInformationSubmenu() {
        DisplayMenu.displayInformation();
        System.out.println("==========================");
        String choice = TypingInput.getInput("Enter your choice: ", scanner);

        // Sử dụng Lambda để xử lý lựa chọn lấy ra từ map
        Runnable action = infoActions.get(choice);
        if (action != null) {
            action.run();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }

    // Hàm tiện ích chung để xử lý các hành động cần truyền vào Scanner
    private void executeActionWithScanner(Map<String, Consumer<Scanner>> actions) {
        System.out.println("==========================");
        String choice = TypingInput.getInput("Enter your choice: ", scanner);

        Consumer<Scanner> action = actions.get(choice);
        if (action != null) {
            action.accept(scanner); // Thực thi Lambda action
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }
}
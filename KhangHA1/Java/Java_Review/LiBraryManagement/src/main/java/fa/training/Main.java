package fa.training;

import fa.training.controller.AuthorController;
import fa.training.controller.BookController;
import fa.training.controller.CategoryController;
import fa.training.util.ConsoleUtil;
import fa.training.view.MainView;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final MainView view = new MainView();

    private static final AuthorController authorController =
            new AuthorController();

    private static final CategoryController categoryController =
            new CategoryController();

    private static final BookController bookController =
            new BookController();

    public static void main(String[] args) {

        Map<Integer, Runnable> actions = new HashMap<>();

        actions.put(1, Main::authorMenu);
        actions.put(2, Main::categoryMenu);
        actions.put(3, Main::bookMenu);

        boolean running = true;

        while (running) {

            view.printMainMenu();

            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 4) {
                running = false;
                view.printExitMessage();
                continue;
            }

            actions.getOrDefault(
                    choice,
                    () -> view.printInvalidChoice()
            ).run();
        }
    }

    private static void authorMenu() {

        Map<Integer, Runnable> actions = Map.of(
                1, () -> authorController.addAuthor(),
                2, () -> authorController.displayAllAuthors(),
                3, () -> authorController.updateAuthor(),
                4, () -> authorController.deleteAuthor()
        );

        boolean back = false;

        while (!back) {

            authorController.getView().printMenu();

            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 5) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    () -> authorController.getView().printInvalidChoice()
            ).run();
        }
    }

    private static void categoryMenu() {

        Map<Integer, Runnable> actions = Map.of(
                1, () -> categoryController.addCategory(),
                2, () -> categoryController.displayAllCategories(),
                3, () -> categoryController.updateCategory(),
                4, () -> categoryController.deleteCategory()
        );

        boolean back = false;

        while (!back) {

            categoryController.getView().printMenu();

            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 5) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    () -> categoryController.getView().printInvalidChoice()
            ).run();
        }
    }

    private static void bookMenu() {

        Map<Integer, Runnable> actions = Map.of(
                1, () -> bookController.addBook(),
                2, () -> bookController.displayAllBooks(),
                3, () -> bookController.findBookById(),
                4, () -> bookController.updateBook(),
                5, () -> bookController.deleteBook()
        );

        boolean back = false;

        while (!back) {

            bookController.getView().printMenu();

            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 6) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    () -> bookController.getView().printInvalidChoice()
            ).run();
        }
    }
}
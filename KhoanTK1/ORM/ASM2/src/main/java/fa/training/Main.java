package fa.training;

import fa.training.view.MenuView;
import fa.training.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        // Tu dong dong session factory khi thoat chuong trinh
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        // Khoi dong giao dien menu quan ly
        new MenuView().start();
    }
}
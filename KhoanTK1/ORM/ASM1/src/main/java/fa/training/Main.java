package fa.training;

import fa.training.view.MenuView;
import fa.training.util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        // Tu dong shutdown SessionFactory khi tat chuong trinh
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        // Khoi chay menu
        new MenuView().start();
    }
}
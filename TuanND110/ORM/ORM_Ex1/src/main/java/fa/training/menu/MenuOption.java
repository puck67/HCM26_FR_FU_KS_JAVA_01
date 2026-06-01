package fa.training.menu;

public class MenuOption {

    private final int key;
    private final String label;
    private final Runnable action;

    public MenuOption(int key, String label, Runnable action) {
        this.key = key;
        this.label = label;
        this.action = action;
    }

    public int getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public Runnable getAction() {
        return action;
    }
}


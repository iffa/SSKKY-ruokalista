package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri Elo
 */
public class ToolbarMenuEvent {
    private int dialog;
    public static final int FOOD_TODAY_DIALOG = 0;
    public static final int RATE_TODAY_DIALOG = 1;

    public ToolbarMenuEvent(int dialog) {
        this.dialog = dialog;
    }

    public int getDialog() {
        return dialog;
    }
}

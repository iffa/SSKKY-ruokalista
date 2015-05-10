package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class LoadStartEvent {
    boolean dialog;

    public LoadStartEvent(boolean dialog) {
        this.dialog = dialog;
    }

    public boolean getShowDialog() {
        return dialog;
    }
}

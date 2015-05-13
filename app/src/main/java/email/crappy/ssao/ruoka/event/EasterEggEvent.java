package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class EasterEggEvent {
    boolean showEaster;

    public EasterEggEvent(boolean showEaster) {
        this.showEaster = showEaster;
    }

    public boolean getShowEaster() {
        return showEaster;
    }
}

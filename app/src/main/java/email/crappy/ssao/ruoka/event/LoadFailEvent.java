package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class LoadFailEvent {
    public final String reason;

    public LoadFailEvent(String reason) {
        this.reason = reason;
    }
}

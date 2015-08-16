package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class ShareFoodEvent {
    public CharSequence day;

    public ShareFoodEvent(CharSequence day) {
        this.day = day;
    }
}

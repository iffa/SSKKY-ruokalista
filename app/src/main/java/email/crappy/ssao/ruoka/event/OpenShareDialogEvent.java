package email.crappy.ssao.ruoka.event;

import java.util.ArrayList;

/**
 * @author Santeri 'iffa'
 */
public class OpenShareDialogEvent {
    public ArrayList<String> dates;

    public OpenShareDialogEvent(ArrayList<String> dates) {
        this.dates = dates;
    }
}

package email.crappy.ssao.ruoka.data.util;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isExpired(Date date) {
        return date.before(GregorianCalendar.getInstance().getTime());
    }
}

package email.crappy.ssao.ruoka.data.util;

import java.util.Date;
import java.util.GregorianCalendar;

import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isExpired(Date date) {
        Timber.i("Comparing %s with %s", date, GregorianCalendar.getInstance().getTime());
        return date.before(GregorianCalendar.getInstance().getTime());
    }
}

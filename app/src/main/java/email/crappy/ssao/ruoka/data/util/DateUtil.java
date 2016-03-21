package email.crappy.ssao.ruoka.data.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isExpired(Date date) {
        Timber.i("Comparing %s with %s", date, Calendar.getInstance().getTime());
        return date.before(Calendar.getInstance().getTime());
    }

    public static boolean isToday(String date) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);

    }
}

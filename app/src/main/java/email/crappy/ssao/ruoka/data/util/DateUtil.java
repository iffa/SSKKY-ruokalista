package email.crappy.ssao.ruoka.data.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {

    /**
     * Checks if a given Date is "expired" == before the current Date
     *
     * @param date Date
     * @return True if expired
     */
    public static boolean isExpired(Date date) {
        Timber.i("Comparing %s with %s", date, Calendar.getInstance(Locale.GERMAN).getTime());
        return date.before(Calendar.getInstance(Locale.GERMAN).getTime());
    }

    /**
     * Checks if a given date (dd.MM) is today (or tomorrow)
     *
     * @param date     Date (dd.MM)
     * @param tomorrow Set to true if checking for tomorrow
     * @return True if today
     */
    public static boolean isToday(String date, boolean tomorrow) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        if (tomorrow) calendar.add(Calendar.DATE, 1);

        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);
    }

    public static boolean isCurrentWeek(String weekNumber) {
        int currentWeek = Calendar.getInstance(Locale.GERMAN).get(Calendar.WEEK_OF_YEAR);
        Timber.i("Comparing week %s with %s", weekNumber, currentWeek);
        return currentWeek == Integer.parseInt(weekNumber);
    }
}

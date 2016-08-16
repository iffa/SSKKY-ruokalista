package email.crappy.ssao.ruoka.data.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import email.crappy.ssao.ruoka.R;
import timber.log.Timber;

/**
 * Utility class to make working with dates a bit easier.
 *
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static Calendar getCurrentCalendar() {
        return GregorianCalendar.getInstance(Locale.GERMAN);
    }

    /**
     * Checks if a given date is "expired", i.e. before the current date
     *
     * @param date Date
     * @return True if expired
     */
    public static boolean isExpired(Date date) {
        Timber.i("Comparing %s with %s", date, Calendar.getInstance(Locale.GERMAN).getTime());
        return date.before(getCurrentCalendar().getTime());
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

        Calendar calendar = getCurrentCalendar();
        if (tomorrow) calendar.add(Calendar.DATE, 1);

        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);
    }

    public static boolean isRemainingWeek(String weekNumber) {
        int currentWeek = getCurrentCalendar().get(Calendar.WEEK_OF_YEAR);

        if (Integer.parseInt(weekNumber) < currentWeek) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isCurrentWeek(String weekNumber) {
        int currentWeek = getCurrentCalendar().get(Calendar.WEEK_OF_YEAR);
        Timber.i("Comparing week %s with %s", weekNumber, currentWeek);
        return currentWeek == Integer.parseInt(weekNumber);
    }

    public static int getWeekNumber(Date date) {
        Calendar calendar = getCurrentCalendar();
        calendar.setTime(date);

        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getDayResource(Date date) {
        Calendar calendar = getCurrentCalendar();
        calendar.setTime(date);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return R.string.monday;
            case Calendar.TUESDAY:
                return R.string.tuesday;
            case Calendar.WEDNESDAY:
                return R.string.wednesday;
            case Calendar.THURSDAY:
                return R.string.thursday;
            case Calendar.FRIDAY:
                return R.string.friday;
            default:
                return R.string.app_name;
        }
    }

    public static int getDayResource(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return R.string.monday;
            case Calendar.TUESDAY:
                return R.string.tuesday;
            case Calendar.WEDNESDAY:
                return R.string.wednesday;
            case Calendar.THURSDAY:
                return R.string.thursday;
            case Calendar.FRIDAY:
                return R.string.friday;
            default:
                return R.string.app_name;
        }
    }
}

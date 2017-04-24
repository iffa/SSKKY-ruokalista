package email.crappy.ssao.ruoka.data.util;

import android.annotation.SuppressLint;

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
     * @param date Date
     * @return True if today
     */
    public static boolean isToday(Date date) {
        Calendar current = getCurrentCalendar();

        Calendar item = getCurrentCalendar();
        item.setTime(date);

        return current.get(Calendar.DAY_OF_MONTH) == item.get(Calendar.DAY_OF_MONTH)
                && current.get(Calendar.MONTH) == item.get(Calendar.MONTH);
    }

    public static boolean isRemainingWeek(int weekNumber) {
        int currentWeek = getCurrentCalendar().get(Calendar.WEEK_OF_YEAR);

        return weekNumber >= currentWeek;
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

    @SuppressLint("SwitchIntDef")
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

    @SuppressLint("SwitchIntDef")
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

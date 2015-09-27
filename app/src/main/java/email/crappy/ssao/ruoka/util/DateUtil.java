package email.crappy.ssao.ruoka.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isToday(String date) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);

    }

    public static boolean isCurrentWeek(String weekString) {
        GregorianCalendar current = new GregorianCalendar();
        int week = Integer.parseInt(weekString);

        return current.get(Calendar.WEEK_OF_YEAR) == week;
    }

    public static boolean isDateThisWeek(String date) {
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar item = getItemCalendar(date, current);

        return item.get(Calendar.WEEK_OF_YEAR) == current.get(Calendar.WEEK_OF_YEAR);
    }

    public static boolean isValentines() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == 14 && calendar.get(Calendar.MONTH) == 1;
    }

    public static boolean isAfterTodaySunday(String pvm) {
        GregorianCalendar current = new GregorianCalendar();

        if (current.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            if (getItemCalendar(pvm, current).get(Calendar.DAY_OF_YEAR) - 1 == current.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    private static GregorianCalendar getItemCalendar(String date, GregorianCalendar current) {
        GregorianCalendar item = new GregorianCalendar();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        try {
            item.setTime(sdf.parse(date + "." + current.get(Calendar.YEAR)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return item;
    }
}

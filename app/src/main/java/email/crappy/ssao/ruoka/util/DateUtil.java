package email.crappy.ssao.ruoka.util;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Santeri 'iffa'
 */
public class DateUtil {
    public static boolean isDataExpired(String expiration) {
        GregorianCalendar current = new GregorianCalendar();
        GregorianCalendar expire = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        try {
            expire.setTime(sdf.parse(expiration));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.d("expire: " + expire.get(GregorianCalendar.DAY_OF_MONTH) + " " + expire.get(GregorianCalendar.MONTH) + " " + expire.get(GregorianCalendar.YEAR));
        Logger.d("current: " + current.get(GregorianCalendar.DAY_OF_MONTH) + " " + current.get(GregorianCalendar.MONTH) + " " + current.get(GregorianCalendar.YEAR));

        return current.after(expire);
    }

    public static boolean isToday(String date) {
        int day = Integer.parseInt(date.split("\\.")[0]);
        int month = Integer.parseInt(date.split("\\.")[1]);

        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == day && calendar.get(Calendar.MONTH) == (month - 1);

    }

    public static boolean isValentines() {
        GregorianCalendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_MONTH) == 14 && calendar.get(Calendar.MONTH) == 1;
    }

}

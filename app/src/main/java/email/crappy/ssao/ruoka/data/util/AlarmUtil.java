package email.crappy.ssao.ruoka.data.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Locale;

import email.crappy.ssao.ruoka.data.receiver.AlarmReceiver;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class AlarmUtil {
    public static final int ID = 1337;

    public static void setRepeatingAlarm(Context context, int hh, int mm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance(Locale.GERMAN);
        Calendar now = Calendar.getInstance(Locale.GERMAN);
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(now)) {
            Timber.i("Moving alarm to tomorrow to avoid hiccups");
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Timber.i("Alarm has been set for %s", calendar.toString());
    }
}

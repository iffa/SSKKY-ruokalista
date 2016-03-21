package email.crappy.ssao.ruoka.data.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import email.crappy.ssao.ruoka.data.receiver.AlarmReceiver;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class AlarmUtil {
    public static final int ID = 1337;

    public static void setRepeatingAlarm(Context context, int hh, int mm, int ss) { // TODO: Customizable alarm time?
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);

        if (calendar.before(now)) {
            Timber.i("Moving alarm to tomorrow");
            calendar.add(Calendar.DATE, 1);
        }

        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Timber.i("Alarm has been set for %s", calendar.toString());
    }
}

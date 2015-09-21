package email.crappy.ssao.ruoka.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.Calendar;

import email.crappy.ssao.ruoka.RuokaApplication;

/**
 * Called on boot & app launch
 *
 * @author Santeri 'iffa'
 */
public class SetAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals(RuokaApplication.ACTION_SET_ALARM)) {
            Logger.d("SetAlarmReceiver called: " + intent.getAction());

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            // Not going to mess with an existing alarm
            boolean alarmUp = (PendingIntent.getService(context, 69,
                    new Intent(context, NotificationService.class)
                            .setAction(RuokaApplication.ACTION_FIRE_NOTIFICATION),
                    PendingIntent.FLAG_NO_CREATE) != null);

            if (alarmUp) {
                Logger.d("Alarm was already up, not setting it again");
                return;
            }

            Intent i = new Intent(context, NotificationService.class);
            i.setAction(RuokaApplication.ACTION_FIRE_NOTIFICATION);
            PendingIntent alarmIntent = PendingIntent.getService(context, 69,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            // Avoid triggering alarm unintentionally
            if (calendar.get(Calendar.HOUR_OF_DAY) > 10) {
                Logger.d("Added a day to the alarm time to avoid weird stuff");
                calendar.add(Calendar.DATE, 1);
            }

            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }
}

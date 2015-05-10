package email.crappy.ssao.ruoka.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.Calendar;

import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.service.NotificationService;

/**
 * Called on boot & app launch
 *
 * @author Santeri 'iffa'
 */
public class SetAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || context == null || intent.getAction() == null) {
            Logger.d("someone sent us null in SetAlarmReceiver >:(");
            return;
        }
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals(RuokaApplication.ACTION_SET_ALARM)) {
            Logger.d("SetAlarmReceiver called: " + intent.getAction());

            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(context, NotificationService.class);
            PendingIntent alarmIntent = PendingIntent.getService(context, 69,
                    i, PendingIntent.FLAG_UPDATE_CURRENT);


            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }
}

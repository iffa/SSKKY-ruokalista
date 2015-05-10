package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Intent;

/**
 * @author Santeri 'iffa'
 */
public class RuokaApplication extends Application {
    public static final String ACTION_SET_ALARM = "email.crappy.ssao.ruoka.SET_ALARM";

    @Override
    public void onCreate() {
        super.onCreate();

        // Tell SetAlarmReceiver to set the alarm (for notifications)
        Intent setAlarmIntent = new Intent();
        setAlarmIntent.setAction(ACTION_SET_ALARM);
        sendBroadcast(setAlarmIntent);
    }
}

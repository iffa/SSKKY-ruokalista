package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Intent;

import com.parse.Parse;

/**
 * @author Santeri 'iffa'
 */
public class RuokaApplication extends Application {
    public static final String ACTION_SET_ALARM = "email.crappy.ssao.ruoka.SET_ALARM";
    public static final String BILLING_KEY = null;
    public static final String PARSE_KEY = null; // TODO: WTF are these for real
    public static final String PARSE_KEY_2 = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Tell SetAlarmReceiver to set the alarm (for notifications)
        Intent setAlarmIntent = new Intent();
        setAlarmIntent.setAction(ACTION_SET_ALARM);
        sendBroadcast(setAlarmIntent);

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_KEY, PARSE_KEY_2);
    }
}

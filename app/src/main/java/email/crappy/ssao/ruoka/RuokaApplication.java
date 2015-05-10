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
    public static final String PARSE_APP_ID = null;
    public static final String PARSE_MASTER = null;

    @Override
    public void onCreate() {
        super.onCreate();

        // Tell SetAlarmReceiver to set the alarm (for notifications)
        Intent setAlarmIntent = new Intent();
        setAlarmIntent.setAction(ACTION_SET_ALARM);
        sendBroadcast(setAlarmIntent);

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_MASTER);
    }
}

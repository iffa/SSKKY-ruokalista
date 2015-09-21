package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;

import email.crappy.ssao.ruoka.model.Rating;

/**
 * TODO: Clean up dialog fragment classes, refactor all around
 *
 * @author Santeri 'iffa'
 */
public class RuokaApplication extends Application {
    public static File cacheDir;
    public static final String ACTION_SET_ALARM = "email.crappy.ssao.ruoka.SET_ALARM";
    public static final String ACTION_FIRE_NOTIFICATION = "email.crappy.ssao.ruoka.FIRE_NOTIFICATION";
    public static final String BILLING_KEY = null;
    public static final String PARSE_APP_ID = null;
    public static final String PARSE_CLIENT = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Tell SetAlarmReceiver to set the alarm (for notifications)
        Intent setAlarmIntent = new Intent();
        setAlarmIntent.setAction(ACTION_SET_ALARM);
        sendBroadcast(setAlarmIntent);

        // Initialize Parse
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Rating.class);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT);

        // Anonymous users ftw
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().increment("RunCount");
        ParseUser.getCurrentUser().saveInBackground();

        // Cache dir
        cacheDir = getCacheDir();
    }
}

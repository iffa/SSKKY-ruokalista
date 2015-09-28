package email.crappy.ssao.ruoka.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.orhanobut.logger.Logger;

import email.crappy.ssao.ruoka.MainActivity;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.model.RuokaJsonObject;
import email.crappy.ssao.ruoka.settings.SettingsActivity;
import email.crappy.ssao.ruoka.util.DateUtil;
import email.crappy.ssao.ruoka.util.RetrofitService;
import rx.Observer;

/**
 * @author Santeri 'iffa'
 */
public class NotificationService extends Service implements Observer<RuokaJsonObject> {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("NotificationService has been summoned, checking preferences");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean enabled = sharedPref.getBoolean(SettingsActivity.KEY_NOTIFICATIONS_ENABLED, true);
        if (enabled) {
            Logger.d("Notifications are good to go, call FoodApi");
            new RetrofitService().getFood(this, false);
        } else {
            Logger.d("Notifications are disabled by user");
        }

        return START_NOT_STICKY;
    }

    private void showNotification(Item item) {
        String[] kamaSplit = item.getKama().split("\\n");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_stat_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle(getResources().getString(R.string.notification_title))
                        .setContentText(kamaSplit[0])
                        .setAutoCancel(true);
        if (kamaSplit.length > 1) {
            mBuilder.setSubText(kamaSplit[1]);
        }

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(69, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Logger.d("FoodApi call failed, not showing notification");
    }

    @Override
    public void onNext(RuokaJsonObject ruokaJsonObject) {
        for (Ruoka ruoka : ruokaJsonObject.getRuoka()) {
            for (Item item : ruoka.getItems()) {
                if (DateUtil.isToday(item.getPvm())) {
                    showNotification(item);
                    break;
                }
            }
        }
    }
}

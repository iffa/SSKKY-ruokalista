package email.crappy.ssao.ruoka.data.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.model.Day;
import email.crappy.ssao.ruoka.ui.MainActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Inject
    DataManager dataManager;
    private static final int NOTIFICATION_ID = 69;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Alarm fired");
        SSKKYApplication.get(context).getComponent().inject(this);

        if (!dataManager.getPreferencesHelper().getNotificationsEnabled()
                || dataManager.getPreferencesHelper().getWeeks() == null) {
            Timber.i("Not showing notification as user has them disabled or no data is loaded");
            return;
        }

        dataManager.getPreferencesHelper()
                .getCurrentDayObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(day -> {
                    Timber.i("Found data for current day, showing notification");
                    showNotification(context, day);
                }, throwable -> {
                    Timber.w(throwable, "No data for current day found");
                });
    }

    private void showNotification(Context context, Day day) {
        String[] foodSplit = day.food.split("\\n");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(context.getResources().getString(R.string.notification_title))
                .setContentText(foodSplit[0])
                .setAutoCancel(false);
        if (foodSplit.length > 1) {
            mBuilder.setSubText(foodSplit[1]);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}

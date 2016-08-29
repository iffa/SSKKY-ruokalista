package email.crappy.ssao.ruoka.data.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.ui.MainActivity;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 69;

    @Inject
    DataManager dataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Notification alarm fired");
        SSKKYApplication.get(context).component().inject(this);

        if (!dataManager.getPreferencesHelper().getNotificationsEnabled()) {
            Timber.i("Not showing notification as user has them disabled");
            return;
        }

        dataManager.isValid().filter(valid -> valid)
                .flatMap(valid -> dataManager.dataStream())
                .flatMap(items -> dataManager.next(items))
                .compose(dataManager.schedulers())
                .subscribe(today -> {
                    Timber.i("Got data for notification");
                    showNotification(context, today);
                }, throwable -> Timber.e(throwable, "Failed to get data for notification"));
    }

    private void showNotification(Context context, FoodItem item) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(item.food);

        if (item.secondaryFood != null) builder.setSubText(item.secondaryFood);

        Intent resultIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

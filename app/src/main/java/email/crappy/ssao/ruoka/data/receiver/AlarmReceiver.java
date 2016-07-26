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

    @Inject DataManager dataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Notification alarm fired");
        SSKKYApplication.get(context).component().inject(this);

        if (!dataManager.getPreferencesHelper().getNotificationsEnabled()
                /*|| dataManager.getPreferencesHelper().getWeeks() == null*/) {
            Timber.i("Not showing notification as user has them disabled or no data is loaded");
            return;
        }

        /*
        final boolean forTomorrow;
        forTomorrow = Calendar.getInstance(Locale.GERMAN).get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || dataManager.getPreferencesHelper().getNotificationTime().hourOfDay > 12;

        dataManager.getPreferencesHelper()
                .getCurrentDayObservable(forTomorrow)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(day -> {
                    Timber.i("Found data, showing notification");
                    showNotification(context, day, forTomorrow);
                }, throwable -> {
                    Timber.w(throwable, "No data found");
                });
                */
    }

    private void showNotification(Context context, FoodItem item, boolean forTomorrow) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setAutoCancel(false)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle(forTomorrow ? context.getString(R.string.notification_title_tomorrow) : context.getString(R.string.notification_title))
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

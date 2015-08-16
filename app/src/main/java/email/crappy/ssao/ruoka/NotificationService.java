package email.crappy.ssao.ruoka;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.orhanobut.logger.Logger;

import java.io.FileNotFoundException;

import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.pojo.RuokaJsonObject;
import email.crappy.ssao.ruoka.ui.activity.MainActivity;
import email.crappy.ssao.ruoka.util.DateUtil;
import email.crappy.ssao.ruoka.util.PojoUtil;

/**
 * @author Santeri 'iffa'
 */
public class NotificationService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("NotificationService has been summoned");

        if (RuokaApplication.shouldDownloadData()) {
            return START_NOT_STICKY;
        }

        RuokaJsonObject data;
        try {
            data = PojoUtil.generatePojoFromJson(getApplicationContext());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return START_NOT_STICKY;
        }

        for (Ruoka ruoka : data.getRuoka()) {
            for (Item item : ruoka.getItems()) {
                if (DateUtil.isToday(item.getPvm())) {
                    showNotification(item);
                    break;
                }
            }
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
}

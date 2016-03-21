package email.crappy.ssao.ruoka.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.DataManager;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Inject
    DataManager dataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Timber.i("Alarm fired");
        SSKKYApplication.get(context).getComponent().inject(this);

        if(!dataManager.getPreferencesHelper().getNotificationsEnabled()
                || dataManager.getPreferencesHelper().getWeeks() == null) {
            Timber.i("Not showing notification as user has them disabled or no data is loaded");
            return;
        }

        Timber.i("Showing notification");
    }
}

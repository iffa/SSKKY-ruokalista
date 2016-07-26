package email.crappy.ssao.ruoka.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.DataManager;

/**
 * @author Santeri 'iffa'
 */
public class BootReceiver extends BroadcastReceiver {
    @Inject DataManager dataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        SSKKYApplication.get(context).component().inject(this);

        dataManager.setAlarm(context);
    }
}

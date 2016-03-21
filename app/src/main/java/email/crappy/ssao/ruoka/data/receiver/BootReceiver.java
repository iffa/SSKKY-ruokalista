package email.crappy.ssao.ruoka.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import email.crappy.ssao.ruoka.data.util.AlarmUtil;

/**
 * @author Santeri 'iffa'
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmUtil.setRepeatingAlarm(context, 10, 0, 0);
    }
}

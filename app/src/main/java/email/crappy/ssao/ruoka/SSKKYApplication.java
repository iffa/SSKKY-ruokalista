package email.crappy.ssao.ruoka;

import android.app.Application;

import com.crashlytics.android.answers.Answers;

import io.fabric.sdk.android.Fabric;

/**
 * @author Santeri 'iffa'
 */
public class SSKKYApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Answers());
    }
}

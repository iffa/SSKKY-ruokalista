package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Context;
import android.os.Debug;

import com.crashlytics.android.answers.Answers;

import email.crappy.ssao.ruoka.injection.component.ApplicationComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerApplicationComponent;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class SSKKYApplication extends Application {
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //Debug.startMethodTracing("sskky");
            Timber.plant(new Timber.DebugTree());
        }

        //Fabric.with(this, new Answers());
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    public static SSKKYApplication get(Context context) {
        return (SSKKYApplication) context.getApplicationContext();
    }
}

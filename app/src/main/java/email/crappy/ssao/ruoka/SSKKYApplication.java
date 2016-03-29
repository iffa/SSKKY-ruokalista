package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Context;

import email.crappy.ssao.ruoka.injection.component.ApplicationComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerApplicationComponent;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
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
            Timber.plant(new Timber.DebugTree());
        }
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

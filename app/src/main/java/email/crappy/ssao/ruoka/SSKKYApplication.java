package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.answers.Answers;

import email.crappy.ssao.ruoka.injection.component.ApplicationComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerApplicationComponent;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;

/**
 * @author Santeri 'iffa'
 */
public class SSKKYApplication extends Application {
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Answers());
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

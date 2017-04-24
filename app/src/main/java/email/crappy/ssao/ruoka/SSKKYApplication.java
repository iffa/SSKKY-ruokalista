package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.injection.component.ApplicationComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerApplicationComponent;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
import io.github.prashantsolanki3.shoot.Shoot;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class SSKKYApplication extends Application {
    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    @Inject
    DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        getComponent().inject(this);

        refWatcher = LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        // Initializing Shoot
        Shoot.with(this);

        // Updating theme according to user preferences
        dataManager.updateTheme();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public static SSKKYApplication getInstance(Context context) {
        return (SSKKYApplication) context.getApplicationContext();
    }
}

package email.crappy.ssao.ruoka;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import net.ypresto.timbertreeutils.CrashlyticsLogExceptionTree;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.injection.component.ApplicationComponent;
import email.crappy.ssao.ruoka.injection.component.DaggerApplicationComponent;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class SSKKYApplication extends Application {
    private ApplicationComponent applicationComponent;
    private RefWatcher refWatcher;

    @Inject DataManager dataManager;

    @Override
    public void onCreate() {
        super.onCreate();

        component().inject(this);

        Crashlytics crashlytics = new Crashlytics.Builder()
                /*.core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())*/
                .build();

        Fabric.with(this, crashlytics, new Answers());

        refWatcher = LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Timber.plant(new CrashlyticsLogExceptionTree());

        // Updating theme according to user preferences
        dataManager.updateTheme();
    }

    public RefWatcher refWatcher() {
        return refWatcher;
    }

    public ApplicationComponent component() {
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

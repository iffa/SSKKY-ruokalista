package email.crappy.ssao.ruoka.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.receiver.AlarmReceiver;
import email.crappy.ssao.ruoka.data.receiver.BootReceiver;
import email.crappy.ssao.ruoka.injection.ApplicationContext;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;
import email.crappy.ssao.ruoka.injection.module.DataModule;
import email.crappy.ssao.ruoka.ui.settings.SettingsFragment;

/**
 * @author Santeri 'iffa'
 */
@Singleton
@Component(modules = {ApplicationModule.class, DataModule.class})
public interface ApplicationComponent {
    void inject(AlarmReceiver alarmReceiver);

    void inject(BootReceiver bootReceiver);

    void inject(SSKKYApplication sskkyApplication);

    void inject(SettingsFragment settingsFragment);

    @ApplicationContext
    Context context();

    Application application();

    ActivityComponent plus(ActivityModule activityModule);
}

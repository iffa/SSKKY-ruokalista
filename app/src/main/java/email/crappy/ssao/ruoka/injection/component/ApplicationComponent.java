package email.crappy.ssao.ruoka.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.ListService;
import email.crappy.ssao.ruoka.data.PreferencesHelper;
import email.crappy.ssao.ruoka.injection.ApplicationContext;
import email.crappy.ssao.ruoka.injection.module.ApplicationModule;

/**
 * @author Santeri 'iffa'
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ApplicationContext
    Context context();

    Application application();

    ListService listService();

    PreferencesHelper preferencesHelper();

    DataManager dataManager();
}

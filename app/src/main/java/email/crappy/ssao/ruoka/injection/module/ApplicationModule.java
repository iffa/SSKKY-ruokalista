package email.crappy.ssao.ruoka.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import email.crappy.ssao.ruoka.data.FoodService;
import email.crappy.ssao.ruoka.injection.ApplicationContext;

/**
 * @author Santeri 'iffa'
 */
@Module
public class ApplicationModule {
    protected final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }
}

package email.crappy.ssao.ruoka.injection.module;

import android.app.Activity;
import android.content.Context;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import email.crappy.ssao.ruoka.injection.ActivityContext;

/**
 * @author Santeri 'iffa'
 */
@Module
public class ActivityModule {
    private Activity activity;

    @Inject
    public ActivityModule(Activity activity) {
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return activity;
    }
}

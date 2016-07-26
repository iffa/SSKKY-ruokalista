package email.crappy.ssao.ruoka.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import email.crappy.ssao.ruoka.injection.ActivityContext;

/**
 * @author Santeri 'iffa'
 */
@Module
public class ActivityModule {
    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
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

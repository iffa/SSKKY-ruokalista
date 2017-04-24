package email.crappy.ssao.ruoka.injection.module;

import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import email.crappy.ssao.ruoka.injection.ActivityContext;

/**
 * @author Santeri Elo
 */
@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment provideFragment() {
        return fragment;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return fragment.getActivity();
    }
}

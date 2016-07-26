package email.crappy.ssao.ruoka.injection.component;

import dagger.Subcomponent;
import email.crappy.ssao.ruoka.injection.PerActivity;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;
import email.crappy.ssao.ruoka.ui.MainActivity;

/**
 * @author Santeri 'iffa'
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
}

package email.crappy.ssao.ruoka.injection.component;

import dagger.Component;
import email.crappy.ssao.ruoka.injection.PerActivity;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;
import email.crappy.ssao.ruoka.ui.MainActivity;

/**
 * @author Santeri 'iffa'
 */
@PerActivity
@Component(modules = ActivityModule.class, dependencies = ApplicationComponent.class)
public interface ActivityComponent {
    void inject(MainActivity activity);
}

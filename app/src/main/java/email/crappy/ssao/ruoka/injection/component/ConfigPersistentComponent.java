package email.crappy.ssao.ruoka.injection.component;

import dagger.Subcomponent;
import email.crappy.ssao.ruoka.injection.ConfigPersistent;
import email.crappy.ssao.ruoka.injection.module.ActivityModule;

/**
 * @author Santeri 'iffa'
 */
@ConfigPersistent
@Subcomponent
public interface ConfigPersistentComponent {
    ActivityComponent activityComponent(ActivityModule module);
}

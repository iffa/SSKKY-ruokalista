package email.crappy.ssao.ruoka.injection.component;

import dagger.Subcomponent;
import email.crappy.ssao.ruoka.injection.PerFragment;
import email.crappy.ssao.ruoka.injection.module.FragmentModule;
import email.crappy.ssao.ruoka.ui.home.HomePresenter;

/**
 * @author Santeri Elo
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    @PerFragment
    HomePresenter homePresenter();
}
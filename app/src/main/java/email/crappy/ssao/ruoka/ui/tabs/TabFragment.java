package email.crappy.ssao.ruoka.ui.tabs;

import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

/**
 * @author Santeri 'iffa'
 */
public class TabFragment extends MvpFragment<TabView, TabPresenter> {
    @NonNull
    @Override
    public TabPresenter createPresenter() {
        return new TabPresenter();
    }
}

package email.crappy.ssao.ruoka.ui.tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class TabFragment extends MvpFragment<TabView, TabPresenter> {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @NonNull
    @Override
    public TabPresenter createPresenter() {
        return new TabPresenter();
    }
}

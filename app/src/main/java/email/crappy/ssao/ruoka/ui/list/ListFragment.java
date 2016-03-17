package email.crappy.ssao.ruoka.ui.list;

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
public class ListFragment extends MvpFragment<ListView, ListPresenter> {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @NonNull
    @Override
    public ListPresenter createPresenter() {
        return new ListPresenter();
    }
}

package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;

/**
 * TODO: Implement this class completely
 *
 * @author Santeri 'iffa'
 */
public class PageFragment extends Fragment {
    public PageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


}

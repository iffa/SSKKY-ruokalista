package email.crappy.ssao.ruoka.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;

/**
 * @author Santeri 'iffa'
 */
public class SadFragment extends Fragment implements View.OnClickListener {
    public SadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sad, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
    }
}

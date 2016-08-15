package email.crappy.ssao.ruoka.ui.weeks;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri Elo
 */
public class WeeksFragment extends Fragment {
    public static WeeksFragment newInstance() {
        return new WeeksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weeks, container, false);
    }
}

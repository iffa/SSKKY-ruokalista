package email.crappy.ssao.ruoka.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skyfishjy.library.RippleBackground;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;

/**
 * @author Santeri 'iffa'
 */
public class SadFragment extends Fragment implements View.OnClickListener {
    public SadFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sad, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RippleBackground rippleBackground = (RippleBackground) getView().findViewById(R.id.rippleBackground);
        rippleBackground.startRippleAnimation();

        getView().findViewById(R.id.sadFaceLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        ((MainActivity)getActivity()).restartActivity();
    }
}

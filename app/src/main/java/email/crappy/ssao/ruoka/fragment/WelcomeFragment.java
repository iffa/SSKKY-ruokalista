package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class WelcomeFragment extends Fragment {
    @InjectView(R.id.welcomeTitle)
    TextView welcomeTitle;
    @InjectView(R.id.welcomeMessage)
    TextView welcomeMessage;


    public WelcomeFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        ButterKnife.inject(view);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Logger.d("onViewCreated has been called :D");

        super.onViewCreated(view, savedInstanceState);

        // TODO: Animate welcome-screen
        final PropertyAction titleAction = PropertyAction.newPropertyAction(view.findViewById(R.id.welcomeLayout)).
                interpolator(new DecelerateInterpolator()).
                translationY(-200).
                duration(550).
                alpha(0.4f).
                build();

        final PropertyAction spinnerAction = PropertyAction.newPropertyAction(view.findViewById(R.id.loadingSpinner)).
                interpolator(new DecelerateInterpolator()).
                translationY(400).
                duration(700).
                alpha(0.2f).
                build();

        Player.init().animate(titleAction).then().animate(spinnerAction).play();

        // TODO: Start data loading
    }
}

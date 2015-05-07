package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;

/**
 * @author Santeri 'iffa'
 */
public class WelcomeFragment extends Fragment {

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
                translationY(-100).
                duration(550).
                alpha(0.0f).
                build();

        final PropertyAction spinnerAction = PropertyAction.newPropertyAction(view.findViewById(R.id.loadingSpinner)).
                interpolator(new AnticipateOvershootInterpolator()).
                translationY(300).
                duration(700).
                alpha(0.0f).
                build();

        Player.init().animate(titleAction).then().animate(spinnerAction).play();

        if (getArguments().getBoolean("update", false)) {
            TextView title = (TextView) view.findViewById(R.id.welcomeTitle);
            TextView message = (TextView) view.findViewById(R.id.welcomeMessage);
            title.setText(R.string.welcome_update_title);
            message.setText(R.string.welcome_update_message);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Loading data
        // TODO: Make sure this isn't called again
        final MainActivity activity = (MainActivity) getActivity();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.downloadData(false);
            }
        }, 4000);

    }
}

package email.crappy.ssao.ruoka.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.eftimoff.androidplayer.Player;
import com.eftimoff.androidplayer.actions.property.PropertyAction;
import com.orhanobut.logger.Logger;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class WelcomeFragment extends Fragment {
    DownloadTaskFragment mTaskFragment;
    private static final String TAG_TASK_FRAGMENT = "taskFragment";

    public WelcomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        mTaskFragment = (DownloadTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        if (mTaskFragment == null) {
            Logger.d("mTaskFragment was null, creating new");
            mTaskFragment = new DownloadTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }

        if (!mTaskFragment.isRunning()) {
            mTaskFragment.setRunning(true);
            Logger.d("Loading data (mTaskFragment is not running)");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTaskFragment.loadData();
                }
            }, 4000);

            // Showing animations if data loading is just beginning
            final PropertyAction titleAction = PropertyAction.newPropertyAction(getView().findViewById(R.id.welcomeLayout)).
                    interpolator(new DecelerateInterpolator()).
                    translationY(-150).
                    duration(550).
                    alpha(0.0f).
                    build();

            final PropertyAction spinnerAction = PropertyAction.newPropertyAction(getView().findViewById(R.id.loadingSpinner)).
                    interpolator(new AnticipateOvershootInterpolator()).
                    translationY(300).
                    duration(700).
                    alpha(0.0f).
                    build();

            Player.init().animate(titleAction).then().animate(spinnerAction).play();

            if (getArguments().getBoolean("update", false)) {
                TextView title = (TextView) getView().findViewById(R.id.welcomeTitle);
                TextView message = (TextView) getView().findViewById(R.id.welcomeMessage);
                title.setText(R.string.welcome_update_title);
                message.setText(R.string.welcome_update_message);
            }
        }
    }
}

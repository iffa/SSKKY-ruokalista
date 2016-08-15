package email.crappy.ssao.ruoka.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.home.HomeFragment;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import email.crappy.ssao.ruoka.ui.view.NonSwipeableViewPager;
import email.crappy.ssao.ruoka.ui.weeks.WeeksFragment;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    @Inject DataManager dataManager;

    @BindView(R.id.pager_content)
    NonSwipeableViewPager pager;

    @BindView(R.id.fab_scroll)
    FloatingActionButton fab;

    public static Intent createIntent(Context context, boolean newTask) {
        Intent intent = new Intent(context, MainActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);

        setupViewPager();

        dataManager.setAlarm(this);

        dataManager.dataStream().subscribe(items -> {
            Timber.i("DATA DATA DATA OM NOM");
        });
    }

    private void setupViewPager() {
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return HomeFragment.newInstance();
                    case 1:
                        return WeeksFragment.newInstance();
                    default:
                        return HomeFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }

    @OnClick(R.id.fab_scroll)
    public void onFabClick() {
        switch (pager.getCurrentItem()) {
            case 0:
                pager.setCurrentItem(1, true);
                rotateFab(false);
                break;
            case 1:
                pager.setCurrentItem(0, true);
                rotateFab(true);
                break;
            default:
                Timber.e("User broke the app");
                break;
        }
    }

    @OnLongClick(R.id.fab_scroll)
    public boolean onFabLongClick() {
        Toast.makeText(this, R.string.fab_scroll_hint, Toast.LENGTH_LONG).show();
        return true;
    }

    private void rotateFab(boolean up) {
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(fab)
                .rotation(up ? 0f : 180f)
                .withLayer()
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setInterpolator(interpolator)
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(dataManager.getPreferencesHelper().getIsDebug() ? R.menu.activity_main_debug : R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.createIntent(this));
                break;
            case R.id.action_clear:
                dataManager.getPreferencesHelper().clear();
                startActivity(createIntent(this, true));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package email.crappy.ssao.ruoka.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.list.ListFragment;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import email.crappy.ssao.ruoka.ui.tabs.TabFragment;
import timber.log.Timber;

/**
 * Container for all list fragments
 *
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    @Inject
    DataManager dataManager;

    public static Intent getStartIntent(Context context, boolean clearStack) {
        Intent intent = new Intent(context, MainActivity.class);
        if (clearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);

        Timber.i("Should show notifications: %s", dataManager.getPreferencesHelper().getNotificationsEnabled());
        Timber.i("Should show ads: %s", dataManager.getPreferencesHelper().getShowAds());
        Timber.i("Should debug: %s", dataManager.getPreferencesHelper().getIsDebug());
        Timber.i("Layout to show: %s", dataManager.getPreferencesHelper().getMenuLayout().name());
        Timber.i("Should hide old weeks: %s", dataManager.getPreferencesHelper().getHideOldWeeks());

        if (savedInstanceState == null) {
            Fragment contentFragment;
            switch (dataManager.getPreferencesHelper().getMenuLayout()) {
                case LIST:
                    contentFragment = ListFragment.newInstance(dataManager.getPreferencesHelper().getShowAds());
                    break;
                case TABS:
                    contentFragment = new TabFragment();
                    break;
                default:
                    contentFragment = ListFragment.newInstance(dataManager.getPreferencesHelper().getShowAds());
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, contentFragment).commit();
        }

        dataManager.setAlarm(this);
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
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.action_clear:
                dataManager.getPreferencesHelper().clear();
                startActivity(getStartIntent(this, true));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package email.crappy.ssao.ruoka.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;
import email.crappy.ssao.ruoka.ui.settings.SettingsActivity;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class MainActivity extends BaseActivity {
    @Inject DataManager dataManager;

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

        if (savedInstanceState == null) {
            // TODO: Content
        }

        dataManager.setAlarm(this);

        dataManager.dataStream().subscribe(items -> {
            Timber.i("DATA DATA DATA OM NOM");
        });

        dataManager.updateData().subscribe();
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

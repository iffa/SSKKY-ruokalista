package email.crappy.ssao.ruoka.ui.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;

/**
 * @author Santeri 'iffa'
 */
public class SettingsActivity extends BaseActivity {
    private SettingsFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment, fragment.getTag()).commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

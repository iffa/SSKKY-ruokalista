package email.crappy.ssao.ruoka.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import net.xpece.android.support.preference.Fixes;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.ui.base.BaseActivity;

/**
 * @author Santeri 'iffa'
 */
public class SettingsActivity extends BaseActivity {
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fixes.updateLayoutInflaterFactory(getLayoutInflater());

        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            SettingsFragment fragment = SettingsFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content, fragment, fragment.getTag())
                    .commit();
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

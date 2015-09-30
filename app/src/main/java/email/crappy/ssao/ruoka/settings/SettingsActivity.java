package email.crappy.ssao.ruoka.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.BuildConfig;
import email.crappy.ssao.ruoka.MainActivity;
import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri Elo
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements BillingProcessor.IBillingHandler, SharedPreferences.OnSharedPreferenceChangeListener {
    private BillingProcessor bp;
    public static final String KEY_NOTIFICATIONS_ENABLED = "settings_notifications";
    public static final String KEY_DEBUG = "settings_debug";
    public static final String KEY_THEME = "settings_theme";
    public static final String KEY_ABOUT = "settings_about";
    public static final String KEY_DONATE = "settings_donate";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    int easter = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing billing
        bp = new BillingProcessor(this, BuildConfig.BILLING_API_KEY, this);

        // Initializing content
        setTheme();
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting up preferences
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);
        addPreferencesFromResource(R.xml.settings);
        findPreference(KEY_ABOUT).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (easter < 10) {
                    if (easter == 5) {
                        Snackbar.make(findViewById(R.id.contentView), R.string.easter_one_more, Snackbar.LENGTH_SHORT).show();
                    }
                    easter++;
                } else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sp.edit().putString(KEY_THEME, "3").commit();
                }
                return false;
            }
        });
        findPreference(KEY_DEBUG).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (((CheckBoxPreference) preference).isChecked()) {
                    Snackbar.make(findViewById(R.id.contentView), R.string.settings_debug_warning, Snackbar.LENGTH_LONG).show();
                }
                return false;
            }
        });
        findPreference(KEY_DONATE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                bp.purchase(SettingsActivity.this, "donation");
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTheme() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int theme = Integer.parseInt(sp.getString(SettingsActivity.KEY_THEME, "1"));
        switch (theme) {
            case 1:
                break;
            case 2:
                setTheme(R.style.AppThemeManly);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.manly));
                }
                break;
            case 3:
                setTheme(R.style.AppThemeYolo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.yolo));
                }
                break;
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_THEME)) { // Restarting activities to change theme immediately
            TaskStackBuilder.create(this)
                    .addNextIntent(new Intent(this, MainActivity.class))
                    .addNextIntent(this.getIntent())
                    .startActivities();
        }
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onProductPurchased(String s, TransactionDetails transactionDetails) {
        Snackbar.make(findViewById(R.id.contentView), R.string.settings_donate_thanks, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int i, Throwable throwable) {
    }

    @Override
    public void onBillingInitialized() {
    }
}


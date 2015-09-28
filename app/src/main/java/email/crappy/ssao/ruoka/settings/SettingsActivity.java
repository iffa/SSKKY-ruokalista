package email.crappy.ssao.ruoka.settings;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri Elo
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String KEY_NOTIFICATIONS_ENABLED = "settings_notifications";
    public static final String KEY_THEME = "settings_theme";
    public static final String KEY_ABOUT = "settings_about";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    int easter = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addPreferencesFromResource(R.xml.settings);
        findPreference(KEY_ABOUT).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (easter < 5) {
                    if (easter == 4) {
                        Snackbar.make(findViewById(R.id.contentView), R.string.easter_one_more, Snackbar.LENGTH_SHORT).show();
                    }
                    easter++;
                } else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sp.edit().putString(KEY_THEME, "3").commit();
                    Snackbar.make(findViewById(R.id.contentView), R.string.easter_get, Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
                findViewById(R.id.bg_easter).setVisibility(View.VISIBLE);
                break;
        }
    }

}


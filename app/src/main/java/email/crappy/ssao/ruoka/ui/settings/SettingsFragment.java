package email.crappy.ssao.ruoka.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import net.xpece.android.support.preference.ListPreference;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.BuildConfig;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.PreferencesHelper;
import email.crappy.ssao.ruoka.ui.MainActivity;

/**
 * @author Santeri 'iffa'
 */
public class SettingsFragment extends XpPreferenceFragment
        implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String PREF_KEY_VERSION = "pref_version";
    @Inject
    DataManager dataManager;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        addPreferencesFromResource(R.xml.pref_other);

        bindPreferenceSummaryToValue(findPreference(PreferencesHelper.PREF_KEY_THEME));

        findPreference(PREF_KEY_VERSION).setOnPreferenceClickListener(preference -> {
            // Too lazy to make this not leak on orientation change - it's an easter egg anyway...
            new MaterialDialog.Builder(getContext())
                    .title(R.string.pref_easter_title)
                    .content(R.string.pref_easter_description)
                    .items(R.array.pref_easter_items)
                    .show();

            return true;
        });
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        String value = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), "");

        findPreference(PREF_KEY_VERSION).setSummary(BuildConfig.VERSION_NAME);

        onPreferenceChange(preference, value);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SSKKYApplication.get(getContext()).component().inject(this);

        getListView().setFocusable(false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);
        } else {
            preference.setSummary(stringValue);
        }

        return true;
    }

    private void showRestartSnackbar() {
        Snackbar snackbar = Snackbar
                .make(getListView(), R.string.pref_restart, Snackbar.LENGTH_LONG)
                .setAction(R.string.pref_restart_action, view -> {
                    startActivity(MainActivity.createIntent(getContext(), true));
                });

        snackbar.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesHelper.PREF_KEY_THEME)
                || key.equals(PreferencesHelper.PREF_KEY_DEBUG)) {
            dataManager.updateTheme();
            showRestartSnackbar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SSKKYApplication.get(getContext()).refWatcher().watch(this);
    }
}

package email.crappy.ssao.ruoka.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.View;
import android.widget.Toast;

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
    private static final String PREF_KEY_THANKS = "pref_thanks";

    @Inject
    DataManager dataManager;

    @Inject
    PreferencesHelper preferencesHelper;

    private int maddeClicks = 0;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        addPreferencesFromResource(R.xml.pref_other);

        bindPreferenceSummaryToValue(findPreference(PreferencesHelper.PREF_KEY_THEME));

        findPreference(PREF_KEY_THANKS).setOnPreferenceClickListener(preference -> {
            if (maddeClicks < 5) {
                maddeClicks++;
            } else {
                maddeClicks = 0;
                preferencesHelper.setIsMadde(!preferencesHelper.getIsMadde());

                Toast.makeText(getContext(), R.string.pref_madde, Toast.LENGTH_LONG).show();

                startActivity(MainActivity.createIntent(getContext()));
            }

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

        SSKKYApplication.getInstance(getContext()).getComponent().inject(this);

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
                .setAction(R.string.pref_restart_action, view -> startActivity(MainActivity.createIntent(getContext())));

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
}

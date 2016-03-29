package email.crappy.ssao.ruoka.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.XpPreferenceFragment;
import android.view.View;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;

import net.xpece.android.support.preference.ListPreference;
import net.xpece.android.support.preference.PreferenceDividerDecoration;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.PreferencesHelper;
import email.crappy.ssao.ruoka.ui.MainActivity;

/**
 * @author Santeri 'iffa'
 */
public class SettingsFragment extends XpPreferenceFragment implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener, RadialTimePickerDialogFragment.OnTimeSetListener {

    @Override
    public void onCreatePreferences2(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_general);

        addPreferencesFromResource(R.xml.pref_other);

        bindPreferenceSummaryToValue(findPreference(PreferencesHelper.PREF_KEY_THEME));
        bindPreferenceSummaryToValue(findPreference(PreferencesHelper.PREF_KEY_LAYOUT));

        findPreference(PreferencesHelper.PREF_KEY_NOTIFICATION_PICKER).setOnPreferenceClickListener(this);
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        String value = PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), "");

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
        getListView().addItemDecoration(new PreferenceDividerDecoration(getContext()).drawBottom(true));
        setDivider(null);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list.
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            // Set the summary to reflect the new value.
            preference.setSummary(
                    index >= 0
                            ? listPreference.getEntries()[index]
                            : null);
        } else {
            // For all other preferences, set the summary to the value's
            // simple string representation.
            preference.setSummary(stringValue);
        }

        return true;
    }

    private void showThemeSnackbar() {
        Snackbar snackbar = Snackbar
                .make(getListView(), R.string.pref_restart, Snackbar.LENGTH_LONG)
                .setAction(R.string.pref_restart_action, view -> {
                    startActivity(MainActivity.getStartIntent(getContext(), true));
                });

        snackbar.show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesHelper.PREF_KEY_THEME)
                || key.equals(PreferencesHelper.PREF_KEY_LAYOUT)
                || key.equals(PreferencesHelper.PREF_KEY_ADS)
                || key.equals(PreferencesHelper.PREF_KEY_DEBUG)
                || key.equals(PreferencesHelper.PREF_KEY_HIDE_OLD_WEEKS)) {
            // Show Snackbar prompting user to restart the app in order to apply changes
            showThemeSnackbar();
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case PreferencesHelper.PREF_KEY_NOTIFICATION_PICKER:
                PreferencesHelper.NotificationTime startTime = SSKKYApplication.get(getContext())
                        .getComponent()
                        .preferencesHelper()
                        .getNotificationTime();
                RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(this)
                        .setThemeCustom(R.style.RadialTimePicker)
                        .setDoneText(getResources().getString(R.string.done))
                        .setStartTime(startTime.hourOfDay, startTime.minute);
                rtpd.show(getFragmentManager(), rtpd.getClass().getName());

                break;
        }
        return true;
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        SSKKYApplication.get(getContext())
                .getComponent()
                .dataManager()
                .setNotificationTime(getContext(), hourOfDay, minute);
    }
}

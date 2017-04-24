package email.crappy.ssao.ruoka.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceManager;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.squareup.moshi.JsonAdapter;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.injection.ApplicationContext;
import rx.Observable;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class PreferencesHelper {
    public static final String PREF_KEY_THEME = "pref_theme";
    public static final String PREF_KEY_DEBUG = "pref_debug";
    private static final String PREF_KEY_NOTIFICATIONS = "pref_notifications";
    private static final String PREF_KEY_MADDE = "pref_madde";
    private static final String PREF_KEY_FOOD = "pref_food";
    private final SharedPreferences preferences;
    private final RxSharedPreferences rxPreferences;
    private final JsonAdapter<List<FoodItem>> jsonAdapter;

    @Inject
    PreferencesHelper(@ApplicationContext Context context, JsonAdapter<List<FoodItem>> jsonAdapter) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rxPreferences = RxSharedPreferences.create(preferences);

        this.jsonAdapter = jsonAdapter;
    }

    /**
     * Clears preferences - duh.
     */
    public void clear() {
        preferences.edit().clear().apply();
    }

    /**
     * Gets the current data (not a stream of data) as an Observable.
     *
     * @return Emits the current data
     */
    public Observable<List<FoodItem>> data() {
        return Observable.just(preferences.getString(PREF_KEY_FOOD, null)).flatMap(this::fromJson);
    }

    Observable<List<FoodItem>> save(List<FoodItem> items) {
        return Observable.defer(() -> {
            try {
                putItems(items);
                return Observable.just(items);
            } catch (IOException exception) {
                return Observable.error(exception);
            }
        });
    }

    Observable<List<FoodItem>> observe() {
        Preference<String> itemsJson = rxPreferences.getString(PREF_KEY_FOOD);

        return itemsJson.asObservable().flatMap(this::fromJson);
    }

    private Observable<List<FoodItem>> fromJson(String json) {
        if (json == null) {
            Timber.w("JSON is null - don't even think about it");
            return Observable.just(null);
        }

        return Observable.defer(() -> {
            try {
                List<FoodItem> items = jsonAdapter.fromJson(json);
                return Observable.just(items);
            } catch (IOException exception) {
                return Observable.error(exception);
            }
        });
    }

    private void putItems(List<FoodItem> items) throws IOException {
        preferences.edit().putString(PREF_KEY_FOOD, jsonAdapter.toJson(items)).apply();
    }

    //
    // Settings-related preferences
    //

    /**
     * @return True if notifications are enabled
     */
    public boolean getNotificationsEnabled() {
        return preferences.getBoolean(PREF_KEY_NOTIFICATIONS, true);
    }

    /**
     * @return True if debug mode is enabled (in settings)
     */
    public boolean getIsDebug() {
        return preferences.getBoolean(PREF_KEY_DEBUG, false);
    }

    /**
     * @return True if easter egg is active
     */
    public boolean getIsMadde() {
        return preferences.getBoolean(PREF_KEY_MADDE, false);
    }

    /**
     * @param madde True if easter egg should be active, otherwise false
     */
    public void setIsMadde(boolean madde) {
        preferences.edit().putBoolean(PREF_KEY_MADDE, madde).apply();
    }

    @AppCompatDelegate.NightMode
    int getDayNightMode() {
        switch (Integer.parseInt(preferences.getString(PREF_KEY_THEME, "0"))) {
            case 0:
                return AppCompatDelegate.MODE_NIGHT_NO;
            case 1:
                return AppCompatDelegate.MODE_NIGHT_YES;
            case 2:
                return AppCompatDelegate.MODE_NIGHT_AUTO;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }
}

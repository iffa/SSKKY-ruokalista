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

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class PreferencesHelper {
    public static final String PREF_KEY_NOTIFICATIONS = "pref_notifications";
    public static final String PREF_KEY_THEME = "pref_theme";
    public static final String PREF_KEY_DEBUG = "pref_debug";
    private static final String PREF_KEY_FOOD = "pref_food";
    private final SharedPreferences preferences;
    private final RxSharedPreferences rxPreferences;
    private final JsonAdapter<List<FoodItem>> jsonAdapter;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context, JsonAdapter<List<FoodItem>> jsonAdapter) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rxPreferences = RxSharedPreferences.create(preferences);

        this.jsonAdapter = jsonAdapter;
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public Observable<List<FoodItem>> observe() {
        Preference<String> itemsJson = rxPreferences.getString(PREF_KEY_FOOD);

        return itemsJson.asObservable().flatMap(this::fromJson);
    }

    public Observable<List<FoodItem>> save(List<FoodItem> items) {
        return Observable.create(subscriber -> {
            try {
                putItems(items);
                subscriber.onNext(items);
            } catch (IOException exception) {
                subscriber.onError(exception);
            }
            subscriber.onCompleted();
        });
    }

    private Observable<List<FoodItem>> fromJson(String json) {
        return Observable.create(subscriber -> {
            try {
                List<FoodItem> items = jsonAdapter.fromJson(json);
                subscriber.onNext(items);
            } catch (IOException exception) {
                subscriber.onError(exception);
            }
        });
    }

    private void putItems(List<FoodItem> items) throws IOException {
        preferences.edit().putString(PREF_KEY_FOOD, jsonAdapter.toJson(items)).apply();
    }

    //
    // Settings-related preferences
    //

    public boolean getNotificationsEnabled() {
        return preferences.getBoolean(PREF_KEY_NOTIFICATIONS, true);
    }

    public boolean getIsDebug() {
        return preferences.getBoolean(PREF_KEY_DEBUG, false);
    }

    @AppCompatDelegate.NightMode
    public int getDayNightMode() {
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

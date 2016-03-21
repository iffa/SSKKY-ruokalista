package email.crappy.ssao.ruoka.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.Day;
import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import email.crappy.ssao.ruoka.injection.ApplicationContext;
import rx.Observable;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class PreferencesHelper {
    private static final String PREF_KEY_WEEK_LIST = "PREF_WEEK_LIST";
    private static final String PREF_KEY_EXPIRATION_DATE = "PREF_EXPIRATION_DATE";
    private static final String PREF_KEY_NOTIFICATION_TIME = "PREF_NOTIFICATION_TIME";
    public static final String PREF_KEY_NOTIFICATIONS = "PREF_NOTIFICATIONS";
    public static final String PREF_KEY_NOTIFICATION_PICKER = "PREF_NOTIFICATION_PICKER";
    public static final String PREF_KEY_ADS = "PREF_ADS";
    public static final String PREF_KEY_THEME = "PREF_THEME";
    public static final String PREF_KEY_LAYOUT = "PREF_LAYOUT";
    public static final String PREF_KEY_DEBUG = "PREF_DEBUG";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public void putWeeks(List<Week> weeks) {
        sharedPreferences.edit().putString(PREF_KEY_WEEK_LIST, gson.toJson(weeks)).apply();
    }

    public List<Week> getWeeks() {
        String weeksJson = sharedPreferences.getString(PREF_KEY_WEEK_LIST, null);
        if (weeksJson != null) {
            return gson.fromJson(weeksJson, new TypeToken<List<Week>>() {
            }.getType());
        }
        return null;
    }

    public Observable<List<Week>> getWeeksObservable() {
        return Observable.create((Observable.OnSubscribe<List<Week>>) subscriber -> {
            List<Week> weeks = getWeeks();
            if (weeks == null) {
                subscriber.onError(new NullPointerException("Local data is null"));
            } else {
                subscriber.onNext(getWeeks());
                subscriber.onCompleted();
            }
        });
    }

    public Day getCurrentDay(boolean forTomorrow) {
        Day current = null;
        weekLoop:
        {
            for (Week week : getWeeks()) {
                for (Day day : week.days) {
                    if (DateUtil.isToday(day.date, forTomorrow)) {
                        current = day;
                        break weekLoop;
                    }
                }
            }
        }
        return current;
    }

    public Observable<Day> getCurrentDayObservable(boolean forTomorrow) {
        return Observable.create((Observable.OnSubscribe<Day>) subscriber -> {
            Day current = getCurrentDay(forTomorrow);
            if (current == null) {
                subscriber.onError(new NullPointerException("No current day found"));
            } else {
                subscriber.onNext(current);
                subscriber.onCompleted();
            }
        });
    }

    public void putExpirationDate(Date date) {
        sharedPreferences.edit().putString(PREF_KEY_EXPIRATION_DATE, gson.toJson(date)).apply();
    }

    public Date getExpirationDate() {
        String expirationJson = sharedPreferences.getString(PREF_KEY_EXPIRATION_DATE, null);
        if (expirationJson != null) {
            return gson.fromJson(expirationJson, Date.class);
        }
        return null;
    }

    //
    // Settings-related preferences
    //

    public boolean getNotificationsEnabled() {
        return sharedPreferences.getBoolean(PREF_KEY_NOTIFICATIONS, true);
    }

    public NotificationTime getNotificationTime() {
        String notificationJSon = sharedPreferences.getString(PREF_KEY_NOTIFICATION_TIME, null);
        if (notificationJSon != null) {
            return gson.fromJson(notificationJSon, NotificationTime.class);
        }
        return new NotificationTime(10, 0);
    }

    public void putNotificationTime(int hourOfDay, int minute) {
        sharedPreferences.edit().putString(PREF_KEY_NOTIFICATION_TIME, gson.toJson(new NotificationTime(hourOfDay, minute))).apply();
    }

    public boolean getShowAds() {
        return sharedPreferences.getBoolean(PREF_KEY_ADS, true);
    }

    public boolean getIsDebug() {
        return sharedPreferences.getBoolean(PREF_KEY_DEBUG, false);
    }

    public int getThemeRes() {
        switch (Integer.parseInt(sharedPreferences.getString(PREF_KEY_THEME, "0"))) {
            case 0:
                return R.style.Theme_SSKKY_Light;
            case 1:
                return R.style.Theme_SSKKY_Dark;
            case 2:
                return R.style.Theme_SSKKY_Pink;
            default:
                return R.style.Theme_SSKKY_Light;
        }
    }

    public MenuLayout getMenuLayout() {
        return MenuLayout.valueOf(Integer.parseInt(sharedPreferences.getString(PREF_KEY_LAYOUT, "0")));
    }

    public enum MenuLayout {
        LIST(0),
        TABS(1);

        private final int value;
        private static Map<Integer, MenuLayout> map = new HashMap<>();

        MenuLayout(int value) {
            this.value = value;
        }

        static {
            for (MenuLayout layout : MenuLayout.values()) {
                map.put(layout.value, layout);
            }
        }

        public static MenuLayout valueOf(int value) {
            return map.get(value);
        }

    }

    public class NotificationTime {
        public int hourOfDay;
        public int minute;

        public NotificationTime(int hourOfDay, int minute) {
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }
    }
}

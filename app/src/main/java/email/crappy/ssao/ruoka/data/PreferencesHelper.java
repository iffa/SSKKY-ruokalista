package email.crappy.ssao.ruoka.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.injection.ApplicationContext;
import rx.Observable;

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class PreferencesHelper {
    public static final String PREF_FILE_NAME = "sskky_ruokalista_pref";
    private static final String PREF_KEY_WEEK_LIST = "PREF_KEY_WEEK_LIST";
    private static final String PREF_KEY_EXPIRATION_DATE = "PREF_KEY_EXPIRATION_DATE";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        gson = new GsonBuilder().setDateFormat("dd.MM.yyyy").create();
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
        return Observable.create((Observable.OnSubscribe<List<Week>>) subscriber -> getWeeks());
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
}

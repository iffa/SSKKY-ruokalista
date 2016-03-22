package email.crappy.ssao.ruoka.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.data.util.AlarmUtil;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class DataManager {
    private final ListService listService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(ListService listService, PreferencesHelper preferencesHelper) {
        this.listService = listService;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    /**
     * Fetches and returns an Observable with view-friendly data
     *
     * @return Observable
     */
    public Observable<List<Week>> getWeeks(boolean onlyRemaining) {
        if (preferencesHelper.getWeeks() == null || DateUtil.isExpired(preferencesHelper.getExpirationDate())) {
            return getRemoteWeeks(onlyRemaining)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return getLocalWeeks(onlyRemaining)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Fetches data from remote and automatically saves it locally while returning usable data for the view
     *
     * @param onlyRemaining True if should return only remaining weeks
     * @return Observable
     */
    private Observable<List<Week>> getRemoteWeeks(boolean onlyRemaining) {
        Timber.i("Getting data from remote");
        return listService.getList()
                .map(listResponse -> {
                    preferencesHelper.putExpirationDate(listResponse.expirationDate);
                    preferencesHelper.putWeeks(listResponse.weeks);

                    return listResponse.weeks;
                })
                .map(weeks -> {
                    if (onlyRemaining) return onlyRemaining(weeks);
                    return weeks;
                });
    }

    /**
     * Fetches data from local in view-friendly fashion
     *
     * @param onlyRemaining True if should return only remaining weeks
     * @return Observable
     */
    private Observable<List<Week>> getLocalWeeks(boolean onlyRemaining) {
        Timber.i("Getting data from local");
        return preferencesHelper.getWeeksObservable().map(weeks -> {
            if (onlyRemaining) return onlyRemaining(weeks);
            return weeks;
        });
    }

    private List<Week> onlyRemaining(List<Week> allWeeks) {
        Timber.i("Returning only remaining weeks");
        List<Week> remaining = new ArrayList<>();
        for (Week week : allWeeks) {
            if (DateUtil.isRemainingWeek(week.title.split("\\s+")[1])) {
                remaining.add(week);
                Timber.i("Added remaining week %s", week.title);
            }
        }
        return remaining;
    }

    public void setNotificationTime(Context context, int hourOfDay, int minute) {
        preferencesHelper.putNotificationTime(hourOfDay, minute);
        Timber.i("Notification time set to %s:%s, resetting alarm", hourOfDay, minute);

        AlarmUtil.setRepeatingAlarm(context, hourOfDay, minute, 0);
    }

    public void setAlarm(Context context) {
        PreferencesHelper.NotificationTime time = preferencesHelper.getNotificationTime();
        Timber.i("Setting alarm %s:%s", time.hourOfDay, time.minute);

        AlarmUtil.setRepeatingAlarm(context, time.hourOfDay, time.minute, 0);

    }
}

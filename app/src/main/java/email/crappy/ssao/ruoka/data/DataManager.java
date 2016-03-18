package email.crappy.ssao.ruoka.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.Week;
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
    public Observable<List<Week>> getWeeks() {
        if (preferencesHelper.getWeeks() == null || DateUtil.isExpired(preferencesHelper.getExpirationDate())) {
            return getRemoteWeeks();
        }
        return getLocalWeeks();
    }

    /**
     * Fetches data from remote and automatically saves it locally while returning usable data for the view
     *
     * @return Observable
     */
    private Observable<List<Week>> getRemoteWeeks() {
        Timber.i("Getting data from remote");
        return listService.getList()
                .map(listResponse -> {
                    preferencesHelper.putExpirationDate(listResponse.expirationDate);
                    preferencesHelper.putWeeks(listResponse.weeks);

                    return listResponse.weeks;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Fetches data from local in view-friendly fashion
     *
     * @return Observable
     */
    private Observable<List<Week>> getLocalWeeks() {
        Timber.i("Getting data from local");
        return preferencesHelper.getWeeksObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

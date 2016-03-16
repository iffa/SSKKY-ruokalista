package email.crappy.ssao.ruoka.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.Week;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    /**
     * Fetches and returns an Observable with view-friendly data
     *
     * @return Observable
     */
    public Observable<List<Week>> getWeeks() {
        if (preferencesHelper.getWeeks() == null) {
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
        return listService.getList()
                .map(listResponse -> {
                    preferencesHelper.putExpirationDate(listResponse.expirationDate);
                    preferencesHelper.putWeeks(listResponse.weeks);

                    return listResponse.weeks;
                })
                .compose(applySchedulers());
    }

    /**
     * Fetches data from local in view-friendly fashion
     *
     * @return Observable
     */
    private Observable<List<Week>> getLocalWeeks() {
        return preferencesHelper.getWeeksObservable()
                .compose(applySchedulers());
    }

    private <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

package email.crappy.ssao.ruoka.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.data.util.AlarmUtil;
import rx.Completable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
@Singleton
public class DataManager {
    private final FoodService listService;
    private final PreferencesHelper preferencesHelper;

    @Inject
    public DataManager(FoodService listService, PreferencesHelper preferencesHelper) {
        this.listService = listService;
        this.preferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return preferencesHelper;
    }

    public Observable<List<FoodItem>> dataStream() {
        return preferencesHelper.observe().doOnNext(items -> Timber.d("%s local items", items.size())).compose(schedulers());
    }

    public Completable updateData() {
        return listService.getList()
                .doOnError(throwable -> Timber.e(throwable, "Data update failed"))
                .doOnNext(items -> Timber.d("Got %s items from server", items.size()))
                .flatMap(preferencesHelper::save)
                .toCompletable()
                .doOnCompleted(() -> Timber.d("Completed data update call"))
                .compose(schedulersCompletable());
    }

    public void updateTheme() {
        //noinspection WrongConstant
        AppCompatDelegate.setDefaultNightMode(preferencesHelper.getDayNightMode());
    }

    public void setAlarm(Context context) {
        AlarmUtil.setRepeatingAlarm(context, 10, 0);
    }

    <T> Observable.Transformer<T, T> schedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Completable.CompletableTransformer schedulersCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

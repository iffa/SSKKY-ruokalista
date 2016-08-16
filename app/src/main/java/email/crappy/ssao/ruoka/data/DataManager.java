package email.crappy.ssao.ruoka.data;

import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.data.util.AlarmUtil;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import rx.Completable;
import rx.Observable;
import rx.Single;
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
        return preferencesHelper.observe()
                .doOnNext(items -> Timber.d("%s local items", items.size()))
                .compose(schedulers());
    }

    public Observable<Map<Integer, List<FoodItem>>> sectioned(List<FoodItem> items) {
        return Observable.create(subscriber -> {
            Map<Integer, List<FoodItem>> map = new LinkedHashMap<>();

            for (FoodItem item : items) {
                int week = DateUtil.getWeekNumber(item.date);

                if (!DateUtil.isRemainingWeek(week) || item.date.before(DateUtil.getCurrentCalendar().getTime())) {
                    continue;
                }

                if (map.get(week) != null) {
                    List<FoodItem> current = map.get(week);
                    current.add(item);
                } else {
                    List<FoodItem> newList = new ArrayList<>();
                    newList.add(item);

                    map.put(week, newList);
                }
            }

            subscriber.onNext(map);
            subscriber.onCompleted();
        });
    }

    public Observable<FoodItem> next(List<FoodItem> items) {
        return Observable.create(subscriber -> {
            FoodItem next = null;

            for (FoodItem item : items) {
                if (DateUtil.isToday(item.date)) {
                    next = item;
                    break;
                }
            }

            subscriber.onNext(next);
            subscriber.onCompleted();
        });
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

    public <T> Observable.Transformer<T, T> schedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    Completable.CompletableTransformer schedulersCompletable() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

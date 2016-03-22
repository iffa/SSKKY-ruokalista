package email.crappy.ssao.ruoka.ui.list;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author Santeri 'iffa'
 */
public class ListPresenter extends MvpBasePresenter<ListView> {
    private DataManager dataManager;

    public ListPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadContent() {
        if (isViewAttached()) getView().showLoading();

        dataManager.getWeeks(dataManager.getPreferencesHelper().getHideOldWeeks())
                .subscribe(weeks -> {
                    Timber.i("loadContent() next");
                    int currentWeek = 0;
                    for (Week week : weeks) {
                        String weekNumber = week.title.split("\\s+")[1];
                        if (DateUtil.isCurrentWeek(weekNumber)) {
                            currentWeek = weeks.indexOf(week);
                            Timber.i("Current week number is %s -> %s", currentWeek, week.title);

                            break;
                        }
                    }

                    if (isViewAttached()) getView().showContent(weeks, currentWeek);
                }, throwable -> {
                    Timber.e(throwable, "loadContent error");
                    if (isViewAttached()) getView().showError(throwable);
                });
    }
}

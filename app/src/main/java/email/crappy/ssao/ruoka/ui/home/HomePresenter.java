package email.crappy.ssao.ruoka.ui.home;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import email.crappy.ssao.ruoka.data.DataManager;
import timber.log.Timber;

/**
 * @author Santeri Elo
 */
@Singleton
public class HomePresenter extends MvpBasePresenter<HomeMvpView> {
    private DataManager dataManager;

    @Inject
    public HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void load() {
        if (isViewAttached()) getView().showLoading(false);

        dataManager.updateData().subscribe(() -> {
        }, throwable -> getView().showError(throwable, true));

        dataManager.dataStream()
                .filter(items -> items != null && items.size() > 0)
                .doOnNext(items -> dataManager.next(items).compose(dataManager.schedulers()).subscribe(item -> {
                    if (item == null && isViewAttached()) {
                        getView().showNextEmpty();
                    } else {
                        getView().setNext(item);
                    }
                }))
                .flatMap(foodItems -> dataManager.sectioned(foodItems))
                .compose(dataManager.schedulers())
                .subscribe(map -> {
                    Timber.i("Finished loading data for UI");
                    if (isViewAttached()) {
                        getView().setData(map);
                        getView().showContent();
                    }
                }, throwable -> {
                    Timber.e("Failed to load data for UI");
                    if (isViewAttached()) {
                        getView().showError(throwable, false);
                    }
                });
    }
}

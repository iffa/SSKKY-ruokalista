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
    HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @SuppressWarnings("ConstantConditions")
    void load() {
        if (isViewAttached()) getView().showLoading(false);

        dataManager.isValid()
                .flatMap(valid -> valid ? dataManager.dataStream() : dataManager.updateData())
                .doOnNext(items -> dataManager.next(items).compose(dataManager.schedulers()).subscribe(foodItem -> {
                    if (isViewAttached()) {
                        if (foodItem == null) {
                            getView().showNextEmpty();
                        } else {
                            getView().setNext(foodItem);
                        }
                    }
                }))
                .flatMap(items -> dataManager.sectioned(items))
                .compose(dataManager.schedulers())
                .subscribe(map -> {
                    Timber.i("Finished loading data for UI");
                    if (isViewAttached()) {
                        getView().setData(map);
                        getView().showContent();
                        getView().loadAds();
                    }
                }, throwable -> {
                    Timber.e(throwable, "Failed to load data for UI");
                    if (isViewAttached()) {
                        getView().showError(throwable, false);
                    }
                });
    }
}

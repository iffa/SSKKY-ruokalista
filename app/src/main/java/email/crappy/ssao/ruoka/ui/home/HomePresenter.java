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

        dataManager.updateData().subscribe(
                () -> {
                    Timber.i("Updated data, showing content");
                    if (isViewAttached()) {
                        getView().showContent();
                    }
                },
                throwable -> Timber.e(throwable, "Failed to update data"));

        dataManager.dataStream()
                .filter(items -> items != null && items.size() > 0)
                .flatMap(foodItems -> dataManager.sectioned(foodItems))
                .subscribe(map -> {
                    if (isViewAttached()) {
                        getView().setData(map);
                        getView().showContent();
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().showError(throwable, false);
                    }
                });
    }
}

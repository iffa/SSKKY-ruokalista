package email.crappy.ssao.ruoka.ui.home;

import android.support.annotation.NonNull;

import net.grandcentrix.thirtyinch.TiPresenter;
import net.grandcentrix.thirtyinch.rx.RxTiPresenterSubscriptionHandler;

import javax.inject.Inject;

import email.crappy.ssao.ruoka.data.DataManager;
import email.crappy.ssao.ruoka.injection.PerFragment;
import timber.log.Timber;

/**
 * @author Santeri Elo
 */
@PerFragment
public class HomePresenter extends TiPresenter<HomeView> {
    private final RxTiPresenterSubscriptionHandler rxHelper = new RxTiPresenterSubscriptionHandler(this);
    private DataManager dataManager;

    @HomeFragment.Type
    private int type;

    @Inject
    HomePresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    void setType(@HomeFragment.Type int type) {
        this.type = type;
    }

    @Override
    protected void onAttachView(@NonNull HomeView view) {
        super.onAttachView(view);

        rxHelper.manageViewSubscription(dataManager.isValid()
                .flatMap(valid -> valid ? dataManager.dataStream() : dataManager.updateData())
                .doOnNext(items -> dataManager.next(items).compose(dataManager.schedulers()).subscribe(foodItem -> sendToView(v -> {
                    if (foodItem == null) {
                        Timber.v("No data, showing empty (next)");
                        v.showNextEmpty();
                    } else {
                        Timber.v("Have data, showing (next)");
                        v.showNext(foodItem);
                    }
                })))
                .flatMap(items -> dataManager.sectioned(items))
                .compose(dataManager.schedulers())
                .subscribe(map -> {
                    Timber.i("Finished loading data for UI");

                    sendToView(v -> {
                        getView().showContent(map);
                        getView().loadAds();
                    });
                }, throwable -> {
                    Timber.e(throwable, "Failed to load data for UI");

                    sendToView(v -> getView().showError(throwable));
                })
        );
    }
}

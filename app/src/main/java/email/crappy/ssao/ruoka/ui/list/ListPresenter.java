package email.crappy.ssao.ruoka.ui.list;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import email.crappy.ssao.ruoka.data.DataManager;
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

        dataManager.getWeeks().subscribe(weeks -> {
            Timber.i("loadContent() next");
            if (isViewAttached()) getView().showContent(weeks);
        }, throwable -> {
            Timber.e(throwable, "loadContent error");
            if (isViewAttached()) getView().showError(throwable);
        });
    }
}

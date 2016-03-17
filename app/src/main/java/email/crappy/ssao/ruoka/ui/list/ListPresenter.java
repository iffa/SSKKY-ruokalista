package email.crappy.ssao.ruoka.ui.list;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import email.crappy.ssao.ruoka.data.DataManager;

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
            if (isViewAttached()) getView().showContent(weeks);
        }, throwable -> {
            if (isViewAttached()) getView().showError();
        });
    }
}

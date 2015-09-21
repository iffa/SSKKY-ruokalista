package email.crappy.ssao.ruoka.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

/**
 * @author Santeri Elo
 */
public class FoodPresenter extends MvpBasePresenter<FoodView> implements MvpPresenter<FoodView> {

    public void loadCurrentWeek() {
        // TODO: Load stuff
        getView().showLoading(false);
    }

    @Override
    public void attachView(FoodView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }
}

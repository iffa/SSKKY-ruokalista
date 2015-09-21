package email.crappy.ssao.ruoka.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import email.crappy.ssao.ruoka.model.RuokaJsonObject;
import email.crappy.ssao.ruoka.util.RetrofitService;
import rx.Observer;

/**
 * @author Santeri Elo
 */
public class FoodPresenter extends MvpBasePresenter<FoodView> implements MvpPresenter<FoodView>, Observer<RuokaJsonObject> {
    public void loadData() {
        // TODO: Load stuff
        getView().showLoading(false);

        // Loading food data
        new RetrofitService().getFood(this, true);
    }

    @Override
    public void attachView(FoodView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    /* Observer callbacks */

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        getView().showError(e, false);
    }

    @Override
    public void onNext(RuokaJsonObject ruokaJsonObject) {
        getView().setData(ruokaJsonObject.getRuoka());
        getView().showContent();
    }
}

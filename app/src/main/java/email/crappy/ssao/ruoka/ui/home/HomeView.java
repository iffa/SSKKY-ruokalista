package email.crappy.ssao.ruoka.ui.home;

import net.grandcentrix.thirtyinch.TiView;
import net.grandcentrix.thirtyinch.callonmainthread.CallOnMainThread;

import java.util.List;
import java.util.Map;

import email.crappy.ssao.ruoka.data.model.FoodItem;

/**
 * @author Santeri Elo
 */
interface HomeView extends TiView {
    @CallOnMainThread
    void showLoading();

    @CallOnMainThread
    void showError(Throwable throwable);

    @CallOnMainThread
    void showContent(Map<Integer, List<FoodItem>> items);

    @CallOnMainThread
    void showNext(FoodItem next);

    @CallOnMainThread
    void showNextEmpty();

    @CallOnMainThread
    void loadAds();
}

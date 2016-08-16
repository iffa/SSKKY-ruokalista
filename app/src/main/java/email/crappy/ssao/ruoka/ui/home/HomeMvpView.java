package email.crappy.ssao.ruoka.ui.home;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;
import java.util.Map;

import email.crappy.ssao.ruoka.data.model.FoodItem;

/**
 * @author Santeri Elo
 */
public interface HomeMvpView extends MvpLceView<Map<Integer, List<FoodItem>>> {
    void setNext(FoodItem next);

    void showNextEmpty();
}

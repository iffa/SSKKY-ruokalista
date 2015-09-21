package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.pojo.Ruoka;

/**
 * @author Santeri 'iffa'
 */
public class FoodFragment extends MvpLceViewStateFragment<RelativeLayout, List<Ruoka>, FoodView, FoodPresenter> implements FoodView {
    public FoodFragment() {
    }

    @Override
    public FoodPresenter createPresenter() {
        return new FoodPresenter();
    }

    @Override
    public LceViewState<List<Ruoka>, FoodView> createViewState() {
        setRetainInstance(true);
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Ruoka> getData() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        //ArrayList<Card> cards = new ArrayList<>();

        for (Ruoka ruokaItem : RuokaApplication.data.getRuoka()) {
            /*
            RuokaListCard card = new RuokaListCard(getActivity(), ruokaItem);
            card.init();
            cards.add(card);
            */
        }

        /*
        CardGridStaggeredArrayAdapter mCardArrayAdapter = new CardGridStaggeredArrayAdapter(getActivity(), cards);
        AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
        CardGridStaggeredView listView = (CardGridStaggeredView) getActivity().findViewById(R.id.card_grid);
        animCardArrayAdapter.setAbsListView(listView);
        listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
        */
    }

    @Override
    protected String getErrorMessage(Throwable throwable, boolean b) {
        return null;
    }

    @Override
    public void setData(List<Ruoka> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadCurrentWeek();
    }
}

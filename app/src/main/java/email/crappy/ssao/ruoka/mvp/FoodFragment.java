package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.mvp.fixed.MvpLceViewStateFragmentFixed;
import email.crappy.ssao.ruoka.recycler.AllWeeksAdapter;
import email.crappy.ssao.ruoka.recycler.SelectedWeekAdapter;
import email.crappy.ssao.ruoka.recycler.WrapLinearLayoutManager;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * TODO: Fix layout :D
 * TODO: Selected week card, other weeks also cards, but smaller? How to?
 *
 * @author Santeri 'iffa'
 */
public class FoodFragment extends MvpLceViewStateFragmentFixed<RelativeLayout, List<Ruoka>, FoodView, FoodPresenter> implements FoodView {
    private List<Ruoka> data;
    SelectedWeekAdapter selectedWeekAdapter;
    AllWeeksAdapter allWeeksAdapter;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.secondaryRecyclerView)
    RecyclerView secondaryRecyclerView;

    @NonNull
    @Override
    public FoodPresenter createPresenter() {
        return new FoodPresenter();
    }

    @NonNull
    @Override
    public LceViewState<List<Ruoka>, FoodView> createViewState() {
        setRetainInstance(true);
        return new RetainingLceViewState<>();
    }

    @Override
    public List<Ruoka> getData() {
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        selectedWeekAdapter = new SelectedWeekAdapter(getActivity());
        allWeeksAdapter = new AllWeeksAdapter(getActivity());

        recyclerView.setLayoutManager(new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(selectedWeekAdapter);

        secondaryRecyclerView.setLayoutManager(new WrapLinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        secondaryRecyclerView.setAdapter(allWeeksAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected String getErrorMessage(Throwable throwable, boolean b) {
        return null;
    }

    @Override
    public void setData(List<Ruoka> data) {
        this.data = data;
    }

    @Override
    public void showContent() {
        super.showContent();

        Logger.d("showContent() called, data size is " + data.size());

        List<String> allWeeks = new ArrayList<>();
        for (Ruoka item : data) {
            // Show current week in main RecyclerView
            if (DateUtil.isThisWeek(item.getItems().get(0).getPvm())) {
                selectedWeekAdapter.setItems(item.getItems());
                Logger.d("Found current week: " + item.getTitle());
                //break;
            }

            allWeeks.add(item.getTitle());
            Logger.d("Added week to allWeeks-list: " + item.getTitle());
        }
        allWeeksAdapter.setItems(allWeeks);
        secondaryRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData();
    }
}

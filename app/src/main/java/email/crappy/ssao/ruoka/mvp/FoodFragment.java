package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.mvp.fixed.MvpLceViewStateFragmentFixed;
import email.crappy.ssao.ruoka.recycler.AllWeeksAdapter;
import email.crappy.ssao.ruoka.recycler.RecyclerItemClickListener;
import email.crappy.ssao.ruoka.recycler.SelectedWeekAdapter;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * TODO: Fix layout :D
 * TODO: Selected week card, other weeks also cards, but smaller? How to?
 *
 * @author Santeri 'iffa'
 */
public class FoodFragment extends MvpLceViewStateFragmentFixed<RelativeLayout, List<Ruoka>, FoodView, FoodPresenter> implements FoodView, AdapterView.OnItemLongClickListener, RecyclerItemClickListener.OnItemClickListener {
    private List<Ruoka> data;
    SelectedWeekAdapter selectedWeekAdapter;
    AllWeeksAdapter allWeeksAdapter;
    @Bind(R.id.list_current_food)
    ListView foodListView;
    @Bind(R.id.recycler_other_weeks)
    RecyclerView weeksRecyclerView;

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

        foodListView.setAdapter(selectedWeekAdapter);
        foodListView.setOnItemLongClickListener(this);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        weeksRecyclerView.setLayoutManager(gridLayoutManager);
        weeksRecyclerView.setHasFixedSize(true);
        weeksRecyclerView.setAdapter(allWeeksAdapter);
        weeksRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), this));

        setListViewHeightBasedOnItems(foodListView); // Make room for grid
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
            if (DateUtil.isDateThisWeek(item.getItems().get(0).getPvm())) {
                selectedWeekAdapter.setItems(item.getItems());
                Logger.d("Found current week: " + item.getTitle());
            }

            allWeeks.add(item.getTitle());
            Logger.d("Added week to allWeeks-list: " + item.getTitle());
        }
        allWeeksAdapter.setItems(allWeeks);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData();
    }

    /**
     * Called when a list item is long clicked.
     *
     * @param adapterView
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Logger.d("Selected week item clicked, pos " + position);
        Item clicked = ((Item) selectedWeekAdapter.getItem(position));

        // TODO: Share dialog or something
        Toast toast = Toast.makeText(getActivity(), "TODO: share dialog", Toast.LENGTH_SHORT);
        toast.show();

        return true;
    }

    /**
     * Called when a recycler item is clicked (other weeks)
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        Logger.d("Other week item clicked, pos: " + position);
        String clicked = allWeeksAdapter.getItem(position);

        // TODO: implement
        Toast toast = Toast.makeText(getActivity(), "Clicked: " + clicked + " - TODO: change above", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * wrap_content of sorts for ListView
     * @param listView ListView to modify
     * @return true if height was set
     */
    private boolean setListViewHeightBasedOnItems(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int numberOfItems = listAdapter.getCount();
            int totalItemsHeight = 0;

            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
            return true;
        }
        return false;
    }
}
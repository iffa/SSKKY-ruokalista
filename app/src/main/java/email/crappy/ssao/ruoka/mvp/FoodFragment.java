package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.adapter.AllWeeksAdapter;
import email.crappy.ssao.ruoka.adapter.SelectedWeekAdapter;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.mvp.fixed.MvpLceViewStateFragmentFixed;
import email.crappy.ssao.ruoka.ui.DividerItemDecoration;
import email.crappy.ssao.ruoka.ui.activity.MainActivity;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri 'iffa'
 */
@SuppressWarnings("JavaDoc")
public class FoodFragment extends MvpLceViewStateFragmentFixed<RelativeLayout, List<Ruoka>, FoodView, FoodPresenter> implements FoodView, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, SlidingUpPanelLayout.PanelSlideListener {
    private List<Ruoka> data;
    SelectedWeekAdapter selectedWeekAdapter;
    AllWeeksAdapter allWeeksAdapter;
    @Bind(R.id.recycler_food_list)
    RecyclerView foodRecyclerView;
    //@Bind(R.id.grid_other_weeks)
    //GridView weeksGridView;
    //@Bind(R.id.card_current_header)
    //LinearLayout cardHeader;
    //@Bind(R.id.layout_panel)
    //SlidingUpPanelLayout panelLayout;
    //@Bind(R.id.panel_slide_text)
    //TextSwitcher panelTextSwitcher;

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
        return inflater.inflate(R.layout.fragment_food_experimental, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (MainActivity.EASTER_PINK_THEME) { // Easter egg get! :D
            // TODO: (after initial release) MANLY versions of food icons for consistency!
            //cardHeader.setBackgroundResource(R.color.manly);
        }

        //panelLayout.setPanelSlideListener(this);

        selectedWeekAdapter = new SelectedWeekAdapter(getActivity());
        allWeeksAdapter = new AllWeeksAdapter(getActivity());

        foodRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodRecyclerView.setItemAnimator(new DefaultItemAnimator());
        foodRecyclerView.setAdapter(selectedWeekAdapter);
        //foodRecyclerView.setOnItemLongClickListener(this);

        /*
        weeksGridView.setAdapter(allWeeksAdapter);
        weeksGridView.setOnItemClickListener(this);

        TextView slideExpandTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.view_text_panel_slide, null);
        TextView slideCollapseTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.view_text_panel_slide, null);
        Animation in = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
        panelTextSwitcher.setInAnimation(in);
        panelTextSwitcher.setOutAnimation(out);
        panelTextSwitcher.addView(slideExpandTextView);
        panelTextSwitcher.addView(slideCollapseTextView);

        setListViewHeightBasedOnItems(foodRecyclerView); // Make room for grid
        */
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

        // Easter egg for now
        EventBus.getDefault().post(new EasterEggEvent(false));

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Logger.d("Other week item clicked, pos: " + position);
        String clicked = allWeeksAdapter.getItem(position);

        // TODO: implement
        Toast toast = Toast.makeText(getActivity(), "Clicked: " + clicked + " - TODO: change above", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * wrap_content of sorts for ListView
     *
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

    /* Sliding panel listener methods */

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelCollapsed(View panel) {
        //panelTextSwitcher.setText("Pyyhkäise ylös valitaksesi näytettävän viikon");
    }

    @Override
    public void onPanelExpanded(View panel) {
        //panelTextSwitcher.setText("Valitse haluamasi viikko, tai pyyhkäise alas");
    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelHidden(View panel) {

    }
}
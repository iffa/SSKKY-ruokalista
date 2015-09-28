package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.orhanobut.logger.Logger;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.MainActivity;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.adapter.WeekAdapter;
import email.crappy.ssao.ruoka.event.RatingSaveEvent;
import email.crappy.ssao.ruoka.event.ToolbarMenuEvent;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.model.Rating;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.mvp.fixed.MvpLceViewStateFragmentFixed;
import email.crappy.ssao.ruoka.ui.dialog.InfoDialogFragment;
import email.crappy.ssao.ruoka.ui.dialog.RatingDialogFragment;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri 'iffa'
 */
@SuppressWarnings("JavaDoc")
public class FoodFragment extends MvpLceViewStateFragmentFixed<RelativeLayout, List<Ruoka>, FoodView, FoodPresenter> implements FoodView {
    private List<Ruoka> data;
    WeekAdapter adapter;
    @Bind(R.id.list_current_food)
    RecyclerView foodListView;
    @Bind(R.id.bg_easter)
    ImageView easterBackground;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public List<Ruoka> getData() {
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (MainActivity.EASTER_YOLO) {
            easterBackground.setVisibility(View.VISIBLE);
        }

        adapter = new WeekAdapter();

        foodListView.setLayoutManager(new LinearLayoutManager(getContext()));
        foodListView.setAdapter(adapter);
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
        for (Ruoka item : data) {
            if (DateUtil.isDateThisWeek(item.getItems().get(0).getPvm())) {
                Logger.d("Found current week: " + item.getTitle());
            }
        }

        adapter.setItems(data);
    }

    public Map<Item, Boolean> getTodayOrAfterSunday() {
        Map<Item, Boolean> itemMap = new HashMap<>();
        for (Ruoka ruoka : data) {
            if (itemMap.size() > 0) {
                break;
            }
            for (Item item : ruoka.getItems()) {
                if (DateUtil.isToday(item.getPvm())) {
                    itemMap.put(item, false);
                    break;
                } else if (DateUtil.isAfterTodaySunday(item.getPvm())) {
                    itemMap.put(item, true);
                    break;
                }
            }
        }
        return itemMap;
    }

    public Item getToday() {
        Item today = null;
        for (Ruoka ruoka : data) {
            if (today != null) {
                break;
            }
            for (Item item : ruoka.getItems()) {
                if (DateUtil.isToday(item.getPvm())) {
                    today = item;
                    break;
                }
            }
        }
        return today;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData();
    }

    @SuppressWarnings("unused")
    public void onEvent(ToolbarMenuEvent event) {
        switch (event.getDialog()) {
            case ToolbarMenuEvent.FOOD_TODAY_DIALOG:
                Map<Item, Boolean> todayItem = getTodayOrAfterSunday();
                if (todayItem.size() > 0) {
                    Item foodItem = (Item) todayItem.keySet().toArray()[0];

                    if (todayItem.get(foodItem)) {
                        InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_sunday), foodItem.getKama()).show(getFragmentManager(), "todayDialog");
                    } else {
                        InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_today_title), foodItem.getKama()).show(getFragmentManager(), "todayDialog");
                    }
                } else {
                    InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message)).show(getFragmentManager(), "noFoodDialog");
                }
                break;
            case ToolbarMenuEvent.RATE_TODAY_DIALOG:
                Item foodItem = getToday();
                if (foodItem == null) {
                    InfoDialogFragment.newInstance(getResources().getString(R.string.dialog_nofood_title), getResources().getString(R.string.dialog_nofood_message)).show(getFragmentManager(), "noFoodDialog");
                } else {
                    ParseQuery<Rating> query = Rating.getQuery();
                    query.whereEqualTo("date", foodItem.getPvm());
                    query.whereEqualTo("user", ParseUser.getCurrentUser());
                    query.getFirstInBackground(new GetCallback<Rating>() {
                        @Override
                        public void done(Rating rating, com.parse.ParseException e) {
                            Logger.d("Got first in background");
                            if (e != null && e.getCode() != com.parse.ParseException.OBJECT_NOT_FOUND) {
                                Logger.d("ParseException: " + e.toString());

                                InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_parse_error_message)).show(getFragmentManager(), "parseErrorDialog");
                                return;
                            }

                            if (rating == null) {
                                Logger.d("Rating is null in getFirstInBackground");
                                new RatingDialogFragment().show(getFragmentManager(), "ratingDialog");
                            } else {
                                Logger.d("Rating is not null = already exists?");
                                InfoDialogFragment.newInstance(getResources().getString(R.string.error), getResources().getString(R.string.dialog_rated_already_message)).show(getFragmentManager(), "alreadyRatedDialog");
                            }
                        }
                    });
                }
                break;
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(RatingSaveEvent event) {
        Rating rating = new Rating();
        rating.setUuidString();

        Item todayItem = getToday();

        rating.setDescription(event.getDescription());
        rating.setOpinion(event.getOpinion());
        rating.setDate(todayItem.getPvm());
        rating.setFood(todayItem.getKama());
        rating.setUser(ParseUser.getCurrentUser());

        ParseUser.getCurrentUser().put("rating", rating);
        ParseUser.getCurrentUser().saveInBackground();

        Snackbar successToast = Snackbar.make(contentView, R.string.toast_rating, Snackbar.LENGTH_SHORT);
        successToast.show();
    }
}
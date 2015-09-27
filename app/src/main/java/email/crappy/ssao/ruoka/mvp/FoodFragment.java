package email.crappy.ssao.ruoka.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.adapter.WeekAdapter;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.mvp.fixed.MvpLceViewStateFragmentFixed;
import email.crappy.ssao.ruoka.ui.activity.MainActivity;
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

        if (MainActivity.EASTER_PINK_THEME) {
            // TODO
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

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadData();
    }
}
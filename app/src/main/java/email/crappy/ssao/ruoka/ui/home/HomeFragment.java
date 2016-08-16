package email.crappy.ssao.ruoka.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import email.crappy.ssao.ruoka.ui.list.WeekAdapter;
import email.crappy.ssao.ruoka.ui.view.DateBoxView;

/**
 * @author Santeri Elo
 */
public class HomeFragment extends MvpLceFragment<NestedScrollView, Map<Integer, List<FoodItem>>, HomeMvpView, HomePresenter> implements HomeMvpView {
    private Unbinder unbinder;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.recycler_weeks)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public HomePresenter createPresenter() {
        return SSKKYApplication.get(getContext()).component().presenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DateBoxView dateBoxView = (DateBoxView) view.findViewById(R.id.date_today);

        Date current = DateUtil.getCurrentCalendar().getTime();
        dateBoxView.setDay(current);
        dateBoxView.setDate(current);

        // Setup list
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new WeekAdapter(getContext()));
        recyclerView.setNestedScrollingEnabled(false);

        // Load content
        loadData(false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @Override
    public void setData(Map<Integer, List<FoodItem>> data) {
        ((WeekAdapter) recyclerView.getAdapter()).setItems(data);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.load();
    }
}

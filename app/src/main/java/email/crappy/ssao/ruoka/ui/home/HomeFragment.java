package email.crappy.ssao.ruoka.ui.home;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.grandcentrix.thirtyinch.plugin.TiFragmentPlugin;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.ui.base.BaseFragment;
import email.crappy.ssao.ruoka.ui.list.WeekAdapter;
import email.crappy.ssao.ruoka.ui.view.DateBoxView;
import io.github.prashantsolanki3.shoot.Shoot;
import io.github.prashantsolanki3.shoot.listener.OnShootListener;
import timber.log.Timber;

/**
 * @author Santeri Elo
 */
public class HomeFragment extends BaseFragment implements HomeView {
    private static final String KEY_TYPE = "type";
    private final TiFragmentPlugin<HomePresenter, HomeView> presenterPlugin =
            new TiFragmentPlugin<>(() -> getComponent().homePresenter());
    private Unbinder unbinder;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MAIN, POUKAMA})
    @interface Type {
    }

    public static final int MAIN = 0;
    public static final int POUKAMA = 1;

    public static HomeFragment newInstance(@Type int type) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        addPlugin(presenterPlugin);
    }

    @BindView(R.id.root)
    FrameLayout rootLayout;

    @BindView(R.id.loadingView)
    ProgressBar progressBar;

    @BindView(R.id.errorView)
    TextView errorView;

    @BindView(R.id.contentView)
    NestedScrollView contentView;

    @BindView(R.id.recycler_weeks)
    RecyclerView recyclerView;

    @BindView(R.id.next_container)
    LinearLayout nextContainer;

    @BindView(R.id.next_empty)
    TextView nextEmpty;

    @BindView(R.id.next_date)
    DateBoxView dateBox;

    @BindView(R.id.next_food)
    TextView nextFood;

    @BindView(R.id.next_food_secondary)
    TextView nextFoodVeg;

    @BindView(R.id.ad_bottom)
    AdView bottomAd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(KEY_TYPE)) {
            //noinspection WrongConstant
            presenterPlugin.getPresenter().setType(getArguments().getInt(KEY_TYPE));
        } else {
            Timber.e(new NullPointerException("No type given"));
        }
    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        // Show sexy Toast about update
        Shoot.once(Shoot.APP_VERSION, "WHAT_IS_THIS", new OnShootListener() {
            @Override
            public void onExecute(int i, String s, int i1) {
                Toast.makeText(getContext(), R.string.app_update, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showNext(FoodItem next) {
        dateBox.setDay(next.date);
        dateBox.setDate(next.date);

        nextFood.setText(next.food);
        nextFoodVeg.setText(next.secondaryFood);

        nextEmpty.setVisibility(View.GONE);
        dateBox.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNextEmpty() {
        nextEmpty.setVisibility(View.VISIBLE);
        dateBox.setVisibility(View.GONE);
    }

    @Override
    public void loadAds() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("84275ACA55FEE01D25BACD7DC1A42F7A")
                .build();

        bottomAd.loadAd(adRequest);
    }

    @Override
    public void showLoading() {
        TransitionManager.beginDelayedTransition(rootLayout);
        contentView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
        TransitionManager.beginDelayedTransition(rootLayout);
        contentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);

        errorView.setText(throwable.getLocalizedMessage());
    }

    @Override
    public void showContent(Map<Integer, List<FoodItem>> items) {
        TransitionManager.beginDelayedTransition(rootLayout);
        contentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);

        ((WeekAdapter) recyclerView.getAdapter()).setItems(items);
    }
}
package email.crappy.ssao.ruoka.ui.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.SSKKYApplication;
import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.ui.list.adapter.WeekAdapter;

/**
 * @author Santeri 'iffa'
 */
public class ListFragment extends MvpFragment<ListView, ListPresenter> implements ListView {
    private static final String ARGS_SHOW_ADS = "ARGS_SHOW_ADS";
    @Bind(R.id.loading)
    View loading;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.adView)
    AdView adView;

    public static ListFragment newInstance(boolean showAds) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARGS_SHOW_ADS, showAds);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        WeekAdapter adapter = new WeekAdapter(getContext());
        recyclerView.setAdapter(adapter);

        presenter.loadContent();
    }

    @NonNull
    @Override
    public ListPresenter createPresenter() {
        return new ListPresenter(SSKKYApplication.get(getContext()).getComponent().dataManager());
    }

    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(Throwable throwable) {
        Snackbar.make(recyclerView, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
        loading.setVisibility(View.GONE);
    }

    @Override
    public void showContent(List<Week> weeks) {
        loading.setVisibility(View.GONE);

        ((WeekAdapter) recyclerView.getAdapter()).setItems(weeks);

        if (getArguments().getBoolean(ARGS_SHOW_ADS)) {
            adView.setVisibility(View.VISIBLE);
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("3E17D9B5B1509443815503025302253C")
                    .build();
            adView.loadAd(request);
        }
    }
}

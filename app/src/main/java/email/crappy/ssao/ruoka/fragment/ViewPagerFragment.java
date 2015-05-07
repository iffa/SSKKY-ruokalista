package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import butterknife.ButterKnife;
import butterknife.InjectView;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;
import email.crappy.ssao.ruoka.pojo.Ruoka;

/**
 * @author Santeri 'iffa'
 */
public class ViewPagerFragment extends Fragment {
    @InjectView(R.id.viewPager)
    ViewPager viewPager;

    @InjectView(R.id.viewPagerTab)
    SmartTabLayout viewPagerTab;

    public ViewPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentPagerItems items = FragmentPagerItems.with(getActivity().getApplicationContext()).create();

        // TODO: Fix orientation change bugs (emulator issue? confirmed not)
        for (Ruoka item : ((MainActivity)getActivity()).data.getRuoka()) {
            items.add(FragmentPagerItem.of(item.getTitle(), PageFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getFragmentManager(), items);
        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

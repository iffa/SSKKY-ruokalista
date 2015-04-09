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
import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class ViewPagerFragment extends Fragment {
    public ViewPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        ButterKnife.inject(this, view);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        FragmentPagerItems items = FragmentPagerItems.with(getActivity().getApplicationContext()).create();

        // TODO: Dynamically add items from the data
        items.add(FragmentPagerItem.of("Title", PageFragment.class));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getFragmentManager(), items);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewPagerTab);
        viewPagerTab.setViewPager(viewPager);

        return view;
    }
}

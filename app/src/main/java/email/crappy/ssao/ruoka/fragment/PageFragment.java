package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import icepick.Icepick;
import icepick.Icicle;

/**
 * TODO: Implement this class completely
 *
 * @author Santeri 'iffa'
 */
public class PageFragment extends Fragment {
    public PageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TODO: Use position to get data from Mainactivity.data
        int position = FragmentPagerItem.getPosition(getArguments());

        TextView debug = (TextView) view.findViewById(R.id.text_debug);
        TextView debug2 = (TextView) view.findViewById(R.id.text_debug2);
        debug.setText(String.valueOf(position));

        Ruoka ruoka = ((MainActivity)getActivity()).data.getRuoka().get(position);
        debug2.setText(ruoka.getTitle() + "\n" + ruoka.getUniqueId() + "\n");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


}

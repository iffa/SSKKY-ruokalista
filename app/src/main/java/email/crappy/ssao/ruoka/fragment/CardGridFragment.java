package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri 'iffa'
 */
public class CardGridFragment extends Fragment {


    public CardGridFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_grid, container, false);
        ButterKnife.inject(view);

        return view;
    }


}

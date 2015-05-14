package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.ButterKnife;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;
import email.crappy.ssao.ruoka.card.RuokaListCard;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import it.gmariotti.cardslib.library.extra.staggeredgrid.internal.CardGridStaggeredArrayAdapter;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import it.gmariotti.cardslib.library.internal.Card;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Card> cards = new ArrayList<>();
        MainActivity activity = (MainActivity) getActivity();

        for (Ruoka ruokaItem : activity.data.getRuoka()) {
            RuokaListCard card = new RuokaListCard(getActivity(), ruokaItem);
            card.init();
            cards.add(card);
        }

        CardGridStaggeredArrayAdapter mCardArrayAdapter = new CardGridStaggeredArrayAdapter(getActivity(), cards);
        CardGridStaggeredView mGridView = (CardGridStaggeredView) view.findViewById(R.id.card_grid);
        if (mGridView != null) {
            mGridView.setAdapter(mCardArrayAdapter);
        }
    }
}

package email.crappy.ssao.ruoka.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.ui.card.RuokaListCard;
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
        return inflater.inflate(R.layout.fragment_card_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Card> cards = new ArrayList<>();

        for (Ruoka ruokaItem : RuokaApplication.data.getRuoka()) {
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

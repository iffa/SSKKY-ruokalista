package email.crappy.ssao.ruoka.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.AnimationAdapter;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.RuokaApplication;
import email.crappy.ssao.ruoka.event.OpenShareDialogEvent;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.ui.card.RuokaListCard;
import email.crappy.ssao.ruoka.ui.fragment.dialog.ShareFoodDialogFragment;
import it.gmariotti.cardslib.library.extra.staggeredgrid.internal.CardGridStaggeredArrayAdapter;
import it.gmariotti.cardslib.library.extra.staggeredgrid.view.CardGridStaggeredView;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * @author Santeri 'iffa'
 */
public class DataFragment extends Fragment {
    public DataFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        ArrayList<Card> cards = new ArrayList<>();

        for (Ruoka ruokaItem : RuokaApplication.data.getRuoka()) {
            RuokaListCard card = new RuokaListCard(getActivity(), ruokaItem);
            card.init();
            cards.add(card);
        }

        CardGridStaggeredArrayAdapter mCardArrayAdapter = new CardGridStaggeredArrayAdapter(getActivity(), cards);
        AnimationAdapter animCardArrayAdapter = new AlphaInAnimationAdapter(mCardArrayAdapter);
        CardGridStaggeredView listView = (CardGridStaggeredView) getActivity().findViewById(R.id.card_grid);
        animCardArrayAdapter.setAbsListView(listView);
        listView.setExternalAdapter(animCardArrayAdapter, mCardArrayAdapter);
    }

    public void onEvent(OpenShareDialogEvent event) {
        ShareFoodDialogFragment.newInstance(event.dates).show(getFragmentManager(), "shareFoodDialog");
    }
}

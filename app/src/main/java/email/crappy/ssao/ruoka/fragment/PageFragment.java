package email.crappy.ssao.ruoka.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import com.dexafree.materialList.view.MaterialListView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import icepick.Icepick;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * TODO: Implement this class completely
 *
 * @author Santeri 'iffa'
 */
public class PageFragment extends Fragment {
    @InjectView(R.id.ruokaListView)
    MaterialListView ruokaListView;

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

        int position = FragmentPagerItem.getPosition(getArguments());
        Ruoka ruoka = ((MainActivity)getActivity()).data.getRuoka().get(position);

        ruokaListView.setItemAnimator(new LandingAnimator());

        for (Item item : ruoka.getItems()) {
            SmallImageCard card = new SmallImageCard(getActivity());
            card.setDescription(item.getKama());
            card.setTitle(item.getPaiva() + " - " + item.getPvm());
            card.setDrawable(R.drawable.ic_launcher);
            card.setDismissible(false);

            ruokaListView.add(card);
        }

        /*
        ruokaListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(CardItemView view, int position) {
            }

            @Override
            public void onItemLongClick(CardItemView view, int position) {
            }
        });
        */

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }


}

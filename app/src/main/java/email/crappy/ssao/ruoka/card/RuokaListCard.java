package email.crappy.ssao.ruoka.card;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.activity.MainActivity;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 *
 * @author Santeri 'iffa'
 */
public class RuokaListCard extends CardWithList {
    private Ruoka ruoka;

    public RuokaListCard(Context context, Ruoka ruoka) {
        super(context);
        this.ruoka = ruoka;
    }

    @Override
    protected CardHeader initCardHeader() {
        CardHeader header = new CardHeader(getContext());
        header.setTitle(ruoka.getTitle());
        return header;
    }

    @Override
    protected void initCard() {
        setSwipeable(false);
    }

    @Override
    protected List<ListObject> initChildren() {
        List<ListObject> mObjects = new ArrayList<ListObject>();

        for(Item item : ruoka.getItems()) {
            if (item.getKama().toLowerCase().contains("talon tapaan")) {
                item.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(LinearListView linearListView, View view, int i, ListObject listObject) {
                        EventBus.getDefault().post(new EasterEggEvent());
                    }
                });
            }
            mObjects.add(item);

        }

        return mObjects;
    }

    @Override
    public View setupChildView(int childPosition, ListObject object, View view, ViewGroup parent) {
        TextView dayText = (TextView) view.findViewById(R.id.item_day);
        TextView dateText = (TextView) view.findViewById(R.id.item_date);
        TextView foodText = (TextView) view.findViewById(R.id.item_food);

        // Setting the text for all TextViews from the item
        Item item = (Item) object;
        dayText.setText(item.getPaiva());
        dateText.setText(item.getPvm());
        foodText.setText(item.getKama());

        if (MainActivity.isToday(item.getPvm())) {
            // Setting today bold
            dayText.setTypeface(null, Typeface.BOLD);
            dateText.setTypeface(null, Typeface.BOLD);
            dayText.setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));
            dateText.setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));
        }

        return view;
    }


    @Override
    public int getChildLayoutId() {
        return R.layout.card_list_item;
    }
}

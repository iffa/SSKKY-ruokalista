package email.crappy.ssao.ruoka.ui.card;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.event.EasterEggEvent;
import email.crappy.ssao.ruoka.event.OpenShareDialogEvent;
import email.crappy.ssao.ruoka.event.ShareFoodEvent;
import email.crappy.ssao.ruoka.pojo.Item;
import email.crappy.ssao.ruoka.pojo.Ruoka;
import email.crappy.ssao.ruoka.util.DateUtil;
import in.raveesh.proteus.ImageView;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import it.gmariotti.cardslib.library.prototypes.LinearListView;

/**
 * @author Santeri 'iffa'
 */
public class RuokaListCard extends CardWithList implements CardWithList.OnItemClickListener, CardHeader.OnClickCardHeaderPopupMenuListener {
    private Ruoka ruoka;

    public RuokaListCard(Context context, Ruoka ruoka) {
        super(context);
        EventBus.getDefault().register(this);
        this.ruoka = ruoka;
    }

    @Override
    protected CardHeader initCardHeader() {
        CardHeader header = new CardHeader(getContext());
        header.setPopupMenu(R.menu.menu_card, this);
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

        for (Item item : ruoka.getItems()) {
            if (item.getKama().toLowerCase().contains("talon tapaan")) {
                item.setOnItemClickListener(this);
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
        ImageView foodIcon = (ImageView) view.findViewById(R.id.item_food_type);

        // Setting the text for all TextViews from the item
        Item item = (Item) object;
        dayText.setText(item.getPaiva());
        dateText.setText(item.getPvm());
        foodText.setText(item.getKama());

        if (DateUtil.isToday(item.getPvm())) {
            // Setting today bold
            dayText.setTypeface(null, Typeface.BOLD);
            dateText.setTypeface(null, Typeface.BOLD);
            dayText.setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));
            dateText.setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));
        }

        // Setting food icon to match the food
        if (item.getKama().toLowerCase().contains("keitto")) {
            foodIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_spoon));
            foodIcon.setPaintResource(R.color.gold);
        } else if (item.getKama().toLowerCase().contains("pasta")) {
            foodIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pasta));
            foodIcon.setPaintResource(R.color.gold);
        } else if ((item.getKama().toLowerCase().contains("kala") && !item.getKama().toLowerCase().contains("kreikkalainen")) || item.getKama().toLowerCase().contains("lohi")) {
            foodIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_fish));
            foodIcon.setPaintResource(R.color.gold);
        } else if (item.getKama().toLowerCase().contains("broiler")) {
            foodIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_chicken));
            foodIcon.setPaintResource(R.color.gold);
        } else if (item.getKama().toLowerCase().contains("pihvi")) {
            foodIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_steak));
            foodIcon.setPaintResource(R.color.gold);
        }


        return view;
    }


    @Override
    public int getChildLayoutId() {
        return R.layout.card_list_item;
    }

    @Override
    public void onItemClick(LinearListView linearListView, View view, int i, ListObject listObject) {
        EventBus.getDefault().post(new EasterEggEvent(false));
    }

    @Override
    public void onMenuItemClick(BaseCard baseCard, MenuItem menuItem) {
        ArrayList<String> dates = new ArrayList<String>();
        for (Item item : ruoka.getItems()) {
            dates.add(item.getPaiva() + " " + item.getPvm());
        }
        switch (menuItem.getItemId()) {
            case R.id.action_share:
                EventBus.getDefault().post(new OpenShareDialogEvent(dates));
                break;
        }
    }

    public void onEvent(ShareFoodEvent event) {
        String date = event.day.toString();

        for (Item item : ruoka.getItems()) {
            if (date.contains(item.getPvm())) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ruokana " + item.getPvm());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.getKama());
                getContext().startActivity(Intent.createChooser(sharingIntent, getContext().getResources().getString(R.string.share_via)));
                break;
            }
        }
    }
}

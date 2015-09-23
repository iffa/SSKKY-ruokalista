package email.crappy.ssao.ruoka.recycler;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class SelectedWeekAdapter extends AbsListViewAnnotatedAdapter implements SelectedWeekAdapterBinder {
    @ViewType(
            layout = R.layout.item_current_food_card,
            views = {
                    @ViewField(id = R.id.item_day, name = "day", type = TextView.class),
                    @ViewField(id = R.id.item_date, name = "date", type = TextView.class),
                    @ViewField(id = R.id.item_food, name = "food", type = TextView.class),
                    @ViewField(id = R.id.item_food_type, name = "foodType", type = ImageView.class)
            }
    )
    public final int defaultType = 0;

    List<Item> items;

    public SelectedWeekAdapter(Context context) {
        super(context);
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public void bindViewHolder(SelectedWeekAdapterHolders.DefaultTypeViewHolder vh, int position) {
        Item item = items.get(position);

        vh.date.setText(item.getPvm());
        vh.day.setText(item.getPaiva());
        vh.food.setText(item.getKama());

        // Highlight today's food in the list
        if (DateUtil.isToday(item.getPvm())) {
            vh.date.setTypeface(null, Typeface.BOLD);
            vh.day.setTypeface(null, Typeface.BOLD);
            vh.food.setTypeface(null, Typeface.BOLD);
        } else { // To fix random items appearing bold >:(
            vh.date.setTypeface(null, Typeface.NORMAL);
            vh.day.setTypeface(null, Typeface.NORMAL);
            vh.food.setTypeface(null, Typeface.NORMAL);
        }

        // TODO: image switch case, copy pasta from V2
        // Setting food icon to match the food
        if (item.getKama().toLowerCase().contains("keitto")) {
            vh.foodType.setImageDrawable(vh.foodType.getResources().getDrawable(R.drawable.ic_spoon));
        } else if (item.getKama().toLowerCase().contains("pasta")) {
            vh.foodType.setImageDrawable(vh.foodType.getResources().getDrawable(R.drawable.ic_pasta));
        } else if ((item.getKama().toLowerCase().contains("kala") && !item.getKama().toLowerCase().contains("kreikkalainen")) || item.getKama().toLowerCase().contains("lohi")) {
            vh.foodType.setImageDrawable(vh.foodType.getResources().getDrawable(R.drawable.ic_fish));
        } else if (item.getKama().toLowerCase().contains("broiler")) {
            vh.foodType.setImageDrawable(vh.foodType.getResources().getDrawable(R.drawable.ic_chicken));
        } else if (item.getKama().toLowerCase().contains("pihvi")) {
            vh.foodType.setImageDrawable(vh.foodType.getResources().getDrawable(R.drawable.ic_steak));
        }
    }
}

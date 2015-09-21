package email.crappy.ssao.ruoka.recycler;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class SelectedWeekAdapter extends SupportAnnotatedAdapter implements SelectedWeekAdapterBinder {
    @ViewType(
            layout = R.layout.item_list_current_week,
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

    public SelectedWeekAdapter(Context context, List<Item> items) {
        super(context);
        this.items = items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
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
    }
}

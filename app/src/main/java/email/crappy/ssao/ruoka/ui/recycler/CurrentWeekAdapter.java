package email.crappy.ssao.ruoka.ui.recycler;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.pojo.Item;

/**
 * @author Santeri Elo
 */
public class CurrentWeekAdapter extends SupportAnnotatedAdapter implements CurrentWeekAdapterBinder {
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

    public CurrentWeekAdapter(Context context, List<Item> items) {
        super(context);
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void bindViewHolder(CurrentWeekAdapterHolders.DefaultTypeViewHolder vh, int position) {
        Item item = items.get(position);

        vh.date.setText(item.getPvm());
        vh.day.setText(item.getPaiva());
        vh.food.setText(item.getKama());

        // TODO: image switch case
    }
}

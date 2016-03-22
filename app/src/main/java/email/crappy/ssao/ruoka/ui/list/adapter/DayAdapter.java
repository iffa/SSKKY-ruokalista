package email.crappy.ssao.ruoka.ui.list.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.Day;
import email.crappy.ssao.ruoka.data.util.DateUtil;

/**
 * @author Santeri 'iffa'
 */
public class DayAdapter extends SupportAnnotatedAdapter implements DayAdapterBinder {
    @ViewType(
            layout = R.layout.item_day,
            views = {
                    @ViewField(id = R.id.date, name = "date", type = TextView.class),
                    @ViewField(id = R.id.day, name = "day", type = TextView.class),
                    @ViewField(id = R.id.food, name = "food", type = TextView.class),
                    @ViewField(id = R.id.food_veg, name = "foodVeg", type = TextView.class),
                    @ViewField(id = R.id.icon, name = "icon", type = ImageView.class)
            }
    )
    public final int item = 0;
    private List<Day> days;

    public DayAdapter(Context context, List<Day> days) {
        super(context);
        this.days = days;
    }

    public DayAdapter(Context context) {
        super(context);
    }

    public void setItems(List<Day> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return days == null ? 0 : days.size();
    }

    @Override
    public void bindViewHolder(DayAdapterHolders.ItemViewHolder vh, int position) {
        Day day = days.get(position);
        String[] food = day.food.split("\\n");

        vh.date.setText(day.date);
        vh.day.setText(day.day);
        vh.food.setText(food[0]);
        vh.foodVeg.setText((food.length > 1 ? food[1] : ""));

        if (DateUtil.isToday(day.date, false)) {
            vh.food.setTypeface(null, Typeface.BOLD);
        } else {
            vh.food.setTypeface(null, Typeface.NORMAL);
        }
    }
}

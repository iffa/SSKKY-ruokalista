package email.crappy.ssao.ruoka.ui.list;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.data.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class DayAdapter extends SupportAnnotatedAdapter implements DayAdapterBinder {
    @ViewType(
            layout = R.layout.item_day,
            views = {
                    @ViewField(id = R.id.day, name = "day", type = TextView.class),
                    @ViewField(id = R.id.food, name = "food", type = TextView.class),
                    @ViewField(id = R.id.food_veg, name = "foodVeg", type = TextView.class),
            }
    )
    public final int item = 0;
    private List<FoodItem> days;

    public DayAdapter(Context context) {
        super(context);
    }

    public void setItems(List<FoodItem> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return days == null ? 0 : days.size();
    }

    @Override
    public void bindViewHolder(DayAdapterHolders.ItemViewHolder vh, int position) {
        FoodItem day = days.get(position);

        vh.day.setText(vh.day.getResources().getString(DateUtil.getDayResource(day.date)));
        vh.food.setText(day.food);

        if (day.secondaryFood.isEmpty()) {
            vh.foodVeg.setVisibility(View.GONE);
        } else {
            vh.foodVeg.setVisibility(View.VISIBLE);
            vh.foodVeg.setText(day.secondaryFood);
        }
    }
}
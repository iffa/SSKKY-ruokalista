package email.crappy.ssao.ruoka.ui.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;
import java.util.Map;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.FoodItem;
import email.crappy.ssao.ruoka.data.util.DateUtil;
import email.crappy.ssao.ruoka.ui.view.DashedItemDecorator;

/**
 * @author Santeri Elo
 */
public class WeekAdapter extends SupportAnnotatedAdapter implements WeekAdapterBinder {
    @ViewType(
            layout = R.layout.item_week,
            views = {
                    @ViewField(id = R.id.week, name = "title", type = TextView.class),
                    @ViewField(id = R.id.list_days, name = "list", type = RecyclerView.class)
            }
    )
    public final int item = 0;

    private Map<Integer, List<FoodItem>> weekMap;

    public WeekAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return weekMap == null ? 0 : weekMap.size();
    }

    @Override
    public void bindViewHolder(WeekAdapterHolders.ItemViewHolder vh, int position) {
        // Suspicious :-)
        @SuppressWarnings("SuspiciousMethodCalls")
        List<FoodItem> foodItems = weekMap.get(weekMap.keySet().toArray()[position]);

        vh.title.setText(vh.title.getResources().getString(R.string.week, DateUtil.getWeekNumber(foodItems.get(0).date)));

        if (vh.list.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getInflater().getContext(), LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            layoutManager.setAutoMeasureEnabled(true);
            vh.list.setLayoutManager(layoutManager);
            vh.list.setNestedScrollingEnabled(false);

            // Set a sexy divider
            vh.list.addItemDecoration(new DashedItemDecorator());
        }

        if (vh.list.getAdapter() == null) {
            vh.list.setAdapter(new DayAdapter(getInflater().getContext()));
        }

        ((DayAdapter) vh.list.getAdapter()).setItems(foodItems);
    }

    public void setItems(Map<Integer, List<FoodItem>> items) {
        this.weekMap = items;
        notifyItemRangeChanged(0, items.size());
    }
}


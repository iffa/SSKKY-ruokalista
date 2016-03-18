package email.crappy.ssao.ruoka.ui.list.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.data.model.Week;
import email.crappy.ssao.ruoka.ui.base.DividerItemDecoration;

/**
 * @author Santeri 'iffa'
 */
public class WeekAdapter extends SupportAnnotatedAdapter implements WeekAdapterBinder {
    @ViewType(
            layout = R.layout.item_week,
            views = {
                    @ViewField(id = R.id.week_title, name = "title", type = TextView.class),
                    @ViewField(id = R.id.list_days, name = "list", type = RecyclerView.class)
            }
    )
    public final int item = 0;
    private List<Week> items;

    public WeekAdapter(Context context, List<Week> items) {
        super(context);
        this.items = items;
    }

    public WeekAdapter(Context context) {
        super(context);
    }

    public void setItems(List<Week> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void bindViewHolder(WeekAdapterHolders.ItemViewHolder vh, int position) {
        Week week = items.get(position);

        vh.title.setText(week.title);

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
            vh.list.addItemDecoration(new DividerItemDecoration(getInflater().getContext(),
                    DividerItemDecoration.VERTICAL_LIST));
        }

        if (vh.list.getAdapter() == null) {
            vh.list.setAdapter(new DayAdapter(getInflater().getContext()));
        }

        ((DayAdapter) vh.list.getAdapter()).setItems(week.days);
    }
}

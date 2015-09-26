package email.crappy.ssao.ruoka.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.AbsListViewAnnotatedAdapter;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class AllWeeksAdapter extends AbsListViewAnnotatedAdapter implements AllWeeksAdapterBinder {
    @ViewType(
            layout = R.layout.item_list_other_weeks_card,
            views = {
                    @ViewField(id = R.id.item_week, name = "week", type = TextView.class),
                    @ViewField(id = R.id.card_other_week_layout, name = "layout", type = CardView.class)
            }
    )
    public final int defaultType = 0;
    private List<String> items;

    public AllWeeksAdapter(Context context) {
        super(context);
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void bindViewHolder(AllWeeksAdapterHolders.DefaultTypeViewHolder vh, int position) {
        String item = items.get(position);

        vh.week.setText(item);
        Logger.d("bindViewHolder in AllWeeksAdapter, position " + position);

        String[] titleSplit = item.split("\\s+");

        if (DateUtil.isCurrentWeek(titleSplit[1])) {
            Logger.d("Found current week in AllWeeksAdapter");
            vh.week.setTypeface(null, Typeface.BOLD);
        } else {
            vh.week.setTypeface(null, Typeface.NORMAL);
        }
    }
}

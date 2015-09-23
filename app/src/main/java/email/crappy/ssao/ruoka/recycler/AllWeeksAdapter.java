package email.crappy.ssao.ruoka.recycler;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

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
public class AllWeeksAdapter extends SupportAnnotatedAdapter implements AllWeeksAdapterBinder {
    @ViewType(
            layout = R.layout.item_list_other_weeks_card,
            views = {
                    @ViewField(id = R.id.item_week, name = "week", type = TextView.class)
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

    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void bindViewHolder(AllWeeksAdapterHolders.DefaultTypeViewHolder vh, int position) {
        String item = items.get(position);

        vh.week.setText(item);
        Logger.d("bindViewHolder in AllWeeksAdapter, position " + position);

        String[] titleSplit = item.split("\\s+");
        Logger.d("titleSplit[1] = " + titleSplit[1]);

        if (DateUtil.isCurrentWeek(titleSplit[1])) {
            vh.week.setTypeface(null, Typeface.BOLD);
            vh.itemView.setClickable(false);
        } else {
            vh.week.setTypeface(null, Typeface.NORMAL);
            vh.itemView.setClickable(true);
        }
    }
}

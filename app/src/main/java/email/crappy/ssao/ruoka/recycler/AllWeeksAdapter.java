package email.crappy.ssao.ruoka.recycler;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import email.crappy.ssao.ruoka.R;

/**
 * @author Santeri Elo
 */
public class AllWeeksAdapter extends SupportAnnotatedAdapter implements AllWeeksAdapterBinder {
    @ViewType(
            layout = R.layout.item_list_other_week,
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

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public void bindViewHolder(AllWeeksAdapterHolders.DefaultTypeViewHolder vh, int position) {
        String item = items.get(position);

        vh.week.setText(item);
        Logger.d("bindViewHolder in AllWeeksAdapter, position " + position);
    }
}

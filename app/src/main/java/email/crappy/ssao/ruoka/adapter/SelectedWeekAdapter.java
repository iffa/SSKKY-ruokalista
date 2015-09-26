package email.crappy.ssao.ruoka.adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.ui.BlurTransformation;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class SelectedWeekAdapter extends SupportAnnotatedAdapter implements SelectedWeekAdapterBinder {
    @ViewType(
            layout = R.layout.item_food_experimental,
            views = {
                    //@ViewField(id = R.id.item, name = "day", type = TextView.class),
                    @ViewField(id = R.id.item_date, name = "date", type = TextView.class),
                    @ViewField(id = R.id.item_info_layout, name = "infoBoxLayout", type = LinearLayout.class),
                    @ViewField(id = R.id.item_food_main, name = "mainFood", type = TextView.class),
                    @ViewField(id = R.id.item_shadow, name = "shadow", type = View.class),
                    @ViewField(id = R.id.item_food_secondary, name = "secondaryFood", type = TextView.class),
                    @ViewField(id = R.id.item_background, name = "background", type = ImageView.class)
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
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public void bindViewHolder(final SelectedWeekAdapterHolders.DefaultTypeViewHolder vh, int position) {
        Item item = items.get(position);

        vh.date.setText(item.getPaiva() + " " + item.getPvm());

        String main = item.getKama().split(",")[0];
        String secondary = item.getKama().replace(main + ", ", "");

        vh.mainFood.setText(main);
        vh.secondaryFood.setText(secondary);

        // Highlight today's food in the list
        // Debug: highlight first one
        if (DateUtil.isToday(item.getPvm()) || position == 2) {
            Picasso.with(getInflater().getContext())
                    .load(getBackground(item.getKama()))
                    .into(vh.background, new Callback() {
                        @Override
                        public void onSuccess() {
                            vh.shadow.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            Picasso.with(getInflater().getContext())
                    .load(getBackground(item.getKama()))
                    .transform(new BlurTransformation(15))
                    .into(vh.background, new Callback() {
                        @Override
                        public void onSuccess() {
                            vh.shadow.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }


        // TODO: change background image based on food type? idk
        // Setting food icon to match the food
        /*
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
        */
    }

    private int getBackground(String food) {
        if (food.toLowerCase().contains("keitto")) {
            return R.drawable.bg_soup;
        } else if (food.toLowerCase().contains("pasta")) {
            return R.drawable.bg_pasta;
        } else if ((food.toLowerCase().contains("kala") && !food.toLowerCase().contains("kreikkalainen")) || food.toLowerCase().contains("lohi")) {
            return R.drawable.bg_fish;
        } else if (food.toLowerCase().contains("broiler")) {
            return R.drawable.bg_chicken;
        } else if (food.toLowerCase().contains("pihvi")) {
            return R.drawable.bg_steak;
        }
        return R.drawable.bg_cutlery;
    }
}

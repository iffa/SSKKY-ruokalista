package email.crappy.ssao.ruoka.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import email.crappy.ssao.ruoka.R;
import email.crappy.ssao.ruoka.model.Item;
import email.crappy.ssao.ruoka.model.Ruoka;
import email.crappy.ssao.ruoka.util.DateUtil;

/**
 * @author Santeri Elo
 */
public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.ViewHolder> {
    List<Ruoka> items;

    public void setItems(List<Ruoka> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public WeekAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_week, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(WeekAdapter.ViewHolder vh, int position) {
        Ruoka ruoka = items.get(position);
        List<Item> items = ruoka.getItems();

        vh.title.setText(ruoka.getTitle());
        String week = ruoka.getTitle().split("\\s+")[1];
        if (DateUtil.isCurrentWeek(week)) {
            vh.title.setTypeface(null,  Typeface.BOLD);
        } else {
            vh.title.setTypeface(null,  Typeface.NORMAL);
        }

        for (Item item : items) {
            int pos = items.indexOf(item);

            vh.getDate(pos).setText(item.getPaiva() + " " + item.getPvm());

            String main = item.getKama().split(",")[0];
            String secondary = item.getKama().replace(main + ", ", "");

            vh.getMainFood(pos).setText(main);
            vh.getSecondaryFood(pos).setText(secondary);

            // Highlight today's food in the list
            if (DateUtil.isToday(item.getPvm()) || DateUtil.isAfterTodaySunday(item.getPvm())) {
                vh.getDate(pos).setTypeface(null, Typeface.BOLD);
                vh.getMainFood(pos).setTypeface(null, Typeface.BOLD);
            } else {
                vh.getDate(pos).setTypeface(null, Typeface.NORMAL);
                vh.getMainFood(pos).setTypeface(null, Typeface.NORMAL);
            }

            if (item.getKama().toLowerCase().contains("keitto")) {
                vh.getFoodType(pos).setImageDrawable(vh.itemView.getResources().getDrawable(R.drawable.ic_spoon));
            } else if (item.getKama().toLowerCase().contains("pasta")) {
                vh.getFoodType(pos).setImageDrawable(vh.itemView.getResources().getDrawable(R.drawable.ic_pasta));
            } else if ((item.getKama().toLowerCase().contains("kala") && !item.getKama().toLowerCase().contains("kreikkalainen")) || item.getKama().toLowerCase().contains("lohi")) {
                vh.getFoodType(pos).setImageDrawable(vh.itemView.getResources().getDrawable(R.drawable.ic_fish));
            } else if (item.getKama().toLowerCase().contains("broiler")) {
                vh.getFoodType(pos).setImageDrawable(vh.itemView.getResources().getDrawable(R.drawable.ic_chicken));
            } else if (item.getKama().toLowerCase().contains("pihvi")) {
                vh.getFoodType(pos).setImageDrawable(vh.itemView.getResources().getDrawable(R.drawable.ic_steak));
            }
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        LinearLayout cardLayout;

        TextView date1;
        TextView mainFood1;
        TextView secondaryFood1;
        ImageView foodType1;

        TextView date2;
        TextView mainFood2;
        TextView secondaryFood2;
        ImageView foodType2;

        TextView date3;
        TextView mainFood3;
        TextView secondaryFood3;
        ImageView foodType3;

        TextView date4;
        TextView mainFood4;
        TextView secondaryFood4;
        ImageView foodType4;

        TextView date5;
        TextView mainFood5;
        TextView secondaryFood5;
        ImageView foodType5;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_header_title);
            cardLayout = (LinearLayout) itemView.findViewById(R.id.layout_card);

            date1 = (TextView) itemView.findViewById(R.id.item_date1);
            date2 = (TextView) itemView.findViewById(R.id.item_date2);
            date3 = (TextView) itemView.findViewById(R.id.item_date3);
            date4 = (TextView) itemView.findViewById(R.id.item_date4);
            date5 = (TextView) itemView.findViewById(R.id.item_date5);

            mainFood1 = (TextView) itemView.findViewById(R.id.item_food_main1);
            mainFood2 = (TextView) itemView.findViewById(R.id.item_food_main2);
            mainFood3 = (TextView) itemView.findViewById(R.id.item_food_main3);
            mainFood4 = (TextView) itemView.findViewById(R.id.item_food_main4);
            mainFood5 = (TextView) itemView.findViewById(R.id.item_food_main5);

            secondaryFood1 = (TextView) itemView.findViewById(R.id.item_food_secondary1);
            secondaryFood2 = (TextView) itemView.findViewById(R.id.item_food_secondary2);
            secondaryFood3 = (TextView) itemView.findViewById(R.id.item_food_secondary3);
            secondaryFood4 = (TextView) itemView.findViewById(R.id.item_food_secondary4);
            secondaryFood5 = (TextView) itemView.findViewById(R.id.item_food_secondary5);

            foodType1 = (ImageView) itemView.findViewById(R.id.item_food_type1);
            foodType2 = (ImageView) itemView.findViewById(R.id.item_food_type2);
            foodType3 = (ImageView) itemView.findViewById(R.id.item_food_type3);
            foodType4 = (ImageView) itemView.findViewById(R.id.item_food_type4);
            foodType5 = (ImageView) itemView.findViewById(R.id.item_food_type5);
        }

        public TextView getDate(int pos) {
            switch (pos) {
                case 0:
                    return date1;
                case 1:
                    return date2;
                case 2:
                    return date3;
                case 3:
                    return date4;
                case 4:
                    return date5;
            }
            return null;
        }

        public TextView getMainFood(int pos) {
            switch (pos) {
                case 0:
                    return mainFood1;
                case 1:
                    return mainFood2;
                case 2:
                    return mainFood3;
                case 3:
                    return mainFood4;
                case 4:
                    return mainFood5;
            }
            return null;
        }

        public TextView getSecondaryFood(int pos) {
            switch (pos) {
                case 0:
                    return secondaryFood1;
                case 1:
                    return secondaryFood2;
                case 2:
                    return secondaryFood3;
                case 3:
                    return secondaryFood4;
                case 4:
                    return secondaryFood5;
            }
            return null;
        }

        public ImageView getFoodType(int pos) {
            switch (pos) {
                case 0:
                    return foodType1;
                case 1:
                    return foodType2;
                case 2:
                    return foodType3;
                case 3:
                    return foodType4;
                case 4:
                    return foodType5;
            }
            return null;
        }
    }
}
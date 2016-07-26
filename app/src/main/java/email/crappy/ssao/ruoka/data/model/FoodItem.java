package email.crappy.ssao.ruoka.data.model;

import java.util.Date;

/**
 * @author Santeri Elo
 */
public class FoodItem {
    public final Date date;
    public final String food;
    public final String secondaryFood;

    public FoodItem(Date date, String food, String secondaryFood) {
        this.date = date;
        this.food = food;
        this.secondaryFood = secondaryFood;
    }

    @Override
    public String toString() {
        return String.format("FoodItem[date='%s', food='%s', secondaryFood='%s']", date.toString(), food, secondaryFood);
    }
}

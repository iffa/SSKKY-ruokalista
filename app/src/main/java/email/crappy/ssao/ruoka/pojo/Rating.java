package email.crappy.ssao.ruoka.pojo;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

/**
 * @author Santeri 'iffa'
 */
public class Rating extends ParseObject {

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getOpinion() {
        return getString("opinion");
    }

    public void setOpinion(String opinion) {
        put("opinion", opinion);
    }

    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        put("date", date);
    }

    public String getFood() {
        return getString("food");
    }

    public void setFood(String food) {
        put("food", food);
    }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }

    public static ParseQuery<Rating> getQuery() {
        return ParseQuery.getQuery(Rating.class);
    }
}

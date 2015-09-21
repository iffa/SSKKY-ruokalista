package email.crappy.ssao.ruoka.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * @author Santeri 'iffa'
 */
@ParseClassName("Rating")
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

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public static ParseQuery<Rating> getQuery() {
        return ParseQuery.getQuery(Rating.class);
    }
}

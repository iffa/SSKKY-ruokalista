
package email.crappy.ssao.ruoka.data.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Week {

    @SerializedName("title")
    public String title;
    @SerializedName("days")
    public List<Day> days = new ArrayList<Day>();

}

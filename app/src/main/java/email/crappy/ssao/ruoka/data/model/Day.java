
package email.crappy.ssao.ruoka.data.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Day {

    @SerializedName("pvm")
    @Expose
    public String date;
    @SerializedName("paiva")
    @Expose
    public String day;
    @SerializedName("kama")
    @Expose
    public String food;

}

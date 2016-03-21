
package email.crappy.ssao.ruoka.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class ListResponse {

    @SerializedName("expirationDate")
    public Date expirationDate;
    @SerializedName("weeks")
    public List<Week> weeks = new ArrayList<Week>();

}

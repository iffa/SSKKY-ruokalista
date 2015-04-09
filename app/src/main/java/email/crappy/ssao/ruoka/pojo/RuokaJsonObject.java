package email.crappy.ssao.ruoka.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class RuokaJsonObject {
    @Expose
    private List<Ruoka> ruoka = new ArrayList<Ruoka>();

    public List<Ruoka> getRuoka() {
        return ruoka;
    }

    public void setRuoka(List<Ruoka> ruoka) {
        this.ruoka = ruoka;
    }

}

package email.crappy.ssao.ruoka.pojo;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class Ruoka {

    @Expose
    private String uniqueId;
    @Expose
    private String title;
    @Expose
    private List<Item> items = new ArrayList<Item>();

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}

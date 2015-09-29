package email.crappy.ssao.ruoka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

// TODO: Get rid of uniqueId (useless leftover from Windows Phone)
public class Ruoka implements Parcelable {

    @Expose
    private String uniqueId;
    @Expose
    private String title;
    @Expose
    private List<Item> items = new ArrayList<Item>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItems() {
        return items;
    }

    protected Ruoka(Parcel in) {
        uniqueId = in.readString();
        title = in.readString();
        if (in.readByte() == 0x01) {
            items = new ArrayList<Item>();
            in.readList(items, Item.class.getClassLoader());
        } else {
            items = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uniqueId);
        dest.writeString(title);
        if (items == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(items);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ruoka> CREATOR = new Parcelable.Creator<Ruoka>() {
        @Override
        public Ruoka createFromParcel(Parcel in) {
            return new Ruoka(in);
        }

        @Override
        public Ruoka[] newArray(int size) {
            return new Ruoka[size];
        }
    };
}
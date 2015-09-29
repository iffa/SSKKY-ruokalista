package email.crappy.ssao.ruoka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

public class Item implements Parcelable {

    @Expose
    private String pvm;
    @Expose
    private String paiva;
    @Expose
    private String kama;

    public String getPvm() {
        return pvm;
    }

    public String getPaiva() {
        return paiva;
    }

    public String getKama() {
        return kama;
    }

    protected Item(Parcel in) {
        pvm = in.readString();
        paiva = in.readString();
        kama = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pvm);
        dest.writeString(paiva);
        dest.writeString(kama);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
package email.crappy.ssao.ruoka.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class RuokaJsonObject implements Parcelable {
    @Expose
    private List<Ruoka> ruoka = new ArrayList<Ruoka>();

    public List<Ruoka> getRuoka() {
        return ruoka;
    }

    public void setRuoka(List<Ruoka> ruoka) {
        this.ruoka = ruoka;
    }

    protected RuokaJsonObject(Parcel in) {
        if (in.readByte() == 0x01) {
            ruoka = new ArrayList<Ruoka>();
            in.readList(ruoka, Ruoka.class.getClassLoader());
        } else {
            ruoka = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (ruoka == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ruoka);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RuokaJsonObject> CREATOR = new Parcelable.Creator<RuokaJsonObject>() {
        @Override
        public RuokaJsonObject createFromParcel(Parcel in) {
            return new RuokaJsonObject(in);
        }

        @Override
        public RuokaJsonObject[] newArray(int size) {
            return new RuokaJsonObject[size];
        }
    };
}
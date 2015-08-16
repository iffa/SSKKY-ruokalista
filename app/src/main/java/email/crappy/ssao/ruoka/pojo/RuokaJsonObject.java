package email.crappy.ssao.ruoka.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class RuokaJsonObject implements Parcelable {
    @Expose
    private List<Ruoka> ruoka = new ArrayList<Ruoka>();
    @Expose
    private String expiration;

    public List<Ruoka> getRuoka() {
        return ruoka;
    }

    public void setRuoka(List<Ruoka> ruoka) {
        this.ruoka = ruoka;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    protected RuokaJsonObject(Parcel in) {
        if (in.readByte() == 0x01) {
            ruoka = new ArrayList<Ruoka>();
            in.readList(ruoka, Ruoka.class.getClassLoader());
        } else {
            ruoka = null;
        }
        expiration = in.readString();
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
        dest.writeString(expiration);
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
package email.crappy.ssao.ruoka.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.prototypes.CardWithList;

public class Item implements Parcelable, CardWithList.ListObject {
    CardWithList.OnItemClickListener mClickListener;

    @Expose
    private String pvm;
    @Expose
    private String paiva;
    @Expose
    private String kama;

    public String getPvm() {
        return pvm;
    }

    public void setPvm(String pvm) {
        this.pvm = pvm;
    }

    public String getPaiva() {
        return paiva;
    }

    public void setPaiva(String paiva) {
        this.paiva = paiva;
    }

    public String getKama() {
        return kama;
    }

    public void setKama(String kama) {
        this.kama = kama;
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

    @Override
    public String getObjectId() {
        return pvm;
    }

    @Override
    public Card getParentCard() {
        return null;
    }

    @Override
    public void setOnItemClickListener(CardWithList.OnItemClickListener onItemClickListener) {
        mClickListener = onItemClickListener;
    }

    @Override
    public CardWithList.OnItemClickListener getOnItemClickListener() {
        return mClickListener;
    }

    @Override
    public boolean isSwipeable() {
        return false;
    }

    @Override
    public void setSwipeable(boolean b) {

    }

    @Override
    public CardWithList.OnItemSwipeListener getOnItemSwipeListener() {
        return null;
    }

    @Override
    public void setOnItemSwipeListener(CardWithList.OnItemSwipeListener onItemSwipeListener) {

    }
}
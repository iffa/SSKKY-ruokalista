package email.crappy.ssao.ruoka.pojo;

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;

@Parcel
public class Item {

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

}

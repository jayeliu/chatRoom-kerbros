package bin;
import DES.DES;

public class Authenticator {
    String k;
    String id_c;
    String ad_c;
    String ts;

    public Authenticator(String k, String id_c, String ad_c, String ts) {
        this.k = k;
        this.id_c = id_c;
        this.ad_c = ad_c;
        this.ts = ts;
    }

    @Override
    public String toString() {
        try {
            return DES.DES(String.join(",",id_c , ad_c ,ts ),k);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getId_c() {
        return id_c;
    }

    public void setId_c(String id_c) {
        this.id_c = id_c;
    }

    public String getAd_c() {
        return ad_c;
    }

    public void setAd_c(String ad_c) {
        this.ad_c = ad_c;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}

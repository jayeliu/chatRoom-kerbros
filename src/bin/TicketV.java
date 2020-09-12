package bin;
import DES.DES;

public class TicketV {
    String kc_v;
    String k_v;
    String id_c;
    String ad_c;
    String id_v;
    String ts;
    String lifetime;

    public TicketV(String kc_v, String k_v,String id_c, String ad_c, String id_v, String ts, String lifetime) {
        this.kc_v = kc_v;
        this.k_v = k_v;
        this.id_c = id_c;
        this.ad_c = ad_c;
        this.id_v = id_v;
        this.ts = ts;
        this.lifetime = lifetime;
    }

    @Override
    public String toString() {
        try {
            return DES.DES(String.join(",",kc_v, id_c, ad_c, id_v, ts, lifetime),k_v);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getK_v() {
        return k_v;
    }

    public void setK_v(String k_v) {
        this.k_v = k_v;
    }

    public String getKc_v() {
        return kc_v;
    }

    public void setKc_v(String kc_v) {
        this.kc_v = kc_v;
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

    public String getId_v() {
        return id_v;
    }

    public void setId_v(String id_v) {
        this.id_v = id_v;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }
}

package as;

import DES.DES;

public class TicketTGS {
    private String kc_tgs;
    private String idc;
    private String adc;
    private String id_tgs;
    private String timeStamp;
    private String lifeTime;
    private String k_tgs;

    public TicketTGS(String k_tgs,String kc_tgs, String idc, String adc, String id_tgs, String timeStamp, String lifeTime) {
        this.k_tgs=k_tgs;
        this.kc_tgs = kc_tgs;
        this.idc = idc;
        this.adc = adc;
        this.id_tgs = id_tgs;
        this.timeStamp = timeStamp;
        this.lifeTime = lifeTime;
    }

    public String getK_tgs() {
        return k_tgs;
    }

    public void setK_tgs(String k_tgs) {
        this.k_tgs = k_tgs;
    }

    public String getKc_tgs() {
        return kc_tgs;
    }

    public void setKc_tgs(String kc_tgs) {
        this.kc_tgs = kc_tgs;
    }

    public String getIdc() {
        return idc;
    }

    public void setIdc(String idc) {
        this.idc = idc;
    }

    public String getAdc() {
        return adc;
    }

    public void setAdc(String adc) {
        this.adc = adc;
    }

    public String getId_tgs() {
        return id_tgs;
    }

    public void setId_tgs(String id_tgs) {
        this.id_tgs = id_tgs;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(String lifeTime) {
        this.lifeTime = lifeTime;
    }

    @Override
    public String toString() {

        try {
			return DES.DES(String.join(",",kc_tgs,idc,adc,id_tgs,timeStamp,lifeTime),k_tgs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

}

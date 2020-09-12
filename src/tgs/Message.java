package tgs;

import DES.DES;
import bin.Authenticator;
import bin.TicketTGS;
import bin.TicketV;

public class Message {
    static String TYPE7 = "7";
    static String TYPE8 = "8";
    static String TYPE9 = "9";
    //client向TGS通信
    public static String Message3(String id_v, TicketTGS ticketTGS, Authenticator authenticatorTGS){
        return String.join(",",TYPE7,id_v,ticketTGS.toString(),authenticatorTGS.toString());
    }

    //TGS向client通信
    public static String Message4(String id_v, TicketV ticketV, String KEYc_tgs, String k_cv) throws Exception {
        String Message = String.join(",",k_cv, id_v, ticketV.getTs(),ticketV.toString());
        return TYPE8+","+ DES.DES(Message,KEYc_tgs);
    }

}

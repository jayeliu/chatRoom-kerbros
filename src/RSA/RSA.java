package RSA;

import java.math.BigInteger;
import java.util.Scanner;

public class RSA {
    int n,e;
    private int q;
    private int p;
    private int d;

    public RSA(){
        p=Find_Prime();
        while (true){
            q = Find_Prime();
            if(q!=p) break;
        }
        int N=(p - 1) * (q - 1);
        n = p * q;
        e = Make_e(N);
        d = Make_d(N, e);
    }
    public boolean Is_Prime(int n){
        int k = (int) Math.sqrt((double)n);
        for (int i = 2; i <= k; i++) {
            if ((n % i) == 0)
                return false;
        }
        return true;
    }
    public int Find_Prime(){
        int k;
        while (true){
            k = (int) (Math.random() * 100);
            if (k>1&&Is_Prime(k)) return k;
        }
    }
    public int Make_d(int n,int e){
        int k=1;
        while (true){
            if((k*n+1)%e==0)
                return (k * n + 1) / e;
            k++;
        }
    }

    public int Make_e(int N){
        int e;
        while (true){
            e = (int) (Math.random() * N)-1;
            if (e>1&&gcd(N,e)==1) return e;

        }
    }

    public int gcd(int a, int b) {
        int gcd;
        if (b == 0)
            gcd = a;
        else
            gcd = gcd(b, a % b);
        return gcd;
    }
    public int getQ() {
        return q;
    }
    public int getP() {
        return p;
    }
    public int getD() {
        return d;
    }
    static int Encrypt_Decryption(int m,int e_d,int n){
        BigInteger M = new BigInteger(String.valueOf(m));
        BigInteger N = new BigInteger(String.valueOf(n));
        BigInteger E_D = new BigInteger(String.valueOf(e_d));
        return M.modPow(E_D, N).intValue();

    }
    public static int hashCode(int i) {
        return i % 29;
    }
    public static void main(String arg[]){
        String a=Encrypt("qwe&202cb962ac59075b964b07152d234b7070,-1,1599997106604");
        System.out.println(Decryp(a));
/*
        Scanner s = new Scanner(System.in);
        System.out.println("Input message：");
        String Message = "";
        Message = s.nextLine();
        char[] strToChar = Message.toCharArray();

        char[] Code = new char[strToChar.length];
        char[] get_messgae=new char[strToChar.length];
        RSA A=new RSA();
        RSA B=new RSA();
        System.out.println("A_n: "+A.n+" A_e: "+A.e+" A_D: "+A.getD());
        System.out.println("B_n: "+B.n+" B_e: "+B.e+" B_D: "+B.getD());
        //A send message to B
        //Encrypt
        System.out.println("Encrypt");
        for (int i=0;i<strToChar.length;i++) {
            Code[i] = (char)Encrypt_Decryption(strToChar[i], B.e, B.n);
            System.out.print(Code[i]);
        }
        //Decryption
        System.out.println("\nDecryption");
        for (int i=0;i<Code.length;i++) {
            get_messgae[i] = (char)(Encrypt_Decryption(Code[i], B.d, B.n));
            System.out.print(get_messgae[i]);
        }

        //A sign
        //Sign
        char[] Sign = new char[strToChar.length];
        System.out.println("\n\nsign:");
        for (int i=0;i<strToChar.length;i++) {
            Sign[i] = (char)Encrypt_Decryption(hashCode(strToChar[i]), A.getD(), A.n);
            System.out.print(Sign[i]);
        }
        //B verify
        char[] verify = new char[strToChar.length];
        System.out.println("\nVerify:");
        for (int i=0;i<strToChar.length;i++) {
            verify[i] = (char)Encrypt_Decryption(Sign[i], A.e, A.n);
            if(verify[i]!=hashCode(get_messgae[i])){
                System.out.println("Verify fail!");
                break;
            }
            else if(i==strToChar.length-1)System.out.println("Verify A sent the message!");
        }
     */

    }
    public static String Encrypt(String Message){
        char[] strToChar = Message.toCharArray();
        String result="";
        char[] Code = new char[strToChar.length];
        char[] get_messgae=new char[strToChar.length];
        //RSA A=new RSA();
        //RSA B=new RSA();
        //System.out.println("A_n: "+A.n+" A_e: "+A.e+" A_D: "+A.getD());
        //System.out.println("B_n: "+B.n+" B_e: "+B.e+" B_D: "+B.getD());
        //A send message to B
        //Encrypt
        System.out.println("Encrypt");
        for (int i=0;i<strToChar.length;i++) {
            Code[i] = (char)Encrypt_Decryption(strToChar[i], 3235, 3569);
            result += Code[i];
           // System.out.print(Code[i]);
        }
        //System.out.println();
        return result;
    }
    public static String Decryp( String Message){
        char[] strToChar = Message.toCharArray();
        String result="";
        char[] Code = new char[strToChar.length];
        char[] get_messgae=new char[strToChar.length];
        for (int i=0;i<Code.length;i++) {
            get_messgae[i] = (char)(Encrypt_Decryption(strToChar[i], 379, 3569));
            result += get_messgae[i];
            //System.out.print(get_messgae[i]);
        }
        return result;
    }

}

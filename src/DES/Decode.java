package DES;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class Decode {
	
	static final int []s1={14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7,
			                0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8,
			                4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0,
			               15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13};
	static final int []s2={15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10,
							3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5,
							0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15,
						   13,8,10,1,3,15,4,2,11,6,7,12,0,5,14,9};
	static final int []s3={10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8,
						   13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1,
						   13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7,
						    1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12};
	static final int []s4={ 7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15,
						   13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9,
						   10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4,
						   3,15,0,6,10,1,13,8,9,4,5,11,12,7,2,14};
	static final int []s5={ 2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9,
						   14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6,
						    4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14,
						   11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3};
	static final int []s6={12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11,
			          	   10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8,
			          	    9,14,15,5,2,8,12,3,7,0,4,10,1,13,11,6,
			          	    4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13};
	static final int []s7={ 4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1,
						   13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6,
						    1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2,
						   6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12};
	static final int []s8={13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7,
							1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2,
							7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8,
						    2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11};
	static final int []P={58,50,42,34,26,18,10,2,
						   60,52,44,36,28,20,12,4,
						   62,54,46,38,30,22,14,6,
						   64,56,48,40,32,24,16,8,
						   57,49,41,33,25,17,9,1,
						   59,51,43,35,27,19,11,3,
						   61,53,45,37,29,21,13,5,
						   63,55,47,39,31,23,15,7};
	static final int []P1={40,8,48,16,56,24,64,32,
						   39,7,47,15,55,23,63,31,
						   38,6,46,14,54,22,62,30,
						   37,5,45,13,53,21,61,29,
						   36,4,44,12,52,20,60,28,
						   35,3,43,11,51,19,59,27,
						   34,2,42,10,50,18,58,26,
						   33,1,41, 9,49,17,57,25};
	static final int []round= {1,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
	static final int []round1= {0,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1};
	static final int []pc1= {57,49,41,33,25,17,9,
							  1,58,50,42,34,26,18,
							 10,2,59,51,43,35,27,
							 19,11,3,60,52,44,36,
							 63,55,47,39,31,23,15,
							  7,62,54,46,38,30,22,
							 14,6,61,53,45,37,29,
							 21,13,5,28,20,12,4};
	static final int []pc2={14,17,11,24,1,5,3,28,
							15,6,21,10,23,19,12,4,
							26,8,16,7,27,20,13,2,
							41,52,31,37,47,55,30,40,
							51,45,33,48,44,49,39,56,
							34,53,46,42,50,36,29,32};
	static final int []p= {16,7,20,21,29,12,28,17,1,15,23,26,5,18,31,10,
			                2,8,24,14,32,27,3,9,19,13,30,6,22,11,4,25};
	public static String BinatyThransfer(String temp) {
		String ans="";
		for(int i=0;i<temp.length();i++) {
			switch(temp.charAt(i)) {
			case '0':ans+="0000";break;
			case '1':ans+="0001";break;
			case '2':ans+="0010";break;
			case '3':ans+="0011";break;
			case '4':ans+="0100";break;
			case '5':ans+="0101";break;
			case '6':ans+="0110";break;
			case '7':ans+="0111";break;
			case '8':ans+="1000";break;
			case '9':ans+="1001";break;
			case 'a':ans+="1010";break;
			case 'b':ans+="1011";break;
			case 'c':ans+="1100";break;
			case 'd':ans+="1101";break;
			case 'e':ans+="1110";break;
			case 'f':ans+="1111";break;
			}
		}
		return ans;
	}
	public static String BinatyThransfer1(int temp) {
		String ans="";
		
			switch(temp) {
			case 0:ans+="0000";break;
			case 1:ans+="0001";break;
			case 2:ans+="0010";break;
			case 3:ans+="0011";break;
			case 4:ans+="0100";break;
			case 5:ans+="0101";break;
			case 6:ans+="0110";break;
			case 7:ans+="0111";break;
			case 8:ans+="1000";break;
			case 9:ans+="1001";break;
			case 10:ans+="1010";break;
			case 11:ans+="1011";break;
			case 12:ans+="1100";break;
			case 13:ans+="1101";break;
			case 14:ans+="1110";break;
			case 15:ans+="1111";break;
			
		}
		return ans;
	}
	public static String erase(String temp) {
		String ans="";
		for(int i=0;i<8;i++) {
			ans+=temp.substring(i*8, i*8+8);
		}
		return ans;
	}
	public static String Ip(String temp) {
		String ans="";
		for(int i=0;i<P.length;i++) {
			ans+=temp.charAt(P[i]-1);
		}
		return ans;} //初始置换用P
	public static String Ip1(String temp) {
		String ans="";
		for(int i=0;i<P1.length;i++) {
			ans+=temp.charAt(P1[i]-1);
		}
		return ans;
		}//逆初始置换用P1
	public static String expand(String right) {
		String ans="";
		for(int i=0;i<right.length()/4;i++) {
			ans+=right.charAt((i*4-1+right.length())%right.length());
			ans+=right.substring(i*4, i*4+4);
			ans+=right.charAt((i*4+4)%right.length());
		}
		return ans;}//扩充置换（right）
	public static String xor(String x1,String x2) {
		if(x1.length()!=x2.length()) {
			return null;
		}
		String ans="";
		for(int i=0;i<x1.length();i++) {
			if(x1.charAt(i)!=x2.charAt(i)) {
				ans+="1";
			}else {
				ans+="0";
			}
		}
		return ans;}//异或操作
	static byte[] string2bytes(String input) { 
		StringBuilder in = new StringBuilder(input); 
		int remainder = in.length() % 8; 
		if (remainder > 0)
			for (int i = 0; i < 8 - remainder; i++)
				in.append("0"); 
		byte[] bts = new byte[in.length() / 8];

		// Step 8 Apply compression
		for (int i = 0; i < bts.length; i++)
			bts[i] = (byte) Integer.parseInt(in.substring(i * 8, i * 8 + 8), 2);

		return bts; 
	}
	public static String choose(String temp) {
		String ans="";
		for(int i=0;i<temp.length()/6;i++) {
			String t=temp.substring(i*6, i*6+6);
			int row=(t.charAt(0)-'0')*2+t.charAt(5)-'0';
			int col=(t.charAt(1)-'0')*8+(t.charAt(2)-'0')*4+(t.charAt(3)-'0')*2+(t.charAt(4)-'0');
			int location=row*16+col;
			switch(i) {
			case 0:ans+=BinatyThransfer1(s1[location]);break;
			case 1:ans+=BinatyThransfer1(s2[location]);break;
			case 2:ans+=BinatyThransfer1(s3[location]);break;
			case 3:ans+=BinatyThransfer1(s4[location]);break;
			case 4:ans+=BinatyThransfer1(s5[location]);break;
			case 5:ans+=BinatyThransfer1(s6[location]);break;
			case 6:ans+=BinatyThransfer1(s7[location]);break;
			case 7:ans+=BinatyThransfer1(s8[location]);break;
			}
			
		}
		return ans;}//S盒选择（48位进 32位出）
	public static String p(String temp) {
		String ans="";
		for(int i=0;i<temp.length();i++) {
			ans+=temp.charAt(p[i]-1);
		}
		return ans;}//S盒选择后置换 用p
	public static String rotate(String temp,int i) {
		int count=round[i];
		String ans="";
		for(int j=0;j<temp.length();j++) {
			ans+=temp.charAt((j+count)%temp.length());
		}
		return ans;}//密钥轮转 用round
	public static String rotate1(String temp,int i) {
		int count=round1[i];
		String ans="";
		for(int j=0;j<temp.length();j++) {
			ans+=temp.charAt((j-count+temp.length())%temp.length());
		}
		return ans;}//密钥轮转 用round
	public static String ip1(String temp) {
		String ans="";
		for(int i=0;i<pc1.length;i++) {
			ans+=temp.charAt(pc1[i]-1);
		}
		return ans;}//用于密钥初始置换
	public static String ip2(String temp) {
		String ans="";
		for(int i=0;i<pc2.length;i++) {
			ans+=temp.charAt(pc2[i]-1);
		}
		return ans;}//用于密钥轮转后置换
	public static String decode(String message,String key) throws NoSuchAlgorithmException {
		byte[] b=Base64.getDecoder().decode(message);
		String answer="";
		for(byte a:b) {
			answer+= Integer.toBinaryString((a & 0xFF) + 0x100).substring(1);
			
		}
		String secret=MD5.MD5.gethash(key).substring(0,16);
		int len=answer.length();
		String ans="";
		int count=0;
		while(len!=0) {
			String partition="";
			if(len<=64) {
				partition=answer.substring(count*64,count*64+len);
				for(int i=len;i<64;i++) {
					partition+="0";
				}
				len=0;
			}
			else {
				partition=answer.substring(count*64,count*64+64);
				len-=64;
			}
		String source=partition;
		source=Ip(source);
		String left=source.substring(0, source.length()/2);
		String right=source.substring(source.length()/2, source.length());
		//String secret="133457799bbcdff1";
		
		secret=BinatyThransfer(secret);
		secret=erase(secret);
		secret=ip1(secret);
		String c=secret.substring(0, secret.length()/2);
		String d=secret.substring(secret.length()/2, secret.length());
		for(int i=0;i<16;i++) {
			c=rotate(c,i);
			d=rotate(d,i);
		}
		for(int i=0;i<16;i++) {
			//System.out.println("L"+i+":"+left);
			//System.out.println("R"+i+":"+right);
			String temp=right;
			right=expand(right);
			c=rotate1(c,i);
			d=rotate1(d,i);
			String k=ip2(c+d);
			//System.out.println("K"+i+":"+k);
			right=xor(right,k);
			right=choose(right);
			right=p(right);
			right=xor(left,right);
			
			left=temp;
		}
		String C=Ip1(right+left);
		ans+=C;
		count++;
		}
		String num=ans.substring(ans.length()-64, ans.length());
		int n=Integer.parseInt(num, 2);
		String end=ans.substring(0,ans.length()-64-n);
		byte[] temp=string2bytes(end);
		String s=new String(temp);
		//System.out.println("secret:"+message);
		//System.out.println("source:"+s);
		return s;
	}
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		/*Scanner scan=new Scanner(System.in);
		//String sourceall="/home/zhou/test/secret1";
		System.out.println("please input secret file path:");
		String sourceall=scan.nextLine();
		BufferedInputStream bis = null;
		 
		bis = new BufferedInputStream(new FileInputStream(sourceall));
		 
		int length= bis.available();
		byte []b = new byte[length];
		 
		bis.read(b, 0, length);
		String answer="";
		for(byte a:b) {
			answer+= Integer.toBinaryString((a & 0xFF) + 0x100).substring(1);
			
		}
		bis.close();
		System.out.println("please input secret key");
		String secret=scan.nextLine();
		//String secret="133457799bbcdff1";
		scan.close();
		long a=System.currentTimeMillis();
		int len=answer.length();
		String ans="";
		int count=0;
		while(len!=0) {
			String partition="";
			if(len<=64) {
				partition=answer.substring(count*64,count*64+len);
				for(int i=len;i<64;i++) {
					partition+="0";
				}
				len=0;
			}
			else {
				partition=answer.substring(count*64,count*64+64);
				len-=64;
			}
		String source=partition;
		source=Ip(source);
		String left=source.substring(0, source.length()/2);
		String right=source.substring(source.length()/2, source.length());
		//String secret="133457799bbcdff1";
		
		secret=BinatyThransfer(secret);
		secret=erase(secret);
		secret=ip1(secret);
		String c=secret.substring(0, secret.length()/2);
		String d=secret.substring(secret.length()/2, secret.length());
		for(int i=0;i<16;i++) {
			c=rotate(c,i);
			d=rotate(d,i);
		}
		for(int i=0;i<16;i++) {
			//System.out.println("L"+i+":"+left);
			//System.out.println("R"+i+":"+right);
			String temp=right;
			right=expand(right);
			c=rotate1(c,i);
			d=rotate1(d,i);
			String k=ip2(c+d);
			//System.out.println("K"+i+":"+k);
			right=xor(right,k);
			right=choose(right);
			right=p(right);
			right=xor(left,right);
			
			left=temp;
		}
		String C=Ip1(right+left);
		ans+=C;
		count++;
		}
		String num=ans.substring(ans.length()-64, ans.length());
		int n=Integer.parseInt(num, 2);
		String end=ans.substring(0,ans.length()-64-n);
		byte[] temp=string2bytes(end);
		FileOutputStream fos = new FileOutputStream(new File("/home/zhou/test/source"));
		   fos.write(temp,0,temp.length );
		   fos.flush();
		   fos.close();
		System.out.println("time used of decryption"+(System.currentTimeMillis()-a)+"ms");
		//System.out.println(""+ans);*/
		decode(DES.des("中国人民共和国你好hello", "hellotgs"),"hellotgs");
	}

}

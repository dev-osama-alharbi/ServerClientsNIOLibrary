package sa.osama_alharbi.serverClients.io;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ByteGenerator {
	
	public static byte[] generat(String data){
		
		return data.getBytes();
	}
	
	public static String toString(byte[] data){
		try {
			return new String(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(data);
	}
	
	public static byte[] generat(String[] data){
		 ArrayList<Byte> byteList = new ArrayList<Byte>();
	        for (String str: data) {
	            int len = str.getBytes().length;
	            ByteBuffer bb = ByteBuffer.allocate(4);
	            bb.putInt(len);
	            byte[] lenArray = bb.array();
	            for (byte b: lenArray) {
	                byteList.add(b);
	            }
	            byte[] strArray = str.getBytes();
	            for (byte b: strArray) {
	                byteList.add(b);
	            }
	        }
	        byte[] result = new byte[byteList.size()];
	        for (int i=0; i<byteList.size(); i++) {
	            result[i] = byteList.get(i);
	        }
	        return result;
	}
	
	public static String[] toStringArray(byte[] data){
		ArrayList<String> strList = new ArrayList<String>();
        for (int i=0; i< data.length;) {
            byte[] lenArray = new byte[4];
            for (int j=i; j<i+4; j++) {
                lenArray[j-i] = data[j];
            }
            ByteBuffer wrapped = ByteBuffer.wrap(lenArray);
            int len = wrapped.getInt();
            byte[] strArray = new byte[len];
            for (int k=i+4; k<i+4+len; k++) {
                strArray[k-i-4] = data[k];
            }
            strList.add(new String(strArray));
            i += 4+len;
        }
        return strList.toArray(new String[strList.size()]);
	}
	
	public static byte[] generat(char data){
		return String.valueOf(data).getBytes();
	}
	
	public static char toChar(byte[] data){
		return new String(data).charAt(0);
	}
	
	public static byte[] generat(char[] data){
		return String.valueOf(data).getBytes();
	}
	
	public static char[] toCharArray(byte[] data){
		return new String(data).toCharArray();
	}
	
	public static byte[] generat(int data){
		ByteBuffer bb = ByteBuffer.allocate(10);
		bb.asIntBuffer().put(data);
		return bb.array();
	}
	
	public static int toInt(byte[] data){
		ByteBuffer bb = ByteBuffer.allocate(10);
		bb.put(data);
		bb.flip();
		return bb.asIntBuffer().get();
	}
	
	public static byte[] generat(double data){
		return String.valueOf(data+"").getBytes();
	}
	
	public static double toDouble(byte[] data){
		return Double.valueOf(new String(data));
	}
	
	public static byte[] generat(long data){
		return String.valueOf(data+"").getBytes();
	}
	
	public static long toLong(byte[] data){
		return Long.valueOf(new String(data));
	}
}

/**
 * 
 */
package jp.neap.hanne;

import java.util.LinkedList;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author uraya
 *
 */
public class CryptUtil {

	private static final String SECRET_KEY = "bCcz7gFc7IHmcNUmhfCXdRndPtAYrMc0";
	
	public static String encrypt(String plainText) {
		try {
			SecretKey deskey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
			byte[] inputInfo = plainText.getBytes("UTF-8");
			if (inputInfo == null) {
				return "";
			}
			Cipher c1 = Cipher.getInstance("AES");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			String result = byteToString(c1.doFinal(inputInfo));
			return result;
		}
		catch (Exception e) {
			return "";
		}
	}

	public static String decrypt(String encryptedText) {
		try {
			SecretKey deskey = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
			byte[] cipherInfo = stringToByte(encryptedText);
			Cipher c1 = Cipher.getInstance("AES");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			String result = new String(c1.doFinal(cipherInfo), "UTF-8");
			return result;
		}
		catch (Exception e) {
			return "";
		}
	}

	private static String byteToString(byte[] b) {
    	final StringBuffer buf = new StringBuffer();
    	for ( int i = 0 ; i < b.length ; i++ ) {
    		final String hexText = Integer.toHexString((int)b[i] & 255);
    		if (hexText.length() > 1)
    			buf.append(hexText);
    		else
    			buf.append("0").append(hexText);
    	}
    	return buf.toString();
    }

    private static byte[] stringToByte(String t) {
    	final LinkedList list = new LinkedList();
    	for ( int i = 0 ; i < t.length() ; i += 2 ) {
    		final StringBuffer buffer = new StringBuffer(5);
    		buffer.append(t.charAt(i)).append(t.charAt(i+1));
    		final byte b = Integer.valueOf(buffer.toString(), 16).byteValue();
    		list.add(new Byte(b));
    	}
    	final byte[] ret = new byte[list.size()];
    	for ( int i = 0 ; i < list.size() ; i++ ) {
    		final Byte b = (Byte)list.get(i);
    		ret[i] = b;
    	}
    	return ret;
    }

    /**
	 * 
	 */
	private CryptUtil() {
		// TODO Auto-generated constructor stub
	}

}

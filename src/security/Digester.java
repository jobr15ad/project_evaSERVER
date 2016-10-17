package security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.security.MessageDigest;


//TODO: Add comments to this class, in order for everyone to understand the code.
public class Digester {

  //TODO: Set SALT and KEY in config-file.
  private final static String SALT = "n0zaCTADRUuTb@JUp01n%5@(l@IAaLlZ";
  private final static String KEY = "40674244454045cb9a70040a30e1c007";
  private static MessageDigest digester;

  static {
    try {
      digester = MessageDigest.getInstance("MD5");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Hash string with MD5 hashing
   * @param str
   * @return MD5 hash of string
   */
  public static String hash(String str) {
    if (str == null || str.length() == 0) {
      throw new IllegalArgumentException("Error");
    }
    return Digester._hash(str);
  }

  public static String hashWithSalt(String str){
    if (str == null || str.length() == 0) {
      throw new IllegalArgumentException("Error");
    }

    str = str + Digester.SALT;

    return Digester._hash(str);
  }

  private static String _hash(String str){
    digester.update(str.getBytes());
    byte[] hash = digester.digest();
    StringBuffer hexString = new StringBuffer();
    for (byte aHash : hash) {
      if ((0xff & aHash) < 0x10) {
        hexString.append("0" + Integer.toHexString((0xFF & aHash)));
      } else {
        hexString.append(Integer.toHexString(0xFF & aHash));
      }
    }
    return hexString.toString();
  }

  public static String encrypt(String s) {
    return base64Encode(xorWithKey(s.getBytes(), KEY.getBytes()));
  }

  public static String decrypt(String s) {
    return new String(xorWithKey(base64Decode(s), KEY.getBytes()));
  }

  private static byte[] xorWithKey(byte[] a, byte[] key) {
    byte[] out = new byte[a.length];
    for (int i = 0; i < a.length; i++) {
      out[i] = (byte) (a[i] ^ key[i%key.length]);
    }
    return out;
  }

  private static byte[] base64Decode(String s) {
    try {
      BASE64Decoder d = new BASE64Decoder();
      return d.decodeBuffer(s);
    } catch (IOException e) {throw new RuntimeException(e);}
  }

  private static String base64Encode(byte[] bytes) {
    BASE64Encoder enc = new BASE64Encoder();
    return enc.encode(bytes).replaceAll("\\s", "");

  }

}
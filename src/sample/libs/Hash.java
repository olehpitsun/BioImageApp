package sample.libs;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

    public static String generateSalt(int len)
    {
        String symbols = "abcdefghijklmno0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
        StringBuilder randString = new StringBuilder();
        int count = len;
        for(int i=0;i<count;i++)
            randString.append(symbols.charAt((int)(Math.random()*symbols.length())));
        return randString.toString();
    }

    public static String hash(String st, String salt) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        if(salt != null)
            st = st.concat(salt);
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        return md5Hex;
    }
}

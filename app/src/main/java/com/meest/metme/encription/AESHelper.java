package com.meest.metme.encription;

import android.annotation.SuppressLint;
import android.content.Context;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESHelper {
    Context context;
    private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";


    public static String encrypt(String strNormalText,String r_P_key){
       // MyStringRandomGen msr = new MyStringRandomGen();
       // String seedValue = msr.generateRandomString();


        String seedValue = r_P_key.substring(0,16);
        String normalTextEnc="";
        String key="";
        try {
            normalTextEnc = AESHelper.encryption(seedValue, strNormalText);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalTextEnc;
    }

    @SuppressLint("NewApi")
    public static String decrypt(String strEncryptedText, String key){
        String seedValue = key.substring(0,16);
        String strDecryptedText="";
        try {
            strDecryptedText = AESHelper.decryption(seedValue, strEncryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDecryptedText;
    }

    public static String encryption(String key, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return toHex(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decryption(String key, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] enc = toByte(encrypted);
            byte[] original = cipher.doFinal(enc);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

    public static String msgDecrypt(String lastMsg, String chatHeadId) {
        String test = lastMsg;
        String msg = AESHelper.decrypt(test, chatHeadId);
        if (msg != null) {
            test = msg;
        }
        return test;
    }
}
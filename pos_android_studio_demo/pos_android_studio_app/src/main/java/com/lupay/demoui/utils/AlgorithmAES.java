package com.lupay.demoui.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AlgorithmAES {
    final static String algorithm = "AES/CBC/PKCS5Padding";
    final static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static String generateKey(int n) {
        String encodedKey=null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(n);
            SecretKey key = keyGenerator.generateKey();
            encodedKey=byteArrayToHexString(key.getEncoded());
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            TRACE.d("key:"+byteArrayToHexString(key.getEncoded()));
//            encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
//        }
        } catch (NoSuchAlgorithmException e) {
            TRACE.d("NoSuchAlgorithmException: " + e);
        }

        return encodedKey;
    }
    public static String generateIv() {
        String encodedIv=null;
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        encodedIv=byteArrayToHexString(new IvParameterSpec(iv).getIV());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            TRACE.d("iv:"+byteArrayToHexString(new IvParameterSpec(iv).getIV()));
//            encodedIv = Base64.getEncoder().encodeToString(new IvParameterSpec(iv).getIV());
//        }
        return encodedIv;
    }
    public static String encrypt(String input, String key, String iv) {
        String result="";
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            byte[] decodedKey = hexStringToByteArray(key);
            byte[] decodediv = hexStringToByteArray(iv);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            decodedKey = Base64.getDecoder().decode(key);
//            decodediv = Base64.getDecoder().decode(iv);
//        }
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"), new IvParameterSpec(decodediv,0,decodediv.length));
            byte[] cipherText = cipher.doFinal(input.getBytes());
            result=byteArrayToHexString(cipherText);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            TRACE.d("cipherText:"+byteArrayToHexString(cipherText));
//            result= Base64.getEncoder().encodeToString(cipherText);
//        }
        } catch (NoSuchPaddingException e) {
            TRACE.d("NoSuchPaddingException: " + e);
        } catch (NoSuchAlgorithmException e) {
            TRACE.d("NoSuchAlgorithmException: " + e);
        } catch (InvalidAlgorithmParameterException e) {
            TRACE.d("InvalidAlgorithmParameterException: " + e);
        } catch (InvalidKeyException e) {
            TRACE.d("InvalidKeyException: " + e);
        } catch (BadPaddingException e) {
            TRACE.d("BadPaddingException: " + e);
        } catch (IllegalBlockSizeException e) {
            TRACE.d("IllegalBlockSizeException: " + e);
        }

        return result;
    }
    public static String decrypt(String cipherText, String key, String iv) {
        byte[] plainText= new byte[0];
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            byte[] decodedKey = hexStringToByteArray(key);
            byte[] decodediv = hexStringToByteArray(iv);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            decodedKey = Base64.getDecoder().decode(key);
//            decodediv = Base64.getDecoder().decode(iv);
//        }

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decodedKey, 0,  decodedKey.length, "AES"), new IvParameterSpec(decodediv,0,decodediv.length));
            plainText = cipher.doFinal(hexStringToByteArray(cipherText));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
//        }
        } catch (NoSuchPaddingException e) {
            TRACE.d("NoSuchPaddingException: " + e);
        } catch (NoSuchAlgorithmException e) {
            TRACE.d("NoSuchAlgorithmException: " + e);
        } catch (InvalidAlgorithmParameterException e) {
            TRACE.d("InvalidAlgorithmParameterException: " + e);
        } catch (InvalidKeyException e) {
            TRACE.d("InvalidKeyException: " + e);
        } catch (BadPaddingException e) {
            TRACE.d("BadPaddingException: " + e);
        } catch (IllegalBlockSizeException e) {
            TRACE.d("IllegalBlockSizeException: " + e);
        }
        return new String(plainText);
    }
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }
    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }
}

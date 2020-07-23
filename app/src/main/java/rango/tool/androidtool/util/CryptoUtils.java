package rango.tool.androidtool.util;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {
    private static final String TAG = "DA_CryptoUtils";

    private static final String TRANSFORMATION = "AES";

    private static String SECRET_KEY = "e81wolan12dfw31f";

    public static String encrypt(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), TRANSFORMATION);
            @SuppressLint("GetInstance")
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] bytesToDecrypt = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(bytesToDecrypt, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decrypt(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), TRANSFORMATION);
            @SuppressLint("GetInstance")
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            byte[] encryptedContent = Base64.decode(data.getBytes(), Base64.DEFAULT);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(encryptedContent);
            return new String(original, Charset.forName("UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }
}

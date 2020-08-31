package rango.tool.androidtool.http.lib.signature;

import androidx.annotation.NonNull;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA256 {

    private static final String HMAC_SHA_256 = "HmacSHA256";
    private static final String UTF8 = "utf-8";

    private static final String secret = "45fdc8c7fbd249a0d040991921d2bd9815b3297e";

    /**
     * sha256_HMAC加密
     *
     * @param message 消息
     * @return 加密后字符串
     */
    public static String sha256_HMAC(@NonNull byte[] message) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(UTF8), HMAC_SHA_256);
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message);

            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {

        }
        return hash;
    }

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }
}

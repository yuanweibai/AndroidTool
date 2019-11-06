package rango.tool.androidtool.encrypt;

import java.io.InputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;

public class EnCryuptUtils {

    public static InputStream genDecodeStream(InputStream inputStream) {
        if (null == inputStream) {
            return null;
        }

        final String debugStr = "Iu[Ki}96TZp]pri/";
        StringBuilder aesKeyStringBuffer = new StringBuilder();
        aesKeyStringBuffer.append(debugStr.subSequence(4, 8));
        aesKeyStringBuffer.append(debugStr.subSequence(0, 4));
        aesKeyStringBuffer.append(debugStr.subSequence(12, 16));
        aesKeyStringBuffer.append(debugStr.subSequence(8, 12));

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            Key key = new SecretKeySpec(aesKeyStringBuffer.toString().getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            return new CipherInputStream(inputStream, cipher);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

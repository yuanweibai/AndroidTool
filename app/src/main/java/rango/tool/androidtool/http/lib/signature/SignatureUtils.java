package rango.tool.androidtool.http.lib.signature;

public class SignatureUtils {

    public static String generateSignature(String timeStamp, byte[] bytes) {
        String digest = generateDigest(timeStamp, bytes);
        return join(timeStamp, digest);
    }

    private static String join(String timeStamp, String digest) {
        return timeStamp +
                "," +
                digest;
    }

    private static String generateDigest(String timeStamp, byte[] bytes) {
        byte[] timeBytes = timeStamp.getBytes();
        int length = bytes == null ? 0 : bytes.length;
        byte[] byteArray = new byte[timeBytes.length + length];

        int index = 0;
        for (int i = 0; i < timeBytes.length; i++) {
            index = i;
            byteArray[index] = timeBytes[i];
        }

        for (int i = 0; i < length; i++) {
            index++;
            byteArray[index] = bytes[i];
        }
        return HMACSHA256.sha256_HMAC(byteArray);
    }
}

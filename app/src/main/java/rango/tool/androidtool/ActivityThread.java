package rango.tool.androidtool;


import rango.tool.androidtool.util.CryptoUtils;

public class ActivityThread {


    private static int mFlags;

    public static void main(String[] args) throws Exception {

        String string = "spyIFKsRDDywx96YXDjAyiuo0imMzoGBOIhavrlmvma5A0gXJTzklg/jrGbVUd11VqigDk7zJCQt\n" +
                "p7wEnVs3pnn5rU7WsT+F+yTJhe9zcLM7l84rL+/TO4qVN+hBS6mson6KhsiZXV/b06jpcbKanAxe\n" +
                "FZbO9qUldlJUUC/aG2dA5IMKwvuBfyZA8kimNmpvaLOxpWEd0iAxbslecjKiTV86SSn0aBwN4T/A\n" +
                "Qm6uykGdLKG/2B4sVRq7fFzUSsROKIZUMr9BwwLwOUSiA7yQubA5o/EELjHeoy6YIg8S+e5ifjSi\n" +
                "y42AUATZKpyz9v9KFf4ZR2hHxGHUp5b75FLtAii7F8CIq5Bd384VEXyGSlrRaHNeCrbtlWzBozHq\n" +
                "QAugtaJnOtijIpUJFU4wEiy3rVJpXUAZd58Va7MzcJzsOyRn1lAK7PQvKShFbI91WkQ8NYglST3+\n" +
                "OoPcMfEbXRnKh8Jbnf39Fr9+MqtFUbFp4OwPLmiHEapPXt+esv7tHUup\n";

        String value = CryptoUtils.decrypt(string);
        System.out.println("value = " +value);


    }

    static void setFlags(int flags, int mask) {
        mFlags = (mFlags & ~mask) | (flags & mask);
    }
}

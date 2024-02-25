package com.example.mad.systeminfos;
import android.os.Build;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptingPwd {

    private static final String AES_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "123ArunaShantha@"; // Change this to your own key

    public String encrypt(String input) {
        String hashKey = "NA";
        try {
            Key key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(input.getBytes());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hashKey=  Base64.getEncoder().encodeToString(encryptedBytes);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return  hashKey;
    }
    public  String decrypt(String input) {
        try {
            Key key = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
            }
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

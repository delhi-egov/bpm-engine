package in.gov.bpm.db

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Created by vaibhav on 22/7/16.
 */

public class EncryptionSbiEPay {
    private static byte[] key = [
        0x2d, 0x2a, 0x2d, 0x42, 0x55, 0x49, 0x4c, 0x44, 0x41, 0x43, 0x4f, 0x44, 0x45, 0x2d, 0x2a, 0x2d
    ];

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF8"));
            String encryptedString = new String(Base64.getEncoder().encode(cipherText),"UTF-8");
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] cipherText = Base64.getDecoder().decode(encryptedText.getBytes("UTF8"));
            String decryptedString = new String(cipher.doFinal(cipherText),"UTF-8");
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String keyText = "wImKoNvsqbSswM/bO0FT4A==";
        key = keyText.getBytes("UTF-8");

        String requestParams = "1000003|DOM|IN|INR|2|Other|https://test.sbiepay.com/secure/sucess.jsp|https://test.sbiepay.com/secure/fail.jsp|SBIEPAY|2|2|NB|ONLINE|ONLINE";
        String requestParams2 = "1000003|DOM|IN|INR|2|Other|https://localhost:8888/secure/sucess.jsp|https://localhost:8888/secure/fail.jsp|SBIEPAY|2|2|NB|ONLINE|ONLINE";
        String billingDetails = "BillerName|Mumbai|Maharastra|403706|India|+91|222|1234567|9892456281|biller@gmail.com|N";
        String shippingDetails = "ShipperName|Mayuresh Enclave, Sector 20, Plat A-211, Nerul(w),NaviMumbai,403706|Mumbai|Maharastra|India|403706|+91|222|30988373|9812345678|N";
        String paymentDetails = "aggGtwmapID| | | | | | |";

        System.out.println(encrypt(requestParams2));
        System.out.println(encrypt(billingDetails));
        System.out.println(encrypt(shippingDetails));
        System.out.println(encrypt(paymentDetails));

    }
    
}
